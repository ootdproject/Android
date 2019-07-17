package itmediaengineering.duksung.ootd;

import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setupViewPager(viewpagerContent);
        tabContent.setupWithViewPager(viewpagerContent);
        setupTabIcons();
    }

    private void setupViewPager(ViewPager viewPager) {
        MainPagerAdapter adapter = new MainPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(MainFragment.newInstance(), "First");
        adapter.addFragment(FeedFragment.newInstance(), "Second");
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

        txtFirst.setText("Home");
        txtSecond.setText("Feed");
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