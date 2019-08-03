package ru.asedias.vkbugtracker.ui.holders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import ru.asedias.vkbugtracker.FragmentStackActivity;
import ru.asedias.vkbugtracker.fragments.BTFragment;
import ru.asedias.vkbugtracker.ui.LayoutHelper;

/**
 * Created by rorom on 20.10.2018.
 */

public class BindableHolder<I> extends RecyclerView.ViewHolder implements BindableHolderInterface<I>, View.OnClickListener {

    protected I data;
    public FragmentStackActivity parent;
    public BTFragment currentFragment;

    public BindableHolder(View itemView) {
        super(itemView);
        if(itemView.getContext() instanceof FragmentStackActivity) {
            this.parent = ((FragmentStackActivity)itemView.getContext());
            this.currentFragment = parent.getCurrentFragment();
        }
    }

    @Override
    public void bind(I data) {
        this.data = data;
        itemView.setOnClickListener(this);
        itemView.setLayoutParams(LayoutHelper.fullWidthRecycler());
    }

    @Override
    public void onClick(View v) { }

    public View $(int id) {
        return itemView.findViewById(id);
    }
}
