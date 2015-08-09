package me.notify;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Created by David Rommel, B. on 8/8/15.
 */
public class BaseActivity extends ActionBarActivity {
    SharedPreferences preferences;
    ActionBar actionBar;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = getSharedPreferences("tokens", Context.MODE_PRIVATE);
        actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            if (getFragmentManager().getBackStackEntryCount() > 0 ){
                getFragmentManager().popBackStack();
            } else {
                super.onBackPressed();
            }
        }else if(item.getItemId() == R.id.action_logout){

            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("id", "");
            editor.putString("email", "");
            editor.putString("first_name", "");
            editor.putString("last_name", "");
            editor.putString("age", "");
            editor.putString("auth_token", "");
            editor.apply();

            Intent intent = new Intent(BaseActivity.this, FirstActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}