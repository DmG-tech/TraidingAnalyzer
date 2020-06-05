package util;

public class FloatParser {
    public static float parse(String s) throws NullPointerException {
        float result = 0f;
        if (s == null) throw new NullPointerException("указаны некорректные данные");
        String value = s.replace(",","");
        //Для миллиардов
        if (value.endsWith("B")) {
            result = Float.parseFloat(value.substring(0, value.length() - 2)) * 1000000000;
        }
        //Для миллионов
        else if (value.endsWith("M")) {
            result = Float.parseFloat(value.substring(0, value.length() - 2)) * 100000000;
        }
        //Для процентов
        else if (value.endsWith("%")) {
            result = Float.parseFloat(value.substring(0, value.length() - 2));
        } else {
            result = Float.parseFloat(value);
        }
        return FloatRounding.round(result);
    }
}
