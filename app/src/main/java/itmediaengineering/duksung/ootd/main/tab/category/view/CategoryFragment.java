package itmediaengineering.duksung.ootd.main.tab.category.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import itmediaengineering.duksung.ootd.MainActivity;
import itmediaengineering.duksung.ootd.R;
import itmediaengineering.duksung.ootd.main.tab.category.adapter.CardPagerAdapter;
import itmediaengineering.duksung.ootd.main.tab.category.adapter.CardPagerViewHolder;
import itmediaengineering.duksung.ootd.main.tab.category.presenter.CategoryPresenter;
import itmediaengineering.duksung.ootd.main.tab.upload.UploadActivity;

/*MainFragment는 home 탭을 보여준다
날씨정보와 위치정보 추천받은 이미지 카드뷰 페이저를 관리해야하며
추가적으로 서버로 부터 받은 추천 데이터의 이미지와 해시태그 값에 대해 웹 검색을 연결해야 함
-------------------------------------------------------------------------
현재 위치정보와 날씨정보 요청은 날씨정보 트래픽 용량 부족으로 막아놓은 상태
서버 추천알고리즘 미완성으로 통신 없이 가상 이미지만 뿌려주고 있음
*/

public class CategoryFragment extends Fragment implements MainActivity.onKeyBackPressedListener {
    public static final String TAG = CategoryFragment.class.getSimpleName();

    @BindView(R.id.category_top)
    CardView categoryTop;
    @BindView(R.id.category_bottom)
    CardView categoryBottom;
    @BindView(R.id.category_pair)
    CardView categoryPair;
    @BindView(R.id.category_outer)
    CardView categoryOuter;
    @BindView(R.id.pager)
    ViewPager viewPager;

    protected FragmentManager fm;

    public static CategoryFragment newInstance(){
        return new CategoryFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_category, container, false);
        ButterKnife.bind(this, rootView);

        fm = getChildFragmentManager();

        CardPagerAdapter pagerAdapter = new CardPagerAdapter(getActivity().getSupportFragmentManager());
        pagerAdapter.notifyDataSetChanged();
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setCurrentItem(3);
        viewPager.setPadding(100, 0, 100, 0);

        for (int i = 0; i < 5; i++) {
            pagerAdapter.addItem(CardPagerViewHolder.newInstance());
        }

        fm.beginTransaction()
                .add(R.id.pager, CategoryRecommendFragment.newInstance(), "mainFragment")
                .commit();

        return rootView;
    }

    @OnClick(R.id.category_top)
    public void onClickCategoryTop(){
        Intent intent = new Intent(getActivity(), CategoryActivity.class);
        intent.putExtra("category", "top");
        intent.putExtra("title", "Top");
        startActivity(intent);
    }

    @OnClick(R.id.category_bottom)
    public void onClickCategoryBottom(){
        Intent intent = new Intent(getActivity(), CategoryActivity.class);
        intent.putExtra("category", "bottom");
        intent.putExtra("title", "Bottom");
        startActivity(intent);
    }

    @OnClick(R.id.category_pair)
    public void onClickCategoryPair(){
        Intent intent = new Intent(getActivity(), CategoryActivity.class);
        intent.putExtra("category", "pair");
        intent.putExtra("title", "Pair");
        startActivity(intent);
    }

    @OnClick(R.id.category_outer)
    public void onClickCategoryOuter(){
        Intent intent = new Intent(getActivity(), CategoryActivity.class);
        intent.putExtra("category", "outer");
        intent.putExtra("title", "Outer");
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
    }
    @Override
    public void onBack() {
        MainActivity activity = (MainActivity)getActivity();
        activity.setOnBackPressedListener(null);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.e("Other", "onAttach()");
        ((MainActivity)context).setOnBackPressedListener(this);
    }
}
