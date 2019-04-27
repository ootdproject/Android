package itmediaengineering.duksung.ootd.data.weather;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Item {

    @SerializedName("baseDate")
    @Expose
    private Integer baseDate;
    @SerializedName("baseTime")
    @Expose
    private String baseTime;
    @SerializedName("category")
    @Expose
    private String category;
    @SerializedName("nx")
    @Expose
    private Integer nx;
    @SerializedName("ny")
    @Expose
    private Integer ny;
    @SerializedName("obsrValue")
    @Expose
    private Double obsrValue;

    public Integer getBaseDate() {
        return baseDate;
    }

    public void setBaseDate(Integer baseDate) {
        this.baseDate = baseDate;
    }

    public String getBaseTime() {
        return baseTime;
    }

    public void setBaseTime(String baseTime) {
        this.baseTime = baseTime;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Integer getNx() {
        return nx;
    }

    public void setNx(Integer nx) {
        this.nx = nx;
    }

    public Integer getNy() {
        return ny;
    }

    public void setNy(Integer ny) {
        this.ny = ny;
    }

    public Double getObsrValue() {
        return obsrValue;
    }

    public void setObsrValue(Double obsrValue) {
        this.obsrValue = obsrValue;
    }

}