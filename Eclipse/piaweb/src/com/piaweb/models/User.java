package com.piaweb.models;

import java.io.InputStream;

import java.util.Calendar;

import javax.validation.constraints.*;

import org.hibernate.validator.constraints.Length;

import com.piaweb.data.Encoder;


public class User {
	private Encoder encoder=new Encoder();
	@NotEmpty(message="No se permite este campo vacio")
	private String ID_Usuario;
	@NotEmpty(message="No se permite este campo vacio")
	
	@Pattern(message = "La contraseña debe contener al menos"
			+ " una letra minuscula, una letra mayuscula, un numero, un signo de puntuacion, debe contener entre 8 y 16 caracteres",
			regexp = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[\\.\\;\\-\\:@#$%]).{8,16})")
	private String contra;
	@NotEmpty(message="No se permite este campo vacio")
	@Email(message="Correo no valido")
	@Length(max=40, message="Ingrese menos de 40 caracteres")
	private String correo;
	@NotEmpty(message="No se permite este campo vacio")
	@Length(max=20, message="Ingrese menos de 20 caracteres")
	private String nombre;
	@NotEmpty(message="No se permite este campo vacio")
	@Length(max=20, message="Ingrese menos de 20 caracteres")
	private String apellidoP;
	@NotEmpty(message="No se permite este campo vacio")
	@Length(max=20, message="Ingrese menos de 20 caracteres")
	private String apellidoM;

	@Past(message="La fecha debe ser una fecha pasada")
	private Calendar fecha_nacimiento;
	
	private String contraNueva;
	private String confirmarContrasena;
	
	private InputStream imagen;
	private boolean activo;
	public User() {
		
	}
	
	public User(String nombre, String contra, String correo, String apellidoP, String apellidoM,
			InputStream imagen,boolean activo) {
		super();
		//Voy a meter una contra nueva ya validada
		
			this.contra = encoder.encodeString(contra);
			this.correo = encoder.encodeString(correo);
			this.apellidoP = encoder.encodeString(apellidoP);
			this.apellidoM = encoder.encodeString(apellidoM);
			this.activo = activo;
			this.nombre=encoder.encodeString(nombre);	
			this.imagen=imagen;
		
	}
	
	public User(String iD_Usuario,String nombre, String contra, String correo, String apellidoP, String apellidoM,
			Calendar fecha_nacimiento, boolean activo) {
		super();
		ID_Usuario = encoder.encodeString(iD_Usuario);
		this.contra = encoder.encodeString(contra);
		this.correo = encoder.encodeString(correo);
		this.apellidoP = encoder.encodeString(apellidoP);
		this.apellidoM = encoder.encodeString(apellidoM);
		this.fecha_nacimiento = fecha_nacimiento;
		this.activo = activo;
		this.nombre=encoder.encodeString(nombre);
	}
	
	public String getConfirmarContrasena() {
		return confirmarContrasena;
	}

	public void setConfirmarContrasena(String confirmarContrasena) {
		this.confirmarContrasena = confirmarContrasena;
	}

	public String getID_Usuario() {
		return ID_Usuario;
	}
	public void setID_Usuario(String iD_Usuario) {
		ID_Usuario = iD_Usuario;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getContra() {
		return contra;
	}
	public void setContra(String contra) {
		this.contra = contra;
	}
	public String getCorreo() {
		return correo;
	}
	public void setCorreo(String correo) {
		this.correo = correo;
	}
	public String getApellidoP() {
		return apellidoP;
	}
	public void setApellidoP(String apellidoP) {
		this.apellidoP = apellidoP;
	}
	public String getApellidoM() {
		return apellidoM;
	}
	public void setApellidoM(String apellidoM) {
		this.apellidoM =apellidoM;
	}
	public Calendar getFecha_nacimiento() {
		return fecha_nacimiento;
	}
	public void setFecha_nacimiento(Calendar fecha_nacimiento) {
		this.fecha_nacimiento = fecha_nacimiento;
	}
	public boolean isActivo() {
		return activo;
	}
	public void setActivo(boolean activo) {
		this.activo = activo;
	}
	
	public String getContraNueva() {
		return contraNueva;
	}

	public void setContraNueva(String contraNueva) {
		this.contraNueva = contraNueva;
	}

	public InputStream getImagen() {
		return imagen;
	}

	public void setImagen(InputStream imagen) {
		this.imagen = imagen;
	
	
}
}