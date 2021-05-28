package com.travelnow.controllers;

import java.time.LocalDate;
import java.util.List;

import javax.annotation.security.RolesAllowed;

import com.travelnow.core.security.models.MyUserDetails;
import com.travelnow.models.Comment;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import com.travelnow.services.CommentsService;

@RestController
public class CommentsApiController {

    CommentsService commentsservice;

    @Autowired
    public CommentsApiController(CommentsService commentsservice) {
        super();
        this.commentsservice = commentsservice;
    }

    // /api/commensts/{hotelId}
    // List<Commets>
    @GetMapping("/api/comments/{hotelId}")
    public List<Comment> hotelFreeRooms(@PathVariable("hotelId") int id) {
        // Trzeba przygotować serwis który zaciągnie je z bazy danych
        // Dane wstawiane do api powinny być w kolejności malejacej datami aby nie trzeba było ich sortować po stronie frontend 
        // na próbe robie sobie randomowe dane

        // zamiast tego pownino być  List<Comment> testComments = service.getByHotelsId(id)
        List<Comment> comments = commentsservice.getByHotelsId(id);
        //List<Comment> testComments = new ArrayList<Comment>() ;

        // Comment test1 = new Comment();
        // test1.setAuthorUserName("John Snow");
        // test1.setHotelsId(999);
        // test1.setPostDate(LocalDate.now());
        // test1.setText("Wow, it is awesome");
        // test1.setRating(5);
        // testComments.add(test1);

        // Comment test2 = new Comment();
        // test2.setAuthorUserName("Rocky Balboa");
        // test2.setHotelsId(999);
        // test2.setPostDate(LocalDate.now().minusDays(2));
        // test2.setText("Could be better");
        //test2.setRating(3);
        //testComments.add(test2);

        return comments;
    }


    @PutMapping("/api/comments")
    @RolesAllowed("ROLE_USER")
    public Comment putComment(@RequestBody Comment comment) {
        // in comment valid only text, rating and hotels_Id

        // username from secourity
        UserDetails user = getAutheticatedUser();
        comment.setAuthorUserName(user.getUsername());
        // String name = "dummy";
        // comment.setAuthorUserName(name);
        // date from local clock
        comment.setPostDate(LocalDate.now());
        try {
            comment = commentsservice.putComment(comment);
            } catch (Exception e) { }
        // put comment with service (validate text (check if no html code in text (XSS) attacks) and rating from 1 to 5)
        // if put operation succeed service returns comment with fulfilled id
        // else throw exception
        // comment = service.putComment();

        return comment;
    }



    public MyUserDetails getAutheticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (MyUserDetails) auth.getPrincipal();
    }
}
