package com.blundell.hangovercures.comments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.blundell.hangovercures.Cure;
import com.blundell.hangovercures.HangoverCuresApplication;
import com.blundell.hangovercures.free.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.List;

public class CommentsActivityView extends AppCompatActivity implements CommentsMvp.View, CommentsStream.Listener {

    public static final String EXTRA_CURE = "com.blundell.hangovercures.comments.EXTRA_CURE";

    private CommentsMvp.Presenter presenter;
    private CommentsStream commentsStream;
    private AdView advertWidget;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Cure cure = (Cure) getIntent().getSerializableExtra(EXTRA_CURE);
        HangoverCuresApplication dependencyProvider = (HangoverCuresApplication) getApplicationContext();
        presenter = CommentsPresenter.newInstance(dependencyProvider, this);
        presenter.onCreate(cure);
    }

    @Override
    public void create() {
        setContentView(R.layout.comments_activity);
        commentsStream = (CommentsStream) findViewById(R.id.comments_stream);
        commentsStream.setListener(this);
        advertWidget = (AdView) findViewById(R.id.adView);
    }

    @Override
    public void show(Cure cure) {
        commentsStream.show(cure);
    }

    @Override
    public void show(AdRequest advertRequest) {
        advertWidget.loadAd(advertRequest);
    }

    @Override
    public void show(List<ViewComment> comments) {
        commentsStream.show(comments);
    }

    @Override
    public void showInteractionsFor(final ViewComment comment) {
        // TODO this will be dismised on rotation change
        new AlertDialog.Builder(this)
            .setItems(new String[]{"Up vote", "Down vote"}, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (which == 0) {
                        presenter.onSelectedVoteUp(comment);
                        dialog.dismiss();
                    } else {
                        presenter.onSelectedVoteDown(comment);
                        dialog.dismiss();
                    }
                }
            })
            .show();
    }

    @Override
    public void update(ViewComment comment) {
        commentsStream.update(comment);
    }

    @Override
    public void notifyAlreadyVoted() {
        Snackbar.make(commentsStream, "No voting twice :-(", Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void show(String username) {
        commentsStream.show(username);
    }

    @Override
    public void onCommented(String comment) {
        presenter.onCommented(comment);
    }

    @Override
    public void onSelected(ViewComment comment) {
        presenter.onSelected(comment);
    }

    @Override
    public void onFilterByMostPopularFirst() {
        presenter.onSelectedFilterCommentsByMostPopularFirst();
    }

    @Override
    public void onFilterByNewestFirst() {
        presenter.onSelectedFilterCommentByNewestFirst();
    }
}
