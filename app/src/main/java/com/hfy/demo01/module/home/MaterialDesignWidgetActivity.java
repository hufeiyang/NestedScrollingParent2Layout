package com.hfy.demo01.module.home;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
    Button mBtnShowSnackBar;

//    @BindView(R.id.ll_container)
//    LinearLayout mLlContainer;

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

    @OnClick({R.id.btn_snack_bar})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_snack_bar:
                showSnackBar();
                break;

            default:
                break;
        }
    }

    private void showSnackBar() {
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
