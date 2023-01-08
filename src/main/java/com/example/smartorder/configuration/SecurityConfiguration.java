package com.example.smartorder.configuration;

import com.example.smartorder.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	private final MemberService memberService;

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public CustomAuthenticationProvider customAuthenticationProvider() {
		return new CustomAuthenticationProvider(memberService, passwordEncoder());
	}

	@Bean
	UserAuthenticationFailureHandler getFailureHandler() {
		return new UserAuthenticationFailureHandler();
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
			.antMatchers("/",
				"/member/register",
				"/member/email-auth",
				"/cart/**",
				"/ceo/**",
				"/order/**"
				)
			.permitAll();

		http.authorizeRequests()
			.antMatchers("/admin/**").hasAuthority("ROLE_ADMIN");
//			.antMatchers("/ceo/**").hasAuthority("ROLE_CEO");

		http.formLogin()
			.loginPage("/member/login")
			.defaultSuccessUrl("/")
			.failureHandler(getFailureHandler())
			.permitAll();



//			.and()
//			.addFilterBefore(customAuthenticationFilter(),
//				UsernamePasswordAuthenticationFilter.class);

		http.logout()
			.logoutRequestMatcher(new AntPathRequestMatcher("/member/logout"))
			.logoutSuccessUrl("/")
			.invalidateHttpSession(true);

		http.exceptionHandling()
			.accessDeniedPage("/error/denied");

		super.configure(http);
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {

//		auth.userDetailsService(memberService)
//			.passwordEncoder(passwordEncoder());
//
//		super.configure(auth);
		auth.authenticationProvider(customAuthenticationProvider());
	}
}
