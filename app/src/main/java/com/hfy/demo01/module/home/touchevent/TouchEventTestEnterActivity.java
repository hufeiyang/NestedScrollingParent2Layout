package com.hfy.demo01.module.home.touchevent;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import com.hfy.demo01.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 事件分发、滑动冲突入口页面
 *
 * @author hufeiyang
 */
public class TouchEventTestEnterActivity extends AppCompatActivity {

    @BindView(R.id.btn_test_nested_scrolling)
    Button btnGoToViewTestActivity;
    @BindView(R.id.btn_test_traditional_nested_scroll)
    Button btnTestTraditionalNestedScroll;
    @BindView(R.id.btn_test_traditional_scroll_view_recycler_view)
    Button btnTestTraditionalScrollViewRecyclerView;
    @BindView(R.id.btn_test_traditional_nested_scroll_view_recycler_view)
    Button btnTestTraditionalNestedScrollViewRecyclerView;

    public static void launch(FragmentActivity activity) {
        Intent intent = new Intent(activity, TouchEventTestEnterActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_touch_event_test_enter);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_test_nested_scrolling,
            R.id.btn_test_traditional_nested_scroll,
            R.id.btn_test_traditional_scroll_view_recycler_view,
            R.id.btn_test_traditional_nested_scroll_view_recycler_view,
            R.id.btn_test_nested_scroll_parent2_view_recycler_view,
            R.id.btn_test_nested_scroll_recycler_view_and_recycler_view,
            R.id.btn_test_recycler_view_and_recycler_view
    })
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_test_traditional_nested_scroll:
                NestedScrollTestActivity.launch(this, false);
                break;
            case R.id.btn_test_nested_scrolling:
                NestedScrollTestActivity.launch(this, true);
                break;
            case R.id.btn_test_traditional_scroll_view_recycler_view:
                ScrollViewAndRecyclerViewActivity.launch(this, 1);
                break;
            case R.id.btn_test_traditional_nested_scroll_view_recycler_view:
                ScrollViewAndRecyclerViewActivity.launch(this, 2);
                break;
            case R.id.btn_test_nested_scroll_parent2_view_recycler_view:
                ScrollViewAndRecyclerViewActivity.launch(this, 3);
                break;

            case R.id.btn_test_recycler_view_and_recycler_view:
                RecyclerViewAndRecyclerViewActivity.launch(this, false);
                break;
            case R.id.btn_test_nested_scroll_recycler_view_and_recycler_view:
                RecyclerViewAndRecyclerViewActivity.launch(this, true);
                break;
            default:
                break;
        }
    }

}
