package com.hfy.demo01.module.home.fragment;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.PictureDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.StreamEncoder;
import com.google.gson.JsonObject;
import com.hfy.demo01.R;
import com.hfy.demo01.common.customview.MyToast;
import com.hfy.demo01.module.home.designsupportlibrarytest.NotificationActivity;
import com.hfy.demo01.module.home.designsupportlibrarytest.ViewEventTestActivity;
import com.hfy.demo01.module.home.designsupportlibrarytest.MaterialDesignWidgetActivity;
import com.hfy.demo01.module.home.view.CommonTitleTestActivityActivity;
import com.hfy.demo01.module.mvp.view.MvpActivity;
import com.pixplicity.sharp.Sharp;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class FirstFragment extends Fragment {

    private static final int PERMISSIOINS_REQUEST_CODE_CALL = 1000;
    private static final String TAG = "FirstFragment";
    @BindView(R.id.button)
    Button mButton;

    @BindView(R.id.btn_go_to_notification_activity)
    Button mBtnGoToNotificationPage;

    @BindView(R.id.iv_card_view)
    ImageView mImageView;

//    @BindView(R.id.flash_sale_progress_bar)
//    ProgressBar progressBar;
//
//    @BindView(R.id.autoverticalscrolltextview)
//    AutoVerticalScrollTextView autoVerticalScrollTextView;

    @BindView(R.id.autoSwitchTextView)
    AutoSwitchTextView autoSwitchTextView;

    private Unbinder mUnbind;
    private int tempProgress = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_first, null);

        mUnbind = ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        //Sharp 加载SVG文件
        Sharp.loadResource(getResources(), R.raw.text).into(mImageView);

        //glide-svg 库里面的方法， 清晰
//        RequestBuilder<PictureDrawable> requestBuilder = GlideApp.with(this)
//                .as(PictureDrawable.class)
//                .transition(withCrossFade())
//                .listener(new SvgSoftwareLayerSetter());
//        requestBuilder.load("http://www.webhek.com/wordpress/wp-content/uploads/2014/05/kiwi.svg").into(viewImage1);
//        requestBuilder.load(R.raw.text).into(mImageView);


        //可以加载svg图片?，不过显示没有上面那种方式清晰(** 实测不行啊啊啊啊啊 )
//        Glide.with(this).load("http://www.webhek.com/wordpress/wp-content/uploads/2014/05/kiwi.svg").into(mImageView);
//        Glide.with(this).load(R.raw.text).into(mImageView);

        //这种方法？？？
//        GenericRequestBuilder<Uri, InputStream, SVG, PictureDrawable> requestBuilder = Glide.with(this)
//                .using(Glide.buildStreamModelLoader(Uri.class, this), InputStream.class)
//                .from(Uri.class)
//                .as(SVG.class)
//                .transcode(new SvgDrawableTranscoder(), PictureDrawable.class)
//                .sourceEncoder(new StreamEncoder())
//                .cacheDecoder(new FileToStreamDecoder<>(new SVGDecoder()))
//                .decoder(new SVGDecoder())
//                .listener(new SvgSoftwareLayerSetter<Uri>());
//
//        requestBuilder
//                .diskCacheStrategy(DiskCacheStrategy.NONE)
//                .load(Uri.parse("http://www.webhek.com/wordpress/wp-content/uploads/2014/05/kiwi.svg"))
//                .into(cardHolder.iv_card);
    }

    @OnClick({R.id.button,
            R.id.btn_go_to_notification_activity,
            R.id.btn_go_to_call_activity,
            R.id.btn_go_to_material_design_activity,
            R.id.btn_go_to_view_test_activity,
            R.id.btn_go_to_common_title_test_activity
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button:
                MvpActivity.launch(getActivity());
                break;
            case R.id.btn_go_to_notification_activity:
                NotificationActivity.launch(getActivity());
                break;
            case R.id.btn_go_to_call_activity:
                call();
                break;
            case R.id.btn_go_to_material_design_activity:
                MaterialDesignWidgetActivity.launch(getActivity());
                break;
            case R.id.btn_go_to_view_test_activity:
                ViewEventTestActivity.launch(getActivity());
//                MyToast.showMsg(getActivity(), "hhhh");
//                MyToast.showMsg(getActivity(), "hhhfffyyy");

                break;

            case R.id.btn_go_to_common_title_test_activity:
//                CommonTitleTestActivityActivity.launch(getActivity());

                ArrayList<String> scrollMessageList = new ArrayList<>();
                scrollMessageList.add("hehe撒打发斯蒂芬");
                scrollMessageList.add("aaa沙发上发生的发放");
                scrollMessageList.add("hjjk阿斯蒂芬");

//                autoVerticalScrollTextView.setData(scrollMessageList);
//                autoVerticalScrollTextView.start();

                autoSwitchTextView.setData(scrollMessageList);

//                int progress = 80;

//                //1000ms走完100，10ms走1， 所以10*progress ms 走完。
//                long period = 10;
//                Timer timer = new Timer();
//                timer.schedule(new TimerTask() {
//                    @Override
//                    public void run() {
//                        if (tempProgress == progress){
//                            cancel();
//                        }
//                        progressBar.setProgress(tempProgress);
//                        tempProgress = tempProgress + 1 ;
//                    }
//                }, 0, period);
                break;
            default:
                break;
        }
    }

    private void call() {

        //检查权限
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            //没有就去申请
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CALL_PHONE)) {
                // 用户拒绝过这个权限了，应该提示用户，为什么需要这个权限。
                AlertDialog dialog = new AlertDialog.Builder(getContext())
                        .setMessage("解释一下，因为你要打电话，所以需要打电话权限。")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                //继续请求权限
                                requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, PERMISSIOINS_REQUEST_CODE_CALL);
                            }
                        }).create();
                dialog.show();
            } else {
                //请求权限，fragment中运行时权限的特殊处理、在Fragment中申请权限、不要使用ActivityCompat.requestPermissions、直接使用Fragment的requestPermissions方法、否则会回调到Activity的 onRequestPermissionsResult
                requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, PERMISSIOINS_REQUEST_CODE_CALL);
            }
        } else {
            callPhone();
        }

    }

    /**
     * 申请权限的回调
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSIOINS_REQUEST_CODE_CALL) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callPhone();
            }
            //拒绝后，再次弹框提示申请权限 ，若勾选“不再询问”后点击拒绝，会走此逻辑（此时shouldShowRequestPermissionRationale返回false）。
            //因为已经拒绝且不在提示，需要告诉用户如何打开权限。
            else if (!ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CALL_PHONE)) {
                AlertDialog dialog = new AlertDialog.Builder(getContext())
                        .setMessage("该功能需要访问电话的权限，当前未打开权限！ 打开权限的方法：设置-应用—权限。")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create();
                dialog.show();
            } else {
                Toast.makeText(getContext(), "被拒绝了", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 打电话.
     * <p>
     * 注意：
     * android.intent.action.CALL需要在配置文件中添加拨号权限 且 点击后直接拨号
     * android.intent.action.DIAL只是调用拨号键盘，不用在文件中添加权限
     */
    private void callPhone() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + "10086"));
        startActivity(intent);
    }
}
