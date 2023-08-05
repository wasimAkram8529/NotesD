package com.example.notesd;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class addNotes extends AppCompatActivity {

    EditText addTitle, addMessage;
    Button submit;

    Toolbar custom_toolbar;

    ImageView save;

    DatabaseHelper databaseHelper;

    private static boolean flag = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notes);

        Log.d("addNotes", "onCreate method executed");

        addTitle = findViewById(R.id.edtTitle);
        addMessage = findViewById(R.id.edtMessage);
        //submit = findViewById(R.id.btnSave);
        custom_toolbar = findViewById(R.id.custom_toolbar);
        save    = findViewById(R.id.save);

        /*setSupportActionBar(custom_toolbar);
        if(getSupportActionBar() != null){
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.baseline_arrow_back_24);
            getSupportActionBar().setTitle("Notes");
        }*/

        Toolbar toolbar = findViewById(R.id.custom_toolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setHomeAsUpIndicator(R.drawable.custom_back_button);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        databaseHelper = DatabaseHelper.getInstance(this);


        int check = updateTheNotes();

        if(check == 1)
            return;

        save.setOnClickListener(v -> {
            String title = addTitle.getText().toString();
            String message = addMessage.getText().toString();

            if (!title.trim().isEmpty() && !message.trim().isEmpty()) {
                databaseHelper.notesdao().addNotes(new Notes(title, message));
                Intent iNext = new Intent();
                setResult(RESULT_OK, iNext);
                finish();
            } else {
                Toast.makeText(addNotes.this, "Please Fill The Title and Message", Toast.LENGTH_SHORT).show();
            }
        });

        /*TextView back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(addNotes.this, MainActivity.class);
                startActivity(intent);
            }
        });*/
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int itemId = item.getItemId();

        if(itemId == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private int updateTheNotes() {
        Intent intent = getIntent();
        int notePosition = intent.getIntExtra("note_position", -1);
        if (notePosition == -1) {
            // new note
            return 0;
        }

        ArrayList<Notes> arrayList = (ArrayList<Notes>) databaseHelper.notesdao().getAllInstance();
        Notes note = arrayList.get(notePosition);

        EditText titleEditText = findViewById(R.id.edtTitle);
        EditText messageEditText = findViewById(R.id.edtMessage);

        titleEditText.setText(note.getTitle());
        messageEditText.setText(note.getMessage());

        addMessage.setOnClickListener(v -> {
            save.setVisibility(View.VISIBLE);
        });

        ImageView saveButton = findViewById(R.id.save);
        saveButton.setOnClickListener(v -> {
            String title = titleEditText.getText().toString();
            String message = messageEditText.getText().toString();

            note.setTitle(title);
            note.setMessage(message);
            databaseHelper.notesdao().updateNotes(note);

            Intent resultIntent = new Intent();
            resultIntent.putExtra("updated_note_position", notePosition);
            setResult(Activity.RESULT_OK, resultIntent);
            finish();
        });

        return 1;
    }
}