package com.example.cuongdx.frequentpattern;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.cuongdx.frequentpattern.adapter.KqAdapter;
import com.example.cuongdx.frequentpattern.model.KQ;
import com.example.cuongdx.frequentpattern.service.Common;
import com.example.cuongdx.frequentpattern.service.FileService;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Main2Activity extends AppCompatActivity {
    ListView lv;
    ArrayList<String> list = new ArrayList<String>();
    ArrayList<KQ> listkq = new ArrayList<KQ>();
    KqAdapter adapter;
    String API_BASE_URL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Detection Result");
        setSupportActionBar(toolbar);
        lv = (ListView)findViewById(R.id.lv);
        getAllResult();
    }

    public void getAllResult() {
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(60, TimeUnit.MINUTES)
                .readTimeout(60, TimeUnit.MINUTES)
                .writeTimeout(60, TimeUnit.MINUTES)
                .build();
//        listkq.clear();
        API_BASE_URL = "http://"+ Common.ip+":8080/Server_X/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(new NullOnEmptyConverterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        FileService service = retrofit.create(FileService.class);
        Call<JsonObject> jsonCall = service.readKQJson();
        jsonCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JSONObject ob = null;
                try {
                    ob = new JSONObject(response.body().toString());
                    JSONArray arr = ob.getJSONArray("kq");

                    for (int i = 0; i < Integer.parseInt(arr.length() + ""); i++) {
                        String ten = arr.getJSONObject(i).getString("ten");
                        String kq = arr.getJSONObject(i).getString("kq");
                        listkq.add(new KQ(ten, kq));
                        list.add(ten+" : " + kq);
                    }
                    adapter = new KqAdapter(getApplicationContext(), listkq);
                    lv.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

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
}


