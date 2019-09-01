package itmediaengineering.duksung.ootd.main.tab.upload;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import itmediaengineering.duksung.ootd.R;
import itmediaengineering.duksung.ootd.main.tab.upload.classifier.Classifier;
import itmediaengineering.duksung.ootd.main.tab.upload.classifier.TensorFlowImageClassifier;
import itmediaengineering.duksung.ootd.main.tab.upload.classifier.env.ClassifierImageUtils;
import itmediaengineering.duksung.ootd.utils.ImageUtils;

public class ClassifierActivity extends AppCompatActivity {

    private static final int INPUT_SIZE = 224;
    private static final int IMAGE_MEAN = 128;
    private static final float IMAGE_STD = 128.0f;
    private static final String INPUT_NAME = "input";
    private static final String OUTPUT_NAME = "MobilenetV1/Predictions/Softmax";

    private static final String MODEL_FILE = "file:///android_asset/20000_2.pb";
    private static final String LABEL_FILE = "file:///android_asset/labels.txt";

    private Classifier classifier;

    private Bitmap rgbFrameBitmap = null;
    private Bitmap croppedBitmap = null;
    private Bitmap cropCopyBitmap;

    ArrayList<Classifier.Recognition> recognitions = new ArrayList<Classifier.Recognition>();

    private int previewWidth = 0;
    private int previewHeight = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classifier);

        rgbFrameBitmap = Bitmap.createBitmap(previewWidth, previewHeight, Bitmap.Config.ARGB_8888);
        croppedBitmap = Bitmap.createBitmap(INPUT_SIZE, INPUT_SIZE, Bitmap.Config.ARGB_8888);
        Bitmap img = getIntent().getParcelableExtra("classifier");

        previewWidth = img.getWidth();
        previewHeight = img.getHeight();

        classifier =
                TensorFlowImageClassifier.create(
                        getAssets(),
                        MODEL_FILE,
                        LABEL_FILE,
                        INPUT_SIZE,
                        IMAGE_MEAN,
                        IMAGE_STD,
                        INPUT_NAME,
                        OUTPUT_NAME);

        /*final Canvas canvas = new Canvas(croppedBitmap);
        canvas.drawBitmap(rgbFrameBitmap, frameToCropTransform, null);

        // For examining the actual TF input.
        if (SAVE_PREVIEW_BITMAP) {
            ClassifierImageUtils.saveBitmap(croppedBitmap);
        }*/

        /*AsyncTask.execute(
                () -> {
                    //final long startTime = SystemClock.uptimeMillis();
                    final List<Classifier.Recognition> results = classifier.recognizeImage(img);
                    //lastProcessingTimeMs = SystemClock.uptimeMillis() - startTime;
                    recognitions = (ArrayList<Classifier.Recognition>) results;
                    cropCopyBitmap = Bitmap.createBitmap(croppedBitmap);
                    //resultsView.setResults(results);
                    //requestRender();
                    //computing = false;
                });*/

        Intent intent = new Intent();
        intent.putParcelableArrayListExtra("classifier_result", recognitions);
        setResult(RESULT_OK, intent);
        finish();
    }


}
