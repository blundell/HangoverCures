package com.blundell.hangovercures.comments;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Hours;
import org.joda.time.Minutes;
import org.joda.time.Months;
import org.joda.time.Weeks;
import org.joda.time.Years;

class CommentTimeStampFormatter {

    public String format(DateTime commentedAt) {
        DateTime now = DateTime.now();
        Minutes minutesBetween = Minutes.minutesBetween(commentedAt, now);
        if (minutesBetween.isLessThan(Minutes.ONE)) {
            return "just now";
        }
        Hours hoursBetween = Hours.hoursBetween(commentedAt, now);
        if (hoursBetween.isLessThan(Hours.ONE)) {
            return formatMinutes(minutesBetween.getMinutes());
        }
        Days daysBetween = Days.daysBetween(commentedAt, now);
        if (daysBetween.isLessThan(Days.ONE)) {
            return formatHours(hoursBetween.getHours());
        }
        Weeks weeksBetween = Weeks.weeksBetween(commentedAt, now);
        if (weeksBetween.isLessThan(Weeks.ONE)) {
            return formatDays(daysBetween.getDays());
        }
        Months monthsBetween = Months.monthsBetween(commentedAt, now);
        if (monthsBetween.isLessThan(Months.ONE)) {
            return formatWeeks(weeksBetween.getWeeks());
        }
        Years yearsBetween = Years.yearsBetween(commentedAt, now);
        if (yearsBetween.isLessThan(Years.ONE)) {
            return formatMonths(monthsBetween.getMonths());
        }
        return formatYears(yearsBetween.getYears());
    }

    private String formatMinutes(int minutes) {
        return format(minutes, " minute ago", " minutes ago");
    }

    private String formatHours(int hours) {
        return format(hours, " hour ago", " hours ago");
    }

    private String formatDays(int days) {
        return format(days, " day ago", " days ago");
    }

    private String formatWeeks(int weeks) {
        return format(weeks, " week ago", " weeks ago");
    }

    private String formatMonths(int months) {
        return format(months, " month ago", " months ago");
    }

    private String formatYears(int years) {
        return format(years, " year ago", " years ago");
    }

    private String format(int hand, String singular, String plural) {
        if (hand == 1) {
            return hand + singular;
        } else {
            return hand + plural;
        }
    }

}
