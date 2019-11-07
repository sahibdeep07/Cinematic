package cheema.hardeep.sahibdeep.brotherhood.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cheema.hardeep.sahibdeep.brotherhood.R;

public class RecommendedFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recommended, container, false);
        view.findViewById(R.id.textView);
        return view;
    }

}
