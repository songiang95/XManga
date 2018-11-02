package com.example.songiang.xmanga.View;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Toast;

import com.example.songiang.xmanga.Adapter.MangaAdapter;
import com.example.songiang.xmanga.Model.Manga;
import com.example.songiang.xmanga.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class ListMangaFragment extends Fragment {
    private ArrayList<Manga> listManga;
    private MangaAdapter mangaAdapter;
    private RecyclerView recyclerView;
    private int visibleItems,totalItems,firstVisibleItems;
    private int pageNext = 2;
    private int pageMax;
    private boolean isScrolling = false;
    private RecyclerView.LayoutManager layoutManager;
    private String url;
    public ListMangaFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_list_manga, container, false);
        url = getArguments().getString("url");
        recyclerView = rootView.findViewById(R.id.recycle_view);
        listManga = new ArrayList<Manga>();
        new DownloadTask().execute(url);
        mangaAdapter = new MangaAdapter(getActivity(),listManga);
        configRecyclerView();
        recyclerView.setAdapter(mangaAdapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    if (pageNext <= pageMax)
                        isScrolling = true;
                    else{
                        isScrolling=false;
                        if(!recyclerView.canScrollVertically(1))
                        Toast.makeText(getContext(),"No more chapter",Toast.LENGTH_SHORT).show();

                    }
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                visibleItems = layoutManager.getChildCount();
                totalItems = layoutManager.getItemCount();
                firstVisibleItems = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
                if (isScrolling&&(visibleItems+firstVisibleItems)==totalItems)
                {
                    isScrolling = false;
                    updateRecycleView();
                }


            }
        });
        return rootView;
    }

    private void updateRecycleView() {
            String page = "";
            if (url.contains("key=")) {
                page = "&page=";
            } else
                page = "?page=";
            page = page + Integer.toString(pageNext);
            new DownloadTask().execute(url + page);
            pageNext++;
            mangaAdapter.notifyDataSetChanged();

    }

    private void configRecyclerView(){
        layoutManager = new GridLayoutManager(getActivity(),3);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(layoutManager);
    }

    private class DownloadTask extends AsyncTask<String, Void, ArrayList<Manga>> {
        @Override
        protected ArrayList<Manga> doInBackground(String... strings) {


            try {
                Document document = Jsoup.connect(strings[0]).get();
                if (document != null) {
                    Elements page = document.select(".pagination");
                    for(Element element: page)
                    {
                        Element pageSubject = element.getElementsByAttribute("placeholder").first();
                        if (pageSubject!=null)
                        {
                            String num = pageSubject.attr("placeholder");
                            pageMax = Integer.parseInt(num);

                        }

                    }
                    Elements sub = document.select(".item");
                    for (Element element : sub) {
                        Manga manga = new Manga();
                        Element chapSubject = element.getElementsByTag("p").first();
                        Element imgSubject = element.getElementsByTag("img").first();
                        Element linkSubject = element.getElementsByTag("a").first();


                        //pasre to model
                        if (chapSubject != null) {
                            String chap = chapSubject.ownText();
                            chap = chap.substring(2);
                            manga.setChapterNum(chap);
                        }
                        if (imgSubject != null) {
                            String imgUrl = imgSubject.attr("src");
                            String name = imgSubject.attr("alt");
                            manga.setName(name);
                            manga.setImg(imgUrl);
                        }
                        if (linkSubject != null) {
                            String link = linkSubject.attr("href");
                            link = "https://hentaivn.net" + link;
                            manga.setUrl(link);
                        }

                        listManga.add(manga);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return  listManga;
        }

        @Override
        protected void onPostExecute(ArrayList<Manga> mangas) {
            super.onPostExecute(mangas);
            mangaAdapter.notifyDataSetChanged();
        }
    }
}
