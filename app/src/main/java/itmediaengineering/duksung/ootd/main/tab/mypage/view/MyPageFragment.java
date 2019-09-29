package itmediaengineering.duksung.ootd.main.tab.mypage.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TabWidget;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import itmediaengineering.duksung.ootd.R;
import itmediaengineering.duksung.ootd.main.tab.mypage.presenter.SaleType;
import itmediaengineering.duksung.ootd.utils.BundleKey;
import itmediaengineering.duksung.ootd.utils.PreferenceUtils;

public class MyPageFragment extends Fragment{
    private static final String TAG = MyPageFragment.class.getSimpleName();

    @BindView(R.id.my_page_user_image)
    ImageView userImgView;
    @BindView(R.id.my_page_user_nickname)
    TextView userNickName;
    @BindView(R.id.my_page_user_description)
    TextView userDesc;
    @BindView(R.id.tab_host)
    FragmentTabHost tabHost;
    @BindView(android.R.id.tabs)
    TabWidget tabWidget;
    @BindView(android.R.id.tabcontent)
    FrameLayout tabFrame;

    public static MyPageFragment newInstance() {
        return new MyPageFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_my_page, container, false);
        ButterKnife.bind(this, rootView);

        userNickName.setText(PreferenceUtils.getNickname());

        tabHost.setup(getContext(), getChildFragmentManager(), android.R.id.tabcontent);
        tabHost.addTab(tabHost.newTabSpec("tab1").setIndicator("판매중"),
                MyPageTabFragment.class, makeBundle(SaleType.onSale));
        tabHost.addTab(tabHost.newTabSpec("tab2").setIndicator("판매완료"),
                MyPageTabFragment.class, makeBundle(SaleType.soldOut));
        tabHost.addTab(tabHost.newTabSpec("tab3").setIndicator("찜"),
                MyPagePickFragment.class, makeBundle(SaleType.pick));

        return rootView;
    }

    private Bundle makeBundle(SaleType saleType){
        Bundle bundle = new Bundle();
        bundle.putSerializable(BundleKey.SALE_TYPE, saleType);
        return bundle;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }
}

