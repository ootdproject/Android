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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import itmediaengineering.duksung.ootd.R;
import itmediaengineering.duksung.ootd.main.tab.mypage.adapter.MyPageGalleryAdapter;
import itmediaengineering.duksung.ootd.main.tab.mypage.presenter.MyPageContract;
import itmediaengineering.duksung.ootd.main.tab.mypage.presenter.MyPagePresenter;

public class MyPageFragment extends Fragment implements MyPageContract.View {
    private static final String TAG = MyPageFragment.class.getSimpleName();

    @BindView(R.id.mypage_user_image)
    ImageView userImg;
    @BindView(R.id.mypage_user_nickname)
    TextView userNick;
    @BindView(R.id.mypage_user_description)
    TextView userDscrp;
    @BindView(R.id.mypage_recycler_view)
    RecyclerView myPageRecyclerView;

    protected MyPageGalleryAdapter adapter;
    protected MyPagePresenter presenter;

    public static MyPageFragment newInstance(){
        return new MyPageFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup rootView = (ViewGroup) inflater
                .inflate(R.layout.fragment_my_page, container, false);
        ButterKnife.bind(this, rootView);

        adapter = new MyPageGalleryAdapter(getContext());//, items);
        myPageRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
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
