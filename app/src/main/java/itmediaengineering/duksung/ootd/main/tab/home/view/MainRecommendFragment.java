package itmediaengineering.duksung.ootd.main.tab.home.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import itmediaengineering.duksung.ootd.R;
import itmediaengineering.duksung.ootd.main.tab.home.adapter.CardPagerAdapter;
import itmediaengineering.duksung.ootd.main.tab.home.adapter.CardPagerViewHolder;

public class MainRecommendFragment extends Fragment {
    //bundle = getArguments();
    //movieDataArrayList = bundle.getParcelableArrayList("movie_list_simple_data");

    @BindView(R.id.pager)
    ViewPager viewPager;

    //private static List<MovieInfo> movieDataArrayList = new ArrayList<>();
    //private Bundle bundle;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    //생성자 대신 정적 팩토리 메소드를 사용
    //getlnstance： 인자에 기술된 객체를 반환하지만，인자와 같은 값을 갖지 않을
    //수도 있다. 싱글턴(singleton) 패턴을 따를 경우，이 메서드는 인자 없이 항상
    //같은 객체를 반환한다.
    //newlnstance： getlnstance와 같지만 호출할 때마다 다른 객체를 반환한다.
    public static MainRecommendFragment newInstance(){

        MainRecommendFragment fragment = new MainRecommendFragment();
        //Bundle args = new Bundle();
        //args.putParcelableArrayList("movie_list_simple_data", data);
        //fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.recommned_list, container, false);
        ButterKnife.bind(this, rootView);

        /*CardPagerAdapter pagerAdapter = new CardPagerAdapter(getActivity().getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(5);

        for (int i = 0; i < 5; i++) {
            pagerAdapter.addItem(CardPagerViewHolder.newInstance());
        }

        viewPager.setPadding(100, 0, 100, 0);
        viewPager.setPageMargin(getResources().getDisplayMetrics().widthPixels / 9);
*/
        return rootView;
    }
}
