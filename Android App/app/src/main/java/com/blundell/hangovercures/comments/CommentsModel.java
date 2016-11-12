package com.blundell.hangovercures.comments;

import com.blundell.hangovercures.Cure;
import com.blundell.hangovercures.Log;
import com.google.android.gms.ads.AdRequest;

import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class CommentsModel implements CommentsMvp.Model {

    private final CompositeSubscription subscriptions = new CompositeSubscription();

    private final UseCaseModelAdapter modelAdapter;
    private final Log log;

    public CommentsModel(UseCaseModelAdapter modelAdapter, Log log) {
        this.modelAdapter = modelAdapter;
        this.log = log;
    }

    @Override
    public void saveComment(Cure.Id id, ViewComment comment, final CommentSavedCallback callback) {
        Subscription subscription = Observable.create(modelAdapter.saveComment(id, comment))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                new Observer<ViewComment>() {
                    @Override
                    public void onCompleted() {
                        // nothing
                    }

                    @Override
                    public void onError(Throwable e) {
                        // oh well no adverts
                        log.e("Failed in get adverts request", e);
                    }

                    @Override
                    public void onNext(ViewComment comment) {
                        callback.onSaved(comment);
                    }
                }
            );
        subscriptions.add(subscription);
    }

    @Override
    public void loadAdvertRequest(final AdvertRequestLoadedCallback callback) {
        Subscription subscription = Observable.create(modelAdapter.getAdvertRequest())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                new Observer<AdRequest>() {
                    @Override
                    public void onCompleted() {
                        // nothing
                    }

                    @Override
                    public void onError(Throwable e) {
                        // oh well no adverts
                        log.e("Failed in get adverts request", e);
                    }

                    @Override
                    public void onNext(AdRequest adRequest) {
                        callback.onLoaded(adRequest);
                    }
                }
            );
        subscriptions.add(subscription);
    }

    @Override
    public void loadCommentsFor(final Cure cure, final SortOrder sortOrder, final CommentsLoadedCallback callback) {
        Subscription subscription = Observable.create(modelAdapter.getCommentsFor(cure, sortOrder))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                new Observer<List<ViewComment>>() {
                    @Override
                    public void onCompleted() {
                        // nothing
                    }

                    @Override
                    public void onError(Throwable e) {
                        // TODO, fail gracefully
                        log.e("failed to get comments for " + cure.getId(), e);
                    }

                    @Override
                    public void onNext(List<ViewComment> comments) {
                        callback.onLoaded(comments);
                    }
                }
            );
        subscriptions.add(subscription);
    }

    @Override
    public void saveVoteFor(final Cure.Id cureId, ViewComment comment, Vote vote, final CommentUpdatedCallback callback) {
        Subscription subscription = Observable.create(modelAdapter.saveVoteFor(cureId, comment, vote))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                new Observer<ViewComment>() {
                    @Override
                    public void onCompleted() {
                        // nothing
                    }

                    @Override
                    public void onError(Throwable e) {
                        // TODO, fail gracefully
                        log.e("failed to save vote for " + cureId, e);
                    }

                    @Override
                    public void onNext(ViewComment comment) {
                        callback.onUpdated(comment);
                    }
                }
            );
        subscriptions.add(subscription);
    }

    @Override
    public boolean canVoteOnCommentWith(Cure.Id cureId, Comment.Id commentId) {
        return modelAdapter.canVoteOnCommentWith(cureId, commentId);
    }

    @Override
    public void loadUser(UserLoadedCallback callback) {
        callback.onLoaded("anonymous");
    }

    @Override
    public void close() {
        // no-op
    }
}
