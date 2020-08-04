package com.cos.securityex01.config.auth;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.cos.securityex01.model.User;

import lombok.Data;

// Authentication 객체에 저장할 수 있는 유일한 타입 → 세션에 담음
public class PrincipalDetails implements UserDetails, OAuth2User {

	
	private User user;
	private Map<String, Object> attributes;

	public PrincipalDetails(User user) {
		super();
		this.user = user;
	}
	
	public User getUser() {
		return user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}
	
	// OAuth2.0 로그인시 사용
	public PrincipalDetails(User user, Map<String, Object> attributes) {
		this.user = user;
		this.attributes = attributes;
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() { // 비밀번호 5번 이상 틀리면 Lock 걸리게 할 때
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() { // 계정 활성화 되어있는지 확인할 때
		return true;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() { // 권한
		List<GrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority(user.getRole()));
		System.out.println("PrincipalDetails 확인 : " + authorities);
		return authorities;
	}

	// 리소스 서버로부터 받는 회원정보
	// 토큰요청하고 응답받는 회원정보 들어옴
	@Override
	public Map<String, Object> getAttributes() { 
		return attributes;
	}

	
	@Override
	public String getName() {
		return "제공자 ID";
	}

}
