package com.example.cuongdx.frequentpattern;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cuongdx.frequentpattern.model.User;
import com.example.cuongdx.frequentpattern.service.FileResponse;
import com.example.cuongdx.frequentpattern.service.FileService;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.Normalizer;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import utils.Utils;

public class InfoActivity extends AppCompatActivity {
    private DrawerLayout mdrawerlayout;
    private ActionBarDrawerToggle mtoggle1;
    private Toolbar mtoolbar;
    private NavigationView mnav;
    private TextView toolbartext;
    Button btn_send;
    EditText ten,lop,masinhvien,mahocphan,malop;
    String API_BASE_URL;
    File file;
    String imei;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        mdrawerlayout = (DrawerLayout) findViewById(R.id.activity_info);
        mtoolbar = (Toolbar) findViewById(R.id.nav_action);
        setSupportActionBar(mtoolbar);
        toolbartext = (TextView) findViewById(R.id.toolbar_text);
        btn_send = (Button) findViewById(R.id.bt_sendinfo);
        btn_send.setText("SEND");
        ten = (EditText) findViewById(R.id.ed_name);
        lop = (EditText) findViewById(R.id.ed_lop);
        malop = (EditText) findViewById(R.id.ed_malop);
        masinhvien = (EditText) findViewById(R.id.ed_mssv);
        mahocphan = (EditText) findViewById(R.id.ed_mahocphan);
        ten.setText("");
        lop.setText("");
        malop.setText("");
        mahocphan.setText("");
        masinhvien.setText("");

        mtoggle1 = new ActionBarDrawerToggle(InfoActivity.this, mdrawerlayout, R.string.Open, R.string.Close);
        toolbartext.setText("Info");
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
                        Intent info = new Intent(InfoActivity.this, InfoActivity.class);
                        startActivity(info);
                        overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                        finish();
                        break;
                    case R.id.nav_scan:
                        Intent scan = new Intent(InfoActivity.this, MainActivity.class);
                        startActivity(scan);
                        overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                        finish();
                        break;
                    case R.id.nav_list_student:
                        Intent list = new Intent(InfoActivity.this, ListActivity.class);
                        startActivity(list);
                        overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                        finish();
                        break;
                }
                mdrawerlayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        imei = tm.getDeviceId();

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                File sdcard = Utils.getDirectory();
                file = new File(sdcard, "transaction_" + imei + ".txt");

                if(ten.getText().length()==0 || lop.getText().length()==0 || malop.getText().length()==0 || masinhvien.getText().length()==0 || mahocphan.getText().length()==0 ){
                    Toast.makeText(getBaseContext(), "Please fill all feild", Toast.LENGTH_SHORT).show();
                }else{
                    user = new User(deAccent(ten.getText().toString()),deAccent(lop.getText().toString()),deAccent(masinhvien.getText().toString()), deAccent(malop.getText().toString()),deAccent(mahocphan.getText().toString()), deAccent(imei.toString()));
                    StringBuilder text = new StringBuilder();
                    try {
                        BufferedReader br = new BufferedReader(new FileReader(file));
                        String line;
                        while ((line = br.readLine()) != null) {
                            text.append(line);
                            text.append('\n');
                        }
                        br.close();
                    } catch (IOException e) {
                    }
                    if(file.length()==0 || !file.exists()){
                        Toast.makeText(getBaseContext(), "Please Scan now", Toast.LENGTH_SHORT).show();
                    }else{
                        uploadfile("",user, imei);
                    }


                }
            }
        });
    }

    public static String deAccent(String str) {
        try {
            String temp = Normalizer.normalize(str, Normalizer.Form.NFD);
            Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
            return pattern.matcher(temp).replaceAll("").replaceAll("đ", "d").replace("Đ","D");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    protected void uploadfile(String ip,User user, String imei) {
        API_BASE_URL = "http://202.191.58.39:8080/Server_X/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        FileService service = retrofit.create(FileService.class);
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        builder.addFormDataPart("files", file.getName(), RequestBody.create(MediaType.parse("text/*"), file));

        MultipartBody requestBody = builder.build();
        Call<FileResponse> call = service.file(imei,user.getTen(),user.getLop(),user.getMalop(),user.getMssv(),user.getMahocphan(),requestBody);
        call.enqueue(new Callback<FileResponse>() {
            @Override
            public void onResponse(Call<FileResponse> call, Response<FileResponse> response) {
                Toast.makeText(getBaseContext(), "Data was sent to server", Toast.LENGTH_LONG).show();
                btn_send.setText("SENT");
            }
            @Override
            public void onFailure(Call<FileResponse> call, Throwable t) {
                Toast.makeText(getBaseContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}
