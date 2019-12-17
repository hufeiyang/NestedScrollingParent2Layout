package com.hfy.demo01.module.home.leaktest;

import android.app.Activity;
import android.content.Context;

public class FYManager
{
    //volatile：解决双重check null带来的问题：
    //第一个check不为null，但其实并未初始化。volatile保证第一个check不为null时一定是初始化的（保证可见性、有序性）。
    private static volatile FYManager instance;

    //上面的static HFYManager，持有Context引用，若是activity，会引起内存泄漏（ApplicationContext不会泄漏）
    private Context mContext;

    //单例锁
    private static final byte[] SYNC_LOCK = new byte[0];

    private FYManager(Context context) {
        //解决 可能内存泄漏的问题
        if (context instanceof Activity) {
            this.mContext = context.getApplicationContext();
        }
        else {
            this.mContext = context;
        }

    }

    /**
     * 获取单例，懒加载
     * @param context 上下文
     * @return WalletTaManager
     *
     * 不在方法上加synchronized，双重check null，在初始化前加锁。（减少性能开销）
     */
    public static FYManager getInstance(Context context) {
        if (null == instance) {
            synchronized (SYNC_LOCK) {
                if (null == instance) {
                    instance = new FYManager(context);
                }
            }
        }

        return instance;
    }

    public String getName(){
        return "HFY";
    }
}
