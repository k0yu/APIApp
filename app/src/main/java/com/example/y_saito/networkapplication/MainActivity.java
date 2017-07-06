package com.example.y_saito.networkapplication;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class MainActivity extends AppCompatActivity {
    EditText editText;
    Button button;
    // フッターのプログレスバー（クルクル）
    View mFooter;
    // 1ページ辺りの項目数
    Integer per_page = 20;
    ListView listView;
    ArrayList<Event> list;
    EventListAdapter adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = (EditText) findViewById(R.id.edit_text);

        button = (Button) findViewById(R.id.button);
        listView = (ListView) findViewById(R.id.listView);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listView.addFooterView(getFooter());
                // エディットテキストのテキストを取得
                final String keyword = editText.getText().toString();
                list = new ArrayList<>();

                if (checkConnect()) {
                    list = getEventList(keyword, 1);
                    adapter = new EventListAdapter(MainActivity.this, R.layout.event_list, list);

                    listView.setAdapter(adapter);


                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            ListView list = (ListView)parent;
                            Event event = (Event) list.getItemAtPosition(position);
                            Intent intent = new Intent(getApplication(), SubActivity.class);
                            intent.putExtra("event", event);
                            startActivity(intent);
                        }
                    });
                }



                listView.setOnScrollListener(new AbsListView.OnScrollListener() {

                    // スクロール中の処理
                    @Override
                    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                        // 最初とスクロール完了したとき
                        if ((totalItemCount - visibleItemCount) == firstVisibleItem) {

                            // アイテムの数 フッター分の1を引く(+1からstartさせるのでひかない?)
                            int itemCount = totalItemCount;
                            list.addAll(getEventList(keyword, totalItemCount));
                            adapter.notifyDataSetChanged();
                        }
                    }

                    // ListViewがスクロール中かどうか状態を返すメソッドです
                    @Override
                    public void onScrollStateChanged(AbsListView arg0, int arg1) {
                    }
                });
            }

        });

    }

    // エラーメッセージ表示
    private void showLoadError() {
        Toast toast = Toast.makeText(this, "データを取得できませんでした。", Toast.LENGTH_SHORT);
        toast.show();
    }

    private View getFooter() {
        if (mFooter == null) {
            mFooter = getLayoutInflater().inflate(R.layout.listview_footer, null);
        }
        return mFooter;
    }

    private boolean checkConnect(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();

        if (info.isConnected()) {
            Toast.makeText(getApplicationContext(), info.getTypeName() + " connected", Toast.LENGTH_SHORT).show();
            return true;
        }else{
            return false;
        }
    }

    private ArrayList<Event> getEventList(String keyword, int start){
        final ArrayList<Event> eventList = new ArrayList<>();
        AsyncJsonLoader asyncJsonLoader = new AsyncJsonLoader(new AsyncJsonLoader.AsyncCallback() {
            // 実行前
            public void preExecute() {
            }

            // 実行後
            public void postExecute(JSONObject result) {
                if (result == null) {
                    showLoadError(); // エラーメッセージを表示
                    return;
                }
                try {
                    JSONArray eventArray = result.getJSONArray("events");
                    for (int i = 0; i < eventArray.length(); i++) {
                        JSONObject eventObj = eventArray.getJSONObject(i);
                        JSONObject jsonevent = eventObj.getJSONObject("event");
                        //Log.d("title", event.getString("title"));
                        Event event = new Event();
                        event.setTitle(jsonevent.getString("title"));
                        event.setId(jsonevent.getInt("event_id"));
                        event.setCatchCopy(jsonevent.getString("catch"));
                        event.setDescription(jsonevent.getString("description"));
                        eventList.add(event);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    showLoadError(); // エラーメッセージを表示
                }
            }

            // 実行中
            public void progressUpdate(int progress) {
            }

            // キャンセル
            public void cancel() {
            }
        });
        // 処理を実行
        asyncJsonLoader.execute("http://api.atnd.org/events/?format=json&count=" + per_page + "&start=" + start +"&keyword=" + keyword);
        return eventList;
    }
}
