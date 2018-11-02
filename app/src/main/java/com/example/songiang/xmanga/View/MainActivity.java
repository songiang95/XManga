package com.example.songiang.xmanga.View;

import android.app.SearchManager;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.support.v7.widget.SearchView;


import com.example.songiang.xmanga.R;

public class MainActivity extends AppCompatActivity {
    private String[] list_type;
    private ListView leftDrawerList;
    private int currentPosition=0;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list_type = getResources().getStringArray(R.array.list_type);
        leftDrawerList = (ListView) findViewById(R.id.left_drawer);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        leftDrawerList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_activated_1, list_type));
        leftDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        if (savedInstanceState ==null)
        {
            selectItem(currentPosition);
            setActionBarTitle(currentPosition);
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =(SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false);
        searchView.setSubmitButtonEnabled(true);
        return true;
    }

    //Set click listener
  private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
            currentPosition = position;


        }
    }//end DrawerItemClickListener
    private void selectItem(int position)
    {
        Fragment fragment = new ListMangaFragment();
        Bundle bundle = new Bundle();
        String url;
        switch (position) {
            case 1:
                url="https://hentaivn.net/tieu-diem.html";
                break;
            case 2:
                url="https://hentaivn.net/the-loai-37-full_color.html";
                break;
            case 3:
                url="https://hentaivn.net/the-loai-99-khong_che.html";
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
                url ="https://hentaivn.net/the-loai-250-sweating.html";
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
                url="https://hentaivn.net/danh-sach.html";

        }
        bundle.putString("url",url);
        fragment.setArguments(bundle);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame,fragment);
        ft.addToBackStack(null);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
        //set action bar title
        setActionBarTitle(position);

        drawerLayout.closeDrawer(leftDrawerList);
    }

    private void setActionBarTitle(int position) {
        String title = list_type[position];
        getSupportActionBar().setTitle(title);
    }
}//end MainActivity