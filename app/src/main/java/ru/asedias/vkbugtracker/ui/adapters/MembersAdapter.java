package ru.asedias.vkbugtracker.ui.adapters;

import android.support.annotation.NonNull;
import android.support.v4.widget.TextViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import ru.asedias.vkbugtracker.BTApp;
import ru.asedias.vkbugtracker.FragmentStackActivity;
import ru.asedias.vkbugtracker.R;
import ru.asedias.vkbugtracker.api.webmethods.models.MembersList;
import ru.asedias.vkbugtracker.fragments.ProfileFragment;
import ru.asedias.vkbugtracker.fragments.ReportDetailsFragment;
import ru.asedias.vkbugtracker.ui.CropCircleTransformation;
import ru.asedias.vkbugtracker.ui.ThemeController;
import ru.asedias.vkbugtracker.ui.holders.BindableHolder;

/**
 * Created by Roma on 10.05.2019.
 */

public class MembersAdapter extends DataAdapter<MembersAdapter.MemberHolder, MembersList> {

    public MembersAdapter() {
        this.data = new MembersList();
    }

    @NonNull
    @Override
    public MemberHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        super.onCreateViewHolder(parent, viewType);
        return new MemberHolder();
    }

    @Override
    public void onBindViewHolder(@NonNull MemberHolder holder, int position) {
        holder.bind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data != null ? data.getSize() : 0;
    }

    public class MemberHolder extends BindableHolder<MembersList.Member> {

        public TextView place;
        public ImageView photo;
        public TextView name;
        public TextView rating;
        public TextView count;

        public MemberHolder() {
            super(inflater.inflate(R.layout.members_item, null, false));
            this.place = (TextView) $(R.id.place);
            this.photo = (ImageView) $(R.id.photo);
            this.name = (TextView) $(R.id.name);
            this.rating = (TextView) $(R.id.rating);
            this.count = (TextView) $(R.id.count);
        }

        @Override
        public void bind(MembersList.Member data) {
            super.bind(data);
            this.place.setTextColor(ThemeController.getTextColor());
            this.name.setTextColor(ThemeController.getTextColor());
            this.rating.setTextColor(ThemeController.getTextSecondaryColor());
            this.count.setTextColor(ThemeController.getTextColor());
            if(data.pos > 0) {
                this.place.setText(String.valueOf(data.pos));
                this.place.setVisibility(View.VISIBLE);
            } else {
                this.place.setVisibility(View.INVISIBLE);
            }
            this.name.setText(data.full_name);
            this.count.setText(BTApp.QuantityString(R.plurals.reports, data.counters.get(0), data.counters.get(0)));
            this.rating.setText(String.valueOf(data.counters.get(1)));
            if(data.photo_url != null)
            Picasso.with(BTApp.context)
                    .load(data.photo_url.contains("http") ? data.photo_url : "https://vk.com"+data.photo_url)
                    .transform(new CropCircleTransformation())
                    .placeholder(photo.getDrawable())
                    .into(this.photo);
        }

        @Override
        public void onClick(View v) {
            ((FragmentStackActivity)v.getContext()).replaceFragment(ProfileFragment.newInstance(data.uid), 0);
        }
    }


}
