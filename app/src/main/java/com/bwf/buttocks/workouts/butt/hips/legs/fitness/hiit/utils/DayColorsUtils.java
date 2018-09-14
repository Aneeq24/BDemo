package com.bwf.buttocks.workouts.butt.hips.legs.fitness.hiit.utils;

import android.graphics.Typeface;
import android.widget.TextView;

import com.bwf.buttocks.workouts.butt.hips.legs.fitness.hiit.R;

import java.util.Calendar;


public class DayColorsUtils {

    public static void setDayColors(TextView textView, int textColor, int typeface, int background) {
        if (textView == null) {
            return;
        }
        textView.setTypeface(null, typeface);
        textView.setTextColor(textColor);
        textView.setBackgroundResource(background);
    }


    public static void setSelectedDayColors(TextView dayLabel, CalendarProperties calendarProperties) {
        setDayColors(dayLabel, calendarProperties.getSelectionLabelColor(), Typeface.NORMAL,
                R.drawable.background_color_circle_selector);

        dayLabel.getBackground().setColorFilter(calendarProperties.getSelectionColor(),
                android.graphics.PorterDuff.Mode.MULTIPLY);
    }

    public static void setCurrentMonthDayColors(Calendar day, Calendar today, TextView dayLabel,
                                                CalendarProperties calendarProperties) {
        if (today.equals(day)) {
            setDayColors(dayLabel, calendarProperties.getTodayLabelColor(), Typeface.BOLD,
                    R.drawable.background_transparent);
        } else {
            setDayColors(dayLabel, calendarProperties.getDaysLabelsColor(), Typeface.NORMAL,
                    R.drawable.background_transparent);
        }
    }
}
