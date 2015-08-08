package me.notify;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by David Rommel, B. on 8/9/15.
 */
public class ChannelPage extends BaseActivity {
    String title, description, id, owner_id, category, logo, count;
    SharedPreferences preferences;
    Button subscribe, create;
    TextView Ttitle, Tdescription, Tcategory;
    ImageView mainImage, blurImage;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.channel_page);
        preferences = getSharedPreferences("tokens", Context.MODE_PRIVATE);
        title = getIntent().getStringExtra("title");
        description = getIntent().getStringExtra("description");
        owner_id = getIntent().getStringExtra("owner_id");
        category = getIntent().getStringExtra("category");
        logo = getIntent().getStringExtra("logo");
        count = getIntent().getStringExtra("count");
        id = getIntent().getStringExtra("id");
        subscribe = (Button) findViewById(R.id.subscribe);
        create = (Button) findViewById(R.id.create);
        Ttitle = (TextView) findViewById(R.id.title);
        Tdescription = (TextView) findViewById(R.id.description);
        Tcategory = (TextView) findViewById(R.id.category);
        mainImage = (ImageView) findViewById(R.id.mainImage);
        blurImage = (ImageView) findViewById(R.id.blurImage);

        Picasso.with(ChannelPage.this).load(logo).transform(new BlurTransformation(ChannelPage.this)).into(blurImage);
        Picasso.with(ChannelPage.this).load(logo).into(mainImage);
        Ttitle.setText(title);
        Tdescription.setText(description);
        Tcategory.setText(category);

        if(preferences.getString("id", "").equals(owner_id)){
            create.setVisibility(View.VISIBLE);
            subscribe.setVisibility(View.GONE);
        }else{
            subscribe.setVisibility(View.VISIBLE);
            create.setVisibility(View.GONE);
        }

    }
}
