package itmediaengineering.duksung.ootd.main.tab.mypage.view;

import android.content.SharedPreferences;
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

/*
MyPageFragment에서는 사용자 정보 페이지(myPage) 탭을 보여줌
서버에서 받아온 사용자 업로드 이미지를 갤러리 형식으로 보여주어야 하며
로그아웃 및 사용자 자기소개를 수정할 수 있도록 구성하여야 함
--------------------------------------------------------------------
현재 플리커에서 받아오는 이미지를 recyclerView와 GridView를 통해서 보여주고 있음
Recycler 뷰에 헤더를 달아서 사용자 정보 부분과 갤러리 부분을 통합하였음
*/

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

    private final int TYPE_HEADER = 0;
    private final int TYPE_ITEM = 1;

    public static MyPageFragment newInstance() {
        return new MyPageFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_my_page, container, false);
        ButterKnife.bind(this, rootView);

        userNickName.setText(PreferenceUtils.getNickname());

        tabHost.setup(getContext(), getChildFragmentManager(), android.R.id.tabcontent);
        //tabHost.getTabWidget().getChildAt(0).setBackgroundColor(Color.parseColor("#ce6776"));
        //tabHost.addTab(tabHost.newTabSpec("tab1").setIndicator(getTabIndicator(tabHost.getContext(), R.string.app_name, android.R.drawable.star_on)).,
        //        MyPagePagerFragment.class, null);
        tabHost.addTab(tabHost.newTabSpec("tab1").setIndicator("판매중"),
                MyPagePagerFragment.class, makeBundle(SaleType.onSale));
        tabHost.addTab(tabHost.newTabSpec("tab2").setIndicator("판매완료"),
                MyPagePagerFragment.class, makeBundle(SaleType.soldOut));
        tabHost.addTab(tabHost.newTabSpec("tab3").setIndicator("찜"),
                MyPagePagerFragment.class, makeBundle(SaleType.pick));

        //tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab()).setBackgroundResource(R.drawable.tab_selected);

        /*setupViewPager(myPageViewPager);
        tabWidget.(myPageViewPager);
        tabWidget.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });*/

        /*GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 3);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int i) {
                switch(adapter.getItemViewType(i)){
                    case TYPE_HEADER:
                        return 3;       // 3개로 나눈 영역 중에 3개를 합쳐 사용하겠다
                    case TYPE_ITEM:
                        return 1;       // 3개로 나눈 영역 중에 1개를 사용하겠다
                    default:
                        return 1;
                }
            }
        });

        myPageRecyclerView.setLayoutManager(layoutManager);
        //myPageRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        myPageRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.left = 3;
                outRect.right = 3;
                outRect.bottom = 3;

                // Add top margin only for the first item to avoid double space between items
                if (parent.getChildLayoutPosition(view) != 0) {
                    outRect.top = 3;
                } else {
                    outRect.top = 0;
                }
            }
        });*/

        return rootView;
    }

    private Bundle makeBundle(SaleType saleType){
        Bundle bundle = new Bundle();
        bundle.putSerializable(BundleKey.SALE_TYPE, saleType);
        return bundle;
    }

    /*private View getTabIndicator(Context context, int title, int icon) {
        View view = LayoutInflater.from(context).inflate(R.layout.tab_layout, null);
        ImageView iv = view.findViewById(R.id.imageView);
        iv.setImageResource(icon);
        TextView tv = view.findViewById(R.id.textView);
        tv.setText(title);
        return view;
    }*/

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    private void setupViewPager(ViewPager viewPager) {
       /* adapter = new MyPagePagerAdapter(getFragmentManager());
        adapter.addFragment(FeedFragment.newInstance(), "First");
        adapter.addFragment(CategoryFragment.newInstance(), "Second");
        adapter.addFragment(UploadFragment.newInstance(), "Third");
        adapter.addFragment(MyPageFragment.newInstance(), "Fourth");
        viewPager.setOffscreenPageLimit(4);
        viewPager.setAdapter(adapter);*/
    }
}

