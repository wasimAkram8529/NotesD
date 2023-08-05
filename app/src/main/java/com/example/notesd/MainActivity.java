package com.example.notesd;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerNotes;
    RelativeLayout llNotes;
    FloatingActionButton addNew;
    static ArrayList<Notes> arrList = new ArrayList<>();
    DatabaseHelper databaseHelper;
    RecyclerviewAdapter recyclerviewAdapter;

    Toolbar toolbar;

    AutoCompleteTextView autoCompleteTextView;

    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        //autoCompleteTextView = findViewById(R.id.autoCompleteTextView);

        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle("Notes");
        }

        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        boolean isLoggedIn = prefs.getBoolean("isLoggedIn", false);

        if (!isLoggedIn) {
            // User is not logged in, show the login screen
            Intent intent = new Intent(MainActivity.this, login.class);
            startActivity(intent);
        }
        findId();
        showData();
        addNew.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, addNotes.class);
            startActivityForResult(intent, 1); // Use a request code (1 in this case)
        });

        TextView searchView = findViewById(R.id.searchView);

        searchView.setOnClickListener(v -> {
            dialog = new Dialog(MainActivity.this);

            // set custom dialog
            dialog.setContentView(R.layout.serach_notes);

            // set custom height and width
            //dialog.getWindow().setLayout(900,2100);
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
            layoutParams.copyFrom(dialog.getWindow().getAttributes());
            layoutParams.width = (int) (getResources().getDisplayMetrics().widthPixels * 1); // 80% of screen width
            layoutParams.height = (int) (getResources().getDisplayMetrics().heightPixels * 1); // 60% of screen height
            dialog.getWindow().setAttributes(layoutParams);

            // set transparent background
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            // show dialog
            dialog.show();

            EditText editText = dialog.findViewById(R.id.edit_text);
            RecyclerView recyclerView = dialog.findViewById(R.id.recyclerView);
            recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
            SearchNotesCustomAdapter searchNotesCustomAdapter = new SearchNotesCustomAdapter(MainActivity.this, arrList);
            recyclerView.setAdapter(searchNotesCustomAdapter);

            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    searchNotesCustomAdapter.getFilter().filter(s);
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            TextView cancelDialog = dialog.findViewById(R.id.cancelDialog);
            cancelDialog.setOnClickListener(v1 -> {
                dialog.dismiss();
            });
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Dismiss the dialog if it's showing when coming back from another activity
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            // Refresh the data when returning from addNotes activity
            showData();
            int updatedNotePosition = data.getIntExtra("updated_note_position", -1);
            if (updatedNotePosition != -1) {
                recyclerNotes.scrollToPosition(updatedNotePosition);
            }

            else{
                recyclerNotes.scrollToPosition(arrList.size() - 1);
            }
        }
    }

    public void showData() {
        arrList = (ArrayList<Notes>) databaseHelper.notesdao().getAllInstance();
        recyclerviewAdapter = new RecyclerviewAdapter(this, arrList, databaseHelper);
        recyclerNotes.setAdapter(recyclerviewAdapter);
    }

    private void findId() {
        recyclerNotes = findViewById(R.id.recyclerNotes);
        llNotes = findViewById(R.id.llNotes);
        addNew = findViewById(R.id.addNew);

        recyclerNotes.setLayoutManager(new GridLayoutManager(this, 2));
        databaseHelper = DatabaseHelper.getInstance(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        new MenuInflater(this).inflate(R.menu.opt_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int itemId = item.getItemId();

        if(itemId == R.id.logOut){
            SharedPreferences.Editor editor = getSharedPreferences("MyPrefs", MODE_PRIVATE).edit();
            editor.putBoolean("isLoggedIn", false);
            editor.apply();

            Intent intent = new Intent(MainActivity.this, login.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

}