package com.rp.mood.diary.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Transient;

import java.util.Date;
@Entity(generateGettersSetters = true)
public class Mood  {


    @Override
    public String toString() {
        return "{ id:'"+ moodId + "', mood:'" + mood + "', moodTime:'" + moodTime +"', moodDesc='" + moodDesc +"'}";
    }


    @Id
    private Long moodId;
    // Define a String type field to hold teacher's name
    @Property(nameInDb = "mood_name")@NotNull
    private String mood;

    // Define a String type field to hold student's address
    @Property(nameInDb = "mood_time")
    private Date moodTime;


    @Property(nameInDb = "mood_desc")
    private String moodDesc;


    @Transient
    private boolean isExpanded;



    public Mood() {
    }

    public Mood(String mood,String moodDesc) {
        this.mood = mood;
        this.moodDesc = moodDesc;
        this.moodTime = new Date();
    }

    @Generated(hash = 1880245020)
    public Mood(Long moodId, @NotNull String mood, Date moodTime, String moodDesc) {
        this.moodId = moodId;
        this.mood = mood;
        this.moodTime = moodTime;
        this.moodDesc = moodDesc;
    }

    public String getMood() {
        return this.mood;
    }

    public void setMood(String mood) {
        this.mood = mood;
    }

    public Date getMoodTime() {
        return this.moodTime;
    }

    public void setMoodTime(Date moodTime) {
        this.moodTime = moodTime;
    }

    public Long getMoodId() {
        return moodId;
    }

    public void setMoodId(Long moodId) {
        this.moodId = moodId;
    }

    public String getMoodDesc() {
        return moodDesc;
    }

    public void setMoodDesc(String moodDesc) {
        this.moodDesc = moodDesc;
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }
}
