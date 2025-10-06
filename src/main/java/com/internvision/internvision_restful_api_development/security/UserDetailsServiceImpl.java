package com.internvision.internvision_restful_api_development.security;

import com.internvision.internvision_restful_api_development.model.document.User;
import com.internvision.internvision_restful_api_development.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository repo;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User u = repo.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new UserDetailsImpl(u);
    }
}

