package com.travelnow.services;
import java.util.List;

import com.travelnow.models.Comment;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import com.travelnow.dao.CommentDao;

@Service
public class CommentsService {
    CommentDao commentDao;
    Integer minRating = 1;
    Integer maxRating = 5;
    @Autowired
    public CommentsService(@Qualifier("CommentDaoImplementation") CommentDao commentDao) {
        this.commentDao = commentDao;
    }
    public List<Comment> getByHotelsId(int id) {
        return commentDao.getByHotelsId(id);
    }
    public Comment putComment(Comment comment) throws Exception{
        int rating = comment.getRating();
        if(rating >= minRating && rating <= maxRating){
            comment = commentDao.putComment(comment);
            return comment;
        } else {
            throw new Exception("Rating is not between 1 and 5");
        }
    }
    
}
