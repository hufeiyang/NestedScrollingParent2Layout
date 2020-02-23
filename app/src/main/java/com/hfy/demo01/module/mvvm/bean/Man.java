package com.hfy.demo01.module.mvvm.bean;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.BindingAdapter;


/**
 * 侠客 ViewModel
 */
public class Man extends BaseObservable {
    private String name;

    private String level;

    public Man(String name, String level) {
        this.name = name;
        this.level = level;
    }

    /**
     * @return
     * @Bindable BR中生成一个对应的字段，BR编译时生成，类似R文件
     */
    @Bindable
    public String getName() {
        return name;
    }

    /**
     * notifyPropertyChanged(BR.name),通知系统BR.name已发送变化，并更新UI
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
//        notifyPropertyChanged(BR.name);
    }

    @Bindable
    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
//        notifyPropertyChanged(BR.level);
    }

}
