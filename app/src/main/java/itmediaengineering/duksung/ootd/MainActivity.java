package itmediaengineering.duksung.ootd;

import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import itmediaengineering.duksung.ootd.main.adapter.MainPagerAdapter;
import itmediaengineering.duksung.ootd.main.tab.feed.view.FeedFragment;
import itmediaengineering.duksung.ootd.main.tab.home.view.MainFragment;
import itmediaengineering.duksung.ootd.main.tab.mypage.MyPageFragment;
import itmediaengineering.duksung.ootd.main.tab.upload.UploadFragment;

/*
탭을 구현하는 MainActivity
탭과 함께 뷰페이져 구성하여 각 탭들의 상태유지를 생각해야 함
*/

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "TAG MESSAGE";

    @BindView(R.id.viewpager_content)
    ViewPager viewpagerContent;
    @BindView(R.id.tab_content)
    TabLayout tabContent;
    @BindView(R.id.main_fullScreen)
    ConstraintLayout mainFullscreen;
    @BindView(R.id.main_toolbar)
    Toolbar mainToolbar;
    @BindView(R.id.main_toolbar_title)
    ConstraintLayout locationTitle;
    @BindView(R.id.main_activity_location_view)
    TextView locationView;
    @BindView(R.id.main_activity_location_icon)
    ImageView locationChangeBtn;
    @BindView(R.id.main_activity_noti_btn)
    ImageView notiBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setupViewPager(viewpagerContent);
        tabContent.setupWithViewPager(viewpagerContent);
        tabContent.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        locationTitle.setClickable(true);
                        locationView.setText("나의 위치");
                        locationChangeBtn.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        locationTitle.setClickable(false);
                        locationView.setText("카테고리");
                        locationChangeBtn.setVisibility(View.GONE);
                        break;
                    case 2:
                        locationTitle.setClickable(false);
                        locationView.setText("나의 물품 올리기");
                        locationChangeBtn.setVisibility(View.GONE);
                        break;
                    case 3:
                        locationTitle.setClickable(false);
                        locationView.setText("마이 페이지");
                        locationChangeBtn.setVisibility(View.GONE);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) { }

            @Override
            public void onTabReselected(TabLayout.Tab tab) { }
        });

        setupTabIcons();

    }

    private void setupViewPager(ViewPager viewPager) {
        MainPagerAdapter adapter = new MainPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(FeedFragment.newInstance(), "First");
        adapter.addFragment(MainFragment.newInstance(), "Second");
        adapter.addFragment(UploadFragment.newInstance(), "Third");
        adapter.addFragment(MyPageFragment.newInstance(), "Fourth");
        viewPager.setOffscreenPageLimit(4);
        viewPager.setAdapter(adapter);
    }

    private void setupTabIcons() {
        View viewFirst = getLayoutInflater().inflate(R.layout.custom_tab, null);
        View viewSecond = getLayoutInflater().inflate(R.layout.custom_tab, null);
        View viewThird = getLayoutInflater().inflate(R.layout.custom_tab, null);
        View viewFourth = getLayoutInflater().inflate(R.layout.custom_tab, null);

        ImageView imgFirst = viewFirst.findViewById(R.id.img_tab);
        ImageView imgSecond = viewSecond.findViewById(R.id.img_tab);
        ImageView imgThird = viewThird.findViewById(R.id.img_tab);
        ImageView imgFourth = viewFourth.findViewById(R.id.img_tab);

        TextView txtFirst = viewFirst.findViewById(R.id.txt_tab);
        TextView txtSecond = viewSecond.findViewById(R.id.txt_tab);
        TextView txtThird = viewThird.findViewById(R.id.txt_tab);
        TextView txtFourth = viewFourth.findViewById(R.id.txt_tab);

        imgFirst.setImageResource(R.drawable.icn_home_inactive);
        imgSecond.setImageResource(R.drawable.icn_menu_outlined);
        imgThird.setImageResource(R.drawable.icn_add_outlined);
        imgFourth.setImageResource(R.drawable.icn_profile_inactive);

        txtFirst.setText("Feed");
        txtSecond.setText("Category");
        txtThird.setText("Upload");
        txtFourth.setText("My Page");

        tabContent.getTabAt(0).setCustomView(viewFirst);
        tabContent.getTabAt(1).setCustomView(viewSecond);
        tabContent.getTabAt(2).setCustomView(viewThird);
        tabContent.getTabAt(3).setCustomView(viewFourth);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public interface onKeyBackPressedListener{
        void onBack();
    }

    private onKeyBackPressedListener onKeyBackPressedListener;
    public void setOnBackPressedListener(onKeyBackPressedListener listener){
        onKeyBackPressedListener = listener;
    }
}