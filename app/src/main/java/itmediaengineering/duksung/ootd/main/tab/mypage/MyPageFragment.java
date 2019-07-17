package itmediaengineering.duksung.ootd.main.tab.mypage;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import itmediaengineering.duksung.ootd.R;
import itmediaengineering.duksung.ootd.main.tab.mypage.adapter.MyPageGalleryAdapter;
import itmediaengineering.duksung.ootd.main.tab.mypage.presenter.MyPageContract;
import itmediaengineering.duksung.ootd.main.tab.mypage.presenter.MyPagePresenter;

/*
MyPageFragment에서는 사용자 정보 페이지(myPage) 탭을 보여줌
서버에서 받아온 사용자 업로드 이미지를 갤러리 형식으로 보여주어야 하며
로그아웃 및 사용자 자기소개를 수정할 수 있도록 구성하여야 함
--------------------------------------------------------------------
현재 플리커에서 받아오는 이미지를 recyclerView와 GridView를 통해서 보여주고 있음
Recycler 뷰에 헤더를 달아서 사용자 정보 부분과 갤러리 부분을 통합하였음
*/

public class MyPageFragment extends Fragment implements MyPageContract.View {
    private static final String TAG = MyPageFragment.class.getSimpleName();

    @BindView(R.id.mypage_recycler_view)
    RecyclerView myPageRecyclerView;

    protected MyPageGalleryAdapter adapter;
    protected MyPagePresenter presenter;

    private final int TYPE_HEADER = 0;
    private final int TYPE_ITEM = 1;

    public static MyPageFragment newInstance(){
        return new MyPageFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_my_page, container, false);
        ButterKnife.bind(this, rootView);

        adapter = new MyPageGalleryAdapter();//, items);

        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 3);
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
        });
        setUpAdapter();
        return rootView;
    }

    private void setUpAdapter(){
        if(isAdded()){  // 프래그먼트가 액티비티에 연결되었는지(호스팅되고 있는지) 확인
                        // 그렇지 않다면 액티비티에 의존한 오퍼레이션 실행은 실패
                        // 프래그먼트는 어떤 액티비티에도 연결되지 않은 상태로 존재할 수 있다
            myPageRecyclerView.setAdapter(adapter);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        presenter = new MyPagePresenter();
        presenter.attachView(this);
        presenter.setAdapterModel(adapter);
        presenter.setAdapterView(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        refreshList(null);
    }

    protected void refreshList(String search) {
        presenter.getMyGallery();
    }

    @Override
    public void toast(String msg) {
        Runnable r = () -> Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
        getActivity().runOnUiThread(r);
    }

    @Override
    public void onSuccessGetGallery() {
        Log.d(TAG, "Success getting My Page Gallery!");
    }
}
