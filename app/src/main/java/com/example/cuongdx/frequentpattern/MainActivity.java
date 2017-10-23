package com.example.cuongdx.frequentpattern;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.cuongdx.frequentpattern.circleprogress.DonutProgress;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import utils.Application;
import utils.Utils;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_WRITE_STORAGE = 2;
    ListView lv;
    ArrayList<String> list = new ArrayList<String>();
    ArrayAdapter adapter;
    EditText ip;
    TextView textprogress, toolbartext;
    DonutProgress progress;
    Button btn, btn_send;
    String imei;
    private DrawerLayout mdrawerlayout;
    private ActionBarDrawerToggle mtoggle;
    private Toolbar mtoolbar;
    private NavigationView mnav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn = (Button) findViewById(R.id.button);
        lv = (ListView) findViewById(R.id.lv);
        textprogress = (TextView) findViewById(R.id.textprogress);
        progress = (DonutProgress) findViewById(R.id.progressBar);
        mdrawerlayout = (DrawerLayout) findViewById(R.id.activity_main);
        mtoolbar = (Toolbar) findViewById(R.id.nav_action);
        toolbartext = (TextView) findViewById(R.id.toolbar_text);
        toolbartext.setText("Scan");
        progress.setText(0 + "%");
        setSupportActionBar(mtoolbar);
        mtoggle = new ActionBarDrawerToggle(MainActivity.this, mdrawerlayout, R.string.Open, R.string.Close);
        mdrawerlayout.addDrawerListener(mtoggle);
        progress.setProgress(0);
        mtoggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mnav = (NavigationView) findViewById(R.id.nav);
        mnav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.nav_info:

                        Intent info = new Intent(MainActivity.this, InfoActivity.class);
                        startActivity(info);
                        overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                        finish();
                        break;
                    case R.id.nav_scan:

                        Intent scan = new Intent(MainActivity.this, MainActivity.class);
                        startActivity(scan);
                        overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                        finish();
                        break;
                    case R.id.nav_list_student:
                        Intent list = new Intent(MainActivity.this, ListActivity.class);
                        startActivity(list);
                        overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                        finish();
                        break;
                }
                mdrawerlayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });


        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list);
        lv.setAdapter(adapter);
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        imei = tm.getDeviceId();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    ScanAll s = new ScanAll();
                    s.execute();

                } catch (Exception ex) {

                }
            }
        });

        makeRequest();

        Intent intent = getIntent();
        if (intent != null && intent.getBooleanExtra("install", false)) {
            Application app = (Application) intent.getSerializableExtra("app");
            ScanOne s = new ScanOne();
            s.execute(app);

        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mtoggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    protected void makeRequest() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                REQUEST_WRITE_STORAGE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    class ScanAll extends AsyncTask<Void, Application, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            textprogress.setText("scanning");
        }


        @Override
        protected Void doInBackground(Void... params) {
            try {
                Utils.run(getApplicationContext(), imei);
                for (Application app : Utils.listApp) {
                    SystemClock.sleep(30);
                    publishProgress(app);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Application... values) {
            super.onProgressUpdate(values);
            Application app = values[0];
            int a = Utils.listApp.indexOf(app) * 100 / Utils.listApp.size() + 1;
            progress.setProgress(a);
            progress.setText(a + "%");
            String s = app.getName() + " --> " + Arrays.toString(app.getListPermission());
            System.out.println("update: " + s);
            if (list.contains(s) == false) {
                list.add(s);
            }

            adapter.notifyDataSetChanged();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            adapter.notifyDataSetChanged();
            textprogress.setText("scan complete");
        }
    }

    class ScanOne extends AsyncTask<Application, Application, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            textprogress.setText("scanning");
        }


        @Override
        protected String doInBackground(Application... params) {
            Application app = params[0];
//            app = Utils.scanApp(getApplicationContext(), app);
            String s = app.getName() + " --> " + Arrays.toString(app.getListPermission());
            if (list.contains(s) == false) {
                list.add(s);
            }
            return s;
        }

        @Override
        protected void onProgressUpdate(Application... values) {
            super.onProgressUpdate(values);
            Application app = values[0];
//            progress.setProgress(Arrays.asList(Utils.listApp).indexOf(app));
            progress.setProgress(Utils.listApp.size());
            progress.setText((Utils.listApp).indexOf(app) + "");
            String s = app.getName() + " --> " + Arrays.toString(app.getListPermission());
            if (list.contains(s) == false) {
                list.add(s);
            }
            adapter.notifyDataSetChanged();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            textprogress.setText("scan complete");
        }
    }
}


