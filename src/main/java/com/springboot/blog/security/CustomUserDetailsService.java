package com.springboot.blog.security;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.springboot.blog.entity.User;
import com.springboot.blog.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {

        Optional<User> user = this.userRepository.findByUserNameOrEmail(usernameOrEmail, usernameOrEmail);

        if(user.isEmpty())
            throw new UsernameNotFoundException("User not found with username or email: " + usernameOrEmail);

        // agora tendo o objeto User, preciso converter o Set de role em Set<GrantedAuthoruty>

        User userCatched = user.get();

        Set<GrantedAuthority> authorities = userCatched
                .getRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toSet());

        // essa classe User, implementa userDetails
        return new org.springframework.security.core.userdetails.User(userCatched.getEmail(),
                userCatched.getPassword(), authorities
        );

    }
}


