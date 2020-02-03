package ru.vnn.nick.italent;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.vnn.nick.italent.activities.MainActivity;
import ru.vnn.nick.italent.activities.MainActivity_;
import ru.vnn.nick.italent.dto.Post;
import ru.vnn.nick.italent.dto.Talent;
import ru.vnn.nick.italent.dto.User;
import ru.vnn.nick.italent.fragments.FragmentWithParams;

public class SearchFragment extends Fragment implements FragmentWithParams {
    private ScrollView sv;

    public SearchFragment() {

    }

    public static Fragment newInstance() {
        return new SearchFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_search, container, false);

        sv = view.findViewById(R.id.search_sv);

//        getUsers("");
        return view;
    }

    public void getUsers(final String searchString) {
        sv.removeAllViews();
        LinearLayout ll = new LinearLayout(getContext());
        ll.setOrientation(LinearLayout.VERTICAL);

        App.getUserClient().getUsers().enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (!response.isSuccessful())
                    return;

                assert response.body() != null;
                for (User user : response.body()) {
                    View v1 = getLayoutInflater().inflate(R.layout.search_people_item_fragment, ll, false);

                    mappingUserToSearchItemLayout(user, v1);

                    ll.addView(v1);
                }

            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Toast.makeText(
                        getContext(),
                        "Registration error." + t.getCause().getMessage(),
                        Toast.LENGTH_SHORT
                ).show();
                Log.d("TAG", t.getCause().getMessage());
            }
        });

        sv.addView(ll);
    }

    private void mappingUserToSearchItemLayout(User user, View view) {
        final ImageView avatar = view.findViewById(R.id.people_item_avatar);
        final TextView username = view.findViewById(R.id.people_username_search);
        final TextView name = view.findViewById(R.id.people_name_search);
        final TextView id = view.findViewById(R.id.people_id);

        if (user.getAvararUrl() != null) {
            Picasso
                    .get()
                    .load((getString(R.string.server_url) + user.getAvararUrl()).trim())
                    .into(avatar);
        }
        id.setText(String.valueOf(user.getId()));
        username.setText(user.getUsername());
        if (user.getFirstName() != null && user.getLastName() != null) {
            name.setText(String.join(" ", user.getFirstName(), user.getLastName()));
        } else {
            name.setVisibility(View.INVISIBLE);
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, String> params = new HashMap<>();
                params.put("username", username.getText().toString());
                ((MainActivity_) getActivity()).changeFragment(getActivity().findViewById(R.id.menu_profile), params);

                // Go to user profile here
            }
        });
    }

    public void getTalents(final String searchString) {
        sv.removeAllViews();
        LinearLayout ll = new LinearLayout(getContext());
        ll.setOrientation(LinearLayout.VERTICAL);

        App.getTalentsClient().getTalents(searchString).enqueue(new Callback<List<Talent>>() {
            @Override
            public void onResponse(@NonNull Call<List<Talent>> call, @NonNull Response<List<Talent>> response) {
                if (!response.isSuccessful())
                    return;

                assert response.body() != null;
                for (Talent talent : response.body()) {
                    View v = getLayoutInflater().inflate(R.layout.search_talent_item_fragment, ll, false);

                    mappintTalentToSearchItem(talent, v);

                    ll.addView(v);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Talent>> call, @NonNull Throwable t) {
                Toast.makeText(
                        getContext(),
                        "Network error." + t.getCause().getMessage(),
                        Toast.LENGTH_SHORT
                ).show();
                Log.d("TAG", t.getCause().getMessage());
            }
        });

        sv.addView(ll);
    }

    private void mappintTalentToSearchItem(Talent talent, View view) {
        final TextView talentName = view.findViewById(R.id.talent_name);

        talentName.setText(talent.getName());

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Go to search top posts by talent view here
            }
        });
    }

    public void clearMainArea() {
        sv.removeAllViews();
    }

    @Override
    public void provideParams(Map params) {

    }
}
