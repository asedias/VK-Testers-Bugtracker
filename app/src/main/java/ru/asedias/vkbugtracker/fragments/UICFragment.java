package ru.asedias.vkbugtracker.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;

import ru.asedias.vkbugtracker.MainActivity;
import ru.asedias.vkbugtracker.R;
import ru.asedias.vkbugtracker.ui.UIController;

/**
 * Created by rorom on 31.10.2018.
 */

public class UICFragment extends Fragment {

    protected UIController UIC;
    protected String title;
    protected boolean setTitleNeeded = true;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UIC = ((MainActivity)getActivity()).getController();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(UIC.getFragmentManager().getBackStackEntryCount() == 0) {
            getActivity().finish();
        }
    }

    public void updateUIState() {
        UIC.getAppbar().setExpanded(true, true);
        if(setTitleNeeded) {
            UIC.HideSearch();
            UIC.getToolbar().setTitle(title);
        } else {
            UIC.ShowSearch();
            UIC.getToolbar().setTitle("");
        }
        if(UIC.getFragmentManager().getBackStackEntryCount() > 1) {
            UIC.getToolbar().setNavigationIcon(R.drawable.ic_ab_back_arrow_dark);
        } else {
            UIC.getToolbar().setNavigationIcon(R.drawable.ic_logo);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUIState();
    }

    @Override
    public void onStart() {
        super.onStart();
        updateUIState();
    }
}
