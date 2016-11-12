package com.blundell.hangovercures.archive;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.blundell.hangovercures.free.R;

class NewYearDialog extends AlertDialog {

    public NewYearDialog(Context context) {
        super(context, android.R.style.Theme_Dialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.explanation_dialog);

        TextView titleView = (TextView) findViewById(R.id.new_year_title_text);
        titleView.setText(getContext().getString(R.string.new_year_title));

        TextView textView = (TextView) findViewById(R.id.new_year_message_text);
        textView.setText(getContext().getString(R.string.new_year_desc));
    }
}
