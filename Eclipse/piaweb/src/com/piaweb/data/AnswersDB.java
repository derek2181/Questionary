package com.piaweb.data;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.sql.DataSource;

import com.piaweb.models.*;
import com.piaweb.viewmodels.AnswerCardViewModel;

public class AnswersDB {
	private DataSource source;
	public AnswersDB(DataSource source) {
		this.source = source;
	}
	private Encoder encoder=new Encoder();
	public boolean insertAnswer(Answer answer, boolean image) throws SQLException {
		boolean success = true;
		Connection connection = null;
		CallableStatement st = null;
		try {
			connection = source.getConnection();
			st = connection.prepareCall("{CALL sp_Respuestas(?, null, ?, ?, ?,?,?,?,?, null)}");
			st.setString(1, "A");
			st.setString(2, encoder.encodeString(answer.getRespuesta()));
			if(image) {
				st.setBlob(3, answer.getImagen());
			} else {
				st.setNull(3, java.sql.Types.BLOB);				
			}
			st.setTimestamp(4, java.sql.Timestamp.valueOf(java.time.LocalDateTime.now()));
			st.setBoolean(5, false);
			st.setBoolean(6,  true);
			st.setInt(7, answer.getID_Pregunta());
			st.setString(8, encoder.encodeString(answer.getID_Usuario()));
			
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
	
	public List<AnswerCardViewModel> getAnswersByQuestionPagination(int id, int page,String user) throws SQLException{
		List<AnswerCardViewModel> listOfAnswers = new ArrayList<AnswerCardViewModel>();
		Connection connection = null;
		CallableStatement st = null;
		int start = page * 10 -10;
		try {
			connection = source.getConnection();
			st = connection.prepareCall("{CALL sp_Respuestas(?, null, null, null, null, null,null,?,?,?)}");
			st.setString(1, "G");
			st.setInt(2, id);
			st.setString(3, user);
			st.setInt(4, start);
			ResultSet result = st.executeQuery();
			while(result.next()) {
				AnswerCardViewModel answer = new AnswerCardViewModel();
				answer.getAnswer().setID_Respuesta(result.getInt("ID_Respuesta"));
				answer.getAnswer().setRespuesta(result.getString("respuesta"));
				try {
					if(result.getBlob("imagen") != null) {
						if(result.getBlob("imagen").getBinaryStream().available() != 0) {
							answer.getAnswer().setImagen(result.getBlob("imagen").getBinaryStream());
						}
					}
				}catch(IOException ex) {
					ex.printStackTrace();
				}
				//answer.getAnswer().setImagen(result.getBlob("imagen").getBinaryStream());
				Timestamp fecha=result.getTimestamp("fecha");
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(fecha);
				answer.getAnswer().setFecha((Calendar)calendar.clone());
				answer.getAnswer().setActivo_editar(result.getBoolean("activo_editar"));
				answer.getAnswer().setActivo_eliminar(result.getBoolean("activo_eliminar"));
				answer.getAnswer().setID_Pregunta(result.getInt("ID_Pregunta"));
				answer.getAnswer().setID_Usuario(result.getString("ID_Usuario"));
				answer.setNombreUsuario(result.getString("nombreUsuario"));
				answer.setLikes(result.getInt("Likes"));
				answer.setDislikes(result.getInt("Dislikes"));
				answer.setLiked(result.getInt("Liked")); //3 es null
				answer.setDisliked(result.getInt("Disliked"));
				listOfAnswers.add(answer);
			}
		}catch(SQLException ex) {
			ex.printStackTrace();
		} finally {
			connection.close();
			st.close();
		}
		return listOfAnswers;
	}
	
	public int getNumberOfAnswersByQuestion(int id)throws SQLException {
		int numberOfResults = 0;
		Connection connection = null;
		CallableStatement st = null;
		try {
			connection = source.getConnection();
			st = connection.prepareCall("{CALL sp_Respuestas(?, null, null, null, null, null,null,?,null,null)}");
			st.setString(1, "N");
			st.setInt(2, id);
			ResultSet result = st.executeQuery();
			result.next();
			numberOfResults = result.getInt(1);
			
			
		}catch(SQLException ex) {
			ex.printStackTrace();
		} finally {
			connection.close();
			st.close();
		}
		
		
		return numberOfResults;
	}
	
	public AnswerCardViewModel getAnswerById(int id) throws SQLException {
		AnswerCardViewModel answer = new AnswerCardViewModel();
		Connection connection = null;
		CallableStatement st = null;
		try {
			connection = source.getConnection();
			st = connection.prepareCall("{CALL sp_Respuestas(?, ?, null, null, null, null,null,null,null,null)}");
			st.setString(1, "1");
			st.setInt(2, id);
			ResultSet result = st.executeQuery();
			
			result.next();
			
			answer.getAnswer().setRespuesta(result.getString("respuesta"));
			answer.getAnswer().setID_Respuesta(result.getInt("ID_Respuesta"));
			answer.getAnswer().setID_Usuario(result.getString("ID_Usuario"));
			answer.getAnswer().setID_Pregunta(result.getInt("ID_Pregunta"));
			Blob imagen = result.getBlob("imagen");
			if(imagen != null) {
				try {
					if(imagen.getBinaryStream().available() != 0) {
						answer.getAnswer().setImagen(result.getBlob("imagen").getBinaryStream());					
					}					
				}catch(IOException ex) {
					ex.printStackTrace();
				}
			}
			
			
		}catch(SQLException ex) {
			ex.printStackTrace();
			return null;
		} finally {
			connection.close();
			st.close();
		}
		return answer;
	}
	
	public boolean editAnswer(Answer answer)throws SQLException {
		Connection connection = null;
		CallableStatement st = null;
		boolean success = true;
		try {
			connection = source.getConnection();
			st = connection.prepareCall("{CALL sp_Respuestas(?, ?, ?, ?, null, null,null,null,null,null)}");
			st.setString(1, "U");
			st.setInt(2, answer.getID_Respuesta());
			st.setString(3, answer.getRespuesta());
			st.setBlob(4, answer.getImagen());
			
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
	
	public Blob getAnswerImageById(int id) throws SQLException{
		Connection connection = null;
		CallableStatement st = null;
		Blob imagen = null;
		try {
			connection = source.getConnection();
			st = connection.prepareCall("{CALL sp_Respuestas(?, ?, null, null, null, null,null,null,null,null)}");
			st.setString(1, "I");
			st.setInt(2, id);
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
	
	public AnswerCardViewModel getCorrectAnswerByQuestionId(int id,String usuario) throws SQLException{
		Connection connection = null;
		CallableStatement st = null;
		AnswerCardViewModel answer = new AnswerCardViewModel();
		try {
			connection = source.getConnection();
			st = connection.prepareCall("{CALL sp_Respuestas(?, null, null, null, null, null,null,?,?,null)}");
			st.setString(1,"C");
			st.setInt(2, id);
			st.setString(3,usuario);
			ResultSet result = st.executeQuery();
			result.next();
			answer.getAnswer().setID_Respuesta(result.getInt("ID_Respuesta"));
			answer.getAnswer().setRespuesta(result.getString("respuesta"));
			
			try {
				if(result.getBlob("imagen") != null) {
					if(result.getBlob("imagen").getBinaryStream().available() != 0) {
						answer.getAnswer().setImagen(result.getBlob("imagen").getBinaryStream());
					}
				}
			}catch(IOException ex) {
				ex.printStackTrace();
			}
			
			//answer.getAnswer().setImagen(result.getBlob("imagen").getBinaryStream());
			Timestamp fecha=result.getTimestamp("fecha");
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(fecha);
			answer.getAnswer().setFecha((Calendar)calendar.clone());
			answer.getAnswer().setActivo_editar(result.getBoolean("activo_editar"));
			answer.getAnswer().setActivo_eliminar(result.getBoolean("activo_eliminar"));
			answer.getAnswer().setID_Pregunta(result.getInt("ID_Pregunta"));
			answer.getAnswer().setID_Usuario(result.getString("ID_Usuario"));
			answer.setNombreUsuario(result.getString("nombreUsuario"));
			answer.setLikes(result.getInt("Likes"));
			answer.setDislikes(result.getInt("Dislikes"));
			answer.setLiked(result.getInt("Liked"));
			answer.setDisliked(result.getInt("Disliked"));
			
			
		}catch(SQLException ex) {
			ex.printStackTrace();
			return null;
		} finally {
			connection.close();
			st.close();
		}
		
		return answer;
	}
	
	public boolean deleteAnswerById(int id)throws SQLException {
		Connection connection = null;
		CallableStatement st = null;
		boolean success = true;
		try {
			connection = source.getConnection();
			st = connection.prepareCall("{CALL sp_Respuestas(?, ?, null, null, null, null,null,null,null,null)}");
			st.setString(1, "D");
			st.setInt(2, id);
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
}
