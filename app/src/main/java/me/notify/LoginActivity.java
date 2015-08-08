package me.notify;

import android.os.Bundle;

/**
 * Created by David Rommel, B. on 8/9/15.
 */
public class LoginActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_layout);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.layoutToReplace, new LoginFragment())
                    .commit();
        }
    }
}