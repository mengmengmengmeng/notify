package me.notify;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

/**
 * Created by David Rommel, B. on 8/8/15.
 */
public class SplashScreen extends Activity {
    SharedPreferences preferences;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        preferences = getSharedPreferences("tokens", Context.MODE_PRIVATE);
        token = preferences.getString("auth_token", "");

        int SPLASH_TIME_OUT = 3000;
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                if ("".equals(token)) {
                    Intent i = new Intent(SplashScreen.this, FirstActivity.class);
                    startActivity(i);

                    finish();
                }else{
                    Intent i = new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(i);

                    finish();
                }

            }
        }, SPLASH_TIME_OUT);
    }

}