package itmediaengineering.duksung.ootd.data.mygallery;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/*
myPage 탭 프래그먼트에서 사용자 업로드 이미지 정보를 받아올 객체
현재는 플리커에서 받아오는 객체로 구성하였음
서버와 연결 후 다시 수정 해야함
*/

public class GalleryResponse {
    @SerializedName("photos")
    @Expose
    private Photos photos;
    @SerializedName("stat")
    @Expose
    private String stat;

    public Photos getPhotos() {
        return photos;
    }

    public void setPhotos(Photos photos) {
        this.photos = photos;
    }

    public String getStat() {
        return stat;
    }

    public void setStat(String stat) {
        this.stat = stat;
    }

}
