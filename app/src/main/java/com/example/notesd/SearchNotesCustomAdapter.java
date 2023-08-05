package com.example.notesd;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public class SearchNotesCustomAdapter extends RecyclerView.Adapter<SearchNotesCustomAdapter.ViewHolder> implements Filterable {

    private Context context;
    private ArrayList<Notes> originalList;
    private ArrayList<Notes> filteredList;
    private CustomFilter customFilter;
    private int lastPosition = -1;

    private int clickedPosition = -1;

    public SearchNotesCustomAdapter(Context context, ArrayList<Notes> itemList) {
        this.context = context;
        originalList = itemList;
        filteredList = itemList;
        customFilter = new CustomFilter();
    }

    // ViewHolder class to hold the views of each item in the RecyclerView
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitle, txtMessage;

        ViewHolder(View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtMessage = itemView.findViewById(R.id.txtMessage);
        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.notes_row, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Bind data to each item in the RecyclerView
        holder.txtTitle.setText(filteredList.get(position).getTitle());
        holder.txtMessage.setText(filteredList.get(position).getMessage());
        setAnimation(holder.itemView, position);

        holder.itemView.setOnClickListener(v -> {
            clickedPosition = holder.getAdapterPosition();
            Notes note = filteredList.get(clickedPosition);
            if (note == null) {
                // Handle null note object if needed
                return;
            }

            Intent intent = new Intent(context, addNotes.class);
            intent.putExtra("note_position", clickedPosition);
            ((Activity) context).startActivityForResult(intent, 1); // Use request code 1

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
            ArrayList<Notes> tempList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                // If the search query is empty, show all items
                tempList.addAll(originalList);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Notes item : originalList) {
                    // Implement your custom filtering logic here
                    // In this example, we check if the item contains the search query
                    String itemToSearch = item.getTitle();
                    if (itemToSearch.toLowerCase().contains(filterPattern)) {
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
            filteredList = (ArrayList<Notes>) results.values;
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
}


