package com.example.cuongdx.frequentpattern;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cuongdx.frequentpattern.adapter.ListStudentAdapter;
import com.example.cuongdx.frequentpattern.model.User;
import com.example.cuongdx.frequentpattern.service.FileService;
import com.example.cuongdx.frequentpattern.service.ListStudent;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ListActivity extends AppCompatActivity {
    private DrawerLayout mdrawerlayout;
    private ActionBarDrawerToggle mtoggle1;
    private Toolbar mtoolbar;
    private NavigationView mnav;
    private TextView toolbartext;
    private ListView listView;
    private ArrayList<User> contactList;
    private ListStudentAdapter adapter;
    private TextView textlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contactList = new ArrayList<>();
        listView = (ListView) findViewById(R.id.list);
        textlist = (TextView) findViewById(R.id.textregis);

        setContentView(R.layout.activity_list);
        mdrawerlayout = (DrawerLayout) findViewById(R.id.activity_info);
        mtoolbar = (Toolbar) findViewById(R.id.nav_action);
        setSupportActionBar(mtoolbar);
        toolbartext = (TextView) findViewById(R.id.toolbar_text);
        toolbartext.setText("List Student");
        mtoggle1= new ActionBarDrawerToggle(ListActivity.this, mdrawerlayout, R.string.Open, R.string.Close);
        mdrawerlayout.addDrawerListener(mtoggle1);
        mtoggle1.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mtoolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mdrawerlayout.openDrawer(GravityCompat.START);
            }
        });
        mnav = (NavigationView) findViewById(R.id.nav);
        mnav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.nav_info:
                        Intent info = new Intent(ListActivity.this,InfoActivity.class);
                        startActivity(info);
                        overridePendingTransition(R.anim.pull_in_right,R.anim.push_out_left);
                        finish();
                        break;
                    case R.id.nav_scan:
                        Intent scan = new Intent(ListActivity.this,MainActivity.class);
                        startActivity(scan);
                        overridePendingTransition(R.anim.pull_in_right,R.anim.push_out_left);
                        finish();
                        break;
                    case R.id.nav_list_student:
                        Intent list = new Intent(ListActivity.this,ListActivity.class);
                        startActivity(list);
                        overridePendingTransition(R.anim.pull_in_right,R.anim.push_out_left);
                        finish();
                        break;

                }
                mdrawerlayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
        getAllUser();
    }

    public void getAllUser(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.151.133:8080/Server_X/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        FileService service = retrofit.create(FileService.class);
        Call<JsonObject> jsonCall = service.readJson();
        jsonCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Toast.makeText(ListActivity.this, response.body().toString(), Toast.LENGTH_LONG).show();
//                contactList = response.body().getListStudent();
//                Toast.makeText(ListActivity.this, contactList.size(), Toast.LENGTH_LONG).show();
//                adapter = new ListStudentAdapter(ListActivity.this, contactList);
//                listView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }
}
