package com.example.smartorder.model;

import static com.example.smartorder.type.UserStatus.STATUS_EMAIL_REQ;
import static com.example.smartorder.type.UserStatus.STATUS_STOP;
import static com.example.smartorder.type.UserStatus.STATUS_WITHDRAW;

import com.example.smartorder.entity.Member;
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

	/**
	 * 만료 여부
	 */
	@Override
	public boolean isAccountNonExpired() {
		if (STATUS_WITHDRAW == member.getUserStatus()) {
			return false;
		}
		return true;
	}

	/**
	 * 잠김 여부
	 */
	@Override
	public boolean isAccountNonLocked() {
		if (STATUS_STOP == member.getUserStatus()) {
			return false;
		}
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	/**
	 * 활성화 여부
	 */
	@Override
	public boolean isEnabled() {
		if (STATUS_EMAIL_REQ == member.getUserStatus()) {
			return false;
		}
		return true;
	}
}