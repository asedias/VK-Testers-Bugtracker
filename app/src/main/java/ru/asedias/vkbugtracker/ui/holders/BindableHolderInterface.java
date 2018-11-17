package ru.asedias.vkbugtracker.ui.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by rorom on 20.10.2018.
 */

public interface BindableHolderInterface<I> {
    void bind(I data);
    void click(View v);
}
