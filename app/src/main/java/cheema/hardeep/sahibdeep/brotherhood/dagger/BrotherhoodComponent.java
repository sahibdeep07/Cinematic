package cheema.hardeep.sahibdeep.brotherhood.dagger;

import cheema.hardeep.sahibdeep.brotherhood.activities.ActorActivity;
import cheema.hardeep.sahibdeep.brotherhood.activities.DetailActivity;
import cheema.hardeep.sahibdeep.brotherhood.activities.GenreActivity;
import cheema.hardeep.sahibdeep.brotherhood.fragments.NowPlayingFragment;
import cheema.hardeep.sahibdeep.brotherhood.fragments.UpcomingFragment;
import dagger.Component;

@Component(modules = BrotherhoodModule.class)
public interface BrotherhoodComponent {

    void inject(GenreActivity genreActivity);

    void inject(ActorActivity actorActivity);

    void inject(UpcomingFragment upcomingFragment);

    void inject(NowPlayingFragment nowPlayingFragment);

    void inject(DetailActivity detailActivity);
}
