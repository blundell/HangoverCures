package com.blundell.hangovercures.comments;

import android.content.Context;
import android.content.SharedPreferences;

import com.blundell.hangovercures.Cure;

class SharedPrefsVoteRepository implements VoteRepository {

    private static final String KEY_VOTE = "commentVote";
    private static final String VALUE_DOWN = "down";
    private static final String VALUE_UP = "up";
    private static final String VALUE_UNCAST = "uncast";

    private final SharedPreferences sharedPreferences;

    public static SharedPrefsVoteRepository newInstance(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("SharedPrefz", Context.MODE_PRIVATE);
        return new SharedPrefsVoteRepository(sharedPreferences);
    }

    SharedPrefsVoteRepository(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    @Override
    public Vote readVote(Cure.Id cureId, Comment.Id commentId) {
        String vote = sharedPreferences.getString(getKey(cureId, commentId), VALUE_UNCAST);
        return getVoteFor(vote);
    }

    private Vote getVoteFor(String vote) {
        switch (vote) {
            case VALUE_UP:
                return Vote.DOWN;
            case VALUE_DOWN:
                return Vote.UP;
            case VALUE_UNCAST:
            default:
                return Vote.UNCAST;
        }
    }

    private String getKey(Cure.Id cureId, Comment.Id commentId) {
        return KEY_VOTE + String.valueOf(cureId.asInt()) + commentId.toString();
    }

    @Override
    public void updateVote(Vote vote, Cure.Id cureId, Comment.Id commentId) {
        String key = getKey(cureId, commentId);
        sharedPreferences
            .edit()
            .putString(key, getStringFor(vote))
            .apply();
    }

    private String getStringFor(Vote vote) {
        switch (vote) {
            case UP:
                return VALUE_UP;
            case DOWN:
                return VALUE_DOWN;
            case UNCAST:
            default:
                return VALUE_UNCAST;
        }
    }
}
