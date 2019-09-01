package itmediaengineering.duksung.ootd.utils;

public enum LikeType {
    LIKE(true),
    CANCEL(false);

    public final boolean key;

    LikeType(boolean key) {
        this.key = key;
    }
}
