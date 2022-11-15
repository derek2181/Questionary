package com.piaweb.servlets;

import java.io.IOException;
import java.sql.SQLException;
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

import com.piaweb.data.AnswersDB;
import com.piaweb.data.CategoriesDB;
import com.piaweb.data.QuestionsDB;
import com.piaweb.models.Answer;
import com.piaweb.models.Category;
import com.piaweb.models.Question;
import com.piaweb.models.User;
import com.piaweb.viewmodels.AnswerCardViewModel;
import com.piaweb.viewmodels.QuestionDetailsViewModel;

/**
 * Servlet implementation class EditarRespuesta
 */
@WebServlet("/EditarRespuesta")
@MultipartConfig(maxFileSize = 16777215)
public class EditarRespuesta extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private CategoriesDB categoriesDB;
	private AnswersDB answersDB;
	@Resource(name="jdbc/PiaWeb")
	private DataSource pool;
	private Validator validator;
	
	public void init() throws ServletException{
		super.init();
		try {
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
    public EditarRespuesta() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		User userLoggedIn = (User)request.getSession().getAttribute("user");
		getCategoriesFromDB(request, response);
		if(userLoggedIn == null) {
			response.sendRedirect("/piaweb/Acceso/Login");
		} else {
			
			String answerID = request.getParameter("answerID");
			if(answerID != null) {
				int answerIDInteger = 0;
				try {
					answerIDInteger = Integer.parseInt(answerID);
				}catch(Exception ex) {
					ex.printStackTrace();
				}
				
				try {
					//GET THE ANSWER BY ID
					AnswerCardViewModel answerToEdit = answersDB.getAnswerById(answerIDInteger);
					if(answerToEdit != null) {
						if(answerToEdit.getAnswer().getID_Usuario().equals(userLoggedIn.getID_Usuario())) {
							request.setAttribute("answerToEdit", answerToEdit);
							if(request.getAttribute("showSuccessMessage") == null)
								request.setAttribute("showSuccessMessage", false);
							request.getRequestDispatcher("/Pages/answer-edition.jsp").forward(request, response);	
						} else {
							//Mostrar la pagina de error porque no puedes acceder por no ser dueño de la pregunta
						}
					} else {
						//Mostrar que esa pregunta no existe o fue eliminada
						request.getRequestDispatcher("/Pages/error.jsp").forward(request, response);
					}
					
					
				}catch(SQLException ex) {
					ex.printStackTrace();
				}
				
			}
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		User userLoggedIn = (User)request.getSession().getAttribute("user");
		
		if(userLoggedIn == null) {
			response.sendRedirect("/piaweb/Acceso/Login");
		} else {
			String answerID = request.getParameter("answerID");
			if(answerID != null) {
				int answerIDInteger = 0;
				try {
					answerIDInteger = Integer.parseInt(answerID);
				}catch(Exception ex) {
					ex.printStackTrace();
				}
				
				boolean success = false;
				AnswerCardViewModel answerToEdit = null;
				try {
					
					String userID = request.getParameter("userID");
					if(userLoggedIn.getID_Usuario().equals(userID)) {
						Answer answerEdited = new Answer();
						answerToEdit = answersDB.getAnswerById(answerIDInteger);
						if(answerToEdit != null)
							answerEdited = answerToEdit.getAnswer();
						//Editar la pregunta en la base de datos
						//questionEdited.setEncabezado(request.getParameter("encabezado"));
						//questionEdited.setDescripcion(request.getParameter("descripcion"));
						answerEdited.setRespuesta(request.getParameter("respuesta"));
						Set<ConstraintViolation<Answer>> errors = validator.validate(answerEdited);
						Map<String,String> errorsMessages = new HashMap<String, String>();
						if(!errors.isEmpty()){
							for(ConstraintViolation<Answer> constraint : errors) {
								errorsMessages.put(constraint.getPropertyPath().toString(), constraint.getMessage());
							}
							request.setAttribute("showSuccessMessage", false);
							request.setAttribute("errors", errorsMessages);
							request.setAttribute("answerToEdit", answerToEdit);
							getCategoriesFromDB(request, response);
							request.getRequestDispatcher("/Pages/answer-edition.jsp").forward(request, response);
						} else {
							//String quitarImagenParam = request.getParameter("quitarImagenParam");
							
							Part filePart = request.getPart("imagen");
							String quitarImagenParam = request.getParameter("quitarImagen");
							if(filePart.getInputStream().available() != 0 && quitarImagenParam == null) {
								answerEdited.setImagen(filePart.getInputStream());								
							} else if(quitarImagenParam != null) {
								answerEdited.setImagen(filePart.getInputStream());
							}
							success = answersDB.editAnswer(answerEdited);

						}
						
					}
					
				}catch(SQLException ex) {
					ex.printStackTrace();
				}
				
				//MANDAR EL GET
				request.setAttribute("showSuccessMessage", success);
				request.setAttribute("showDBError", !success ? true : false);

				doGet(request,response);
				//request.getRequestDispatcher("/Pages/question-edition.jsp").forward(request, response);
			}
		}
	}
	
	private void getCategoriesFromDB(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		try {
			List<Category> categories = categoriesDB.getAllCategories();
			request.setAttribute("categories", categories);
		} catch(SQLException ex) {
			ex.printStackTrace();
		}
	}

}
