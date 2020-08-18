package com.mercari.service;

import com.mercari.domain.User;
import com.mercari.repository.UsersRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class InsertUserService {
    
    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
	 * ユーザー情報を登録します. パスワードはここでハッシュ化されます
	 * 
	 * @param administrator 管理者情報
	 */
	public void insert(User user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		usersRepository.insert(user);
	}

	public User findByMailAddress(String email) {
		return usersRepository.findByMailAddress(email);
	}
	
	public void update(User user) {
		usersRepository.update(user);
	}
    
}