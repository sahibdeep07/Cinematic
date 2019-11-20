package cheema.hardeep.sahibdeep.brotherhood.models;

/**
 * Defining CallerType enum to handle different type of logic inside MovieAdapter
 * based on the Caller {Recommended, Now_Playing or Upcoming}
 */
public enum CallerType {
    RECOMMENDED,
    NOW_PLAYING,
    UPCOMING;

    public boolean isRecommended() {
        return this == RECOMMENDED;
    }

    public boolean isNowPlaying() {
        return this == NOW_PLAYING;
    }

    public boolean isUpcoming() {
        return this == UPCOMING;
    }
}
