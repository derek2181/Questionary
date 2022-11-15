package com.piaweb.servlets;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.sql.DataSource;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import com.piaweb.data.*;
import com.piaweb.models.Answer;
import com.piaweb.models.Category;
import com.piaweb.models.User;
import com.piaweb.viewmodels.AnswerCardViewModel;
import com.piaweb.viewmodels.QuestionDetailsViewModel;

/**
 * Servlet implementation class DetallesPregunta
 */
@WebServlet("/DetallesPregunta")
@MultipartConfig(maxFileSize = 16777215)
public class DetallesPregunta extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private QuestionsDB questionsDB;
	private CategoriesDB categoriesDB;
	private AnswersDB answersDB;
	
	private Validator validator;
	@Resource(name="jdbc/PiaWeb")
	private DataSource pool;
	public void init() throws ServletException{
		super.init();
		try {
			questionsDB = new QuestionsDB(pool);
			categoriesDB = new CategoriesDB(pool);
			answersDB = new AnswersDB(pool);
		} catch(Exception ex) {
			throw new ServletException(ex);
		}
		ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
		validator = validatorFactory.getValidator();
	}
	
	
	/**
     * @see HttpServlet#HttpServlet()
     */
    public DetallesPregunta() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		getCategoriesFromDB(request, response);
		//Manejar parametros 
		if(manageParameters(request, response)) {
			
			//response.sendRedirect("/piaweb/DetallesPregunta?questionID=" + "piaweb/Preguntar");
			Object show = request.getAttribute("showSuccessMessage");
			if(show == null)
				request.setAttribute("showSuccessMessage", false);
			request.getRequestDispatcher("/Pages/question-details.jsp").forward(request, response);
		} else {
			request.getRequestDispatcher("/Pages/error.jsp").forward(request, response);
		}
	}
	private boolean manageParameters(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String questionID = request.getParameter("questionID");
		boolean success = true;
		if(questionID != null) {
			//Traer la info de la pregunta desde la base de datos
			int id = 0;
			try {
				id = Integer.parseInt(questionID);
			}catch(Exception e) {
				e.printStackTrace();
			}
			
			String pageNumber = request.getParameter("pageNumber");
			int pageNumberInteger = 0;
			if(pageNumber != null) {
				try {
					pageNumberInteger = Integer.parseInt(pageNumber);				
				}catch(Exception ex) {
					ex.printStackTrace();
				}
				
			} else {
				pageNumberInteger = 1;
			}
			
			
			try {
				String usuario="";
				User userLoggedIn = (User)request.getSession().getAttribute("user");
				
				if(userLoggedIn!=null)
					usuario=userLoggedIn.getID_Usuario();
				QuestionDetailsViewModel questionDetails = questionsDB.getQuestionById(id,usuario);
				//BUSCAR LAS RESPUESTAS EN LA BASE DE DATOS
				int totalOfAnswers = answersDB.getNumberOfAnswersByQuestion(id);
				int numberOfPages = totalOfAnswers/10;
				if(totalOfAnswers % 10 != 0) {
					numberOfPages++;
				}
				
				//Comprobar si el numero de pagina solicitada es menor o igual al numero total de paginas, si no mostrar la pagina de error
				//Si el numero de paginas es 0 entrar de cualquier forma y mostrar los detalles de la pregunta
				List<AnswerCardViewModel> answers = answersDB.getAnswersByQuestionPagination(id, pageNumberInteger,usuario);
				AnswerCardViewModel correctAnswer = answersDB.getCorrectAnswerByQuestionId(id,usuario);
				if(pageNumberInteger <= numberOfPages && pageNumberInteger > 0) {
					if(questionDetails != null) {
						request.setAttribute("questionDetails", questionDetails);
						request.setAttribute("pageNumber", pageNumberInteger);
						request.setAttribute("numberOfPages", numberOfPages);
						request.setAttribute("totalOfAnswers", totalOfAnswers);
						request.setAttribute("correctAnswer", correctAnswer);
						request.setAttribute("answers", answers);
					} else {
						success = false;
					}					
				} else if(numberOfPages == 0) {
					if(questionDetails != null) {
						request.setAttribute("questionDetails", questionDetails);
						request.setAttribute("pageNumber", pageNumberInteger);
						request.setAttribute("numberOfPages", numberOfPages);
						request.setAttribute("totalOfAnswers", totalOfAnswers);
						request.setAttribute("correctAnswer", correctAnswer);
						request.setAttribute("answers", answers);
					} else {
						success = false;
					}
				} else {
					success = false;
					
				}
				
			}catch(SQLException ex) {
				success = false;
				ex.printStackTrace();
			}
		} else {
			success = false;
		}
		
		return success;
	}
	private void getCategoriesFromDB(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		try {
			List<Category> categories = categoriesDB.getAllCategories();
			request.setAttribute("categories", categories);
		} catch(SQLException ex) {
			ex.printStackTrace();
		}
	}
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//Invocar base de datos para publicar la respuesta
		String accion = request.getParameter("accion");
		if(accion != null) {
			switch(accion) {
			case "responder":
				responderPost(request, response);
				break;
			case "marcarCorrecta":
				marcarCorrectaPost(request, response);
				break;
			case "desmarcarCorrecta":
				desmarcarCorrectaPost(request,response);
				break;
			case "eliminarRespuesta":
				eliminarRespuestaPost(request, response);
				break;
			case "eliminarPregunta":
				eliminarPreguntaPost(request,response);
				break;
			}
		}
	}
	
	private void marcarCorrectaPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//Ir a la base de datos y setear la nueva respuesta correcta
		//validar que solo el dueño pueda hacer esto
		String questionID = request.getParameter("questionID");
		String answerID = request.getParameter("answerID");
		int answerIDInteger = 0;
		int questionIDInteger = 0;
		try {
			answerIDInteger = Integer.parseInt(answerID);
			questionIDInteger = Integer.parseInt(questionID);
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		
		try {
			boolean success = questionsDB.setCorrectAnswer(questionIDInteger, answerIDInteger);
			
		}catch(SQLException ex) {
			ex.printStackTrace();
		}
		
		//doGet(request, response);
		//Hacer el redirect
		response.sendRedirect("/piaweb/DetallesPregunta?questionID="+questionID);
	}
	
	private void desmarcarCorrectaPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//Ir a la base de datos y setear la nueva respuesta correcta
		String questionID = request.getParameter("questionID");
		String answerID = request.getParameter("answerID");
		int answerIDInteger = 0;
		int questionIDInteger = 0;
		try {
			answerIDInteger = Integer.parseInt(answerID);
			questionIDInteger = Integer.parseInt(questionID);
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		
		try {
			boolean success = questionsDB.setCorrectAnswer(questionIDInteger, null);
			
		}catch(SQLException ex) {
			ex.printStackTrace();
		}
		
		//doGet(request, response);
		response.sendRedirect("/piaweb/DetallesPregunta?questionID="+questionID);
	}
	
	private void responderPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		Answer answer = new Answer();
		int idPreguntaINT = 0;
		try {
			idPreguntaINT = Integer.parseInt(request.getParameter("questionID"));
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		answer.setID_Pregunta(idPreguntaINT);
		answer.setRespuesta(request.getParameter("respuesta"));
		User userLoggedIn = (User)request.getSession().getAttribute("user");
		if(userLoggedIn != null)
			answer.setID_Usuario(userLoggedIn.getID_Usuario());
		InputStream imageStream = null;
		Part filePart = request.getPart("image");
		boolean success = false;
		Map<String, String> errores = new HashMap<String,String>();
		Set<ConstraintViolation<Answer>> constraintViolations = validator.validate(answer);
		if(!constraintViolations.isEmpty()) {
			for(ConstraintViolation constraint : constraintViolations) {
				errores.put(constraint.getPropertyPath().toString(), constraint.getMessage());
			}
			request.setAttribute("errors", errores);
			request.setAttribute("answer", answer);
			doGet(request, response);
		} else {
			try {
				if(filePart != null) {
					imageStream = filePart.getInputStream();
					answer.setImagen(imageStream);
					
					success = answersDB.insertAnswer(answer, true);
				} else {
					success = answersDB.insertAnswer(answer, false);
				}			
			} catch(SQLException ex) {
				ex.printStackTrace();
			}
			request.setAttribute("showSuccessMessage", success);
			request.setAttribute("showDBError", !success?true : false);
			doGet(request, response);
			
		}
		
		//Mostrar que la respuesta fue publicada exitosamente y regresar la vista,
		
	}

	private void eliminarRespuestaPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		String answerID = request.getParameter("answerID");
		int answerIDInteger = 0;

		try {
			answerIDInteger = Integer.parseInt(answerID);
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		
		try {
			boolean success = answersDB.deleteAnswerById(answerIDInteger);
		}catch(SQLException ex) {
			ex.printStackTrace();
		}
		
		response.sendRedirect("/piaweb/DetallesPregunta?questionID="+request.getParameter("questionID"));
	}
	
	private void eliminarPreguntaPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String questionID = request.getParameter("questionID");
		int questionIDInteger = 0;
	
		try {
			questionIDInteger = Integer.parseInt(questionID);
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		boolean success = false;
		try {
			success = questionsDB.deleteQuestionById(questionIDInteger);
		}catch(SQLException ex) {
			ex.printStackTrace();
		}
		if(success) {
			String userID = request.getParameter("userID");
			response.sendRedirect("/piaweb/Perfil/Preguntas?userID="+userID);
		} else {			
			doGet(request, response);
		}
	}
}
