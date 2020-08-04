package com.cos.securityex01.test;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.cos.securityex01.model.User;
import com.cos.securityex01.repository.UserRepository;

@RestController
public class OptionalControllerTest {

	@Autowired
	private UserRepository userRepository;
	
	@GetMapping("/test/user/{id}")
	public User 옵셔널_유저찾기(@PathVariable int id) {
//		Optional<User> userOptional = userRepository.findById(id);
//		User user;
//		
//		if(userOptional.isPresent()) {
//			user = userOptional.get();
//		} else {
//			user = new User();
//		}

//		// Supplier 함수가 하나일 때 화살표 함수 사용하여 함수, 타입 안적어줘도 됨. 무조건 get 실행될꺼니까 
//		User user = userRepository.findById(id).orElseGet(()-> {
//			// 널일 때 해당 값 나옴(널을 피하는 방법)
//				return new User();			
//		});
		
		User user = userRepository.findById(id)
				.orElseThrow(() -> {
						return new NullPointerException("값이 없음");
				});		
		return user;
	}
}
