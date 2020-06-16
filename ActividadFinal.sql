Create table TipoUsuario(
	codigoTipoUsuario int not null auto_increment, 
    descripcion varchar(150),
    Primary Key PK_codigoTipoUsuario(codigoTipoUsuario)
);


Create table Usuarios(
	codigoUsuario int not null auto_increment, 
    usuarioLogin varchar(45) not null,
    usuarioContraseña varchar(45) not null, 
    usuarioEstado tinyint default 0, 
    usuarioFecha date default 0, 
    usuarioHora time default 0,
    codigoTipoUsuario int not null, 
    Primary Key PK_codigoUsuario(codigoUsuario),
    CONSTRAINT FK_Usuarios_TipoUsuario Foreign Key (codigoTipoUsuario) REFERENCES TipoUsuario(codigoTipoUsuario)
);



-- PROCEDIMIENTOS DE USUARIOS
Delimiter $$
Create procedure sp_AgregarUsuario(IN userLogin varchar(45), IN userPassword varchar(45), IN tipoUsuario int)
Begin	
	Insert into Usuarios(usuarioLogin, usuarioContraseña, codigoTipoUsuario)
		values(userLogin, userPassword, tipoUsuario);
End$$
Delimiter ;

Delimiter $$
Create procedure sp_ListarUsuarios()
Begin
	Select 
		Usuarios.codigoUsuario, 
		Usuarios.usuarioLogin,
		Usuarios.usuarioContraseña,
		Usuarios.usuarioEstado,
		Usuarios.usuarioFecha,
		Usuarios.usuarioHora,
        usuarios.codigoTipoUsuario
        from Usuarios; 
End$$
Delimiter ;

Delimiter $$
Create procedure sp_EliminarUsuarios(IN codigo int)
Begin 
	Delete from Usuarios 
		Where codigoUsuario = codigo;
End$$
Delimiter ; 

Delimiter $$
Create procedure sp_EditarUsuario(IN nuevoUsuario varchar(45), IN nuevaContraseña varchar(45), IN codigo int)
Begin
	Update Usuarios set Usuarios.usuarioLogin = nuevoUsuario, Usuarios.usuarioContraseña = nuevaContraseña
		Where codigoUsuario = codigo; 
End$$
Delimiter ; 


--    TIPO USUARIO INGRESOS

Insert into TipoUsuario(descripcion)
	values('root');
Insert into TipoUsuario(descripcion)
	values('admin');
Insert into TipoUsuario(descripcion)
	values('invitado');

-- PROCEDIMIENTOS TIPO USUARIO
   
Delimiter $$
Create procedure sp_ListarTipoUsuario() 
Begin
	Select 
		TipoUsuario.codigoTipoUsuario, 
		TipoUsuario.descripcion
        From TipoUsuario;
End$$
Delimiter ;   

Delimiter $$
Create procedure sp_BuscarTipoUsuario(IN codigo int) 
Begin
	Select 
		TipoUsuario.codigoTipoUsuario, 
		TipoUsuario.descripcion
        From TipoUsuario
			Where codigoTipoUsuario = codigo; 
End$$
Delimiter ; ]


-- TRIGGER FECHA USUARIO
Delimiter $$
Create trigger tr_Fecha_Usuario
	before insert on Usuarios
    For Each Row
    Begin
		 set new.usuarioFecha = curdate();
    End$$
Delimiter ;

--  PROCEDIMIENTO ESTADO Y HORA
Delimiter $$
Create procedure sp_Hora_Fecha(IN codigo int)
Begin
	Update Usuarios set usuarioEstado = 1, usuarioHora = curtime()
		Where codigoUsuario = codigo;
End$$
Delimiter ;

-- PROCEDIMIENTO ESTADO SALIDA
Delimiter $$
Create procedure sp_Hora_Salida(IN codigo int)
Begin
	Update Usuarios set usuarioEstado = 0
		Where codigoUsuario = codigo;
End$$
Delimiter ;

CALL sp_AgregarUsuario('bsicay', 'wmb8CkDq', 1);

