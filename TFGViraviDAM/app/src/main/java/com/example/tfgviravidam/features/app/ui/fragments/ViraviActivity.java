package com.example.tfgviravidam.features.app.ui.fragments;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.tfgviravidam.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ViraviActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viravi);
        BottomNavigationView navigation = findViewById(R.id.bottomNavigationView);
        navigation.setOnItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private final BottomNavigationView.OnItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.explore:
                    item.setIcon(R.drawable.globe);
                    loadFragment(new ExploreFragment());
                    return true;
                case R.id.profile:
                    item.setIcon(R.drawable.usuario__3_);
                    loadFragment(new ProfileFragment());
                    return true;
                case R.id.home:
                    item.setIcon(R.drawable.chat_de_burbujas__1_);
                    loadFragment(new ChatListFragment());
                    return true;
                default:
                    return true;
            }

        }
    };

    public void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, fragment);
        transaction.commit();
    }
}
