package com.blundell.hangovercures.comments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.blundell.hangovercures.free.R;

import java.util.ArrayList;
import java.util.List;

class CommentListAdapter extends BaseAdapter {

    private final List<ViewComment> comments = new ArrayList<>();

    private final LayoutInflater layoutInflater;
    private final CommentTimeStampFormatter commentTimeStampFormatter;

    public CommentListAdapter(LayoutInflater layoutInflater) {
        this.layoutInflater = layoutInflater;
        commentTimeStampFormatter = new CommentTimeStampFormatter();
    }

    public void updateWith(List<ViewComment> newComments) {
        this.comments.clear();
        this.comments.addAll(newComments);
    }

    public void updateWith(ViewComment comment) {
        int position = -1;
        for (int i = 0; i < comments.size(); i++) {
            ViewComment listComment = comments.get(i);
            if (listComment.isEqualTo(comment)) {
                position = i;
            }
        }
        if (position == -1) {
            comments.add(0, comment);
        } else {
            comments.remove(position);
            comments.add(position, comment);
        }
    }

    @Override
    public int getCount() {
        return comments.size();
    }

    @Override
    public ViewComment getItem(int position) {
        return comments.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.comment_list_item, parent, false);
            TextView author = (TextView) convertView.findViewById(R.id.comment_author);
            TextView time = (TextView) convertView.findViewById(R.id.comment_time);
            TextView message = (TextView) convertView.findViewById(R.id.comment_text);
            TextView votes = (TextView) convertView.findViewById(R.id.comment_votes);
            viewHolder = new ViewHolder();
            viewHolder.author = author;
            viewHolder.time = time;
            viewHolder.message = message;
            viewHolder.votes = votes;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        ViewComment comment = getItem(position);
        if (comment.isConfirmed()) {
            int confirmedColor = parent.getResources().getColor(android.R.color.white);
            viewHolder.author.setTextColor(confirmedColor);
            viewHolder.time.setTextColor(confirmedColor);
            viewHolder.message.setTextColor(confirmedColor);
            viewHolder.votes.setTextColor(confirmedColor);
        } else {
            int tempColor = parent.getResources().getColor(android.R.color.darker_gray);
            viewHolder.author.setTextColor(tempColor);
            viewHolder.time.setTextColor(tempColor);
            viewHolder.message.setTextColor(tempColor);
            viewHolder.votes.setTextColor(tempColor);
        }
        viewHolder.author.setText(comment.getAuthor());
        viewHolder.time.setText(commentTimeStampFormatter.format(comment.getCommentedAt()));
        viewHolder.message.setText(comment.getMessage());
        viewHolder.votes.setText(getTemplateText(parent, R.string.comment_votes_up_down, comment.getVotesUp(), comment.getVotesDown()));

        return convertView;
    }

    private String getTemplateText(ViewGroup parent, int template, Object... params) {
        return parent.getContext().getString(template, params);
    }

    static class ViewHolder {
        TextView author;
        TextView time;
        TextView message;
        TextView votes;
    }
}
