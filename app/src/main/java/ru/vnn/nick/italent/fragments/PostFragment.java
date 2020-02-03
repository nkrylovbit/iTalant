package ru.vnn.nick.italent.fragments;

import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.vnn.nick.italent.App;
import ru.vnn.nick.italent.R;
import ru.vnn.nick.italent.dto.Post;

import static android.widget.LinearLayout.VERTICAL;

public class PostFragment extends Fragment implements FragmentWithParams,
        SwipeRefreshLayout.OnRefreshListener,
        View.OnScrollChangeListener {
    String TAG = "PostActivity";

    SwipeRefreshLayout mSwipeRefreshLayout;

    public PostFragment() {
    }

    public static Fragment newInstance() {
        return new PostFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        mSwipeRefreshLayout = new SwipeRefreshLayout(getContext());

        mSwipeRefreshLayout.setOnRefreshListener(this);

        ScrollView sv = new ScrollView(getContext());

        sv.setOnScrollChangeListener(this);

        mSwipeRefreshLayout.addView(sv);

        LinearLayout ll = new LinearLayout(getContext());
        ll.setOrientation(VERTICAL);

        App.getPostClient().getAllPosts().enqueue(new Callback<List<Map<String, Object>>>() {
            @Override
            public void onResponse(Call<List<Map<String, Object>>> call, Response<List<Map<String, Object>>> response) {
                if (!response.isSuccessful())
                    return;

                assert response.body() != null;
                for (Map<String, Object> post : response.body()) {
                    View v1 = inflater.inflate(R.layout.news_item_fragment, ll, false);

                    mapPostToView(post, v1);

//                    castTime("nagibator", post.getPublished_at(), v1.findViewById(R.id.tv_post_author));
                    ll.addView(v1);
                }

            }

            @Override
            public void onFailure(Call<List<Map<String, Object>>> call, Throwable t) {
                Toast.makeText(
                        container.getContext(),
                        "Registration error." + t.getCause().getMessage(),
                        Toast.LENGTH_SHORT
                ).show();
                Log.d("TAG", t.getCause().getMessage());
            }
        });

        sv.addView(ll);
        return mSwipeRefreshLayout;
    }

    public void mapPostToView (Map post, View view) {
        final String username = String.valueOf(post.get("userId"));
        Map accountTalent = (Map) post.get("accountTalent");
        Map talent = (Map) accountTalent.get("talent");
        int color = ((Double) accountTalent.get("color")).intValue();
        final String talentName = (String) talent.get("name");
        final String postTitle = (String) post.get("comment");
        final String imageLink = (String) post.get("imageLink");
        final String date = String.valueOf(post.get("createdWhen"));
        castTime(username, date, view.findViewById(R.id.tv_post_author));
//        final String avatar = (String) post.get()

        ((TextView) view.findViewById(R.id.tv_post_talent)).setText(talentName);
        ((TextView) view.findViewById(R.id.tv_post_talent)).getBackground().setColorFilter(color , PorterDuff.Mode.SRC_ATOP);
        ((TextView) view.findViewById(R.id.tv_post_title)).setText(postTitle);
//        ((TextView) view.findViewById(R.id.tv_post_author)).setText(username);
        if (imageLink != null) {
            Picasso.get().load(App.beUrl() + imageLink).into(((ImageView) view.findViewById(R.id.iv_post)));
        }

    }

    @Override
    public void onRefresh() {
        Log.d(TAG, "hello");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Отменяем анимацию обновления
                mSwipeRefreshLayout.setRefreshing(false);

            }
        }, 3000);
    }

    @Override
    public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

        double wow = (double) scrollY / v.getHeight();

        if (v.canScrollVertically(1)) {
            mSwipeRefreshLayout.setRefreshing(false);
        } else {
            mSwipeRefreshLayout.setRefreshing(true);
        }


        Log.d(TAG, String.format("%b, %d / %d = %f", v.canScrollVertically(1), scrollY, v.getHeight(), wow));
    }

    private void castTime (final String prefix, final String time, final TextView view) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        PrettyTime prettyTime = new PrettyTime(Locale.getDefault());
        try {
            Date date = format.parse(time);
            String ago = prettyTime.format(date);
            view.setText(String.join(",\n", prefix, ago));
        } catch (ParseException e) {
            view.setText(String.join(",\n", prefix, time));
            e.printStackTrace();
        }
    }

    @Override
    public void provideParams(Map params) {

    }
}
