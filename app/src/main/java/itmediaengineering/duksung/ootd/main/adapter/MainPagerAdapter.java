package itmediaengineering.duksung.ootd.main.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

public class MainPagerAdapter extends FragmentStatePagerAdapter {
    ArrayList<Fragment> items = new ArrayList<>();

    //프래그먼트매니저 객체를 받아서 초기화하는 생성자 코드
    public MainPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void addItem(Fragment item) {
        items.add(item);
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        return items.get(position);
    }

    @Override
    public int getCount() {
        return items.size();
}
}