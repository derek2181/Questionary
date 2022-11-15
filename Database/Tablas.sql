
-- drop database piaweb;
CREATE DATABASE piaweb;
use piaweb;
CREATE TABLE usuarios(
ID_Usuario VARCHAR(20) PRIMARY KEY NOT NULL,
nombre VARCHAR(20) NOT NULL,
contra VARCHAR(20) NOT NULL,
correo VARCHAR(40) NOT NULL,
apellidoP VARCHAR(20) NOT NULL,
apellidoM VARCHAR (20) NOT NULL,
fecha_nacimiento DATE NOT NULL,
imagen BLOB,
activo BIT DEFAULT 1
);
ALTER TABLE usuarios ADD COLUMN creacion DATETIME DEFAULT CURRENT_TIMESTAMP;
CREATE TABLE categorias(
ID_Categoria INT PRIMARY KEY AUTO_INCREMENT,
nombre VARCHAR(30) NOT NULL
);

INSERT INTO categorias(nombre) VALUES("Videojuegos");
CREATE TABLE preguntas(
ID_Pregunta INT  PRIMARY KEY NOT NULL AUTO_INCREMENT,
encabezado VARCHAR(50) NOT NULL,
descripcion VARCHAR(255) NOT NULL,
fecha DATETIME,
Imagen MEDIUMBLOB,
ID_Usuario VARCHAR(20) NOT NULL,
ID_Categoria INT NOT NULL,
activo BIT DEFAULT 1,
ID_RespuestaCorrecta INT,
CONSTRAINT FK_preguntas_respuestas FOREIGN KEY(ID_RespuestaCorrecta) REFERENCES respuestas(ID_Respuesta),
CONSTRAINT  FK_preguntas_usuarios FOREIGN KEY (ID_Usuario) REFERENCES usuarios(ID_Usuario),
CONSTRAINT  FK_preguntas_categorias FOREIGN KEY (ID_Categoria) REFERENCES categorias(ID_Categoria)
);
ALTER TABLE preguntas ADD COLUMN activo_editar BIT DEFAULT NULL;

CREATE TABLE respuestas(
ID_Respuesta INT PRIMARY KEY AUTO_INCREMENT,
respuesta VARCHAR(40),
imagen VARCHAR(255),
fecha DATETIME,
activo_editar BIT DEFAULT 1,
activo_eliminar BIT DEFAULT 1,
ID_Pregunta INT,
ID_Usuario VARCHAR(20), 
CONSTRAINT  FK_respuestas_preguntas FOREIGN KEY (ID_Pregunta) REFERENCES preguntas(ID_Pregunta),
CONSTRAINT  FK_respuestas_usuarios FOREIGN KEY (ID_Usuario) REFERENCES usuarios(ID_Usuario)

);
ALTER TABLE usuario_respuesta CHANGE UTIL util BIT ;
 
CREATE TABLE usuario_respuesta(
ID_Usuario VARCHAR(20), 
ID_Respuesta INT,
PRIMARY KEY(ID_Usuario,ID_Respuesta),
CONSTRAINT  FK_usuario_respuesta_respuesta FOREIGN KEY (ID_Respuesta) REFERENCES respuestas(ID_Respuesta),
CONSTRAINT  FK_usuario_respuesta_usuario FOREIGN KEY (ID_Usuario) REFERENCES usuarios(ID_Usuario)

);
CREATE TABLE usuario_pregunta(
ID_Usuario VARCHAR(20), 
ID_Pregunta INT,
util BIT DEFAULT NULL,
favorito BIT DEFAULT NULL,
PRIMARY KEY(ID_Usuario,ID_Pregunta),
CONSTRAINT  FK_usuario_pregunta_preguntas FOREIGN KEY (ID_Pregunta) REFERENCES preguntas(ID_Pregunta),
CONSTRAINT  FK_usuario_pregunta_usuarios FOREIGN KEY (ID_Usuario) REFERENCES usuarios(ID_Usuario)
);



/*
PROCEDURES  */
-- usuarios
CALL sp_Usuarios('A',"Esteban","adereka@hotmail.com","12345678","cortes","sad","02/08/06","sadf",null);
-- preguntas, los ultimos dos son, id de usuario y id de pregunta


CALL sp_Preguntas('S',null,null,null,null,null,null,"derix",null,0,"dora",null,null,null,null,null,null);

CALL sp_Preguntas('Q',null,null,null,null,null,null,null,null,0, "derix",null,null,null,null,null);


-- respuestas, los ultimos dos son, id de pregunta y id de usuario
CALL sp_Respuestas('A',null,"Respuesta","Imagen prueba","12/02/21",2,"derix");

INSERT INTO preguntas(encabezado,descripcion,fecha,imagen,ID_Usuario,ID_Categoria) 
VALUES ("LA COLONIA VILLALUZ ?","Que tal esta diganme todos","15/02/21",null,"derix",1);

INSERT INTO usuario_pregunta(ID_Usuario,ID_Pregunta,util,favorito) VALUES("Esteban",1,null,1);
CALL sp_Preguntas('Q',null,null,null,null,null,null,'derix',null,0,null,null,null,null,null,null);
CALL sp_Usuario_Pregunta('F',"derix",1,true);

UPDATE usuario_pregunta SET favorito=null WHERE usuario_pregunta.ID_Pregunta=1;

UPDATE usuarios SET activo=0 WHERE ID_Usuario="Esteban";