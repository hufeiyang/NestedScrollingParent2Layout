package com.hfy.demo01.module.home.touchevent.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hfy.demo01.R;
import com.hfy.demo01.module.home.touchevent.view.NestedScrollingParent2LayoutImpl3;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class NestedScrollTestFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    Unbinder unbinder;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private NestedScrollingParent2LayoutImpl3 mNestedScrollingParent2Layout;


    private int mFragmentIndex;

    public NestedScrollTestFragment() {
        // Required empty public constructor
    }

    public static NestedScrollTestFragment newInstance() {
        NestedScrollTestFragment fragment = new NestedScrollTestFragment();
        return fragment;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NestedScrollTestFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NestedScrollTestFragment newInstance(String param1, String param2) {
        NestedScrollTestFragment fragment = new NestedScrollTestFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_nested_scroll_test, container, false);
        unbinder = ButterKnife.bind(this, view);

        initView();

        return view;
    }

    private void initView() {

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

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new NestedScrollTestRecyclerViewAdapter(getContext(), dataBeans));

    }

    /**
     * 这个方法仅仅工作在FragmentPagerAdapter的场景中。（普通的activity中的一个fragment 不会调用。）
     *
     * @param visibleToUser
     */
    @Override
    public void setUserVisibleHint(boolean visibleToUser) {
        super.setUserVisibleHint(visibleToUser);

        if (visibleToUser && isCurrentDisplayedFragment()) {
            if (mNestedScrollingParent2Layout != null) {
                mNestedScrollingParent2Layout.setChildRecyclerView(recyclerView);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (isCurrentDisplayedFragment()) {
            if (mNestedScrollingParent2Layout != null) {
                mNestedScrollingParent2Layout.setChildRecyclerView(recyclerView);
            }
        }
    }

    /**
     * 是当前展示的fragment
     * @return
     */
    private boolean isCurrentDisplayedFragment() {
        if (getView() == null || !(getView().getParent() instanceof View)) {
            return false;
        }
        View parent = (View) getView().getParent();
        if (parent instanceof ViewPager) {
            ViewPager viewPager = (ViewPager) parent;
            int currentItem = viewPager.getCurrentItem();
            return currentItem == mFragmentIndex;
        }
        return false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void setNestedParentLayout(NestedScrollingParent2LayoutImpl3 nestedScrollingParent2Layout) {
        mNestedScrollingParent2Layout = nestedScrollingParent2Layout;
    }

    public void setIndex(int i) {
        mFragmentIndex = i;
    }
}
