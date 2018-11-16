package com.example.songiang.xmanga.View;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.songiang.xmanga.R;

public class SameAuthorActitvity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_same_author_actitvity);
        Intent intent = getIntent();
        String author_url = intent.getStringExtra("AUTHOR_URL");
         Bundle bundle = new Bundle();
         bundle.putString("url",author_url);
        Fragment fragment = new ListMangaFragment();
        fragment.setArguments(bundle);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.same_author_content,fragment);
      //  ft.addToBackStack(null);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }
}
