package ru.vnn.nick.italent.fragments;

import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import ru.vnn.nick.italent.R;

@EFragment(R.layout.fragment_post_info)
public class PostInfoFragment extends android.support.v4.app.Fragment {
    @ViewById(R.id.iv_post_content)
    ImageView postImage;

    @ViewById(R.id.tv_post_talent)
    TextView talent;

    @ViewById(R.id.tv_post_title)
    TextView description;

    @ViewById(R.id.tv_post_author)
    TextView authorAndTime;

    @ViewById(R.id.iv_post_author)
    ImageView authorImage;

    @ViewById(R.id.tv_comment_count)
    TextView commentCount;

    @ViewById(R.id.tv_like_count)
    TextView likeCount;

//    @ViewById(R.id.ib_like_image)
//    ImageButton like;
}
