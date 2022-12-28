package com.example.smartorder.configuration;

import com.example.smartorder.member.exception.MemberException;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

public class UserAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
		AuthenticationException exception) throws IOException, ServletException {

		String msg = "로그인에 실패하였습니다.";

		if (exception instanceof UsernameNotFoundException) {
			msg = exception.getMessage();
		} else if (exception instanceof BadCredentialsException) {
			msg = exception.getMessage();
		} else if (exception instanceof MemberException) {
			msg = exception.getMessage();
		}

		setUseForward(true);
		setDefaultFailureUrl("/member/login?error=true");
		request.setAttribute("errorMessage", msg);

		super.onAuthenticationFailure(request, response, exception);
	}
}
