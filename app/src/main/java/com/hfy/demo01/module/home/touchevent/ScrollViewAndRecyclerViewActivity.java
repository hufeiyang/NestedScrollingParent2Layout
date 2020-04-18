package com.hfy.demo01.module.home.touchevent;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hfy.demo01.R;
import com.hfy.demo01.module.home.touchevent.fragment.DataBean;
import com.hfy.demo01.module.home.touchevent.fragment.NestedScrollTestRecyclerViewAdapter;
import com.hfy.demo01.module.home.touchevent.view.NestedScrollingParent2LayoutImpl2;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * ScrollView+RecyclerView,滑动冲突解决方案对比
 * @author hufeiyang
 */
public class ScrollViewAndRecyclerViewActivity extends AppCompatActivity {


    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private int mType;

    public static void launch(FragmentActivity activity, int type) {
        Intent intent = new Intent(activity, ScrollViewAndRecyclerViewActivity.class);
        intent.putExtra("type", type);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();


        mType = intent.getIntExtra("type", 0);
        if (mType ==1) {
            setContentView(R.layout.activity_scroll_view_and_recycler_view);
        } else if (mType ==2){
            setContentView(R.layout.activity_nested_scroll_view_and_recycler_view);
        }else if (mType ==3){
            setContentView(R.layout.activity_scroll_view_parent2_view_and_recycler_view);
        }

        ButterKnife.bind(this);

        if (mType ==3){
            NestedScrollingParent2LayoutImpl2 nestedScrollingParent2 = findViewById(R.id.nested_layout);
            View header = findViewById(R.id.tv_head);
            nestedScrollingParent2.setHeader(header);
        }

        initRecyclerView();
    }

    private void initRecyclerView() {
        ArrayList<DataBean> dataBeans = new ArrayList<>();
        dataBeans.add(new DataBean(1 + "TextView", "https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=2650138538,1827686917&fm=15&gp=0.jpg"));
        dataBeans.add(new DataBean(2 + "TextView", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1582440395993&di=16f2a6878f4b5d76e2122b008b80da0e&imgtype=0&src=http%3A%2F%2Fn.sinaimg.cn%2Fsinacn11%2F266%2Fw1600h1066%2F20180318%2F8390-fyshfur1533535.jpg"));
        dataBeans.add(new DataBean(3 + "TextView", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1582440395993&di=80f6dd0dbd89d64a282b7e779d188177&imgtype=0&src=http%3A%2F%2Fhimg2.huanqiu.com%2Fattachment2010%2F2013%2F0711%2F20130711013802170.jpg"));
        dataBeans.add(new DataBean(4 + "TextView", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1582440395992&di=eeee6904d7a12ea9b0a9a7bd004ef5d7&imgtype=0&src=http%3A%2F%2Fimg.pconline.com.cn%2Fimages%2Fupload%2Fupc%2Ftx%2Fitbbs%2F1510%2F28%2Fc15%2F14555696_1446001070504_mthumb.jpg"));
        dataBeans.add(new DataBean(5 + "TextView", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1582440395992&di=5043a645040c6b3d3cdc9116e50ef5ab&imgtype=0&src=http%3A%2F%2Fi0.sinaimg.cn%2Fty%2F2014%2F1204%2FU11648P6DT20141204190014.jpg"));

        dataBeans.add(new DataBean(6 + "TextView", "https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=2650138538,1827686917&fm=15&gp=0.jpg"));
        dataBeans.add(new DataBean(7 + "TextView", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1582440395993&di=16f2a6878f4b5d76e2122b008b80da0e&imgtype=0&src=http%3A%2F%2Fn.sinaimg.cn%2Fsinacn11%2F266%2Fw1600h1066%2F20180318%2F8390-fyshfur1533535.jpg"));
        dataBeans.add(new DataBean(8 + "TextView", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1582440395993&di=80f6dd0dbd89d64a282b7e779d188177&imgtype=0&src=http%3A%2F%2Fhimg2.huanqiu.com%2Fattachment2010%2F2013%2F0711%2F20130711013802170.jpg"));
        dataBeans.add(new DataBean(9 + "TextView", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1582440395992&di=eeee6904d7a12ea9b0a9a7bd004ef5d7&imgtype=0&src=http%3A%2F%2Fimg.pconline.com.cn%2Fimages%2Fupload%2Fupc%2Ftx%2Fitbbs%2F1510%2F28%2Fc15%2F14555696_1446001070504_mthumb.jpg"));
        dataBeans.add(new DataBean(10 + "TextView", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1582440395992&di=5043a645040c6b3d3cdc9116e50ef5ab&imgtype=0&src=http%3A%2F%2Fi0.sinaimg.cn%2Fty%2F2014%2F1204%2FU11648P6DT20141204190014.jpg"));

        dataBeans.add(new DataBean(11 + "TextView", "https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=2650138538,1827686917&fm=15&gp=0.jpg"));
        dataBeans.add(new DataBean(12 + "TextView", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1582440395993&di=16f2a6878f4b5d76e2122b008b80da0e&imgtype=0&src=http%3A%2F%2Fn.sinaimg.cn%2Fsinacn11%2F266%2Fw1600h1066%2F20180318%2F8390-fyshfur1533535.jpg"));
        dataBeans.add(new DataBean(13 + "TextView", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1582440395993&di=80f6dd0dbd89d64a282b7e779d188177&imgtype=0&src=http%3A%2F%2Fhimg2.huanqiu.com%2Fattachment2010%2F2013%2F0711%2F20130711013802170.jpg"));
        dataBeans.add(new DataBean(14 + "TextView", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1582440395992&di=eeee6904d7a12ea9b0a9a7bd004ef5d7&imgtype=0&src=http%3A%2F%2Fimg.pconline.com.cn%2Fimages%2Fupload%2Fupc%2Ftx%2Fitbbs%2F1510%2F28%2Fc15%2F14555696_1446001070504_mthumb.jpg"));
        dataBeans.add(new DataBean(15 + "TextView", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1582440395992&di=5043a645040c6b3d3cdc9116e50ef5ab&imgtype=0&src=http%3A%2F%2Fi0.sinaimg.cn%2Fty%2F2014%2F1204%2FU11648P6DT20141204190014.jpg"));


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new NestedScrollTestRecyclerViewAdapter(this, dataBeans));
    }
}
