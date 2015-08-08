package me.notify;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by David Rommel, B. on 8/8/15.
 */
public class TaskRegisterAndLogin {
    SharedPreferences preferences;
    Context context;
    String first_name, last_name, age, mobile_number, email, password;
    ProgressDialog prog;
    public static int LOGIN = 0;
    public static int REGISTER = 1;
    int method;

    public TaskRegisterAndLogin(Context context, SharedPreferences preferences, String first_name,
                                String last_name, String age, String mobile_number, String email, String password, int method){
        this.preferences = preferences;
        this.context = context;
        this.first_name = first_name;
        this.last_name = last_name;
        this.age = age;
        this.mobile_number = mobile_number;
        this.email = email;
        this.password = password;
        this.method = method;
    }

    public void executeRegistration() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            new registerUser().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            new registerUser().execute();
        }
    }

    public class registerUser extends AsyncTask<String,Integer,Boolean>
    {

        String str ="";
        HttpResponse response;
        String responseBody;

        @Override
        protected void onPreExecute() {
            prog = new ProgressDialog(context);
            prog.setMessage("Please wait..");
            prog.show();
            prog.setCancelable(false);
        }

        @Override
        protected Boolean doInBackground(String... params) {
            boolean successlogin;
            try {
                String boundary = "---------------------------This is the boundary";
                HttpClient client = new DefaultHttpClient();

                MultipartEntity entity = new MultipartEntity(
                        HttpMultipartMode.BROWSER_COMPATIBLE, boundary, null);

                HttpPost do_this;
                if(method == 1){
                    entity.addPart("user[first_name]", new StringBody(first_name));
                    entity.addPart("user[last_name]", new StringBody(last_name));
                    entity.addPart("user[age]", new StringBody(age));
                    entity.addPart("user[mobile_number]", new StringBody(mobile_number));
                    entity.addPart("user[email]", new StringBody(email));
                    entity.addPart("user[password]", new StringBody(password));
                    //entity.addPart("birthday", new StringBody(birthday));
                    //entity.addPart("avatar", new FileBody(file, "image/jpeg"));
                    do_this = new HttpPost(context.getString(R.string.register));
                }else{
                    entity.addPart("user[email]", new StringBody(email));
                    entity.addPart("user[password]", new StringBody(password));
                    //entity.addPart("birthday", new StringBody(birthday));
                    //entity.addPart("avatar", new FileBody(file, "image/jpeg"));
                    do_this = new HttpPost(context.getString(R.string.login));
                }

                /*do_this.addHeader("Content-Type", "multipart/form-data; boundary="
                        + boundary);*/
                //do_this.addHeader(new BasicHeader("Authorization", preferences.getString("token", "")));
                do_this.setEntity(entity);
                response = client.execute(do_this);

                responseBody = EntityUtils.toString(response.getEntity());

                if(response.getStatusLine().getStatusCode() == 201)
                {
                    successlogin=true;
                    if(method == 1){
                        Log.v("Registration : ", responseBody);
                    }else{
                        Log.v("Login : ", responseBody);
                    }


                }else if(response.getStatusLine().getStatusCode() == 200){
                    successlogin=true;
                    if(method == 1){
                        Log.v("Registration : ", responseBody);
                    }else{
                        Log.v("Login : ", responseBody);
                    }
                }
                else
                {
                    if(method == 1){
                        str="Registration Failed : "+responseBody + "  "+ response.getStatusLine().getStatusCode();
                        Log.v(" ", str);
                    }else{
                        str="Login Failed : "+responseBody + "  "+ response.getStatusLine().getStatusCode();
                        Log.v(" ", str);
                    }
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
                try {
                    JSONObject me = new JSONObject(responseBody);

                    CheckIfNull checkIfNull = new CheckIfNull();

                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("id", checkIfNull.check_if_null(me.getString("id")));
                    editor.putString("email", checkIfNull.check_if_null(me.getString("email")));
                    editor.putString("first_name", checkIfNull.check_if_null(me.getString("first_name")));
                    editor.putString("last_name", checkIfNull.check_if_null(me.getString("last_name")));
                    editor.putString("age", checkIfNull.check_if_null(me.getString("age")));
                    editor.putString("auth_token", checkIfNull.check_if_null(me.getString("auth_token")));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(context, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }else{
                Toast.makeText(context, "Email is invalid", Toast.LENGTH_LONG).show();
            }

        }
    }
}
