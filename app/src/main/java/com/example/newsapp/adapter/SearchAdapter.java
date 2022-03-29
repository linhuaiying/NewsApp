package com.example.newsapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newsapp.R;
import com.example.newsapp.View.showPublishContentView.showPublishContentActivity;
import com.example.newsapp.bean.UsersNewsbean.UsersNews;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {
    private List<UsersNews> newsList;
    public SearchAdapter(List<UsersNews> newsList) {
        this.newsList = newsList;
    }
    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_item_layout, parent, false);
        SearchViewHolder searchViewHolder = new SearchViewHolder(view);
        searchViewHolder.searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = searchViewHolder.getAdapterPosition();
                showPublishContentActivity.actionStart(parent.getContext(), newsList.get(position));
            }
        });
        return searchViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
        UsersNews usersNews = newsList.get(position);
        holder.title.setText(usersNews.getTitle());
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    static class SearchViewHolder extends RecyclerView.ViewHolder{
        View searchView;
        TextView title;
        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            searchView = itemView;
            title = itemView.findViewById(R.id.title);
        }

    }
}
