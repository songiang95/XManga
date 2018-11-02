package com.example.songiang.xmanga.View;

import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.songiang.xmanga.Adapter.DetailAdapter;
import com.example.songiang.xmanga.Adapter.PagerAdapter;
import com.example.songiang.xmanga.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class ReadingActivity extends AppCompatActivity {
    private ArrayList<String> imagesUrl;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading);
        String url = getIntent().getStringExtra("url");
        imagesUrl = new ArrayList<>();
        new DownloadImage().execute(url);


    }
    private void addControl()
    {
        viewPager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tab_layout);
        FragmentManager fm = getSupportFragmentManager();
        PagerAdapter pagerAdapter = new PagerAdapter(fm,imagesUrl);
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));
    }

    private class DownloadImage extends AsyncTask<String, Void, ArrayList<String>>
    {
        @Override
        protected ArrayList<String> doInBackground(String... strings) {
            Document document;
            try {
                document = (Document) Jsoup.connect(strings[0]).get();
                if (document != null) {
                    Elements elements = document.select("img[src$=1200]");
                    for (Element element : elements) {

                        Element imgSubject = element.getElementsByTag("img").first();
                        if (imgSubject!=null)
                        {
                            String img_url = imgSubject.attr("src");

                            imagesUrl.add(img_url);
                        }
                    }
                }
            }catch (IOException e)
            {
                e.printStackTrace();
            }
            return imagesUrl;
        }

        @Override
        protected void onPostExecute(ArrayList<String> strings) {
            super.onPostExecute(strings);
            addControl();
        }
    }
}
