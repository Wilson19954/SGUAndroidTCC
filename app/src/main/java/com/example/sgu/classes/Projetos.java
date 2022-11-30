package com.example.sgu.classes;

public class Projetos {

    private String cod, desc, custo, tag, nome, doc_user, img;
    long data;

    public Projetos( String cod, String desc, String custo, String tag, String nome, String doc_user, long data, String img) {
        this.cod = cod;
        this.desc = desc;
        this.custo = custo;
        this.tag = tag;
        this.nome = nome;
        this.doc_user = doc_user;
        this.data = data;
        this.img = img;
    }

    public Projetos(){}

    public String getImg() {return img;}

    public void setImg(String img) {this.img = img;}

    public String getDesc() {return desc;}

    public void setDesc(String desc) {this.desc = desc;}

    public String getCusto() {return custo;}

    public void setCusto(String custo) {this.custo = custo;}

    public String getTag() {return tag;}

    public void setTag(String tag) {this.tag = tag;}

    public String getNome() {return nome;}

    public void setNome(String nome) {this.nome = nome;}

    public String getDoc_user() {return doc_user;}

    public void setDoc_user(String doc_user) {this.doc_user = doc_user;}

    public long getData() {return data;}

    public void setData(long data) {this.data = data;}

    public String getCod() {return cod;}

    public void setCod(long data) {this.cod = cod;}

}
