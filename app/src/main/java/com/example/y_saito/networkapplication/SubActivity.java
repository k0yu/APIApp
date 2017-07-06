package com.example.y_saito.networkapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.widget.TextView;

/**
 * Created by y-saito on 2017/07/05.
 */

public class SubActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sub);
        Intent intent = getIntent();
        Event event = (Event) intent.getSerializableExtra("event");

        TextView title = (TextView) findViewById( R.id.title );
        title.setText(event.getTitle());

        TextView catchCopy = (TextView) findViewById( R.id.catchCopy );
        catchCopy.setText(event.getCatchCopy());

        WebView description = (WebView) findViewById( R.id.description );
        String html = "<html><head><meta http-equiv='content-type' content='text/html;charset=UTF-8'></head><body>" +
                event.getDescription() +
                "</body></html>";
        description.loadData(html, "text/html; charset=utf-8", "utf-8");

//        WebView description = (WebView) findViewById( R.id.description );
//        String url = "http://atnd.org/events/" + event.getId();
//        description.loadUrl(url);
    }
}
