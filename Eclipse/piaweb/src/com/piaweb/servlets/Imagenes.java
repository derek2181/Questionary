package com.piaweb.servlets;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.SQLException;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import com.piaweb.data.AnswersDB;
import com.piaweb.data.QuestionsDB;
import com.piaweb.data.UsersDB;
import com.piaweb.models.User;

/**
 * Servlet implementation class Imagenes
 */
@WebServlet("/Imagenes")
public class Imagenes extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private QuestionsDB questionsDB;
	private UsersDB usersDB;
	private AnswersDB answersDB;
	@Resource(name="jdbc/PiaWeb")
	private DataSource pool;
	
	
	public void init() throws ServletException{
		super.init();
		try {
			questionsDB = new QuestionsDB(pool);
			 usersDB=new UsersDB(pool);
			 answersDB = new AnswersDB(pool);
		} catch(Exception ex) {
			throw new ServletException(ex);
		}
	}
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Imagenes() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String path = request.getPathInfo();
		if(path != null) {
			switch(path) {
			case "/Preguntas":
				preguntas(request, response);
				break;
			case "/Usuario":
				usuarios(request,response);
				break;
			case "/Respuestas":
				respuestas(request,response);
				break;
			}
		}
		
		//Manejar las rutas con parametros
		
		//request.getRequestDispatcher("/Pages/error.jsp").forward(request, response);
	}
	
	
	
	private void usuarios(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String imageID = request.getParameter("imageID");
		if(imageID != null) {
			
			try {
				Blob image = (Blob)usersDB.getImageByUserId(imageID);
				if(image != null) {
					if(image.getBinaryStream().available() != 0) {
						byte[] imageData = image.getBytes(1, (int)image.length());
						response.setContentType("image/gif");
						OutputStream out = response.getOutputStream();
						out.write(imageData);
						out.flush();
						out.close();						
					}else {
						
						request.getRequestDispatcher("/Images/user.jpg").forward(request, response);
					}
				}else {
					request.getRequestDispatcher("/Images/user.jpg").forward(request, response);

				}
				
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}
		
		
	}
	private void respuestas(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
		String imageID = request.getParameter("imageID");
		if(imageID != null) {
			int id = 0;
			try {
				id = Integer.parseInt(imageID);
			} catch(Exception ex) {
				ex.printStackTrace();
			}
			
			 try {
				 Blob image = answersDB.getAnswerImageById(id);
				 if(image != null) {
					 if(image.getBinaryStream().available() != 0) {
						 byte[] imageData = image.getBytes(1, (int)image.length());
						 response.setContentType("image/gif");
						 OutputStream out = response.getOutputStream();
						out.write(imageData);
						out.flush();
						out.close();
					 }
				 }
			 } catch(SQLException ex) {
				 ex.printStackTrace();
			 }
		}
	}
	
	private void preguntas(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String imageID = request.getParameter("imageID");
		if(imageID != null) {
			int id = 0;
			try {
				id = Integer.parseInt(imageID);
			}catch(Exception ex) {
				ex.printStackTrace();
			}
			try {
				Blob image = questionsDB.getQuestionImageById(id);
				if(image != null) {
					if(image.getBinaryStream().available() != 0) {
						byte[] imageData = image.getBytes(1, (int)image.length());
						response.setContentType("image/gif");
						OutputStream out = response.getOutputStream();
						out.write(imageData);
						out.flush();
						out.close();						
					}
				}
				
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}
	}
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
