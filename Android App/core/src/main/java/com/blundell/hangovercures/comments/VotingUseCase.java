package com.blundell.hangovercures.comments;

import com.blundell.hangovercures.Cure;

public class VotingUseCase {

    private final VoteRepository voteRepository;

    public VotingUseCase(VoteRepository voteRepository) {
        this.voteRepository = voteRepository;
    }

    public boolean canVoteOnCommentWith(Cure.Id cureId, Comment.Id commentId) {
        Vote vote = voteRepository.readVote(cureId, commentId);
        return vote == Vote.UNCAST;
    }

    public void saveVote(Vote vote, Cure.Id cureId, Comment.Id commentId) {
        voteRepository.updateVote(vote, cureId, commentId);
    }
}
