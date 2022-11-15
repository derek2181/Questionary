package com.piaweb.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.annotation.Resource;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import com.piaweb.data.CategoriesDB;
import com.piaweb.data.InteractionDB;
import com.piaweb.data.QuestionsDB;
import com.piaweb.data.UsersDB;
import com.piaweb.viewmodels.QuestionDetailsViewModel;

/**
 * Servlet implementation class Interaccion
 */
@WebServlet("/Interaccion")
public class Interaccion extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private QuestionsDB questionsDB;
	private InteractionDB interactionDB;
	@Resource(name="jdbc/PiaWeb")
	private DataSource pool;
	static private String requestUrl;
	private Validator validator;
    public Interaccion() {
        super();
        // TODO Auto-generated constructor stub
    }
    
	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		super.init();
		try {
			questionsDB=new QuestionsDB(pool);
			interactionDB = new InteractionDB(pool);
			//usersDB = new UsersDB(pool);
		} catch(Exception ex) {
			throw new ServletException(ex);
		}
		ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
		validator = validatorFactory.getValidator();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			PrintWriter out=response.getWriter();
			response.setContentType("text/html");
			String condition=request.getParameter("condition");
			String userID=request.getParameter("userID");
			if(condition.equals("Question")){
			String idPregunta=request.getParameter("id");
		
				
				String action=request.getParameter("action");
				
				if(action.equals("favQuestion")) {
					int doomy=interactionDB.insertFav(userID,Integer.parseInt(idPregunta,10));
					 out.print(doomy+"<i class=\"far fa-star\"></i>");		
				}else {
					boolean interaccion=request.getParameter("tipo").equals("Like");
					int []likes=interactionDB.insertLike(userID,Integer.parseInt(idPregunta,10) , interaccion);
					out.print(likes[0]+"<i class=\"far fa-thumbs-up\"></i>");
				}
			
			
			}else {
				boolean interaccion=request.getParameter("tipo").equals("Like");
				String idRespuesta=request.getParameter("id");
				int []likes=interactionDB.insertLikeToAnswer(userID,Integer.parseInt(idRespuesta), interaccion);
				out.print(likes[0]+"<i class=\"far fa-thumbs-up\"></i>");
			}
			
		} catch (NumberFormatException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		doGet(request, response);
	}

}
