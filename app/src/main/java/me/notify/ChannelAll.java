package me.notify;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import com.melnykov.fab.FloatingActionButton;
import com.melnykov.fab.ScrollDirectionListener;

/**
 * Created by David Rommel, B. on 8/9/15.
 */
public class ChannelAll extends Fragment {
    SharedPreferences preferences;
    ListView listView;
    FloatingActionButton fab;
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.channel_all, container, false);
        preferences = getActivity().getSharedPreferences("tokens", Context.MODE_PRIVATE);
        listView = (ListView) v.findViewById(R.id.listView);
        fab = (FloatingActionButton) v.findViewById(R.id.fab);

        fab.attachToListView(listView, new ScrollDirectionListener() {
            @Override
            public void onScrollDown() {
                fab.hide();
            }

            @Override
            public void onScrollUp() {
                fab.show();
            }
        }, new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                Log.d("ListViewFragment", "onScrollStateChanged()");
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                Log.d("ListViewFragment", "onScroll()");
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        //new getAnnouncement().execute();
        return v;

    }
}
