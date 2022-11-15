package com.piaweb.servlets;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Error
 */
@WebServlet("/Error")
public class Error extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Error() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		int errorCode = response.getStatus();
		if(errorCode != 200) {
			String exceptionMessage = "Algo ha salido mal...";
			if(errorCode == 500) {
				Throwable throwable = (Throwable) request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
				exceptionMessage = throwable.getMessage();
				//exceptionMessage = "Ha ocurrido un error de ejecucion en el servidor, estamos trabajando para solucionarlo.";
			}
			if(errorCode == 404) {
				exceptionMessage = "No hemos encontrado este recurso...";
			}
			String mensaje = Integer.toString(errorCode);
			request.setAttribute("mensaje", mensaje);
			request.setAttribute("mensajeError", exceptionMessage);
			request.getRequestDispatcher("/Pages/error-page.jsp").forward(request, response);			
		} else {
			response.sendRedirect("/piaweb/");
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
