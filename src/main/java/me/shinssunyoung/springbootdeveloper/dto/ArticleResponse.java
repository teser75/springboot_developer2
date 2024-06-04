package me.shinssunyoung.springbootdeveloper.dto;

import lombok.Getter;
import me.shinssunyoung.springbootdeveloper.domain.Article;

@Getter
public class ArticleResponse {

    private final String title;
    private final String content;

    public ArticleResponse( Article artice ){
        this.title = artice.getTitle();
        this.content = artice.getContent();
    }

}
