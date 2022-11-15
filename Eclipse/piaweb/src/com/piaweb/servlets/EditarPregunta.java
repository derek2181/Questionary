package com.piaweb.servlets;

import java.io.IOException;
import java.io.InputStream;
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

import com.piaweb.data.CategoriesDB;
import com.piaweb.data.QuestionsDB;
import com.piaweb.models.Category;
import com.piaweb.models.Question;
import com.piaweb.models.User;
import com.piaweb.viewmodels.QuestionDetailsViewModel;

/**
 * Servlet implementation class EditarPregunta
 */
@WebServlet("/EditarPregunta")
@MultipartConfig(maxFileSize = 16777215)
public class EditarPregunta extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private QuestionsDB questionsDB;
	private CategoriesDB categoriesDB;
	@Resource(name="jdbc/PiaWeb")
	private DataSource pool;
	private Validator validator;
	public void init() throws ServletException{
		super.init();
		try {
			questionsDB = new QuestionsDB(pool);
			categoriesDB = new CategoriesDB(pool);
		} catch(Exception ex) {
			throw new ServletException(ex);
		}
		ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
		validator = validatorFactory.getValidator();
	}
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EditarPregunta() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		getCategoriesFromDB(request,response);
		User userLoggedIn = (User)request.getSession().getAttribute("user");
		
		if(userLoggedIn == null) {
			response.sendRedirect("/piaweb/Acceso/Login");
		} else {
			
			String questionID = request.getParameter("questionID");
			if(questionID != null) {
				int questionIDInteger = 0;
				try {
					questionIDInteger = Integer.parseInt(questionID);
				}catch(Exception ex) {
					ex.printStackTrace();
				}
				
				try {
					QuestionDetailsViewModel questionToEdit = questionsDB.getQuestionById(questionIDInteger, userLoggedIn.getID_Usuario());
					if(questionToEdit != null) {
						if(questionToEdit.getIdUsuario().equals(userLoggedIn.getID_Usuario())) {
							request.setAttribute("questionToEdit", questionToEdit);
							if(request.getAttribute("showSuccessMessage") == null)
								request.setAttribute("showSuccessMessage", false);
							if(request.getAttribute("showDBError") == null)
								request.setAttribute("showDBError", false);
							request.getRequestDispatcher("/Pages/question-edition.jsp").forward(request, response);							
							//Aqui va todo el proceso
						} else {
							//Mostrar la pagina de error porque no puedes acceder por no ser due�o de la pregunta
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
		User userLoggedIn = (User)request.getSession().getAttribute("user");
		
		if(userLoggedIn == null) {
			response.sendRedirect("/piaweb/Acceso/Login");
		} else {
			String questionID = request.getParameter("questionID");
			if(questionID != null) {
				int questionIDInteger = 0;
				try {
					questionIDInteger = Integer.parseInt(questionID);
				}catch(Exception ex) {
					ex.printStackTrace();
				}
				
				boolean success = false;
				QuestionDetailsViewModel questionToEdit = null;
				try {
					
					String userID = request.getParameter("userID");
					if(userLoggedIn.getID_Usuario().equals(userID)) {
						Question questionEdited = new Question();
						questionToEdit = questionsDB.getQuestionById(questionIDInteger,userLoggedIn.getID_Usuario());
						if(questionToEdit != null)
							questionEdited = questionToEdit.getQuestion();
						//Editar la pregunta en la base de datos
						questionEdited.setEncabezado(request.getParameter("encabezado"));
						questionEdited.setDescripcion(request.getParameter("descripcion"));
						Set<ConstraintViolation<Question>> errors = validator.validate(questionEdited);
						Map<String,String> errorsMessages = new HashMap<String, String>();
						if(!errors.isEmpty()){
							for(ConstraintViolation<Question> constraint : errors) {
								errorsMessages.put(constraint.getPropertyPath().toString(), constraint.getMessage());
							}
							request.setAttribute("showSuccessMessage", false);
							request.setAttribute("showDBError", false);
							request.setAttribute("errors", errorsMessages);
							request.setAttribute("questionToEdit", questionToEdit);
							getCategoriesFromDB(request, response);
							request.getRequestDispatcher("/Pages/question-edition.jsp").forward(request, response);
						} else {
							//String quitarImagenParam = request.getParameter("quitarImagenParam");
							questionEdited.setID_Categoria(Integer.parseInt(request.getParameter("categoria")));
							Part filePart = request.getPart("imagen");
							String quitarImagenParam = request.getParameter("quitarImagen");
							if(filePart.getInputStream().available() != 0 && quitarImagenParam == null) {
								questionEdited.setImagen(filePart.getInputStream());								
							} else if(quitarImagenParam != null) {
								questionEdited.setImagen(filePart.getInputStream());
							}
							success = questionsDB.editQuestion(questionEdited);

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

}
