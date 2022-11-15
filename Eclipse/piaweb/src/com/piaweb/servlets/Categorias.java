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
import com.piaweb.models.Category;
import com.piaweb.viewmodels.QuestionCardViewModel;

/**
 * Servlet implementation class Categorias
 */
@WebServlet("/Categorias")
public class Categorias extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private QuestionsDB questionsDB;
	private CategoriesDB categoriesDB;
	@Resource(name="jdbc/PiaWeb")
	private DataSource pool;
	public void init() throws ServletException{
		super.init();
		try {
			questionsDB = new QuestionsDB(pool);
			categoriesDB = new CategoriesDB(pool);
		} catch(Exception ex) {
			throw new ServletException(ex);
		}
	}   
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Categorias() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String categoryID = request.getParameter("categoryID");
		getCategoriesFromDB(request,response);
		if(categoryID != null) {
			//Buscar las preguntas con esa categoria y acomodar la pagina
			int id = 0;
			try {
				id = Integer.parseInt(categoryID);
			} catch(Exception ex) {
				ex.printStackTrace();
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
			/*
			try {
				
			} catch(SQLException ex) {
				ex.printStackTrace();
			}
			*/
			int totalOfResults = 0;
			List<QuestionCardViewModel> questions  = null;
			try {
				Category category = categoriesDB.getCategoryById(id);
				totalOfResults = questionsDB.getNumberOfQuestionsWithCategory(id);
				questions = questionsDB.getQuestionByCategoryPagination(id, pageNumberInteger);
				int numberOfPages = totalOfResults / 10;
				if(totalOfResults % 10 > 0) {
					numberOfPages++;
				}
				//Comprobar el numero de pagina con el total de resultados para averiguar cuando activar el boton de siguiente
				if(pageNumberInteger <= numberOfPages && pageNumberInteger > 0 || numberOfPages == 0) {					
					request.setAttribute("numberOfPages", numberOfPages);
					request.setAttribute("pageNumber", pageNumberInteger);
					request.setAttribute("questions", questions);
					request.setAttribute("category", category);
				} else {
					//Mostrar pagina de error
					request.getRequestDispatcher("/Pages/error.jsp").forward(request, response);
				}
				
				
				request.getRequestDispatcher("/Pages/categories.jsp").forward(request, response);
			}catch(SQLException ex) {
				ex.printStackTrace();
				request.getRequestDispatcher("/Pages/error.jsp").forward(request, response);
			}
			
			//QuestionCardViewModel model = question
			//Obtener el numero de paginas para poder manejarlas si no se manda el parametro de pagina pero se manda el id de categoria y existe
			//mostrar la pagina 1
		} else {
			
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
