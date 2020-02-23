package com.hfy.demo01.module.home.designsupportlibrarytest;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.FragmentActivity;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.hfy.demo01.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 材料设计的控件 的使用
 *
 * @author hufy
 */
public class MaterialDesignWidgetActivity extends AppCompatActivity {

    @BindView(R.id.btn_snack_bar)
    public Button mBtnShowSnackBar;

    public static void launch(FragmentActivity activity) {
        Intent intent = new Intent(activity, MaterialDesignWidgetActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material_design_widget);

        ButterKnife.bind(this);

        initView();
    }

    private void initView() {

    }

    @OnClick({R.id.btn_snack_bar,
            R.id.btn_test_behavior})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_snack_bar:
                showSnackBar();
                break;
            case R.id.btn_test_behavior:
                BehaviorTestActivity.launch(this);
                break;
            default:
                break;
        }
    }

    private void showSnackBar() {
        //第一个参数是为了找到Snackbar的父控件，给个view就行
        Snackbar.make(mBtnShowSnackBar, "Snackbar标题", Snackbar.LENGTH_LONG)
                .setAction("点击事件", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(MaterialDesignWidgetActivity.this, "点击了snackbar", Toast.LENGTH_SHORT).show();
                    }
                })
                .show();
    }

}
