package com.example.songiang.xmanga.View;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
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
    private String mCurrentUrl = "https://hentaivn.net/danh-sach.html";
    private int pageNext = 2;
    private int pageMax, visibleItems, totalItems, firstVisibleItems;
    private boolean isScrolling;
    private boolean isLastManga;
    private RecyclerView.LayoutManager mLayoutManager;
    private LoadHomeThread mLoadHomeThread;
    private final int SHOW_LOADING_PROGRESS_MSG = 1;
    private final int SHOW_MANGA_MSG = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mListManga = new ArrayList<>();

        initLeftDrawer();
        initRv();
        setSupportActionBar(toolbar);
        mLoadHomeThread = new LoadHomeThread();
        mLoadHomeThread.start();
//        mLoadHomeThread.startLoad(mCurrentUrl);


//        if (savedInstanceState == null) {
//            mCurrentUrl = getType(mCurrentPosition);
//            setActionBarTitle(mCurrentPosition);
//            mLoadTask = new LoadHomeTask(MainActivity.this);
//            mLoadTask.execute(mCurrentUrl);
//        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLoadHomeThread.exit();

    }

    private void initRv() {
        mLayoutManager = new GridLayoutManager(this, 3);
        rvHome.setLayoutManager(mLayoutManager);
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
        mLoadHomeThread.startLoad(mCurrentUrl + page);
        pageNext++;
    }

    private void initLeftDrawer() {
        mListMangaType = getResources().getStringArray(R.array.list_type);
        leftDrawer.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_activated_1, mListMangaType));
        leftDrawer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mListManga.clear();
                drawerLayout.closeDrawer(Gravity.RIGHT, true);
                mCurrentUrl = getType(position);
                mCurrentPosition = position;
                setActionBarTitle(mCurrentPosition);
                mLoadHomeThread.startLoad(mCurrentUrl);

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


    private class LoadHomeThread extends Thread {
        private Handler mBackgroundHandler;
        private Handler mUiHandler;

        @Override
        public void run() {
            super.run();
            Looper.prepare();
            mBackgroundHandler = new Handler();
            mUiHandler = new Handler(getMainLooper()) {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    switch (msg.what) {
                        case SHOW_LOADING_PROGRESS_MSG:
                            pbLoading.setVisibility(View.VISIBLE);
                            break;
                        case SHOW_MANGA_MSG:
                            pbLoading.setVisibility(View.GONE);
                            ArrayList mangaList = (ArrayList) msg.obj;
                            mListManga.addAll(mangaList);
                            if (mAdapter == null) {
                                mAdapter = new MangaAdapter(MainActivity.this, mListManga);
                                rvHome.setAdapter(mAdapter);
                            } else {
                                mAdapter.notifyItemRangeInserted(mListManga.size() - mangaList.size(), mangaList.size());
                            }
                            break;
                    }
                }
            };
            startLoad(mCurrentUrl);
            Looper.loop();
        }

        public void startLoad(String url) {
            mBackgroundHandler.post(() -> {
                Message showProgressMsg = mUiHandler.obtainMessage(SHOW_LOADING_PROGRESS_MSG);
                mUiHandler.sendMessage(showProgressMsg);
                List<Manga> listManga = new ArrayList<>();
                try {
                    Document document = Jsoup.connect(url).get();
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
                } finally {
                    if (!listManga.isEmpty()) {
                        Message showMangaMsg = mUiHandler.obtainMessage(SHOW_MANGA_MSG, listManga);
                        mUiHandler.sendMessage(showMangaMsg);
                    }
                }
            });
        }

        public void exit() {
            mBackgroundHandler.getLooper().quit();
        }
    }

}//end MainActivity