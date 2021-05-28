package com.travelnow.dao;

import com.travelnow.core.dbacces.models.security.User;

public interface UserDao {
    User putUser(User user, String mail);
}
