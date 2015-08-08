package me.notify;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.kbeanie.imagechooser.api.ChooserType;
import com.kbeanie.imagechooser.api.ChosenImage;
import com.kbeanie.imagechooser.api.ImageChooserListener;
import com.kbeanie.imagechooser.api.ImageChooserManager;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Created by David Rommel, B. on 8/9/15.
 */
public class CreateChannelActivity extends BaseActivity implements ImageChooserListener {
    EditText description, name;
    Spinner category, status;
    FrameLayout frameLsubmit;
    SharedPreferences preferences;
    ImageView setPhoto;
    private int chooserType;
    private ImageChooserManager imageChooserManager;
    File file;
    String filePath;
    ProgressDialog progDl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_channel_activity);
        preferences = getSharedPreferences("tokens", Context.MODE_PRIVATE);

        description = (EditText) findViewById(R.id.description);
        name = (EditText) findViewById(R.id.name);
        category = (Spinner) findViewById(R.id.category);
        status = (Spinner) findViewById(R.id.status);
        frameLsubmit = (FrameLayout) findViewById(R.id.frameLSubmit);
        setPhoto = (ImageView) findViewById(R.id.setPhoto);

        setPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();
            }
        });

        Resources res = getResources();
        ArrayAdapter<String> mAdapter;
        mAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, res.getStringArray(R.array.category_arrays));
        ArrayAdapter<String> mAdapter1;
        mAdapter1 = new ArrayAdapter<>(this, R.layout.spinner_item, res.getStringArray(R.array.status_arrays));
        category.setAdapter(mAdapter);
        status.setAdapter(mAdapter1);

        frameLsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    new posterinoPhotorino().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                } else {
                    new posterinoPhotorino().execute();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK
                && (requestCode == ChooserType.REQUEST_PICK_PICTURE || requestCode == ChooserType.REQUEST_CAPTURE_PICTURE)) {
            imageChooserManager.submit(requestCode, data);
        } else {
        }
    }

    public void openDialog(){
        final Dialog dialog = new Dialog(CreateChannelActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog_for_upload_photo);

        TextView gallery, camera;

        gallery = (TextView) dialog.findViewById(R.id.gallery);
        camera = (TextView) dialog.findViewById(R.id.camera);

        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("preview_image", "1");
                editor.commit();
            }
        });
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("preview_image", "2");
                editor.commit();
            }
        });

        dialog.show();
    }

    private void chooseImage() {
        chooserType = ChooserType.REQUEST_PICK_PICTURE;
        imageChooserManager = new ImageChooserManager(this,
                ChooserType.REQUEST_PICK_PICTURE, "TesteeMe", false);
        imageChooserManager.setImageChooserListener(this);
        try {
            filePath = imageChooserManager.choose();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void takePicture() {
        chooserType = ChooserType.REQUEST_CAPTURE_PICTURE;
        imageChooserManager = new ImageChooserManager(this,
                ChooserType.REQUEST_CAPTURE_PICTURE, "TesteeMe", false);
        imageChooserManager.setImageChooserListener(this);
        try {
            filePath = imageChooserManager.choose();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onImageChosen(final ChosenImage image) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (image != null) {
                    file = new File(image.getFilePathOriginal());
                    setPhoto.setImageURI(Uri.parse(image.getFilePathOriginal()));
                } else {
                    Log.i("NULL: ", "Chosen Image: Is null");
                }
            }
        });
    }

    @Override
    public void onError(String s) {

    }

    public class posterinoPhotorino extends AsyncTask<String,Integer,Boolean>
    {

        String str ="";
        @Override
        protected void onPreExecute() {
            progDl = new ProgressDialog(CreateChannelActivity.this);
            progDl.setTitle("Processing...");
            progDl.setCancelable(false);
            progDl.show();

        }

        @Override
        protected Boolean doInBackground(String... params) {
            boolean successlogin = false;
            try {
                String boundary = "---------------------------This is the boundary";

                HttpClient client = new DefaultHttpClient();
                HttpResponse response;

                MultipartEntity entity = new MultipartEntity(
                        HttpMultipartMode.BROWSER_COMPATIBLE, boundary, null);

                entity.addPart("channel[title]", new StringBody(name.getText().toString()));
                entity.addPart("channel[description]", new StringBody(description.getText().toString()));
                entity.addPart("channel[status]", new StringBody(status.getSelectedItem().toString()));
                entity.addPart("channel[category]", new StringBody(category.getSelectedItem().toString()));
                entity.addPart("channel[logo]", new FileBody(file, "image/jpeg"));

                String url = getString(R.string.create_channel)+ preferences.getString("id", "")+"/channels";
                Log.v("URLLLL", url);
                HttpPost do_this = new HttpPost(url);
                do_this.addHeader("Content-Type", "multipart/form-data; boundary="
                        + boundary);
                Log.v("AUTH_TOKEN", preferences.getString("auth_token", ""));
                do_this.addHeader(new BasicHeader("Authorization", preferences.getString("auth_token", "")));
                do_this.setEntity(entity);
                response = client.execute(do_this);

                String responseBody = EntityUtils.toString(response.getEntity());

                if(response.getStatusLine().getStatusCode() == 201)
                {
                    successlogin=true;
                    Log.v("HEHE", "YEY");
                }else if(response.getStatusLine().getStatusCode() == 200){
                    successlogin=true;
                    Log.v("HEHE", "YEY");
                }
                else
                {
                    str="Registration Failed : "+responseBody + "  "+ response.getStatusLine().getStatusCode();
                    Log.v("HEHE", str);
                    successlogin=false;
                }

            }catch (UnsupportedEncodingException e) {
                e.printStackTrace();

            } catch (ClientProtocolException e) {
                e.printStackTrace();

            } catch (IOException e) {
                e.printStackTrace();

            }
            return successlogin;
        }
        protected void onPostExecute(Boolean result) {
            progDl.dismiss();
            if(result)
            {
                Toast.makeText(CreateChannelActivity.this, "Channel successfully updated", Toast.LENGTH_LONG).show();
                finish();

            }else{
                Toast.makeText(CreateChannelActivity.this, "Please retry.", Toast.LENGTH_LONG).show();

            }

        }
    }
}
