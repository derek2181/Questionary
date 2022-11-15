
Delimiter ; //


INSERT INTO usuarios(ID_Usuario,contra,correo,apellidoP,apellidoM,fecha_nacimiento,activo) VALUES("derix","123","123","sdfs","asdf","02/02/10",1);
DROP PROCEDURE IF EXISTS `sp_Usuarios`;
DELIMITER //
CREATE PROCEDURE sp_Usuarios(
IN Opcion CHAR(1),
IN ID_UsuarioP VARCHAR(20),
IN correo VARCHAR(20),
IN contra VARCHAR(20),
IN apellidoP VARCHAR(20),
IN apellidoM VARCHAR (20),
IN fecha_nacimiento DATE
)
BEGIN
IF Opcion='A'
THEN  
INSERT INTO usuarios(ID_Usuario,contra,correo,apellidoP,apellidoM,fecha_nacimiento,activo) VALUES(ID_UsuarioP,contra,correo,apellidoP,apellidoM,fecha_nacimiento,1);
END IF;
IF Opcion='G'
THEN
SELECT usuarios.ID_Usuario, usuarios.contra, usuarios.correo, usuarios.apellidoP, usuarios.apellidoM,usuarios.fecha_nacimiento FROM usuarios WHERE usuarios.ID_Usuario=ID_UsuarioP;
END IF;
IF Opcion='B'
THEN
UPDATE usuarios SET usuarios.activo = 0 WHERE usuarios.ID_Usuario=ID_UsuarioP;
END IF;
IF Opcion='C'
THEN
UPDATE usuarios SET 
usuarios.fecha_nacimiento=fecha_nacimiento,
usuarios.contra=contra,
usuarios.apellidoP=apellidoP,
usuarios.apellidoM=apellidoM,
usuarios.correo=correo WHERE usuarios.ID_Usuario=ID_Usuario;
END IF;
END //


DELIMITER ;

