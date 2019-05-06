package ru.asedias.vkbugtracker.fragments;

import android.app.Fragment;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;

import java.io.Serializable;

import ru.asedias.vkbugtracker.FragmentStackActivity;

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
    protected int cardOffset = 0;

    public BTFragment setCategory(int catID) {
        this.catID = catID;
        return this;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.parent = (FragmentStackActivity) getActivity();
        this.top = getArguments().getBoolean("top", false);
        this.root = getArguments().getBoolean("root", true);
        this.setRetainInstance(true);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public void updateUIState() {
        this.parent.getToolbar().setLogo(logo);
        if(catID != 0 && this.parent.getBottomNavView().getSelectedItemId() != catID) this.parent.getBottomNavView().setSelectedItemId(catID);
        this.parent.getAppbar().setExpanded(true, true);
        this.parent.getToolbar().setSubtitle(subtitle);
        if(setTitleNeeded || !top) {
            this.parent.toolbarToFullWidth(cardOffset);
            this.parent.getToolbar().setTitle(title);
        } else {
            this.parent.toolbarToCard(cardOffset);
            this.parent.getToolbar().setTitle("");
        }
        if(!top) {
            this.parent.showNavBack();
        } else {
            this.parent.showNavLogo(true);
        }
        getView().setBackgroundColor(currentBackground);
    }

    @Override
    public void onStart() {
        super.onStart();
        if(root) updateUIState();
    }
}
