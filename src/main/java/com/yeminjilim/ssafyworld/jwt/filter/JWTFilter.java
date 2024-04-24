package com.yeminjilim.ssafyworld.jwt.filter;

import com.yeminjilim.ssafyworld.jwt.JWTProvider;
import com.yeminjilim.ssafyworld.jwt.error.CustomAccessDeniedException;
import com.yeminjilim.ssafyworld.jwt.error.CustomJWTException;
import com.yeminjilim.ssafyworld.jwt.error.JWTErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.RequestPath;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class JWTFilter implements WebFilter {

    private final ServerAuthenticationEntryPoint authenticationEntryPoint;
    private final JWTProvider jwtProvider;

    private static final List<String> exceptPath = List.of("/","/login","/register");

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        try {
            ServerHttpRequest request = exchange.getRequest();
            String path = request.getPath().toString();

            Authentication authentication =
                    new AnonymousAuthenticationToken("key","null",List.of(new SimpleGrantedAuthority("ROLE_ANONYMOUS")));

            if(!exceptPath.contains(path)) {
                authentication = jwtProvider.getToken(request);
            }

            return chain.filter(exchange)
                    .contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication));
        } catch (CustomJWTException e) {
            return authenticationEntryPoint.commence(exchange, e);
        }
    }
}
