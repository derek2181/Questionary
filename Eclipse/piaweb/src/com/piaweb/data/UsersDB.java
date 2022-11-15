package com.piaweb.data;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.sql.DataSource;
import com.piaweb.models.*;


public class UsersDB {
	private DataSource source;
	private Encoder encoder=new Encoder();
	public UsersDB(DataSource source) {
		this.source = source;
	}
	
	public String test() throws SQLException{
		Connection connection = source.getConnection();
		String nombre = "";
		CallableStatement st = connection.prepareCall("{CALL sp_Usuarios(?,?,null,null,null,null,null)}");
		st.setString(1, "G");
		st.setString(2, "derix");
		ResultSet result = st.executeQuery();
		while(result.next()) {
			nombre = result.getString("ID_Usuario");
		}
		return nombre;
	}
	
	public boolean editar_usuario(CambiarDatos user)throws SQLException {
		boolean estado=true;
		Connection connection = null;
		CallableStatement st = null;
		try {
			connection = source.getConnection();
			st = connection.prepareCall("{CALL sp_Usuarios(?,?,?,?,?,?,?,?,?)}");
		
			st.setString(1, "C");
			st.setString(2,user.getID_Usuario());
			st.setString(3,user.getCorreo());
			st.setString(4,user.getContra());
			st.setString(5, user.getApellidoP());
			st.setString(6, user.getApellidoM());
			st.setDate(7,new java.sql.Date (user.getFecha_nacimiento().getTimeInMillis()));
			st.setString(8, user.getNombre());
			st.setBlob(9,user.getImagen());
			
			st.execute();
		
			} 
			catch (SQLException e) {
				e.printStackTrace();
				estado=false;
			} finally {
				connection.close();
				st.close();
			}
		return estado;
	}
	public boolean register(User user) throws SQLException {
		boolean estado=true;
		Connection connection = null;
		CallableStatement st = null;
		try {
		connection = source.getConnection();
		st = connection.prepareCall("{CALL sp_Usuarios(?,?,?,?,?,?,?,?,null)}");
	
		st.setString(1, "A");
		st.setString(2, encoder.encodeString(user.getID_Usuario()));
		st.setString(3, encoder.encodeString(user.getCorreo()));
		st.setString(4, user.getContra());
		st.setString(5, encoder.encodeString(user.getApellidoP()));
		st.setString(6, encoder.encodeString(user.getApellidoM()));
		st.setDate(7,new java.sql.Date (user.getFecha_nacimiento().getTimeInMillis()));
		st.setString(8, encoder.encodeString(user.getNombre()));
		st.execute();
	
		} 
		catch (SQLException e) {
			e.printStackTrace();
			estado=false;
		} finally {
			connection.close();
			st.close();
		}
		
		return estado;
	}
	
	public Blob getImageByUserId(String username) throws SQLException {
		
		Connection connection = null;
		CallableStatement st = null;
		Blob imagen=null;
		try {
		
		 connection = source.getConnection();
		st = connection.prepareCall("{CALL sp_Usuarios(?,?,null,null,null,null,null,null,null)}");
	
		st.setString(1, "I");
		st.setString(2, encoder.encodeString(username));

		
		ResultSet rs=st.executeQuery();
	
		rs.next();
		imagen = rs.getBlob("imagen");
	
		
		}
		
		catch (SQLException e) {
			e.printStackTrace();
			return null;
		}finally {
			connection.close();
			st.close();
		}
		return imagen;
	}
	public User login(String username, String password) throws SQLException {
		User usuario=new User();
		Connection connection = null;
		CallableStatement st = null;
		try {
		
		 connection = source.getConnection();
		st = connection.prepareCall("{CALL sp_Usuarios(?,?,null,?,null,null,null,null,null)}");
	
		st.setString(1, "G");
		st.setString(2, encoder.encodeString(username));
		st.setString(3, encoder.encodeString(password));
		
		ResultSet rs=st.executeQuery();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-dd-MM");

		rs.next();
		usuario.setID_Usuario(rs.getString("ID_Usuario"));
		usuario.setApellidoM(rs.getString("apellidoM"));
		usuario.setApellidoP(rs.getString("apellidoP"));
		usuario.setCorreo(rs.getString("correo"));
		usuario.setActivo(rs.getBoolean("activo"));
		usuario.setNombre(rs.getString("nombre"));
		
		java.sql.Date fecha=rs.getDate("fecha_nacimiento");
		 Calendar calendar = Calendar.getInstance();
	     calendar.setTime(fecha);	
	     usuario.setFecha_nacimiento((Calendar)calendar.clone());
		
		}
		
		catch (SQLException e) {
			e.printStackTrace();
			return null;
		}finally {
			connection.close();
			st.close();
		}
		return usuario;
	}
		

	
	public User getUserById(String username) throws SQLException {
		User usuario=new User();
		Connection connection = null;
		CallableStatement st = null;
		try {
		
			connection = source.getConnection();
			st = connection.prepareCall("{CALL sp_Usuarios(?,?,null,null,null,null,null,null,null)}");
		
			st.setString(1, "D");
			st.setString(2, encoder.encodeString(username));
			
			ResultSet rs=st.executeQuery();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-dd-MM");
			rs.next();
			String prueba=rs.getString("apellidoM");
			String prueba2=encoder.encodeString(rs.getString("apellidoM"));
			String prueba3=encoder.encodeString(rs.getString("apellidoM"));
			String prueba4=encoder.encodeString(prueba3);
			usuario.setID_Usuario(rs.getString("ID_Usuario"));
			usuario.setApellidoM(rs.getString("apellidoM"));
			usuario.setApellidoP(rs.getString("apellidoP"));
			usuario.setCorreo(rs.getString("correo"));
			usuario.setActivo(rs.getBoolean("activo"));
			usuario.setNombre(rs.getString("nombre"));
			
			java.sql.Date fecha=rs.getDate("fecha_nacimiento");
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(fecha);	
			usuario.setFecha_nacimiento((Calendar)calendar.clone());
				
	
		}
		
		catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			connection.close();
			st.close();
		}
		return usuario;
	}
	
	
}
