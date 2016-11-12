package com.blundell.hangovercures.comments;

import com.blundell.hangovercures.AdvertRequest;
import com.blundell.hangovercures.Cure;
import com.google.android.gms.ads.AdRequest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.joda.time.DateTime;

import rx.Observable;
import rx.Subscriber;

/**
 * Here we convert from the inner model of our use case model, to the model of MVP
 */
class UseCaseModelAdapter {
    private final CommentUseCase useCase;
    private final VotingUseCase votingUseCase;

    public UseCaseModelAdapter(CommentUseCase useCase, VotingUseCase votingUseCase) {
        this.useCase = useCase;
        this.votingUseCase = votingUseCase;
    }

    public Observable.OnSubscribe<ViewComment> saveComment(final Cure.Id cureId, final ViewComment viewComment) {
        return new Observable.OnSubscribe<ViewComment>() {
            @Override
            public void call(Subscriber<? super ViewComment> subscriber) {
                Comment comment = convertToComment(viewComment);
                Comment.Id commentId = useCase.saveComment(cureId, comment);
                ViewComment confirmedViewComment = convertToViewComment(commentId, comment);
                subscriber.onNext(confirmedViewComment);
                subscriber.onCompleted();
            }
        };
    }

    public Observable.OnSubscribe<AdRequest> getAdvertRequest() {
        return new Observable.OnSubscribe<AdRequest>() {
            @Override
            public void call(Subscriber<? super AdRequest> subscriber) {
                AdRequest.Builder builder = new AdRequest.Builder();
                AdvertRequest advertRequest = useCase.createAdvertRequest();
                if (advertRequest.shouldUseEmulatorForTestRequests()) {
                    builder.addTestDevice(AdRequest.DEVICE_ID_EMULATOR);
                }
                AdRequest adRequest = builder.build();
                subscriber.onNext(adRequest);
                subscriber.onCompleted();
            }
        };
    }

    public Observable.OnSubscribe<List<ViewComment>> getCommentsFor(final Cure cure, final SortOrder sortOrder) {
        return new Observable.OnSubscribe<List<ViewComment>>() {
            @Override
            public void call(Subscriber<? super List<ViewComment>> subscriber) {
                List<Comment> comments = useCase.getComments(cure.getId());
                List<ViewComment> viewComments = new ArrayList<>();
                for (Comment comment : comments) {
                    viewComments.add(convertToViewComment(comment));
                }
                Collections.sort(viewComments, sortOrder);
                subscriber.onNext(viewComments);
                subscriber.onCompleted();
            }
        };
    }

    private Comment convertToComment(ViewComment viewComment) {
        Comment.Id id = new Comment.Id("");
        return convertToComment(id, viewComment);
    }

    private Comment convertToComment(Comment.Id id, ViewComment viewComment) {
        String author = viewComment.getAuthor();
        DateTime commentedAt = viewComment.getCommentedAt();
        String message = viewComment.getMessage();
        int votesUp = viewComment.getVotesUp();
        int votesDown = viewComment.getVotesDown();
        return new Comment(id, author, commentedAt, message, votesUp, votesDown);
    }

    private ViewComment convertToViewComment(Comment comment) {
        Comment.Id commentId = comment.getId();
        return convertToViewComment(commentId, comment);
    }

    private ViewComment convertToViewComment(Comment.Id commentId, Comment comment) {
        String id = commentId.toString();
        String author = comment.getAuthor();
        DateTime commentedAt = comment.getCommentedAt();
        String message = comment.getMessage();
        int votesUp = comment.getVotesUp();
        int votesDown = comment.getVotesDown();
        return new ConfirmedComment(id, author, commentedAt, message, votesUp, votesDown);
    }

    public Observable.OnSubscribe<ViewComment> saveVoteFor(final Cure.Id cureId, final ViewComment viewComment, final Vote vote) {
        return new Observable.OnSubscribe<ViewComment>() {
            @Override
            public void call(Subscriber<? super ViewComment> subscriber) {
                Comment comment = convertToComment(viewComment.getId(), viewComment);
                useCase.updateComment(cureId, comment);
                votingUseCase.saveVote(vote, cureId, comment.getId());
                ViewComment confirmedViewComment = convertToViewComment(comment);

                subscriber.onNext(confirmedViewComment);
                subscriber.onCompleted();
            }
        };
    }

    public boolean canVoteOnCommentWith(Cure.Id cureId, Comment.Id commentId) {
        return votingUseCase.canVoteOnCommentWith(cureId, commentId);
    }
}
