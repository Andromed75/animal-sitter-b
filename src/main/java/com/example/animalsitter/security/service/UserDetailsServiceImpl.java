package com.example.animalsitter.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.animalsitter.domain.User;
import com.example.animalsitter.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	@Autowired
	UserRepository userRepository;

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String pseudo) throws UsernameNotFoundException {
		User user = userRepository.findByPseudo(pseudo)
				.orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + pseudo));

		return UserDetailsImpl.build(user);
	}

}
