package me.notify;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by David Rommel, B. on 8/8/15.
 */
public class LoginActivity extends Activity{
    FrameLayout frameLcreateAccount, frameLlogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        frameLcreateAccount = (FrameLayout) findViewById(R.id.frameLcreateAccount);
        frameLlogin = (FrameLayout) findViewById(R.id.frameLlogin);

        frameLcreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(intent);
            }
        });
    }
}
