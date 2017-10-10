package com.example.cuongdx.frequentpattern;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cuongdx.frequentpattern.service.FileResponse;
import com.example.cuongdx.frequentpattern.service.FileService;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import utils.Application;
import utils.Utils;
import net.sourceforge.jtds.jdbc.Driver;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_WRITE_STORAGE = 2;
    ListView lv;
    ArrayList<String> list = new ArrayList<String>();
    ArrayAdapter adapter;
    EditText ip;
    TextView textprogress;
    ProgressBar progress;
    Button btn, btn1;
    String API_BASE_URL;
    File file;
    String imei;
    private static final String url = "jdbc:mysql://192.168.0.101:80/permission_application";
    private static final String user = "root";
    private static final String pass = "vertrigo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn = (Button) findViewById(R.id.button);
        btn1 = (Button) findViewById(R.id.button1);
        lv = (ListView) findViewById(R.id.lv);
        ip = (EditText) findViewById(R.id.etid);
        textprogress = (TextView) findViewById(R.id.textprogress);
        progress = (ProgressBar) findViewById(R.id.progressBar);
        ip.setText("");

        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list);
        lv.setAdapter(adapter);
        TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
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

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                File sdcard = Utils.getDirectory();
                file = new File(sdcard, "transaction_"+imei+".txt");
//                Connect task=new Connect();
//                task.execute();

//                String myUrl = "jdbc:mysql://10.0.0.2:80/permission";
//
//                try {
//                    Class.forName("com.mysql.jdbc.Driver");
//                    Connection conn = DriverManager.getConnection(myUrl, "root", "");
//
//                    // the mysql insert statement
//                    String query = " insert into user (stt, name)"+ " values (?, ?)";
//                    // create the mysql insert preparedstatement
//                    PreparedStatement preparedStmt = conn.prepareStatement(query);
//                    preparedStmt.setInt(1, 5);
//                    preparedStmt.setString (2, "Rubble");
//                    Log.e("son","abana");
//
//
//                    // execute the preparedstatement
//                    preparedStmt.execute();
//                    preparedStmt.close();
//                    conn.close();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//


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



                if (ip.getText().toString() != "") {
                    uploadfile(ip.getText().toString(),imei);
                } else {
                    Toast.makeText(MainActivity.this, "you need fill ip of server", Toast.LENGTH_LONG).show();
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


    protected void uploadfile(String ip, String imei) {
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
        Call<FileResponse> call = service.file(imei,requestBody);
        call.enqueue(new Callback<FileResponse>() {
            @Override
            public void onResponse(Call<FileResponse> call, Response<FileResponse> response) {

            }

            @Override
            public void onFailure(Call<FileResponse> call, Throwable t) {
                Toast.makeText(getBaseContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
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

    private class Connect extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            String connection="";

            // create a mysql database connection
            //String myDriver = "org.gjt.mm.mysql.Driver";
            String myUrl = "jdbc:mysql://192.168.0.101:3306/permission";

            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection(myUrl, "root", "");

                // the mysql insert statement
                String query = " insert into user (stt, name)"+ " values (?, ?)";
                // create the mysql insert preparedstatement
                PreparedStatement preparedStmt = conn.prepareStatement(query);
                preparedStmt.setInt(1, 5);
                preparedStmt.setString (2, "Rubble");
                Log.e("son","abana");


                // execute the preparedstatement
                preparedStmt.execute();

                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            Log.e("son","abana");
            return connection;

        }


        @Override
        protected void onPostExecute(String result) {
        }
    }


    class ScanAll extends AsyncTask<Void, Application, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            textprogress.setText("scanning");
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            adapter.notifyDataSetChanged();
            textprogress.setText("scan complete");
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Utils.run(getApplicationContext(),imei);
                for (Application app : Utils.listApp) {
//                    app = Utils.scanApp(getApplicationContext(), app);
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
            progress.setProgress((Utils.listApp).indexOf(app));
            String s = app.getName() + " --> " + Arrays.toString(app.getListPermission());
            System.out.println("update: " + s);
            if (list.contains(s) == false) {
                list.add(s);
            }

            adapter.notifyDataSetChanged();
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
            progress.setProgress(Arrays.asList(Utils.listApp).indexOf(app));
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


