package com.codepath.apps.MyTwitterRedux;

import java.text.DecimalFormat;

/**
 * Created by mrozelle on 5/31/2015.
 */
public class ThemeStringFormatter {
    private static final String[] UNITS = new String[]{"","K", "M", "B"};
    private static final int MAX_LENGTH = 4;

    public static String roundedNumberFormat(String numStr) {

        int num = Integer.parseInt(numStr);
        if (num < 10000) {
            return new DecimalFormat("#,###,###").format(num);
        } else {
            String r = new DecimalFormat("##0.#E0").format(Double.parseDouble(numStr));
            r = r.replaceAll("E[0-9]", UNITS[Character.getNumericValue(r.charAt(r.length() - 1)) / 3]);
            while(r.length() > MAX_LENGTH || r.matches("[0-9]+\\.[a-z]")){
                r = r.substring(0, r.length()-2) + r.substring(r.length() - 1);
            }
            return r;
        }

    }
}
