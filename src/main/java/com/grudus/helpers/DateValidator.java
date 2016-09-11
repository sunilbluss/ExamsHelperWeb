package com.grudus.helpers;

import java.util.Calendar;
import java.util.Date;


public class DateValidator {

    public static final Date MINIMAL_DATE;

    static {
        Calendar start = Calendar.getInstance();
        start.set(2014, Calendar.FEBRUARY, 1);

        MINIMAL_DATE = start.getTime();
    }

    public static boolean dateIsCorrect(Date date) {
        return date.getTime() > MINIMAL_DATE.getTime()
                && date.getTime() < Calendar.getInstance().getTimeInMillis();
    }

}
