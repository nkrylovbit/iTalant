package ru.vnn.nick.italent.fragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import ru.vnn.nick.italent.R;
import ru.vnn.nick.italent.dto.Post;

@EFragment(R.layout.fragment_post_details)
public class PostDetailsFragment extends android.support.v4.app.Fragment {
    private Post post;
    private LayoutInflater inflater;
    private View postInfo;
    private View postComments;

    public static PostDetailsFragment withPost (final Post post) {
        PostDetailsFragment fragment = new PostDetailsFragment();

        fragment.post = post;

        return fragment;
    }

    @AfterViews
    void afterViews () {
        inflater = getLayoutInflater();

//        postInfo = inflater.inflate(R.layout.fragment_post_info,  (ViewGroup) getView());
//        postComments = inflater.inflate(R.layout.fragment_post_comments, (ViewGroup) getView());
    }
}
