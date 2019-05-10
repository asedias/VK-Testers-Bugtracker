package ru.asedias.vkbugtracker.ui.holders;

import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import ru.asedias.vkbugtracker.BTApp;
import ru.asedias.vkbugtracker.R;
import ru.asedias.vkbugtracker.api.webmethods.models.ProductInfo;
import ru.asedias.vkbugtracker.ui.CropCircleTransformation;
import ru.asedias.vkbugtracker.ui.ThemeController;

/**
 * Created by rorom on 15.12.2018.
 */

public class ProductHeaderHolder extends BindableHolder<ProductInfo> {

    public TextView name;
    public TextView version;
    public ImageView photo;
    public Button btn1;
    public Button btn2;
    public Button btn3;
    public View buttonsWrap;

    public ProductHeaderHolder(LayoutInflater inflater) {
        super(inflater.inflate(R.layout.profile_head_group, null, false));
        this.name = itemView.findViewById(R.id.profile_name);
        this.version = itemView.findViewById(R.id.profile_last_seen);
        this.photo = itemView.findViewById(R.id.profile_photo);
        this.btn1 = itemView.findViewById(R.id.profile_btn1);
        this.btn2 = itemView.findViewById(R.id.profile_btn2);
        this.btn3 = itemView.findViewById(R.id.profile_btn3);
        this.buttonsWrap = itemView.findViewById(R.id.profile_buttons_wrap);
    }

    @Override
    public void bind(ProductInfo data) {
        super.bind(data);
        this.btn1.setVisibility(View.GONE);
        this.btn2.setVisibility(View.GONE);
        this.btn3.setVisibility(View.GONE);
        this.name.setText(data.title);
        this.name.setTextColor(ThemeController.getTextColor());
        this.version.setSingleLine(false);
        this.version.setText(Html.fromHtml(data.description));
        this.version.setClickable(true);
        this.version.setMovementMethod(LinkMovementMethod.getInstance());
        Picasso.with(BTApp.context)
                .load(data.photo)
                .transform(new CropCircleTransformation())
                .into(this.photo);
        for(int i = 0; i < data.actionButtons.size() + data.actionDropDowns.size() + data.actionProds.size(); i++) {
            if(i < data.actionButtons.size()) {
                if(i == 0) {
                    this.btn1.setText(data.actionButtons.get(i).title);
                    this.btn1.setVisibility(View.VISIBLE);
                } else if(i == 1) {
                    this.btn2.setText(data.actionButtons.get(i).title);
                    this.btn2.setVisibility(View.VISIBLE);
                }
            } else if(i < data.actionButtons.size() + data.actionDropDowns.size()){
                if(i == 0) {
                    this.btn2.setText(data.actionDropDowns.get(0).title);
                    this.btn2.setVisibility(View.VISIBLE);
                }
                if(i == 1) {
                    this.btn1.setText(data.actionDropDowns.get(0).title);
                    this.btn1.setVisibility(View.VISIBLE);
                }
            } else {
                if(i == 0) {
                    if(data.actionProds.size() > 0) {
                        String text = data.actionProds.get(0);
                        this.btn2.setVisibility(View.VISIBLE);
                        if(text.equals(",")) {
                            this.btn2.setText(text.substring(0, text.indexOf(",")));
                        } else if(text.length() > 0){
                            this.btn2.setText(text);
                        } else {
                            this.btn2.setVisibility(View.GONE);
                            this.buttonsWrap.setVisibility(View.GONE);
                        }
                    } else {
                        this.buttonsWrap.setVisibility(View.GONE);
                    }
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
    }
}
