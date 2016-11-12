package com.blundell.hangovercures.archive;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.blundell.hangovercures.free.R;

class ExplanationDialog extends AlertDialog {

    public ExplanationDialog(Context context) {
        super(context, android.R.style.Theme_Dialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.explanation_dialog);

        TextView titleView = (TextView) findViewById(R.id.explanation_header_text);
        titleView.setText(getContext().getString(R.string.why_do_hangovers_happen));

        TextView textView = (TextView) findViewById(R.id.explanation_description_text);
        textView.setText(getContext().getString(R.string.why_hangover_happen_desc));
    }
}
