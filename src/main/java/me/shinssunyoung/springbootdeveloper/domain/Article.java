package me.shinssunyoung.springbootdeveloper.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Article {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id", updatable = false)
    private Long id;


    @Column( name="title", nullable=false)
    private String title;

    @Column( name="content" , nullable = false )
    private  String content;

    @CreatedDate
    @Column(name="created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name="updated_at")
    private LocalDateTime updatedAt;


    @Column(name="author" , nullable = false)
    private String author;



    @Builder
    public Article(String author, String title, String content){
        this.title = title;
        this.content = content;
        this.author = author;
    }

    public void update( String title, String content){
        this.title = title;
        this.content = content;
    }


    /*
    protected Article(){

    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

     */




}
