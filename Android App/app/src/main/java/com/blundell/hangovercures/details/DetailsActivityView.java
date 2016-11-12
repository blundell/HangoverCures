package com.blundell.hangovercures.details;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.blundell.hangovercures.Cure;
import com.blundell.hangovercures.HangoverCuresApplication;
import com.blundell.hangovercures.comments.CommentsActivityView;
import com.blundell.hangovercures.free.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class DetailsActivityView extends AppCompatActivity implements DetailsMvp.View {

    public static final String EXTRA_CURE = "com.blundell.hangovercures.details.EXTRA_CURE";

    private DetailsMvp.Presenter presenter;
    private TextView titleWidget;
    private TextView descriptionWidget;
    private RatingBar ratingBarWidget;
    private AdView advertWidget;
    private View viewCommentsWidget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Cure cure = (Cure) getIntent().getSerializableExtra(EXTRA_CURE);
        HangoverCuresApplication dependencyProvider = (HangoverCuresApplication) getApplicationContext();
        presenter = DetailsPresenter.newInstance(dependencyProvider, this);
        presenter.onCreate(cure);
    }

    @Override
    public void create() {
        setContentView(R.layout.details_activity);
        advertWidget = (AdView) findViewById(R.id.adView);
        titleWidget = (TextView) findViewById(R.id.cure_title_text);
        descriptionWidget = (TextView) findViewById(R.id.cure_description_text);
        ratingBarWidget = (RatingBar) findViewById(R.id.cure_rating);
        viewCommentsWidget = findViewById(R.id.cure_comments_button);
    }

    @Override
    public void show(final Cure cure) {
        titleWidget.setText(cure.getTitle());
        descriptionWidget.setText(cure.getDescription());
        viewCommentsWidget.setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    presenter.onSelectedViewCommentsFor(cure);
                }
            }
        );
    }

    @Override
    public void show(AdRequest advertRequest) {
        advertWidget.loadAd(advertRequest);
    }

    @Override
    public void show(final CureRating cureRating) {
        ratingBarWidget.setRating(cureRating.getRating());
        ratingBarWidget.setOnRatingBarChangeListener(
            new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                    presenter.onNewRating(rating);
                }
            }
        );
    }

    @Override
    public void navigateToCommentsFor(Cure cure) {
        Intent intent = new Intent(this, CommentsActivityView.class);
        intent.putExtra(CommentsActivityView.EXTRA_CURE, cure);
        startActivity(intent);
    }

    @Override
    public void resumeAdvert() {
        advertWidget.resume();
    }

    @Override
    public void pauseAdvert() {
        advertWidget.pause();
    }

    @Override
    public void destroyAdvert() {
        advertWidget.destroy();
    }
}
