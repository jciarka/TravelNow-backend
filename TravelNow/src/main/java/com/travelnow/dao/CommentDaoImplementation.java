package com.travelnow.dao;
import java.util.Date;
import java.util.List;
import java.time.LocalDate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import com.travelnow.models.Comment;
import java.time.Instant;
import java.time.ZoneId;

@Repository("CommentDaoImplementation")
public class CommentDaoImplementation implements CommentDao{
    private JdbcTemplate jdbcTemplate;
    @Autowired
    public CommentDaoImplementation(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    RowMapper<Comment> commentMapper =  (rs, i) -> {
        Comment r = new Comment();
        r.setId(rs.getInt("id"));
        r.setText(rs.getString("text"));
        r.setRating(rs.getInt("rating"));
        Date dateToConvert = rs.getDate("postDate");
        LocalDate convertedDate = Instant.ofEpochMilli(dateToConvert.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
        r.setPostDate(convertedDate);
        r.setHotelsId(rs.getInt("hotels_Id"));
        Integer usersId = rs.getInt("users_id");
        final String sqlUsername = "select username from users where id = ?";
        String username = jdbcTemplate.queryForObject(sqlUsername, String.class, usersId);
        r.setAuthorUserName(username);
        return r;
    };




    @Override
    public List<Comment> getByHotelsId(int id) {
        final String sql = "select id, text, rating, postDate, hotels_Id, users_id from comments where hotels_Id = ? order by postDate desc";
        List<Comment> reservations = jdbcTemplate.query(sql, commentMapper, id);
        return reservations;
    }




    @Override
    public Comment putComment(Comment comment) {
        Date postdate = java.util.Date.from(comment.getPostDate().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        final String sqlUsername = "select id from users where username = ?";
        Integer usersId = jdbcTemplate.queryForObject(sqlUsername, Integer.class, comment.getAuthorUserName());
        final String sqlinsert = "insert into comments (text, rating, postdate, users_id, hotels_id) values (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sqlinsert, comment.getText(), comment.getRating(), postdate, usersId, comment.getHotelsId());
        final String sqlget = "select id from comments where (text = ?) and (rating = ?) and (postdate = ?) and (users_id = ?) and (hotels_id = ?)";
        Integer id = jdbcTemplate.queryForObject(sqlget, int.class, comment.getText(), comment.getRating(), postdate, usersId, comment.getHotelsId());
        comment.setId(id);
        return comment;
    }
    
}
