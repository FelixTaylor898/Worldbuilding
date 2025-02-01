package com.java.backend.security;

import com.java.backend.model.AppUser;
import com.java.backend.data.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AppUserDetailsService implements UserDetailsService {

    private final AppUserRepository userRepository;

    @Autowired
    public AppUserDetailsService(AppUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<AppUser> appUser = userRepository.findByUsername(username);

        if (appUser.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }

        AppUser user = appUser.get();

        return User.builder()
                .username(user.getUsername())
                .password(user.getPasswordHash())  // Or appUser.getPassword() if not hashed yet
                .authorities("ROLE_USER")
                .build();
    }
}
