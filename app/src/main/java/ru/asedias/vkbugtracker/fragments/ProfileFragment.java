package ru.asedias.vkbugtracker.fragments;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.asedias.vkbugtracker.BTApp;
import ru.asedias.vkbugtracker.R;
import ru.asedias.vkbugtracker.TimeUtils;
import ru.asedias.vkbugtracker.api.WebRequest;
import ru.asedias.vkbugtracker.api.apimethods.GetUserProfile;
import ru.asedias.vkbugtracker.api.apimethods.models.UserProfile;
import ru.asedias.vkbugtracker.api.webmethods.GetUserCard;
import ru.asedias.vkbugtracker.api.webmethods.models.ProfileActivity;
import ru.asedias.vkbugtracker.api.webmethods.models.Report;
import ru.asedias.vkbugtracker.api.webmethods.models.WebCardUser;
import ru.asedias.vkbugtracker.data.UserData;
import ru.asedias.vkbugtracker.ui.CardItemDecorator;
import ru.asedias.vkbugtracker.ui.CropCircleTransformation;
import ru.asedias.vkbugtracker.ui.ProfileActivityView;
import ru.asedias.vkbugtracker.ui.ThemeController;
import ru.asedias.vkbugtracker.ui.adapters.DataAdapter;
import ru.asedias.vkbugtracker.ui.holders.BindableHolder;
import ru.asedias.vkbugtracker.ui.holders.reportview.DetailItemHolder;

/**
 * Created by Roma on 10.05.2019.
 */

public class ProfileFragment extends CardRecyclerFragment<ProfileFragment.ProfileAdapter> implements FitSystemWindowsFragment {

    public int uid;
    private int layoutType = R.layout.profile_head;

    public static ProfileFragment newInstance(int uid) {
        ProfileFragment fr = new ProfileFragment();
        Bundle args = new Bundle();
        args.putInt("uid", uid);
        fr.setArguments(args);
        return fr;
    }

    public ProfileFragment() {
        this.mAdapter = new ProfileAdapter();
        this.title = "";
        this.setTitleNeeded = true;
        this.drawToolbarBG = false;
    }

    @Override
    protected View OnCreateContentView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        updateLayoutType(this.getResources().getConfiguration());
        return super.OnCreateContentView(inflater, container, savedInstanceState);
    }

    @Override
    public WebRequest getRequest() {
        if(getArguments() != null) {
            this.uid = getArguments().getInt("uid", 86185582);
        }
        return new GetUserProfile(this, this.uid > 0 ? this.uid : UserData.getUID(), this::loadDataFromWeb);
    }

    private UserProfile loadDataFromWeb(UserProfile data) {
        new GetUserCard(this.uid > 0 ? this.uid : UserData.getUID(), new Callback<WebCardUser>() {
            @Override
            public void onResponse(Call<WebCardUser> call, Response<WebCardUser> response) {
                data.get(0).webInfo = response.body();
                new Thread(() -> {
                    try {
                        String script = data.get(0).webInfo.scripts.get(data.get(0).webInfo.scripts.size()-1);
                        String activityJson = Pattern.compile(".+BugtrackerComponents.ReporterPage.render\\((\\{.+\\})\\);" , Pattern.DOTALL).matcher(script).replaceAll("$1");
                        JSONArray activity = new JSONObject(activityJson).getJSONArray("activity");
                        for(int i = 0; i < activity.length(); i++) {
                            if(activity.get(i) instanceof JSONObject)
                                data.get(0).webInfo.activities.add(new ProfileActivity(activity.getJSONObject(i)));
                        }
                        parent.getToolbar().post(() -> getAdapter().notifyDataSetChanged());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }).start();
            }

            @Override
            public void onFailure(Call<WebCardUser> call, Throwable t) {
                showError(t);
            }
        }).execute();
        return data;
    }

    @Override
    public void onConfigurationChanged(Configuration var1) {
        super.onConfigurationChanged(var1);
        updateLayoutType(var1);
    }

    private void updateLayoutType(Configuration var1) {
        if(var1.screenWidthDp > 640) {
            this.drawToolbarBG = true;
            this.layoutType = R.layout.profile_head_group_wide;
        } else if(var1.screenWidthDp > 480) {
            this.drawToolbarBG = true;
            this.layoutType = R.layout.profile_head_group;
        } else {
            this.drawToolbarBG = false;
            this.layoutType = R.layout.profile_head;
        }
        if(this.getAdapter() != null) this.getAdapter().notifyItemChanged(0);
        if(Build.VERSION.SDK_INT < 20) {
            this.mContent.requestFitSystemWindows();
        } else {
            this.mContent.requestApplyInsets();
        }

    }

    @Override
    public boolean fitSystemWindows(Rect var1) {
        ViewGroup.MarginLayoutParams toolbarParams = (ViewGroup.MarginLayoutParams)parent.getToolbar().getLayoutParams();
        if(this.layoutType != R.layout.profile_head) {
            this.enableOverlay = false;
            this.drawToolbarBG = true;
            toolbarParams.topMargin = 0;
            this.cardOffset = 0;
            this.parent.toolbarToFullWidth(cardOffset);
            this.mList.setPadding(0, BTApp.dp(56) + cardOffset + topOffset, 0, bottomOffset);
            this.parent.getAppbarCard().setAlpha(drawToolbarBG ? 1.0F : 0);
            return false;
        } else {
            this.enableOverlay = true;
            this.drawToolbarBG = false;
            toolbarParams.topMargin = var1.top;
            this.cardOffset = var1.top;
            this.parent.toolbarToFullWidth(cardOffset);
            this.mList.setPadding(0, BTApp.dp(-2), 0, bottomOffset);
            parent.getToolbar().setLayoutParams(toolbarParams);
            this.parent.getAppbarCard().setAlpha(drawToolbarBG ? 1.0F : 0);
            return true;
        }
    }

    @Override
    protected CardItemDecorator onCreateCardDecorator() {
        CardItemDecorator decorator = super.onCreateCardDecorator();
        decorator.setPadding(BTApp.dp(2), BTApp.dp(3), 0, 0);
        return decorator;
    }

    @Override
    public void onResume() {
        super.onResume();
        Drawable icon = BTApp.Drawable(R.drawable.ic_ab_back_arrow_dark);
        if(layoutType == R.layout.profile_head) DrawableCompat.setTint(icon, Color.WHITE);
        parent.getToolbar().setNavigationIcon(icon);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ViewGroup.MarginLayoutParams toolbarParams = (ViewGroup.MarginLayoutParams)parent.getToolbar().getLayoutParams();
        toolbarParams.topMargin = 0;
    }

    public class ProfileAdapter extends DataAdapter<BindableHolder, UserProfile> implements CardItemDecorator.Provider {

        //public GetUserExternalInfo.Result tracker_info = new GetUserExternalInfo.Result();
        private final int TYPE_HEADER = 0;
        private final int TYPE_DETAIL_STATUS = 30;
        private final int TYPE_DETAIL_PRODUCTS = 31;
        private final int TYPE_DETAIL_REPORTS = 32;
        private final int TYPE_DETAIL_BROWSER = 33;
        private final int TYPE_ACTIVITY = 2;

        @NonNull
        @Override
        public BindableHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            super.onCreateViewHolder(parent, viewType);
            if(viewType == TYPE_ACTIVITY) return new ProfileActivityHolder();
            if(viewType >= 30 && viewType != 31) return new DetailItemHolder(inflater, viewType);
            if(viewType == 31) return new DetailItemHolder(inflater, data.get(0).webInfo);
            return new ProfileHeaderHolder();
        }

        @Override
        public void onBindViewHolder(@NonNull BindableHolder holder, int position) {
            if(holder instanceof DetailItemHolder) {
                Report.Detail detail = new Report.Detail();
                if(data.get(0).webInfo == null) {
                    switch (position) {
                        case 1:
                            detail.title = "...";
                            break;
                        case 3:
                            detail.title = "";
                            break;
                        case 2:
                        case 4:
                            detail = null;
                            break;
                    }
                } else {
                    String[] info = data.get(0).webInfo.information.split(", ");
                    switch (position) {
                        case 1:
                            detail.title = info[0];
                            break;
                        case 3:
                            detail.title = BTApp.String(R.string.all_reports);
                            if(info.length > 1) detail.title = info[1];
                            break;
                        case 2:
                            ((DetailItemHolder)holder).setWebUser(data.get(0).webInfo);
                        case 4:
                            detail = null;
                            break;
                    }
                }
                ((DetailItemHolder)holder).bind(detail);
            } else {
                holder.bind(data.get(0));
            }
        }

        @Override
        public int getItemViewType(int position) {
            switch (position) {
                case 0: return TYPE_HEADER;
                case 1: return TYPE_DETAIL_STATUS;
                case 2: return TYPE_DETAIL_PRODUCTS;
                case 3: return TYPE_DETAIL_REPORTS;
                case 4: return TYPE_DETAIL_BROWSER;
                //case 2: return tracker_info.isTester ? TYPE_DETAIL : TYPE_ACTIVITY;
                //case 3: return TYPE_DETAIL;
                //case 4: return TYPE_ACTIVITY;
            }
            return TYPE_ACTIVITY;
        }

        @Override
        public int getItemCount() {
            return data == null ? 0 : 6;//tracker_info.isTester ? 5 : 3;//return (tracker_info.isTester ? 6 : 3);
        }

        @Override
        public int getBlockType(int var1) {
            int type = getItemViewType(var1);
            if(type == TYPE_DETAIL_BROWSER) return CardItemDecorator.BOTTOM;
            if(type == TYPE_ACTIVITY || var1 == 0) return CardItemDecorator.TOP;
            return CardItemDecorator.MIDDLE;
        }


        public class ProfileHeaderHolder extends BindableHolder<UserProfile.Data> {

            public View mScrimTop;
            public View mPhotoOverlay;
            public View mScrimBottom;
            public ImageView mUserPhoto;
            public TextView mUserName;
            public TextView mUserOnline;
            public Button mButton1;
            public Button mButton2;
            public Button mButton3;

            public ProfileHeaderHolder() {
                super(inflater.inflate(layoutType, null, false));
                this.mScrimTop = itemView.findViewById(R.id.profile_scrim1);
                this.mScrimBottom = itemView.findViewById(R.id.profile_scrim2);
                this.mPhotoOverlay = itemView.findViewById(R.id.profile_photo_overlay);
                this.mUserName = itemView.findViewById(R.id.profile_name);
                this.mUserPhoto = itemView.findViewById(R.id.profile_photo);
                this.mUserOnline = itemView.findViewById(R.id.profile_last_seen);
                this.mButton1 = itemView.findViewById(R.id.profile_btn1);
                this.mButton2 = itemView.findViewById(R.id.profile_btn2);
                this.mButton3 = itemView.findViewById(R.id.profile_btn3);
            }

            @Override
            public void bind(UserProfile.Data user) {
                this.mUserName.setText(user.firstName + " " + user.lastName);
                this.mButton1.setOnClickListener(v -> {
                    Intent message = new Intent(Intent.ACTION_VIEW);
                    message.setData(Uri.parse("vk://vk.me/id" + uid));
                    parent.startActivity(message);
                });
                this.mButton2.setVisibility(View.GONE);
                this.mButton3.setVisibility(View.GONE);
                if(user.deactivated == null) {
                    if(user.online == 1) {
                        this.mUserOnline.setText(R.string.online);
                    } else {
                        int id = R.string.last_seen_profile_f;
                        if(user.sex == 2) {
                            id = R.string.last_seen_profile_m;
                        }
                        this.mUserOnline.setText(String.format(BTApp.String(id), TimeUtils.langDate(user.last_seen.time).toLowerCase()));
                    }
                } else {
                    this.mUserOnline.setVisibility(View.INVISIBLE);
                    this.mButton1.setVisibility(View.GONE);
                }
                if(layoutType == R.layout.profile_head && user.crop_photo != null && user.crop_photo.photo.sizes.size() > 0
                        | user.deactivated == null) {
                    DisplayMetrics metrics = new DisplayMetrics();
                    getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
                    UserProfile.Size photo = new UserProfile.Size();
                    for(int i = 0; i < user.crop_photo.photo.sizes.size(); i++) {
                        if(user.crop_photo.photo.sizes.get(i).width < metrics.widthPixels && user.crop_photo.photo.sizes.get(i).width > photo.width) {
                            photo = user.crop_photo.photo.sizes.get(i);
                        }
                    }
                    Picasso.with(itemView.getContext()).load(photo.url).into(new Target() {
                        @Override public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                            UserProfile.Rect finalrect;
                            UserProfile.Crop crop = user.crop_photo.crop;
                            UserProfile.Rect rect = user.crop_photo.rect;
                            if(rect.x == 0 && rect.y == 0 && rect.x2 == 100 && rect.y2 == 100) {
                                finalrect = crop;
                            } else {
                                finalrect = rect;
                            }
                            int start = (int) (bitmap.getHeight() * finalrect.y / 100);
                            int end = (int) (bitmap.getHeight() * finalrect.y2 / 100) - start;
                            if(end > bitmap.getHeight() * 0.6666666) end = (int) (bitmap.getHeight() * 0.6666666);
                            Bitmap cropped = Bitmap.createBitmap(bitmap, 0, start, bitmap.getWidth(), end);
                            mUserPhoto.setImageBitmap(cropped);
                        }
                        @Override public void onBitmapFailed(Drawable errorDrawable) { }
                        @Override public void onPrepareLoad(Drawable placeHolderDrawable) { }
                    });
                } else {
                    String url = user.photo_50;
                    if(user.photo_200 != null) url = user.photo_200;
                    else if(user.photo_100 != null) url = user.photo_100;
                    RequestCreator creator = Picasso.with(itemView.getContext()).load(url);
                    if(layoutType != R.layout.profile_head) creator.transform(new CropCircleTransformation());
                    creator.into(this.mUserPhoto);
                }
            }
        }

        public class ProfileActivityHolder extends BindableHolder<UserProfile.Data> {

            private ProfileActivityView activityView;
            private HorizontalScrollView root;
            private ImageView scrimLeft;
            private ImageView scrimRight;
            private int maxScroll;
            private TextView title;

            public ProfileActivityHolder() {
                super(inflater.inflate(R.layout.profile_activity, null, false));
                root = (HorizontalScrollView) $(R.id.scroll);
                activityView = (ProfileActivityView) $(R.id.profileActivityView);
                scrimLeft = (ImageView) $(R.id.scrim_left);
                scrimRight = (ImageView) $(R.id.scrim_right);
                title = (TextView) $(R.id.title);
            }

            @Override
            public void bind(UserProfile.Data var1) {
                activityView.setData(var1.webInfo == null ? new ArrayList<>() : var1.webInfo.activities);
                root.setHorizontalScrollBarEnabled(false);
                DrawableCompat.setTint(scrimLeft.getBackground(), ThemeController.getBackground());
                DrawableCompat.setTint(scrimRight.getBackground(), ThemeController.getBackground());
                root.getViewTreeObserver().addOnScrollChangedListener(() -> {
                    maxScroll = activityView.getMeasuredWidth() - root.getMeasuredWidth();
                    this.scrimLeft.setAlpha(getAlphaLeft());
                    this.scrimRight.setAlpha(getAlphaRight());
                });
            }

            private float getAlphaLeft() {
                if(root.getScrollX() > 0) {
                    return (float)root.getScrollX() / BTApp.dp(16);
                } else {
                    return 0.0F;
                }
            }

            private float getAlphaRight() {
                if(maxScroll - root.getScrollX() > 0) {
                    return (float)(maxScroll - root.getScrollX()) / BTApp.dp(16);
                } else {
                    return 0.0F;
                }
            }
        }

    }
}
