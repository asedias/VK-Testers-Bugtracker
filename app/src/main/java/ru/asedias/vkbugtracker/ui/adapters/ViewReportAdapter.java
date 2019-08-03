package ru.asedias.vkbugtracker.ui.adapters;

import android.app.Activity;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import ru.asedias.vkbugtracker.BTApp;
import ru.asedias.vkbugtracker.R;
import ru.asedias.vkbugtracker.api.webmethods.models.Report;
import ru.asedias.vkbugtracker.ui.CardItemDecorator;
import ru.asedias.vkbugtracker.ui.DividerItemDecoration;
import ru.asedias.vkbugtracker.ui.LayoutHelper;
import ru.asedias.vkbugtracker.ui.holders.reportview.AttachmentHolder;
import ru.asedias.vkbugtracker.ui.holders.reportview.AuthorHolder;
import ru.asedias.vkbugtracker.ui.holders.BindableHolder;
import ru.asedias.vkbugtracker.ui.holders.reportview.CommentHolder;
import ru.asedias.vkbugtracker.ui.holders.reportview.DetailItemHolder;
import ru.asedias.vkbugtracker.ui.holders.reportview.FooterHolder;
import ru.asedias.vkbugtracker.ui.holders.HeaderHolder;
import ru.asedias.vkbugtracker.ui.holders.PhotosGridHolder;
import ru.asedias.vkbugtracker.ui.holders.reportview.TextHolder;

/**
 * Created by rorom on 10.11.2018.
 */

public class ViewReportAdapter extends RecyclerView.Adapter<BindableHolder> implements DividerItemDecoration.Provider, CardItemDecorator.Provider {

    private Report data = new Report();
    private LayoutInflater mInflater;
    private final static int TYPE_HEADER = 0;
    private final static int TYPE_AUTHOR = 1;
    private final static int TYPE_TEXT = 2;
    private final static int TYPE_PHOTOS = 3;
    private final static int TYPE_ATTACHMENT = 4;
    private final static int TYPE_FOOTER = 5;
    private final static int TYPE_DETAIL_TEXT = 6;
    private final static int TYPE_DETAIL_PHOTO = 7;
    private final static int TYPE_SHOW_MORE = 8;
    private final static int TYPE_COMMENT = 9;
    private int rid = 0;

    public void setData(Report data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BindableHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(mInflater == null) {
            mInflater = ((Activity) parent.getContext()).getLayoutInflater();
        }
        switch (viewType) {
            case TYPE_HEADER: return new HeaderHolder(mInflater);
            case TYPE_AUTHOR: return new AuthorHolder(mInflater);
            case TYPE_TEXT: return new TextHolder(mInflater);
            case TYPE_PHOTOS: return new PhotosGridHolder((Activity) parent.getContext());
            case TYPE_ATTACHMENT: return new AttachmentHolder(mInflater);
            case TYPE_FOOTER: return new FooterHolder(mInflater, getRid());
            case TYPE_DETAIL_PHOTO: return new DetailItemHolder(mInflater, 0);
            case TYPE_DETAIL_TEXT: return new DetailItemHolder(mInflater, 1);
            case TYPE_SHOW_MORE: return new DetailItemHolder(mInflater, data.details);
        }
        return new CommentHolder(mInflater);
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0) return TYPE_HEADER;
        if(position == 1) return TYPE_AUTHOR;
        if(position == 2) return TYPE_TEXT;
        if(position == 3) return TYPE_PHOTOS;
        if(position < (4 + data.attachments.size())) {
            return TYPE_ATTACHMENT;
        } else if(position == (4 + data.attachments.size())) {
            return TYPE_FOOTER;
        }
        position -= data.attachments.size();
        if(position == 5) return TYPE_DETAIL_PHOTO;
        if(position == 6) return TYPE_DETAIL_TEXT;
        if(position == 7) return TYPE_SHOW_MORE;
        if(position == 8) return TYPE_HEADER;
        return TYPE_COMMENT;
    }

    @Override
    public void onBindViewHolder(@NonNull BindableHolder holder, int position) {
        holder.itemView.setLayoutParams(LayoutHelper.fullWidthRecycler());
        switch (getItemViewType(position)) {
            case TYPE_HEADER: {
                if(position == 0) {
                    holder.bind(data.title); break;
                }
                holder.bind(BTApp.QuantityString(R.plurals.good_comments, data.comments.size(), data.comments.size()));
                break;
            }
            case TYPE_AUTHOR: {
                holder.bind(data.author);
                break;
            }
            case TYPE_TEXT: {
                holder.bind(data.description);
                break;
            }
            case TYPE_PHOTOS: {
                holder.bind(data.photos); break;
            }
            case TYPE_ATTACHMENT: {
                holder.bind(data.attachments.get(position - 4));
                if(getItemViewType(position+1) == TYPE_FOOTER) {
                    ((RecyclerView.LayoutParams)holder.itemView.getLayoutParams()).bottomMargin = BTApp.dp(8);
                }
                break;
            }
            case TYPE_FOOTER: holder.bind(data.footer); break;
            case TYPE_DETAIL_PHOTO: holder.bind(data.details.get(0)); break;
            case TYPE_DETAIL_TEXT: holder.bind(data.details.get(1)); break;
            case TYPE_SHOW_MORE: holder.bind(null); break;
            case TYPE_COMMENT: holder.bind(data.comments.get(position - getItemCount() + data.comments.size()));
        }
        //holder.bind(data.title);
    }

    @Override
    public int getItemCount() {
        return data.title != null ? 4 + data.attachments.size() + (data.comments.size() > 0 ? 5 + data.comments.size() : 4) : 0;
    }

    public int getRid() {
        return rid;
    }

    public void setRid(int rid) {
        this.rid = rid;
    }

    @Override
    public boolean needDrawDividerAfter(int position) {
        int type = getItemViewType(position);
        if(type == TYPE_COMMENT) return true;
        //if(type == TYPE_FOOTER) return true;
        //if(type == TYPE_SHOW_MORE) return true;
        if(type == TYPE_ATTACHMENT && getItemViewType(position+1) == TYPE_FOOTER) return true;
        return false;
    }

    @Nullable
    @Override
    public boolean needMarginBottom(int position) {
        int type = getItemViewType(position);
        if(type == TYPE_ATTACHMENT && getItemViewType(position+1) == TYPE_FOOTER) return true;
        return false;
    }

    @Override
    public int getBlockType(int var1) {
        int type = getItemViewType(var1);
        if(type == TYPE_FOOTER || type == TYPE_SHOW_MORE) return CardItemDecorator.BOTTOM;
        if(type == TYPE_HEADER || type == TYPE_DETAIL_PHOTO) return CardItemDecorator.TOP;
        return CardItemDecorator.MIDDLE;
    }
}
