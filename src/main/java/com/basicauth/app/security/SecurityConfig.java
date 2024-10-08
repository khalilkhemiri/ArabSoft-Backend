package com.basicauth.app.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import com.basicauth.app.service.UserDetail;

@Configuration
@ConditionalOnProperty(value = "security.enabled" , havingValue = "true")
public class SecurityConfig {

	@Autowired
	private UserDetail userDetail;

	@Autowired
	private UnauthorizedEntrypoint unauthorizedEntrypoint;
	@Autowired
	private JWTAuthFIlter jwtAuthFIlter;
	@Bean
	public static PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}



	@Bean
	public SecurityFilterChain filterSecurity(HttpSecurity http) throws Exception {

		http.csrf(htpSecurity -> htpSecurity.disable());

		http.securityMatcher("/admin-page","/index","/contact","/register*")
		.authorizeHttpRequests(
				(authorize) -> authorize
				.requestMatchers("/admin-page").hasRole("ADMIN")
				.requestMatchers("/index").hasAnyRole("CHEF","ADMIN","PERSONNEL","EMPLOYE")
				.requestMatchers("/contact").permitAll()
				.requestMatchers("/register*").permitAll()
				.anyRequest().authenticated())


				.sessionManagement(manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authenticationProvider(authenticationProvider()).addFilterBefore(
						jwtAuthFIlter, UsernamePasswordAuthenticationFilter.class
				);
		return http.build();
	}

	@Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers(new AntPathRequestMatcher("/h2-console/**"));
    }
	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		daoAuthenticationProvider.setUserDetailsService(userDetail);
		daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
		return daoAuthenticationProvider;
	}



	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}
}