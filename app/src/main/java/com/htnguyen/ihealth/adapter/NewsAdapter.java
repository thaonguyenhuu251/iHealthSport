package com.htnguyen.ihealth.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.htnguyen.ihealth.R;
import com.htnguyen.ihealth.model.ModelClass;

import java.util.ArrayList;

public class NewsAdapter extends PagerAdapter {

    Context context;
    ArrayList<ModelClass> modelClassArrayList;

    public NewsAdapter(Context context, ArrayList<ModelClass> modelClassArrayList) {
        this.context = context;
        this.modelClassArrayList = modelClassArrayList;
    }

    @NonNull
    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        LayoutInflater inflater = ((FragmentActivity) context).getLayoutInflater();
        View itemView = inflater.inflate(R.layout.layout_item_news, container, false);
        ImageView imageView = itemView.findViewById(R.id.imageview);
        Glide.with(context).load(modelClassArrayList.get(position).getUrlToImage()).into(imageView);
        ((ViewPager) container).addView(itemView);

        itemView.setOnClickListener(view -> {

        });

        return itemView;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return modelClassArrayList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        // TODO Auto-generated method stub
        return view == ((View) object);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // TODO Auto-generated method stub
        ((ViewPager) container).removeView((View) object);
    }
}
