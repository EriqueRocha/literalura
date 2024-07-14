package com.alura.literalura.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class BookEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<PersonEntity> authors;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> languages;

    private long download_count;


    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<PersonEntity> getAuthors() {
        return authors;
    }

    public void setAuthors(List<PersonEntity> authors) {
        this.authors = authors;
    }

    public List<String> getLanguages() {
        return languages;
    }

    public void setLanguages(List<String> languages) {
        this.languages = languages;
    }

    public long getDownload_count() {
        return download_count;
    }

    public void setDownload_count(long download_count) {
        this.download_count = download_count;
    }
}
