package com.blundell.hangovercures.archive;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.blundell.hangovercures.Cure;
import com.blundell.hangovercures.free.R;

import java.util.List;

public class ArchiveList extends ListView {

    private CureListAdapter listAdapter;

    public ArchiveList(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ArchiveList(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        LayoutInflater inflater = LayoutInflater.from(getContext());
        listAdapter = new CureListAdapter(inflater);
        setAdapter(listAdapter);
        TextView title = (TextView) inflater.inflate(R.layout.cure_list_header, this, false);
        title.setText(R.string.why_do_hangovers_happen);
        addHeaderView(title);
        setHeaderDividersEnabled(true);
    }

    public void setSelectionListener(final Listener listener) {
        setOnItemClickListener(
            new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (position == 0) {
                        listener.onSelectedWhyDoHangoversHappen();
                    } else {
                        listener.onSelected(listAdapter.getItem(position - 1));
                    }
                }
            }
        );
    }

    public void updateWith(List<Cure> cures) {
        listAdapter.updateWith(cures);
        listAdapter.notifyDataSetChanged();
    }

    interface Listener {
        void onSelectedWhyDoHangoversHappen();

        void onSelected(Cure cure);
    }
}
