package com.example.smartorder.member.service.Impl;

import static com.example.smartorder.member.type.ErrorCode.USER_NOT_FOUND;
import static com.example.smartorder.member.type.UserRole.ROLE_USER;
import static com.example.smartorder.member.type.UserStatus.STATUS_EMAIL_REQ;
import static com.example.smartorder.member.type.UserStatus.STATUS_ING;

import com.example.smartorder.component.MailComponents;
import com.example.smartorder.member.entity.Member;
import com.example.smartorder.member.model.CustomUserDetails;
import com.example.smartorder.member.model.MemberInput;
import com.example.smartorder.member.repository.MemberRepository;
import com.example.smartorder.member.service.MemberService;
import com.example.smartorder.member.type.UserRole;
import com.example.smartorder.util.PasswordUtils;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MemberServiceImpl implements MemberService {

	private final MemberRepository memberRepository;
	private final MailComponents mailComponents;

	@Override
	public boolean register(MemberInput parameter) {

		String userId = parameter.getUserId();
		Optional<Member> optionalMember = memberRepository.findById(userId);
		if (optionalMember.isPresent()) {
			return false;
		}

		String encPassword = PasswordUtils.encPassword(parameter.getPw());

		String uuid = UUID.randomUUID().toString();

		Member member = Member.builder()
			.userId(userId)
			.userName(parameter.getUserName())
			.phone(parameter.getPhone())
			.pw(encPassword)
			.emailAuthYn(false)
			.emailAuthKey(uuid)
			.userRole(ROLE_USER)
			.userStatus(STATUS_EMAIL_REQ)
			.build();
		memberRepository.save(member);

		String subject = "smartorder 사이트 가입을 축하드립니다.";
		String text = "<p>smartorder 사이트 가입을 축하드립니다.</p>" + "<p>아래 링크를 클릭하시면 회원 활성화가 됩니다.</p>"
			+ "<div><a href='http://localhost:8080/member/email-auth?uuid=" + uuid
			+ "'> 링크 클릭 </a></div>";
		mailComponents.sendMail(userId, subject, text);

		return true;
	}

	@Override
	public boolean emailAuth(String uuid) {

		Optional<Member> optionalMember = memberRepository.findByEmailAuthKey(uuid);
		if (!optionalMember.isPresent()) {
			return false;
		}

		Member member = optionalMember.get();

		if (member.isEmailAuthYn()) {
			return false;
		}

		member.setEmailAuthYn(true);
		member.setEmailAuthDt(LocalDateTime.now());
		member.setUserStatus(STATUS_ING);
		memberRepository.save(member);

		return true;
	}

	@Override
	public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<Member> optionalMember = memberRepository.findById(username);
		if (!optionalMember.isPresent()) {
			throw new UsernameNotFoundException(USER_NOT_FOUND.getDescription());
		}
		Member member = optionalMember.get();

		List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
		grantUserRole(member, grantedAuthorities);

		return new CustomUserDetails(member, grantedAuthorities);
	}

	private static void grantUserRole(Member member, List<GrantedAuthority> grantedAuthorities) {
		grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));

//		if (!ROLE_USER.equals(member.getUserRole())) {
//			grantedAuthorities.add(new SimpleGrantedAuthority(member.getUserRole()));
//		}

		if (member.isCeoYn()) {
			grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_CEO"));
		}

		if (member.isAdminYn()) {
			grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
		}
	}

}
