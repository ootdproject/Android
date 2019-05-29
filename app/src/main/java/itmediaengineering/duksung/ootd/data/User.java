package itmediaengineering.duksung.ootd.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/*
사용자 정보를 전달할 때 사용할 객체
서버 구성이 완료되면 다시 작성해야 함
*/

public class User {
    @SerializedName("uid")
    @Expose
    private Integer uid;
}
