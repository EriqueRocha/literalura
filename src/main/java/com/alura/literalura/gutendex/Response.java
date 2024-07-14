package com.alura.literalura.gutendex;

import com.alura.literalura.model.Book;

import java.util.List;

public class Response {

    private int count;
    private String next;
    private String previous;
    private List<Book> results;

//-------------------------------------------------------------


    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public String getPrevious() {
        return previous;
    }

    public void setPrevious(String previous) {
        this.previous = previous;
    }

    public List<Book> getResults() {
        return results;
    }

    public void setResults(List<Book> results) {
        this.results = results;
    }
}
