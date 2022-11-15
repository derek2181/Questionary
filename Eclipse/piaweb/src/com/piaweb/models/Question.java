package com.piaweb.models;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class Question {

	private int ID_Pregunta;
	@NotEmpty (message = "Por favor rellene este campo")
	private String encabezado;
	private String descripcion;
	private Calendar fecha;
	private InputStream imagen;
	private String ID_Usuario;
	@NotNull 
	private int ID_Categoria;
	
	private boolean activo;
	private boolean activo_editar;
	public Question() {
		
	}
	public Question(int iD_Pregunta, String encabezado, String descripcion, Calendar fecha, InputStream imagen,
			String iD_Usuario, int iD_Categoria, boolean activo) {
		super();
		ID_Pregunta = iD_Pregunta;
		this.encabezado = encabezado;
		this.descripcion = descripcion;
		this.fecha = fecha;
		this.imagen = imagen;
		ID_Usuario = iD_Usuario;
		ID_Categoria = iD_Categoria;
		this.activo = activo;
	}
	
	public boolean isActivo_editar() {
		return activo_editar;
	}
	public void setActivo_editar(boolean activo_editar) {
		this.activo_editar = activo_editar;
	}
	public int getID_Pregunta() {
		return ID_Pregunta;
	}
	public void setID_Pregunta(int iD_Pregunta) {
		ID_Pregunta = iD_Pregunta;
	}
	public String getEncabezado() {
		return encabezado;
	}
	public void setEncabezado(String encabezado) {
		this.encabezado = encabezado;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public Calendar getFecha() {
		return fecha;
	}
	public String getFechaFormat() {
		SimpleDateFormat simpleFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String resultado = simpleFormat.format(fecha.getTime());
		return resultado;
	}
	
	public void setFecha(Calendar fecha) {
		this.fecha = fecha;
	}
	public InputStream getImagen() {
		return imagen;
	}
	public void setImagen(InputStream imagen) {
		this.imagen = imagen;
	}
	public String getID_Usuario() {
		return ID_Usuario;
	}
	public void setID_Usuario(String iD_Usuario) {
		ID_Usuario = iD_Usuario;
	}
	public int getID_Categoria() {
		return ID_Categoria;
	}
	public void setID_Categoria(int iD_Categoria) {
		ID_Categoria = iD_Categoria;
	}
	public boolean isActivo() {
		return activo;
	}
	public void setActivo(boolean activo) {
		this.activo = activo;
	}
	
}
