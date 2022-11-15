package com.piaweb.data;
import javax.sql.DataSource;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;
import java.util.Map;
import java.util.Set;
import com.piaweb.models.*;
import com.piaweb.viewmodels.QuestionCardViewModel;
import com.piaweb.viewmodels.QuestionDetailsViewModel;


public class QuestionsDB {
	private DataSource source;
	public QuestionsDB(DataSource source) {
		this.source = source;
	}
	private Encoder encoder=new Encoder();
	public int getNumberOfRecentQuestions() throws SQLException{
		int count = 0;
		Connection connection = null;
		CallableStatement st = null;
		try {
			connection = source.getConnection();
			st = connection.prepareCall("{CALL sp_Preguntas(?,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null)}");
			st.setString(1, "2");
			ResultSet result = st.executeQuery();
			result.next();
			count = result.getInt(1);
			
		}catch(SQLException ex) {
			ex.printStackTrace();
		} finally {
			
			connection.close();
			st.close();
		}
		return count;
	}
	public List<QuestionCardViewModel> getRecentQuestionsPag(int page) throws SQLException{
		List<QuestionCardViewModel> listOfQuestions=new ArrayList<>();
		
		int start = page * 10 -10;
		Connection connection = null;
		CallableStatement st = null;
		try {
			connection = source.getConnection();
			st = connection.prepareCall("{CALL sp_Preguntas(?,null,null,null,null,null,null,null,null,?,null,null,null,null,null,null,null)}");
			st.setString(1, "1");
			st.setInt(2, start);
			ResultSet result = st.executeQuery();
			while(result.next()) {
				QuestionCardViewModel question = new QuestionCardViewModel();
				question.setIdPregunta(result.getInt("ID_Pregunta"));
				question.setEncabezado(result.getString("encabezado"));
				question.setActivo(result.getBoolean("activo"));
				
				Timestamp fecha = result.getTimestamp("fecha");
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(fecha);
				question.setFecha((Calendar)calendar.clone());
				question.getFecha().add(Calendar.HOUR, 6);
				question.setIdCategoria(result.getInt("ID_Categoria"));
				question.setNombreCategoria(result.getString("nombreCategoria"));
				question.setIdUsuario(result.getString("ID_Usuario"));
				question.setNombreUsuario(result.getString("nombreUsuario"));
				question.setLikes(result.getInt("Likes"));
				question.setDislikes(result.getInt("Dislikes"));
				listOfQuestions.add(question);
			}
			
			
		}catch(SQLException ex) {
			ex.printStackTrace();
		}finally {
			connection.close();
			st.close();
			
		}
		return listOfQuestions;
	}
	public int numberOfQuestions(String buscador,
			int[] votosI,int[] favoritesI,Calendar calendarInicio,Calendar calendarFin,
			String categorias)throws SQLException {
		Connection connection = null;
		CallableStatement st = null;
		int count=0;
		String procedure="{CALL sp_Preguntas(?,null,null,null,?,?,null,null,null,null,?,?,?,?,?,?,null)}";
		try {
			connection = source.getConnection();
			st = connection.prepareCall(procedure);
			st.setString(1, "Z");
			st.setDate(2,calendarInicio==null ? null : new java.sql.Date (calendarInicio.getTimeInMillis()));
			st.setDate(3,calendarFin==null ? null : new java.sql.Date (calendarFin.getTimeInMillis()));
			st.setString(4,buscador);
			st.setString(5,categorias==null?null:categorias);
			st.setInt(6, votosI==null?0:votosI[0]);
			st.setInt(7, votosI==null?100:votosI[1]);
			st.setInt(8, favoritesI==null?0:favoritesI[0]);
			st.setInt(9, favoritesI==null?100:favoritesI[1]);
			ResultSet result = st.executeQuery();
			result.next();
			count = result.getInt(1);
		}catch(SQLException ex) {
			ex.printStackTrace();
		} finally {
			connection.close();
			st.close();
		}
		return count;
	}
	
	public int numberOfQuestionsByUser(String usuario,int opcion) throws SQLException {
		Connection connection = null;
		CallableStatement st = null;
		int count=0;
		String procedure="{CALL sp_Preguntas(?,null,null,null,null,null,null,?,null,null,null,null,null,null,null,null,null)}";
		try {
			connection = source.getConnection();
			st = connection.prepareCall(procedure);
			if(opcion==1)
			st.setString(1, "Y"); //numero preguntas hechas
			else if(opcion==2)
			st.setString(1, "H"); //numero favs dados
			else if(opcion==3)
			st.setString(1, "W"); // numero likesdados
			else if(opcion==4)
			st.setString(1, "V"); // numero dislikesdados

			st.setString(2,usuario);
			ResultSet result = st.executeQuery();
			result.next();
			count = result.getInt(1);
		}catch(SQLException e) {
			e.printStackTrace();
		} finally {
			connection.close();
			st.close();
		}
		return count;
	}
	public int numberOfAnswersByUser(String usuario) throws SQLException {
		Connection connection = null;
		CallableStatement st = null;
		int count=0;
		String procedure="{CALL sp_Respuestas(?, null, null, null, null, null,null,null,?,null)}";	
		try {
			connection = source.getConnection();
			st = connection.prepareCall(procedure);
		
			st.setString(1, "Q"); //numero respuestashechas
		

			st.setString(2,usuario);
			ResultSet result = st.executeQuery();
			result.next();
			count = result.getInt(1);
		}catch(SQLException e) {
			e.printStackTrace();
		} finally {
			connection.close();
			st.close();
		}
		
		return count;
	}
	public List<QuestionDetailsViewModel> getAnswersByUser(String usuario,int page) throws SQLException{
		int start = page * 10 -10;
		List<QuestionDetailsViewModel> lista = new ArrayList<>();
		
		Connection connection = null;
		CallableStatement st = null;
		String procedure="{CALL sp_Respuestas(?, null, null, null, null, null,null,null,?,?)}";
		
		try {
		connection = source.getConnection();
		st = connection.prepareCall(procedure);
		st.setString(1, "B"); 
		st.setString(2,usuario);
		st.setInt(3, start);
	
		ResultSet rs=st.executeQuery();
		
		while(rs.next()) {
			QuestionDetailsViewModel question=new QuestionDetailsViewModel();
			
			//respuestas.respuesta,respuestas.fecha,respuestas.ID_Pregunta
			
			question.setIdUsuario(rs.getString("ID_Usuario"));
			question.getQuestion().setDescripcion(rs.getString("respuesta"));
			Timestamp fecha=rs.getTimestamp("fecha");
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(fecha);
			question.getQuestion().setFecha((Calendar)calendar.clone());
			question.getQuestion().setID_Pregunta(rs.getInt("ID_Pregunta"));
			lista.add(question);
		}
		
		}catch(SQLException ex) {
		ex.printStackTrace();
	
		} finally {
		connection.close();
		st.close();
		}
	

	
		return lista;
	
		
	}
	
	public List<QuestionDetailsViewModel> getQuestionsByUser(String usuario,int page,int opcion) throws SQLException{
		int start = page * 10 -10;
		List<QuestionDetailsViewModel> lista = new ArrayList<>();
		
		Connection connection = null;
		CallableStatement st = null;
		
		String procedure="{CALL sp_Preguntas(?,null,null,null,null,null,null,?,null,?,null,null,null,null,null,null,null)}";
		
		try {
			connection = source.getConnection();
			st = connection.prepareCall(procedure);
		
			switch(opcion) {
			case 1:
				st.setString(1, "Q"); //trae las preguntas hechas por el usuario
				break;
			case 2:
				st.setString(1, "F");//Trae las preguntas que se les ha dado fav
				break;
			case 3:
				st.setString(1, "X"); //Trae las preguntas que se les ha dado like
				break;
			case 4:
				st.setString(1, "M");//Trae las preguntas que se les ha dado dislike
				break;
			}
			
			st.setString(2,usuario);
			st.setInt(3, start);
		
			ResultSet rs=st.executeQuery();
			
			
			while(rs.next()) {
				QuestionDetailsViewModel question=new QuestionDetailsViewModel();
				
				question.getQuestion().setActivo(rs.getBoolean("activo"));
				question.getQuestion().setDescripcion(rs.getString("descripcion"));
				question.getQuestion().setEncabezado(rs.getString("encabezado"));
				
				Timestamp fecha=rs.getTimestamp("fecha");
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(fecha);
				question.getQuestion().setFecha((Calendar)calendar.clone());
				
				question.getQuestion().setID_Pregunta(rs.getInt("ID_Pregunta"));
				
					if(rs.getBlob("imagenPregunta")!= null)
						question.setImage(true);
					else
						question.setImage(false);
					
					
				question.setIdUsuario(rs.getString("ID_Usuario"));
				question.setNombreUsuario(rs.getString("nombreUsuario"));
				question.setApellidoPUsuario(rs.getString("apellidoP"));
				
				question.setIdCategoria(rs.getInt("ID_Categoria"));
				question.setNombreCategoria(rs.getString("nombreCategoria"));
				
				question.setLikes(rs.getInt("Likes"));
				question.setDislikes(rs.getInt("Dislikes"));
				question.setFavs(rs.getInt("Favorito"));
				lista.add(question);
				
			}
		
	
			
		}catch(SQLException ex) {
			ex.printStackTrace();
		
		} finally {
			connection.close();
			st.close();
		}
		
	
		
		return lista;
		
		
	}
	public List<QuestionDetailsViewModel> searchQuestions(String buscador,int page,
			int[] votosI,int[] favoritesI,Calendar calendarInicio,Calendar calendarFin,
			String categorias) throws SQLException {
		int start = page * 10 -10;
		List<QuestionDetailsViewModel> lista = new ArrayList<>();
		
		Connection connection = null;
		CallableStatement st = null;
		String procedure="{CALL sp_Preguntas(?,null,null,null,?,?,null,null,null,?,?,?,?,?,?,?,null)}";
			
		try {
			SimpleDateFormat format=null;
			String inicio=null;
			String fin= null;
			connection = source.getConnection();
			st = connection.prepareCall(procedure);
			st.setString(1, "S");
			if(calendarInicio!=null && calendarFin!=null ) {
			 format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;
			inicio = format.format(calendarInicio.getTime());
			fin= format.format(calendarFin.getTime());
			}
		
			
			st.setObject(2, inicio==null?null:inicio);
			st.setObject(3,fin==null?null:fin);
			st.setInt(4, start);
			st.setString(5,buscador);
			if(categorias!=null)
				if(categorias.equals("Todas"))
					categorias=null;
			st.setString(6,categorias);
			st.setInt(7, votosI==null?0:votosI[0]);
			st.setInt(8, votosI==null?100:votosI[1]);
			st.setInt(9, favoritesI==null?0:favoritesI[0]);
			st.setInt(10, favoritesI==null?100:favoritesI[1]);
			
			
			ResultSet rs=st.executeQuery();
			
			
			while(rs.next()) {
				QuestionDetailsViewModel question=new QuestionDetailsViewModel();
				
				question.getQuestion().setActivo(rs.getBoolean("activo"));
				question.getQuestion().setDescripcion(rs.getString("descripcion"));
				question.getQuestion().setEncabezado(rs.getString("encabezado"));
				
				Timestamp fecha=rs.getTimestamp("fecha");
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(fecha);
				question.getQuestion().setFecha((Calendar)calendar.clone());
				question.getQuestion().getFecha().add(Calendar.HOUR, 6);
				question.getQuestion().setID_Pregunta(rs.getInt("ID_Pregunta"));
				
					if(rs.getBlob("imagenPregunta")!= null)
						question.setImage(true);
					else
						question.setImage(false);
					
					
				question.setIdUsuario(rs.getString("ID_Usuario"));
				question.setNombreUsuario(rs.getString("nombreUsuario"));
				question.setApellidoPUsuario(rs.getString("apellidoP"));
				
				question.setIdCategoria(rs.getInt("ID_Categoria"));
				question.setNombreCategoria(rs.getString("nombreCategoria"));
				question.setLikes(rs.getInt("Likes"));
				question.setDislikes(rs.getInt("Dislikes"));
				question.setFavs(rs.getInt("Favorito"));
				lista.add(question);
				
			}
		
	
			
		}catch(SQLException ex) {
			ex.printStackTrace();
		
		} finally {
			connection.close();
			st.close();
		}
		
	
		
		return lista;
		
	}
	
	public boolean insertQuestion(Question question, boolean image) throws SQLException {
		boolean success = true;

		Connection connection = null;
		CallableStatement st = null;
		String procedureWithImage = "{CALL sp_Preguntas(?,null,?,?,?,null,?,?,?,null,null,null,null,null,null,null,null)}";
		String procedureWithNoImage = "{CALL sp_Preguntas(?,null,?,?,?,null,null,?,?,null,null,null,null,null,null,null,null)}";
		try {
			connection = source.getConnection();
			st = connection.prepareCall(image ? procedureWithImage : procedureWithNoImage);
			st.setString(1, "A");
			st.setString(2, encoder.encodeString(question.getEncabezado()));
			st.setString(3, encoder.encodeString(question.getDescripcion()));
//			st.setDate(4, new java.sql.Date(System.currentTimeMillis()));
			TimeZone utc = TimeZone.getTimeZone("UTC");
			Calendar cal = Calendar.getInstance(utc);			
			st.setTimestamp(4, java.sql.Timestamp.valueOf(java.time.LocalDateTime.now()));
			if(image) {
				st.setBlob(5, question.getImagen());
				st.setString(6, encoder.encodeString(question.getID_Usuario()));
				st.setInt(7, question.getID_Categoria());				
			} else {
				st.setString(5, encoder.encodeString(question.getID_Usuario()));
				st.setInt(6, question.getID_Categoria());	
			}
			st.execute();
		}catch(SQLException ex) {
			ex.printStackTrace();
			success = false;
		} finally {
			connection.close();
			st.close();
		}
		return success;
	}
	public QuestionDetailsViewModel getQuestionById(int id,String usuario) throws SQLException {
		QuestionDetailsViewModel question = new QuestionDetailsViewModel();
		Connection connection = null;
		CallableStatement st = null;
		try {
			connection = source.getConnection();
			st = connection.prepareCall("{CALL sp_Preguntas(?,?,null,null,null,null,null,?,null,null,null,null,null,null,null,null,null)}");
			st.setString(1, "G");
			st.setInt(2, id);
			st.setString(3, usuario);
			ResultSet result = st.executeQuery();
			result.next();
			if(result.getString("encabezado") != null) {
				question.getQuestion().setActivo(result.getBoolean("activo"));
				question.getQuestion().setDescripcion(result.getString("descripcion"));
				question.getQuestion().setEncabezado(result.getString("encabezado"));
				
				Timestamp fecha=result.getTimestamp("fecha");
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(fecha);
				question.getQuestion().setFecha((Calendar)calendar.clone());
				question.getQuestion().getFecha().add(Calendar.HOUR, 6);
				question.getQuestion().setID_Pregunta(result.getInt("ID_Pregunta"));
				question.getQuestion().setActivo_editar(result.getBoolean("activo_editar"));
				try {
					if(result.getBlob("imagenPregunta") != null) {
						if(result.getBlob("imagenPregunta").getBinaryStream().available() != 0) {
							question.setImage(true);
							question.getQuestion().setImagen(result.getBlob("imagenPregunta").getBinaryStream());
						}					
					} else {
						question.setImage(false);
					}
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					question.setImage(true);
					e.printStackTrace();
					
				}
				question.setLikes(result.getInt("Likes"));
				question.setDislikes(result.getInt("Dislikes"));
				question.setFavs(result.getInt("Favorito"));
				question.setIdUsuario(result.getString("ID_Usuario"));
				question.setNombreUsuario(result.getString("nombreUsuario"));
				question.setApellidoPUsuario(result.getString("apellidoP"));
				
				question.setIdCategoria(result.getInt("ID_Categoria"));
				question.setNombreCategoria(result.getString("nombreCategoria"));
				question.setLiked(result.getInt("Liked"));
				question.setDisliked(result.getInt("Disliked"));
				question.setFaved(result.getInt("Faved"));
				
			} else {
				return null;
			}
			
			
		}catch(SQLException ex) {
			ex.printStackTrace();
			
			return null;
		} finally {
			connection.close();
			st.close();
		}
		
		return question;
	}
	
	public Blob getQuestionImageById(int id) throws SQLException {
		Connection connection = null;
		CallableStatement st = null;
		Blob imagen = null;
		try {
			connection = source.getConnection();
			st = connection.prepareCall("{CALL sp_Preguntas(?,?,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null)}");
			st.setString(1, "I");
			st.setInt(2,id);
			ResultSet result = st.executeQuery();
			result.next();
			imagen = result.getBlob("imagen");
			
			
		}catch(SQLException ex) {
			ex.printStackTrace();
			
			
		}finally {
			connection.close();
			st.close();
		}
		
		return imagen;
	}
	
	public boolean setCorrectAnswer(int questionID, Object answerID) throws SQLException{
		boolean success = true;
		Connection connection = null;
		CallableStatement st = null;
		try {
			connection = source.getConnection();
			st = connection.prepareCall("{CALL sp_Preguntas(?,?,null,null,null,null,null,null,null,null,null,null,null,null,null,null,?)}");
			st.setString(1, "R");
			st.setInt(2, questionID);
			//st.setInt(3, answerID);
			st.setObject(3, answerID);
			st.execute();
			
		}catch(SQLException ex) {
			ex.printStackTrace();
			success = false;
		}finally {
			connection.close();
			st.close();
		}
		
		return success;
	}
	
	public List<QuestionCardViewModel> getQuestionByCategoryPagination(int id, int page) throws SQLException{
		List<QuestionCardViewModel> listOfQuestions = new ArrayList<QuestionCardViewModel>();
		int start = page * 10 -10;
		Connection connection = null;
		CallableStatement st = null;
		try {
			connection = source.getConnection();
			st = connection.prepareCall("{CALL sp_Preguntas(?,null,null,null,null,null,null,null,?,?,null,null,null,null,null,null,null)}");
			st.setString(1, "U");
			st.setInt(2, id);
			st.setInt(3, start);
			ResultSet result = st.executeQuery();
			while(result.next()) {
				QuestionCardViewModel question = new QuestionCardViewModel();
				question.setIdPregunta(result.getInt("ID_Pregunta"));
				question.setEncabezado(result.getString("encabezado"));
				question.setActivo(result.getBoolean("activo"));
				
				Timestamp fecha = result.getTimestamp("fecha");
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(fecha);
				question.setFecha((Calendar)calendar.clone());
				question.setIdCategoria(result.getInt("ID_Categoria"));
				question.setNombreCategoria(result.getString("nombreCategoria"));
				question.setIdUsuario(result.getString("ID_Usuario"));
				question.setNombreUsuario(result.getString("nombreUsuario"));
				listOfQuestions.add(question);
			}
			
			
		}catch(SQLException ex) {
			ex.printStackTrace();
		}finally {
			connection.close();
			st.close();
			
		}
		return listOfQuestions;
	}
	
	
	public int getNumberOfQuestionsWithCategory(int idCategory) throws SQLException{
		int count = 0;
		Connection connection = null;
		CallableStatement st = null;
		try {
			connection = source.getConnection();
			st = connection.prepareCall("{CALL sp_Preguntas(?,null,null,null,null,null,null,null,?,null,null,null,null,null,null,null,null)}");
			st.setString(1, "N");
			st.setInt(2, idCategory);
			ResultSet result = st.executeQuery();
			result.next();
			count = result.getInt(1);
			
		}catch(SQLException ex) {
			ex.printStackTrace();
		} finally {
			
			connection.close();
			st.close();
		}
		return count;
	}
	
	public boolean editQuestion(Question question) throws SQLException{
		Connection connection = null;
		CallableStatement st = null;
		boolean success = true;
		try {
			connection = source.getConnection();
			st = connection.prepareCall("{CALL sp_Preguntas(?,?,?,?,null,null,?,null,?,null,null,null,null,null,null,null,null)}");
			st.setString(1, "C");
			st.setInt(2, question.getID_Pregunta());
			st.setString(3, encoder.encodeString(question.getEncabezado()));
			st.setString(4, encoder.encodeString(question.getDescripcion()));
			st.setBlob(5, question.getImagen());
			st.setInt(6, question.getID_Categoria());
			st.execute();
			
			
		}catch(SQLException ex) {
			ex.printStackTrace();
			success = false;
		} finally {
			connection.close();
			st.close();
		}
		
		return success;
	}
	
	public boolean deleteQuestionById(int id) throws SQLException{
		Connection connection = null;
		CallableStatement st = null;
		boolean success = true;
		try {
			connection = source.getConnection();
			st = connection.prepareCall("{CALL sp_Preguntas(?,?,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null)}");
			st.setString(1, "B");
			st.setInt(2, id);
			
			st.execute();
		}catch(SQLException ex) {
			ex.printStackTrace();
			success = false;
		}
		return success;
	}
}
