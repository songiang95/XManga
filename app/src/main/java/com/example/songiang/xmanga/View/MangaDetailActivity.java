package com.example.songiang.xmanga.View;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.songiang.xmanga.Adapter.DetailAdapter;
import com.example.songiang.xmanga.Model.Chapter;
import com.example.songiang.xmanga.Model.Manga;
import com.example.songiang.xmanga.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class MangaDetailActivity extends Activity{
    private Manga manga;
    private ArrayList<Chapter> listChapter;
    private RecyclerView recyclerView;
    private ImageView imgView;
    private TextView tvName, tvAuthor, tvTags, tvDescription;
    private String tags="";
    private DetailAdapter detailAdapter;
    private String author_url = "https://hentaivn.net";
    private String author_name = "Cùng tác giả ";
    private TextView tv_author;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manga_detail);
        manga = (Manga) getIntent().getSerializableExtra("MANGA");
        listChapter = new ArrayList<Chapter>();
        detailAdapter = new DetailAdapter(this,listChapter);

        initView();

        recyclerView.setAdapter(detailAdapter);
        new GetMangaDetailTask().execute(manga.getUrl());

    }
    public void onClick(View view)
    {

            Intent intent = new Intent(this, SameAuthorActitvity.class);
            intent.putExtra("AUTHOR_URL", author_url);
            startActivity(intent);


    }

    private void initView()
    {
        imgView = findViewById(R.id.img_detail);
        Glide.with(this)
                .load(manga.getImg())
                .asBitmap()
                .atMost()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .animate(android.R.anim.fade_in)
                .approximate()
                .into(imgView);
        tvName = findViewById(R.id.name_detail);
        tvName.setText(manga.getName());

        tvTags = findViewById(R.id.tags);

        recyclerView = findViewById(R.id.recycle_chapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this,5));
        recyclerView.hasFixedSize();
    }

    private class GetMangaDetailTask extends AsyncTask<String, Void, ArrayList<Chapter>> {
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
                        listChapter.add(chapter);
                    }//end get chapter
                    for (Element element:authorElements)
                    {
                        Element authorSubject = element.getElementsByTag("a").first();
                              author_url += authorSubject.attr("href");
                              author_name += authorSubject.text();
                    }
                    //get tag
                    Elements tagsElement = document.select(".tag");
                    for(Element element:tagsElement)
                    {
                        Element tagSubject = element.getElementsByTag("a").first();
                        if (tagSubject!=null)
                        {
                            String tag = tagSubject.text()+" ";
                            tags += tag;
                        }
                    }


                }

            }catch (IOException e)
            {
                e.printStackTrace();
            }
            return listChapter;
        }

        @Override
        protected void onPostExecute(ArrayList<Chapter> chapters) {
            super.onPostExecute(chapters);
            tvTags.setText(tags);
            tv_author = findViewById(R.id.same_author);
            tv_author.setText(author_name);
            detailAdapter.notifyDataSetChanged();
        }
    }
}
