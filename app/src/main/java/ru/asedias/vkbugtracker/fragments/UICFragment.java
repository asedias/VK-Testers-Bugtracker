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
    protected boolean top = false;
    protected int catID = 0;

    public UICFragment setCategory(int catID) {
        this.catID = catID;
        return this;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UIC = ((MainActivity)getActivity()).getController();
        top = getArguments().getBoolean("top");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public void updateUIState() {
        if(catID != 0) UIC.getBottomNavView().setSelectedItemId(catID);
        UIC.getAppbar().setExpanded(true, true);
        if(setTitleNeeded || !top) {
            UIC.HideSearch();
            UIC.getToolbar().setTitle(title);
        } else {
            UIC.ShowSearch();
            UIC.getToolbar().setTitle("");
        }
        if(!top) {
            UIC.ShowNavBack();
        } else {
            UIC.ShowNavLogo();
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
