package ru.asedias.vkbugtracker.fragments;

import android.app.Fragment;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewCompat;

import java.io.Serializable;

import ru.asedias.vkbugtracker.BTApp;
import ru.asedias.vkbugtracker.FragmentStackActivity;
import ru.asedias.vkbugtracker.R;

import static ru.asedias.vkbugtracker.ThemeManager.currentBackground;

/**
 * Created by rorom on 31.10.2018.
 */

public class BTFragment extends Fragment implements Serializable {

    protected FragmentStackActivity parent;
    protected String title;
    protected Drawable logo;
    protected boolean setTitleNeeded = true;
    protected String subtitle;
    protected boolean top = false;
    protected boolean root = true;
    protected int catID = 0;

    public BTFragment setCategory(int catID) {
        this.catID = catID;
        return this;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parent = (FragmentStackActivity) getActivity();
        top = getArguments().getBoolean("top", false);
        root = getArguments().getBoolean("root", true);
        this.setRetainInstance(true);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public void updateUIState() {
        parent.getToolbar().setLogo(logo);
        if(catID != 0 && parent.getBottomNavView().getSelectedItemId() != catID) parent.getBottomNavView().setSelectedItemId(catID);
        parent.getAppbar().setExpanded(true, true);
        parent.getToolbar().setSubtitle(subtitle);
        if(setTitleNeeded || !top) {
            parent.hideSearch();
            parent.getToolbar().setTitle(title);
        } else {
            parent.showSearch();
            parent.getToolbar().setTitle("");
        }
        if(!top) {
            parent.showNavBack();
        } else {
            parent.showNavLogo();
        }
        getView().setBackgroundColor(currentBackground);
    }

    @Override
    public void onStart() {
        super.onStart();
        if(root) updateUIState();
    }
}
