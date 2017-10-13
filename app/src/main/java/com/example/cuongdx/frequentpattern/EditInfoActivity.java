package com.example.cuongdx.frequentpattern;

import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class EditInfoActivity extends AppCompatActivity {
    private DrawerLayout mdrawerlayout;
    private ActionBarDrawerToggle mtoggle1;
    private Toolbar mtoolbar;
    private NavigationView mnav;
    private TextView toolbartext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_info);
        setContentView(R.layout.activity_list);
        mdrawerlayout = (DrawerLayout) findViewById(R.id.activity_info);
        mtoolbar = (Toolbar) findViewById(R.id.nav_action);
        setSupportActionBar(mtoolbar);
        toolbartext = (TextView) findViewById(R.id.toolbar_text);
        toolbartext.setText("List Student");
        mtoggle1= new ActionBarDrawerToggle(EditInfoActivity.this, mdrawerlayout, R.string.Open, R.string.Close);
        mdrawerlayout.addDrawerListener(mtoggle1);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mtoolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
