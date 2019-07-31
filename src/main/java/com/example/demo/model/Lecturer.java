package com.example.demo.model;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "lecturer")
public class Lecturer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, length = 1024)
    private String imgSrc;

    @Column(nullable = false)
    private Long score;

    @Column(unique = true, updatable = false)
    String uId = UUID.randomUUID().toString();

    public Lecturer() {
        this.score = 0L;
    }

    public Lecturer(String name, String imgSrc) {
        this.name = name;
        this.imgSrc = imgSrc;
        this.score = 0L;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgSrc() {
        return imgSrc;
    }

    public void setImgSrc(String imgSrc) {
        this.imgSrc = imgSrc;
    }

    public Long getScore() {
        return score;
    }

    public void setScore(Long score) {
        this.score = score;
    }

    public void increaseScore(long val) {
        this.score += val;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    @Override
    public String toString() {
        return "Lecturer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", imgSrc='" + imgSrc + '\'' +
                ", score=" + score +
                ", uId='" + uId + '\'' +
                '}';
    }
}
