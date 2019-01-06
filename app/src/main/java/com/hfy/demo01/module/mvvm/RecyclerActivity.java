package com.hfy.demo01.module.mvvm;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.hfy.demo01.R;
import com.hfy.demo01.databinding.ActivityRecyclerBinding;
import com.hfy.demo01.databinding.ItemLayoutRecyclerBinding;
import com.hfy.demo01.module.mvvm.bean.Man;

import java.util.ArrayList;
import java.util.List;

public class RecyclerActivity extends AppCompatActivity {

    private ActivityRecyclerBinding binding;

    /**
     * launch MvpActivity
     */
    public static void launch(Activity activity) {
        Intent intent = new Intent(activity, RecyclerActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        setContentView(R.layout.activity_recycler);

        // 1、使用 DataBindingUtil.setContentView() 代替  setContentView(R.layout.activity_recycler);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_recycler);

        //直接通过binding找到了recyclerView
        LinearLayoutManager layout = new LinearLayoutManager(this);
        layout.setOrientation(LinearLayoutManager.VERTICAL);
        binding.recycler.setLayoutManager(layout);
        binding.recycler.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        ManAdapter manAdapter = new ManAdapter(getList());
        binding.recycler.setAdapter(manAdapter);
    }

    private List<Man> getList() {
        List<Man> list = new ArrayList<>();
        list.add(new Man("乔峰", "帮主"));
        list.add(new Man("虚竹", "小和尚"));
        return list;
    }

    private class ManAdapter extends RecyclerView.Adapter<ManViewHolder> {

        private final List<Man> mList;

        public ManAdapter(List<Man> list) {
            mList = list;
        }

        @NonNull
        @Override
        public ManViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

            //2、这里是把item_layout对应的Binding 注入iewHolder（以前是View）
            ItemLayoutRecyclerBinding manViewBinding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()), R.layout.item_layout_recycler, viewGroup, false);
            return new ManViewHolder(manViewBinding);
        }

        @Override
        public void onBindViewHolder(@NonNull ManViewHolder manViewHolder, int i) {

            //3、绑定只用使用Binding，很简洁。
            manViewHolder.manViewBinding.setMan(mList.get(i));
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }
    }

    private class ManViewHolder extends RecyclerView.ViewHolder {

        private final ItemLayoutRecyclerBinding manViewBinding;

        //4、这里没有findViewById()去找各个view。就holder了Binding。
        public ManViewHolder(ItemLayoutRecyclerBinding manViewBinding) {
            super(manViewBinding.getRoot());
            this.manViewBinding = manViewBinding;
        }
    }
}
