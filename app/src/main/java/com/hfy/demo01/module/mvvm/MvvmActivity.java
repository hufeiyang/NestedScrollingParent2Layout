package com.hfy.demo01.module.mvvm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.hfy.demo01.module.mvvm.bean.Man;

public class MvvmActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        setContentView(R.layout.activity_mvvm);

        //这里使用DataBindingUtil.setContentView()
        //ActivityMvvmBinding是在写好<layout>布局后,make project,自动生成的Binding辅助类
//        ActivityMvvmBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_mvvm);
//        binding.setTime(new Date());
//
        Man man = new Man("段誉","爱情高手");
//        binding.setMan(man);
//
//        //事件处理方法一，binding.
//        binding.btnTest.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //Model实体类发生变化
//                man.setName("段正淳");
//            }
//        });
//
//        //事件处理方法二：
//        binding.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Man man = new Man("段正淳","爱情高高高手");
//                binding.setMan(man);
//            }
//        });
//
//
//        binding.setHome("安徽");
//
//        ArrayList<String> strings = new ArrayList<>();
//        strings.add("一");
//        strings.add("二");
//        binding.setList(strings);


    }



    /**
     * launch MvpActivity
     */
    public static void launch(Activity activity) {
        Intent intent = new Intent(activity, MvvmActivity.class);
        activity.startActivity(intent);
    }
}
