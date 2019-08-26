package itmediaengineering.duksung.ootd.main.tab.mypage.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import itmediaengineering.duksung.ootd.R;
import itmediaengineering.duksung.ootd.data.post.Post;

public class EditPostPopUpActivity extends AppCompatActivity {
    public static final int MY_PAGE_POPUP_BUTTON_OPTION_STATE = 0;
    public static final int MY_PAGE_POPUP_BUTTON_OPTION_EDIT = 1;
    public static final int MY_PAGE_POPUP_BUTTON_OPTION_DELETE = 2;

    @BindView(R.id.edit_post_popup_sale_to_sold_btn)
    TextView saleToSoldBtn;
    @BindView(R.id.edit_post_popup_btn)
    TextView editPostBtn;
    @BindView(R.id.edit_post_popup_delete_btn)
    TextView deletePostBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_post_pop_up);
        ButterKnife.bind(this);

        if(getIntent().getStringExtra("sale_state") != null) {
            String saleToSoldBtnStr = getIntent().getStringExtra("sale_state");
            saleToSoldBtn.setText(saleToSoldBtnStr);
        }
    }

    @OnClick(R.id.edit_post_popup_sale_to_sold_btn)
    public void onClickPostSaleToSoldBtn(){
        Post post = getIntent().getParcelableExtra("post");
        Intent intent = new Intent();
        if(saleToSoldBtn.getText().equals("판매 중")){
            post.setSale(true);
        } else {
            post.setSale(false);
        }
        intent.putExtra("post", post);
        intent.putExtra("button_option", MY_PAGE_POPUP_BUTTON_OPTION_STATE);
        setResult(RESULT_OK, intent);
        finish();
    }

    @OnClick(R.id.edit_post_popup_btn)
    public void onClickPostEditBtn(){
        Post post = getIntent().getParcelableExtra("post");
        Intent intent = new Intent();
        intent.putExtra("post", post);
        intent.putExtra("button_option", MY_PAGE_POPUP_BUTTON_OPTION_EDIT);
        setResult(RESULT_OK, intent);
        finish();
    }

    @OnClick(R.id.edit_post_popup_delete_btn)
    public void onClickPostDeleteBtn(){
        Post post = getIntent().getParcelableExtra("post");
        Intent intent = new Intent();
        intent.putExtra("post", post);
        intent.putExtra("button_option", MY_PAGE_POPUP_BUTTON_OPTION_DELETE);
        setResult(RESULT_OK, intent);
        finish();
    }
}
