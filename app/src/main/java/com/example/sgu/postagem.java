package com.example.sgu;

public class postagem {

    private String nome, post;
    private int img;

    public postagem(){}

    public postagem(String nome, String post, int img) {
        this.nome = nome;
        this.post = post;
        this.img = img;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }
}
