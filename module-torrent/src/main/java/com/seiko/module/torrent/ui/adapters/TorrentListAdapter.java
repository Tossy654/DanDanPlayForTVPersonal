/*
 * Copyright (C) 2016-2018 Yaroslav Pronin <proninyaroslav@mail.ru>
 *
 * This file is part of LibreTorrent.
 *
 * LibreTorrent is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * LibreTorrent is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with LibreTorrent.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.seiko.module.torrent.ui.adapters;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.format.DateUtils;
import android.text.format.Formatter;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;


import com.seiko.module.torrent.R;
import com.seiko.module.torrent.models.BasicStateParcel;
import com.seiko.module.torrent.models.sort.TorrentSortingComparator;
import com.seiko.torrent.constants.TorrentStateCode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import static com.seiko.module.torrent.constants.ConstantsKt.INFINITY_SYMBOL;

public class TorrentListAdapter extends SelectableAdapter<TorrentListAdapter.ViewHolder>
{
    private static final String TAG = "TorrentListAdapter";

    private Context context;
    private ViewHolder.ClickListener clickListener;
    private int rowLayout;
    /* Filtered items */
    private List<TorrentListItem> currentItems;
    private Map<String, TorrentListItem> allItems;
    private AtomicReference<TorrentListItem> curOpenTorrent = new AtomicReference<>();
    private DisplayFilter displayFilter = new DisplayFilter();
    private SearchFilter searchFilter = new SearchFilter();
    private TorrentSortingComparator sorting;

    public TorrentListAdapter(Context context,
                              int rowLayout, ViewHolder.ClickListener clickListener,
                              TorrentSortingComparator sorting)
    {
        this.context = context;
        this.rowLayout = rowLayout;
        this.clickListener = clickListener;
        this.sorting = sorting;
        allItems = new HashMap<>();
        currentItems = new ArrayList<>(allItems.values());
        Collections.sort(currentItems, sorting);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);

        return new ViewHolder(v, clickListener, currentItems);
    }

    @SuppressWarnings("ResourceType")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        TorrentListItem item = currentItems.get(position);

        holder.indicatorCurOpenTorrent.setBackground(
                ContextCompat.getDrawable(context, android.R.color.transparent));

        TypedArray a = context.obtainStyledAttributes(new TypedValue().data, new int[] {
                R.attr.torrent_defaultSelectRect,
                R.attr.torrent_defaultRectRipple
        });

        if (isSelected(position)) {
            holder.itemTorrentList.setBackground(a.getDrawable(0));
        } else {
            holder.itemTorrentList.setBackground(a.getDrawable(1));
        }
        a.recycle();

        holder.name.setText(item.name);
        if (item.stateCode == TorrentStateCode.DOWNLOADING_METADATA) {
            holder.progress.setIndeterminate(true);
        } else {
            holder.progress.setIndeterminate(false);
            holder.progress.setProgress(item.progress);
        }

        String stateString = "";
        switch (item.stateCode) {
            case TorrentStateCode.DOWNLOADING:
                stateString = context.getString(R.string.torrent_status_downloading);
                break;
            case TorrentStateCode.SEEDING:
                stateString = context.getString(R.string.torrent_status_seeding);
                break;
            case TorrentStateCode.PAUSED:
                stateString = context.getString(R.string.torrent_status_paused);
                break;
            case TorrentStateCode.STOPPED:
                stateString = context.getString(R.string.torrent_status_stopped);
                break;
            case TorrentStateCode.FINISHED:
                stateString = context.getString(R.string.torrent_status_finished);
                break;
            case TorrentStateCode.CHECKING:
                stateString = context.getString(R.string.torrent_status_checking);
                break;
            case TorrentStateCode.DOWNLOADING_METADATA:
                stateString = context.getString(R.string.torrent_status_downloading_metadata);
        }
        holder.item.setText(stateString);

        String counterTemplate = context.getString(R.string.torrent_download_counter_ETA_template);
        String totalBytes = Formatter.formatFileSize(context, item.totalBytes);
        String receivedBytes;
        String ETA;
        if (item.ETA == -1)
            ETA = "\u2022 " + INFINITY_SYMBOL;
        else if (item.ETA == 0)
            ETA = "";
        else
            ETA = "\u2022 " + DateUtils.formatElapsedTime(item.ETA);

        if (item.progress == 100)
            receivedBytes = totalBytes;
        else
            receivedBytes = Formatter.formatFileSize(context, item.receivedBytes);

        holder.downloadCounter.setText(String.format(counterTemplate, receivedBytes, totalBytes,
                                                     (item.totalBytes == 0 ? 0 : item.progress), ETA));

        String speedTemplate = context.getString(R.string.torrent_download_upload_speed_template);
        String downloadSpeed = Formatter.formatFileSize(context, item.downloadSpeed);
        String uploadSpeed = Formatter.formatFileSize(context, item.uploadSpeed);
        holder.downloadUploadSpeed.setText(String.format(speedTemplate, downloadSpeed, uploadSpeed));

        String peersTemplate = context.getString(R.string.torrent_peers_template);
        holder.peers.setText(String.format(peersTemplate, item.peers, item.totalPeers));
        holder.setPauseButtonState(item.stateCode == TorrentStateCode.PAUSED);

        if (item.error != null) {
            holder.error.setVisibility(View.VISIBLE);
            String errorTemplate = context.getString(R.string.torrent_error_template);
            holder.error.setText(String.format(errorTemplate, item.error));
        } else {
            holder.error.setVisibility(View.GONE);
        }

        TorrentListItem curTorrent = curOpenTorrent.get();
        if (curTorrent != null) {
            if (getItemPosition(curTorrent) == position) {
                if (!isSelected(position)) {
                    a = context.obtainStyledAttributes(new TypedValue().data, new int[]{ R.attr.torrent_curOpenTorrentIndicator });
                    holder.itemTorrentList.setBackground(a.getDrawable(0));
                    a.recycle();
                }

                holder.indicatorCurOpenTorrent.setBackground(
                        ContextCompat.getDrawable(context, R.color.torrent_accent));
            }
        }
    }

    public synchronized void addItem(TorrentListItem item)
    {
        if (item == null)
            return;

        TorrentListItem filtered = displayFilter.filter(item);
        if (filtered != null) {
            currentItems.add(filtered);
            Collections.sort(currentItems, sorting);
            notifyItemInserted(currentItems.indexOf(filtered));
        }
        allItems.put(item.torrentId, item);
    }

    public synchronized void addItems(Collection<TorrentListItem> items)
    {
        if (items == null)
            return;

        List<TorrentListItem> filtered = displayFilter.filter(items);
        currentItems.addAll(filtered);
        Collections.sort(currentItems, sorting);
        notifyItemRangeInserted(0, filtered.size());
        for (TorrentListItem item : items)
            allItems.put(item.torrentId, item);
    }

    /*
     * Mark the torrent as currently open.
     */

    public void markAsOpen(TorrentListItem item)
    {
        curOpenTorrent.set(item);
        notifyDataSetChanged();
    }

    public synchronized void updateItem(BasicStateParcel state)
    {
        if (state == null)
            return;

        TorrentListItem item = allItems.get(state.torrentId);
        if (item == null)
            item = new TorrentListItem();
        item.copyFrom(state);

        if (!currentItems.contains(item)) {
            TorrentListItem filtered = displayFilter.filter(item);
            if (filtered != null) {
                currentItems.add(item);
                Collections.sort(currentItems, sorting);
                notifyItemInserted(currentItems.indexOf(filtered));
            }
        } else {
            int position = currentItems.indexOf(item);
            if (position >= 0) {
                currentItems.remove(position);
                TorrentListItem filtered = displayFilter.filter(item);
                if (filtered != null) {
                    currentItems.add(position, item);
                    Collections.sort(currentItems, sorting);
                    int newPosition = currentItems.indexOf(item);
                    if (newPosition == position)
                        notifyItemChanged(position);
                    else
                        notifyDataSetChanged();
                } else {
                    notifyItemRemoved(position);
                }
            }
        }
        allItems.put(item.torrentId, item);
    }

    public void setDisplayFilter(DisplayFilter displayFilter)
    {
        if (displayFilter == null)
            return;

        this.displayFilter = displayFilter;
        currentItems.clear();
        currentItems.addAll(displayFilter.filter(allItems.values()));
        Collections.sort(currentItems, sorting);

        notifyDataSetChanged();
    }

    public void search(String searchPattern)
    {
        if (searchPattern == null)
            return;

        searchFilter.filter(searchPattern);
    }

    public void clearAll()
    {
        allItems.clear();

        int size = currentItems.size();
        if (size > 0) {
            currentItems.clear();
            notifyItemRangeRemoved(0, size);
        }
    }

    public void deleteItem(String id)
    {
        if (id == null)
            return;

        currentItems.remove(getItem(id));
        allItems.remove(id);

        notifyDataSetChanged();
    }

    public void setSorting(TorrentSortingComparator sorting)
    {
        if (sorting == null)
            return;

        this.sorting = sorting;
        Collections.sort(currentItems, sorting);

        notifyItemRangeChanged(0, currentItems.size());
    }

    public TorrentListItem getItem(int position)
    {
        if (position < 0 || position >= currentItems.size())
            return null;

        return currentItems.get(position);
    }

    public TorrentListItem getItem(String id)
    {
        if (id == null || !allItems.containsKey(id))
            return null;

        TorrentListItem item = allItems.get(id);

        return (currentItems.contains(item) ? item : null);
    }

    public int getItemPosition(TorrentListItem item)
    {
        if (item == null)
            return -1;

        return currentItems.indexOf(item);
    }

    public int getItemPosition(String id)
    {
        if (id == null || !allItems.containsKey(id))
            return -1;

        return currentItems.indexOf(allItems.get(id));
    }

    public boolean isEmpty()
    {
        return currentItems.isEmpty();
    }

    @Override
    public int getItemCount()
    {
        return currentItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnLongClickListener
    {
        private Context context;
        private ClickListener listener;
        private List<TorrentListItem> items;
        LinearLayout itemTorrentList;
        TextView name;
        ImageButton pauseButton;
        ProgressBar progress;
        TextView item;
        TextView downloadCounter;
        TextView downloadUploadSpeed;
        TextView peers;
        TextView error;
        View indicatorCurOpenTorrent;
        private AnimatedVectorDrawableCompat playToPauseAnim;
        private AnimatedVectorDrawableCompat pauseToPlayAnim;
        private AnimatedVectorDrawableCompat currAnim;

        public ViewHolder(View itemView, final ClickListener listener, final List<TorrentListItem> items)
        {
            super(itemView);

            this.context = itemView.getContext();
            this.listener = listener;
            this.items = items;
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

            playToPauseAnim = AnimatedVectorDrawableCompat.create(context, R.drawable.torrent_play_to_pause);
            pauseToPlayAnim = AnimatedVectorDrawableCompat.create(context, R.drawable.torrent_pause_to_play);
            itemTorrentList = itemView.findViewById(R.id.item_torrent_list);
            name = itemView.findViewById(R.id.torrent_name);
            pauseButton = itemView.findViewById(R.id.pause_torrent);
            pauseButton.setOnClickListener((View v) -> {
                int position = getAdapterPosition();
                if (listener != null && position >= 0) {
                    TorrentListItem item = items.get(position);
                    listener.onPauseButtonClicked(position, item);
                }
            });
            progress = itemView.findViewById(R.id.torrent_progress);
//            Utils.colorizeProgressBar(context, progress);
            item = itemView.findViewById(R.id.torrent_status);
            downloadCounter = itemView.findViewById(R.id.torrent_download_counter);
            downloadUploadSpeed = itemView.findViewById(R.id.torrent_download_upload_speed);
            peers = itemView.findViewById(R.id.torrent_peers);
            error = itemView.findViewById(R.id.torrent_error);
            indicatorCurOpenTorrent = itemView.findViewById(R.id.indicator_cur_open_torrent);
        }

        @Override
        public void onClick(View v)
        {
            int position = getAdapterPosition();

            if (listener != null && position >= 0) {
                TorrentListItem item = items.get(position);
                listener.onItemClicked(position, item);
            }
        }

        @Override
        public boolean onLongClick(View v)
        {
            int position = getAdapterPosition();

            if (listener != null && position >= 0) {
                TorrentListItem item = items.get(getAdapterPosition());
                listener.onItemLongClicked(position, item);

                return true;
            }

            return false;
        }

        void setPauseButtonState(boolean isPause)
        {
            AnimatedVectorDrawableCompat prevAnim = currAnim;
            currAnim = (isPause ? pauseToPlayAnim : playToPauseAnim);
            pauseButton.setImageDrawable(currAnim);
            if (currAnim != prevAnim)
                currAnim.start();
        }

        public interface ClickListener
        {
            void onItemClicked(int position, TorrentListItem item);

            boolean onItemLongClicked(int position, TorrentListItem item);

            void onPauseButtonClicked(int position, TorrentListItem item);
        }
    }

    public static class DisplayFilter
    {
        int constraintCode;

        /* Without filtering */
        public DisplayFilter() { }

        public DisplayFilter(int constraint)
        {
            constraintCode = constraint;
        }

        public List<TorrentListItem> filter(Collection<TorrentListItem> items)
        {
            List<TorrentListItem> filtered = new ArrayList<>();
            if (items != null) {
//                if (constraintCode != null) {
                    for (TorrentListItem item : items)
                        if (item.stateCode == constraintCode)
                            filtered.add(item);
//                } else {
//                    filtered.addAll(items);
//                }
            }

            return filtered;
        }

        public TorrentListItem filter(TorrentListItem item)
        {
            if (item == null)
                return null;

//            if (constraintCode != null) {
                if (item.stateCode == constraintCode)
                    return item;
//            } else {
//                return item;
//            }

            return null;
        }
    }

    private class SearchFilter extends Filter
    {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence)
        {
            currentItems.clear();

            if (charSequence.length() == 0) {
                currentItems.addAll(displayFilter.filter(allItems.values()));
            } else {
                String filterPattern = charSequence.toString().toLowerCase().trim();
                for (TorrentListItem item : allItems.values())
                    if (item.name.toLowerCase().contains(filterPattern))
                        currentItems.add(item);
            }
            Collections.sort(currentItems, sorting);

            return new FilterResults();
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults)
        {
            notifyDataSetChanged();
        }
    }
}
