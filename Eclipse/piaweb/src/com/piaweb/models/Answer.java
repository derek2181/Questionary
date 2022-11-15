package com.piaweb.models;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;

public class Answer {
	
	
	private int ID_Respuesta;
	@NotEmpty(message = "Por favor ingrese una respuesta")
	@Length(max=255, message = "Ingrese menos de 255 caracteres")
	private String respuesta;
	private InputStream imagen;
	private Calendar fecha;	
	private boolean activo_eliminar;
	private boolean activo_editar; 
	private String ID_Usuario;
	private int ID_Pregunta;
	
	public Answer() {
		
	}
	public Answer(int iD_Respuesta, String respuesta, InputStream imagen, Calendar fecha, boolean activo_eliminar,
			boolean activo_editar, String iD_Usuario) {
		super();
		ID_Respuesta = iD_Respuesta;
		this.respuesta = respuesta;
		this.imagen = imagen;
		this.fecha = fecha;
		this.activo_eliminar = activo_eliminar;
		this.activo_editar = activo_editar;
		ID_Usuario = iD_Usuario;
	}
	public int getID_Respuesta() {
		return ID_Respuesta;
	}
	public void setID_Respuesta(int iD_Respuesta) {
		ID_Respuesta = iD_Respuesta;
	}
	public String getRespuesta() {
		return respuesta;
	}
	public void setRespuesta(String respuesta) {
		this.respuesta = respuesta;
	}
	public InputStream getImagen() {
		return imagen;
	}
	public void setImagen(InputStream imagen) {
		this.imagen = imagen;
	}
	public Calendar getFecha() {
		return fecha;
	}
	
	public String getFechaFormat() {
		SimpleDateFormat simpleFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy ");
		String resultado = simpleFormat.format(fecha.getTime());
		return resultado;
	}
	public void setFecha(Calendar fecha) {
		this.fecha = fecha;
	}
	public boolean isActivo_eliminar() {
		return activo_eliminar;
	}
	public void setActivo_eliminar(boolean activo_eliminar) {
		this.activo_eliminar = activo_eliminar;
	}
	public boolean isActivo_editar() {
		return activo_editar;
	}
	public void setActivo_editar(boolean activo_editar) {
		this.activo_editar = activo_editar;
	}
	public String getID_Usuario() {
		return ID_Usuario;
	}
	public void setID_Usuario(String iD_Usuario) {
		ID_Usuario = iD_Usuario;
	}
	public int getID_Pregunta() {
		return ID_Pregunta;
	}
	public void setID_Pregunta(int iD_Pregunta) {
		ID_Pregunta = iD_Pregunta;
	}
	
}
