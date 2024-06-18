package com.emp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.emp.db.Connector;

import java.io.IOException;

public class SettingsActivity extends AppCompatActivity {

    private TextView refreshDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_settings);

        this.refreshDatabase = this.findViewById(R.id.refresh_database);
        this.refreshDatabase.setOnClickListener(v -> {
            try {
                Connector.syncMediaStoreData(SettingsActivity.this);
                Toast.makeText(SettingsActivity.this, "Databases synced", Toast.LENGTH_SHORT).show();
            } catch(IOException e) {
                Emp.e(e.getMessage());
            }
        });
    }
}