package ru.asedias.vkbugtracker.fragments;

import ru.asedias.vkbugtracker.BTApp;
import ru.asedias.vkbugtracker.R;

/**
 * Created by rorom on 27.11.2018.
 */

public class ProductsFragment extends TabbedFragment {

    public static ProductsFragment newInstance() {
        ProductsFragment fragment = new ProductsFragment();
        fragment.withFragments(ProductListFragment.newInstance(false), ProductListFragment.newInstance(true));
        fragment.withTitles(BTApp.String(R.string.my_products), BTApp.String(R.string.all_products));
        return fragment;
    }

    public ProductsFragment() {
        this.title = BTApp.String(R.string.prefs_products);
        this.setTitleNeeded = true;
        this.cardOffset = 48;
    }
}
