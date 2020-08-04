package com.cos.securityex01.config.auth.oauth;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.cos.securityex01.config.auth.PrincipalDetails;
import com.cos.securityex01.config.auth.provider.FaceBookUserInfo;
import com.cos.securityex01.config.auth.provider.GoogleUserInfo;
import com.cos.securityex01.config.auth.provider.OAuth2UserInfo;
import com.cos.securityex01.model.User;
import com.cos.securityex01.repository.UserRepository;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {
	
	@Autowired
	private UserRepository userRepository;

	// userRequset 는 code를 받아서 accessToken 을 응답 받은 객체
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		OAuth2User oAuth2User = super.loadUser(userRequest); // google의 회원 프로필
		// 1. PrincipalDetails에 oAuth2User 정보를 집어 넣어 준다.
		// 2. PrincipalDetails를 리턴한다.
		System.out.println("oAuth2User : " + oAuth2User); // 토큰을 통해 응답받은 회원정보
		System.out.println("oAuth2User : " + userRequest.getAccessToken().getTokenValue());  
		System.out.println("oAuth2User : " + userRequest.getClientRegistration());  
		return processOAuth2User(userRequest, oAuth2User);
	}
	
	private OAuth2User processOAuth2User(OAuth2UserRequest userRequest, OAuth2User oAuth2User) { // principalDetails를 만들어서 넘기기
		
		// 일반적으로는 로그인할 때 유저 정보 User
		// 1. OAuth2로 로그인할 때 유저 정보 attributes(콤포지션)	
		// 2. DB에 있는 사용자인지 확인
		// 있을 시 : 사용자의 유저정보 update
		// 없을 시 : 회원가입 insert
		// return PrincipalDetails() 
		
		OAuth2UserInfo oAuth2UserInfo = null;
		if (userRequest.getClientRegistration().getRegistrationId().equals("google")) {
			oAuth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
		} else if (userRequest.getClientRegistration().getRegistrationId().equals("faceBook")) {
			oAuth2UserInfo = new FaceBookUserInfo(oAuth2User.getAttributes());
		} else {
			System.out.println("우리는 구글과 페이스북만 지원합니다.");
		}
		
		Optional<User> userOptional = 
				userRepository.findByProviderAndProviderId(oAuth2UserInfo.getProvider(), oAuth2UserInfo.getProviderId());
		
		User user;
		if (userOptional.isPresent()) {
			user = userOptional.get();
		} else {
			// user의 패스워드가 null이기 때문에 OAuth 유저는 일반적인 로그인을 할 수 없음
			user = User.builder()
					.username(oAuth2UserInfo.getProvider()+"_"+oAuth2UserInfo.getProviderId())
					.email(oAuth2UserInfo.getEmail())
					.role("ROLE_USER")
					.provider(oAuth2UserInfo.getProvider())
					.providerId(oAuth2UserInfo.getProviderId())
					.build();
			userRepository.save(user);
		}
		
		return new PrincipalDetails(user, oAuth2User.getAttributes());
	}
}