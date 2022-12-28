package com.example.smartorder.member.model;

import static com.example.smartorder.member.type.UserStatus.STATUS_EMAIL_REQ;
import static com.example.smartorder.member.type.UserStatus.STATUS_ING;

import com.example.smartorder.member.entity.Member;
import java.util.Collection;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@RequiredArgsConstructor
@Getter
public class CustomUserDetails implements UserDetails {

	@Delegate
	private final Member member;

	private final Collection<? extends GrantedAuthority> authorities;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return member.getPw();
	}

	@Override
	public String getUsername() {
		return member.getUserId();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
//		if (STATUS_EMAIL_REQ.equals(member.getUserStatus())) {
//			return false;
//		}
		return true;
	}
}
