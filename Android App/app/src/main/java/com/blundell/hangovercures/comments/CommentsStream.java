package com.blundell.hangovercures.comments;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.blundell.hangovercures.Cure;
import com.blundell.hangovercures.free.R;

import java.util.List;

public class CommentsStream extends LinearLayout {
    private TextView cureTitleWidget;
    private View commentSendWidget;
    private EditText commentInputWidget;
    private CommentListAdapter commentListAdapter;
    private ListView commentsListWidget;
    private String username;
    private Spinner filterWidget;

    public CommentsStream(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CommentsStream(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        // Have comment replies like clover / youtube
        // Have a show more (paginate at 25 comments)
        setOrientation(VERTICAL);
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.comments_view, this);
        cureTitleWidget = (TextView) view.findViewById(R.id.comments_cure_title_label);
        commentInputWidget = (EditText) view.findViewById(R.id.comments_user_input_edit_text);
        commentSendWidget = view.findViewById(R.id.comments_user_input_send_button);
        commentsListWidget = (ListView) view.findViewById(R.id.comments_stream_list);
        commentListAdapter = new CommentListAdapter(inflater);
        commentsListWidget.setAdapter(commentListAdapter);
        filterWidget = (Spinner) view.findViewById(R.id.comments_filter_spinner);
    }

    public void setListener(final Listener listener) {
        commentSendWidget.setOnClickListener(
            new OnClickListener() {
                @Override
                public void onClick(View v) {
                    String commentText = commentInputWidget.getText().toString();
                    commentInputWidget.setText("");
                    listener.onCommented(commentText);
                }
            }
        );
        commentsListWidget.setOnItemClickListener(
            new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    listener.onSelected(((CommentListAdapter) parent.getAdapter()).getItem(position));
                }
            }
        );
        filterWidget.setOnItemSelectedListener(
            new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (position == 0) {
                        listener.onFilterByMostPopularFirst();
                    } else {
                        listener.onFilterByNewestFirst();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // never called
                }
            }
        );
    }

    public void show(Cure cure) {
        cureTitleWidget.setText(cure.getTitle());
    }

    public void show(String username) {
        this.username = username;
        // TODO could load the users profile pic here
    }

    public void show(List<ViewComment> comments) {
        commentListAdapter.updateWith(comments);
        commentListAdapter.notifyDataSetChanged();
        commentsListWidget.smoothScrollToPosition(0);
    }

    public void update(ViewComment comment) {
        commentListAdapter.updateWith(comment);
        commentListAdapter.notifyDataSetChanged();
    }

    interface Listener {
        void onCommented(String comment);

        void onSelected(ViewComment comment);

        void onFilterByMostPopularFirst();

        void onFilterByNewestFirst();
    }
}
