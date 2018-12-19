package ru.asedias.vkbugtracker.ui.holders;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import ru.asedias.vkbugtracker.BugTrackerApp;
import ru.asedias.vkbugtracker.MainActivity;
import ru.asedias.vkbugtracker.R;
import ru.asedias.vkbugtracker.api.webmethods.models.ProductList;
import ru.asedias.vkbugtracker.fragments.ViewProductFragment;
import ru.asedias.vkbugtracker.ui.CropCircleTransformation;
import ru.asedias.vkbugtracker.ui.UIController;

/**
 * Created by rorom on 17.11.2018.
 */

public class ProductHolder extends BindableHolder<ProductList.Product> {

    private ImageView photo;
    private TextView title;
    private TextView subtitle;
    private TextView version;

    public ProductHolder(LayoutInflater inflater) {
        super(inflater.inflate(R.layout.product_item, null, false));
        this.photo = itemView.findViewById(R.id.photo);
        this.title = itemView.findViewById(R.id.title);
        this.subtitle = itemView.findViewById(R.id.subtitle);
        this.version = itemView.findViewById(R.id.version);
    }

    @Override
    public void bind(ProductList.Product data) {
        super.bind(data);
        Picasso.with(BugTrackerApp.context)
                .load(data.photo)
                .transform(new CropCircleTransformation())
                .placeholder(BugTrackerApp.Drawable(R.drawable.ic_detail))
                .into(this.photo);
        setText(this.title, data.title);
		if(data.subtitles.size() > 0) setText(this.subtitle, data.subtitles.get(0));
		else this.subtitle.setVisibility(View.GONE);
        if(data.subtitles.size() > 1) setText(this.version, data.subtitles.get(1));
        else this.version.setVisibility(View.GONE);
    }

    void setText(TextView textView, String data) {
        if(data != null && data.length() > 0) {
            textView.setVisibility(View.VISIBLE);
            textView.setText(data);
        } else {
            textView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        UIController uic = ((MainActivity)v.getContext()).getController();
        uic.ReplaceFragment(ViewProductFragment.newInstance(data.id), 0);
    }
}
