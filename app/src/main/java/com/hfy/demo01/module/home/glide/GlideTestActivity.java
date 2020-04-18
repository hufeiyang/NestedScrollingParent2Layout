package com.hfy.demo01.module.home.glide;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.target.ViewTarget;
import com.hfy.demo01.R;

import java.io.File;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Glide test
 *
 * @author hufeiyang
 */
public class GlideTestActivity extends AppCompatActivity {

    @BindView(R.id.iv_glide_test)
    ImageView ivGlideTest;

    @BindView(R.id.iv_glide_test_gif)
    ImageView ivGlideTestGif;

    @BindView(R.id.tv_glide_test)
    TextView tvGlideTest;

    public static void launch(FragmentActivity activity) {
        Intent intent = new Intent(activity, GlideTestActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glide_test);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {

        //使用Glide，我们就完全不用担心图片内存浪费，甚至是内存溢出的问题。
        // 因为Glide从来都不会直接将图片的完整尺寸全部加载到内存中，而是用多少加载多少。
        // Glide会自动判断ImageView的大小，然后只将这么大的图片像素加载到内存当中，帮助我们节省内存开支。

        Glide.with(this)
                .load("http://a1.att.hudong.com/05/00/01300000194285122188000535877.jpg")
//                .asGif()//只允许加载静态图片,若原本url就是静态图，则会加载失败
                .placeholder(R.drawable.brvah_sample_footer_loading_progress)
                .error(R.drawable.common_google_signin_btn_icon_disabled)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
//                .override(200, 200)//指定要加载到控件的图片大小（由下载的原图压缩所得）
                .into(ivGlideTest);

//        Glide.with(this)
//                .load("https://n.sinaimg.cn/tech/transform/550/w330h220/20200103/0f09-imrkkfx2327344.gif")
////                .asBitmap()//只允许加载静态图片，gif就展示第一帧
//                .placeholder(R.drawable.brvah_sample_footer_loading_progress)
//                .error(R.drawable.common_google_signin_btn_icon_disabled)
//                .diskCacheStrategy(DiskCacheStrategy.NONE)
//                .skipMemoryCache(true)
//                .into(ivGlideTestGif);

        //自定义GlideUrl，可用于修改cache key
        Glide.with(this)
                .load(new GlideUrl("https://n.sinaimg.cn/tech/transform/550/w330h220/20200103/0f09-imrkkfx2327344.gif"))
                .into(ivGlideTestGif);


        //会阻塞，直到获取到结果或超时，用于子线程同步请求。with的context也不能传activity了
        new Thread(new Runnable() {
            @Override
            public void run() {
                FutureTarget<GlideDrawable> futureTarget = Glide.with(getApplicationContext())
                        .load("http://a1.att.hudong.com/05/00/01300000194285122188000535877.jpg")
                        .into(100, 100);
                try {
                    futureTarget.get(2000, TimeUnit.MILLISECONDS);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                }
            }
        }).start();



        //自定义Target
        Glide.with(this)
                .load("http://a1.att.hudong.com/05/00/01300000194285122188000535877.jpg")
                .error(R.drawable.common_google_signin_btn_icon_disabled)
                .into(new SimpleTarget<GlideDrawable>() {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                        ivGlideTest.setImageDrawable(resource);
                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        super.onLoadFailed(e, errorDrawable);
                        ivGlideTest.setImageDrawable(errorDrawable);
                    }
                });

        Glide.with(this)
                .load("http://a1.att.hudong.com/05/00/01300000194285122188000535877.jpg")
                .error(R.drawable.common_google_signin_btn_icon_disabled)
                .into(new ViewTarget<TextView, GlideDrawable>(tvGlideTest) {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {

                    }
                });

        //预加载，先缓存下来，后面再用来展示就很快
        Glide.with(this)
                .load("http://a1.att.hudong.com/05/00/01300000194285122188000535877.jpg")
                .preload();

        //downloadOnly
        Glide.with(this)
                .load("http://a1.att.hudong.com/05/00/01300000194285122188000535877.jpg")
                .downloadOnly(new SimpleTarget<File>() {
                    @Override
                    public void onResourceReady(File resource, GlideAnimation<? super File> glideAnimation) {
                        //可以对 File处理

                    }


                });

        //和into(100, 100)类似，会阻塞，用于子线程，with的context也不能传activity了。
        FutureTarget<File> fileFutureTarget = Glide.with(getApplicationContext())
                .load("http://a1.att.hudong.com/05/00/01300000194285122188000535877.jpg")
                .downloadOnly(100, 100);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    File file = fileFutureTarget.get();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();


        //listener
        Glide.with(this)
                .load("http://a1.att.hudong.com/05/00/01300000194285122188000535877.jpg")
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        Toast.makeText(GlideTestActivity.this, "下载成功！", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                })
                .preload();

        //图片转换
        Glide.with(this)
                .load("http://a1.att.hudong.com/05/00/01300000194285122188000535877.jpg")
                .dontTransform()
//                .transform(...)
                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
//                .fitCenter()
                .centerCrop()
                .into(ivGlideTest);
    }

}
