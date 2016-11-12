package com.blundell.hangovercures.comments;

import com.blundell.hangovercures.Cure;

interface VoteRepository {

    Vote readVote(Cure.Id cureId, Comment.Id commentId);

    void updateVote(Vote vote, Cure.Id cureId, Comment.Id commentId);
}
