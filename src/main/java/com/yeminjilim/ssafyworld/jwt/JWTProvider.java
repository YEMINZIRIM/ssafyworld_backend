package com.yeminjilim.ssafyworld.jwt;

import com.nimbusds.oauth2.sdk.util.StringUtils;
import com.yeminjilim.ssafyworld.jwt.error.CustomJWTException;
import com.yeminjilim.ssafyworld.jwt.error.JWTErrorCode;
import com.yeminjilim.ssafyworld.member.entity.Member;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Component
public class JWTProvider {

    private class JWT {
        private static final String BEARER = "Bearer";
        private final Claims claims;

        public JWT(String token) {

            if(StringUtils.isBlank(token)) {
                throw new CustomJWTException(JWTErrorCode.INVALID_JWT);
            }

            if (token.startsWith(BEARER)) {
                token = token.substring(7);
            }

            this.claims = getClaims(token);
        }

        //jwt body를 가져온다.
        private Claims getClaims(String token) {
            try {
                return Jwts.parserBuilder().setSigningKey(secret).build()
                        .parseClaimsJws(token).getBody();
            } catch (SignatureException e) {
                throw new CustomJWTException(JWTErrorCode.INVALID_SIGNATURE);
            } catch (ExpiredJwtException e) {
                throw new CustomJWTException(JWTErrorCode.EXPIRED_JWT);
            } catch (JwtException e){
                throw new CustomJWTException(JWTErrorCode.INVALID_JWT);
            }
        }

        public Object get(String key) {
            return claims.get(key);
        }

        public <T> T get(String key, Class<T> clazz) {
            return claims.get(key, clazz);
        }
    }


    private String secret;

    public JWTProvider(@Value("${jwt.secret}") String secret) {
        this.secret = secret;
    }

    public Authentication getToken(ServerHttpRequest request) {
        String token = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        JWT jwt = new JWT(token);

        String userId = jwt.get("sub",String.class);

        if(userId == null){
            //TODO : JWT의 body에 필수 필드가 없는 경우
            throw new CustomJWTException(JWTErrorCode.INVALID_JWT);
        }

        String roles = jwt.get("roles", String.class);

        Collection<? extends GrantedAuthority> authorities = roles == null ? Collections.emptyList() : Arrays
                .stream(roles.split(","))
                .map(String::trim)
                .map(SimpleGrantedAuthority::new)
                .toList();

        UserDetails userDetails = User.builder()
                .username(userId)
                .password(UUID.randomUUID().toString())
                .authorities(authorities)
                .build();

        return new UsernamePasswordAuthenticationToken(userDetails, "", authorities);
    }

    public Map<String, Object> getClaims(String token) {
        JWT jwt = new JWT(token);
        return jwt.getClaims(token);
    }

    public String makeToken(Member member) {

        Instant instant = Instant.now().truncatedTo(ChronoUnit.SECONDS);
        Instant expiredDate = instant.plus(30, ChronoUnit.MINUTES);

        HashMap<String,Object> claims = new HashMap<>();
        claims.put("name", member.getMemberInfo().getName());

        return Jwts.builder()
                .setHeaderParam("type", "accessToken")
                .setHeaderParam("alg", "HS256")
                .setSubject(member.getMemberInfo().getMemberId().toString())
                .addClaims(claims)
                .setIssuedAt(Date.from(instant))
                .setExpiration(Date.from(expiredDate))
                .claim("roles","ROLE_USER")
                .signWith(SignatureAlgorithm.HS256, secret) // TODO : 수정을 해야될것 같다.
                .compact();
    }


}
