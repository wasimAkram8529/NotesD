package com.example.notesd;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class CustomRecyclerAdapter extends RecyclerView.Adapter<CustomRecyclerAdapter.ViewHolder> implements Filterable {

    private ArrayList<String> originalList;
    private Context context;
    private ArrayList<String> filteredList;
    private CustomFilter customFilter;
    private int lastPosition = -1;
    private OnItemClickListener itemClickListener;

    public CustomRecyclerAdapter(Context context, ArrayList<String> itemList) {
        this.context = context;
        originalList = itemList;
        filteredList = itemList;
        customFilter = new CustomFilter();
    }

    // ViewHolder class to hold the views of each item in the RecyclerView
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textCountry;

        ViewHolder(View itemView) {
            super(itemView);
            textCountry = itemView.findViewById(R.id.textMessage);
        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.list_item_layout, parent, false);
        return new ViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Bind data to each item in the RecyclerView
        String country = filteredList.get(position);
        holder.textCountry.setText(country);
        setAnimation(holder.itemView, position);
        holder.itemView.setOnClickListener(v -> {
            if (itemClickListener != null) {
                itemClickListener.onItemClick(country);
            }
        });
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    @NonNull
    @Override
    public Filter getFilter() {
        if (customFilter == null) {
            customFilter = new CustomFilter();
        }
        return customFilter;
    }

    private class CustomFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();
            ArrayList<String> tempList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                // If the search query is empty, show all items
                tempList.addAll(originalList);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (String item : originalList) {
                    // Implement your custom filtering logic here
                    // In this example, we check if the item contains the search query
                    if (item.toLowerCase().contains(filterPattern)) {
                        tempList.add(item);
                    }
                }
            }

            filterResults.values = tempList;
            filterResults.count = tempList.size();
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredList = (ArrayList<String>) results.values;
            notifyDataSetChanged();
        }
    }

    private void setAnimation(View view, int Position){

        if(Position > lastPosition){
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.country_name_anim);
            view.startAnimation(animation);
            lastPosition = Position;
        }

    }

    public interface OnItemClickListener {
        void onItemClick(String country);
    }

    // Setter for the item click listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }
}

