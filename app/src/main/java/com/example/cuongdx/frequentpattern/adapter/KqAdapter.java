package com.example.cuongdx.frequentpattern.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cuongdx.frequentpattern.R;
import com.example.cuongdx.frequentpattern.model.KQ;

import java.util.ArrayList;

/**
 * Created by Dong Son on 27-May-18.
 */

public class KqAdapter extends ArrayAdapter<KQ> {
        public KqAdapter(Context context, ArrayList<KQ> users) {
            super(context, 0, users);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            KQ kq = getItem(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_row_kq, parent, false);
            }
            TextView tvName = (TextView) convertView.findViewById(R.id.tvname);
            TextView tvHome = (TextView) convertView.findViewById(R.id.tvkq);
            ImageView imgCheck = (ImageView) convertView.findViewById(R.id.img);
            tvName.setText(kq.getTen());
            tvHome.setText(kq.getKq());
            if(kq.getKq().equals("normal")) {
                imgCheck.setBackgroundResource(R.drawable.sign_check_icon);
            } else {
                imgCheck.setBackgroundResource(R.drawable.no);
            }
            return convertView;
        }
}
