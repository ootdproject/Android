package itmediaengineering.duksung.ootd.search.view;

import java.util.ArrayList;

import itmediaengineering.duksung.ootd.main.tab.upload.classifier.Classifier;

public interface RecognitionContract {
    void onCategoryRecognitionChanged(ArrayList<Classifier.Recognition> recognitions);
    void onColorRecognitionChanged(ArrayList<Classifier.Recognition> recognitions);
}
