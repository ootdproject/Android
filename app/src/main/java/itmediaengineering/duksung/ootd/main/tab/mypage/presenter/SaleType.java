package itmediaengineering.duksung.ootd.main.tab.mypage.presenter;

public enum SaleType {
    onSale(true, null),
    soldOut(false, null),
    pick(null, true);

    public final Boolean isSale;
    public final Boolean isPick;

    SaleType(Boolean isSale, Boolean isPick) {
        this.isSale = isSale;
        this.isPick = isPick;
    }
}
