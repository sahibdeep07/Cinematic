package cheema.hardeep.sahibdeep.brotherhood.database;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.List;

import cheema.hardeep.sahibdeep.brotherhood.models.UserInfo;


/**
 * This class store user preferences as UserInfo object
 * -- Object cannot be stored directly into shared preferences so this class uses GSON to convert
 * object into string and store it
 * -- Upon getting the user preferences, with help of GSON string is convert back to UserInfo object
 */
public class SharedPreferenceProvider {

    private static final String USER_INFO_PREFERENCES = "user-info-preferences";
    private static final String KEY_USER_INFO = "user-info";
    private static Gson gson = new Gson();

    /**
     * Save user name to shared preferences
     * This method get the UserInfo object from shared perference if it exist then update the name
     * If UserInfo object doesn't not exist in shared preferences then it creates a new object with
     * provided name and save it
     */
    public static void saveUserName(Context context, String name) {
        UserInfo userInfo = getUserInfo(context);
        userInfo.setName(name);
        saveUserInfo(context, userInfo);
    }

    /**
     * Get user name from shared preferences
     * This method get the UserInfo object from shared preference if it exist then return the name
     * from object
     */
    public static String getUserName(Context context) {
        return getUserInfo(context).getName();
    }

    /**
     * Save user genres to shared preferences
     * This method get the UserInfo object from shared perference if it exist then update the genres
     * If UserInfo object doesn't not exist in shared preferences then it creates a new object with
     * provided genres and save it
     */
    public static void saveUserGenres(Context context, List<String> genres) {
        UserInfo userInfo = getUserInfo(context);
        userInfo.setGenres(genres);
        saveUserInfo(context, userInfo);
    }

    /**
     * Get user genres from shared preferences
     * This method get the UserInfo object from shared preference if it exist then return the genres
     * from object
     */
    public static List<String> getUserGenres(Context context) {
        return getUserInfo(context).getGenres();
    }

    /**
     * Save user actors to shared preferences
     * This method get the UserInfo object from shared preference if it exist then update the actors
     * If UserInfo object doesn't not exist in shared preferences then it creates a new object with
     * provided actors and save it
     */
    public static void saveUserActors(Context context, List<String> actors) {
        UserInfo userInfo = getUserInfo(context);
        userInfo.setActors(actors);
        saveUserInfo(context, userInfo);
    }

    /**
     * Get user actors from shared preferences
     * This method get the UserInfo object from shared preference if it exist then return the actors
     * from object
     */
    public static List<String> getUserActors(Context context) {
        return getUserInfo(context).getActors();
    }

    /**
     * Get full userInfo from shared preferences if its exits
     */
    public static UserInfo getFullUserInfo(Context context) {
        return getUserInfo(context);
    }

    private static UserInfo getUserInfo(Context context) {
        String userInfoJson = getUserInfoPreferences(context).getString(KEY_USER_INFO, null);
        return userInfoJson != null ? gson.fromJson(userInfoJson, UserInfo.class) : new UserInfo();
    }

    private static void saveUserInfo(Context context, UserInfo userInfo) {
        SharedPreferences.Editor editor = getUserInfoPreferences(context).edit();
        editor.putString(KEY_USER_INFO, gson.toJson(userInfo));
        editor.apply();
    }


    private static SharedPreferences getUserInfoPreferences(Context context) {
        return context.getSharedPreferences(USER_INFO_PREFERENCES, Context.MODE_PRIVATE);
    }
}
