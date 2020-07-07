package com.example.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerAdaptor extends RecyclerView.Adapter<RecyclerAdaptor.ViewHolder>{
    List<Article> articles;
    Context context;
    Montrealfragment mcontext;
    Vaccinefragment vcontext;
    public RecyclerAdaptor(List<Article> articles,Montrealfragment montrealfragment) {
        this.articles = articles;
        this.mcontext = montrealfragment;
    }
    public RecyclerAdaptor(List<Article> articles, Vaccinefragment vaccinefragment) {
        this.articles = articles;
        this.vcontext = vaccinefragment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View articleheadings= layoutInflater.inflate(R.layout.articleheadings, parent, false);
        ViewHolder viewHolder = new ViewHolder(articleheadings);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
            holder.textView.setText(articles.get(position).getArticle());
            holder.textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), ExpandArticle.class);
                    Log.i("expand article","position"+articles.get(position).getExpandarticle());
                    String expand = articles.get(position).getExpandarticle();
                    intent.putExtra("expandarticle",expand);
                    v.getContext().startActivity(intent);
                }
            });

    }

    @Override
    public int getItemCount() {
        return articles.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView textView;
        CardView cardView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.cardView = (CardView)itemView.findViewById(R.id.cardview);
            this.textView = (TextView)itemView.findViewById(R.id.headings);
        }
    }
}
