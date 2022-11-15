package com.piaweb.servlets;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.text.ParseException;
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
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import javax.sql.DataSource;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import com.piaweb.data.*;
import com.piaweb.models.*;

/**
 * Servlet implementation class Preguntar
 */
@WebServlet("/Preguntar")
@MultipartConfig(maxFileSize = 16777215)
public class Preguntar extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private QuestionsDB questionsDB;
	private CategoriesDB categoriesDB;
	@Resource(name="jdbc/PiaWeb")
	private DataSource pool;
	private Validator validator;
    /**
     * @see HttpServlet#HttpServlet()
     */
	
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
    public Preguntar() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//VALIDAR QUE LA SESION ESTE ACTIVA Y SI NO LO ESTA REDIRECCIONAR AL LOGIN
		getCategoriesFromDB(request,response);
		HttpSession session = request.getSession();
		User user = (User)session.getAttribute("user");
		if(user == null) {
			String pathTrace = request.getHeader("referer");
			
			//session.setAttribute("Direccion", pathTrace+"Preguntar");
			response.sendRedirect("/piaweb/Acceso/Login?returnURL=" + "piaweb/Preguntar");
			//request.getRequestDispatcher("/Acceso/Login").forward(request, response);
		} else {
			request.setAttribute("showSuccessMessage", false);
			request.setAttribute("showDBError", false);
			String categoryID = request.getParameter("categoryID");
			if(categoryID != null) {
				request.setAttribute("categoryID", categoryID);
			}
			request.getRequestDispatcher("/Pages/question-creation.jsp").forward(request, response);
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
		//OBTENER LOS DATOS DEL FORMULARIO
		// TODO valta revisar que no ingresa cosas vacias
		HttpSession session = request.getSession();
		User userLogged = (User)session.getAttribute("user");
		
		Question question = new Question();
		
		question.setDescripcion(request.getParameter("descripcion"));
		question.setEncabezado(request.getParameter("encabezado"));
		question.setID_Categoria(Integer.parseInt(request.getParameter("categoria")));
		question.setID_Usuario(userLogged.getID_Usuario());
		InputStream imageStream = null;
		Part filePart = request.getPart("imagen");
		
		//validar los errores y mostrarlos
		Set<ConstraintViolation<Question>> errors = validator.validate(question);
		Map<String,String> errorsMessages = new HashMap<String, String>();
		if(!errors.isEmpty()){
			for(ConstraintViolation<Question> constraint : errors) {
				errorsMessages.put(constraint.getPropertyPath().toString(), constraint.getMessage());
			}
			request.setAttribute("showSuccessMessage", false);
			request.setAttribute("showDBError", false);
			request.setAttribute("errors", errorsMessages);
			getCategoriesFromDB(request, response);
			request.getRequestDispatcher("/Pages/question-creation.jsp").forward(request, response);
		} else {
			
			boolean success = false;
			try {
				if(filePart != null) {
					imageStream = filePart.getInputStream();
					question.setImagen(imageStream);
					success = questionsDB.insertQuestion(question, true);
				} else {
					success = questionsDB.insertQuestion(question, false);
				}			
			} catch(SQLException ex) {
				ex.printStackTrace();
			}
			request.setAttribute("showSuccessMessage", success);
			request.setAttribute("showDBError", !success ? true : false);
			getCategoriesFromDB(request, response);
			request.getRequestDispatcher("/Pages/question-creation.jsp").forward(request, response);
		}
		
		//Mostrar la vista de redactar preguntas con una alerta tipo bootstrap diciendo que se publico la pregunta correctamente
	}

}
