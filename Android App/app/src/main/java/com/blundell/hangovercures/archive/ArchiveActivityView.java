package com.blundell.hangovercures.archive;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.blundell.hangovercures.Cure;
import com.blundell.hangovercures.HangoverCuresApplication;
import com.blundell.hangovercures.details.DetailsActivityView;
import com.blundell.hangovercures.free.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.List;

import org.joda.time.DateTime;

public class ArchiveActivityView extends AppCompatActivity implements ArchiveMvp.View, ArchiveList.Listener {

    private static final int SHOW_NEW_YEAR_DIALOG = -5;

    private ArchiveMvp.Presenter presenter;
    private ArchiveList listWidget;
    private AdView advertWidget;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        HangoverCuresApplication dependencyProvider = (HangoverCuresApplication) getApplicationContext();
        this.presenter = ArchivePresenter.newInstance(dependencyProvider, this);
        presenter.onCreate();
    }

    @Override
    public void create() {
        setContentView(R.layout.archive_activity);
        listWidget = (ArchiveList) findViewById(R.id.listView);
        listWidget.setSelectionListener(this);
        advertWidget = (AdView) findViewById(R.id.adView);

        DateTime dateNow = DateTime.now();
        if (dateNow.getMonthOfYear() == 1 && dateNow.getDayOfMonth() == 1) { // TODO this needs to be moved into our model
            showDialog(SHOW_NEW_YEAR_DIALOG);
        }
    }

    @Override
    public void onSelectedWhyDoHangoversHappen() {
        showDialog(2); // TODO go through presenter
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == SHOW_NEW_YEAR_DIALOG) {
            return new NewYearDialog(this);
        } else {
            return new ExplanationDialog(this);
        }
    }

    @Override
    public void onSelected(Cure cure) {
        presenter.onSelected(cure);
    }

    @Override
    public void navigateToDetailsFor(Cure cure) {
        Intent intent = new Intent(this, DetailsActivityView.class);
        intent.putExtra(DetailsActivityView.EXTRA_CURE, cure); // TODO once we have a local database just pass the ID & do a lookup
        startActivity(intent);
    }

    @Override
    public void show(List<Cure> hangoverCures) {
        listWidget.updateWith(hangoverCures);
    }

    @Override
    public void show(AdRequest advertRequest) {
        advertWidget.loadAd(advertRequest);
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.onResume();
    }

    @Override
    public void resumeAdvert() {
        advertWidget.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.onPause();
    }

    @Override
    public void pauseAdvert() {
        advertWidget.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }

    @Override
    public void destroyAdvert() {
        advertWidget.destroy();
    }
}
