package freewill.rakesh.com.howdoyoufeel.tflite;

import android.graphics.RectF;


public class Prediction {


    private final String mood;
    private final Float confidence;


    public Prediction(String mood, Float confidence) {
        this.mood = mood;
        this.confidence = confidence;
    }

    public String getMood() {
        return mood;
    }

    public Float getConfidence() {
        return confidence;
    }

    @Override
    public String toString() {
        return "Prediction{" +
                "mood='" + mood + '\'' +
                ", confidence=" + confidence +
                '}';
    }
}

  