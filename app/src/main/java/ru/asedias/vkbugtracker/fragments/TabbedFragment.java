package ru.asedias.vkbugtracker.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ru.asedias.vkbugtracker.R;

/**
 * Created by rorom on 30.10.2018.
 */

public class TabbedFragment extends BTFragment {

    private ViewPager pager;
    private TabAdapter adapter;
    public List<Fragment> fragmentList = new ArrayList<>();
    public List<String> titlesList = new ArrayList<>();

    public TabbedFragment withFragments(Fragment... fragments) {
        fragmentList.addAll(Arrays.asList(fragments));
        return this;
    }

    public TabbedFragment withTitles(String... titles) {
        titlesList.addAll(Arrays.asList(titles));
        return this;
    }

    public Fragment getCurrentFragment() {
        return fragmentList.get(this.pager.getCurrentItem());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if(this.pager == null) {
            this.pager = new ViewPager(getActivity());
            this.adapter = new TabAdapter(getActivity().getFragmentManager());
            this.pager.setAdapter(adapter);
        }
        this.pager.setId(R.id.ALT);
        return pager;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        parent.getTabLayout().setupWithViewPager(this.pager);
        parent.showTabBar();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        parent.hideTabBar();
    }

    public class TabAdapter extends FragmentPagerAdapter {

        public TabAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return fragmentList.get(position).toString().hashCode();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titlesList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }
    }
}
