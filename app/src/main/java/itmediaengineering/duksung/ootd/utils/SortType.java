package itmediaengineering.duksung.ootd.utils;

public enum  SortType {
    ASC(null),
    DESC("postId,DESC");

    public final String key;

    SortType(String key) {
        this.key = key;
    }
}