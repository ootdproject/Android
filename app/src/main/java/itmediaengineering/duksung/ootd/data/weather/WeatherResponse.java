package itmediaengineering.duksung.ootd.data.weather;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/*
공공 데이터 포탈 api로
날씨 정보를 받아오는 객체
구성 완료됨
*/

public class WeatherResponse {

    @SerializedName("response")
    @Expose
    private Response response;

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

}