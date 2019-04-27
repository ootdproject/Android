package itmediaengineering.duksung.ootd.data.feed;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Post {

    @SerializedName("post_id")
    @Expose
    private String postId;
    @SerializedName("member_id")
    @Expose
    private String memberId;
    @SerializedName("post_created_at")
    @Expose
    private String postCreatedAt;
    @SerializedName("post_count")
    @Expose
    private Integer postCount;
    @SerializedName("combi_id")
    @Expose
    private String combiId;

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getPostCreatedAt() {
        return postCreatedAt;
    }

    public void setPostCreatedAt(String postCreatedAt) {
        this.postCreatedAt = postCreatedAt;
    }

    public Integer getPostCount() {
        return postCount;
    }

    public void setPostCount(Integer postCount) {
        this.postCount = postCount;
    }

    public String getCombiId() {
        return combiId;
    }

    public void setCombiId(String combiId) {
        this.combiId = combiId;
    }

}