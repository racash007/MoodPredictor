package freewill.rakesh.com.howdoyoufeel.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import freewill.rakesh.com.howdoyoufeel.R;

public enum MoodImage {
    ANGRY("Angry", R.drawable.angry),
    ANXIOUS("Anxious",R.drawable.anxious),
    BORED("Bored",R.drawable.bored),
    BUSY("busy",R.drawable.busy),
    FRUSTRATED("Frustrated",R.drawable.frustrated),
    HAPPY("Happy",R.drawable.happy);

    public static final Map<String, Integer> moodMap = Collections.unmodifiableMap(initializeMapping());
    private final String moodName;
    private final int moodImage;


    public String getMoodName() {
        return moodName;
    }

    public int getMoodImage() {
        return moodImage;
    }

    MoodImage(String moodName, int moodImage)
{
    this.moodName = moodName;
    this.moodImage = moodImage;
}

    private static Map<String, Integer> initializeMapping() {
        Map<String, Integer> mMap = new HashMap<String, Integer>();
        for (MoodImage s : MoodImage.values()) {
            mMap.put(s.getMoodName(), s.getMoodImage());
        }
        return mMap;
    }
}
