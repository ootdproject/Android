package itmediaengineering.duksung.ootd.utils;

public class DeepModelPath {
    public static final int INPUT_SIZE = 224;
    public static final int IMAGE_MEAN = 128;
    public static final float IMAGE_STD = 128.0f;
    public static final String INPUT_NAME = "input";
    public static final String OUTPUT_NAME = "MobilenetV1/Predictions/Softmax";
    //public static final String MODEL_FILE = "file:///android_asset/20000_2.pb";
    //public static final String MODEL_FILE = "file:///android_asset/opt.pb";
    public static final String MODEL_FILE = "file:///android_asset/clothes.pb";
    public static final String LABEL_FILE = "file:///android_asset/labels.txt";
    //public static final String COLOR_MODEL_FILE = "file:///android_asset/color_test.pb";
    public static final String COLOR_MODEL_FILE = "file:///android_asset/color.pb";
    public static final String COLOR_LABEL_FILE = "file:///android_asset/color_labels.txt";
}
