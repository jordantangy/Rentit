package com.example.rentit;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ErrWarn {
    public ErrWarn() {
    }

    public static boolean errName(String name, EditText editText) {
        if (name.length() < 1) {
            editText.setError("שם לא תקין");
            editText.requestFocus();
            return true;
        }
        return false;
    }

    public static boolean errPhone(String phone, EditText editText) {
        if (phone.length() < 7) {
            editText.setError("מספר לא תקין");
            editText.requestFocus();
            return true;
        }
        return false;
    }

    public static boolean errArea(String area, TextView textView) {
        if (area.length() < 1||area.equals("בחר אזור")) {
            earnEdit(textView, "אזור לא תקין");
            return true;
        }
        return false;

    }

    public static boolean errPrice(String price, EditText editText) {
        int priseInt = 0;
        try {
            priseInt = Integer.valueOf(price);
        } catch (RuntimeException e) {
            editText.setError("אנא הכנס רק מספרים שלמים");
            editText.requestFocus();
            return true;


        }
        if (priseInt < 1 || priseInt > 15000) {
            editText.setError("אנא הכנס רק מספרים שלמים");
            editText.requestFocus();
            return true;
        }
        return false;

    }

    public static boolean errInsurance(String insurance, EditText editText) {
        if (insurance.length() < 3) {
            editText.setError("ביטוח לא תקין");
            editText.requestFocus();
            return true;
        }
        return false;
    }

    public static boolean errTypeCar(String typeCar, EditText editText) {
        if (typeCar.length() < 2) {
            editText.setError("סוג רכב לא קיים");
            editText.requestFocus();
            return true;
        }

        return false;

    }

    public static boolean errYearCar(String yearCar, EditText editText) {
        int priseInt = 0;
        try {
            priseInt = Integer.valueOf(yearCar);
        } catch (RuntimeException e) {
            editText.setError("אנא הכנס רק מספרים שלמים");
            editText.requestFocus();
            return true;
        }
        if (priseInt < 1800 || priseInt > 2030) {
            editText.setError("אנא הכנס רק מספרים שלמים");
            editText.requestFocus();
            return true;
        }

        return false;

    }

    public static boolean errCity(String city, EditText editText) {
        if (city.length() < 2){
            editText.setError("שם עיר לא תקין");
            editText.requestFocus();
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
