package cheema.hardeep.sahibdeep.brotherhood.models;

import cheema.hardeep.sahibdeep.brotherhood.R;

public enum GenreEnum {
    ACTION("action", R.drawable.icon_action),
    ADVENTURE("adventure", R.drawable.icon_adventure),
    ANIMATION("animation", R.drawable.icon_animation),
    COMEDY("comedy",R.drawable.icon_comedy),
    CRIME("crime", R.drawable.icon_crime),
    DOCUMENTARY("documentary", R.drawable.icon_documentary),
    DRAMA("drama", R.drawable.icon_drama),
    FAMILY("family", R.drawable.icon_family),
    FANTASY("fantasy", R.drawable.icon_fantasy),
    HISTORY("history", R.drawable.icon_history),
    HORROR("horror", R.drawable.icon_horror),
    MUSIC("music", R.drawable.icon_music),
    MYSTERY("mystery", R.drawable.icon_mystery),
    ROMANCE("romance", R.drawable.icon_love),
    SCIENCE_FICTION("science fiction", R.drawable.icon_scifi),
    TV_MOVIE("tv movie", R.drawable.icon_tv),
    THRILLER("thriller", R.drawable.icon_thriller),
    WAR("war", R.drawable.icon_war),
    WESTERN("western", R.drawable.icon_western);


    String name;
    int icon;

    GenreEnum(String name, int icon) {
        this.name = name;
        this.icon = icon;
    }

    public static GenreEnum getEnum(String name) {
        for (GenreEnum genreEnum : GenreEnum.values()) {
            if(genreEnum.name.equalsIgnoreCase(name)) return genreEnum;
        }
        return GenreEnum.ACTION;
    }
}
