package me.notify;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * Created by David Rommel, B. on 8/9/15.
 */
public class CreateAnnouncement extends BaseActivity{
    EditText title, message;
    FrameLayout submit;
    SharedPreferences preferences;
    ProgressDialog prog;
    String channel_id;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_announcement_fragment);
        preferences = getSharedPreferences("tokens", Context.MODE_PRIVATE);

        title = (EditText) findViewById(R.id.title);
        message = (EditText) findViewById(R.id.message);
        submit = (FrameLayout) findViewById(R.id.frameLSubmit);

        channel_id = getIntent().getStringExtra("channel_id");

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    new postAnnouncement().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                } else {
                    new postAnnouncement().execute();
                }
            }
        });
    }

    public class postAnnouncement extends AsyncTask<String,Integer,Boolean>
    {
        String str ="";
        HttpResponse response;
        String responseBody;

        @Override
        protected void onPreExecute() {
            prog = new ProgressDialog(CreateAnnouncement.this);
            prog.setMessage("Please wait..");
            prog.show();
            prog.setCancelable(false);
        }

        @Override
        protected Boolean doInBackground(String... params) {
            boolean successlogin = false;
            try {
                String boundary = "---------------------------This is the boundary";
                HttpClient client = new DefaultHttpClient();

                MultipartEntity entity = new MultipartEntity(
                        HttpMultipartMode.BROWSER_COMPATIBLE, boundary, null);

                HttpPost do_this;
                entity.addPart("announcement[title]", new StringBody(title.getText().toString()));
                entity.addPart("announcement[message]", new StringBody(message.getText().toString()));
                do_this = new HttpPost(getString(R.string.create_announcement) + preferences.getString("id", "")+"/channels/"+channel_id+"/announcements");

                do_this.addHeader(new BasicHeader("Authorization", preferences.getString("auth_token", "")));
                do_this.setEntity(entity);
                response = client.execute(do_this);

                responseBody = EntityUtils.toString(response.getEntity());

                if(response.getStatusLine().getStatusCode() == 201)
                {
                    successlogin=true;
                    Log.v("subscription : ", responseBody);

                }else if(response.getStatusLine().getStatusCode() == 200){
                    successlogin=true;
                    Log.v("subscription2 : ", responseBody);
                }
                else
                {
                    str="Login Failed : "+responseBody + "  "+ response.getStatusLine().getStatusCode();
                    Log.v(" ", str);
                    successlogin=false;
                }

            } catch (IOException e) {
                e.printStackTrace();
                successlogin= false;
            }

            return successlogin;
        }
        protected void onPostExecute(Boolean result) {
            prog.dismiss();
            if(result){
                Intent intent = new Intent(CreateAnnouncement.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }else{
                Toast.makeText(CreateAnnouncement.this, "Fill all values", Toast.LENGTH_LONG).show();
            }

        }
    }
}
