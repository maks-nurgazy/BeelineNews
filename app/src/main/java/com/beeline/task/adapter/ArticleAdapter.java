package com.beeline.task.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.beeline.task.R;
import com.beeline.task.models.Article;
import com.beeline.task.utils.ItemClickListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder> {

    private List<Article> articleList = new ArrayList<>();
    private ItemClickListener itemClickListener;

    public ArticleAdapter(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }


    public void setItems(Collection<Article> articles) {
        articleList.clear();
        articleList.addAll(articles);
        notifyDataSetChanged();
    }

    public void clearItems() {
        articleList.clear();
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.news_list_item, parent, false);
        return new ArticleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleViewHolder holder, int position) {
        holder.bind(articleList.get(position));
    }

    @Override
    public int getItemCount() {
        return articleList.size();
    }

    class ArticleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tvTitle;
        private TextView tvDescription;
        private ImageView newsImage;

        public ArticleViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            newsImage = itemView.findViewById(R.id.newsImage);
            itemView.setOnClickListener(this);
        }

        public void bind(Article article) {
            tvTitle.setText(article.getTitle());
            tvDescription.setText(article.getDescription());
            Picasso.get()
                    .load(article.getUrlToImage())
                    .fit()
                    .centerCrop()
                    .placeholder(R.drawable.progres_animation)
                    .error(R.drawable.no_image)
                    .into(newsImage);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            itemClickListener.onItemClicked(position);
        }
    }

}
