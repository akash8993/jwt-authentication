package com.jwt.jwt.authentication.service.userDetails;

import com.jwt.jwt.authentication.repository.AppUserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final AppUserRepo appUserRepo;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user=appUserRepo.findByUsername(username);
        return  new User(user.getUsername(),user.getPassword(), user.getAuthorities());
    }
}
