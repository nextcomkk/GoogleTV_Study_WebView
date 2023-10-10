package com.example.androidtv.sample1.activity;

import androidx.fragment.app.FragmentActivity;
import android.os.Bundle;

import com.example.androidtv.sample1.fragment.MainFragment;
import com.example.androidtv.sample1.R;

public class MainActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_browse_fragment, new MainFragment())
                    .commitNow();
        }
    }
}