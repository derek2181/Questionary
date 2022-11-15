
INSERT INTO usuarios(ID_Usuario,nombre,contra,correo,apellidoP,apellidoM,fecha_nacimiento,activo) VALUES("derix","derix","123","123","sdfs","asdf","02/02/10",1);
CREATE DATABASE piaweb;
use piaweb;
DROP PROCEDURE IF EXISTS `sp_Usuarios`;
DELIMITER //
CREATE PROCEDURE sp_Usuarios(
IN Opcion CHAR(1),
IN ID_UsuarioP VARCHAR(20),
IN correo VARCHAR(40),
IN contra VARCHAR(20),
IN apellidoP VARCHAR(20),
IN apellidoM VARCHAR (20),
IN fecha_nacimiento DATE,
IN nombre VARCHAR(20),
IN imagen MEDIUMBLOB
)
BEGIN
IF Opcion='A'
THEN
INSERT INTO usuarios(ID_Usuario,contra,correo,apellidoP,apellidoM,fecha_nacimiento,nombre,activo) VALUES(ID_UsuarioP,contra,correo,apellidoP,apellidoM,fecha_nacimiento,nombre,1);
END IF;
IF Opcion='G'
THEN
SELECT usuarios.ID_Usuario,usuarios.nombre,usuarios.correo, usuarios.apellidoP, usuarios.apellidoM,usuarios.fecha_nacimiento,usuarios.activo, usuarios.imagen FROM usuarios WHERE usuarios.ID_Usuario=ID_UsuarioP AND usuarios.contra=contra;
END IF;
IF Opcion='D'
THEN
SELECT usuarios.ID_Usuario,usuarios.nombre, usuarios.correo, usuarios.apellidoP, usuarios.apellidoM,usuarios.fecha_nacimiento, usuarios.activo FROM usuarios WHERE usuarios.ID_Usuario=ID_UsuarioP;
END IF;

IF Opcion='B'
THEN
UPDATE usuarios SET usuarios.activo = 0 WHERE usuarios.ID_Usuario=ID_UsuarioP;
END IF;
IF Opcion='C'
THEN
UPDATE usuarios SET 
usuarios.contra= COALESCE(contra,usuarios.contra),
usuarios.apellidoP=COALESCE(apellidoP,usuarios.apellidoP),
usuarios.apellidoM=COALESCE(apellidoM,usuarios.apellidoM),
usuarios.correo=COALESCE(correo,usuarios.correo),
usuarios.imagen=COALESCE(imagen,usuarios.imagen),
usuarios.nombre=COALESCE(nombre,usuarios.nombre)
 WHERE usuarios.ID_Usuario=ID_UsuarioP;
END IF;

IF Opcion='I'
THEN
SELECT usuarios.imagen FROM usuarios WHERE usuarios.ID_Usuario=ID_UsuarioP;

END IF;
END //
DELIMITER ;

DELIMITER ;


DROP PROCEDURE IF EXISTS sp_Preguntas;
Delimiter //
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_Preguntas`(
Opcion CHAR(1),
ID_PreguntaP INT,
encabezado VARCHAR(50),
descripcion VARCHAR(255),
fecha DATETIME,
fecha2 DATETIME,
Imagen MEDIUMBLOB,
ID_Usuario VARCHAR(20),
ID_CategoriaP  INT,
inicioPagina INT,
busqueda VARCHAR(50),
categoria VARCHAR(20),
votoInferior INT,
votoSuperior INT,
favoritoInferior INT,
favoritoSuperior INT,
ID_RespuestaCorrectaP INT
)
BEGIN 
IF Opcion='A'
THEN
INSERT INTO preguntas(encabezado,descripcion,fecha,imagen,ID_Usuario,ID_Categoria) VALUES (encabezado,descripcion,DATE_ADD(fecha,INTERVAL -6 HOUR),imagen,ID_Usuario,ID_CategoriaP);

END IF;
IF Opcion='B'
THEN
UPDATE preguntas SET activo=0 WHERE preguntas.ID_Pregunta=ID_PreguntaP;
END IF;
IF Opcion='C'
THEN 
UPDATE preguntas SET 
preguntas.encabezado=encabezado,
preguntas.descripcion=descripcion,
preguntas.imagen=Imagen,
preguntas.ID_Categoria = ID_CategoriaP,
preguntas.activo_editar = 1
WHERE preguntas.ID_Pregunta=ID_PreguntaP;
END IF;
IF Opcion='1'
THEN
SELECT preguntas.ID_Pregunta, preguntas.encabezado, preguntas.fecha,preguntas.activo, usuarios.ID_Usuario,
 usuarios.nombre + usuarios.apellidoP'nombreUsuario', categorias.ID_Categoria, categorias.nombre 'nombreCategoria',
 COUNT(case usuario_pregunta.util when 1 then 1 else null end) 'Likes',
COUNT(case usuario_pregunta.util when 0 then 1 else null end) 'Dislikes'
 FROM preguntas JOIN usuarios ON usuarios.ID_Usuario = preguntas.ID_Usuario
 JOIN categorias ON categorias.ID_Categoria = preguntas.ID_Categoria 
 LEFT JOIN usuario_pregunta ON usuario_pregunta.ID_Pregunta = preguntas.ID_Pregunta WHERE preguntas.activo = 1 GROUP BY preguntas.ID_Pregunta ORDER BY preguntas.fecha DESC LIMIT inicioPagina, 10;
END IF;
IF Opcion='2'
THEN
SELECT COUNT(preguntas.ID_Pregunta) FROM preguntas JOIN usuarios ON usuarios.ID_Usuario = preguntas.ID_Usuario JOIN categorias ON categorias.ID_Categoria = preguntas.ID_Categoria WHERE preguntas.activo = 1;
END IF;
IF Opcion='G'
THEN
SELECT preguntas.ID_Pregunta, preguntas.encabezado, preguntas.descripcion, preguntas.fecha, preguntas.imagen 'imagenPregunta', 
preguntas.activo,preguntas.activo_editar, usuarios.ID_Usuario, usuarios.nombre 'nombreUsuario', usuarios.apellidoP, categorias.ID_Categoria, 
categorias.nombre 'nombreCategoria', COUNT(case usuario_pregunta.util when 1 then 1 else null end) 'Likes',
COUNT(case usuario_pregunta.favorito when 1 then 1 else null end) 'Favorito',
COUNT(case usuario_pregunta.util when 0 then 1 else null end) 'Dislikes',
(CASE WHEN (usuario_pregunta.ID_Usuario=ID_Usuario AND usuario_pregunta.Util=true) THEN 1
WHEN (usuario_pregunta.ID_Usuario=ID_Usuario AND usuario_pregunta.Util=false) THEN 0 ELSE 3 end) 'Liked',
(CASE WHEN (usuario_pregunta.ID_Usuario=ID_Usuario AND usuario_pregunta.favorito=true) THEN 1 ELSE 0 END) 'Faved' 
FROM preguntas JOIN usuarios ON usuarios.ID_Usuario = preguntas.ID_Usuario 
JOIN categorias ON categorias.ID_Categoria = preguntas.ID_Categoria
LEFT JOIN usuario_pregunta ON usuario_pregunta.ID_Pregunta = preguntas.ID_Pregunta  WHERE preguntas.ID_Pregunta = ID_PreguntaP AND preguntas.activo = 1;
END IF;
IF Opcion='I'
THEN
SELECT preguntas.imagen FROM preguntas WHERE ID_Pregunta = ID_PreguntaP;
END IF;

IF Opcion='U'
THEN
SELECT preguntas.ID_Pregunta, preguntas.encabezado, preguntas.fecha,preguntas.activo, usuarios.ID_Usuario, usuarios.nombre + usuarios.apellidoP'nombreUsuario', categorias.ID_Categoria, categorias.nombre 'nombreCategoria'  FROM preguntas JOIN usuarios ON usuarios.ID_Usuario = preguntas.ID_Usuario JOIN categorias ON categorias.ID_Categoria = preguntas.ID_Categoria WHERE preguntas.ID_Categoria=ID_CategoriaP AND preguntas.activo = 1 LIMIT inicioPagina, 10;
END IF;

IF Opcion='N'
THEN
SELECT COUNT(ID_Pregunta) FROM preguntas WHERE ID_Categoria = ID_CategoriaP AND activo = 1;
END IF;

IF Opcion='S'
THEN 
SELECT preguntas.ID_Pregunta, preguntas.encabezado, preguntas.descripcion, preguntas.fecha, preguntas.imagen 
'imagenPregunta', preguntas.activo, usuarios.ID_Usuario, usuarios.nombre 'nombreUsuario', usuarios.apellidoP, 
categorias.ID_Categoria, categorias.nombre 'nombreCategoria',COUNT(case usuario_pregunta.util when 1 then 1 else null end) 'Likes',
COUNT(case usuario_pregunta.favorito when 1 then 1 else null end) 'Favorito',
COUNT(case usuario_pregunta.util when 0 then 1 else null end) 'Dislikes' FROM preguntas JOIN usuarios 
ON usuarios.ID_Usuario = preguntas.ID_Usuario JOIN categorias ON categorias.ID_Categoria = preguntas.ID_Categoria
LEFT JOIN usuario_pregunta ON usuario_pregunta.ID_Pregunta = preguntas.ID_Pregunta WHERE preguntas.activo = 1 AND preguntas.encabezado like CONCAT('%',busqueda,'%') 
AND ((fecha IS NULL AND fecha2 IS NULL) OR  preguntas.fecha between fecha AND fecha2) AND (categoria IS NULL OR categorias.nombre=categoria) GROUP BY preguntas.ID_Pregunta 
HAVING (votoInferior IS NULL OR COUNT(case usuario_pregunta.util when 1 then 1 else null end)>=votoInferior) AND (votoSuperior IS NULL OR COUNT(case usuario_pregunta.util
 when 1 then 1 else null end)<=votoSuperior)
 AND (favoritoInferior IS NULL OR COUNT(case usuario_pregunta.favorito when 1 then 1 else null end)>=favoritoInferior) 
 AND (favoritoSuperior IS NULL OR COUNT(case usuario_pregunta.favorito when 1 then 1 else null end)<=favoritoSuperior) LIMIT inicioPagina, 10 ;
END IF;


IF Opcion='Z'
THEN
SELECT  COUNT(DISTINCT preguntas.ID_Pregunta) 'contador'  FROM preguntas JOIN usuarios 
ON usuarios.ID_Usuario = preguntas.ID_Usuario JOIN categorias ON categorias.ID_Categoria = preguntas.ID_Categoria
LEFT JOIN usuario_pregunta ON usuario_pregunta.ID_Pregunta = preguntas.ID_Pregunta WHERE preguntas.activo = 1 AND preguntas.encabezado like CONCAT('%',busqueda,'%') 
AND (fecha IS NULL OR fecha2 IS NULL OR preguntas.fecha between fecha AND fecha2) AND (categoria IS NULL OR categorias.nombre=categoria) GROUP BY 'contador' 
HAVING (votoInferior IS NULL OR COUNT(case usuario_pregunta.util when 1 then 1 else null end)>=votoInferior) AND (votoSuperior IS NULL OR COUNT(case usuario_pregunta.util
 when 1 then 1 else null end)<=votoSuperior)
 AND (favoritoInferior IS NULL OR COUNT(case usuario_pregunta.favorito when 1 then 1 else null end)>=favoritoInferior) 
 AND (favoritoSuperior IS NULL OR COUNT(case usuario_pregunta.favorito when 1 then 1 else null end)<=favoritoSuperior);
END IF;

-- Cuales preguntas ha hecho el usuario

IF Opcion='Q'
THEN
SELECT preguntas.ID_Pregunta, preguntas.encabezado, preguntas.descripcion, preguntas.fecha, preguntas.imagen 
'imagenPregunta', preguntas.activo, usuarios.ID_Usuario, usuarios.nombre 'nombreUsuario', usuarios.apellidoP, 
categorias.ID_Categoria, categorias.nombre 'nombreCategoria', COUNT(case usuario_pregunta.util when 1 then 1 else null end) 'Likes',
COUNT(case usuario_pregunta.favorito when 1 then 1 else null end) 'Favorito',
COUNT(case usuario_pregunta.util when 0 then 1 else null end) 'Dislikes'  FROM preguntas JOIN usuarios 
ON usuarios.ID_Usuario = preguntas.ID_Usuario JOIN categorias ON categorias.ID_Categoria = preguntas.ID_Categoria
LEFT JOIN usuario_pregunta ON usuario_pregunta.ID_Pregunta = preguntas.ID_Pregunta  WHERE preguntas.ID_Usuario=ID_Usuario AND preguntas.activo = 1 GROUP BY preguntas.ID_Pregunta LIMIT inicioPagina, 10;
END IF;
-- cuales preguntas le da dado fav el usuario
IF Opcion='F'
THEN
SELECT preguntas.ID_Pregunta, preguntas.encabezado, preguntas.descripcion, preguntas.fecha, preguntas.imagen 
'imagenPregunta', preguntas.activo, usuarios.ID_Usuario, usuarios.nombre 'nombreUsuario', usuarios.apellidoP, 
categorias.ID_Categoria, categorias.nombre 'nombreCategoria' ,
 COUNT(case usuario_pregunta.util when 1 then 1 else null end) 'Likes',
COUNT(case usuario_pregunta.favorito when 1 then 1 else null end) 'Favorito',
COUNT(case usuario_pregunta.util when 0 then 1 else null end) 'Dislikes' FROM preguntas JOIN usuarios 
ON usuarios.ID_Usuario = preguntas.ID_Usuario JOIN categorias ON categorias.ID_Categoria = preguntas.ID_Categoria
LEFT JOIN usuario_pregunta ON usuario_pregunta.ID_Pregunta = preguntas.ID_Pregunta WHERE usuario_pregunta.ID_Usuario=ID_Usuario AND usuario_pregunta.favorito=true AND preguntas.activo = 1 GROUP BY preguntas.ID_Pregunta LIMIT inicioPagina, 10 ;
END IF;

-- cuales preguntas le da dado like el usuario
IF Opcion='X'
THEN
SELECT preguntas.ID_Pregunta, preguntas.encabezado, preguntas.descripcion, preguntas.fecha, preguntas.imagen 
'imagenPregunta', preguntas.activo, usuarios.ID_Usuario, usuarios.nombre 'nombreUsuario', usuarios.apellidoP, 
categorias.ID_Categoria, categorias.nombre 'nombreCategoria' ,
 COUNT(case usuario_pregunta.util when 1 then 1 else null end) 'Likes',
COUNT(case usuario_pregunta.favorito when 1 then 1 else null end) 'Favorito',
COUNT(case usuario_pregunta.util when 0 then 1 else null end) 'Dislikes' FROM preguntas JOIN usuarios 
ON usuarios.ID_Usuario = preguntas.ID_Usuario JOIN categorias ON categorias.ID_Categoria = preguntas.ID_Categoria
LEFT JOIN usuario_pregunta ON usuario_pregunta.ID_Pregunta = preguntas.ID_Pregunta WHERE usuario_pregunta.ID_Usuario=ID_Usuario AND usuario_pregunta.util=true AND preguntas.activo = 1 GROUP BY preguntas.ID_Pregunta LIMIT inicioPagina, 10 ;
END IF;

-- cuales preguntas le da dado dislike el usuario
IF Opcion='M'
THEN
SELECT preguntas.ID_Pregunta, preguntas.encabezado, preguntas.descripcion, preguntas.fecha, preguntas.imagen 
'imagenPregunta', preguntas.activo, usuarios.ID_Usuario, usuarios.nombre 'nombreUsuario', usuarios.apellidoP, 
categorias.ID_Categoria, categorias.nombre 'nombreCategoria' ,
 COUNT(case usuario_pregunta.util when 1 then 1 else null end) 'Likes',
COUNT(case usuario_pregunta.favorito when 1 then 1 else null end) 'Favorito',
COUNT(case usuario_pregunta.util when 0 then 1 else null end) 'Dislikes' FROM preguntas JOIN usuarios 
ON usuarios.ID_Usuario = preguntas.ID_Usuario JOIN categorias ON categorias.ID_Categoria = preguntas.ID_Categoria
LEFT JOIN usuario_pregunta ON usuario_pregunta.ID_Pregunta = preguntas.ID_Pregunta WHERE usuario_pregunta.ID_Usuario=ID_Usuario AND usuario_pregunta.util=false AND preguntas.activo = 1 GROUP BY preguntas.ID_Pregunta LIMIT inicioPagina, 10 ;
END IF;
-- Cuantas preguntas ha hecho el usuario
IF Opcion='Y'
THEN
SELECT COUNT(preguntas.ID_Pregunta) FROM preguntas JOIN usuarios 
ON usuarios.ID_Usuario = preguntas.ID_Usuario JOIN categorias ON categorias.ID_Categoria = preguntas.ID_Categoria
 WHERE preguntas.ID_Usuario=ID_Usuario AND preguntas.activo = 1
 ;END IF;
 -- Cuantas preguntas ha hecho dado fav el usuario
 IF Opcion='H'
THEN
SELECT COUNT(preguntas.ID_Pregunta) FROM preguntas JOIN usuarios 
ON usuarios.ID_Usuario = preguntas.ID_Usuario JOIN categorias ON categorias.ID_Categoria = preguntas.ID_Categoria
LEFT JOIN usuario_pregunta ON usuario_pregunta.ID_Pregunta = preguntas.ID_Pregunta WHERE usuario_pregunta.ID_Usuario=ID_Usuario AND usuario_pregunta.favorito=true AND preguntas.activo = 1 ;
END IF;
  -- Cuantas preguntas ha dado like el usuario
 IF Opcion='W'
THEN
SELECT COUNT(preguntas.ID_Pregunta) FROM preguntas JOIN usuarios 
ON usuarios.ID_Usuario = preguntas.ID_Usuario JOIN categorias ON categorias.ID_Categoria = preguntas.ID_Categoria
LEFT JOIN usuario_pregunta ON usuario_pregunta.ID_Pregunta = preguntas.ID_Pregunta WHERE usuario_pregunta.ID_Usuario=ID_Usuario AND usuario_pregunta.util=true AND preguntas.activo = 1;
END IF;
 
  -- Cuantas preguntas ha dado dislike el usuario
 IF Opcion='V'
THEN
SELECT COUNT(preguntas.ID_Pregunta) FROM preguntas JOIN usuarios 
ON usuarios.ID_Usuario = preguntas.ID_Usuario JOIN categorias ON categorias.ID_Categoria = preguntas.ID_Categoria
LEFT JOIN usuario_pregunta ON usuario_pregunta.ID_Pregunta = preguntas.ID_Pregunta WHERE usuario_pregunta.ID_Usuario=ID_Usuario AND usuario_pregunta.util=false AND preguntas.activo = 1;
END IF;

IF Opcion='R'
THEN
UPDATE preguntas SET preguntas.ID_RespuestaCorrecta = ID_RespuestaCorrectaP WHERE ID_Pregunta = ID_PreguntaP;
END IF;
END //

Delimiter //
CREATE DEFINER=root@localhost PROCEDURE sp_Categorias(
Opcion CHAR(1),
ID_CategoriaP INT
)
BEGIN
IF Opcion='I'
THEN
    SELECT * FROM categorias WHERE ID_Categoria =ID_CategoriaP;
END IF;
IF Opcion='G'
THEN
    SELECT * FROM categorias;
END IF;
END //


DROP PROCEDURE IF EXISTS sp_Usuario_Pregunta;
Delimiter //
CREATE PROCEDURE sp_Usuario_Pregunta(
IN Opcion CHAR,
IN ID_UsuarioD VARCHAR(20),
IN ID_PreguntaD INT,
IN utilD BIT
)
BEGIN

IF Opcion='A'

THEN 
SET @userNull=3;
IF EXISTS(SELECT util FROM usuario_pregunta WHERE usuario_pregunta.ID_Usuario=ID_UsuarioD AND usuario_pregunta.ID_Pregunta=ID_PreguntaD)
THEN
SELECT util FROM usuario_pregunta WHERE usuario_pregunta.ID_Usuario=ID_UsuarioD AND usuario_pregunta.ID_Pregunta=ID_PreguntaD INTO @userNull;
ELSE
SET @userNull=3;
END IF;

CASE 
WHEN @userNull=3 THEN
INSERT INTO usuario_pregunta(ID_Usuario, ID_Pregunta, util) VALUES (ID_UsuarioD,ID_PreguntaD,utilD);
WHEN @userNull is null THEN
UPDATE usuario_pregunta SET util=utilD WHERE usuario_pregunta.ID_Usuario=ID_UsuarioD AND usuario_pregunta.ID_Pregunta=ID_PreguntaD;
WHEN (@userNull=1 AND utilD=1) THEN
UPDATE usuario_pregunta SET util=null WHERE usuario_pregunta.ID_Usuario=ID_UsuarioD AND usuario_pregunta.ID_Pregunta=ID_PreguntaD;
WHEN (@userNull=0 AND utilD=0) THEN
UPDATE usuario_pregunta SET util=null WHERE usuario_pregunta.ID_Usuario=ID_UsuarioD AND usuario_pregunta.ID_Pregunta=ID_PreguntaD;
WHEN ((@userNull=1 AND utilD=0) OR (@userNull=0 AND utilD=1)) THEN 
UPDATE usuario_pregunta SET util=utilD WHERE usuario_pregunta.ID_Usuario=ID_UsuarioD AND usuario_pregunta.ID_Pregunta=ID_PreguntaD;
END CASE;


DELETE FROM usuario_pregunta WHERE usuario_pregunta.ID_Usuario=ID_UsuarioD AND usuario_pregunta.ID_Pregunta=ID_PreguntaD
AND util is null AND favorito is null;
CALL sp_Preguntas("G",ID_PreguntaD,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null);
END IF;

IF Opcion='C'
THEN
UPDATE usuario_pregunta
SET usuario_pregunta.util=utilD WHERE usuario_pregunta.ID_Usuario=ID_UsuarioD AND usuario_pregunta.ID_Pregunta=ID_PreguntaD;
END IF;

IF Opcion='F'
THEN
SET @userNull=3;
IF EXISTS(SELECT util FROM usuario_pregunta WHERE usuario_pregunta.ID_Usuario=ID_UsuarioD AND usuario_pregunta.ID_Pregunta=ID_PreguntaD)
THEN
SELECT favorito FROM usuario_pregunta WHERE usuario_pregunta.ID_Usuario=ID_UsuarioD AND usuario_pregunta.ID_Pregunta=ID_PreguntaD INTO @userNull;
ELSE
SET @userNull=3;
END IF;

CASE
WHEN @userNull=3 THEN
INSERT INTO usuario_pregunta(ID_Usuario,ID_Pregunta,favorito) VALUES(ID_UsuarioD,ID_PreguntaD,1);
WHEN @userNull is null THEN
UPDATE usuario_pregunta SET favorito=1 WHERE usuario_pregunta.ID_Usuario=ID_UsuarioD AND usuario_pregunta.ID_Pregunta=ID_PreguntaD;

WHEN @userNull=1 THEN
UPDATE usuario_pregunta SET favorito=null WHERE usuario_pregunta.ID_Usuario=ID_UsuarioD AND usuario_pregunta.ID_Pregunta=ID_PreguntaD;
END CASE;

DELETE FROM usuario_pregunta WHERE usuario_pregunta.ID_Usuario=ID_UsuarioD AND usuario_pregunta.ID_Pregunta=ID_PreguntaD
AND util is null AND favorito is null;
CALL sp_Preguntas("G",ID_PreguntaD,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null);
END IF;
END //



call sp_Usuario_Respuesta("derix",10,true);
DROP PROCEDURE IF EXISTS sp_Usuario_Respuesta;
Delimiter //
CREATE PROCEDURE sp_Usuario_Respuesta(
IN ID_UsuarioD VARCHAR(20),
IN ID_RespuestaD INT,
IN utilD BIT
)
BEGIN
SET @userNull=3;
IF EXISTS(SELECT util FROM usuario_respuesta WHERE usuario_respuesta.ID_Usuario=ID_UsuarioD AND usuario_respuesta.ID_Respuesta=ID_RespuestaD)
THEN
SELECT util FROM usuario_respuesta WHERE usuario_respuesta.ID_Usuario=ID_UsuarioD AND usuario_respuesta.ID_Respuesta=ID_RespuestaD INTO @userNull;
ELSE
SET @userNull=3;
END IF;

CASE 
WHEN @userNull=3 THEN
INSERT INTO usuario_respuesta(ID_Usuario, ID_Respuesta, util) VALUES (ID_UsuarioD,ID_RespuestaD,utilD);
WHEN @userNull is null THEN
UPDATE usuario_respuesta SET util=utilD WHERE usuario_respuesta.ID_Usuario=ID_UsuarioD AND usuario_respuesta.ID_Respuesta=ID_RespuestaD;
WHEN (@userNull=1 AND utilD=1) THEN
UPDATE usuario_respuesta SET util=null  WHERE usuario_respuesta.ID_Usuario=ID_UsuarioD AND usuario_respuesta.ID_Respuesta=ID_RespuestaD;
WHEN (@userNull=0 AND utilD=0) THEN
UPDATE usuario_respuesta SET util=null WHERE usuario_respuesta.ID_Usuario=ID_UsuarioD AND usuario_respuesta.ID_Respuesta=ID_RespuestaD;
WHEN ((@userNull=1 AND utilD=0) OR (@userNull=0 AND utilD=1)) THEN 
UPDATE usuario_respuesta SET util=utilD  WHERE usuario_respuesta.ID_Usuario=ID_UsuarioD AND usuario_respuesta.ID_Respuesta=ID_RespuestaD;
END CASE;
DELETE FROM usuario_respuesta WHERE usuario_respuesta.ID_Usuario=ID_UsuarioD AND usuario_respuesta.ID_Respuesta=ID_RespuestaD AND usuario_respuesta.util is null;
CALL sp_Respuestas("2",ID_RespuestaD, null, null, null, null,null,null,null,null);

END //

-- respuestas
DROP PROCEDURE IF EXISTS sp_Respuestas;
Delimiter //
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_Respuestas`(
Opcion CHAR(1),
ID_RespuestaP INT,
respuesta VARCHAR(255),
imagen MEDIUMBLOB,
fecha DATETIME,
activo_editar BIT,
activo_eliminar BIT,
ID_PreguntaP INT,
ID_UsuarioP VARCHAR(20),
inicio int
)
BEGIN
IF Opcion='A'
THEN 
INSERT INTO respuestas(respuesta,imagen,fecha,ID_Pregunta,ID_Usuario, activo_editar, activo_eliminar) VALUES(respuesta,imagen,fecha,ID_PreguntaP,ID_UsuarioP, activo_editar, activo_eliminar);
END IF;

IF Opcion='E'
THEN 
UPDATE respuestas SET activo_editar=0; 
END IF;

IF Opcion='D'
THEN 
UPDATE respuestas LEFT JOIN preguntas ON preguntas.ID_RespuestaCorrecta = respuestas.ID_Respuesta 
SET respuestas.activo_eliminar=0, preguntas.ID_RespuestaCorrecta = null WHERE respuestas.ID_Respuesta = ID_RespuestaP; 
END IF;
IF Opcion='C'
THEN
SELECT respuestas.ID_Respuesta, respuestas.respuesta, respuestas.imagen, respuestas.fecha, respuestas.activo_editar, 
respuestas.activo_eliminar, respuestas.ID_Pregunta, (concat(concat(usuarios.nombre, ' '), usuarios.apellidoP)) 'nombreUsuario', 
usuarios.ID_Usuario, COUNT(case usuario_respuesta.Util when 1 then 1 else null end) 'Likes', 
COUNT(case usuario_respuesta.Util when 0 then 1 else null end) 'Dislikes',
(CASE WHEN (usuario_respuesta.ID_Usuario=ID_UsuarioP AND usuario_respuesta.Util=true) THEN 1
WHEN (usuario_respuesta.ID_Usuario=ID_UsuarioP AND usuario_respuesta.Util=false) THEN 0 ELSE 3 end) 'Liked' FROM respuestas 
JOIN usuarios ON usuarios.ID_Usuario = respuestas.ID_Usuario 
LEFT JOIN usuario_respuesta ON usuario_respuesta.ID_Respuesta = respuestas.ID_Respuesta
 INNER JOIN preguntas ON preguntas.ID_RespuestaCorrecta = respuestas.ID_Respuesta  
 WHERE respuestas.ID_Pregunta = ID_PreguntaP GROUP BY ID_Respuesta;
END IF;
IF Opcion='U'
THEN
UPDATE respuestas SET
 respuestas.respuesta=respuesta,
 respuestas.imagen=imagen, respuestas.activo_editar = 1 WHERE respuestas.ID_Respuesta=ID_RespuestaP;
END IF;

IF Opcion='N'
THEN
SELECT COUNT(ID_Respuesta) FROM respuestas WHERE ID_Pregunta = ID_PreguntaP;
END IF;

IF Opcion='G'
THEN
SELECT respuestas.ID_Respuesta, respuestas.respuesta, respuestas.imagen, respuestas.fecha, respuestas.activo_editar, 
respuestas.activo_eliminar, respuestas.ID_Pregunta, (concat(concat(usuarios.nombre, ' '), usuarios.apellidoP)) 'nombreUsuario', 
usuarios.ID_Usuario, COUNT(case usuario_respuesta.Util when 1 then 1 else null end) 'Likes', 
COUNT(case usuario_respuesta.Util when 0 then 1 else null end) 'Dislikes',
(CASE WHEN (usuario_respuesta.ID_Usuario=ID_UsuarioP AND usuario_respuesta.Util=true) THEN 1
WHEN (usuario_respuesta.ID_Usuario=ID_UsuarioP AND usuario_respuesta.Util=false) THEN 0 ELSE 3 end) 'Liked'
 FROM respuestas 
JOIN usuarios ON usuarios.ID_Usuario = respuestas.ID_Usuario 
LEFT JOIN usuario_respuesta ON usuario_respuesta.ID_Respuesta = respuestas.ID_Respuesta 
LEFT JOIN preguntas ON preguntas.ID_RespuestaCorrecta = respuestas.ID_Respuesta 
 WHERE respuestas.ID_Pregunta = ID_PreguntaP AND preguntas.ID_RespuestaCorrecta is null 
 GROUP BY ID_Respuesta ORDER BY respuestas.fecha DESC LIMIT inicio, 10 ;
END IF;

IF Opcion ='I'
THEN
SELECT respuestas.imagen FROM respuestas WHERE ID_Respuesta = ID_RespuestaP;
END IF;
IF Opcion ='B'
THEN
SELECT respuestas.respuesta,respuestas.fecha,respuestas.ID_Pregunta,respuestas.ID_Usuario FROM respuestas JOIN preguntas ON
 preguntas.ID_Pregunta=respuestas.ID_Pregunta WHERE respuestas.ID_Usuario = ID_UsuarioP AND respuestas.activo_eliminar = true AND preguntas.activo = 1 LIMIT inicio, 10;
END IF;

IF Opcion ='Q'
THEN
SELECT COUNT(respuestas.ID_Pregunta) FROM respuestas JOIN preguntas ON
 preguntas.ID_Pregunta=respuestas.ID_Pregunta WHERE respuestas.ID_Usuario = ID_UsuarioP AND respuestas.activo_eliminar = true AND preguntas.activo = 1;
END IF;

IF Opcion = '1'
THEN
SELECT respuestas.ID_Respuesta, respuestas.respuesta, respuestas.imagen, respuestas.ID_Usuario, respuestas.ID_Pregunta FROM respuestas WHERE respuestas.ID_Respuesta = ID_RespuestaP;
END IF;

IF Opcion='2'
THEN
SELECT  COUNT(case usuario_respuesta.Util when 1 then 1 else null end) 'Likes', COUNT(case usuario_respuesta.Util when 0 then 1 else null end) 'Dislikes' FROM respuestas JOIN usuarios ON usuarios.ID_Usuario = respuestas.ID_Usuario LEFT JOIN usuario_respuesta ON usuario_respuesta.ID_Respuesta = respuestas.ID_Respuesta  WHERE respuestas.ID_Respuesta = ID_RespuestaP;
END IF;
END //
