package com.bbb.bbdev1.run;

public final class Utils {
    public static double convertToKms(String units, double distance) {
        if (units.contains("kms")) {
            return distance;
        }
        return distance * 1.6;
    }
    public static double convertFromKms(String units, double kms) {
        if (units.contains("kms")) {
            return kms;
        }
        return kms / 1.6;
    }
}
