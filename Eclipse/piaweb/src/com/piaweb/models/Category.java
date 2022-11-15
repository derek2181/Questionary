package com.piaweb.models;

public class Category {
private int ID_Categoria;

public Category(){
	
}

private String nombre;

public Category(int iD_Categoria, String nombre) {
	
	ID_Categoria = iD_Categoria;
	this.nombre = nombre;
}
public int getID_Categoria() {
	return ID_Categoria;
}
public void setID_Categoria(int iD_Categoria) {
	ID_Categoria = iD_Categoria;
}
public String getNombre() {
	return nombre;
}
public void setNombre(String nombre) {
	this.nombre = nombre;
}
}
