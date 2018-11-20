package ru.asedias.vkbugtracker.ui.holders;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import ru.asedias.vkbugtracker.MainActivity;
import ru.asedias.vkbugtracker.R;
import ru.asedias.vkbugtracker.fragments.LoaderFragment;
import ru.asedias.vkbugtracker.fragments.RecyclerFragment;

/**
 * Created by rorom on 20.11.2018.
 */

public class LoadingHolder extends BindableHolder {

    public ProgressBar progress;
    public View error;
    public TextView text;
    public TextView retry;

    public LoadingHolder(LayoutInflater inflater) {
        super(inflater.inflate(R.layout.appkit_load_more, null, false));
        this.progress = itemView.findViewById(R.id.load_more_progress);
        this.error = itemView.findViewById(R.id.load_more_error);
        this.text = itemView.findViewById(R.id.error_text);
        this.retry = itemView.findViewById(R.id.error_retry);
    }

    public void showError(Throwable t) {
        progress.setVisibility(View.INVISIBLE);
        error.setVisibility(View.VISIBLE);
        text.setText(t.getLocalizedMessage());
    }

    @Override
    public void bind(Object data) {
        progress.setVisibility(View.VISIBLE);
        error.setVisibility(View.INVISIBLE);
        retry.setOnClickListener(v -> {
            RecyclerFragment recyclerFragment = (RecyclerFragment) ((MainActivity)itemView.getContext()).getFragmentManager().findFragmentById(R.id.appkit_content);
            recyclerFragment.loadMore(true);
            progress.setVisibility(View.VISIBLE);
            error.setVisibility(View.INVISIBLE);
        });
    }
}
