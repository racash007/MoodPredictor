package com.rp.mood.diary.model.enums;

public enum RisingSign {
    NoSign(-100,-1,-1,-1,-1),
    Aries(1,3,21,4,19),
    Taurus(2,4,20,5,20),
    Gemini(3,5,21,6,20),
    Cancer(4,6,21,7,22),
    Leo(5,7,23,8,22),
    Virgo(6,8,23,9,22),
    Libra(7,9,23,10,22),
    Scorpio(8,10,23,11,21),
    Sagittarius(9,11,22,12,21),
    Capricorn(10,12,22,1,19),
    Aquarius(11,1,20,2,18),
    Pisces(12,2,19,3,20);

    private final int signNumber;
    private final int startMonth;
    private final int startDay;
    private final int endMonth;
    private final int endDay;

    RisingSign(int signNumber, int startMonth, int startDay, int  endMonth, int endDay) {
        this.signNumber = signNumber;
        this.startMonth = startMonth;
        this.startDay = startDay;
        this.endMonth = endMonth;
        this.endDay = endDay;
    }

    public int getSignNumber() {
        return signNumber;
    }

    public int getStartMonth() {
        return startMonth;
    }

    public int getStartDay() {
        return startDay;
    }

    public int getEndMonth() {
        return endMonth;
    }

    public int getEndDay() {
        return endDay;
    }
}
