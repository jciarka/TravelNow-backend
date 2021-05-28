package com.travelnow.dao;

import java.util.List;

import com.travelnow.models.Comment;

public interface CommentDao {

    List<Comment> getByHotelsId(int id);

    Comment putComment(Comment comment);
    
}
