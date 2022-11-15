package com.piaweb.servlets;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
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
import java.sql.Blob;
import com.piaweb.data.*;
import com.piaweb.models.*;
import com.piaweb.viewmodels.QuestionDetailsViewModel;

/**
 * Servlet implementation class Perfil
 */
@WebServlet("/Perfil")
@MultipartConfig(maxFileSize = 16777215)
public class Perfil extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private UsersDB usersDB;
	private CategoriesDB categoriesDB;
	private QuestionsDB questionsDB;
	@Resource(name="jdbc/PiaWeb")
	private DataSource pool;
	
	private Validator validator;
	public void init() throws ServletException{
		super.init();
		try {
			usersDB = new UsersDB(pool);
			categoriesDB = new CategoriesDB(pool);
			questionsDB=new QuestionsDB(pool);
		} catch(Exception ex) {
			throw new ServletException(ex);
		}
		ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
		validator = validatorFactory.getValidator();
	}
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Perfil() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String path = request.getPathInfo();
		getCategoriesFromDB(request, response);
		String userIDFromPost=(String)request.getAttribute("userIDFromPost");
		 String usuarioID=request.getParameter("userID");
		 if(userIDFromPost!=null)
			 usuarioID=userIDFromPost;
			if(usuarioID != null) {
				//BUSCAR LOS DATOS DEL USUARIO CON EL ID DETERMINADO
				try {
					User user = usersDB.getUserById(usuarioID);
					
					if(user != null) {
						request.setAttribute("userInfo", user);
					} 
					
				}catch(SQLException e) {
				
					e.printStackTrace();
				}
			}
			
			int totalOfQuestions=0;
			int totalOfFavs=0;
			int totalOfLikes=0;
			int totalOfDislikes=0;
			int totalOfAnswers=0;
			
			try {
				totalOfQuestions=questionsDB.numberOfQuestionsByUser(usuarioID,1);
				totalOfFavs=questionsDB.numberOfQuestionsByUser(usuarioID,2);
				totalOfLikes=questionsDB.numberOfQuestionsByUser(usuarioID,3);
				totalOfDislikes=questionsDB.numberOfQuestionsByUser(usuarioID,4);
				totalOfAnswers=questionsDB.numberOfAnswersByUser(usuarioID);				
			} catch(SQLException ex) {
				ex.printStackTrace();
			}
		
		
		request.setAttribute("totalOfQuestions",totalOfQuestions);
		request.setAttribute("totalOfFavs",totalOfFavs);
		request.setAttribute("totalOfLikes",totalOfLikes);
		request.setAttribute("totalOfDislikes",totalOfDislikes);
		request.setAttribute("totalOfAnswers",totalOfAnswers);
		
		if(path != null) {
			switch(path) {
			case "/Preguntas":
				preguntas(request, response);
				break;
			case "/Respuestas":
				respuestas(request, response);
				break;
			case "/Dislikes":
				dislikes(request,response);
				break;
			case "/Favoritos":
				favoritos(request,response);
				break;
			case "/Votos":
				votos(request,response);
				break;
			
			}
		}else {
			
			preguntas(request, response);
		}
		
		//Manejar las rutas con parametros
		

		//request.getRequestDispatcher("/Pages/error.jsp").forward(request, response);
	}
		
	
	private boolean manageParameters(HttpServletRequest request, HttpServletResponse response) throws ServletException{
		String paramUserID = request.getParameter("userID");
		String userIDFromPost=(String)request.getAttribute("userIDFromPost");
		if(userIDFromPost!=null)
			paramUserID=userIDFromPost;
		boolean success = true;
		
		if(paramUserID != null) {
			//BUSCAR LOS DATOS DEL USUARIO CON EL ID DETERMINADO
			try {
				User user = usersDB.getUserById(paramUserID);
				
				if(user != null) {
					request.setAttribute("userInfo", user);
				} else {
					success = false;
				}
				
			}catch(SQLException e) {
				success = false;
				e.printStackTrace();
			}
		} else {
			success = false;
		}
		return success;
	}
	private void votos(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if(manageParameters(request, response)) {
			try {
				String userIDFromPost=(String)request.getAttribute("userIDFromPost");

				 String pageNumber = request.getParameter("pageNumber");
				 String usuarioID=request.getParameter("userID");
					int pageNumberInteger = 0;
					 if(userIDFromPost!=null)
						 usuarioID=userIDFromPost;
						if(usuarioID != null) {
							//BUSCAR LOS DATOS DEL USUARIO CON EL ID DETERMINADO
							try {
								User user = usersDB.getUserById(usuarioID);
								
								if(user != null) {
									request.setAttribute("userInfo", user);
								} 
								
							}catch(SQLException e) {
							
								e.printStackTrace();
							}
						} 
					if(pageNumber != null) {
						try {
							pageNumberInteger = Integer.parseInt(pageNumber);				
						}catch(Exception ex) {
							ex.printStackTrace();
						}
						
					} else {
						pageNumberInteger = 1;
					}
					
					
				List<QuestionDetailsViewModel> preguntas =questionsDB.getQuestionsByUser(usuarioID, pageNumberInteger,3);
				
				int totalOfResults=questionsDB.numberOfQuestionsByUser(usuarioID,3);
				int numberOfPages = totalOfResults / 10;
				if(totalOfResults % 10 > 0) {
					numberOfPages++;
				}
				if(pageNumberInteger <= numberOfPages && pageNumberInteger > 0 || numberOfPages == 0) {	
					request.setAttribute("TotalPreguntas",totalOfResults);
					request.setAttribute("numberOfPages", numberOfPages);
					request.setAttribute("pageNumber", pageNumberInteger);
					request.setAttribute("preguntasEncontradas", preguntas);
					request.getRequestDispatcher("/Pages/profile-votes.jsp").forward(request, response);			
				} else {
					request.getRequestDispatcher("/Pages/error.jsp").forward(request, response);					
				}
				}catch(SQLException e) {
					e.printStackTrace();
				}

		} else {
			request.getRequestDispatcher("/Pages/error.jsp").forward(request, response);
		}
	}
	private void respuestas(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if(manageParameters(request, response)) {
			try {
				String userIDFromPost=(String)request.getAttribute("userIDFromPost");
				 String pageNumber = request.getParameter("pageNumber");
				 String usuarioID=request.getParameter("userID");
					int pageNumberInteger = 0;
					if(userIDFromPost!=null)
						 usuarioID=userIDFromPost;
						if(usuarioID != null) {
							//BUSCAR LOS DATOS DEL USUARIO CON EL ID DETERMINADO
							try {
								User user = usersDB.getUserById(usuarioID);
								
								if(user != null) {
									request.setAttribute("userInfo", user);
								} 
								
							}catch(SQLException e) {
							
								e.printStackTrace();
							}
						} 
						
					if(pageNumber != null) {
						try {
							pageNumberInteger = Integer.parseInt(pageNumber);				
						}catch(Exception ex) {
							ex.printStackTrace();
						}
						
					} else {
						pageNumberInteger = 1;
					}
					
					
				List<QuestionDetailsViewModel> preguntas =questionsDB.getAnswersByUser(usuarioID, pageNumberInteger);
				
				int totalOfResults=questionsDB.numberOfAnswersByUser(usuarioID);
				int numberOfPages = totalOfResults / 10;
				if(totalOfResults % 10 > 0) {
					numberOfPages++;
				}
				if(pageNumberInteger <= numberOfPages && pageNumberInteger > 0 || numberOfPages == 0) {	
					request.setAttribute("TotalPreguntas",totalOfResults);
					request.setAttribute("numberOfPages", numberOfPages);
					request.setAttribute("pageNumber", pageNumberInteger);
					request.setAttribute("preguntasEncontradas", preguntas);
					request.getRequestDispatcher("/Pages/profile-answers.jsp").forward(request, response);								
				} else {
					request.getRequestDispatcher("/Pages/error.jsp").forward(request, response);
				}
				}catch(SQLException e) {
					e.printStackTrace();
				}
		} else {
			request.getRequestDispatcher("/Pages/error.jsp").forward(request, response);
		}
	}
	
	private void preguntas(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if(manageParameters(request, response)) {
			try {	
				String userIDFromPost=(String)request.getAttribute("userIDFromPost");
				 String usuarioID=request.getParameter("userID");
				 if(userIDFromPost!=null)
					 usuarioID=userIDFromPost;
					if(usuarioID != null) {
						//BUSCAR LOS DATOS DEL USUARIO CON EL ID DETERMINADO
						try {
							User user = usersDB.getUserById(usuarioID);
							
							if(user != null) {
								request.setAttribute("userInfo", user);
							} 
							
						}catch(SQLException e) {
						
							e.printStackTrace();
						}
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
					
					
				List<QuestionDetailsViewModel> preguntas =questionsDB.getQuestionsByUser(usuarioID, pageNumberInteger,1);
				
				int totalOfResults=questionsDB.numberOfQuestionsByUser(usuarioID,1);
				int numberOfPages = totalOfResults / 10;
				if(totalOfResults % 10 > 0) {
					numberOfPages++;
				}
				if(pageNumberInteger <= numberOfPages && pageNumberInteger > 0 || numberOfPages == 0) {	
					request.setAttribute("TotalPreguntas",totalOfResults);
					request.setAttribute("numberOfPages", numberOfPages);
					request.setAttribute("pageNumber", pageNumberInteger);
					request.setAttribute("preguntasEncontradas", preguntas);
					request.getRequestDispatcher("/Pages/profile.jsp").forward(request, response);
				} else {
					request.getRequestDispatcher("/Pages/error.jsp").forward(request, response);
				}
			}catch(SQLException e) {
				e.printStackTrace();
			}
		} else {
			//Poner vista de error que no se encontro el perfil
			request.getRequestDispatcher("/Pages/error.jsp").forward(request, response);	
		}
	}
	private void dislikes(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if(manageParameters(request, response)) {
			//Poner la vista de profile
			try {
				String userIDFromPost=(String)request.getAttribute("userIDFromPost");

				 String pageNumber = request.getParameter("pageNumber");
				 String usuarioID=request.getParameter("userID");
					int pageNumberInteger = 0;
					 if(userIDFromPost!=null)
						 usuarioID=userIDFromPost;
						if(usuarioID != null) {
							//BUSCAR LOS DATOS DEL USUARIO CON EL ID DETERMINADO
							try {
								User user = usersDB.getUserById(usuarioID);
								
								if(user != null) {
									request.setAttribute("userInfo", user);
								} 
								
							}catch(SQLException e) {
							
								e.printStackTrace();
							}
						} 
					if(pageNumber != null) {
						try {
							pageNumberInteger = Integer.parseInt(pageNumber);				
						}catch(Exception ex) {
							ex.printStackTrace();
						}
						
					} else {
						pageNumberInteger = 1;
					}
					
					
				List<QuestionDetailsViewModel> preguntas =questionsDB.getQuestionsByUser(usuarioID, pageNumberInteger,4);
				
				int totalOfResults=questionsDB.numberOfQuestionsByUser(usuarioID,4);
				int numberOfPages = totalOfResults / 10;
				if(totalOfResults % 10 > 0) {
					numberOfPages++;
				}
				if(pageNumberInteger <= numberOfPages && pageNumberInteger > 0 || numberOfPages == 0) {	
					request.setAttribute("numberOfPages", numberOfPages);
					request.setAttribute("pageNumber", pageNumberInteger);
					request.setAttribute("preguntasEncontradas", preguntas);
					request.getRequestDispatcher("/Pages/profile-dislikes.jsp").forward(request, response);			
				} else {
					request.getRequestDispatcher("/Pages/error.jsp").forward(request, response);
				}
				}catch(SQLException e) {
					e.printStackTrace();
				}
		} else {
			//Poner vista de error que no se encontro el perfil
			request.getRequestDispatcher("/Pages/error.jsp").forward(request, response);	
		}
	}
	private void favoritos(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if(manageParameters(request, response)) {
			String userIDFromPost=(String)request.getAttribute("userIDFromPost");

			 String pageNumber = request.getParameter("pageNumber");
			 String usuarioID=request.getParameter("userID");
			 try {
				int pageNumberInteger = 0;
				 if(userIDFromPost!=null)
					 usuarioID=userIDFromPost;
					if(usuarioID != null) {
						//BUSCAR LOS DATOS DEL USUARIO CON EL ID DETERMINADO
						try {
							User user = usersDB.getUserById(usuarioID);
							
							if(user != null) {
								request.setAttribute("userInfo", user);
							} 
							
						}catch(SQLException e) {
						
							e.printStackTrace();
						}
					} 
				if(pageNumber != null) {
					try {
						pageNumberInteger = Integer.parseInt(pageNumber);				
					}catch(Exception ex) {
						ex.printStackTrace();
					}
					
				} else {
					pageNumberInteger = 1;
				}
				
				
			
			List<QuestionDetailsViewModel> preguntas =questionsDB.getQuestionsByUser(usuarioID, pageNumberInteger,2);
			
			
			int totalOfResults=questionsDB.numberOfQuestionsByUser(usuarioID,2);
			int numberOfPages = totalOfResults / 10;
			if(totalOfResults % 10 > 0) {
				numberOfPages++;
			}
			//Poner la vista de profile
			if(pageNumberInteger <= numberOfPages && pageNumberInteger > 0 || numberOfPages == 0) {	
				request.setAttribute("numberOfPages", numberOfPages);
				request.setAttribute("pageNumber", pageNumberInteger);
				request.setAttribute("preguntasEncontradas", preguntas);			
				request.getRequestDispatcher("/Pages/profile-favorites.jsp").forward(request, response);			
			} else {
				request.getRequestDispatcher("/Pages/error.jsp").forward(request, response);
			}
			 } catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
			}
				
		} else {
			//Poner vista de error que no se encontro el perfil
			request.getRequestDispatcher("/Pages/error.jsp").forward(request, response);	
		}
	}
	/**
	 * @throws SQLException 
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void editar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException  {
		
		String nombre=request.getParameter("nombre");
		String apellidoP=request.getParameter("apellidoP");
		String apellidoM=request.getParameter("apellidoM");
		String correo=request.getParameter("correo");
		String contrasenaNueva=request.getParameter("contrasena1");
		User usuarioLogeado =(User) request.getSession().getAttribute("user");
		String contrasenaVieja=request.getParameter("contrasena2");
		String cadena_fecha=request.getParameter("fecha");
		InputStream imageStream = null;
		Part filePart = request.getPart("imagen");
		
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
		
		 Calendar calendar = Calendar.getInstance();
		 if(fecha!=null)
	     calendar.setTime(fecha);
		 else{
			errores.put("fecha_nacimiento","Ingrese algo en la fecha");
			calendar=null;
		 }
		//en el metodo post se obtienen los parametros desde los inputs y desde los gets desde los parametros de la url
		User userInfo=null;
		try {
			userInfo = usersDB.getUserById(usuarioLogeado.getID_Usuario());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(filePart != null) {
			imageStream = filePart.getInputStream();			
		}else {
			imageStream=null;
		}
		
		CambiarDatos user=new CambiarDatos(nombre,contrasenaNueva,correo,apellidoP,apellidoM,calendar,imageStream,true);
		
		
		Set<ConstraintViolation<CambiarDatos>> constraintViolations = validator.validate(user);
		
		if(!constraintViolations.isEmpty()) {
	
			for(ConstraintViolation<CambiarDatos> constraintViolation : constraintViolations) {
			
				errores.put(constraintViolation.getPropertyPath().toString(),constraintViolation.getMessage());
			}
			
			if(errores.get("contra")!=null && (contrasenaNueva==null || contrasenaNueva.equals("")) && (contrasenaVieja.equals("") || contrasenaVieja==null)) {
				errores.remove("contra");
				user.setContra(null);
			}
			
			if(!errores.isEmpty()) {
				
				request.setAttribute("userIDFromPost", usuarioLogeado.getID_Usuario());
				request.setAttribute("errores", errores);
				request.setAttribute("usuarioEdit", usuarioLogeado);
				request.setAttribute("cadena_fecha", cadena_fecha);

				request.setAttribute("Modal", "modal-bg bg-active");
				request.setAttribute("cadena_fecha", cadena_fecha);
				doGet(request,response);
			}
			//request.getRequestDispatcher("/Pages/profile.jsp").forward(request, response);
		}
		
			User usuarioTest=null;
			try {
				usuarioTest = usersDB.login(usuarioLogeado.getID_Usuario(), contrasenaVieja);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			user.setID_Usuario(usuarioLogeado.getID_Usuario());
				
		if(usuarioTest==null&& !contrasenaNueva.equals("")) {
			request.setAttribute("userIDFromPost", usuarioLogeado.getID_Usuario());
			errores.put("ErrorContra", "Contrasena incorrecta");
			request.setAttribute("errores", errores);
			request.setAttribute("cadena_fecha", cadena_fecha);
			request.setAttribute("usuarioEdit", usuarioLogeado);
			request.setAttribute("Modal", "modal-bg bg-active");
			doGet(request,response);
			
		} 
		 
		else {
			if(!errores.isEmpty()) {
				
				request.setAttribute("userIDFromPost", usuarioLogeado.getID_Usuario());
				request.setAttribute("errores", errores);
				request.setAttribute("cadena_fecha", cadena_fecha);
				request.setAttribute("usuarioEdit", usuarioLogeado);
				request.setAttribute("Modal", "modal-bg bg-active");
				
				doGet(request,response);
			}else {
				
				try {
					Blob image =usersDB.getImageByUserId(user.getID_Usuario());
					if(image != null) {
						if(user.getImagen().available()==0)
							user.setImagen(image.getBinaryStream());						
					}
					
					boolean estado=usersDB.editar_usuario(user);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				user.swapData(usuarioLogeado, user);
				request.setAttribute("userIDFromPost", user.getID_Usuario());
				request.setAttribute("Modal", "modal-bg");
				request.setAttribute("cadena_fecha", cadena_fecha);

				request.getSession().setAttribute("user", usuarioLogeado);
				doGet(request,response);
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
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//En el do post va el value del hidden y el action se le pone la ruta que quiero que agarre
		String accion=request.getParameter("accion");
		if(accion!=null) {
			
			switch(accion) {
			case "Editar":
				try {
					editar(request,response);
				} catch (ServletException | IOException | SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			}
			doGet(request, response);
		}
	}

}
