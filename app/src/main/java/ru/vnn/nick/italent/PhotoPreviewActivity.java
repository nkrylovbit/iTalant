package ru.vnn.nick.italent;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.androidannotations.annotations.AfterExtras;
import org.androidannotations.annotations.AfterTextChange;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.FocusChange;
import org.androidannotations.annotations.KeyDown;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.vnn.nick.italent.activities.MainActivity;
import ru.vnn.nick.italent.dto.Talent;
import ru.vnn.nick.italent.fragments.TalentsFragment;
import ru.vnn.nick.italent.fragments.TalentsFragment_;
import ru.vnn.nick.italent.view.drawable.utils.FastBlur;

@SuppressWarnings("ConstantConditions")
@EActivity(R.layout.activity_photo_preview)
public class PhotoPreviewActivity extends AppCompatActivity implements View.OnClickListener {

    @ViewById(R.id.media_preview)
    ImageView photoView;

    @ViewById(R.id.talents_list)
    LinearLayout talentsList;

    @ViewById(R.id.post_preview)
    FrameLayout postPreview;

    @ViewById(R.id.post_title_preview)
    EditText title;

    @ViewById(R.id.talent_choicer)
    TextView talentChoiser;

    @ViewById(R.id.tv_new_talent)
    TextView newTalent;

    @Extra
    Uri photo;

    @ViewById(R.id.preview_frame)
    FrameLayout previewFrame;

    @ViewById(R.id.button_send_post)
    TextView send;

    FrameLayout talentsFragment;

    View preview;
    ImageView backgroundPreview;
    TextView postTalent;
    TextView postTitle;
    TextView postInfo;
    ImageView postAuthorImage;

    SparseArray<Talent> talents = new SparseArray<>();
    Map<Integer, ColorFilter> talent2color = new HashMap<>();

    @AfterViews
    void afterViews () {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setStatusBarColor(getColor(R.color.overlay70percent));
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

//        Talent programming = new Talent();
//        programming.setName("Programming");
//        programming.setColor(getColor(R.color.primaryPink));
//
//        talentsList.addView(generateNewTalent(programming));

        TextView talent = new TextView(this);
        talent.setLayoutParams(
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                )
        );
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) talent.getLayoutParams();
        lp.rightMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());
        talent.setTypeface(getResources().getFont(R.font.sfuitextlight));
        talent.setTextColor(getColor(R.color.white));
        talent.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        talent.setText("Programming");
        int padding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics());
        talent.setPadding(padding,0, padding, 0);
        talent.setBackground(getDrawable(R.drawable.rectangle_talent_label));
        talent.getBackground().setColorFilter(getColor(R.color.primaryPink), PorterDuff.Mode.SRC_ATOP);
        talent.setOnClickListener(this::onClick);

        TextView talent2 = new TextView(this);
        talent2.setLayoutParams(
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                )
        );
        LinearLayout.LayoutParams lp2 = (LinearLayout.LayoutParams) talent2.getLayoutParams();
        lp2.rightMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());
        talent2.setTypeface(getResources().getFont(R.font.sfuitextlight));
        talent2.setTextColor(getColor(R.color.white));
        talent2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        talent2.setText("Guitar");
        talent2.setPadding(padding,0, padding, 0);
        talent2.setBackground(getDrawable(R.drawable.rectangle_talent_label));
        talent2.getBackground().setColorFilter(getColor(R.color.test), PorterDuff.Mode.SRC_ATOP);
        talent2.setOnClickListener(this::onClick);

        talentsList.addView(talent);
        talentsList.addView(talent2);

        postPreview.addView(preview);
        postInfo.setText("nick, just now");
        postTitle.setText("");
        postTitle.setVisibility(View.INVISIBLE);
        postTalent.setVisibility(View.INVISIBLE);

    }

    @AfterExtras
    void afterExtras () {
        if (photo == null) {
            finish();
            return;
        }

        preview = getLayoutInflater().inflate(R.layout.news_item_fragment, postPreview, false);
        backgroundPreview = preview.findViewById(R.id.iv_post);
        postTalent = preview.findViewById(R.id.tv_post_talent);
        postTitle = preview.findViewById(R.id.tv_post_title);
        postInfo = preview.findViewById(R.id.tv_post_author);
        postAuthorImage = preview.findViewById(R.id.iv_post_author);

        backgroundPreview.setImageURI(photo);
    }

    private void uploadPost (String mediaContentUrl) {
        if (mediaContentUrl == null) {
            return;
        }

        Log.d("USER ID:", App.getUserId(this).toString());
        Map<String, Object> params = new HashMap<>();

        params.put("message", postTitle.getText().toString());
        params.put("category", postTalent.getText().toString());
        params.put("post_image", mediaContentUrl);
        params.put("author_id", App.getUserId(this));
//        params.put("category_color", ((ColorDrawable) postTalent.getBackground()).getColor());

        App
                .getPostClient()
                .uploadPost(params)
                .enqueue(new Callback<Map>() {
                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(
                                    getApplicationContext(),
                                    "Post uploaded.",
                                    Toast.LENGTH_SHORT
                            ).show();

                            // goto main menu
                            App.switchActivity(PhotoPreviewActivity.this, MainActivity.class);
                        } else {
                            Toast.makeText(
                                    getApplicationContext(),
                                    "Posting error.",
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call call, @NonNull Throwable t) {
                        Log.d("TAGABLE2", t.getMessage());

                        Toast.makeText(
                                getApplicationContext(),
                                "Network error.",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                });

    }

    private void uploadFile (File mediaFile) {
        // create RequestBody instance from file
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), mediaFile);

        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("file", mediaFile.getName(), requestFile);

        RequestBody desc = RequestBody.create(MediaType.parse("multipart/form-data"), "description");
        App
                .getPostClient()
                .uploadMedia(body, desc)//requestFile)
                .enqueue(new Callback<Map<String, Object>>() {
                    @Override
                    public void onResponse(
                            @NonNull Call<Map<String, Object>> call,
                            @NonNull Response<Map<String, Object>> response) {

                        if (response.isSuccessful()) {

                            assert response.body() != null;

                            uploadPost((String) response.body().get("url"));
                        } else {
                            Toast.makeText(
                                    getApplicationContext(),
                                    "File uploading error.",
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Map<String, Object>> call, @NonNull Throwable t) {
                        Log.d("TAGABLE", t.getMessage());

                        Toast.makeText(
                                getApplicationContext(),
                                "Network error.",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                });
    }

    @Override
    public void onClick(View v) {
        TextView talent = (TextView) v;

        while (talent2color.keySet().iterator().hasNext()) {
            int tv = talent2color.keySet().iterator().next();

//            findViewById(tv).getBackground().setColorFilter(talent2color.get(tv));
        }

        talent2color.clear();


        if (postTalent.getVisibility() == View.VISIBLE) {
            postTalent.setVisibility(View.INVISIBLE);
        }

        if (talent.getText().equals(postTalent.getText())) {
            postTalent.setText("");
            return;
        }

        postTalent.setText(talent.getText());
        postTalent.getBackground().setColorFilter(talent.getBackground().getColorFilter());

        for (int i = 0; i < talentsList.getChildCount(); i++) {
            if (talentsList.getChildAt(i).equals(talent)) {
                continue;
            }

//            talent2color.put(talentsList.getChildAt(i).getId(), talentsList.getChildAt(i).getBackground().getColorFilter());

//            talentsList.getChildAt(i).getBackground().setColorFilter(getColor(R.color.colorPrimaryDark), PorterDuff.Mode.SRC_ATOP);
        }

        postTalent.setVisibility(View.VISIBLE);
    }

    @FocusChange(R.id.post_title_preview)
    void editTitle (boolean hasFocus) {
        if (getString(R.string.input_comment).equals(title.getText().toString())) {
            title.setText("");
        } else if ("".equals(title.getText().toString())) {
            title.setText(getString(R.string.input_comment));
        }

        if (hasFocus) {
            title.setTextColor(getColor(R.color.white));
        } else {
            title.setTextColor(getColor(R.color.searchTextColor));
        }
    }

    @AfterTextChange(R.id.post_title_preview)
    void onTypeTitle () {
        if (!getString(R.string.input_comment).equals(title.getText().toString())) {
            postTitle.setText(title.getText().toString());
            postTitle.setVisibility(View.VISIBLE);
        } else {
            postTitle.setVisibility(View.INVISIBLE);
        }
    }

    @SuppressWarnings("deprecation")
    @Click(R.id.tv_new_talent)
    void openNewTalent () {

        loadFragment(new TalentsFragment_(), R.id.talents_searcher);
        talentsFragment = findViewById(R.id.talents_searcher);


        talentsList.setVisibility(View.INVISIBLE);
        talentChoiser.setVisibility(View.INVISIBLE);
        newTalent.setVisibility(View.INVISIBLE);

        previewFrame.setDrawingCacheEnabled(true);

        previewFrame.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_LOW);

        previewFrame.buildDrawingCache(true);

        Bitmap b = Bitmap.createBitmap(previewFrame.getDrawingCache());

        blur(b, talentsFragment, true);

        talentsFragment.setAlpha(0);
        talentsFragment.setVisibility(View.VISIBLE);
        talentsFragment
                .animate()
                .alpha(1f)
                .setDuration(300)
                .setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                previewFrame.setVisibility(View.GONE);
            }
        }).start();

        previewFrame.setDrawingCacheEnabled(false);
        previewFrame.destroyDrawingCache();
    }

    private void blur(Bitmap bkg, View view, boolean downScale) {
        long startMs = System.currentTimeMillis();
        float scaleFactor = 1;
        float radius = 20;

        if (downScale) {
            scaleFactor = 20;
            radius = 20;
        }

        int width = getWindowManager().getDefaultDisplay().getWidth();
        int height = getWindowManager().getDefaultDisplay().getHeight();
        Bitmap overlay = Bitmap.createBitmap(
                (int) (width / scaleFactor),
                (int) (height / scaleFactor),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(overlay);
        canvas.translate(-view.getLeft()/scaleFactor, -view.getTop()/scaleFactor);
        canvas.scale(1 / scaleFactor, 1 / scaleFactor);
        Paint paint = new Paint();
        paint.setFlags(Paint.FILTER_BITMAP_FLAG);
        canvas.drawBitmap(bkg, 0, 0, paint);

        overlay = FastBlur.doBlur(overlay, (int)radius, true);
        view.setBackground(new BitmapDrawable(getResources(), overlay));
    }

    @KeyDown(KeyEvent.KEYCODE_BACK)
    void unAnimate () {
        if (previewFrame.getVisibility() == View.VISIBLE) {
            finish();
            return;
        }

        previewFrame.setAlpha(1);
        previewFrame.setVisibility(View.VISIBLE);
        talentsFragment
                .animate()
                .alpha(0f)
                .setDuration(300)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        talentsFragment.setVisibility(View.GONE);
                    }
                }).start();

        talentsList.setAlpha(0);
        talentsList.setVisibility(View.VISIBLE);
        hideToVisibleAnimate(talentsList).start();

        talentChoiser.setAlpha(0);
        talentChoiser.setVisibility(View.VISIBLE);
        hideToVisibleAnimate(talentChoiser).start();

        newTalent.setAlpha(0);
        newTalent.setVisibility(View.VISIBLE);
        hideToVisibleAnimate(newTalent).start();
    }

    ViewPropertyAnimator hideToVisibleAnimate (View v) {
        return v.animate()
                .alpha(1)
                .setDuration(300);
    }

    ViewPropertyAnimator visibleToHideAnimate (View v) {
        return v.animate()
                .alpha(0)
                .setDuration(300);
    }

    private void loadFragment(Fragment fragment, int id) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(id, fragment);
        ft.commit();
    }

    public void addNewTalent (Talent talent) {
        talentsList.addView(generateNewTalent(talent));

        unAnimate();
    }

    private TextView generateNewTalent (Talent talent) {
        int padding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics());

        TextView talentView = new TextView(this);
        talentView.setLayoutParams(
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                )
        );
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) talentView.getLayoutParams();
        lp.leftMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());
        lp.rightMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());
        talentView.setTypeface(getResources().getFont(R.font.sfuitextlight));
        talentView.setTextColor(getColor(R.color.white));
        talentView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        talentView.setText(talent.getName());
        talentView.setPadding(padding,0, padding, 0);
        talentView.setBackground(getDrawable(R.drawable.rectangle_talent_label));
        talentView.getBackground().setColorFilter(talent.getColor(), PorterDuff.Mode.SRC_ATOP);
        talentView.setOnClickListener(this);

        return talentView;
    }

    @Click(R.id.button_send_post)
    void send () {
        if (photo == null) {
            finish();
            return;
        }

        File mediaFile = new File(photo.getPath());

        uploadFile(mediaFile);
    }
}
