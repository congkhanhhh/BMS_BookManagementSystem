package com.bookstore.project.service.impl;

import com.bookstore.project.config.AppConfig;
import com.bookstore.project.exception.RestException;
import com.bookstore.project.responses.UserContext;
import com.bookstore.project.service.TokenService;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;


import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.*;

@Service
public class TokenServiceImpl implements TokenService {

    private static final String SECRET = "WkhWamFIVjVkR0U";

    @Autowired
    private AppConfig appConfig;

    private static final String CLAIM_AUTHORITIES = "authorities";

    protected String[] getDefaultClaims() {
        return new String[]{"sub", "nbf", "iss", "exp", "iat", "jti"};
    }

    @Override
    public String generateToken(UserContext userContext) {
        try {
            RSAKey rsaKey = RSAKey.parse(appConfig.getPrivateKey());

            // Generate JWT
            JWSSigner signer = new RSASSASigner(rsaKey);
            Date now = new Date();

            // Prepare JWT with claims set
            // @formatter:off
            JWTClaimsSet.Builder claimsSetBuilder = new JWTClaimsSet.Builder()
                    .jwtID(UUID.randomUUID().toString())
                    .subject(userContext.getUsername())
                    .expirationTime(new Date(now.getTime() + appConfig.getExpirationTime() * 1000));
            // @formatter:on

            for (String key : userContext.getData().keySet()) {
                claimsSetBuilder.claim(key, userContext.getData().get(key));
            }

            List<String> authorities = new ArrayList<>();
            for (GrantedAuthority authority : userContext.getAuthorities()) {
                authorities.add(authority.getAuthority().replace("ROLE_", ""));
            }
            claimsSetBuilder.claim(CLAIM_AUTHORITIES, authorities);

            SignedJWT signedJWT = new SignedJWT(
                    new JWSHeader.Builder(JWSAlgorithm.RS256).keyID(rsaKey.getKeyID()).build(),
                    claimsSetBuilder.build());

            // Compute the RSA signature
            signedJWT.sign(signer);
            return signedJWT.serialize();

        } catch (ParseException | JOSEException e) {
            throw RestException.badRequest(e.toString());
        } catch (Exception e) {
            throw RestException.internalServerError(e.toString());
        }
    }

    @Override
    public UserContext parseToken(String token) {
        try {
            RSAKey rsaKey = RSAKey.parse(appConfig.getPublicKey());
            SignedJWT signedJWT = SignedJWT.parse(token);

            JWSVerifier verifier = new RSASSAVerifier(rsaKey.toPublicJWK());
            if (signedJWT.verify(verifier)) {
                JWTClaimsSet claimsSet = signedJWT.getJWTClaimsSet();

                if (StringUtils.isEmpty(claimsSet.getSubject())) {
                    throw RestException.unauthorized("Claims is invalid");
                }

                if (!(claimsSet.getClaim(CLAIM_AUTHORITIES) instanceof List<?>)) {
                    throw RestException.forbidden("Claims is invalid");
                }

                if (claimsSet.getExpirationTime().before(new Date())) {
                    throw RestException.unauthorized("Token is expired");
                }

                @SuppressWarnings("unchecked")
                String[] roles = ((List<String>) claimsSet.getClaim(CLAIM_AUTHORITIES)).toArray(String[]::new);

                UserContext userContext = new UserContext(claimsSet.getSubject(), roles);

                Map<String, Object> data = userContext.getData();
                for (String key : claimsSet.getClaims().keySet()) {
                    if (key.equals(CLAIM_AUTHORITIES)) {
                        continue;
                    }

                    boolean flag = false;
                    for (String claim : getDefaultClaims()) {
                        if (key.equals(claim)) {
                            flag = true;
                            break;
                        }
                    }

                    if (!flag) {
                        data.put(key, claimsSet.getClaims().get(key));
                    }
                }

                return userContext;
            } else {
                throw RestException.forbidden();
            }

        } catch (ParseException | JOSEException e) {
            throw RestException.badRequest(e.toString());
        } catch (Exception e) {
            throw RestException.internalServerError(e.toString());
        }
    }

    @Override
    public String generateDownloadToken(Long id, String type) {

        String str = id + "|" + type + "|" + System.currentTimeMillis() / 1000 + 60 * 5; // 5 minutes
        String data = Hex.encodeHexString(str.getBytes(StandardCharsets.UTF_8));
        String signature = DigestUtils.md5Hex(str + ":" + SECRET);

        return data + signature;
    }

    @Override
    public boolean verifyDownloadToken(Long id, String type, String token) {
        try {
            if (token.length() < 32) {
                return false;
            }

            String signature = token.substring(token.length() - 32);
            String data = new String(Hex.decodeHex(token.substring(0, token.length() - 32)));

            if (!signature.equals(DigestUtils.md5Hex(data + ":" + SECRET))) {
                return false;
            }

            String[] str = data.split("\\|");
            if (str.length != 3) {
                return false;
            }

            long targetId = Long.parseLong(str[0]);
            long expiration = Long.parseLong(str[2]);

            if (targetId != id) {
                return false;
            }

            if (!type.equals(str[1])) {
                return false;
            }

            return System.currentTimeMillis() / 1000 <= expiration;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
