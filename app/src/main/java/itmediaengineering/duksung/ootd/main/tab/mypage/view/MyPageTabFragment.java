package itmediaengineering.duksung.ootd.main.tab.mypage.view;

import android.app.Activity;
import android.content.Intent;
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
import itmediaengineering.duksung.ootd.data.post.Post;
import itmediaengineering.duksung.ootd.main.tab.mypage.adapter.MyPageGalleryAdapter;
import itmediaengineering.duksung.ootd.main.tab.mypage.presenter.MyPageContract;
import itmediaengineering.duksung.ootd.main.tab.mypage.presenter.MyPagePresenter;
import itmediaengineering.duksung.ootd.main.tab.mypage.presenter.SaleType;
import itmediaengineering.duksung.ootd.main.tab.upload.UploadActivity;
import itmediaengineering.duksung.ootd.utils.BundleKey;

public class MyPageTabFragment extends Fragment implements MyPageContract.View {
    private static final String TAG = MyPageTabFragment.class.getSimpleName();

    public static final int MY_PAGE_POPUP_BUTTON_OPTION_STATE = 0;
    public static final int MY_PAGE_POPUP_BUTTON_OPTION_EDIT = 1;
    public static final int MY_PAGE_POPUP_BUTTON_OPTION_DELETE = 2;

    private static final int INTENT_REQUEST_POST_POP_UP = 302;

    @BindView(R.id.my_page_pager_recycler_view)
    RecyclerView myPageRecyclerView;

    protected MyPageGalleryAdapter adapter;
    protected MyPagePresenter presenter;

    private SaleType saleType;

    public static MyPageTabFragment newInstance(){
        return new MyPageTabFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_my_page_tab, container, false);
        ButterKnife.bind(this, rootView);

        if(getArguments() != null) {
            Bundle bundle = getArguments();
            saleType = (SaleType) bundle.get(BundleKey.SALE_TYPE);
        }

        adapter = new MyPageGalleryAdapter(getContext(), SaleType.onSale);

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
        myPageRecyclerView.setNestedScrollingEnabled(false);
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == INTENT_REQUEST_POST_POP_UP && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                Log.d(TAG, "data is null!");
                return;
            }
            Post post = data.getParcelableExtra("post");

            //각 버튼 별 처리 { 판매 상태변경 / 게시물 수정 / 게시물 삭제 }
            if (data.getIntExtra("button_option", -1)
                    == MY_PAGE_POPUP_BUTTON_OPTION_STATE) {
                presenter.editPostSaleState(post);
            } else if (data.getIntExtra("button_option", -1)
                    == MY_PAGE_POPUP_BUTTON_OPTION_EDIT) {
                Intent intent = new Intent(getActivity(), UploadActivity.class);
                intent.putExtra("post", post);
                startActivity(intent);
                //presenter.editPostSaleState(data.getParcelableExtra("post"));
            } else if (data.getIntExtra("button_option", -1)
                    == MY_PAGE_POPUP_BUTTON_OPTION_DELETE) {
                presenter.deletePost(post);
            }
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        presenter = new MyPagePresenter();
        presenter.attachView(this);
        presenter.setAdapterModel(adapter);
        presenter.setAdapterView(adapter);
        presenter.getMyPosts(saleType);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        //refreshList(saleState);
    }

    protected void refreshList(int sale) {
        presenter.getMyPosts(SaleType.pick);
    }

    @Override
    public void toast(String msg) {
        Runnable r = () -> Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
        getActivity().runOnUiThread(r);
    }

    @Override
    public void onSuccessEditPostSaleState() {
        presenter.getMyPosts(saleType);
        if(saleType == SaleType.onSale){
            toast("판매가 완료되었습니다");
        } else {
            toast("판매 중으로 바뀜");
        }
    }

    @Override
    public void onSuccessGetGallery()  {
        //refreshList(saleState);
        onResume();
        Log.d(TAG, "Success getting My Page Gallery!");
    }

    @Override
    public void onSuccessDeletePost() {
        presenter.getMyPosts(saleType);
        toast("게시물 삭제가 완료되었습니다");
    }

    @Override
    public void onStartPopUp(Post post) {
        Intent intent = new Intent(getActivity(), EditPostPopUpActivity.class);
        if(saleType == SaleType.soldOut){
            intent.putExtra("sale_state", "판매 중");
        }
        intent.putExtra("post", post);
        startActivityForResult(intent, INTENT_REQUEST_POST_POP_UP);
    }
}
