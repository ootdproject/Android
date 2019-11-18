package itmediaengineering.duksung.ootd.data.post;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PostRequest {

    @SerializedName("categoryA")
    @Expose
    private String categoryA;
    @SerializedName("categoryB")
    @Expose
    private String categoryB;
    @SerializedName("cost")
    @Expose
    private String cost;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("dong")
    @Expose
    private String dong;
    @SerializedName("imageUrl")
    @Expose
    private String imageUrl;
    @SerializedName("sale")
    @Expose
    private Boolean sale;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("color")
    @Expose
    private String color;

    public PostRequest(
            String categoryA,
            String categoryB,
            String cost,
            String description,
            String dong,
            Boolean sale,
            String title,
            String color
    ) {
        this.categoryA = categoryA;
        this.categoryB = categoryB;
        this.cost = cost;
        this.description = description;
        this.dong = dong;
        this.sale = sale;
        this.title = title;
        this.color = color;
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

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDong() {
        return dong;
    }

    public void setDong(String dong) {
        this.dong = dong;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Boolean getSale() {
        return sale;
    }

    public void setSale(Boolean sale) {
        this.sale = sale;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
