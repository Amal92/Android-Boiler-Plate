package com.paul.cruz.Utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by amal on 12/10/17.
 */
public class HelperClass {

    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
