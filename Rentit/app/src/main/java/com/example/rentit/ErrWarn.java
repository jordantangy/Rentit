package com.example.rentit;

import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ErrWarn {
    public ErrWarn() {
    }

    public static boolean errName(String name, TextView textView) {
        if (name.length() < 1) {
            ErrWarn.earnEdit(textView, "השם לא תקין");
            return true;
        }
        return false;
    }

    public static boolean errPhone(String phone, TextView textView) {
        if (phone.length() < 7) {
            ErrWarn.earnEdit(textView, "מספר לא תקין");
            return true;
        }
        return false;
    }

    public static boolean errArea(String area, TextView textView) {
        if (area.length() < 1||area.equals("בחר אזור")) {
            ErrWarn.earnEdit(textView, "לא בחרת אזור");
            return true;
        }
        return false;

    }

    public static boolean errPrice(String price, TextView textView) {
        int priseInt = 0;
        try {
            priseInt = Integer.valueOf(price);
        } catch (RuntimeException e) {
            ErrWarn.earnEdit(textView, "אנא הכנס רק מספרים שלמים");
            return true;


        }
        if (priseInt < 1 || priseInt > 15000) {
            ErrWarn.earnEdit(textView, "אנא הכנס רק מספרים שלמים");
            return true;
        }
        return false;

    }

    public static boolean errInsurance(String insurance, TextView textView) {
        if (insurance.length() < 5) {
            ErrWarn.earnEdit(textView, "ביטוח לא תקין");
            return true;
        }
        return false;
    }

    public static boolean errTypeCar(String typeCar, TextView textView) {
        if (typeCar.length() < 2) {
            ErrWarn.earnEdit(textView, "סוג רכב לא קיים");
            return true;
        }

        return false;

    }

    public static boolean errYearCar(String yearCar, TextView textView) {
        int priseInt = 0;
        try {
            priseInt = Integer.valueOf(yearCar);
        } catch (RuntimeException e) {
            ErrWarn.earnEdit(textView, "אנא הכנס רק מספרים שלמים");
            return true;
        }
        if (priseInt < 1800 || priseInt > 2030) {
            ErrWarn.earnEdit(textView, "אנא הכנס שנה מציאותית");
            return true;
        }

        return false;

    }

    public static boolean errCity(String city, TextView textView) {
        if (city.length() < 2){
            earnEdit(textView, "שם עיר לא תקין");
            return true;
        }
        return false;

    }

    public static boolean errStartDate(String startDate, TextView textView) {
        if (startDate.length() < 6) {
            earnEdit(textView, "תאריך לא תקין");
            return true;
        }
        return false;


    }

    public static boolean errEndData(String endData, TextView textView) {
        if (endData.length() < 6) {
            earnEdit(textView, "תאריך לא תקין");
            return true;
        }
        return false;


    }

    public static boolean errRemarker(String remarker, TextView textView) {
        return false;


    }

    public static boolean errImage(String image, int size, TextView textView) {
        if (size < 1){
            earnEdit(textView, "אנא הכנס לפחות תמונה אחת");
            return true;
        }
        return false;

    }

    public static void earnEdit(TextView textView, String warn) {
        textView.setText(warn);
        textView.setVisibility(View.VISIBLE);
    }

    public static boolean errEmail(String email) {
        String emailRegex = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
        Pattern emailPat = Pattern.compile(emailRegex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = emailPat.matcher(email);
        return !matcher.find();
    }
}
