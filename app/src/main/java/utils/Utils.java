package utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.example.cuongdx.frequentpattern.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.StringTokenizer;

public class Utils {
    public static ArrayList<Permission> listPermission;
    public static int numTransactions;
    static ArrayList<String> transacsion = new ArrayList<String>();
    public static ArrayList<Application> listApp = new ArrayList<Application>();

    public static File genTransaction(Context context, String imei) {
        PackageManager pm = context.getPackageManager();
        File item = null;
        List<PackageInfo> info = pm.getInstalledPackages(PackageManager.GET_PERMISSIONS);

        int count = 0;
        listPermission = getListPermission(context);
        try {

            File dirctory = getDirectory();
            item = new File(dirctory, "transaction_"+imei+".txt");
            PrintWriter pw = new PrintWriter(item);
            for (PackageInfo pi : info) {
                long start = 0, end = 0;
                start = System.currentTimeMillis();

                String[] permission = pi.requestedPermissions;
                StringBuilder sb = new StringBuilder();
                String name = pi.packageName;
                int k = 0;
                if (permission != null && permission.length > 0) {
                    pw.print(name + ":");
                    for (String s : permission){
                        if (getID(listPermission, s) != -1) {
                            sb.append(getID(listPermission, s) + " ");
                            pw.print(getID(listPermission, s) + " ");
                        }
                    }
                    if (sb.length() > 0) {
                        transacsion.add(sb.toString());
                    }
                    end = System.currentTimeMillis();
                    pw.print(":"+(end-start));
                    pw.println();
                }



                ApplicationInfo ai = pm.getApplicationInfo(name, 0);
                Application app;
                if (ai != null)
                    app = new Application(converStringtoArray(sb.toString()), pm.getApplicationLabel(ai).toString());
                else
                    app = new Application(converStringtoArray(sb.toString()), name);
                listApp.add(app);
            }

            pw.close();
        } catch (Exception e) {

        }
        numTransactions = transacsion.size();

        Collections.sort(listApp, new Comparator<Application>() {

            @Override
            public int compare(Application o1, Application o2) {
                int[] arr1 = o1.getListPermission();
                int arr2[] = o2.getListPermission();
                if (arr1.length < arr2.length) return -1;
                if (arr1.length > arr2.length) return 1;
                return arr1.length - arr2.length;
            }
        });
        return item;
    }

    public static int getID(ArrayList<Permission> list, String value) {
        for (Permission p : list) {
            if (p.getName().equals(value))
                return p.getId();
        }
        return -1;
    }


    public static File getDirectory() {
        File root = android.os.Environment.getExternalStorageDirectory();
        File directory = new File(root.getAbsolutePath() + File.separator + "Frequent");
        if (!directory.exists())
            directory.mkdir();
        return directory;
    }

    public static void run(Context context, String id) throws FileNotFoundException,
            IOException {
          genTransaction(context, id);
    }


    public static ArrayList<Permission> getListPermission(Context context) {
        ArrayList<Permission> list = new ArrayList<Permission>();
        int count = 0;
        Permission permission;
        try {
            InputStream is = context.getResources().openRawResource(R.raw.list_permission);
            BufferedReader bfr = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = bfr.readLine()) != null) {
                permission = new Permission(++count, line);
                list.add(permission);
            }
        } catch (Exception ex) {

        }
        return list;
    }

    public static Application scanApp(Context context, Application app) {
        boolean b;
        if(app!=null) {
            int list[] = app.getListPermission();
            System.out.println("length: " + list.length);
            if (list.length == 0)
                app.setResult(true);
            else if (list.length < 10) {
                GenRule g = new GenRule(list, context);
                b = g.findRule(context);
                app.setResult(b);
                System.out.println("result: " + app.getName());
            }
        }

        return app;

    }

    public static int[] converStringtoArray(String s) {
        StringTokenizer st = new StringTokenizer(s, " ");
        int arr[] = new int[st.countTokens()];
        int count = 0;
        while (st.hasMoreTokens())
            arr[count++] = Integer.parseInt(st.nextToken());
        Arrays.sort(arr);
        return arr;
    }


}
