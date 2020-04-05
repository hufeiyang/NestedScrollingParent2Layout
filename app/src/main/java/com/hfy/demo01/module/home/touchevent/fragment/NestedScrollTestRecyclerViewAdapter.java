package com.hfy.demo01.module.home.touchevent.fragment;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hfy.demo01.R;
import com.hfy.simpleimageloader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class NestedScrollTestRecyclerViewAdapter extends RecyclerView.Adapter<NestedScrollTestRecyclerViewAdapter.ViewHoder> {
    public static final String TAG = "hfy+NestedScrollTest";
    private final Context context;
    private final ArrayList<DataBean> list;

    public NestedScrollTestRecyclerViewAdapter(Context context, ArrayList<DataBean> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHoder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_nested_scroll_test, viewGroup, false);
        Log.i(TAG, "onCreateViewHolder: ");
        return new ViewHoder(itemView);
    }

    @Override
    public void onViewAttachedToWindow(@NonNull ViewHoder holder) {
        super.onViewAttachedToWindow(holder);
        Log.i(TAG, "onViewAttachedToWindow: "+holder.getAdapterPosition());
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHoder viewHoder, int i) {
        DataBean dataBean = list.get(i);
        viewHoder.textView.setText(dataBean.text);
        ImageLoader.with(context).loadBitmapAsync(dataBean.url,viewHoder.imageView);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHoder extends RecyclerView.ViewHolder {

        private final TextView textView;
        private final ImageView imageView;

        public ViewHoder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}
