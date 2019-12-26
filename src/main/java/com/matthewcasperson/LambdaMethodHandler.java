package com.matthewcasperson;

import com.amazonaws.services.lambda.runtime.Context;
import com.auth0.jwk.Jwk;
import com.auth0.jwk.JwkException;
import com.auth0.jwk.JwkProvider;
import com.auth0.jwk.JwkProviderBuilder;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.gson.Gson;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.security.interfaces.RSAPublicKey;
import java.util.Collections;
import java.util.Map;

public class LambdaMethodHandler {
    public ProxyResponse handleRequest(final Map<String,Object> input, final Context context) {
        validateAuthHeader(input);
        return new ProxyResponse("200", new Gson().toJson(input));
    }

    private void validateAuthHeader(final Map<String,Object> input) {
        final String authorization = extractHeader("Authorization", input);
        final String referer = extractHeader("referer", input);
        final String poolId = extractQueryParam(referer, "poolId");

        try {
            final DecodedJWT decodedJWT = validateToken(authorization, poolId);
            System.out.println("Username is: " + decodedJWT.getClaim("cognito:username"));
            System.out.println("Email is: " + decodedJWT.getClaim("email"));
        } catch (final JwkException exception) {
            System.out.println("Authorization header was missing, invalid, or could not be verified");
        }
    }

    private String extractQueryParam(final String referer, final String queryParam) {
        try {
            return URLEncodedUtils.parse(new URI(referer), StandardCharsets.UTF_8)
                    .stream()
                    .filter(p -> queryParam.equals(p.getName()))
                    .map(NameValuePair::getValue)
                    .findFirst()
                    .orElse("");
        } catch (URISyntaxException e) {
            return "";
        }
    }

    private String extractHeader(final String header, final Map<String,Object> input) {
        return ((Map)input.getOrDefault("headers", Collections.emptyMap()))
                .getOrDefault(header, "")
                .toString();
    }

    private DecodedJWT validateToken(final String jwt, final String userPoolId) throws JwkException {
        final DecodedJWT decodedJwt = JWT.decode(jwt);
        final JwkProvider jwkProvider = new JwkProviderBuilder(
                "https://cognito-idp.$regionName.amazonaws.com/" + userPoolId + "/.well-known/jwks.json").build();
        final Jwk jwk = jwkProvider.get(decodedJwt.getKeyId());
        final Algorithm algorithm = Algorithm.RSA256((RSAPublicKey) jwk.getPublicKey(), null);
        algorithm.verify(decodedJwt);
        return decodedJwt;
    }
}
