package com.emp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.emp.db.Connector;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.io.IOException;

public class SettingsActivity extends AppCompatActivity {

    private TextView refreshDatabase;

    private BottomNavigationView bottomNavigationView;

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

        this.bottomNavigationView = this.findViewById(R.id.navbar);
        this.bottomNavigationView.setSelectedItemId(R.id.action_settings);
        this.bottomNavigationView.setOnItemSelectedListener(item -> {
            final int id = item.getItemId();

            Intent in;
            if(id == R.id.action_playlist) {
                in = new Intent(SettingsActivity.this, PlaylistActivity.class);
            } else if(id == R.id.action_settings) {
                return true;
            } else {
                in = new Intent(SettingsActivity.this, MainActivity.class);
            }

            SettingsActivity.this.startActivity(in);

            return true;
        });
    }
}