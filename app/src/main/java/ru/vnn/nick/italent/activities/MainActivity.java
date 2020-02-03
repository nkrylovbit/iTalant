package ru.vnn.nick.italent.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Size;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;


import org.androidannotations.annotations.AfterTextChange;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.FocusChange;
import org.androidannotations.annotations.Fullscreen;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.TextChange;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import ru.vnn.nick.italent.HomeFragment;
import ru.vnn.nick.italent.InterlocutionsFragment;
import ru.vnn.nick.italent.fragments.PostFragment;
import ru.vnn.nick.italent.fragments.FragmentWithParams;
import ru.vnn.nick.italent.R;
import ru.vnn.nick.italent.SearchFragment;
import ru.vnn.nick.italent.utils.DimensionManager;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {
    private Fragment itFragment;
    private Fragment searchFragment;
    private Fragment interlocutionsFragment;
    private Fragment profileFragment;
    private Fragment active;

    @ViewById(R.id.fl_content)
    FrameLayout content;

    @ViewById(R.id.selected_search_type)
    ImageView searchType;

    @ViewById(R.id.search_text)
    AppCompatEditText searchText;

    @ViewById(R.id.search_button_people)
    AppCompatTextView searchButtonPeople;

    @ViewById(R.id.search_button_talent)
    AppCompatTextView searchButtonTalent;

    @ViewById(R.id.menu_selected)
    FrameLayout selected;

    @ViewById(R.id.menu_selected_rectangle)
    ImageView selectedRectangle;

    @ViewById(R.id.menu_it)
    ImageView menuIT;

    List<Fragment> previousFragments = new ArrayList<>();

    ImageView selectedNow;
    AppCompatTextView activeSearchMode;
    private Timer timer;

    private static final int CAMERA_RC = 1;

    private void loadFragment(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fl_content, fragment);
        ft.commit();
    }

    private void changeActivity(Class clazz) {
        Intent intent = new Intent(this, clazz);

        startActivityForResult(intent, CAMERA_RC);
    }

    @SuppressWarnings("ConstantConditions")
    @AfterViews
    public void afterViews() {

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setSelected();
        setContentMargin();
        selectedNow = menuIT;
        activeSearchMode = searchButtonPeople;
        ((AppCompatTextView) findViewById(R.id.title_action_bar)).setText(R.string.title_news);

        loadFragment(PostFragment.newInstance());
    }

    private void setSelected () {
        selected.getLayoutParams().width = getWindowManager().getDefaultDisplay().getWidth() / 5;
        selectedRectangle.getLayoutParams().width = ((Double) (selected.getLayoutParams().width * 0.4)).intValue();
    }

    private void setContentMargin () {
        int px = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                ((Double) (100 * 98 / 167.0)).intValue(),
                getResources().getDisplayMetrics()
        );
        content.setPadding(0, 0, 0, px);
    }

    @Click({
            R.id.menu_it,
            R.id.menu_search,
            R.id.menu_message,
            R.id.menu_profile
    })
    public void changeFragment (final ImageView clicked) {
        changeFragment(clicked, null);
    }

    public void changeFragment (ImageView clicked, Map<String, String> params) {
        final float from = selected.getX();
        final float to = clicked.getX();

        if (from == to) {
            return;
        }

        selectedNow.setImageTintList(getColorStateList(R.color.semiwhite));
        animate(from, to, selected);
        selectedNow = clicked;
        selectedNow.setImageTintList(getColorStateList(R.color.white));

        switch (selectedNow.getId()) {
            case R.id.menu_it:
                findViewById(R.id.app_bar_layout).setVisibility(View.VISIBLE);
                ((AppCompatTextView) findViewById(R.id.title_action_bar)).setText(R.string.title_news);
                findViewById(R.id.search_container).setVisibility(View.GONE);
                findViewById(R.id.title_action_bar).setVisibility(View.VISIBLE);
                if (itFragment == null) {
                    itFragment = PostFragment.newInstance();
                }
                active = itFragment;
                break;
            case R.id.menu_search:
                findViewById(R.id.app_bar_layout).setVisibility(View.VISIBLE);
                findViewById(R.id.title_action_bar).setVisibility(View.GONE);
                findViewById(R.id.search_container).setVisibility(View.VISIBLE);
                if (searchFragment == null) {
                    searchFragment = SearchFragment.newInstance();
                }
                active = searchFragment;
                break;
            case R.id.menu_message:
                findViewById(R.id.app_bar_layout).setVisibility(View.VISIBLE);
                ((AppCompatTextView) findViewById(R.id.title_action_bar)).setText(R.string.title_messages);
                findViewById(R.id.search_container).setVisibility(View.GONE);
                findViewById(R.id.title_action_bar).setVisibility(View.VISIBLE);
                if (interlocutionsFragment == null) {
                    interlocutionsFragment = InterlocutionsFragment.newInstance();
                }
                active = interlocutionsFragment;
                break;
            case R.id.menu_profile:
                if (profileFragment == null) {
                    profileFragment = HomeFragment.newInstance(10);
                }
                findViewById(R.id.app_bar_layout).setVisibility(View.GONE);
                active = profileFragment;
                break;
        }

        ((FragmentWithParams) active).provideParams (params);
        loadFragment(active);
    }

    @Click({R.id.search_button_people, R.id.search_button_talent})
    void choiceSearchType (final View view) {
        final float from = searchType.getX();
        final float to = view.getX();

        if (from != to) {
            animate(from, to, searchType);
        }

        AppCompatTextView selected = (AppCompatTextView) view;
        selected.setTextColor(getColor(R.color.searchTextActiveColor));

        String search = searchText.getText().toString();
        if (search.isEmpty()) {
            clearFocus();
        }
        if (R.id.search_button_people == selected.getId()) {
            if (search.isEmpty()
                    || getString(R.string.searchForPeople).equals(search)
                    || getString(R.string.searchForTalent).equals(search)) {
                searchText.setText(R.string.searchForPeople);
                searchButtonTalent.setTextColor(getColor(R.color.searchTextColor));
            }

            activeSearchMode = searchButtonPeople;
        } else {
            if (search.isEmpty()
                    || getString(R.string.searchForPeople).equals(search)
                    || getString(R.string.searchForTalent).equals(search)) {
                searchText.setText(R.string.searchForTalent);
                searchButtonPeople.setTextColor(getColor(R.color.searchTextColor));
            }

            activeSearchMode = searchButtonTalent;
        }

        ((SearchFragment) searchFragment).clearMainArea();
    }

    @AfterTextChange(R.id.search_text)
    void search() {
        String search = searchText.getText().toString();

        if (search.isEmpty()
                || getString(R.string.searchForPeople).equals(search)
                || getString(R.string.searchForTalent).equals(search)) {
            return;
        }

        timer = new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                search(searchText.getText().toString());
            }
        }, 1500); // 600 ms delay
    }

    @UiThread
    void search (String searchString) {
        if (activeSearchMode.equals(searchButtonPeople)) {
            ((SearchFragment) searchFragment).getUsers(searchString);
        } else {
            ((SearchFragment) searchFragment).getTalents(searchString);
        }
    }

    @TextChange(R.id.search_text)
    void textChange() {
        if (timer != null) {
            timer.cancel();
        }
    }

    @Click(R.id.search_reset_button)
    void clearSearchLine () {
        searchText.setText("");
    }

    @FocusChange(R.id.search_text)
    void searchText (boolean hasFocus) {
        if (hasFocus) {
//            searchText.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START); -- OLD
            searchText.setTextColor(getColor(R.color.searchTextActiveColor));

            if (getString(R.string.searchForTalent).equals(searchText.getText().toString()) ||
                    getString(R.string.searchForPeople).equals(searchText.getText().toString()))
                searchText.setText("");

            findViewById(R.id.search_reset_button).setVisibility(View.VISIBLE);
        } else {
            if ("".equals(searchText.getText().toString())) {
                if (activeSearchMode.equals(searchButtonPeople)) {
                    searchText.setText(getString(R.string.searchForPeople));
                } else {
                    searchText.setText(getString(R.string.searchForTalent));
                }
            }

//            searchText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER); -- OLD
            searchText.setTextColor(getColor(R.color.searchTextColor));
            findViewById(R.id.search_reset_button).setVisibility(View.GONE);
        }
    }

    @Click(R.id.fl_content)
    void clearFocus () {
        searchText.clearFocus();
        hideKeyboard(this);
    }

    @Click(R.id.menu_plus)
    void newPost () {
        changeActivity(CameraActivity.class);
    }

    @Background
    void animate(final float from, final float to, final View container) {
        final Animation animation = new TranslateAnimation(0, to - from, 0.0f, 0.0f);
        animation.setDuration(200);
        animation.setFillEnabled(true);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                container.setX(to);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        container.startAnimation(animation);
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @OnActivityResult(CAMERA_RC)
    void onCameraResult () {

    }
}
