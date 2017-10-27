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
import java.util.Locale;

/**
 * Created by Dong Son on 26-Oct-17.
 */

public class ListStudentAdapter extends ArrayAdapter<User> {

    Context mContext;
    private ArrayList<User> worldpopulationlist = null;
    private ArrayList<User> arraylist;
    private static class ViewHolder {
        TextView ten;
        TextView mssv;
        TextView lop;
        TextView malop;
        TextView mahocphan;
        ImageView img;
    }

    public ListStudentAdapter(Context context, ArrayList<User> users) {
        super(context, R.layout.layout_row_view, users);
        mContext = context;
        this.worldpopulationlist = users;
        this.arraylist = new ArrayList<User>();
        this.arraylist.addAll(worldpopulationlist);

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
            viewHolder.malop = (TextView) convertView.findViewById(R.id.tvmalop);
            viewHolder.mahocphan = (TextView) convertView.findViewById(R.id.tvmahocphan);
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
        viewHolder.malop.setText("Mã lớp: "+user.getMalop());
        viewHolder.mahocphan.setText("Mã học phần: "+user.getMahocphan());

        return convertView;
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        worldpopulationlist.clear();
        if (charText.length() == 0) {
            worldpopulationlist.addAll(arraylist);
        }
        else
        {
            for (User wp : arraylist)
            {
                if (wp.getMssv().toLowerCase(Locale.getDefault()).contains(charText))
                {
                    worldpopulationlist.add(wp);
                }else{
                    if(wp.getTen().toLowerCase(Locale.getDefault()).contains(charText)){
                        worldpopulationlist.add(wp);
                    }else{
                        if(wp.getLop().toLowerCase(Locale.getDefault()).contains(charText)){
                            worldpopulationlist.add(wp);
                        }else{
                            if(wp.getMahocphan().toLowerCase(Locale.getDefault()).contains(charText)){
                                worldpopulationlist.add(wp);
                            }else{
                                if(wp.getMalop().toLowerCase(Locale.getDefault()).contains(charText)){
                                    worldpopulationlist.add(wp);
                                }
                            }
                        }
                    }
                }
            }
        }
        notifyDataSetChanged();
    }
}
