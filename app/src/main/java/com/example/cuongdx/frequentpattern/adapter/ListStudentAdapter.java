package com.example.cuongdx.frequentpattern.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.cuongdx.frequentpattern.R;
import com.example.cuongdx.frequentpattern.model.User;

import java.util.List;

/**
 * Created by Dong Son on 26-Oct-17.
 */

public class ListStudentAdapter extends ArrayAdapter<User> {
    List<User> list;
    Context context;
    private LayoutInflater mInflater;

    // Constructors
    public ListStudentAdapter(Context context, List<User> objects) {
        super(context, 0, objects);
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        list = objects;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder vh;
        if (convertView == null) {
            View view = mInflater.inflate(R.layout.layout_row_view, parent, false);
            vh = ViewHolder.create((RelativeLayout) view);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        User item = getItem(position);

        vh.textViewTen.setText(item.getTen());
        vh.textViewLop.setText(item.getLop());
        vh.textViewMssv.setText(item.getMssv());
        vh.imageView.setImageResource(R.drawable.ic_info_black_24dp);

        return vh.rootView;
    }

    private static class ViewHolder {
        public final RelativeLayout rootView;
        public final ImageView imageView;
        public final TextView textViewTen;
        public final TextView textViewLop;
        public final TextView textViewMssv;

        private ViewHolder(RelativeLayout rootView, ImageView imageView, TextView textViewTen, TextView textViewLop,TextView textViewMssv) {
            this.rootView = rootView;
            this.imageView = imageView;
            this.textViewTen = textViewTen;
            this.textViewLop = textViewLop;
            this.textViewMssv = textViewMssv;
        }

        public static ViewHolder create(RelativeLayout rootView) {
            ImageView imageView = (ImageView) rootView.findViewById(R.id.imageView);
            TextView textViewTen = (TextView) rootView.findViewById(R.id.tvten);
            TextView textViewLop = (TextView) rootView.findViewById(R.id.tvlop);
            TextView textViewMssv = (TextView) rootView.findViewById(R.id.tvmssv);
            return new ViewHolder(rootView, imageView, textViewTen, textViewLop, textViewMssv);
        }
    }
}
