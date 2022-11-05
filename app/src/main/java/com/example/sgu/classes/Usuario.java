package com.example.sgu.classes;

public class Usuario {
    private int id;
    private String endereco, nome, desc, doc, telefone, email, img, tipo, senha;

    public Usuario(int id, String endereco, String nome, String desc, String doc, String telefone, String email, String img, String tipo, String senha) {
        this.id = id;
        this.endereco = endereco;
        this.nome = nome;
        this.desc = desc;
        this.doc = doc;
        this.telefone = telefone;
        this.email = email;
        this.img = img;
        this.tipo = tipo;
        this.senha = senha;
    }

    public Usuario(String endereco, String nome, String desc, String doc, String telefone, String email, String img, String tipo, String senha) {
        this.endereco = endereco;
        this.nome = nome;
        this.desc = desc;
        this.doc = doc;
        this.telefone = telefone;
        this.email = email;
        this.img = img;
        this.tipo = tipo;
        this.senha = senha;
    }

    public Usuario(){}

    public int getId() {return id;}

    public void setId(int id) {this.id = id;}

    public String getEndereco() {return endereco;}

    public void setEndereco(String endereco) {this.endereco = endereco;}

    public String getNome() {return nome;}

    public void setNome(String nome) {this.nome = nome;}

    public String getDesc() {return desc;}

    public void setDesc(String desc) {this.desc = desc;}

    public String getDoc() {return doc;}

    public void setDoc(String doc) {this.doc = doc;}

    public String getTelefone() {return telefone;}

    public void setTelefone(String telefone) {this.telefone = telefone;}

    public String getEmail() {return email;}

    public void setEmail(String email) {this.email = email;}

    public String getImg() {return img;}

    public void setImg(String img) {this.img = img;}

    public String getTipo() {return tipo;}

    public void setTipo(String tipo) {this.tipo = tipo;}

    public String getSenha() {return senha;}

    public void setSenha(String senha) {this.senha = senha;}

}
