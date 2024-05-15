package com.yeminjilim.ssafyworld.config;

import com.yeminjilim.ssafyworld.jwt.JWTProvider;
import com.yeminjilim.ssafyworld.jwt.filter.JWTFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

@RequiredArgsConstructor
@EnableWebFluxSecurity
@Configuration
public class ReactiveSecurityConfig {

    private final JWTFilter jwtFilter;
    private final ServerAuthenticationEntryPoint authenticationEntryPoint;
    private final ServerAccessDeniedHandler serverAccessDeniedHandler;

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        //TODO 프론트 주소에 맞게 수정
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOriginPattern("*");
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityWebFilterChain configure(ServerHttpSecurity http) {

        http.formLogin().disable()
                .httpBasic().disable()
                .cors().configurationSource(corsConfigurationSource())
                .and()
                .csrf().disable()
//                .oauth2Login((oAuth2LoginSpec -> {
//                    // TODO : 성공, 실패 처리 이제 필요 없지 않나?
//                    oAuth2LoginSpec.authenticationSuccessHandler();
//                    oAuth2LoginSpec.authenticationFailureHandler();
//                }))
                .exceptionHandling((exceptionHandlingSpec -> {
                    exceptionHandlingSpec.accessDeniedHandler(serverAccessDeniedHandler);
                    exceptionHandlingSpec.authenticationEntryPoint(authenticationEntryPoint);
                }))
                // TODO : 이 부분이 필터 등록인데 저도 위치가 맞는지 모르겠어서 찾아봐야할것 가틍ㅁ
                .addFilterBefore(jwtFilter, SecurityWebFiltersOrder.AUTHENTICATION)
//                .authorizeExchange(authorizeExchangeSpec -> {
//                    authorizeExchangeSpec // 권한 체크하기 위한 build
//                            .pathMatchers("/admin/**").hasRole("ROLE_ADMIN") // 권한이 필요한 부분은 여기 아래
//                            .anyExchange().permitAll();
//                })
                ;

        return http.build();
    }
}
