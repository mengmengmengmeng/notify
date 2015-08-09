package me.notify;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.melnykov.fab.FloatingActionButton;
import com.melnykov.fab.ScrollDirectionListener;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by David Rommel, B. on 8/9/15.
 */
public class ChannelSubscribed extends Fragment {
    SharedPreferences preferences;
    ListView listView;
    List<ChannelList> list = new ArrayList<>();
    loadListView listAdapter = null;
    FloatingActionButton fab;
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.channel_subscribed, container, false);
        preferences = getActivity().getSharedPreferences("tokens", Context.MODE_PRIVATE);
        listView = (ListView) v.findViewById(R.id.listView);
        fab = (FloatingActionButton) v.findViewById(R.id.fab);

        fab.attachToListView(listView, new ScrollDirectionListener() {
            @Override
            public void onScrollDown() {
                fab.show();
            }

            @Override
            public void onScrollUp() {
                fab.hide();
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CreateChannelActivity.class);
                startActivity(intent);
            }
        });
        return v;

    }

    class loadListView extends ArrayAdapter<ChannelList> {
        public loadListView() {
            // TODO Auto-generated constructor stub
            super(getActivity(), android.R.layout.simple_list_item_1, list);
        }


        public View getView(int position, View convertView, ViewGroup parent){
            ViewHolder holder;

            if(convertView ==null){
                LayoutInflater inflater=getActivity().getLayoutInflater();
                convertView=inflater.inflate(R.layout.channel_all_list, null);


                holder= new ViewHolder(convertView);

                convertView.setTag(holder);
            }else{
                holder=(ViewHolder)convertView.getTag();

            }

            holder.populateFrom(list.get(position));

            return(convertView);
        }
    }

    class ViewHolder{
        public TextView title = null;
        public TextView description = null;
        public TextView category = null;
        public ImageView logo;


        public ViewHolder(View v) {
            // TODO Auto-generated constructor stub

            title=(TextView)v.findViewById(R.id.title);
            description=(TextView)v.findViewById(R.id.description);
            category = (TextView) v.findViewById(R.id.category);
            logo = (ImageView) v.findViewById(R.id.logo);
        }

        void populateFrom(ChannelList s) /*throws java.text.ParseException*/{
            title.setText(s.getTitle());
            description.setText(s.getDescription());
            category.setText(s.getCategory());

            Picasso.with(getActivity()).load(s.getLogo()).into(logo);
        }
    }

    public class getChannel extends AsyncTask<Void, Integer, Boolean>
    {
        String str="";
        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO Auto-generated method stub
            boolean successRetrieve = false;

            try{
                ServiceHandler sh = new ServiceHandler();
                String url = getString(R.string.subscribed)+preferences.getString("id", "")+"/subscribed";

                str = sh.makeServiceCall(url, preferences.getString("auth_token", ""), ServiceHandler.GET);

                try{
                    JSONArray newJson = new JSONArray(str);

                    for (int i=0; i< newJson.length(); i++){
                        JSONObject obj_data = newJson.getJSONObject(i);
                        ChannelList data = new ChannelList();

                        data.setChannel(obj_data.getString("id"), obj_data.getString("title"),
                                obj_data.getString("description"), obj_data.getString("status"),
                                obj_data.getString("category"), obj_data.getString("logo"),
                                obj_data.getString("subscribers_count"), obj_data.getString("owner_id"));

                        list.add(data);

                    }
                }catch(JSONException e){
                    successRetrieve = true;
                    e.printStackTrace();
                }

                successRetrieve = true;
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return successRetrieve;

        }

        @Override
        protected void onPostExecute(Boolean result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            if(result){
                listAdapter = new loadListView();
                listView.setAdapter(listAdapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1,int position, long arg3)
                    {
                        Intent intent = new Intent(getActivity(), ChannelPage.class);
                        intent.putExtra("id", list.get(position).getId());
                        intent.putExtra("title", list.get(position).getTitle());
                        intent.putExtra("description", list.get(position).getDescription());
                        intent.putExtra("category", list.get(position).getCategory());
                        intent.putExtra("logo", list.get(position).getLogo());
                        intent.putExtra("count", list.get(position).getSubscribers_count());
                        intent.putExtra("owner_id", list.get(position).getOwner_id());
                        startActivity(intent);
                    }
                });
                listAdapter.notifyDataSetChanged();
            }
        }

    }

    @Override
    public void onResume() {
        super.onResume();

        if(list!=null){
            list.clear();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                new getChannel().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            } else {
                new getChannel().execute();
            }
        }else{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                new getChannel().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            } else {
                new getChannel().execute();
            }
        }
    }
}
