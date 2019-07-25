package cheema.hardeep.sahibdeep.brotherhood.models;

import cheema.hardeep.sahibdeep.brotherhood.R;

public enum GenreEnum {
    ACTION("action", R.drawable.icon_action),
    ADVENTURE("adventure", R.drawable.icon_adventure);

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
