package com.project.serviceImpl;

import com.project.entity.Owner;
import com.project.repository.OwnerRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final OwnerRepository ownerRepository;

    public CustomUserDetailsService(OwnerRepository ownerRepository) {
        this.ownerRepository = ownerRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Owner owner = ownerRepository.findByUsername(username);
        if (owner == null) {
            throw new UsernameNotFoundException("Owner not found with username: " + username);
        }
        return new User(owner.getUsername(), owner.getPassword(), Collections.emptyList());
    }
}
