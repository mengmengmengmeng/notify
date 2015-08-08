package me.notify;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by David Rommel, B. on 8/8/15.
 */
public class FirstActivity extends Activity{
    FrameLayout frameLcreateAccount, frameLlogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_activity);

        frameLcreateAccount = (FrameLayout) findViewById(R.id.frameLcreateAccount);
        frameLlogin = (FrameLayout) findViewById(R.id.frameLlogin);

        frameLcreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FirstActivity.this, RegistrationActivity.class);
                startActivity(intent);
            }
        });

        frameLlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FirstActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}
