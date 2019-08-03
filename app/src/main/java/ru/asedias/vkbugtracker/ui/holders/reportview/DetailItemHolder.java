package ru.asedias.vkbugtracker.ui.holders.reportview;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.support.v4.widget.TextViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import ru.asedias.vkbugtracker.BTApp;
import ru.asedias.vkbugtracker.FragmentStackActivity;
import ru.asedias.vkbugtracker.R;
import ru.asedias.vkbugtracker.api.webmethods.models.ProfileActivity;
import ru.asedias.vkbugtracker.api.webmethods.models.Report;
import ru.asedias.vkbugtracker.api.webmethods.models.WebCardUser;
import ru.asedias.vkbugtracker.data.ProductsData;
import ru.asedias.vkbugtracker.data.UserData;
import ru.asedias.vkbugtracker.fragments.ProfileFragment;
import ru.asedias.vkbugtracker.fragments.ReportDetailsFragment;
import ru.asedias.vkbugtracker.fragments.ReportListFragment;
import ru.asedias.vkbugtracker.fragments.ViewProductFragment;
import ru.asedias.vkbugtracker.ui.CropCircleTransformation;
import ru.asedias.vkbugtracker.ui.OverlayCircleTranformation;
import ru.asedias.vkbugtracker.ui.holders.BindableHolder;

/**
 * Created by rorom on 11.11.2018.
 */

public class DetailItemHolder extends BindableHolder<Report.Detail> {

    private ImageView icon;
    private TextView title;
    private TextView description;
    private ImageView photo;
    private int type;
    private List<Report.Detail> detail;
    private FrameLayout photosWrap;
    private WebCardUser webUser;

    public DetailItemHolder(LayoutInflater inflater, int type) {
        super(inflater.inflate(R.layout.report_detail_item, null, false));
        this.type = type;
        this.icon = itemView.findViewById(R.id.icon);
        this.photo = itemView.findViewById(R.id.photo);
        this.title = itemView.findViewById(R.id.text);
        this.description = itemView.findViewById(R.id.description);
        this.photosWrap = itemView.findViewById(R.id.photosWrap);
    }
    
    public DetailItemHolder(LayoutInflater inflater, WebCardUser user) {
        super(inflater.inflate(R.layout.report_detail_item, null, false));
        this.type = 31;
        this.icon = itemView.findViewById(R.id.icon);
        this.photo = itemView.findViewById(R.id.photo);
        this.title = itemView.findViewById(R.id.text);
        this.description = itemView.findViewById(R.id.description);
        this.photosWrap = itemView.findViewById(R.id.photosWrap);
        this.webUser = user;
    }

    public DetailItemHolder(LayoutInflater inflater, List<Report.Detail>detail) {
        super(inflater.inflate(R.layout.report_detail_item, null, false));
        this.detail = detail;
        this.type = Integer.MAX_VALUE;
        this.icon = itemView.findViewById(R.id.icon);
        this.photo = itemView.findViewById(R.id.photo);
        this.title = itemView.findViewById(R.id.text);
        this.description = itemView.findViewById(R.id.description);
        this.photosWrap = itemView.findViewById(R.id.photosWrap);
    }

    public void setWebUser(WebCardUser webUser) {
        this.webUser = webUser;    }

    @Override
    public void bind(Report.Detail data) {
        super.bind(data);
        if(type == 0) {
            ColorStateList color = this.title.getTextColors();
            TextViewCompat.setTextAppearance(this.title, R.style.TextAppearance_AppCompat_Body2);
            this.title.setTextColor(color);
            this.title.setText(data.description);
            this.photo.setVisibility(View.VISIBLE);
            this.icon.setImageDrawable(BTApp.Drawable(R.drawable.ic_product));
            Picasso.with(BTApp.context)
                    .load(ProductsData.getProductByName(data.description).photo)
                    .transform(new CropCircleTransformation())
                    .placeholder(BTApp.Drawable(R.drawable.ic_product))
                    .into(this.photo);
            this.description.setVisibility(View.GONE);
        } else if(type == 1){
            this.title.setText(data.title);
            this.icon.setImageDrawable(BTApp.Drawable(R.drawable.ic_detail));
            this.photo.setVisibility(View.GONE);
            ColorStateList color = this.description.getTextColors();
            TextViewCompat.setTextAppearance(this.description, R.style.TextAppearance_AppCompat_Body2);
            this.description.setTextColor(color);
            this.description.setVisibility(View.VISIBLE);
            this.description.setText(data.description);
        } else if(type == 30) {
            this.title.setText(data.title);
            this.icon.setImageDrawable(BTApp.Drawable(R.drawable.ic_about));
            this.photo.setVisibility(View.GONE);
            this.description.setVisibility(View.GONE);
        } else if(type == 31) {
            ColorStateList color = this.title.getTextColors();
            TextViewCompat.setTextAppearance(this.title, R.style.TextAppearance_AppCompat_Body2);
            this.title.setTextColor(color);
            if(webUser != null) {
                this.title.setText(BTApp.QuantityString(R.plurals.products, webUser.products_count, webUser.products_count));
            } else {
                this.title.setText("");
            }
            this.icon.setImageDrawable(BTApp.Drawable(R.drawable.ic_product));
            this.photo.setVisibility(View.GONE);
            this.description.setVisibility(View.GONE);
            photosWrap.removeAllViews();
            if(webUser != null && webUser.products_img.size() > 0) {
                int size = BTApp.dp(24);
                int margin = BTApp.dp(3);
                for(int i = 0; i < webUser.products_img.size(); i++) {
                    ImageView photo = new ImageView(itemView.getContext());
                    FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(size, size);
                    lp.leftMargin = i*(size - margin);
                    photosWrap.addView(photo, lp);
                    Picasso.with(BTApp.context)
                            .load(webUser.products_img.get(i).contains("http") ? webUser.products_img.get(i) : "https://vk.com" + webUser.products_img.get(i))
                            .transform(new CropCircleTransformation())
                            .transform(new OverlayCircleTranformation(i == webUser.products_img.size() - 1 ? 1F : 0.64F))
                            .into(photo);
                }
            }
        } else if(type == 32) {
            ColorStateList color = this.title.getTextColors();
            TextViewCompat.setTextAppearance(this.title, R.style.TextAppearance_AppCompat_Body2);
            this.title.setTextColor(color);
            this.title.setText(data.title);
            this.icon.setImageDrawable(BTApp.Drawable(R.drawable.ic_reports));
            this.photo.setVisibility(View.GONE);
            this.description.setVisibility(View.GONE);
        } else {
            this.icon.setImageDrawable(BTApp.Drawable(type == 33 ? R.drawable.ic_web : R.drawable.ic_more));
            this.icon.setColorFilter(BTApp.Color(R.color.colorAccent));
            this.photo.setVisibility(View.GONE);
            this.description.setVisibility(View.GONE);
            TextViewCompat.setTextAppearance(this.title, R.style.TextAppearance_AppCompat_Body2);
            this.title.setTextColor(BTApp.Color(R.color.colorAccent));
            this.title.setText(BTApp.String(type == 33 ? R.string.open_in_browser : R.string.show_more_info));
        }
    }

    @Override
    public void onClick(View v) {
        if(type == 0) {
            parent.replaceFragment(ViewProductFragment.newInstance(ProductsData.getProductByName(data.description).id), 0);
        }
        if(type == Integer.MAX_VALUE) {
            parent.replaceFragment(new ReportDetailsFragment().setDetails(detail), 0);
        }
        if(type == 33) {
            int uid = ((ProfileFragment)currentFragment).uid;
            Intent message = new Intent(Intent.ACTION_VIEW);
            message.setData(Uri.parse("https://vk.com/bugs?act=reporter&id=" + uid));
            BTApp.context.startActivity(message);
        }
        if(type == 32) {
            int uid = ((ProfileFragment)currentFragment).uid;
            parent.replaceFragment(ReportListFragment.newInstance(uid, 0, "", -1), 0);
        }
    }
}
