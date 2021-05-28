package com.travelnow.core.security;

import java.util.Optional;

import com.travelnow.core.dbacces.models.security.User;
import com.travelnow.core.dbacces.models.security.UserRepository;
import com.travelnow.core.security.models.MyUserDetails;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(userName);

        // you can choose this
        // user.orElseThrow(() -> new UsernameNotFoundException("Not found: " + userName));
        // or this below:

        if (!user.isEmpty()) {
            return user.map(MyUserDetails::new).get();
        } else {
            throw new UsernameNotFoundException("Not found: " + userName);
        }
    }
}
