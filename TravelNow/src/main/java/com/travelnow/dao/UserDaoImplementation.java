package com.travelnow.dao;
import org.springframework.stereotype.Repository;
import com.travelnow.core.dbacces.models.security.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

@Repository("UserDaoImplementation")
public class UserDaoImplementation implements UserDao {
    private JdbcTemplate jdbcTemplate;
    @Autowired
    public UserDaoImplementation(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User putUser(User user, String mail) {
    final String sqlinsert = "insert into users (username, password, active, roles, email) values (?, ?, ?, ?, ?)";
    jdbcTemplate.update(sqlinsert, user.getUsername(), user.getPassword(), Character.toString(user.getActive()), user.getRoles(), mail);
    final String sqlget = "select id from users where (username = ?)";
    Integer id = jdbcTemplate.queryForObject(sqlget, int.class, user.getUsername());
    user.setId(id);
    return user;
    }
    
}
