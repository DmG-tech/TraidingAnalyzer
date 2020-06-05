package util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class FloatRounding {
    public static float round(float value) {
        float result = new BigDecimal(value).setScale(2, RoundingMode.CEILING).floatValue();
        return result;
    }

    public static float round(String s) {
        try {
            return round(FloatParser.parse(s));
        }
        catch (Exception e) {
            return 0;
        }
    }
}
