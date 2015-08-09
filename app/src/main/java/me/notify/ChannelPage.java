package me.notify;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by David Rommel, B. on 8/9/15.
 */
public class ChannelPage extends BaseActivity {
    String title, description, id, owner_id, category, logo, count;
    SharedPreferences preferences;
    Button subscribe, create;
    TextView Ttitle, Tdescription, Tcategory;
    ImageView mainImage, blurImage;
    ListView listView;
    List<AnnouncementList> list = new ArrayList<>();
    loadListView listAdapter = null;
    String smsResult, emailResult;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.channel_page);
        preferences = getSharedPreferences("tokens", Context.MODE_PRIVATE);
        title = getIntent().getStringExtra("title");
        description = getIntent().getStringExtra("description");
        owner_id = getIntent().getStringExtra("owner_id");
        category = getIntent().getStringExtra("category");
        logo = getIntent().getStringExtra("logo");
        count = getIntent().getStringExtra("count");
        id = getIntent().getStringExtra("id");
        subscribe = (Button) findViewById(R.id.subscribe);
        create = (Button) findViewById(R.id.create);
        Ttitle = (TextView) findViewById(R.id.title);
        Tdescription = (TextView) findViewById(R.id.description);
        Tcategory = (TextView) findViewById(R.id.category);
        mainImage = (ImageView) findViewById(R.id.mainImage);
        blurImage = (ImageView) findViewById(R.id.blurImage);
        listView = (ListView) findViewById(R.id.listView);

        Picasso.with(ChannelPage.this).load(logo).transform(new BlurTransformation(ChannelPage.this)).into(blurImage);
        Picasso.with(ChannelPage.this).load(logo).into(mainImage);
        Ttitle.setText(title);
        Tdescription.setText(description);
        Tcategory.setText(category);

        if(preferences.getString("id", "").equals(owner_id)){
            create.setVisibility(View.VISIBLE);
            subscribe.setVisibility(View.GONE);
        }else{
            subscribe.setVisibility(View.VISIBLE);
            create.setVisibility(View.GONE);
        }

        subscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCoachMark();
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            new getAnnouncement().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            new getAnnouncement().execute();
        }

    }

    class loadListView extends ArrayAdapter<AnnouncementList> {
        public loadListView() {
            // TODO Auto-generated constructor stub
            super(ChannelPage.this, android.R.layout.simple_list_item_1, list);
        }


        public View getView(int position, View convertView, ViewGroup parent){
            ViewHolder holder;

            if(convertView ==null){
                LayoutInflater inflater=getLayoutInflater();
                convertView=inflater.inflate(R.layout.channel_page_list, null);


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
        public TextView message = null;
        public TextView created_at = null;


        public ViewHolder(View v) {
            // TODO Auto-generated constructor stub

            title=(TextView)v.findViewById(R.id.title);
            message=(TextView)v.findViewById(R.id.message);
            created_at = (TextView) v.findViewById(R.id.created_at);
        }

        void populateFrom(AnnouncementList s) /*throws java.text.ParseException*/{
            title.setText(s.getTitle());
            message.setText(s.getMessage());
            created_at.setText(s.getCreated_at());

        }
    }

    public class getAnnouncement extends AsyncTask<Void, Integer, Boolean>
    {
        String str="";
        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO Auto-generated method stub
            boolean successRetrieve = false;

            try{
                ServiceHandler sh = new ServiceHandler();
                String url = getString(R.string.announcements)+preferences.getString("id","")+"/channels/"+id+"/announcements";
                str = sh.makeServiceCall(url, preferences.getString("auth_token", ""), ServiceHandler.GET);

                try{
                    JSONArray newJson = new JSONArray(str);

                    for (int i=0; i< newJson.length(); i++){
                        JSONObject obj_data = newJson.getJSONObject(i);
                        AnnouncementList data = new AnnouncementList();

                        data.setAnnouncement(obj_data.getString("id"), obj_data.getString("title"),
                                obj_data.getString("message"), obj_data.getString("created_at"));

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
                listAdapter.notifyDataSetChanged();
            }
        }

    }

    public class postSubscribe extends AsyncTask<Void, Integer, Boolean>
    {
        String str ="";
        HttpResponse response;
        String responseBody;
        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO Auto-generated method stub
            boolean successRetrieve = false;

            try {
                String boundary = "---------------------------This is the boundary";
                HttpClient client = new DefaultHttpClient();

                MultipartEntity entity = new MultipartEntity(
                        HttpMultipartMode.BROWSER_COMPATIBLE, boundary, null);

                HttpPost do_this;
                entity.addPart("subscription[sms]", new StringBody(smsResult));
                entity.addPart("subscription[email]", new StringBody(emailResult));
                entity.addPart("subscription[mobile]", new StringBody("false"));
                do_this = new HttpPost(getString(R.string.subscribe)+id+"/subscription");
                do_this.addHeader(new BasicHeader("Authorization", preferences.getString("auth_token", "")));
                do_this.setEntity(entity);
                response = client.execute(do_this);

                responseBody = EntityUtils.toString(response.getEntity());

                if(response.getStatusLine().getStatusCode() == 201)
                {
                    successRetrieve=true;
                    Log.v("Registration : ", responseBody);


                }else if(response.getStatusLine().getStatusCode() == 200){
                    successRetrieve=true;
                    Log.v("yes : ", responseBody);
                }
                else
                {
                    str="no : "+responseBody + "  "+ response.getStatusLine().getStatusCode();
                    Log.v(" ", str);
                    successRetrieve=false;
                }

            } catch (IOException e) {
                e.printStackTrace();
                successRetrieve= false;
            }


            return successRetrieve;

        }

        @Override
        protected void onPostExecute(Boolean result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            if(result){
                Toast.makeText(ChannelPage.this, "Successfully subscribe!", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(ChannelPage.this, "User already subscribed!", Toast.LENGTH_LONG).show();
            }
        }

    }

    public void openCoachMark(){
        final Dialog dialog = new Dialog(ChannelPage.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog_for_check_box);
        final CheckBox sms = (CheckBox) dialog.findViewById(R.id.sms);
        final CheckBox email = (CheckBox) dialog.findViewById(R.id.email);

        Button subscribe = (Button) dialog.findViewById(R.id.subscribe);
        subscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sms.isChecked()){
                    smsResult = "true";
                }else{
                    smsResult = "false";
                }
                if (email.isChecked()){
                    emailResult = "true";
                }else{
                    emailResult = "false";
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    new postSubscribe().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                } else {
                    new postSubscribe().execute();
                }

                dialog.dismiss();
            }
        });

        dialog.show();
    }
}
