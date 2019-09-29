package itmediaengineering.duksung.ootd.search.view;

import java.util.ArrayList;

import itmediaengineering.duksung.ootd.main.tab.upload.classifier.Classifier;

public class RecognitionManager {

    private RecognitionManager() { }

    private static RecognitionManager recognitionManager;

    private ArrayList<RecognitionContract> categoryRecognitionContractList = new ArrayList<>();
    private ArrayList<RecognitionContract> colorRecognitionContractList = new ArrayList<>();

    public void setCategoryRecognitions(ArrayList<Classifier.Recognition> recognitions) {
        if(categoryRecognitionContractList.isEmpty()) {  return; }
        for (int i = 0; i < categoryRecognitionContractList.size(); i++) {
            categoryRecognitionContractList.get(i).onCategoryRecognitionChanged(recognitions);
        }
    }

    public void setColorRecognitions(ArrayList<Classifier.Recognition> recognitions) {
        if(colorRecognitionContractList.isEmpty()) {  return; }
        for (int i = 0; i < colorRecognitionContractList.size(); i++) {
            colorRecognitionContractList.get(i).onColorRecognitionChanged(recognitions);
        }
    }

    public static RecognitionManager getInstance() {
        if(recognitionManager == null) {
            recognitionManager = new RecognitionManager();
        }
        return recognitionManager;
    }

    public void addRecognitionContract(RecognitionContract recognitionContract) {
        categoryRecognitionContractList.add(recognitionContract);
        colorRecognitionContractList.add(recognitionContract);
    }
}
