package ru.asedias.vkbugtracker.ui.drawer;

import android.graphics.LightingColorFilter;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ru.asedias.vkbugtracker.BTApp;
import ru.asedias.vkbugtracker.FragmentStackActivity;
import ru.asedias.vkbugtracker.R;
import ru.asedias.vkbugtracker.data.UserData;
import ru.asedias.vkbugtracker.fragments.ReportListFragment;
import ru.asedias.vkbugtracker.ui.ThemeController;

import static android.support.v7.widget.LinearLayoutManager.VERTICAL;

import android.widget.Toast;

/**
 * Created by rorom on 19.02.2019.
 */

public class DrawerAdapter extends RecyclerView.Adapter {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_BASIC_COLORED = 1;
    private static final int TYPE_SEPARATOR = 2;
    private static final int TYPE_BASIC = 3;
    private static final int TYPE_SECTION = 4;
    private static final int TYPE_DROPDOWN = 5;
    private ArrayList dropdownData = new ArrayList();
    private final RecyclerView list;
    private final DrawerLayout drawer;
    private final LayoutInflater inflater;
    private FragmentStackActivity activity = null;
    private final ArrayList<ItemData> items = new ArrayList<>();
    private Runnable currentTask = null;
    private final DrawerLayout.DrawerListener listener = new DrawerLayout.SimpleDrawerListener() {
        @Override
        public void onDrawerStateChanged(int newState) {
            super.onDrawerStateChanged(newState);
            if(newState == DrawerLayout.STATE_DRAGGING && currentTask != null) {
                currentTask.run();
                currentTask = null;
            }
        }

        @Override
        public void onDrawerSlide(View drawerView, float slideOffset) {
            if(slideOffset <= 0 && currentTask != null) {
                currentTask.run();
                currentTask = null;
            }
            super.onDrawerSlide(drawerView, slideOffset);
        }
    };


    public DrawerAdapter(@NonNull DrawerLayout drawer, @NonNull RecyclerView list, @NonNull LayoutInflater inflater) {
        this.fillItems();
        this.drawer = drawer;
        this.drawer.addDrawerListener(listener);
        this.activity = (FragmentStackActivity) this.drawer.getContext();
        this.list = list;
        this.list.setBackgroundColor(ThemeController.getBackground());
        this.list.setLayoutManager(new LinearLayoutManager(inflater.getContext(), VERTICAL, false));
        this.list.setAdapter(this);
        this.inflater = inflater;
    }

    private void fillItems() {
        this.items.clear();
        this.items.add(new ItemData(BTApp.String(R.string.add_report), BTApp.Drawable(R.drawable.ic_ab_add)));
        this.items.add(new ItemData());
        this.items.add(new ItemData(BTApp.String(R.string.prefs_members), BTApp.Drawable(R.drawable.ic_members)));
        if(UserData.isTester()) {
            this.items.add(new ItemData(BTApp.String(R.string.title_home), BTApp.Drawable(R.drawable.ic_about), null));
            this.items.add(new ItemData(BTApp.String(R.string.prefs_reports), BTApp.Drawable(R.drawable.ic_reports), () -> {
                activity.replaceFragment(ReportListFragment.newInstance(UserData.getUID(), 0, "", -1), R.id.navigation_reports);
            }));
            this.items.add(new ItemData(BTApp.String(R.string.my_bookmarks), BTApp.Drawable(R.drawable.ic_updates), () -> {
                activity.replaceFragment(ReportListFragment.newInstance(true), R.id.navigation_reports);
            }));
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_BASIC: return new ItemHolder(parent);
            case TYPE_BASIC_COLORED: return new ItemHolderColored(parent);
            case TYPE_SEPARATOR: return new SeparatorViewHolder(parent);
            default: return new HeaderHolder(parent);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof ItemHolder) {
            ((ItemHolder)holder).bind(items.get(position-1));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(position <= 2) return position;
        if(position + dropdownData.size() < getItemCount()) {
            if(items.get(position-1).title == null) {
                return TYPE_SEPARATOR;
            } else {
                return TYPE_BASIC;
            }
        }
        if(position + dropdownData.size() == getItemCount()) return TYPE_SECTION;
        return TYPE_DROPDOWN;
    }

    @Override
    public int getItemCount() {
        return items.size() + 1 + (dropdownData.size() > 0 ? dropdownData.size() + 1 : 0);
    }

    private void closeDrawer(@Nullable Runnable task) {
        currentTask = task;
        drawer.closeDrawer(list);
    }

    private void update() {
        fillItems();
        notifyDataSetChanged();
    }

    private class ItemData {
        public String title;
        public Drawable icon;
        public Runnable click;

        public ItemData() { }

        public ItemData(String title, Drawable icon) {
            this.title = title;
            this.icon = icon;
            this.click = () -> {
                Toast.makeText(inflater.getContext(), "Close", Toast.LENGTH_SHORT).show();
            };
        }

        public ItemData(String title, Drawable icon, Runnable click) {
            this.title = title;
            this.icon = icon;
            this.click = click;
        }
    }

    public class DropDownItem {
        public int id;
        public String title;

        public DropDownItem(int id, String title) {
            this.id = id;
            this.title = title;
        }
    }

    private class DropDownData {
        public String hint;
        public String title;
        public List<DropDownItem> items;
        public int currentID = Integer.MIN_VALUE;

        public DropDownData(String hint, String title, DropDownItem... items) {
            this.hint = hint;
            this.title = title;
            this.items = Arrays.asList(items);
        }
    }

    private class HeaderHolder extends RecyclerView.ViewHolder {
        public HeaderHolder(ViewGroup parent) {
            super(inflater.inflate(R.layout.drawer_header, parent, false));
        }
    }

    private class ItemHolder extends RecyclerView.ViewHolder {
        public TextView text;
        public ItemHolder(ViewGroup parent) {
            super(inflater.inflate(R.layout.drawer_item, parent, false));
            this.text = itemView.findViewById(R.id.design_menu_item_text);
        }

        public void bind(ItemData data) {
            this.text.setText(data.title);
            this.text.setTextColor(ThemeController.getTextColor());
            if(data.icon != null) data.icon.setBounds(0, 0, BTApp.dp(24), BTApp.dp(24));
            this.text.setCompoundDrawables(data.icon, null, null, null);
            itemView.setOnClickListener(v -> closeDrawer(data.click));
        }
    }

    private class ItemHolderColored extends ItemHolder {
        public final int color = BTApp.Color(R.color.colorAccent);
        public ItemHolderColored(ViewGroup parent) {
            super(parent);
        }

        @Override
        public void bind(ItemData data) {
            super.bind(data);
            this.text.setTextColor(color);
            data.icon.mutate().setColorFilter(new LightingColorFilter(0, color));
        }
    }

    private class SeparatorViewHolder extends RecyclerView.ViewHolder {

        public SeparatorViewHolder(ViewGroup parent) {
            super(inflater.inflate(R.layout.design_navigation_item_separator, parent, false));
            itemView.setPadding(0, BTApp.dp(8), 0, BTApp.dp(8));
        }

    }

}
