package cheema.hardeep.sahibdeep.brotherhood;

import android.app.Application;

import cheema.hardeep.sahibdeep.brotherhood.dagger.BrotherhoodComponent;
import cheema.hardeep.sahibdeep.brotherhood.dagger.BrotherhoodModule;
import cheema.hardeep.sahibdeep.brotherhood.dagger.DaggerBrotherhoodComponent;

public class Brotherhood extends Application {

    private BrotherhoodComponent brotherhoodComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        brotherhoodComponent = DaggerBrotherhoodComponent
                .builder()
                .brotherhoodModule(new BrotherhoodModule())
                .build();
    }

    public BrotherhoodComponent getBrotherhoodComponent() {
        return brotherhoodComponent;
    }
}
