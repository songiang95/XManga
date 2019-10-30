package com.example.songiang.xmanga.View;

import android.content.Intent;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
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
