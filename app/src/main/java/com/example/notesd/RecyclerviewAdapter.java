package com.example.notesd;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerviewAdapter extends RecyclerView.Adapter<RecyclerviewAdapter.ViewHolder> {

    Context context;
    ArrayList<Notes> arrList;
    DatabaseHelper databaseHelper;


    private int clickedPosition = -1;
    RecyclerviewAdapter(Context context, ArrayList<Notes> arrList, DatabaseHelper databaseHelper)
    {
        this.databaseHelper = databaseHelper;
        this.context = context;
        this.arrList = arrList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.notes_row, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtTitle.setText(arrList.get(position).getTitle());
        holder.txtMessage.setText(arrList.get(position).getMessage());

        holder.llRow.setOnClickListener(v -> {
            clickedPosition = holder.getAdapterPosition();
            Notes note = arrList.get(clickedPosition);
            if (note == null) {
                // Handle null note object if needed
                return;
            }

            Intent intent = new Intent(context, addNotes.class);
            intent.putExtra("note_position", clickedPosition);
            ((Activity) context).startActivityForResult(intent, 1); // Use request code 1
        });

        holder.llRow.setOnLongClickListener(v -> {
            //int pos = holder.getAdapterPosition();

            AlertDialog.Builder builder = new AlertDialog.Builder(context)
                    .setTitle("Delete Note")
                    .setMessage("Are you sure want to delete ?")
                    .setIcon(R.drawable.baseline_delete_24)
                    .setPositiveButton("Yes", (dialog, which) -> {
                        databaseHelper.notesdao().deleteNotes(arrList.get(position));
                        arrList.remove(position);
                        notifyItemRemoved(position);
                    })
                    .setNegativeButton("No", (dialog, which) -> {

                    });

            builder.show();
            return true;
        });
    }


    @Override
    public int getItemCount() {
        return arrList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView txtTitle, txtMessage;
        LinearLayout llRow;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtMessage = itemView.findViewById(R.id.txtMessage);
            llRow =itemView.findViewById(R.id.llRow);
        }
    }
}
