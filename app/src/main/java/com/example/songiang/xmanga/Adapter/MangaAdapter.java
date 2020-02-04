package com.example.songiang.xmanga.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.songiang.xmanga.Model.Manga;
import com.example.songiang.xmanga.R;
import com.example.songiang.xmanga.View.MangaDetailActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MangaAdapter extends RecyclerView.Adapter<MangaAdapter.MangaHolder> {
    private Context mContext;
    private List<Manga> listManga;

    public MangaAdapter(Context context, List<Manga> list) {
        this.mContext = context;
        this.listManga = list;
    }

    @Override
    public MangaHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_manga, parent, false);
        return new MangaHolder(view);
    }

    @Override
    public void onBindViewHolder(final MangaHolder holder, int position) {
        final Manga manga = listManga.get(position);
        holder.tvMangaName.setText(manga.getName());
        Glide.with(mContext)
                .load(manga.getImg())
                .asBitmap()
                .atMost()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .animate(android.R.anim.fade_in)
                .approximate()
                .into(holder.ivCover);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mContext.startActivity(new Intent(mContext, MangaDetailActivity.class).putExtra("MANGA", manga));
            }
        });

    }

    @Override
    public int getItemCount() {
        return listManga.size();
    }


    class MangaHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_cover)
        ImageView ivCover;
        @BindView(R.id.tv_manga_name)
        TextView tvMangaName;

        public MangaHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
