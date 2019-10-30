package com.example.songiang.xmanga.View;

import android.app.SearchManager;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.songiang.xmanga.Adapter.MangaAdapter;
import com.example.songiang.xmanga.Model.Manga;
import com.example.songiang.xmanga.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rv_home)
    RecyclerView rvHome;
    @BindView(R.id.left_drawer)
    ListView leftDrawer;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.pb_loading)
    ProgressBar pbLoading;
    private String[] mListMangaType;
    private int mCurrentPosition = 0;
    private List<Manga> mListManga;
    private MangaAdapter mAdapter;
    private String mCurrentUrl;
    private LoadHomeTask mLoadTask;
    private int pageNext = 2;
    private int pageMax, visibleItems, totalItems, firstVisibleItems;
    private boolean isScrolling;
    private boolean isLastManga;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mListManga = new ArrayList<>();

        initLeftDrawer();
        initRv();
        setSupportActionBar(toolbar);
        if (savedInstanceState == null) {
            mCurrentUrl = getType(mCurrentPosition);
            setActionBarTitle(mCurrentPosition);
            mLoadTask = new LoadHomeTask(MainActivity.this);
            mLoadTask.execute(mCurrentUrl);
        }
    }

    private void initRv() {
        mLayoutManager = new GridLayoutManager(this, 3);

        rvHome.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    if (!isLastManga)
                        isScrolling = true;
                    else {
                        isScrolling = false;
                        if (!rvHome.canScrollVertically(1))
                            Toast.makeText(MainActivity.this, "No more chapter", Toast.LENGTH_SHORT).show();

                    }
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                visibleItems = mLayoutManager.getChildCount();
                totalItems = mLayoutManager.getItemCount();
                firstVisibleItems = ((LinearLayoutManager) Objects.requireNonNull(rvHome.getLayoutManager())).findFirstVisibleItemPosition();
                if (isScrolling && (visibleItems + firstVisibleItems) == totalItems) {
                    isScrolling = false;
                    updateRecycleView();
                }
            }
        });
    }

    private void updateRecycleView() {

        String page = "";
        if (mCurrentUrl.contains("key=")) {
            page = "&page=";
        } else
            page = "?page=";
        page = page + pageNext;
        new LoadHomeTask(this).execute(mCurrentUrl + page);
        pageNext++;
        Log.d("abba", "updateRecycleView: " + mCurrentUrl + page);
    }

    private void initLeftDrawer() {
        mListMangaType = getResources().getStringArray(R.array.list_type);
        leftDrawer.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_activated_1, mListMangaType));
        leftDrawer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                pbLoading.setVisibility(View.VISIBLE);
                mListManga.clear();
                drawerLayout.closeDrawer(Gravity.RIGHT, true);
                mCurrentUrl = getType(position);
                mCurrentPosition = position;
                setActionBarTitle(mCurrentPosition);
                if (mLoadTask != null) {
                    mLoadTask.cancel(true);
                    mLoadTask = null;
                    mLoadTask = new LoadHomeTask(MainActivity.this);
                    mLoadTask.execute(mCurrentUrl);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(true);
        searchView.setSubmitButtonEnabled(true);
        return true;
    }

    private String getType(int position) {
        String url;
        switch (position) {
            case 1:
                url = "https://hentaivn.net/tieu-diem.html";
                break;
            case 2:
                url = "https://hentaivn.net/the-loai-37-full_color.html";
                break;
            case 3:
                url = "https://hentaivn.net/the-loai-99-khong_che.html";
                break;
            case 4:
                url = "https://hentaivn.net/the-loai-73-pregnant.html";
                break;
            case 5:
                url = "https://hentaivn.net/the-loai-69-nurse.html";
                break;
            case 6:
                url = "https://hentaivn.net/the-loai-253-dirty.html";
                break;
            case 7:
                url = "https://hentaivn.net/the-loai-211-old-man.html";
                break;
            case 8:
                url = "https://hentaivn.net/the-loai-98-rape.html";
                break;
            case 9:
                url = "https://hentaivn.net/the-loai-68-ntr.html";
                break;
            case 10:
                url = "https://hentaivn.net/the-loai-62-incest.html";
                break;
            case 11:
                url = "https://hentaivn.net/the-loai-103-mother.html";
                break;
            case 12:
                url = "https://hentaivn.net/the-loai-91-teacher.html";
                break;
            case 13:
                url = "https://hentaivn.net/the-loai-51-gangbang.html";
                break;
            case 14:
                url = "https://hentaivn.net/the-loai-243-father.html";
                break;
            case 15:
                url = "https://hentaivn.net/the-loai-126-milf.html";
                break;
            case 16:
                url = "https://hentaivn.net/the-loai-119-mature.html";
                break;
            case 17:
                url = "https://hentaivn.net/the-loai-82-slave.html";
                break;
            case 18:
                url = "https://hentaivn.net/the-loai-34-cheating.html";
                break;
            case 19:
                url = "https://hentaivn.net/the-loai-250-sweating.html";
                break;
            case 20:
                url = "https://hentaivn.net/the-loai-2-cousin.html";
                break;
            case 21:
                url = "https://hentaivn.net/the-loai-59-housewife.html";
                break;
            case 22:
                url = "https://hentaivn.net/the-loai-74-school_uniform.html";
                break;
            case 23:
                url = "https://hentaivn.net/the-loai-75-schoolgirl.html";
                break;
            default:
                url = "https://hentaivn.net/danh-sach.html";
        }
        return url;
    }

    private void setActionBarTitle(int position) {
        String title = mListMangaType[position];
        Objects.requireNonNull(getSupportActionBar()).setTitle(title);
    }

    private static class LoadHomeTask extends AsyncTask<String, Void, List<Manga>> {

        private WeakReference<MainActivity> mainActivityWeakReference;

        LoadHomeTask(MainActivity activity) {
            mainActivityWeakReference = new WeakReference<>(activity);
        }

        @Override
        protected List<Manga> doInBackground(String... strings) {
            List<Manga> listManga = new ArrayList<>();
            MainActivity activity = mainActivityWeakReference.get();
            try {
                Document document = Jsoup.connect(strings[0]).get();
                if (document != null) {
                    Elements page = document.select(".pagination");

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
            if (listManga.isEmpty()) {
                mainActivityWeakReference.get().isLastManga = true;
            }
            return listManga;
        }

        @Override
        protected void onPostExecute(List<Manga> mangas) {
            MainActivity mainActivity = mainActivityWeakReference.get();
            mainActivity.pbLoading.setVisibility(View.GONE);
            super.onPostExecute(mangas);
            if (mangas.isEmpty()) {
                Toast.makeText(mainActivity, "No more manga", Toast.LENGTH_SHORT).show();
            } else if (mainActivity.mListManga.isEmpty()) {
                mainActivity.mListManga.addAll(mangas);
                mainActivity.mAdapter = new MangaAdapter(mainActivity, (ArrayList<Manga>) mainActivity.mListManga);
                mainActivity.rvHome.setAdapter(mainActivity.mAdapter);
                mainActivity.rvHome.setLayoutManager(mainActivity.mLayoutManager);
            } else {
                int insertPosition = mainActivity.mListManga.size();
                mainActivity.mListManga.addAll(mangas);
                mainActivity.mAdapter.notifyItemRangeInserted(insertPosition, mangas.size());
            }
        }
    }
}//end MainActivity