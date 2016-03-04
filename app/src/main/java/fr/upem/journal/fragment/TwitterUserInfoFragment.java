package fr.upem.journal.fragment;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.twitter.sdk.android.core.models.User;
import fr.upem.journal.R;


public class TwitterUserInfoFragment extends ListFragment {

    public static final String ARG_PAGE = "ARG_PAGE";
    private User user;

    public static TwitterUserInfoFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        TwitterUserInfoFragment fragment = new TwitterUserInfoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_twitter_user_info, container, false);
        return view;
    }

    public void setUser(User user){
        this.user = user;
    }
}
