package cheema.hardeep.sahibdeep.brotherhood.utils;

import android.util.Log;

import org.jsoup.internal.StringUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cheema.hardeep.sahibdeep.brotherhood.models.Genre;
import cheema.hardeep.sahibdeep.brotherhood.models.Movie;

import static cheema.hardeep.sahibdeep.brotherhood.utils.Constants.APP_NAME;
import static cheema.hardeep.sahibdeep.brotherhood.utils.Constants.BASE_IMAGE_URL;
import static cheema.hardeep.sahibdeep.brotherhood.utils.Constants.COMMA;
import static cheema.hardeep.sahibdeep.brotherhood.utils.Constants.DATA_DATE_FORMAT;
import static cheema.hardeep.sahibdeep.brotherhood.utils.Constants.DISPLAY_DATE_FORMAT;
import static cheema.hardeep.sahibdeep.brotherhood.utils.Constants.DISPLAY_DATE_WEEK_RANGE_FORMAT;
import static cheema.hardeep.sahibdeep.brotherhood.utils.Constants.EMPTY;
import static cheema.hardeep.sahibdeep.brotherhood.utils.Constants.HOURS;
import static cheema.hardeep.sahibdeep.brotherhood.utils.Constants.MINUTES;
import static cheema.hardeep.sahibdeep.brotherhood.utils.Constants.RELEASE_DATE_FORMAT;
import static cheema.hardeep.sahibdeep.brotherhood.utils.Constants.WEEK_FORMAT;

public class Utilities {

    /**
     * Given url path and size, this function return proper image url for Glide
     * Example = http://image.tmdb.org/t/p/w342/1TUg5pO1VZ4B0Q1amk3OlXvlpXV.jpg
     */
    public static String createImageUrl(String path, String size) {
        return BASE_IMAGE_URL + size + path;
    }

    /**
     * Get current date in YYYY-MM-DD format
     */
    public static String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat(DATA_DATE_FORMAT, Locale.US);
        return sdf.format(new Date());
    }

    /**
     * Convert Date from yyyy-MM-dd to MMM dd, yyyy
     */
    public static String convertDate(String movieDate) {
        if (movieDate != null) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATA_DATE_FORMAT, Locale.US);
            SimpleDateFormat resultFormat = new SimpleDateFormat(RELEASE_DATE_FORMAT, Locale.US);
            Date date;
            try {
                date = simpleDateFormat.parse(movieDate);
            } catch (Exception e) {
                return EMPTY;
            }
            return resultFormat.format(date);
        } else {
            return EMPTY;
        }
    }

    /**
     * Create Comma seperated Genre String from Genre list
     * @param genres
     */
    public static String createGenreString(List<Genre> genres) {
        List<String> genreNames = new ArrayList<>();
        for (Genre genre : genres) {
           genreNames.add(genre.getName());
        }
        return StringUtil.join(genreNames, COMMA);
    }

    /**
     * Get Date object from String
     */
    public static Date getDateFromString(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATA_DATE_FORMAT, Locale.US);
        try {
            return sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.d(APP_NAME, "Error: " + e.getMessage());
        }
        return null;
    }

    /**
     * Get Week from Date
     */
    public static String getWeekFromDate(String date) {
        return new SimpleDateFormat(WEEK_FORMAT, Locale.US).format(getDateFromString(date));
    }

    /**
     * Check if given date is in future
     */
    public static boolean isDateInFuture(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATA_DATE_FORMAT, Locale.US);
        Date strDate;
        try {
            strDate = sdf.parse(date);
            return System.currentTimeMillis() < strDate.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            Log.d(APP_NAME, "Error: " + e.getMessage());
        }
        return false;
    }

    /**
     * Check if given date is Tomorrow
     */
    public static boolean isDateTomorrow(String date) {
        Date currentDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(DATA_DATE_FORMAT, Locale.US);
        currentDate.setDate(currentDate.getDate() + 1);
        String tomorrowDate = sdf.format(currentDate);
        return date.equals(tomorrowDate);
    }

    /**
     * Generate Display Date of format {EEEE ~ MMM d, yyyy}
     */
    public static String generateDisplayDate(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATA_DATE_FORMAT, Locale.US);
        SimpleDateFormat displaySdf = new SimpleDateFormat(DISPLAY_DATE_FORMAT, Locale.US);
        try {
            Date resultDate = sdf.parse(date);
            return displaySdf.format(resultDate);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.d(APP_NAME, "Error: " + e.getMessage());
        }
        return date;
    }

    /**
     * Get the minimum date from provided list of movies
     */
    public static String getMinMaxDateInWeek(List<Movie> movies) {
        SimpleDateFormat sdf = new SimpleDateFormat(DISPLAY_DATE_WEEK_RANGE_FORMAT, Locale.US);
        List<Date> dates = new ArrayList<>();
        for (Movie movie : movies) {
            if (Utilities.isDateInFuture(movie.getReleaseDate())) {
                dates.add(Utilities.getDateFromString(movie.getReleaseDate()));
            }
        }
        return dates.size() > 0 ? sdf.format(Collections.min(dates)) + " - " + sdf.format(Collections.max(dates)) : null;
    }
}