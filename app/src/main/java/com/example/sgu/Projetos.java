package com.example.sgu;

public class Projetos {

    private String cod, desc, custo, tag, nome, doc_user, galeria;
    long data;

    public Projetos(String cod, String desc, String custo, String tag, String nome, String doc_user, String galeria) {
        this.cod = cod;
        this.desc = desc;
        this.custo = custo;
        this.tag = tag;
        this.nome = nome;
        this.doc_user = doc_user;
        this.galeria = galeria;
    }

    public String getCod() {return cod;}

    public void setCod(String cod) {this.cod = cod;}

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

    public String getGaleria() {return galeria;}

    public void setGaleria(String galeria) {this.galeria = galeria;}

    public long getData() {return data;}

    public void setData(long data) {this.data = data;}

}
