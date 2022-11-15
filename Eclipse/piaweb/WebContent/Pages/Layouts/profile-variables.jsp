
<%@ page import="com.piaweb.models.*,com.piaweb.viewmodels.QuestionDetailsViewModel,java.util.*,java.sql.Blob,java.text.DateFormat, java.text.SimpleDateFormat" %>

<%
boolean showErrors=false;
//Son los datos actualizados desde la basee de datos del usuario logeado
	int numberOfPages = (int)request.getAttribute("numberOfPages");
	int pageNumber = (int)request.getAttribute("pageNumber");
	
	List<QuestionDetailsViewModel> preguntas=(List<QuestionDetailsViewModel>)request.getAttribute("preguntasEncontradas");

	User user = (User)session.getAttribute("user");
	
	Date date=null;
	String fecha_nacimiento="";
	fecha_nacimiento=(String)request.getAttribute("cadena_fecha");
	if(user!=null && fecha_nacimiento==null){
		
    date =user.getFecha_nacimiento().getTime();
 	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    
    fecha_nacimiento = dateFormat.format(date);
    
	}
	
	
	
	User userProfile = (User)request.getAttribute("userInfo");
	
	String contrasenaIncorrecta=(String)request.getAttribute("ErrorContra");
	

	
	Map<String,String> errores =(Map<String,String>)request.getAttribute("errores");
		if(errores!=null)
			showErrors=true;
		String iconoError="<i class='fas fa-exclamation-circle'></i>";	
		
		String modal=(String)request.getAttribute("Modal");
		
	
%>