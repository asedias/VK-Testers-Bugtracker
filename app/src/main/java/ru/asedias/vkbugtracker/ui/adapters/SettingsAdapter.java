package ru.asedias.vkbugtracker.ui.adapters;

import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ru.asedias.vkbugtracker.BTApp;
import ru.asedias.vkbugtracker.FragmentStackActivity;
import ru.asedias.vkbugtracker.R;
import ru.asedias.vkbugtracker.ui.CardItemDecorator;
import ru.asedias.vkbugtracker.ui.DividerItemDecoration;
import ru.asedias.vkbugtracker.ui.Fonts;
import ru.asedias.vkbugtracker.ui.ThemeController;
import ru.asedias.vkbugtracker.ui.holders.BindableHolder;

/**
 * Created by Roma on 08.05.2019.
 */

public class SettingsAdapter extends RecyclerView.Adapter<SettingsAdapter.SettingsHolder> implements CardItemDecorator.Provider {


    private List<SettingsItem> data = new ArrayList<>();
    private LayoutInflater mInflater;
    private FragmentStackActivity parentActivity;
    private final static int TYPE_HEADER = 0;
    private final static int TYPE_BASIC = 1;
    private final static int TYPE_SWITCH = 2;

    public SettingsAdapter(List<SettingsItem> data) {
        this.data.clear();
        this.data = data;
    }

    @NonNull
    @Override
    public SettingsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(this.mInflater == null) {
            this.parentActivity = (FragmentStackActivity) parent.getContext();
            this.mInflater = ((Activity) parent.getContext()).getLayoutInflater();
        }
        return new SettingsHolder();
    }

    @Override
    public void onBindViewHolder(@NonNull SettingsHolder holder, int position) {
        holder.bind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public int getItemViewType(int position) {
        return data.get(position).type;
    }

    @Override
    public int getBlockType(int var1) {
        int type = CardItemDecorator.MIDDLE;
        if(var1 < data.size() - 1 && getItemViewType(var1 + 1) == TYPE_HEADER) type = CardItemDecorator.BOTTOM;
        if(getItemViewType(var1) == TYPE_HEADER) type =  CardItemDecorator.TOP;
        Log.i("CARD_TYPE", var1 + "(" + getItemViewType(var1) + "): " + type);
        return type;
    }

    public class SettingsHolder extends BindableHolder<SettingsItem> {

        public View textWrap;
        public TextView title;
        public TextView subtitle;
        public ImageView icon;
        public SwitchCompat switchCompat;

        public SettingsHolder() {
            super(mInflater.inflate(R.layout.settings_item, null, false));
            this.textWrap = $(R.id.text);
            this.title = (TextView) $(R.id.title);
            this.subtitle = (TextView) $(R.id.subtitle);
            this.icon = (ImageView) $(R.id.icon);
            this.switchCompat = (SwitchCompat) $(R.id.switchCompat);
        }

        @Override
        public void bind(SettingsItem data) {
            super.bind(data);
            this.title.setTextColor(ThemeController.getTextColor());
            if(data.type == TYPE_HEADER) {
                this.title.setVisibility(View.GONE);
                this.subtitle.setVisibility(View.VISIBLE);
                this.subtitle.setText(data.subtitle);
                this.switchCompat.setVisibility(View.GONE);
                this.icon.setVisibility(View.GONE);
                this.itemView.setClickable(false);
                TextViewCompat.setTextAppearance(this.subtitle, android.support.design.R.style.TextAppearance_AppCompat_Body2);
                this.subtitle.setTextColor(ThemeController.getTextColor());
                this.subtitle.setTypeface(Fonts.Medium);
            } else {
                TextViewCompat.setTextAppearance(this.subtitle, android.support.design.R.style.TextAppearance_AppCompat_Body1);
                this.subtitle.setTextColor(ThemeController.getTextSecondaryColor());
                this.subtitle.setTypeface(this.title.getTypeface());
                this.itemView.setClickable(true);
                if(data.icon != null) {
                    this.icon.setImageDrawable(data.icon);
                    this.icon.setVisibility(View.VISIBLE);
                } else {
                    this.icon.setVisibility(View.GONE);
                }
                if(data.title != null && data.title.length() > 0) {
                    this.title.setText(data.title);
                    this.title.setVisibility(View.VISIBLE);
                } else {
                    this.title.setVisibility(View.GONE);
                }
                if(data.subtitle != null && data.subtitle.length() > 0) {
                    this.subtitle.setText(data.title);
                    this.subtitle.setVisibility(View.VISIBLE);
                } else {
                    this.subtitle.setVisibility(View.GONE);
                }
                if(data.changeListener != null) {
                    this.switchCompat.setOnCheckedChangeListener(null);
                    this.switchCompat.setChecked(data.isChecked);
                    this.switchCompat.setOnClickListener(v -> data.isChecked = this.switchCompat.isChecked());
                    this.switchCompat.setOnCheckedChangeListener(data.changeListener);
                    this.switchCompat.setVisibility(View.VISIBLE);
                } else {
                    this.switchCompat.setVisibility(View.GONE);
                }
            }
        }

        @Override
        public void onClick(View v) {
            super.onClick(v);
            if(data.action != null) data.action.run();
        }
    }

    public static class SettingsItem {
        public String title;
        public String subtitle;
        public Drawable icon;
        public Runnable action;
        public int type;
        public Switch.OnCheckedChangeListener changeListener;
        public boolean isChecked;

        public SettingsItem(String subtitle) {
            this.subtitle = subtitle;
            this.type = TYPE_HEADER;
        }

        public SettingsItem(@StringRes int id) {
            this.subtitle = BTApp.String(id);
            this.type = TYPE_HEADER;
        }

        public SettingsItem(String title, Runnable action) {
            this.title = title;
            this.action = action;
            this.type = TYPE_BASIC;
        }

        public SettingsItem(@StringRes int title, Runnable action) {
            this.title = BTApp.String(title);
            this.action = action;
            this.type = TYPE_BASIC;
        }

        public SettingsItem(String title, String subtitle, Runnable action) {
            this.title = title;
            this.subtitle = subtitle;
            this.action = action;
            this.type = TYPE_BASIC;
        }

        public SettingsItem(@StringRes int title, @StringRes int subtitle, Runnable action) {
            this.title = BTApp.String(title);
            this.subtitle = BTApp.String(subtitle);
            this.action = action;
            this.type = TYPE_BASIC;
        }

        public SettingsItem(String title, Drawable icon, Runnable action) {
            this.title = title;
            this.icon = icon;
            this.action = action;
            this.type = TYPE_BASIC;
        }

        public SettingsItem(@StringRes int title, Drawable icon, Runnable action) {
            this.title = BTApp.String(title);
            this.icon = icon;
            this.action = action;
            this.type = TYPE_BASIC;
        }

        public SettingsItem(String title, String subtitle, Drawable icon, Runnable action) {
            this.title = title;
            this.subtitle = subtitle;
            this.icon = icon;
            this.action = action;
            this.type = TYPE_BASIC;
        }

        public SettingsItem(@StringRes int title, @StringRes int subtitle, Drawable icon, Runnable action) {
            this.title = BTApp.String(title);
            this.subtitle = BTApp.String(subtitle);
            this.icon = icon;
            this.action = action;
            this.type = TYPE_BASIC;
        }

        public SettingsItem(String title, boolean isChecked, Switch.OnCheckedChangeListener changeListener) {
            this.title = title;
            this.isChecked = isChecked;
            this.changeListener = changeListener;
            this.type = TYPE_SWITCH;
        }

        public SettingsItem(@StringRes int title, boolean isChecked, Switch.OnCheckedChangeListener changeListener) {
            this.title = BTApp.String(title);
            this.isChecked = isChecked;
            this.changeListener = changeListener;
            this.type = TYPE_SWITCH;
        }

        public SettingsItem(String title, String subtitle, boolean isChecked, Switch.OnCheckedChangeListener changeListener) {
            this.title = title;
            this.subtitle = subtitle;
            this.isChecked = isChecked;
            this.changeListener = changeListener;
            this.type = TYPE_SWITCH;
        }

        public SettingsItem(@StringRes int title, @StringRes int subtitle, boolean isChecked, Switch.OnCheckedChangeListener changeListener) {
            this.title = BTApp.String(title);
            this.subtitle = BTApp.String(subtitle);
            this.isChecked = isChecked;
            this.changeListener = changeListener;
            this.type = TYPE_SWITCH;
        }

        public SettingsItem(String title, Drawable icon, boolean isChecked, Switch.OnCheckedChangeListener changeListener) {
            this.title = title;
            this.icon = icon;
            this.isChecked = isChecked;
            this.changeListener = changeListener;
            this.type = TYPE_SWITCH;
        }

        public SettingsItem(@StringRes int title, Drawable icon, boolean isChecked, Switch.OnCheckedChangeListener changeListener) {
            this.title = BTApp.String(title);
            this.icon = icon;
            this.isChecked = isChecked;
            this.changeListener = changeListener;
            this.type = TYPE_SWITCH;
        }

        public SettingsItem(String title, String subtitle, Drawable icon, boolean isChecked, Switch.OnCheckedChangeListener changeListener) {
            this.title = title;
            this.subtitle = subtitle;
            this.icon = icon;
            this.isChecked = isChecked;
            this.changeListener = changeListener;
            this.type = TYPE_SWITCH;
        }

        public SettingsItem(@StringRes int title, @StringRes int subtitle, Drawable icon, boolean isChecked, Switch.OnCheckedChangeListener changeListener) {
            this.title = BTApp.String(title);
            this.subtitle = BTApp.String(subtitle);
            this.icon = icon;
            this.isChecked = isChecked;
            this.changeListener = changeListener;
            this.type = TYPE_SWITCH;
        }
    }
}
