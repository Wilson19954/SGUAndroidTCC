package com.example.sgu;

public class Publi {

    public String getImg_pub() {
        return img_pub;
    }

    public void setImg_pub(String img_pub) {
        this.img_pub = img_pub;
    }

    public String getDesc_pub() {
        return desc_pub;
    }

    public void setDesc_pub(String desc_pub) {
        this.desc_pub = desc_pub;
    }

    public int getLike_pub() {
        return like_pub;
    }

    public void setLike_pub(int like_pub) {
        this.like_pub = like_pub;
    }

    public String getTag_pub() {
        return tag_pub;
    }

    public void setTag_pub(String tag_pub) {
        this.tag_pub = tag_pub;
    }

    public long getData_pub() {
        return data_pub;
    }

    public void setData_pub(long data_pub) {
        this.data_pub = data_pub;
    }

    public String getNome_user() {
        return nome_user;
    }

    public void setNome_user(String nome_user) {
        this.nome_user = nome_user;
    }

    public String getDoc_user() {
        return doc_user;
    }

    public void setDoc_user(String doc_user) {
        this.doc_user = doc_user;
    }

    public String getImg_user() {
        return img_user;
    }

    public void setImg_user(String img_user) {
        this.img_user = img_user;
    }

    public String getTipo_user() {
        return tipo_user;
    }

    public void setTipo_user(String tipo_user) {
        this.tipo_user = tipo_user;
    }

    private String img_pub, desc_pub;
    private int like_pub;
    private String tag_pub;
    private long data_pub;
    private String nome_user, doc_user, img_user, tipo_user;


    public Publi(String img_pub, String desc_pub, int like_pub, String tag_pub, long data_pub, String nome_user, String doc_user, String img_user, String tipo_user) {
        this.img_pub = img_pub;
        this.desc_pub = desc_pub;
        this.like_pub = like_pub;
        this.tag_pub = tag_pub;
        this.data_pub = data_pub;
        this.nome_user = nome_user;
        this.doc_user = doc_user;
        this.img_user = img_user;
        this.tipo_user = tipo_user;
    }
}
