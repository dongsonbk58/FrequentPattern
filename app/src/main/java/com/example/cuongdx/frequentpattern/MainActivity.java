package com.example.cuongdx.frequentpattern;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import android.widget.Toast;

import com.example.cuongdx.frequentpattern.circleprogress.DonutProgress;
import com.example.cuongdx.frequentpattern.model.User;
import com.example.cuongdx.frequentpattern.service.Common;
import com.example.cuongdx.frequentpattern.service.FileResponse;
import com.example.cuongdx.frequentpattern.service.FileService;
import com.example.cuongdx.frequentpattern.service.RequestPermissionHandler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import utils.Application;
import utils.Utils;

public class MainActivity extends AppCompatActivity {

    private RequestPermissionHandler mRequestPermissionHandler;
    ListView lv;
    ArrayList<String> list = new ArrayList<String>();
    ArrayList<String> listone = new ArrayList<String>();
    ArrayAdapter adapter;
    EditText ip;
    TextView textprogress, toolbartext;
    DonutProgress progress;
    Button btn, btn_send;
    private DrawerLayout mdrawerlayout;
    private ActionBarDrawerToggle mtoggle;
    private Toolbar mtoolbar;
    private NavigationView mnav;
    String API_BASE_URL;
    File file;
    String ipaddress;
    String imei;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRequestPermissionHandler = new RequestPermissionHandler();
        mRequestPermissionHandler.requestPermission(this, new String[] {
                Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.USE_SIP, Manifest.permission.READ_EXTERNAL_STORAGE
        }, 123, new RequestPermissionHandler.RequestPermissionListener() {

            @Override
            public void onSuccess() {
                Toast.makeText(MainActivity.this, "request permission Success", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailed() {
                Toast.makeText(MainActivity.this, "request permission failure", Toast.LENGTH_SHORT).show();
            }
        });


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

        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 2);
        } else {
            TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            imei = tm.getDeviceId();
        }
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

        Intent intent = getIntent();
        if (intent != null && intent.getBooleanExtra("install", false)) {
            Application app = (Application) intent.getSerializableExtra("app");
            ScanOne s = new ScanOne();
            s.execute(app);
        }
    }

    protected void uploadfile(String ip, String imei) {
        API_BASE_URL = "http://"+ Common.ip+":8080/Server_X/";
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(4, TimeUnit.HOURS)
                .readTimeout(4, TimeUnit.HOURS)
                .writeTimeout(4, TimeUnit.HOURS)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(new NullOnEmptyConverterFactory())
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        FileService service = retrofit.create(FileService.class);
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        builder.addFormDataPart("files", file.getName(), RequestBody.create(MediaType.parse("text/*"), file));

        ipaddress = getIp(getApplicationContext());
        MultipartBody requestBody = builder.build();
        Call<FileResponse> call = service.sendfile(imei, ipaddress, requestBody);
        call.enqueue(new Callback<FileResponse>() {
            @Override
            public void onResponse(Call<FileResponse> call, Response<FileResponse> response) {
                Toast.makeText(getBaseContext(), "complete", Toast.LENGTH_LONG).show();
                Intent myIntent = new Intent(getApplicationContext(), Main2Activity.class);
                startActivityForResult(myIntent, 0);
            }
            @Override
            public void onFailure(Call<FileResponse> call, Throwable t) {
                Toast.makeText(getBaseContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
    public class NullOnEmptyConverterFactory extends Converter.Factory {

        @Override
        public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
            final Converter<ResponseBody, ?> delegate = retrofit.nextResponseBodyConverter(this, type, annotations);
            return new Converter<ResponseBody, Object>() {
                @Override
                public Object convert(ResponseBody body) throws IOException {
                    if (body.contentLength() == 0) return null;
                    return delegate.convert(body);                }
            };
        }
    }

    public static String getIp(Context context) {
        WifiManager wifiManager =  (WifiManager) context.getSystemService(context.WIFI_SERVICE);
        WifiInfo wifiInf = wifiManager.getConnectionInfo();
        int ipAddress = wifiInf.getIpAddress();
        String ip = String.format("%d.%d.%d.%d", (ipAddress & 0xff),(ipAddress >> 8 & 0xff),(ipAddress >> 16 & 0xff),(ipAddress >> 24 & 0xff));
        return ip;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mtoggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 2:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {

                }
                break;

            default:
                break;
        }
        mRequestPermissionHandler.onRequestPermissionsResult(requestCode, permissions,
                grantResults);
    }




    class ScanAll extends AsyncTask<Void, Application, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            textprogress.setText("Scanning");
        }


        @Override
        protected Void doInBackground(Void... params) {
            try {
                Utils.run(getApplicationContext(), imei);
                for (Application app : Utils.listApp) {
                    SystemClock.sleep(10);
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
            if (list.contains(s) == false) {
                list.add(s);
            }
            adapter.notifyDataSetChanged();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            adapter.notifyDataSetChanged();
            File sdcard = Utils.getDirectory();
            file = new File(sdcard, "transaction_" + imei + ".txt");
            uploadfile("",imei);
            textprogress.setText("Scan complete");
        }
    }

    class ScanOne extends AsyncTask<Application, Application, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            textprogress.setText("Scanning");
        }

        @Override
        protected String doInBackground(Application... params) {
            Application app = params[0];
            String s = app.getName() + " --> " + Arrays.toString(app.getListPermission());
            String s1 = app.getName()+": "+Arrays.toString(app.getListPermission())+": 0";
            if (list.contains(s) == false) {
                list.add(s);
            }
            if (listone.contains(s1) == false) {
                listone.add(s1);
            }
            try {
                File dirctory = Utils.getDirectory();
                File item = new File(dirctory, "transaction_"+imei+".txt");
                PrintWriter pw = new PrintWriter(item);
                pw.print(s1.replace("[","").replace("]","").replace(",",""));
                pw.println();
                pw.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            return s;
        }

        @Override
        protected void onProgressUpdate(Application... values) {
            super.onProgressUpdate(values);
            Application app = values[0];
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
            textprogress.setText("Scan complete");
            File sdcard = Utils.getDirectory();
            file = new File(sdcard, "transaction_"+imei+".txt");
            uploadfile("",imei);
        }
    }
}


