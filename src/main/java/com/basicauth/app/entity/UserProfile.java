package com.basicauth.app.entity;

import com.basicauth.app.enums.Role;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Entity
@Data
public class UserProfile implements UserDetails {

	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	private String name;
	private String email;
	private String password;
	private Date DateOfBirth;
	private String Number;
	private String token;
	private boolean isOnline;

	@Enumerated(EnumType.STRING)
	private Role role;

    @ManyToOne
    @JoinColumn(name = "chef_hierarchique_id")
    private UserProfile chefHierarchique;

    @ManyToOne
    @JoinColumn(name = "administrateur_id")
    private UserProfile administrateur;

    @OneToMany(mappedBy = "chefHierarchique")
    private List<UserProfile> subordonnes;

    @OneToMany(mappedBy = "administrateur")
    private List<UserProfile> personnels;



	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Collections.singleton(new SimpleGrantedAuthority(role.name()));
	}
	@Override
	public String getUsername() {
		return email;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}
