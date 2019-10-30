package com.example.songiang.xmanga.Adapter;

import android.app.Activity;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.songiang.xmanga.Model.Chapter;
import com.example.songiang.xmanga.R;
import com.example.songiang.xmanga.View.ReadingActivity;

import java.util.ArrayList;

public class DetailAdapter extends RecyclerView.Adapter<DetailAdapter.ChapterHolder> {
    private ArrayList<Chapter> listChapter;
    private Activity activity;

    public DetailAdapter (Activity activity, ArrayList<Chapter> listChapter)
    {
        this.activity = activity;
        this.listChapter = listChapter;
    }

    class ChapterHolder extends RecyclerView.ViewHolder{
        private TextView tvChapter;
        public ChapterHolder(View itemView)
        {
            super(itemView);
            tvChapter = (TextView) itemView.findViewById(R.id.chapter_number);
        }
    }

    @NonNull
    @Override
    public ChapterHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cv_chapter,viewGroup,false);
        return new ChapterHolder(view);
    }

    @Override
    public void onBindViewHolder(final DetailAdapter.ChapterHolder holder, final int position) {
        final Chapter chapter = listChapter.get(position);
        holder.tvChapter.setText(chapter.getName());
        holder.tvChapter.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                activity.startActivity(new Intent(activity,ReadingActivity.class).putExtra("url",chapter.getUrl()));
            }
        });


    }

    @Override
    public int getItemCount()
    {
        return listChapter.size();
    }


}
