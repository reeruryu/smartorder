package com.example.smartorder.common.configuration;

import com.example.smartorder.common.component.JwtAuthenticationFilter;
import com.example.smartorder.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	private final JwtAuthenticationFilter authenticationFilter;
	private final AuthService authService;

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	public void configure(WebSecurity web) throws Exception {

		web.ignoring().antMatchers("/favicon.ico", "/files/**");

		super.configure(web);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// db 사용을 위해 임시로
		// 보안을 위해 나중에 뺴야 됨
		http.csrf().disable();
		http.headers().frameOptions().sameOrigin();

		http.authorizeRequests()
			.antMatchers("/admin/**").hasRole("ADMIN")
			.antMatchers("/ceo/**").hasRole("CEO")
			.antMatchers("/",
				"/auth/**"
				)
			.permitAll()
			.and()
			.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);

		super.configure(http);
	}

	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
}
