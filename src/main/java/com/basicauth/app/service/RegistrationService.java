package com.basicauth.app.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.basicauth.app.entity.UserProfile;
import com.basicauth.app.enums.Role;
import com.basicauth.app.repository.RegisterNewUserRepository;

@Service
public class RegistrationService {
	
	@Autowired
	private RegisterNewUserRepository registerRepo;
	
	@Autowired
	private PasswordEncoder pwdEncoder;

	public boolean registerUser(UserProfile user) {
		Optional<UserProfile> userContainer = registerRepo.findByEmail(user.getEmail());
		if(userContainer.isEmpty()) {
			user.setRole(Role.PERSONNEL);
			user.setPassword(pwdEncoder.encode(user.getPassword()));
			registerRepo.save(user);
			return true;
		}
		return false;
	}
	
	public boolean registerAdmin(UserProfile admin) {
		Optional<UserProfile> userContainer = registerRepo.findByEmail(admin.getEmail());
		if(userContainer.isEmpty()) {
			admin.setRole(Role.ADMIN);
			admin.setPassword(pwdEncoder.encode(admin.getPassword()));
			registerRepo.save(admin);
			return true;
		}
		return false;
	}
	public boolean registerChef(UserProfile chef) {
		Optional<UserProfile> userContainer = registerRepo.findByEmail(chef.getEmail());
		if(userContainer.isEmpty()) {
			chef.setRole(Role.CHEF);
			chef.setPassword(pwdEncoder.encode(chef.getPassword()));
			registerRepo.save(chef);
			return true;
		}
		return false;
	}
	
}
