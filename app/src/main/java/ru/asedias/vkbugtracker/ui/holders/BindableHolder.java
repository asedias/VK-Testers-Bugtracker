package ru.asedias.vkbugtracker.ui.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by rorom on 20.10.2018.
 */

public class BindableHolder<I> extends RecyclerView.ViewHolder implements BindableHolderInterface<I> {
    public BindableHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void bind(I data) {

    }
}
