package com.example.smartorder.component;

import com.example.smartorder.service.AuthService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@RequiredArgsConstructor
public class TokenProvider {
	private static final long TOKEN_EXPIRE_TIME = 5000 * 60 * 60; // 5시간
	private static final String KEY_ROLES = "roles";

	private final AuthService authService;

	@Value("${spring.jwt.secret}")
	private String secretKey;

	// 토큰 생성
	public String generateToken(String username, List<String> roles) {
		Claims claims = Jwts.claims().setSubject(username);
		claims.put(KEY_ROLES, roles);

		Date now = new Date();
		Date expiredDate = new Date(now.getTime() + TOKEN_EXPIRE_TIME);

		return Jwts.builder()
			.setClaims(claims)
			.setIssuedAt(now)
			.setExpiration(expiredDate)
			.signWith(SignatureAlgorithm.HS512, secretKey)
			.compact();
	}

	// jwt token으로부터 인증 정보를 가져오는 메서드
	public Authentication getAuthentication(String token) {
		UserDetails userDetails = authService.loadUserByUsername(getUsername(token));
		return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
	}

	// 이름 추출
	public String getUsername(String token) {
		return parseClaims(token).getSubject();
	}

	// 유효성 및 만료 기간 검사
	public boolean validateToken(String token) {
		if (!StringUtils.hasText(token)) return false;

		return !parseClaims(token).getExpiration().before(new Date());
	}

	private Claims parseClaims(String token) {
		try {
			// Claims 정보
			return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();

		} catch (ExpiredJwtException e) {
			return e.getClaims();
			// TODO throw new ExpiredJwtException
		}
	}



}
