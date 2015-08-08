package me.notify;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;

/**
 * Created by David Rommel, B. on 8/9/15.
 */
public class CreateChannelActivity extends BaseActivity{
    EditText description, name;
    Spinner category, status;
    FrameLayout frameLsubmit;
    SharedPreferences preferences;
    TaskRegisterAndLogin taskRegisterAndLogin;
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

        Resources res = getResources();
        ArrayAdapter<String> mAdapter;
        mAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, res.getStringArray(R.array.category_arrays));
        ArrayAdapter<String> mAdapter1;
        mAdapter1 = new ArrayAdapter<>(this, R.layout.spinner_item, res.getStringArray(R.array.status_arrays));
        category.setAdapter(mAdapter);
        status.setAdapter(mAdapter1);
    }
}
