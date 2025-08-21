package com.makanza.job_portals.service;

import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.makanza.job_portals.model.MyUser;
import com.makanza.job_portals.repository.UserRepository;

@Service
public class MyUserDetailsService implements UserDetailsService{
	

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		
		
		
		
			Optional<MyUser> user=userRepository.findByUsername(username);
			
		if(user.isPresent()) {
			var myUser=user.get();
			return User.builder()
					.username(myUser.getUsername())
					
					.password(myUser.getPassword())
					.roles(getRoles(myUser))
					.build();
					
		}else {
			throw new UsernameNotFoundException(username);
		}
	
			
	}
	private String[] getRoles(MyUser myUser) {
	    if (myUser.getRole() == null) return new String[] {"USER"};
	    return myUser.getRole().split(",");
	}

}
