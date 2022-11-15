package com.piaweb.servlets;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import com.piaweb.data.CategoriesDB;
import com.piaweb.data.QuestionsDB;
import com.piaweb.models.*;
import com.piaweb.viewmodels.QuestionCardViewModel;

/**
 * Servlet implementation class Inicio
 */
@WebServlet("/Inicio")
public class Inicio extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private CategoriesDB categoriesDB;
	private QuestionsDB questionsDB;
	@Resource(name="jdbc/PiaWeb")
	private DataSource pool;
	public void init() throws ServletException{
		super.init();
		try {
			categoriesDB = new CategoriesDB(pool);
			questionsDB=new QuestionsDB(pool);
		} catch(Exception ex) {
			throw new ServletException(ex);
		}
	}   
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Inicio() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		getCategoriesFromDB(request, response);
		getRecentQuestions(request,response);
		String path = request.getPathInfo();
		if(path != null) {
			switch(path) {
			case "/About":
				break;
			}
		}
		
			}
	
	private void getRecentQuestions(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException{
		

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
		
		
		int totalOfResults = 0;
		List<QuestionCardViewModel> questions  = null;
		try {
			questions = questionsDB.getRecentQuestionsPag(pageNumberInteger);
			totalOfResults = questionsDB.getNumberOfRecentQuestions();
			int numberOfPages = totalOfResults / 10;
			if(totalOfResults % 10 > 0) {
				numberOfPages++;
			}
			//Comprobar si el numero de pagina solicitado esta dentro del rango
			if(pageNumberInteger <= numberOfPages && pageNumberInteger > 0 || numberOfPages == 0) {	
				request.setAttribute("numberOfPages", numberOfPages);
				request.setAttribute("pageNumber", pageNumberInteger);
				request.setAttribute("questions", questions);
				request.getRequestDispatcher("/Pages/index.jsp").forward(request, response);
			} else {
				//Error de pagina 
				request.getRequestDispatcher("/Pages/error.jsp").forward(request, response);
			}
		}catch(SQLException ex) {
			ex.printStackTrace();
			request.getRequestDispatcher("/Pages/error.jsp").forward(request, response);
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
		
	}

}
