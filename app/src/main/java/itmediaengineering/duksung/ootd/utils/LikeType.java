package itmediaengineering.duksung.ootd.utils;

public enum LikeType {
    LIKE(true),
    CANCEL(false);

    public final boolean isLike;

    LikeType(boolean isLike) {
        this.isLike = isLike;
    }
}
