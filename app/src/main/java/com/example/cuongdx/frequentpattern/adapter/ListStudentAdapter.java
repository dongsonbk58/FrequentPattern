package com.example.cuongdx.frequentpattern.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.cuongdx.frequentpattern.R;
import com.example.cuongdx.frequentpattern.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dong Son on 26-Oct-17.
 */

public class ListStudentAdapter extends ArrayAdapter<User> {
//    private int lastPosition = -1;
//    Context mContext;
    private static class ViewHolder {
        TextView ten;
        TextView mssv;
        TextView lop;
        ImageView img;
    }

    public ListStudentAdapter(Context context, ArrayList<User> users) {
        super(context, R.layout.layout_row_view, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

//        final View result;
        User user = getItem(position);
        ViewHolder viewHolder;
        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.layout_row_view, parent, false);
            viewHolder.ten = (TextView) convertView.findViewById(R.id.tvten);
            viewHolder.mssv = (TextView) convertView.findViewById(R.id.tvmssv);
            viewHolder.lop = (TextView) convertView.findViewById(R.id.tvlop);
            viewHolder.img = (ImageView) convertView.findViewById(R.id.img);

//            result=convertView;
            convertView.setTag(viewHolder);
        } else {

//            result=convertView;
            viewHolder = (ViewHolder) convertView.getTag();
        }

//        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
//        result.startAnimation(animation);
//        lastPosition = position;

        viewHolder.ten.setText("Tên: "+ user.getTen());
        viewHolder.lop.setText("Lớp: "+user.getLop());
        viewHolder.mssv.setText("MSSV: "+user.getMssv());

        return convertView;
    }
}
