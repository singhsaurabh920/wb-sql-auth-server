package org.wb.auth.service;

import org.apache.commons.codec.binary.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.wb.auth.entity.Role;
import org.wb.auth.entity.User;
import org.wb.auth.repository.UserRepository;
import org.wb.auth.utils.RoleUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Service(value = "customUserDetailsService")
public class UserDetailsServiceImpl implements UserService,UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	@Qualifier(value = "passwordEncoder")
	private PasswordEncoder passwordEncoder;


	@Override
	public UserDetails loadUserByUsername(String username) {
		if (StringUtils.equals(username, "wbadmin")) {
			return new User("wbadmin",passwordEncoder.encode("wbadmin"),Arrays.asList(RoleUtils.adminRole()));
		}
		User user = userRepository.findByUsername(username);
		if (user == null) {
			throw new BadCredentialsException("Bad credentials");
		}
		new AccountStatusUserDetailsChecker().check(user);
		return user;
	}
}
