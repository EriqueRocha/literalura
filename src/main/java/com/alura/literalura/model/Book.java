package com.alura.literalura.model;

import java.util.List;
import java.util.Map;


public class Book {

    private Long id;
    private String title;

    private List<String> subjects;

    private List<Person> authors;

    private List<Person> translators;

    private List<String> bookshelves;

    private List<String> languages;

    private Boolean copyright;

    private String mediaType;

    private Map<String, String> formats;

    private long download_count;

//-------------------------------------------------------


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<String> subjects) {
        this.subjects = subjects;
    }

    public List<Person> getAuthors() {
        return authors;
    }

    public void setAuthors(List<Person> authors) {
        this.authors = authors;
    }

    public List<Person> getTranslators() {
        return translators;
    }

    public void setTranslators(List<Person> translators) {
        this.translators = translators;
    }

    public List<String> getBookshelves() {
        return bookshelves;
    }

    public void setBookshelves(List<String> bookshelves) {
        this.bookshelves = bookshelves;
    }

    public List<String> getLanguages() {
        return languages;
    }

    public void setLanguages(List<String> languages) {
        this.languages = languages;
    }

    public Boolean getCopyright() {
        return copyright;
    }

    public void setCopyright(Boolean copyright) {
        this.copyright = copyright;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public Map<String, String> getFormats() {
        return formats;
    }

    public void setFormats(Map<String, String> formats) {
        this.formats = formats;
    }

    public long getDownload_count() {
        return download_count;
    }

    public void setDownload_count(long download_count) {
        this.download_count = download_count;
    }
}
