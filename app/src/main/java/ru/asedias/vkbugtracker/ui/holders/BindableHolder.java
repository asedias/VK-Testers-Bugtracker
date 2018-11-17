package ru.asedias.vkbugtracker.ui.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by rorom on 20.10.2018.
 */

public class BindableHolder<I> extends RecyclerView.ViewHolder implements BindableHolderInterface<I>, View.OnClickListener {

    I data;

    public BindableHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void bind(I data) {
        this.data = data;
        itemView.setOnClickListener(this);
    }

    @Override
    public void click(View v) {

    }

    @Override
    public void onClick(View v) {
        click(v);
    }
}
