package com.tablefootbal.server.service;

import com.tablefootbal.server.entity.User;
import com.tablefootbal.server.entity.UserRole;
import com.tablefootbal.server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;

@Service
public class UserService implements UserDetailsService
{
	final private
	UserRepository repository;
	
	final private MessageSource messageSource;
	
	@Autowired
	public UserService(UserRepository repository, MessageSource messageSource)
	{
		this.repository = repository;
		this.messageSource = messageSource;
	}
	
	@Override
	public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException
	{
		User user = repository.findByUsername(s);
		
		if (user == null)
		{
			throw new UsernameNotFoundException(
					messageSource.getMessage("error.user_not_found", new Object[]{s}, Locale.getDefault())
			);
		}
		
		return new org.springframework.security.core.userdetails.User(
				user.getUsername(), user.getPassword(), true,
				true, true, true,
				getAuthorities(user.getRoles())
		);
	}
	
	Collection<? extends GrantedAuthority> getAuthorities(Collection<UserRole> roles)
	{
		Collection<GrantedAuthority> authorities = new ArrayList<>();
		
		for (UserRole role : roles)
		{
			authorities.add(new SimpleGrantedAuthority(role.getName()));
		}
		
		return authorities;
	}
}
