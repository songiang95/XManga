package com.example.songiang.xmanga.View;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.songiang.xmanga.Adapter.ImagePagerAdapter;
import com.example.songiang.xmanga.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReadingActivity extends AppCompatActivity {
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.tv_current_page)
    TextView tvCurrentPage;
    @BindView(R.id.tv_total_page)
    TextView tvTotalPage;
    @BindView(R.id.ll_page_number)
    LinearLayout llPageNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading);
        ButterKnife.bind(this);
        String url = getIntent().getStringExtra("url");
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tvCurrentPage.setText(Integer.toString(position + 1));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        new DownloadImage(this).execute(url);
    }

    private static class DownloadImage extends AsyncTask<String, Void, ArrayList<String>> {

        WeakReference<ReadingActivity> mActivityReference;

        DownloadImage(ReadingActivity activity) {
            mActivityReference = new WeakReference<>(activity);
        }

        @Override
        protected ArrayList<String> doInBackground(String... strings) {
            Document document;
            ArrayList<String> listImageUrl = new ArrayList<>();
            try {
                document = Jsoup.connect(strings[0]).get();
                if (document != null) {
                    Element element = document.selectFirst("div#image");
                    Elements elements = element.getElementsByTag("img");
                    // Elements elements = document.select("img[src$=1200]");
                    for (Element e : elements) {

                        Element imgSubject = e.getElementsByTag("img").first();
                        if (imgSubject != null) {
                            String img_url = imgSubject.attr("src");
                            Log.d("abba", "url: " + img_url);
                            listImageUrl.add(img_url);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return listImageUrl;
        }

        @Override
        protected void onPostExecute(ArrayList<String> listImage) {
            super.onPostExecute(listImage);
            if (listImage.isEmpty()) {
                Toast.makeText(mActivityReference.get(), "Load fail", Toast.LENGTH_SHORT).show();
                mActivityReference.get().finish();
            } else {
                ImagePagerAdapter adapter = new ImagePagerAdapter(mActivityReference.get(), listImage);
                adapter.setCallback(new ImagePagerAdapter.PagerImageCallback() {
                    @Override
                    public void onClickImage() {
                        if (mActivityReference.get().llPageNumber.getVisibility() == View.GONE) {
                            mActivityReference.get().llPageNumber.setVisibility(View.VISIBLE);
                        } else {
                            mActivityReference.get().llPageNumber.setVisibility(View.GONE);
                        }
                    }
                });
                mActivityReference.get().viewPager.setAdapter(adapter);
                mActivityReference.get().tvTotalPage.setText("/" + listImage.size());
            }
        }
    }
}
