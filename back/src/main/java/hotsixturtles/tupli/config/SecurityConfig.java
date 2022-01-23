package hotsixturtles.tupli.config;

import hotsixturtles.tupli.config.auth.AppProperties;
import hotsixturtles.tupli.config.auth.JwtAuthenticationFilter;
import hotsixturtles.tupli.config.filter.TokenAuthenticationFilter;
import hotsixturtles.tupli.config.interceptor.OAuth2AuthenticationFailureHandler;
import hotsixturtles.tupli.config.interceptor.OAuth2AuthenticationSuccessHandler;
import hotsixturtles.tupli.config.interceptor.TokenAccessDeniedHandler;
import hotsixturtles.tupli.exception.RestAuthenticationEntryPoint;
import hotsixturtles.tupli.repository.OAuth2AuthorizationRequestBasedOnCookieRepository;
import hotsixturtles.tupli.service.auth.CustomOAuth2UserService;
import hotsixturtles.tupli.service.auth.CustomUserDetailsService;
import hotsixturtles.tupli.service.token.AuthTokenProvider;
import hotsixturtles.tupli.service.token.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    // OAUTH
    //private final CorsProperties corsProperties;
    private final AppProperties appProperties;
    private final AuthTokenProvider tokenProvider;
    private final CustomUserDetailsService userDetailsService;
    private final CustomOAuth2UserService oAuth2UserService;
    private final TokenAccessDeniedHandler tokenAccessDeniedHandler;
//    private final UserRefreshTokenRepository userRefreshTokenRepository;

    // LOCAL
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * WebSecurityConfigurerAdapter 상속 configure 설정
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                // 기본 REST API만 쓰겠다는 소리
                .httpBasic().disable()
                .cors().and()
                .csrf().disable()
                // SockJS는 기본적으로 HTML iframe 요소를 통한 전송을 허용하지 않도록 설정되는데 해당 내용을 해제한다.
                .headers()
                .frameOptions().sameOrigin()
                .and()
                // 세션 안쓰고 JWT 쓸것이므로 비활성화
//            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//            .and()
                .authorizeRequests()
//                .antMatchers("/chat/**").hasRole("USER")  // chat으로 시작하는 리소스에 대한 접근 권한 설정 ; TEST!!!
                .antMatchers("/**").permitAll()
                .and()
                // JwtAuthenticationFilter를 UsernamePasswordAuthenticationFilter보다 앞으로 설정
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling()
                .authenticationEntryPoint(new RestAuthenticationEntryPoint())
                .accessDeniedHandler(tokenAccessDeniedHandler)
//            .and()
//                .authorizeRequests()
//                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
//                .antMatchers("/api/**").hasAnyAuthority(RoleType.USER.getCode())
//                .antMatchers("/api/**/admin/**").hasAnyAuthority(RoleType.ADMIN.getCode())
//                .anyRequest().authenticated()
                .and()
                .oauth2Login()
                .authorizationEndpoint()
                .baseUri("/oauth2/authorization")
                .authorizationRequestRepository(oAuth2AuthorizationRequestBasedOnCookieRepository())
                .and()
                .redirectionEndpoint()
                .baseUri("/*/oauth2/code/*")
                .and()
                .userInfoEndpoint()
                .userService(oAuth2UserService)
                .and()
                .successHandler(oAuth2AuthenticationSuccessHandler())
                .failureHandler(oAuth2AuthenticationFailureHandler());

        http.addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    /**
     * 비밀번호 암호화 : 기본 설정 : bycrypt
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }


    /**
     * OAUTH
     */
    // auth 매니저 설정
    @Override
    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    // 토큰 필터 설정
    @Bean
    public TokenAuthenticationFilter tokenAuthenticationFilter() {
        return new TokenAuthenticationFilter(tokenProvider);
    }

    // 쿠키 기반 인가 Repository
    // 인가 응답을 연계하고 검증할 때 사용
    @Bean
    public OAuth2AuthorizationRequestBasedOnCookieRepository oAuth2AuthorizationRequestBasedOnCookieRepository() {
        return new OAuth2AuthorizationRequestBasedOnCookieRepository();
    }

    // OAUTH 인증 성공 핸들러
    @Bean
    public OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler() {
        return new OAuth2AuthenticationSuccessHandler(
                tokenProvider,
                appProperties,
//                userRefreshTokenRepository,
                oAuth2AuthorizationRequestBasedOnCookieRepository()
        );
    }

    // Oauth 인증 실패 핸들러
    @Bean
    public OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler() {
        return new OAuth2AuthenticationFailureHandler(oAuth2AuthorizationRequestBasedOnCookieRepository());
    }


}
