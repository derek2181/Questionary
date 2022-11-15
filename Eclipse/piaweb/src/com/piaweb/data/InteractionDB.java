package com.piaweb.data;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;

import javax.sql.DataSource;

public class InteractionDB {
	private DataSource source;
	public InteractionDB(DataSource source) {
		this.source = source;
	}
	private Encoder encoder=new Encoder();
	
	//ejemplo
	public int[] insertLike(String username,int ID_Pregunta,boolean interaccion) {
		Connection connection = null;
		CallableStatement st = null;
		int []likes=new int[2];
		try {
			connection = source.getConnection();
			st=connection.prepareCall("{call sp_Usuario_Pregunta(?,?,?,?)}");
			st.setString(1, "A");
			st.setString(2, username);
			st.setInt(3, ID_Pregunta);
			st.setBoolean(4,interaccion);
			ResultSet result=st.executeQuery();
			result.next();
			
				likes[0]=result.getInt("Likes");
				likes[1]=result.getInt("Dislikes");
	
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return likes;
	}
	
	public int[] insertLikeToAnswer(String username,int ID_Pregunta,boolean interaccion) {
		Connection connection = null;
		CallableStatement st = null;
		int []likes=new int[2];
		try {
			connection = source.getConnection();
			st=connection.prepareCall("{call sp_Usuario_Respuesta(?,?,?)}");
			st.setString(1, username);
			st.setInt(2, ID_Pregunta);
			st.setBoolean(3,interaccion);
			ResultSet result=st.executeQuery();
			result.next();
			
				likes[0]=result.getInt("Likes");
				likes[1]=result.getInt("Dislikes");
	
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return likes;
	}
	public int insertFav(String username,int ID_Pregunta) throws SQLException{
		Connection connection = null;
		CallableStatement st = null;
		int favs=0;
		try {
			connection = source.getConnection();
			st = connection.prepareCall("{CALL sp_Usuario_Pregunta(?,?,?,null)}");
			st.setString(1, "F");
			st.setString(2, username);
			st.setInt(3, ID_Pregunta);
			
			ResultSet result=st.executeQuery();
			result.next();
			favs=result.getInt("Favorito");
			
			
		}catch(SQLException ex) {
			ex.printStackTrace();
		} finally {
			
			connection.close();
			st.close();
		
		}
	return favs;
	}
}
