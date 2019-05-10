package ru.asedias.vkbugtracker.ui.holders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import ru.asedias.vkbugtracker.BTApp;
import ru.asedias.vkbugtracker.FragmentStackActivity;
import ru.asedias.vkbugtracker.R;
import ru.asedias.vkbugtracker.api.webmethods.models.ProductList;
import ru.asedias.vkbugtracker.api.webmethods.models.ReportList;
import ru.asedias.vkbugtracker.data.ProductsData;
import ru.asedias.vkbugtracker.fragments.ViewProductFragment;
import ru.asedias.vkbugtracker.fragments.ViewReportFragment;
import ru.asedias.vkbugtracker.ui.CropCircleTransformation;
import ru.asedias.vkbugtracker.ui.FlowLayout;
import ru.asedias.vkbugtracker.ui.Fonts;
import ru.asedias.vkbugtracker.ui.ThemeController;

/**
 * Created by rorom on 20.10.2018.
 */

public class ReportItemHolder extends  BindableHolder<ReportList.ReportItem> {

    public TextView mTitle;
    public TextView mTime;
    public ImageView mAuthor;
    public ImageView mProduct;
    public FlowLayout mTagsLayout;
    private LayoutInflater mInflater;
    private FlowLayout.LayoutParams mTagLP = new FlowLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

    public ReportItemHolder(View itemView, LayoutInflater inflater) {
        super(itemView);
        this.mInflater = inflater;
        this.mTitle = itemView.findViewById(R.id.title);
        this.mTime = itemView.findViewById(R.id.time);
        this.mTagsLayout = itemView.findViewById(R.id.tags_layout);
        this.mAuthor = itemView.findViewById(R.id.author);
        this.mProduct = itemView.findViewById(R.id.product);
        mTagLP.horizontal_spacing = BTApp.dp(6);
        mTagLP.vertical_spacing = BTApp.dp(6);
    }

    @Override
    public void bind(ReportList.ReportItem report) {
        super.bind(report);
        this.mTitle.setText(report.title);
        this.mTitle.setTextColor(ThemeController.getTextColor());
        this.mTime.setText(report.details);
        Picasso.with(BTApp.context)
                .load(report.user.getPhoto200())
                .placeholder(BTApp.Drawable(R.drawable.placeholder_user))
                .transform(new CropCircleTransformation())
                .into(this.mAuthor);
        this.mProduct.setOnClickListener(this);
        this.mTagsLayout.removeAllViews();
        float textPadding = BTApp.dp(24);
        float fullwidth = 0;
        float width = BTApp.mMetrics.widthPixels - BTApp.dp(135);
        for(int i = 1; i < report.tags.size(); i++) {
            final TextView temp = (TextView) mInflater.inflate(R.layout.tag_item, null);
            fullwidth += temp.getPaint().measureText(report.tags.get(i).label) + textPadding;
            if(fullwidth > width) break;
            temp.setText(report.tags.get(i).label);
            temp.setTypeface(Fonts.Regular);
            this.mTagsLayout.addView(temp, mTagLP);
        }
    }

    public void showProduct(boolean show) {
        if(show) {
            ProductList.Product product;
            if(data.product_id == 0) {
                product = ProductsData.getProductByName(data.tags.get(0).label);
            } else {
                product = ProductsData.getProduct(data.product_id);
            }
            this.mProduct.setVisibility(View.VISIBLE);
            Picasso.with(BTApp.context)
                    .load(product.photo)
                    .placeholder(BTApp.Drawable(R.drawable.ic_doc_text))
                    .transform(new CropCircleTransformation())
                    .into(this.mProduct);
        } else {
            this.mProduct.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.product) {
            ((FragmentStackActivity)v.getContext()).replaceFragment(ViewProductFragment.newInstance(data.product_id), 0);
            return;
        }
        ((FragmentStackActivity)v.getContext()).replaceFragment(ViewReportFragment.newInstance(data.id), 0);
    }
}
