package hotsixturtles.tupli.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

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
                .antMatchers("/**").permitAll();
    }

    /**
     * 비밀번호 암호화 : 기본 설정 : bycrypt
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }


}
