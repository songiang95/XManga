package com.example.songiang.xmanga.View;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.songiang.xmanga.Adapter.DetailAdapter;
import com.example.songiang.xmanga.Model.Chapter;
import com.example.songiang.xmanga.Model.Manga;
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
import butterknife.OnClick;

public class MangaDetailActivity extends Activity {
    @BindView(R.id.img_detail)
    ImageView imgDetail;
    @BindView(R.id.name_detail)
    TextView nameDetail;
    @BindView(R.id.tags)
    TextView tvTags;
    @BindView(R.id.same_author)
    Button sameAuthor;
    @BindView(R.id.recycle_chapter)
    RecyclerView recycleChapter;
    private Manga manga;
    private ArrayList<Chapter> listChapter;
    private DetailAdapter detailAdapter;
    private String author_url = "https://hentaivn.net";
    private String author_name = "Cùng tác giả ";
    private String tags;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manga_detail);
        ButterKnife.bind(this);
        manga = (Manga) getIntent().getSerializableExtra("MANGA");
        listChapter = new ArrayList<Chapter>();
        detailAdapter = new DetailAdapter(this, listChapter);

        initView();

        recycleChapter.setAdapter(detailAdapter);
        new GetMangaDetailTask(this).execute(manga.getUrl());

    }

    @OnClick(R.id.same_author)
    public void onClickSameAuthor() {
        Intent intent = new Intent(this, SameAuthorActitvity.class);
        intent.putExtra("AUTHOR_URL", author_url);
        startActivity(intent);
    }

    private void initView() {
        Glide.with(this)
                .load(manga.getImg())
                .into(imgDetail);

        recycleChapter.setLayoutManager(new GridLayoutManager(this, 5));
        recycleChapter.hasFixedSize();
    }

    private static class GetMangaDetailTask extends AsyncTask<String, Void, ArrayList<Chapter>> {

        WeakReference<MangaDetailActivity> mangaDetailActivityWeakReference;

        GetMangaDetailTask(MangaDetailActivity activity) {
            mangaDetailActivityWeakReference = new WeakReference<>(activity);
        }

        @Override
        protected ArrayList<Chapter> doInBackground(String... url) {
            Document document = null;

            try {
                document = (Document) Jsoup.connect(url[0]).get();
                //Get chapter
                if (document != null) {
                    //select chapter
                    Elements chapterElements = document.select("tr");
                    Elements authorElements = document.select("a[href^=/tacgia]");
                    for (Element element : chapterElements) {
                        Chapter chapter = new Chapter();
                        Element urlSubject = element.getElementsByTag("a").first();
                        Element nameSubject = element.getElementsByTag("h2").first();
                        if (urlSubject != null) {
                            String chapter_url = urlSubject.attr("href");
                            chapter_url = "https://hentaivn.net" + chapter_url;
                            chapter.setUrl(chapter_url);
                        }
                        if (nameSubject != null) {
                            String chapter_name = nameSubject.text();

                            chapter.setName(chapter_name);
                        }
                        mangaDetailActivityWeakReference.get().listChapter.add(chapter);
                    }//end get chapter
                    for (Element element : authorElements) {
                        Element authorSubject = element.getElementsByTag("a").first();
                        mangaDetailActivityWeakReference.get().author_url += authorSubject.attr("href");
                        mangaDetailActivityWeakReference.get().author_name += authorSubject.text();
                    }
                    //get tag
                    Elements tagsElement = document.select(".tag");
                    for (Element element : tagsElement) {
                        Element tagSubject = element.getElementsByTag("a").first();
                        if (tagSubject != null) {
                            String tag = tagSubject.text() + " ";
                            mangaDetailActivityWeakReference.get().tags += tag;
                        }
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return mangaDetailActivityWeakReference.get().listChapter;
        }

        @Override
        protected void onPostExecute(ArrayList<Chapter> chapters) {
            super.onPostExecute(chapters);
            mangaDetailActivityWeakReference.get().tvTags.setText(mangaDetailActivityWeakReference.get().tags);
            mangaDetailActivityWeakReference.get().sameAuthor.setText(mangaDetailActivityWeakReference.get().author_name);
            mangaDetailActivityWeakReference.get().detailAdapter.notifyDataSetChanged();
        }
    }
}
