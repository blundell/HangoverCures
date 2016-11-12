package com.blundell.hangovercures.archive;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.blundell.hangovercures.Cure;
import com.blundell.hangovercures.free.R;

import java.util.ArrayList;
import java.util.List;

class CureListAdapter extends BaseAdapter {

    private final List<Cure> cures = new ArrayList<>();

    private final LayoutInflater layoutInflater;

    public CureListAdapter(LayoutInflater layoutInflater) {
        this.layoutInflater = layoutInflater;
    }

    public void updateWith(List<Cure> newCures) {
        this.cures.clear();
        this.cures.addAll(newCures);
    }

    @Override
    public int getCount() {
        return cures.size();
    }

    public Cure getItem(int position) {
        return cures.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the small rating bar view and set it from cache (cache is set from DB , DB is set in dialog) ...
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.cure_list_item, parent, false);
            TextView title = (TextView) convertView.findViewById(android.R.id.text1);
            RatingBar ratingBar = (RatingBar) convertView.findViewById(R.id.cure_rating);
            viewHolder = new ViewHolder();
            viewHolder.title = title;
            viewHolder.ratingBar = ratingBar;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Cure cure = getItem(position);
        viewHolder.title.setText(cure.getTitle());
        viewHolder.ratingBar.setRating(cure.getGlobalRating().asInt());

        return convertView;
    }

    static class ViewHolder {
        TextView title;
        RatingBar ratingBar;
    }
}
