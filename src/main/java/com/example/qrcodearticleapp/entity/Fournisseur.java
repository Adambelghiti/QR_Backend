package com.example.qrcodearticleapp.entity;


import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.List;

@Entity
public class Fournisseur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "fournisseur")
    @JsonManagedReference("fournisseurs-articles")
    private List<Article> articles;

    public String getName() {
        return name;
    }

    public void setId(Long fournisseurId) {
        id = fournisseurId;
    }

    public Long getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }
    // Getters and Setters

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }
}
