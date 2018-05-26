package com.example.cuongdx.frequentpattern;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cuongdx.frequentpattern.adapter.ListStudentAdapter;
import com.example.cuongdx.frequentpattern.model.User;
import com.example.cuongdx.frequentpattern.service.Common;
import com.example.cuongdx.frequentpattern.service.FileService;
import com.google.gson.JsonObject;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Locale;

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
    private ListView studentlist;
    private ArrayList<User> contactList;
    private ListStudentAdapter adapter;
    private EditText editsearch;
    String API_BASE_URL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        contactList = new ArrayList<User>();
        studentlist = (ListView) findViewById(R.id.liststudent);
        mdrawerlayout = (DrawerLayout) findViewById(R.id.activity_list);
        mtoolbar = (Toolbar) findViewById(R.id.nav_action);
        setSupportActionBar(mtoolbar);
        toolbartext = (TextView) findViewById(R.id.toolbar_text);
        editsearch = (EditText) findViewById(R.id.search);
        toolbartext.setText("List Student");
        mtoggle1 = new ActionBarDrawerToggle(ListActivity.this, mdrawerlayout, R.string.Open, R.string.Close);
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
                        Intent info = new Intent(ListActivity.this, InfoActivity.class);
                        startActivity(info);
                        overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                        finish();
                        break;
                    case R.id.nav_scan:
                        Intent scan = new Intent(ListActivity.this, MainActivity.class);
                        startActivity(scan);
                        overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                        finish();
                        break;
                    case R.id.nav_list_student:
                        Intent list = new Intent(ListActivity.this, ListActivity.class);
                        startActivity(list);
                        overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                        finish();
                        break;

                }
                mdrawerlayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        editsearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                String text = editsearch.getText().toString().toLowerCase(Locale.getDefault());
                adapter.filter(text);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
                // TODO Auto-generated method stub
            }
        });
        getAllUser();


    }

    public void getAllUser() {
        API_BASE_URL = "http://"+ Common.ip+":8080/Server_X/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        FileService service = retrofit.create(FileService.class);
        Call<JsonObject> jsonCall = service.readJson();
        jsonCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JSONObject ob = null;
                try {
                    ob = new JSONObject(response.body().toString());
                    JSONArray arr = ob.getJSONArray("student");

                    for (int i = 0; i < Integer.parseInt(arr.length() + ""); i++) {
                        String ten = arr.getJSONObject(i).getString("ten");
                        String lop = arr.getJSONObject(i).getString("lop");
                        String mssv = arr.getJSONObject(i).getString("mssv");
                        String imei = arr.getJSONObject(i).getString("imei");
                        String mahocphan = arr.getJSONObject(i).getString("mahocphan");
                        String malop = arr.getJSONObject(i).getString("malop");
                        User user = new User(ten, lop, mssv, malop, mahocphan, imei);
                        contactList.add(user);
                    }
                    adapter = new ListStudentAdapter(getApplicationContext(), contactList);
                    studentlist.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }
}
