package com.rp.mood.diary.utils;

import static com.rp.mood.diary.utils.DateUtils.normalize;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.toRadians;

public class PerturbationHelper {

    public static double applyJupiterPerturbation(double longitude, double Mj, double julianDate) {
        double Ms = normalize(316.9670 + 0.0334442282 * julianDate);
        return  longitude -0.332 * sin(toRadians(2*Mj - 5*Ms - 67.6))
                -0.056 * sin(toRadians(2*Mj - 2*Ms + 21))
                +0.042 * sin(toRadians(3*Mj - 5*Ms + 21))
                -0.036 * sin(toRadians(Mj - 2*Ms))
                +0.022 * cos(toRadians(Mj - Ms))
                +0.023 * sin(toRadians(2*Mj - 3*Ms + 52))
                -0.016 * sin(toRadians(Mj - 5*Ms - 69));
    }

    public static double applyMoonsPerturbation(double julianDate, double longitude, double Mm, double Nm, double Lm) {

        double F = Lm -Nm;
        double Ms =   normalize(356.0470 + 0.9856002585 * julianDate);
        double ws = 282.9404 + 4.70935E-5   * julianDate;
        double Ls = Ms + ws;
        double D = Lm-Ls;
        return longitude-1.274 * sin(toRadians(Mm - 2*D))    //(Evection)
                +0.658 * sin(toRadians(2*D))     //  (Variation))
                -0.186 * sin(toRadians(Ms))       //  (Yearly equation))
                -0.059 * sin(toRadians(2*Mm - 2*D))
                -0.057 * sin(toRadians(Mm - 2*D + Ms))
                +0.053 * sin(toRadians(Mm + 2*D))
                +0.046 * sin(toRadians(2*D - Ms))
                +0.041 * sin(toRadians(Mm - Ms))
                -0.035 * sin(toRadians(D))         //   (Parallactic equation))
                -0.031 * sin(toRadians(Mm + Ms))
                -0.015 * sin(toRadians(2*F - 2*D))
                +0.011 * sin(toRadians(Mm - 4*D));
    }

    public static double applySaturnPerturbation(double eclipticLongitude, double Ms,double julianDate) {
        double Mj = normalize(19.8950 + 0.0830853001 * julianDate);
        return  eclipticLongitude +0.812 * sin(toRadians(2*Mj - 5*Ms - 67.6))
                -0.229 * cos(toRadians(2*Mj - 4*Ms - 2))
                +0.119 * sin(toRadians(Mj - 2*Ms - 3))
                +0.046 * sin(toRadians(2*Mj - 6*Ms - 69))
                +0.014 * sin(toRadians(Mj - 3*Ms + 32));
    }
}
