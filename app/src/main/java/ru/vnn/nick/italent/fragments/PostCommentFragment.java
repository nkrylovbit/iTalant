package ru.vnn.nick.italent.fragments;

import android.support.v4.app.Fragment;
import android.widget.ImageView;
import android.widget.TextView;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import ru.vnn.nick.italent.R;

@EFragment(R.layout.fragment_post_comment)
public class PostCommentFragment extends Fragment {
    @ViewById(R.id.iv_comment_author)
    ImageView avatar;

    @ViewById(R.id.tv_comment_username)
    TextView username;

    @ViewById(R.id.tv_time)
    TextView time;

    @ViewById(R.id.tv_comment)
    TextView comment;
}
