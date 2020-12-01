package com.beeline.task;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.beeline.task.models.Article;
import com.beeline.task.utils.Constants;
import com.squareup.picasso.Picasso;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class ClickedArticleActivity extends AppCompatActivity {

    ImageView articleImage;
    TextView articleTitle,articleAuthor,publishDate,articleContent;
    DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH);
    DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyy", Locale.ENGLISH);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clicked_article);
        articleImage = findViewById(R.id.articleImage);
        articleTitle = findViewById(R.id.articleTitle);
        articleAuthor = findViewById(R.id.articleAuthor);
        publishDate = findViewById(R.id.publishDate);
        articleContent = findViewById(R.id.articleContent);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Article article = (Article) getIntent().getSerializableExtra(Constants.CLICKED_ARTICLE);

        assert article != null;
        Picasso.get()
                .load(article.getUrlToImage())
                .fit()
                .error(R.drawable.no_image)
                .placeholder(R.drawable.progres_animation)
                .into(articleImage);

        articleTitle.setText(article.getTitle());
        articleAuthor.setText(article.getAuthor());
        publishDate.setText(formatDate(article.getPublishedAt()));
        articleContent.setText(article.getContent());
        articleContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(article.getUrl()));
                startActivity(browserIntent);
            }
        });

    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    private String formatDate(String pattern){
        LocalDate date = LocalDate.parse(pattern, inputFormatter);
        return outputFormatter.format(date);
    }

}