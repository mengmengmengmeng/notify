package me.notify;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * Created by David Rommel, B. on 8/8/15.
 */
public class TaskRegister {
    SharedPreferences preferences;
    Context context;
    String first_name, last_name, age, mobile_number, email, password;

    public TaskRegister(Context context, SharedPreferences preferences, String first_name,
                        String last_name, String age, String mobile_number, String email, String password){
        this.preferences = preferences;
        this.context = context;
        this.first_name = first_name;
        this.last_name = last_name;
        this.age = age;
        this.mobile_number = mobile_number;
        this.email = email;
        this.password = password;
    }

    public class registerUser extends AsyncTask<String,Integer,Boolean>
    {

        String str ="", return_response="";
        HttpResponse response;
        @Override
        protected Boolean doInBackground(String... params) {
            boolean successlogin;
            try {
                String boundary = "---------------------------This is the boundary";
                HttpClient client = new DefaultHttpClient();

                MultipartEntity entity = new MultipartEntity(
                        HttpMultipartMode.BROWSER_COMPATIBLE, boundary, null);

                entity.addPart("first_name", new StringBody(first_name));
                entity.addPart("last_name", new StringBody(last_name));
               // entity.addPart("age", new StringBody(username));
                entity.addPart("mobile_number", new StringBody(email));
                entity.addPart("email", new StringBody(password));
                entity.addPart("password", new StringBody(password));
                //entity.addPart("birthday", new StringBody(birthday));
                //entity.addPart("avatar", new FileBody(file, "image/jpeg"));

                HttpPost do_this = new HttpPost(context.getString(R.string.register));
                do_this.addHeader("Content-Type", "multipart/form-data; boundary="
                        + boundary);
                //do_this.addHeader(new BasicHeader("Authorization", preferences.getString("token", "")));
                do_this.setEntity(entity);
                response = client.execute(do_this);

                String responseBody = EntityUtils.toString(response.getEntity());

                if(response.getStatusLine().getStatusCode() == 201)
                {
                    successlogin=true;
                    Log.v("Registration : ", responseBody);

                }else if(response.getStatusLine().getStatusCode() == 200){
                    successlogin=true;
                    Log.v("Registration : ", responseBody);
                }
                else
                {
                    str="Registration Failed : "+responseBody + "  "+ response.getStatusLine().getStatusCode();
                    successlogin=false;
                }

            } catch (IOException e) {
                e.printStackTrace();
                successlogin= false;
            }

            return successlogin;
        }
        protected void onPostExecute(Boolean result) {
            if(result){

            }else{

            }

        }
    }
}
