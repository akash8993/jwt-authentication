package com.jwt.jwt.authentication.service;

import com.jwt.jwt.authentication.model.AppUser;
import com.jwt.jwt.authentication.repository.AppUserRepo;
import com.jwt.jwt.authentication.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements  AuthService{

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final AppUserRepo appUserRepo;

    @Override
    public String login(String username, String password) {
        var authToken= new UsernamePasswordAuthenticationToken(username, password);

        //To ab ye authentication Manager ki problem hai to identify the authentication provider and
        //authenticate the credentials
        var authenticate= authenticationManager.authenticate(authToken);

        return JwtUtils.generateToken(((UserDetails)authenticate.getPrincipal()).getUsername());

    }

    @Override
    public String signUp(String name, String username, String password) {
        //Check weather User already exist or not
        AppUser appUser=appUserRepo.findByUsername(username);
        if(appUser!=null)
        {
            throw new RuntimeException("User Already Exist");
        }
        var encodedPassword= passwordEncoder.encode(password);

        var authorities= new ArrayList<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));


        AppUser appUserToSave= new AppUser();
        appUserToSave.setName(name);
        appUserToSave.setUsername(username);
        appUserToSave.setPassword(encodedPassword);
        appUserToSave.setAuthorities(authorities);

        appUserRepo.save(appUserToSave);
        return JwtUtils.generateToken(username);
    }
}
