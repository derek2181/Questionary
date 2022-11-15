package com.piaweb.servlets;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import com.piaweb.data.*;
import com.piaweb.models.Category;
import com.piaweb.viewmodels.QuestionDetailsViewModel;

/**
 * Servlet implementation class Busqueda
 */
@WebServlet("/Busqueda")
@MultipartConfig(maxFileSize = 1048576)
public class Busqueda extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
	private QuestionsDB questionsDB;
	private CategoriesDB categoriesDB;
	@Resource(name="jdbc/PiaWeb")
	private DataSource pool;
	static private String requestUrl;
	private Validator validator;
    public void init() throws ServletException {
        super.init();
        try {
        	questionsDB = new QuestionsDB(pool);
        	categoriesDB = new CategoriesDB(pool);
		} catch(Exception ex) {
			throw new ServletException(ex);
		}
		ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
		validator = validatorFactory.getValidator();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    private int[] getFilterInts(String opcion) {
    	int[] eleccion=new int[2];
    	switch(opcion) {
    	case "0":
    		eleccion=null;
    		break;
    	case "1":
    		eleccion[0]=0;
    		eleccion[1]=5;
    		break;
    	case "2":
    		eleccion[0]=5;
    		eleccion[1]=10;
    		break;
    	case "3":
    		eleccion[0]=10;
    		eleccion[1]=15;
    		break;
    	}
    	return eleccion;
    }
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		 String busqueda=request.getParameter("searchbox");
		 getCategoriesFromDB(request, response);
		 if(busqueda!=null) {
			 
			 if(!busqueda.equals("")) {
			 try {
							
			 String pageNumber = request.getParameter("pageNumber");
			 String favorites=request.getParameter("favoritos");
			 String votos=request.getParameter("votos");
			 String categorias=request.getParameter("categories");
			 String fechaInicio=request.getParameter("fechaInicio");
			 String fechaFin=request.getParameter("fechaFin");
			 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			 //variables que se mandaran
			 int [] votosI=null;
			 int[] favoritesI=null;
			 Date fecha=null;
			Date fecha2=null;
			
			 	if(votos!=null && !votos.equals(""))
			 	votosI=getFilterInts(votos);
			 	else 
			 		votosI=null;
			 	if(favorites!=null && !favorites.equals(""))
			 	 favoritesI=getFilterInts(favorites);
			 	else
			 		favoritesI=null;
				
				try {
					
					//Validacion de fecha, si no mete nada las hago nulas
				if(fechaInicio!=null) {
					if(!fechaInicio.equals("")) {
							fecha = sdf.parse(fechaInicio);
							
						}
						
				}
				if(fechaFin!=null) {
					if(!fechaFin.equals("")) {
							fecha2 = sdf.parse(fechaFin);
							
						}		
				}	
				
				if(fecha2==null && fecha!=null) {
					fecha2 = Calendar.getInstance().getTime();
				}
				
			
				
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//Dos calendarios uno para inferior y superior del filtro
				 Calendar calendarInicio = Calendar.getInstance();
				 Calendar calendarFin = Calendar.getInstance();
				 if(fecha!=null && fecha2!=null) {
					 calendarInicio.setTime(fecha);
				 calendarFin.setTime(fecha2);
				 }else {
					 calendarInicio=null;
					 calendarFin=null;
				 }
				 
					if(fecha2!=null && fecha==null) {
						calendarInicio=Calendar.getInstance();
						calendarFin = Calendar.getInstance();
						calendarInicio.add(Calendar.YEAR, -1000);
						 calendarFin.setTime(fecha2);
					}
					
				if(categorias != null) {
					if(categorias.equals("Todas")) {
						categorias = null;
					}					
				}
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
				
			List<QuestionDetailsViewModel> preguntas=questionsDB.searchQuestions(busqueda,
					pageNumberInteger,votosI,favoritesI,calendarInicio,calendarFin,
					categorias);
			
			int totalOfResults=questionsDB.numberOfQuestions(busqueda,votosI,favoritesI,calendarInicio,calendarFin,
					categorias);
					int numberOfPages = totalOfResults / 10;
					if(totalOfResults % 10 > 0) {
						numberOfPages++;
					}
					request.setAttribute("busqueda", busqueda);
					request.setAttribute("numberOfPages", numberOfPages);
					request.setAttribute("pageNumber", pageNumberInteger);
					request.setAttribute("preguntasEncontradas", preguntas);
					request.getRequestDispatcher("/Pages/advanced-search.jsp").forward(request, response);
			 }catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			 } else {
				 
				 response.sendRedirect("/piaweb/");
			 }
			 				
		 }else {
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
		
		//response.getWriter().append("Served at: ").append(request.getContextPath());
	

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void resultado(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException{
		request.getRequestDispatcher("/Pages/advanced-search.jsp").forward(request, response);
	}
	protected void resultadoPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
	
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
	}

}
