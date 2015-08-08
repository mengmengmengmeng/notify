package me.notify;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * Created by David Rommel, B. on 8/9/15.
 */
public class ChannelSubscribed extends Fragment {
    SharedPreferences preferences;
    ListView listView;
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.channel_subscribed, container, false);
        preferences = getActivity().getSharedPreferences("tokens", Context.MODE_PRIVATE);
        listView = (ListView) v.findViewById(R.id.listView);
        //new getAnnouncement().execute();
        return v;

    }
}
