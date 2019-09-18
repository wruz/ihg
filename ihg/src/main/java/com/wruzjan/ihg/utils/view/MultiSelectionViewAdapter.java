package com.wruzjan.ihg.utils.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.wruzjan.ihg.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class MultiSelectionViewAdapter extends RecyclerView.Adapter<MultiSelectionViewAdapter.ViewHolder> {

    @NonNull private final List<Item> items;
    @Nullable private final Listener listener;

    public MultiSelectionViewAdapter(@NonNull List<Item> items, @Nullable Listener listener) {
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.dialog_multichoice_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(items.get(position), position, listener);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static final class ViewHolder extends RecyclerView.ViewHolder {

        private final CheckBox checkedTextView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            checkedTextView = itemView.findViewById(R.id.checked_text);
        }

        void bind(@NonNull final Item item, final int position, @Nullable final Listener listener) {
            checkedTextView.setChecked(item.selected);
            checkedTextView.setText(item.content);
            if (listener != null) {
                checkedTextView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        listener.onClick(position, isChecked);
                    }
                });
            }
        }
    }

    public static final class Item {
        private final boolean selected;
        @NonNull private final String content;

        public Item(boolean selected, @NonNull String content) {
            this.selected = selected;
            this.content = content;
        }
    }

    public interface Listener {

        void onClick(int position, boolean isChecked);
    }
}
