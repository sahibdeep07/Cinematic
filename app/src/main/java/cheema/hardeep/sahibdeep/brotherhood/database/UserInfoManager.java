package cheema.hardeep.sahibdeep.brotherhood.database;

import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.List;

import cheema.hardeep.sahibdeep.brotherhood.models.Actor;
import cheema.hardeep.sahibdeep.brotherhood.models.Genre;
import cheema.hardeep.sahibdeep.brotherhood.models.MovieDetail;
import cheema.hardeep.sahibdeep.brotherhood.models.UserInfo;

/**
 * This class store user preferences as UserInfo object
 * -- Object cannot be stored directly into shared preferences so this class uses GSON to convert
 * object into string and store it
 * -- Upon getting the user preferences, with help of GSON string is convert back to UserInfo object
 */
public class UserInfoManager {

    private static final String USER_INFO_PREFERENCES = "user-info-preferences";
    private static final String KEY_USER_INFO = "user-info";
    private static final String KEY_FIRST_LAUNCH = "first-launch";

    private SharedPreferences sharedPreferences;
    private Gson gson;

    public UserInfoManager(SharedPreferences sharedPreferences, Gson gson) {
        this.sharedPreferences = sharedPreferences;
        this.gson = gson;
    }

    /**
     * Update first launch flag in shared preferences to not show
     * first launch screens again
     */
    public void saveFirstLaunchCompleted() {
        sharedPreferences.edit().putBoolean(KEY_FIRST_LAUNCH, false).apply();
    }

    /**
     * If user already been through first launch the method return false
     * else it return true (default)
     */
    public boolean isFirstLaunch() {
        return sharedPreferences.getBoolean(KEY_FIRST_LAUNCH, true);
    }


    /**
     * Save user name to shared preferences
     * This method get the UserInfo object from shared perference if it exist then update the name
     * If UserInfo object doesn't not exist in shared preferences then it creates a new object with
     * provided name and save it
     */
    public void saveUserName(String name) {
        UserInfo userInfo = getUserInfo();
        userInfo.setName(name);
        saveUserInfo(userInfo);
    }

    /**
     * Get user name from shared preferences
     * This method get the UserInfo object from shared preference if it exist then return the name
     * from object
     */
    public String getUserName() {
        return getUserInfo().getName();
    }

    /**
     * Save user genres to shared preferences
     * This method get the UserInfo object from shared perference if it exist then update the genres
     * If UserInfo object doesn't not exist in shared preferences then it creates a new object with
     * provided genres and save it
     */
    public void saveUserGenres(List<Genre> genres) {
        UserInfo userInfo = getUserInfo();
        userInfo.setGenres(genres);
        saveUserInfo(userInfo);
    }

    /**
     * Get user genres from shared preferences
     * This method get the UserInfo object from shared preference if it exist then return the genres
     * from object
     */
    public List<Genre> getUserGenres() {
        return getUserInfo().getGenres();
    }

    /**
     * Save user actors to shared preferences
     * This method get the UserInfo object from shared preference if it exist then update the actors
     * If UserInfo object doesn't not exist in shared preferences then it creates a new object with
     * provided actors and save it
     */
    public void saveUserActors(List<Actor> actors) {
        UserInfo userInfo = getUserInfo();
        userInfo.setActors(actors);
        saveUserInfo(userInfo);
    }

    /**
     * Get user actors from shared preferences
     * This method get the UserInfo object from shared preference if it exist then return the actors
     * from object
     */
    public List<Actor> getUserActors() {
        return getUserInfo().getActors();
    }

    /**
     * Add user favorite to shared preferences
     * This method get the UserInfo object from shared preference if it exist then update the favorite
     * If UserInfo object doesn't not exist in shared preferences then it creates a new object with
     * provided favorite and save it
     */
    public void addUserFavorite(MovieDetail movieDetail) {
        if (movieDetail != null) {
            UserInfo userInfo = getUserInfo();
            userInfo.getFavoritesList().add(movieDetail);
            saveUserInfo(userInfo);
        }
    }

    /**
     * Remove user favorite to shared preferences
     * This method get the UserInfo object from shared preference if it exist then remove the favorite
     */
    public void removeUserFavorite(MovieDetail targetMovieDetail) {
        UserInfo userInfo = getUserInfo();
        for (MovieDetail movieDetail : userInfo.getFavoritesList()) {
            if (movieDetail.getId().equals(targetMovieDetail.getId()))
                userInfo.getFavoritesList().remove(movieDetail);
        }
        saveUserInfo(userInfo);
    }

    /**
     * Get user favorites from shared preferences
     * This method get the UserInfo object from shared preference if it exist then return the favorites
     * from object
     */
    public List<MovieDetail> getUserFavorites() {
        return getUserInfo().getFavoritesList();
    }

    /**
     * Get full userInfo from shared preferences if its exits
     */
    public UserInfo getFullUserInfo() {
        return getUserInfo();
    }

    private UserInfo getUserInfo() {
        String userInfoJson = sharedPreferences.getString(KEY_USER_INFO, null);
        return userInfoJson != null ? gson.fromJson(userInfoJson, UserInfo.class) : new UserInfo();
    }

    private void saveUserInfo(UserInfo userInfo) {
        sharedPreferences.edit().putString(KEY_USER_INFO, gson.toJson(userInfo)).apply();
    }

}