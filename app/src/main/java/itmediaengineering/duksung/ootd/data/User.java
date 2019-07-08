package itmediaengineering.duksung.ootd.data;

import android.support.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("nickname")
    @Expose
    private String nickname;
    @SerializedName("providerType")
    @Expose
    private String providerType;
    @SerializedName("providerUserId")
    @Expose
    private String providerUserId;
    @SerializedName("token")
    @Expose
    private String token;

    public User(String gender, String nickname, @Nullable String providerType,
                String providerUserId, @Nullable String token) {
        this.gender = gender;
        this.nickname = nickname;
        this.providerType = providerType;
        this.providerUserId = providerUserId;
        this.token = token;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getProviderType() {
        return providerType;
    }

    public void setProviderType(String providerType) {
        this.providerType = providerType;
    }

    public String getProviderUserId() {
        return providerUserId;
    }

    public void setProviderUserId(String providerUserId) {
        this.providerUserId = providerUserId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}