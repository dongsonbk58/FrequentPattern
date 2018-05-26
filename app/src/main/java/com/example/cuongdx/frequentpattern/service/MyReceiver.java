package com.example.cuongdx.frequentpattern.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.example.cuongdx.frequentpattern.MainActivity;

import java.io.Serializable;
import java.util.ArrayList;

import utils.Application;
import utils.Permission;
import utils.Utils;

import static utils.Utils.converStringtoArray;
import static utils.Utils.getID;

public class MyReceiver extends BroadcastReceiver {
    String transaction;
    ArrayList<Integer> list;

    public MyReceiver() {

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        list = new ArrayList<Integer>();
        ArrayList<Permission> listPermissions = Utils.getListPermission(context);
        PackageManager pm = context.getPackageManager();
        String packageName = intent.getDataString();
        String str = packageName.substring(8);
        System.out.println("packageName:" + str);

        try {
            PackageInfo pi = pm.getPackageInfo(str, PackageManager.GET_PERMISSIONS);
            String[] permission = pi.requestedPermissions;
            StringBuilder sb = new StringBuilder();
            int[] temp = null;
            int k = 0;
            if (permission != null) {
                temp = new int[permission.length];
                for (String s : permission) {
                    System.out.println("Permission: " + s);
                    int id = getID(listPermissions, s);
                    if (id != -1) {
                        sb.append(id + " ");
                    }

                }
            }
            ApplicationInfo ai = pm.getApplicationInfo(str, 0);
            Application app;
            int[] list = converStringtoArray(sb.toString());
//            if (ai != null)
//                app = new Application(list, pm.getApplicationLabel(ai).toString());
//            else
            app = new Application(list, str);
            Intent i = new Intent(context, MainActivity.class);
            i.putExtra("app", (Serializable) app);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.putExtra("install",true);
            context.startActivity(i);
            transaction = sb.toString();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (transaction != null)
            Log.i("transaction: ", transaction);
    }

}
