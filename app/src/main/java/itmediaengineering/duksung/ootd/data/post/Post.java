package itmediaengineering.duksung.ootd.data.post;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Post implements Parcelable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("member")
    @Expose
    private Member member;
    @SerializedName("count")
    @Expose
    private Integer count;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("updatedAt")
    @Expose
    private String updatedAt;
    @SerializedName("dong")
    @Expose
    private String dong;
    @SerializedName("cost")
    @Expose
    private String cost;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("sale")
    @Expose
    private Boolean sale;
    @SerializedName("imageUrl")
    @Expose
    private String imageUrl;
    @SerializedName("categoryA")
    @Expose
    private String categoryA;
    @SerializedName("categoryB")
    @Expose
    private String categoryB;

    public Post(String title, String imageUrl) {
        this.title = title;
        this.imageUrl = imageUrl;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getDong() {
        return dong;
    }

    public void setDong(String dong) {
        this.dong = dong;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getSale() {
        return sale;
    }

    public void setSale(Boolean sale) {
        this.sale = sale;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCategoryA() {
        return categoryA;
    }

    public void setCategoryA(String categoryA) {
        this.categoryA = categoryA;
    }

    public String getCategoryB() {
        return categoryB;
    }

    public void setCategoryB(String categoryB) {
        this.categoryB = categoryB;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeParcelable(this.member, flags);
        dest.writeValue(this.count);
        dest.writeString(this.createdAt);
        dest.writeString(this.updatedAt);
        dest.writeString(this.dong);
        dest.writeString(this.cost);
        dest.writeString(this.title);
        dest.writeString(this.description);
        dest.writeValue(this.sale);
        dest.writeString(this.imageUrl);
        dest.writeString(this.categoryA);
        dest.writeString(this.categoryB);
    }

    protected Post(Parcel in) {
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.member = in.readParcelable(Member.class.getClassLoader());
        this.count = (Integer) in.readValue(Integer.class.getClassLoader());
        this.createdAt = in.readString();
        this.updatedAt = in.readString();
        this.dong = in.readString();
        this.cost = in.readString();
        this.title = in.readString();
        this.description = in.readString();
        this.sale = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.imageUrl = in.readString();
        this.categoryA = in.readString();
        this.categoryB = in.readString();
    }

    public static final Parcelable.Creator<Post> CREATOR = new Parcelable.Creator<Post>() {
        @Override
        public Post createFromParcel(Parcel source) {
            return new Post(source);
        }

        @Override
        public Post[] newArray(int size) {
            return new Post[size];
        }
    };
}