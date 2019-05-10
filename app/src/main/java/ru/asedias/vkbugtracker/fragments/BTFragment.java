package ru.asedias.vkbugtracker.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.squareup.picasso.Picasso;

import java.io.Serializable;

import ru.asedias.vkbugtracker.BTApp;
import ru.asedias.vkbugtracker.FragmentStackActivity;
import ru.asedias.vkbugtracker.R;
import ru.asedias.vkbugtracker.data.UserData;
import ru.asedias.vkbugtracker.ui.CropCircleTransformation;
import ru.asedias.vkbugtracker.ui.LayoutHelper;
import ru.asedias.vkbugtracker.ui.ThemeController;

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
    protected boolean showBottom = true;
    protected int catID = 0;
    protected int cardOffset = 0;
    protected int scrW;
    protected int bottomOffset = showBottom ? BTApp.dp(56) : 0;

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

    public void onAttach(Activity var1) {
        super.onAttach(var1);
        this.updateConfiguration();
    }

    public void onConfigurationChanged(Configuration var1) {
        super.onConfigurationChanged(var1);
        this.updateConfiguration();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public void updateUIState() {
        if(!(this instanceof SettingsFragment) && this.parent.getToolbar().getMenu().size() == 0) {
            View icon = this.parent.getLayoutInflater().inflate(R.layout.appkit_toolbar_icon, null, false);
            icon.setLayoutParams(LayoutHelper.params(BTApp.dp(48), BTApp.dp(48)));
            icon.setOnClickListener(v -> {
                this.parent.replaceFragment(new SettingsFragment(), 0);
            });
            this.parent.getToolbar().getMenu().add("").setActionView(icon).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            Picasso.with(BTApp.context)
                    .load(UserData.getPhoto())
                    .transform(new CropCircleTransformation())
                    .placeholder(BTApp.Drawable(R.drawable.placeholder_user))
                    .error(BTApp.Drawable(R.drawable.ic_settings))
                    .into((ImageView) icon);
        }
        if(this instanceof SettingsFragment || this instanceof LoginFragment) this.parent.getToolbar().getMenu().clear();
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
        if(showBottom) {
            this.parent.getBottomNavView().setVisibility(View.VISIBLE);
        } else {
            this.parent.getBottomNavView().setVisibility(View.GONE);
        }
        getView().setBackgroundColor(ThemeController.getBackground());
    }

    @Override
    public void onStart() {
        super.onStart();
        if(root) updateUIState();
    }

    protected void updateConfiguration() {
        this.scrW = this.getResources().getConfiguration().screenWidthDp;
    }
}
