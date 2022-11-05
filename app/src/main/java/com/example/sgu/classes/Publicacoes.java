package com.example.sgu.classes;

public class Publicacoes {

    private int cod;
    private String img, desc, like, tag, doc_user;
    private long data;

    public Publicacoes(String desc, int cod, String doc_user, String tag, String like, String img, long data) {
        this.cod = cod;
        this.img = img;
        this.desc = desc;
        this.like = like;
        this.tag = tag;
        this.doc_user = doc_user;
        this.data = data;
    }

    public Publicacoes() {    }

    public int getCod() {return cod;}

    public void setCod(int cod) {this.cod = cod;}

    public String getImg() {return img;}

    public void setImg(String img) {this.img = img;}

    public String getDesc() {return desc;}

    public void setDesc(String desc) {this.desc = desc;}

    public String getLike() {return like;}

    public void setLike(String like) {this.like = like;}

    public String getTag() {return tag;}

    public void setTag(String tag) {this.tag = tag;}

    public String getDoc_user() {return doc_user;}

    public void setDoc_user(String doc_user) {this.doc_user = doc_user;}

    public long getData() {return data;}

    public void setData(long data) {this.data = data;}

}
