package com.piaweb.models;

import java.io.InputStream;
import java.util.Calendar;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import com.piaweb.data.Encoder;

public class CambiarDatos {
	private Encoder encoder=new Encoder();
	private String ID_Usuario;
	@NotEmpty(message="No se permite este campo vacio")
	@Length(min=8,max=16,message="La longitud debe ser entre 8 y 16 caracteres")
	@Pattern(message = "La contraseña debe contener al menos"
			+ " una letra minuscula, una letra mayuscula, un numero, un signo de puntuacion, debe contener entre 8 y 16 caracteres",
			regexp = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[\\.\\;\\-\\:@#$%]).{8,16})")
	private String contra;
	@NotEmpty(message="No se permite este campo vacio")
	@Length(max=40, message="Ingrese menos de 40 caracteres")
	@Email(message="Correo no valido")
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
	

	private InputStream imagen;
	
	private boolean activo;
	
	public void swapData(User user, CambiarDatos data) {
		//Aqui esta el error del encoding en el perfil
		user.setApellidoM(data.getApellidoM());
		user.setApellidoP(data.getApellidoP());
		user.setNombre(data.getNombre());
		user.setContra(data.getContra()==null?user.getContra():data.getContra());
		user.setCorreo(data.getCorreo());
		user.setFecha_nacimiento(data.getFecha_nacimiento());
		user.setImagen(data.getImagen());		
	}
	public CambiarDatos(String nombre, String contra, String correo, String apellidoP, String apellidoM,
			Calendar calendar,InputStream imagen,boolean activo) {
		super();
		this.contra = encoder.encodeString(contra);
		this.correo = encoder.encodeString(correo);
		this.nombre = encoder.encodeString(nombre);
		this.apellidoP = encoder.encodeString(apellidoP);
		this.apellidoM = encoder.encodeString(apellidoM);
		this.fecha_nacimiento=calendar;
		this.imagen=imagen;
	}


	public InputStream getImagen() {
		return imagen;
	}

	public void setImagen(InputStream imagen) {
		this.imagen = imagen;
	}

	public boolean isActivo() {
		return activo;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
	}

	public String getID_Usuario() {
		return ID_Usuario;
	}

	public void setID_Usuario(String iD_Usuario) {
		ID_Usuario = iD_Usuario;
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

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
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
		this.apellidoM = apellidoM;
	}

	public Calendar getFecha_nacimiento() {
		return fecha_nacimiento;
	}

	public void setFecha_nacimiento(Calendar fecha_nacimiento) {
		this.fecha_nacimiento = fecha_nacimiento;
	}

	
	
}
