package com.travelnow.services;

import java.util.Optional;

import com.travelnow.core.dbacces.models.security.User;
import com.travelnow.core.dbacces.models.security.UserRepository;
import com.travelnow.dao.UserDao;
import com.travelnow.models.auth.CreateUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    
    private final UserRepository userRepository;
    UserDao userDao;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    public UserService(@Qualifier("UserDaoImplementation") UserDao userDao, UserRepository userRepository) {
        this.userDao = userDao;
        this.userRepository = userRepository;
    }

    public User findUserByUsername(String userName) throws UsernameNotFoundException {
        
        Optional<User> user = userRepository.findByUsername(userName);
        return user.orElseThrow(() -> new UsernameNotFoundException("Not found: " + userName));
    }

    public User createAccount(CreateUserInfo createUserInfo) throws Exception {
        Optional<User> tempuser = userRepository.findByUsername(createUserInfo.getUsername());
        if (tempuser.isEmpty()) {
            User incompleteuser = new User();
            incompleteuser.setUsername(createUserInfo.getUsername());
            String encodedPassword = bCryptPasswordEncoder.encode(createUserInfo.getPassword());
            incompleteuser.setPassword(encodedPassword);
            String roles = "";
            if (createUserInfo.getType() == 0) {
                roles = "ROLE_USER";
            }
            if (createUserInfo.getType() == 1) {
                roles = "ROLE_OWNER";
            }
            incompleteuser.setRoles(roles);
            incompleteuser.setActive('Y');
            User user = userDao.putUser(incompleteuser, createUserInfo.getEmail());
            return user;
        } else {
            throw new Exception("User already exists");
        }
    }    
}
