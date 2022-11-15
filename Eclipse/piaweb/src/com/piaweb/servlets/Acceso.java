package com.piaweb.servlets;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import com.piaweb.data.*;
import com.piaweb.models.User;
import com.piaweb.viewmodels.LoginViewModel;

/**
 * Servlet implementation class Acceso
 */
@WebServlet("/Acceso")
public class Acceso extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private UsersDB usersDB;
	@Resource(name="jdbc/PiaWeb")
	private DataSource pool;
	static private String requestUrl;
	private Validator validator;
	public void init() throws ServletException{
		super.init();
		try {
			usersDB = new UsersDB(pool);
		} catch(Exception ex) {
			throw new ServletException(ex);
		}
		ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
		validator = validatorFactory.getValidator();
	}
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Acceso() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String path = request.getPathInfo();
		String returnURL = request.getParameter("returnURL");
		User userLoggedIn = (User)request.getSession().getAttribute("user");
		if(userLoggedIn == null) {
			if(returnURL != null) {
				request.setAttribute("returnURL", returnURL);
			}
			//requestUrl = request.getHeader("referer");
			//requestUrl = (String) request.getAttribute("javax.servlet.forward.request_uri");  
			if(path != null) {
				switch(path) {
				case "/Prueba":
					try {
						response.getWriter().println(usersDB.test());
						
					}catch(Exception ex) {
						ex.printStackTrace();
					}
					break;
				case "/Login":
					login(request, response);
					break;
				case "/Registro":
					registro(request, response);
					break;
				}
				//request.getRequestDispatcher("/Pages/error.jsp").forward(request, response);
			}
			
			//Manejar las rutas con parametros
			
			
		} else {
			String accion = request.getParameter("action");
			if(accion != null) {
				switch(accion) {
				case "logout":
					request.getSession().invalidate();
					response.sendRedirect("/piaweb/");
					break;
				}			
			} else {				
				response.sendRedirect("/piaweb/");
			}
		}
		
		//response.sendRedirect("Login");
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		//response.getWriter().println(path);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String accion=request.getParameter("accion");
		
		switch(accion) {
		case "Registro":
			 registroPost(request,response);
			break;
		case "Login":
			 loginPost(request,response);
			break;
		}
		//request.getRequestDispatcher("/Pages/error.jsp").forward(request, response);

	}
	
	
	
	private void loginPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		
		String usuario=request.getParameter("nombreUsuario");
		String contrasena=request.getParameter("contrasena");
		String returnURL = request.getParameter("returnURL");
		if(returnURL != null) {
			request.setAttribute("returnURL", returnURL);
		}
		LoginViewModel model = new LoginViewModel(usuario, contrasena);
		Set<ConstraintViolation<LoginViewModel>> constraintViolations = validator.validate(model);
		
			User user=null;
		
			HttpSession session = request.getSession();
			try {
				user = usersDB.getUserById(usuario);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Map<String,String> errores=new HashMap<String,String>();
			if(user==null) {
				//Usuario no encontrado mostrar errores en el html
				//Si no encuentra el usuario significa que nisiquiera existe
				
				
				errores.put("NoExistente","Usuario no existente");
			
				request.setAttribute("errores", errores);
				request.getRequestDispatcher("/Pages/login.jsp").forward(request, response);

			}
			else {
				
				//ahora aqui revisamos si esa contrasena coincide con el usuario
				try {
					user=usersDB.login(usuario,contrasena);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(user==null) {	
					
					errores.put("contrasenaIncorrecta","La contrase�a es incorrecta");
					request.setAttribute("usuarioForm", usuario);
					request.setAttribute("errores", errores);
					request.getRequestDispatcher("/Pages/login.jsp").forward(request, response);
					
				}else {
					
					//String pathTrace = (String) session.getAttribute("Direccion");
					String pathTrace = (String)request.getParameter("returnURL");
					session.setMaxInactiveInterval(3600);
					session.setAttribute("user", user);
					//response.sendRedirect(requestUrl);
					if(!pathTrace.equals("null"))
					response.sendRedirect("/"+pathTrace);	
					else
						response.sendRedirect("/piaweb/");	
					
				}
				//Redireccionar al indice e iniciar la session para que se mantenga el usuario
			
								
				session.setAttribute("errores", errores);
			}
			
		
		}
		
		
	
	
	private void registro(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		request.getRequestDispatcher("/Pages/register.jsp").forward(request, response);
	}
	private void login(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		request.getRequestDispatcher("/Pages/login.jsp").forward(request, response);
	}
	
	private void registroPost(HttpServletRequest request, HttpServletResponse response){
		try {
			String nombre=request.getParameter("nombre");
			String apellidoP=request.getParameter("apellidoP");
			String apellidoM=request.getParameter("apellidoM");
			String correo=request.getParameter("email");
			String usuario=request.getParameter("nombreUsuario");
			String contrasena=request.getParameter("contrasena");
			String contrasena2 = request.getParameter("contrasena2");
			String cadena_fecha=request.getParameter("fecha_nacimiento");
			HttpSession session = request.getSession();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-dd-MM");
			Map<String,String> errores=new HashMap<String,String>();

			Date fecha=null;
			try {
				if(cadena_fecha!="")
				fecha = sdf.parse(cadena_fecha);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			boolean dateError = false;
			 Calendar calendar = Calendar.getInstance();
			 if(fecha!=null)
		     calendar.setTime(fecha);
			 else{
				errores.put("fecha_nacimiento","Ingrese algo en la fecha");
				dateError = true;
				calendar=null;
			 }
			 boolean passwordError = false;
			 if(!contrasena.equals(contrasena2)) {
				 errores.put("contra", "El campo contrase�a debe coincidir con el campo confirmar contrase�a");
				 passwordError = true;
			 }
			 
			 boolean userNameError = false;
			 if(usersDB.getUserById(usuario)!=null) {
				errores.put("ID_Usuario","Ya existe un usuario con este nombre");
				userNameError = true;
			}
			//response.getWriter().println(cadena_fecha);
			//Validar que el usuario no se repita 
		
			User user=new User(usuario,nombre,contrasena,correo,apellidoP,apellidoM,calendar,true);
			user.setConfirmarContrasena(contrasena2);
			
				
		
			Set<ConstraintViolation<User>> constraintViolations = validator.validate(user);
			
			if(!constraintViolations.isEmpty() || passwordError || userNameError || dateError) {
				for(ConstraintViolation<User> constraintViolation : constraintViolations) {
				
					errores.put(constraintViolation.getPropertyPath().toString(),constraintViolation.getMessage());
				}
				
				request.setAttribute("errores", errores);
				
				request.setAttribute("fecha_nacimiento",cadena_fecha);
				
				
				request.setAttribute("usuarioRegister", user);
				request.getRequestDispatcher("/Pages/register.jsp").forward(request, response);
				
			} else {
				//Validar que los campos de contrase�a contengan lo mismo
				
				boolean estado=usersDB.register(user);
				if(estado) {
					//INICIAR LA SESSION
					
					session.setMaxInactiveInterval(3600);
					session.setAttribute("user", user);
					//response.sendRedirect(requestUrl);
					response.sendRedirect("/piaweb/");	
				}
				else {
					//INDICAR QUE HUBO UN ERROR EN LA BASE DE DATOS
					request.getRequestDispatcher("/Pages/register.jsp").forward(request, response);
				}
				
					
				
				
			}
			
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}
}
	
	

