package com.blundell.hangovercures.comments;

import com.blundell.hangovercures.Cure;
import com.blundell.hangovercures.archive.User;

import java.util.List;

interface CommentRepository {

    void signInUser(); //TODO UserRepository?

    User getUser();

    Comment.Id saveComment(Cure.Id id, Comment comment);

    List<Comment> readComments(Cure.Id cureId);

    void updateComment(Cure.Id cureId, Comment comment);

}
