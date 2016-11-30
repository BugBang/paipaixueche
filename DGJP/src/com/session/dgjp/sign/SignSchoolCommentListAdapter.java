package com.session.dgjp.sign;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.session.common.BXBaseAdapter;
import com.session.common.ViewHolder;
import com.session.dgjp.R;
import com.session.dgjp.enity.SignAllComment;
import com.session.dgjp.view.StarBar;

import java.util.List;

/**
 * Created by user on 2016-11-25.
 */
public class SignSchoolCommentListAdapter extends BXBaseAdapter<SignAllComment.SignAllCommentModel> {


    public SignSchoolCommentListAdapter(List<SignAllComment.SignAllCommentModel> listModel, Activity activity) {
        super(listModel, activity);
    }

    @Override
    public View getItemView(int position, View convertView, ViewGroup parent, SignAllComment.SignAllCommentModel model) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.sign_all_comment_item, null);
        }

        StarBar mStarBar = ViewHolder.get(convertView, R.id.comment_start);
        TextView mUserName = ViewHolder.get(convertView, R.id.comment_user_name);
        TextView mCommentContent = ViewHolder.get(convertView, R.id.comment_content);

        if (model != null) {
            mStarBar.setStarMark(model.getScore());
            mUserName.setText(model.getName());
            mCommentContent.setText(model.getComment());
        }
        return convertView;
    }
}
