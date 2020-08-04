package com.cos.securityex01.config.auth.provider;

// OAuth2 제공자들마다 응답해주는 속성값이 달라서 공통속성을 만든다.
public interface OAuth2UserInfo {

	String getProviderId();
	String getName();
	String getEmail();
	String getProvider();
}
