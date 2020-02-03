package ru.vnn.nick.italent;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.vnn.nick.italent.dto.Post;
import ru.vnn.nick.italent.dto.User;
import ru.vnn.nick.italent.dto.UserJSON;
import ru.vnn.nick.italent.listeners.AppBarStateChangeListener;
import ru.vnn.nick.italent.listeners.NextRevisionListener;
import ru.vnn.nick.italent.fragments.FragmentWithParams;

public class HomeFragment extends Fragment implements FragmentWithParams {
    private ImageView background;
    private ConstraintLayout avatar;
    private TextView username;
    private TextView name;
    private TextView description;
    private TextView postCount;
    private TextView followerCount;
    private TextView followingCount;
    private TextView exclusive;
    private LinearLayout postsLayout;
    private LinearLayout followerLayout;
    private LinearLayout followingLayout;
    private LinearLayout exclusiveLayout;
    private FrameLayout ratioLayout;
    private ImageView subscribeBtn;
    private ImageView backArrowBtn;
    private ImageView notificationBtn;
    private ImageView messageBtn;
    private ImageView prefsBtn;
    private ImageView donateBtn;
    private ImageView selected;

    private Map<String, String> params;

    private LinearLayout activeLayout;

    private User user;

    private long uid;

    public HomeFragment () {}

    public static Fragment newInstance (final long uid) {
        HomeFragment fragment = new HomeFragment();
        fragment.uid = uid;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_home, container, false);


        background = view.findViewById(R.id.iv_bg_profile);
        avatar = view.findViewById(R.id.cl_avatar_profile);
        username = view.findViewById(R.id.username_on_profile_tv);
        name = view.findViewById(R.id.tv_name_profile);
        description = view.findViewById(R.id.tv_desc_profile);
        postCount = view.findViewById(R.id.post_count);
        followerCount = view.findViewById(R.id.follower_count);
        followingCount = view.findViewById(R.id.following_count);
        exclusive = view.findViewById(R.id.exclusive_count);
        ratioLayout = view.findViewById(R.id.fl_ratio);
        subscribeBtn = view.findViewById(R.id.subscribe_btn);
        backArrowBtn = view.findViewById(R.id.back_arrow_btn);
        notificationBtn = view.findViewById(R.id.notification_btn);
        messageBtn = view.findViewById(R.id.message_btn);
        donateBtn = view.findViewById(R.id.donate_btn);
        prefsBtn = view.findViewById(R.id.preferences_btn);
        selected = view.findViewById(R.id.selected_profile_item);

        postsLayout = view.findViewById(R.id.posts_layout);
        followerLayout = view.findViewById(R.id.followers_layout);
        followingLayout = view.findViewById(R.id.following_layout);
        exclusiveLayout = view.findViewById(R.id.exclusive_layout);

        activeLayout = postsLayout;

        selected.setX(postsLayout.getX());

        View.OnClickListener listener = new NextRevisionListener();
//        prefsBtn.setOnClickListener(listener);
        notificationBtn.setOnClickListener(listener);
        donateBtn.setOnClickListener(listener);
        messageBtn.setOnClickListener(listener);
        ratioLayout.setOnClickListener(listener);
        exclusiveLayout.setOnClickListener(listener);

        ((AppBarLayout) view.findViewById(R.id.abl)).addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                if (State.COLLAPSED.equals(state)) {
                    Log.d("TAG",
                        "FRAME SIZE: " + view.getWidth() + "x" + view.getHeight());
                    Log.d("TAG",
                            "SCREEN SIZE: "
                                    + getActivity().getWindow().getWindowManager().getDefaultDisplay().getWidth()
                                    + "x"
                                    + getActivity().getWindow().getWindowManager().getDefaultDisplay().getHeight());
                    username.setTextColor(getResources().getColor(R.color.searchTextActiveColor));
                    view.findViewById(R.id.fragment_profile_appbar_hsv_talents).setVisibility(View.GONE);
                } else if (State.EXPANDED.equals(state)) {
                    if (activeLayout.equals(postsLayout)) {
                        view.findViewById(R.id.fragment_profile_appbar_hsv_talents).setVisibility(View.VISIBLE);
                    }
                    Log.d("TAG",
                        "FRAME SIZE: " + view.getWidth() + "x" + view.getHeight());
                    Log.d("TAG",
                            "SCREEN SIZE: "
                                    + getActivity().getWindow().getWindowManager().getDefaultDisplay().getWidth()
                                    + "x"
                                    + getActivity().getWindow().getWindowManager().getDefaultDisplay().getHeight());
                    username.setTextColor(getResources().getColor(R.color.white));
                }
            }
        });



//        if (params == null) {
//            getUser("s.kasatkin92@gmail.com", view);
//        } else {
//            getUser(params.get("username"), view);
//        }

        getUser(view);

        if (false) {
            backArrowBtn.setVisibility(View.INVISIBLE);
            subscribeBtn.setVisibility(View.INVISIBLE);
            messageBtn.setVisibility(View.GONE);
            donateBtn.setVisibility(View.GONE);
            notificationBtn.setVisibility(View.VISIBLE);
            prefsBtn.setVisibility(View.VISIBLE);
            prefsBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("TAAAAAAG", view.findViewById(R.id.iv_bg_profile).getWidth()
                            + "x" + view.findViewById(R.id.iv_bg_profile).getHeight());
                    // go to preferences activity
                }
            });
        } else {
            backArrowBtn.setVisibility(View.VISIBLE);
            subscribeBtn.setVisibility(View.VISIBLE);
            messageBtn.setVisibility(View.VISIBLE);
            donateBtn.setVisibility(View.VISIBLE);
            notificationBtn.setVisibility(View.GONE);
            prefsBtn.setVisibility(View.GONE);
            backArrowBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    getUser(1, view);
//                    getPosts(inflater, view);
                    // back to origin profile
                }
            });

            subscribeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Add or remove subscribe
                }
            });
        }

        getPosts(inflater, view);

        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) view.findViewById(R.id.profile_toolbar).getLayoutParams();
//        getActivity().getWindow().getWindowManager().getDefaultDisplay().getHeight();
        lp.height = getActivity().getWindow().getWindowManager().getDefaultDisplay().getHeight()
            - (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                59,
                getResources().getDisplayMetrics());

        Log.d("TAAAAAAG", view.findViewById(R.id.iv_bg_profile).getWidth()
                + "x" + view.findViewById(R.id.iv_bg_profile).getHeight());
        return view;
    }

    private void getUser (final View view) {
        App.getUserClient().getMyUser().enqueue(new Callback<UserJSON>() {
            @Override
            public void onResponse(Call<UserJSON> call, Response<UserJSON> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(view.getContext(), "Response error, please try again", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (response.body() == null) {
                    Toast.makeText(view.getContext(), "Server error, please try again", Toast.LENGTH_SHORT).show();
                    return;
                }

                mappingUserToProfileView(response.body(), view);

            }

            @Override
            public void onFailure(Call<UserJSON> call, Throwable t) {
                Toast.makeText(view.getContext(), "Network error, please try again", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void getUser (final String username, final View view) {

        App.getUserClient().getUserByUsername(username).enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                if (!response.isSuccessful())
                    return;

                if (response.body() == null) {
                    Toast.makeText(view.getContext(), "Error.", Toast.LENGTH_SHORT).show();
                    return;
                }

                user = response.body();

                mappingUserToProfileView(user, view);
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                Toast.makeText(view.getContext(), "Network Error.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void mappingUserToProfileView(UserJSON user, View view) {
        ((TextView) view.findViewById(R.id.username_on_profile_tv)).setText(user.getUsername());
        String name = "";
        if (user.getFirstname() != null) {
            name += user.getFirstname();
        }

        if (user.getLastname() != null) {
            name += user.getLastname();
        }

        ((TextView) view.findViewById(R.id.tv_name_profile)).setText(name);

        if (!name.equals("")) {
            view.findViewById(R.id.tv_name_profile).setVisibility(View.VISIBLE);
        }

        if (user.getDescription() == null) {
            view.findViewById(R.id.tv_desc_profile).setVisibility(View.INVISIBLE);
        } else {
            ((TextView) view.findViewById(R.id.tv_desc_profile)).setText(user.getDescription());
        }

        ((TextView) view.findViewById(R.id.post_count)).setText(String.valueOf(user.getPostcounts()));
        ((TextView) view.findViewById(R.id.follower_count)).setText(String.valueOf(user.getFollowers()));
        ((TextView) view.findViewById(R.id.following_count)).setText(String.valueOf(user.getFollowing()));

        if (user.getBackgroundLink() != null &&
                !("null").equals(user.getBackgroundLink()) &&
                !user.getBackgroundLink().isEmpty()) {
            Picasso.get().load(user.getBackgroundLink()).into(background);
        }

        if (user.getAvatarLink() != null &&
                !("null").equals(user.getAvatarLink()) &&
                !user.getAvatarLink().isEmpty()) {
            Picasso.get().load(user.getAvatarLink()).into(new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    avatar.setBackground(new BitmapDrawable(bitmap));
                }

                @Override
                public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            });
        }
    }

    private void mappingUserToProfileView(User user, View view) {
        username.setText(user.getUsername());
        if (user.getFirstName() == null && user.getLastName() == null) {
            name.setVisibility(View.INVISIBLE);
        }
        name.setText(String.join("\t", user.getFirstName(), user.getLastName()));
        if (user.getDescription().isEmpty()) {
            description.setVisibility(View.INVISIBLE);
        }
        description.setText(user.getDescription());
        postCount.setText(String.valueOf(user.getPostCount()));
        followerCount.setText(String.valueOf(user.getFollowersCount()));
        followingCount.setText(String.valueOf(user.getFollowingCount()));

        if (user.getBackgroundUrl() != null &&
                !("null").equals(user.getBackgroundUrl()) &&
                !user.getBackgroundUrl().isEmpty()) {
            Picasso.get().load(user.getBackgroundUrl()).into(background);
        }

        if (user.getAvararUrl() != null &&
                !("null").equals(user.getAvararUrl()) &&
                !user.getAvararUrl().isEmpty()) {
            Picasso.get().load(user.getAvararUrl()).into(new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    avatar.setBackground(new BitmapDrawable(bitmap));
                }

                @Override
                public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            });
        }
    }

    public void getPosts (LayoutInflater inflater, View view) {
        final List<String> talents = new ArrayList<>();

        LinearLayout rv = view.findViewById(R.id.rv_profile);

        App.getPostClient().getPosts("test").enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if (!response.isSuccessful())
                    return;

                assert response.body() != null;
                for (Post post : response.body()) {
                    View v1 = inflater.inflate(R.layout.news_item_fragment, rv, false);
                    if (post.getPostImage() != null) {

                        Picasso
                                .get()
                                .load((getString(R.string.server_url) + post.getPostImage()).trim())
                                .into((ImageView) v1.findViewById(R.id.iv_post));
                        ((TextView) v1.findViewById(R.id.tv_post_talent)).setText(post.getCategory());
                        talents.add(post.getCategory());
                        ((TextView) v1.findViewById(R.id.tv_post_title)).setText(post.getMessage());
                        ((TextView) v1.findViewById(R.id.tv_post_author)).setText(post.getPublished_at());
//                        Picasso.get().load(R.drawable.not_liked).into((ImageButton) v1.findViewById(R.id.ib_like_image));
//                        ((ImageButton) v1.findViewById(R.id.ib_like_image))
//                                .setBackgroundTintList(getResources().getColorStateList(R.color.primaryPink));
                        ((TextView) v1.findViewById(R.id.tv_comment_count)).setText(String.valueOf(post.getCommentsCount()));
                        ((TextView) v1.findViewById(R.id.tv_like_count)).setText(String.valueOf(post.getLikesCount()));
                        v1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // go to post view
                            }
                        });
                        rv.addView(v1);
                    }
                }

            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                Toast.makeText(
                        getContext(),
                        "Registration error." + t.getCause().getMessage(),
                        Toast.LENGTH_SHORT
                ).show();
                Log.d("TAG", t.getCause().getMessage());
            }
        });


        addTalentsToView (talents, view);
    }

    private void addTalentsToView(List<String> talents, View view) {
        LinearLayout ll = view.findViewById(R.id.fragment_home_appbar_ll_talents);
        for (String talent: talents) {
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void provideParams(Map params) {
        this.params = params;
    }
}
