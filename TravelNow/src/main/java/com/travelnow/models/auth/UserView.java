package com.travelnow.models.auth;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.travelnow.core.dbacces.models.security.User;

public class UserView {

    private int id;
    private String userName;
    private boolean active;
    private List<String> authorities;

    public UserView(User user) {
        this.id = user.getId();
        this.userName = user.getUsername();
        this.active = (user.getActive() == 'Y');
        this.authorities = Arrays.stream(user.getRoles().split(","))
                                    .collect(Collectors.toList());                              
    }

    public int getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public boolean isActive() {
        return active;
    }

    public List<String> getAuthorities() {
        return authorities;
    }
}
