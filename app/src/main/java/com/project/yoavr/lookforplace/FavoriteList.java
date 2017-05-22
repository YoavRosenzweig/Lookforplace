package com.project.yoavr.lookforplace;

import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class FavoriteList extends AppCompatActivity {
    MysqlHelper mysqlHelper;
    Cursor cursor;
    SimpleCursorAdapter adpter;
    ListView listView;
    IfBatteryCharged resiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favorite_places);

        listView= (ListView) findViewById(R.id.listview);

        mysqlHelper = new MysqlHelper(this);
        cursor = mysqlHelper.getReadableDatabase().query("myplace", null, null, null, null, null, null);
        String[] fromColoms = new String[]{"name"};
        int[] toTV = new int[]{android.R.id.text1};

        adpter = new SimpleCursorAdapter(this,android.R.layout.simple_list_item_1, cursor, fromColoms, toTV);
        listView.setAdapter(adpter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        cursor= mysqlHelper.getReadableDatabase().query("myplace", null, null, null, null, null, null);
        adpter.swapCursor(cursor);

        resiver = new IfBatteryCharged();

        IntentFilter ifilter = new IntentFilter();
        ifilter.addAction(Intent.ACTION_POWER_CONNECTED);
        ifilter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        registerReceiver(resiver, ifilter);

    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(resiver);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.setting, menu);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.favorite) {
            Intent intent = new Intent(this, FavoriteList.class);
            startActivity(intent);

        }
        if (item.getItemId() == R.id.mysetting) {
            Intent intent = new Intent(this, Mysetting.class);
            startActivity(intent);
        }

        return true;
    }
}

