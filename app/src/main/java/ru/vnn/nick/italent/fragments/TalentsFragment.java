package ru.vnn.nick.italent.fragments;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.androidannotations.annotations.AfterTextChange;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FocusChange;
import org.androidannotations.annotations.TextChange;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.vnn.nick.italent.App;
import ru.vnn.nick.italent.PhotoPreviewActivity;
import ru.vnn.nick.italent.R;
import ru.vnn.nick.italent.SearchFragment;
import ru.vnn.nick.italent.dto.Talent;

@EFragment(R.layout.fragment_talents)
public class TalentsFragment extends android.support.v4.app.Fragment {
    @ViewById(R.id.search_text)
    EditText searchText;

    @ViewById(R.id.search_reset_button)
    ImageView imageView;

    @ViewById(R.id.talents_searcher_sv)
    ScrollView talentSearcherSv;

    @ViewById(R.id.color_list)
    HorizontalScrollView hsv;

    @ViewById(R.id.color_red)
    Button red;
    @ViewById(R.id.color_orange)
    Button orange;
    @ViewById(R.id.color_yellow)
    Button yellow;
    @ViewById(R.id.color_green)
    Button green;
    @ViewById(R.id.color_darkgreen)
    Button darkGreen;
    @ViewById(R.id.color_whiteblue)
    Button whiteBlue;
    @ViewById(R.id.color_blue)
    Button blue;
    @ViewById(R.id.color_darkblue)
    Button darkBlue;
    @ViewById(R.id.color_violet)
    Button violet;
    @ViewById(R.id.color_pink)
    Button pink;
    @ViewById(R.id.color_black)
    Button black;

    @ViewById(R.id.talent_preview)
    TextView talentPreview;

    private Timer timer;
    private String pickedTalent;
    private long pickedColor;

    @AfterViews
    void afterViews () {
        searchText.setText(getString(R.string.searchForTalent));
    }

    @FocusChange(R.id.search_text)
    void editTitle (boolean hasFocus) {
        if (getString(R.string.searchForTalent).equals(searchText.getText().toString())) {
            searchText.setText("");
        } else if ("".equals(searchText.getText().toString())) {
            searchText.setText(getString(R.string.searchForTalent));
        }

        if (hasFocus) {
            searchText.setTextColor(getResources().getColor(R.color.searchTextActiveColor));
        } else {
            searchText.setTextColor(getResources().getColor(R.color.searchTextColor));
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

    @AfterTextChange(R.id.search_text)
    void search() {
        String search = searchText.getText().toString();

        if (search.isEmpty() || search.equals(getString(R.string.searchForTalent))) {
            return;
        }

        timer = new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                hsv.setVisibility(View.GONE);
                talentPreview.setVisibility(View.GONE);
                search(searchText.getText().toString());
            }
        }, 2000); // 600 ms delay
    }

    @UiThread
    void search (String searchString) {
        talentSearcherSv.removeAllViews();
        LinearLayout ll = new LinearLayout(getContext());
        ll.setOrientation(LinearLayout.VERTICAL);

        Talent t1 = new Talent();
        t1.setName(searchString);
        View view = getLayoutInflater().inflate(R.layout.search_talent_item_fragment, ll);
        mappintTalentToSearchItem(t1, view);

//        App.getTalentsClient().getTalents(searchString).enqueue(new Callback<List<Talent>>() {
//            @Override
//            public void onResponse(@NonNull Call<List<Talent>> call, @NonNull Response<List<Talent>> response) {
//                if (!response.isSuccessful())
//                    return;
//
//                assert response.body() != null;
//                for (Talent talent: response.body()) {
//                    View view = getLayoutInflater().inflate(R.layout.search_talent_item_fragment, ll);
//
//                    mappintTalentToSearchItem(talent, view);
//                }
//            }
//
//            @Override
//            public void onFailure(@NonNull Call<List<Talent>> call, @NonNull Throwable t) {
//                Toast.makeText(
//                        getContext(),
//                        "Network error." + t.getCause().getMessage(),
//                        Toast.LENGTH_SHORT
//                ).show();
//                Log.d("TAG", t.getCause().getMessage());
//            }
//        });

        talentSearcherSv.addView(ll);
    }

    private void mappintTalentToSearchItem (Talent talent, View view) {
        final TextView talentName = view.findViewById(R.id.talent_name);

        talentName.setText(talent.getName());

        talentName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickedTalent = talent.getName();

                hsv.setVisibility(View.VISIBLE);
            }
        });
    }

    @Click({
            R.id.color_red,
            R.id.color_orange,
            R.id.color_yellow,
            R.id.color_green,
            R.id.color_darkgreen,
            R.id.color_whiteblue,
            R.id.color_blue,
            R.id.color_darkblue,
            R.id.color_violet,
            R.id.color_pink,
            R.id.color_black
    })
    void selectColor (Button button) {
        pickedColor = button.getId();
//        talentPreview.getBackground().setColorFilter(button.getBackground().getColorFilter());
        talentPreview.setBackground(button.getBackground());
        talentPreview.setText(pickedTalent);

        if (talentPreview.getVisibility() == View.GONE) {
            talentPreview.setVisibility(View.VISIBLE);
        }
    }

    @Click(R.id.talent_preview)
    void returnNewTalent () {
        Talent newTalent = new Talent();
        newTalent.setName(pickedTalent);

        ColorDrawable cd = (ColorDrawable) talentPreview.getBackground();

        newTalent.setColor(cd.getColor());

        // SEND TALENT TO SERVER

        ((PhotoPreviewActivity) getActivity()).addNewTalent(newTalent);
    }
}
