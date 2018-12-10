package ru.asedias.vkbugtracker.fragments;

import ru.asedias.vkbugtracker.BugTrackerApp;
import ru.asedias.vkbugtracker.R;

/**
 * Created by rorom on 27.11.2018.
 */

public class ProductsFragment extends TabbedFragment {

    public static ProductsFragment newInstance() {
        ProductsFragment fragment = new ProductsFragment();
        fragment.withFragments(ProductListFragment.newInstance(false), ProductListFragment.newInstance(true));
        fragment.withTitles(BugTrackerApp.String(R.string.my_products), BugTrackerApp.String(R.string.all_products));
        return fragment;
    }

    public ProductsFragment() {
        this.title = BugTrackerApp.String(R.string.prefs_products);
        this.setTitleNeeded = false;
    }
}
