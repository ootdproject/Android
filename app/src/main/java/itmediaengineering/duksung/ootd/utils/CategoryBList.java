package itmediaengineering.duksung.ootd.utils;

import java.util.ArrayList;
import java.util.Arrays;

public class CategoryBList {
    public ArrayList<String> topList = new ArrayList<>(
            Arrays.asList("blouse", "dress_shirt", "hoody", "knitwear", "longsleeve",
                    "off_shoulder", "shortsleeve", "sleeveless", "sweatshirt"));
    public ArrayList<String> bottomList = new ArrayList<>(
            Arrays.asList("leggings", "longskirt", "miniskirt", "pants", "shorts"));
    public ArrayList<String> outerList = new ArrayList<>(
            Arrays.asList("cardigan", "coat", "hoodzipup", "jacket", "padding"));
    public ArrayList<String> pairList = new ArrayList<>(
            Arrays.asList("jump_suit", "longdress", "minidress", "suit_twopiece"));

    public ArrayList<String> getTopList() {
        return topList;
    }

    public ArrayList<String> getBottomList() {
        return bottomList;
    }

    public ArrayList<String> getOuterList() {
        return outerList;
    }

    public ArrayList<String> getPairList() {
        return pairList;
    }
}