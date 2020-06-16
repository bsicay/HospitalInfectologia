Create database DBHospitalInfetcologia2015114; 

use DBHospitalInfetcologia2015114; 

Create table Areas(
	codigoArea int not null auto_increment, 
    nombreArea varchar(45),
    Primary Key PK_codigoArea(codigoArea)
    
);


Create table Cargos(
	codigoCargo int not null auto_increment, 
    nombreCargo varchar(45),
    Primary Key PK_codigoCargo(codigoCargo)
);

Create table Pacientes(
	idPaciente int not null auto_increment,
    DPI varchar(20) not null default 0,
    apellido varchar(100) not null, 
    nombre varchar(100) not null, 
    fechaNacimiento date not null, 
    edad int default 0, 
    direccion varchar(150) not null, 
    ocupacion varchar(50) not null, 
    sexo varchar(15),
    Primary Key PK_idPaciente(idPaciente)

);

Create table Horarios(
	codigoHorario int not null auto_increment,
    horarioInicio varchar(8) not null, 
    horarioSalida varchar(8) not null,
    lunes tinyint, 
    martes tinyint,
    miercoles tinyint,
    jueves tinyint,
    viernes tinyint,
    Primary Key PK_codigoHorario(codigoHorario)
    
);


Create table Medicos(
	codigoMedico int not null auto_increment, 
    licenciaMedica int not null default 0, 
    nombres varchar(100) not null,
    apellidos varchar(100) not null,
    horaEntrada varchar(20) not null, 
    horaSalida  varchar(20) not null, 
    turnoMaximo int default 0, 
    sexo varchar(20) default '',
    Primary Key PK_codigoMedico(codigoMedico)
);

Create table contactoUrgencia(
	codigoContactoUrgencia int not null auto_increment, 
    apellidos varchar(100),
    nombres varchar(100),
    numeroContacto varchar(10), 
    codigoPaciente int not null, 
    Primary Key PK_codigoContactoUrgencia(codigoContactoUrgencia), 
    CONSTRAINT FK_contactoUrgencia_Pacientes Foreign Key (codigoPaciente) REFERENCES Pacientes(idPaciente)
    
);

Create table Especialidades(
	codigoEspecialidad int not null auto_increment, 
    nombreEspecialidad varchar(45) not null, 
    Primary Key PK_codigoEspecialidad(codigoEspecialidad)
    
);

Create table Medico_Especialidad(
	codigoMedicoEspecialidad int not null auto_increment, 
    codigoMedico int not null, 
    codigoEspecialidad int not null, 
    codigoHorario int not null, 
    Primary Key PK_codigoMedicoEspecialidad(codigoMedicoEspecialidad),
    CONSTRAINT FK_Medico_Especialidad_Especialidades Foreign Key (codigoEspecialidad) REFERENCES Especialidades(codigoEspecialidad), 
    CONSTRAINT FK_Medico_Especialidad_Horarios Foreign Key (codigoHorario) REFERENCES Horarios(codigoHorario), 
    CONSTRAINT FK_Medico_Especialidad_Medicos Foreign Key (codigoMedico) REFERENCES Medicos(codigoMedico)
    
);

Create table TelefonoMedico(
	codigoTelefonoMedico int not null auto_increment, 
    telefonoPersonal varchar(15) not null, 
    telefonoTrabajo varchar(15) default '-', 
    codigoMedico int not null, 
    Primary Key PK_codigoTelefonoMedico(codigoTelefonoMedico), 
    CONSTRAINT FK_TelefonoMedico_Medicos Foreign Key (codigoMedico) REFERENCES Medicos(codigoMedico) ON DELETE CASCADE

);

Create table ResponsableTurno(
	codigoResponsableTurno int not null auto_increment, 
    nombreResponsable varchar(75), 
    apellidosResponsable varchar(45), 
    telefonoPersonal varchar(10), 
    codigoArea int not null, 
    codigoCargo int not null, 
    Primary Key PK_codigoResponsableTurno(codigoResponsableTurno), 
    CONSTRAINT FK_ResponsableTurno_Areas Foreign Key (codigoArea) REFERENCES Areas(codigoArea),
    CONSTRAINT FK_ResponsableTurno_Cargos Foreign Key (codigoCargo) REFERENCES Cargos(codigoCargo)
);

Create table Turno(
	codigoTurno int not null auto_increment, 
    fechaTurno date not null, 
    fechaCita date not null, 
    valorCita decimal (10,2), 
    codigoMedicoEspecialidad int not null,
    codigoTurnoResponsable int not null, 
    codigoPaciente int not null, 
    Primary Key PK_codigoTurno(codigoTurno), 
    CONSTRAINT FK_Turno_ResponsableTurno Foreign Key (codigoTurnoResponsable) REFERENCES ResponsableTurno(codigoResponsableTurno), 
    CONSTRAINT FK_Turno_Medico_Especialidad Foreign Key (codigoMedicoEspecialidad) REFERENCES Medico_Especialidad(codigoMedicoEspecialidad), 
    CONSTRAINT FK_Turno_Pacientes Foreign Key (codigoPaciente) REFERENCES Pacientes(idPaciente)
);



-- -----------------PROCEDIMIENTO DE MEDICOS
Delimiter $$
Create procedure sp_AgregarMedico(IN lMedica int, nombre varchar(100), apellido varchar(100), hEntrada varchar(20), hSalida varchar(20), genero varchar(20))
Begin 	
    Insert into Medicos(licenciaMedica, nombres, apellidos, horaEntrada, horaSalida, sexo)
		values(lMedica, nombre, apellido, hEntrada, hSalida, genero);
End$$
Delimiter ; 


Delimiter $$
Create procedure sp_ListarMedico()
	Begin
		Select 
			Medicos.codigoMedico, 
            Medicos.licenciaMedica, 
            Medicos.nombres,
            Medicos.apellidos, 
            Medicos.horaEntrada,
            Medicos.horaSalida, 
            Medicos.turnoMaximo,
            Medicos.sexo
			From Medicos;
    End$$
Delimiter ; 

 
Delimiter $$
Create procedure sp_BuscarMedico(IN codigo int)
	Begin
		Select 
			Medicos.codigoMedico, 
            Medicos.licenciaMedica, 
            Medicos.nombres,
            Medicos.apellidos, 
            Medicos.horaEntrada,
            Medicos.horaSalida, 
            Medicos.turnoMaximo, 
            Medicos.sexo
			From Medicos
				Where codigoMedico = codigo;
    End$$
Delimiter ; 

Delimiter $$
Create procedure sp_EditarMedico(IN nuevaLicencia int, IN nuevoNombre varchar(100), IN nuevoApellido varchar(100),
	IN nuevaEntrada varchar(20), IN nuevaSalida varchar(20), IN nuevoSexo varchar(20), IN codigo int)
	Begin
		Update Medicos set licenciaMedica = nuevaLicencia, nombres = nuevoNombre, apellidos = nuevoApellido, horaEntrada = nuevaEntrada,
			horaSalida = nuevaSalida,  sexo = nuevoSexo 
				Where codigoMedico = codigo; 
    End$$
Delimiter ;


Delimiter $$
Create procedure sp_EliminarMedico(IN codigo int)
	Begin
		Delete from Medicos 
			Where codigoMedico = codigo; 
    End$$
Delimiter ;




-- -----------------PROCEDIMIENTO DE TELEFONO - MEDICOS
Delimiter $$
Create procedure sp_AgregarTelefonoMedico(IN telPersonal varchar(15), IN telTrabajo varchar(15), IN cMedico int)
Begin 	
    Insert into TelefonoMedico(telefonoPersonal, telefonoTrabajo, codigoMedico)
		values(telPersonal, telTrabajo, cMedico);
End$$
Delimiter ; 

Delimiter $$
Create procedure sp_ListarTelefonoMedico()
	Begin
		Select 
			TelefonoMedico.codigoTelefonoMedico, 
            TelefonoMedico.telefonoPersonal,  
            TelefonoMedico.telefonoTrabajo, 
            TelefonoMedico.codigoMedico
			From TelefonoMedico;
    End$$
Delimiter ; 

Delimiter $$
Create procedure sp_BuscarTelefonoMedico(IN codigo int)
	Begin
		Select 
			TelefonoMedico.codigoTelefonoMedico, 
            TelefonoMedico.telefonoPersonal,  
            TelefonoMedico.telefonoTrabajo, 
            TelefonoMedico.codigoMedico
			From TelefonoMedico
				Where codigoTelefonoMedico = codigo;
    End$$
Delimiter ; 

Delimiter $$
Create procedure sp_EditarTelefonoMedico(IN nuevoTelefonoPersonal varchar(15), IN nuevoTelefonoTrabajo varchar(25), IN codigo int)
	Begin
		Update TelefonoMedico set telefonoPersonal = nuevoTelefonoPersonal, telefonoTrabajo = nuevoTelefonoTrabajo
				Where codigoTelefonoMedico = codigo; 
    End$$
Delimiter ;

Delimiter $$
Create procedure sp_EliminarTelefonoMedico(IN codigo int)
	Begin
		Delete from TelefonoMedico 
			Where codigoTelefonoMedico = codigo; 
    End$$
Delimiter ;


-- -----------------PROCEDIMIENTO DE PACIENTES

Delimiter $$
Create procedure sp_AgregarPaciente(IN documentoPersonal varchar(20), IN apellidos varchar(100), IN nombres varchar(100), IN fNacimiento date, 
	IN domicilio varchar(150), IN oficio varchar(50), IN genero varchar(15))
    Begin 
		Insert into Pacientes(DPI, apellido, nombre, fechaNacimiento, direccion, ocupacion, sexo)
			values(documentoPersonal, apellidos, nombres, fNacimiento, domicilio, oficio, genero);
    End$$
Delimiter ;


Delimiter $$
Create procedure sp_ListarPaciente()
	Begin
		Select 
			Pacientes.idPaciente, 
            Pacientes.DPI, 
            Pacientes.apellido, 
            Pacientes.nombre, 
            Pacientes.fechaNacimiento, 
            Pacientes.edad, 
            Pacientes.direccion, 
            Pacientes.ocupacion, 
            Pacientes.sexo
			From Pacientes; 
    End$$
Delimiter ;  

select 			
	Pacientes.idPaciente, 
	Pacientes.DPI, 
    Pacientes.apellido, 
    Pacientes.nombre, 
    Pacientes.fechaNacimiento, 
    Pacientes.edad, 
    Pacientes.direccion, 
    Pacientes.ocupacion, 
    Pacientes.sexo, 
    ContactoUrgencia.numeroContacto 
	from Pacientes RIGHT JOIN ContactoUrgencia ON Pacientes.idPaciente = ContactoUrgencia.codigoPaciente
    Order By Pacientes.idPaciente;

Delimiter $$
Create procedure sp_BuscarPaciente(IN codigo int)
	Begin
		select
			Pacientes.idPaciente, 
            Pacientes.DPI, 
            Pacientes.apellido, 
            Pacientes.nombre, 
            Pacientes.fechaNacimiento, 
            Pacientes.edad, 
            Pacientes.direccion, 
            Pacientes.ocupacion, 
            Pacientes.sexo
			From Pacientes
				Where idPaciente = codigo; 			
    End$$
Delimiter ; 


Delimiter $$
Create procedure sp_EditarPaciente(IN nuevoDPI varchar(20), IN nuevoApellido varchar(100), IN nuevoNombre varchar(100), IN nuevaFechaNacimiento date, 
	IN nuevaDireccion varchar(150), IN nuevaOcupacion varchar(50), IN nuevoSexo varchar(15), IN codigo int)
    Begin
		Update Pacientes set DPI = nuevoDPI, apellido = nuevoApellido, nombre = nuevoNombre, fechaNacimiento = nuevaFechaNacimiento, 
			direccion = nuevaDireccion, ocupacion = nuevaOcupacion, sexo = nuevoSexo
				Where idPaciente = codigo; 
    End$$
Delimiter ; 


Delimiter $$
Create procedure sp_EliminarPaciente(IN codigo int)
	Begin
		Delete from Pacientes 
			Where idPaciente = codigo; 
    End$$
Delimiter ;


-- -----------------PROCEDIMIENTO DE AREAS 
Delimiter $$
Create procedure sp_AgregarAreas(IN nArea varchar(45))
    Begin
		Insert into Areas(nombreArea)
			values(nArea); 
	End$$
Delimiter ;

Delimiter $$
Create procedure sp_ListarAreas()
	Begin
		select 
			Areas.codigoArea, 
            Areas.nombreArea
            From Areas;
	End$$
Delimiter ; 


Delimiter $$
Create procedure sp_BuscarArea(IN codigo int)
    Begin
		Select 
			Areas.codigoArea, 
            Areas.nombreArea
            From Areas 
				Where codigoArea = codigo; 
    End$$
Delimiter ; 

Delimiter $$
Create procedure sp_EditarAreas(IN nuevoNombre varchar(45), IN codigo int)
    Begin
		Update Areas set nombreArea = nuevoNombre
			Where codigoArea = codigo; 
	End$$
Delimiter ; 

Delimiter $$
Create procedure sp_ElimiarAreas(IN codigo int)
    Begin
		Delete from Areas
			Where codigoArea = codigo; 
	End$$
Delimiter ; 

-- -----------------PROCEDIMIENTO DE CARGOS

Delimiter $$
Create procedure sp_AgregarCargos(IN nCargo varchar(45))
Begin
	Insert into Cargos(nombreCargo)
		values(nCargo); 
End$$
Delimiter ;


Delimiter $$
Create procedure sp_ListarCargos()
Begin
	select 
		Cargos.codigoCargo, 
		Cargos.nombreCargo
		From Cargos;
End$$
Delimiter ; 



Delimiter $$
Create procedure sp_BuscarCargo(IN codigo int)
Begin
	Select 
		Cargos.codigoCargo, 
		Cargos.nombreCargo
		From Cargos 
			Where Cargos.codigoCargo = codigo; 
End$$
Delimiter ; 

Delimiter $$
Create procedure sp_EditarCargo(IN nuevoNombre varchar(45), IN codigo int)
    Begin
		Update Cargos set nombreCargo = nuevoNombre
			Where codigoCargo = codigo; 
	End$$
Delimiter ; 

Delimiter $$
Create procedure sp_ElimiarCargo(IN codigo int)
    Begin
		Delete from Cargos
			Where codigoCargo = codigo; 
	End$$
Delimiter ;

-- -----------------PROCEDIMIENTOS CONTACTO-URGENCIA
Delimiter $$
Create procedure sp_AgregarContactoUrgencia(IN nombreContacto varchar(100), IN apellidoContacto varchar(100), IN numero varchar(10), IN codigo int)
	Begin
		Insert into ContactoUrgencia(nombres, apellidos, numeroContacto, codigoPaciente)
			values(nombreContacto, apellidoContacto, numero, codigo);
    End$$
Delimiter ;


Delimiter $$
Create procedure sp_ListarContactoUrgencia()
	Begin
		Select 
			ContactoUrgencia.codigoContactoUrgencia, 
            ContactoUrgencia.apellidos,
            ContactoUrgencia.nombres,  
            ContactoUrgencia.numeroContacto, 
            ContactoUrgencia.codigoPaciente
            From ContactoUrgencia;
    End$$
Delimiter ; 

Delimiter $$
Create procedure sp_BuscarContactoUrgencia(IN codigo int)
Begin
		Select 
			contactoUrgencia.codigoContactoUrgencia, 
            contactoUrgencia.nombres, 
            contactoUrgencia.apellidos, 
            contactoUrgencia.numeroContacto, 
            contactoUrgencia.idPaciente
            From ContactoUrgencia
				Where contactoUrgencia.codigoContactoUrgencia = codigo; 
End$$
Delimiter ; 

Delimiter $$
Create procedure sp_EditarContactoUrgencia(IN nuevoNombre varchar(100), IN nuevoApellido varchar(100), IN nuevoNumeroContacto varchar(10), 
						codigo int)
	Begin
		Update contactoUrgencia set nombres = nuevoNombre, apellidos = nuevoApellido, numeroContacto = nuevoNumeroContacto
				Where codigoContactoUrgencia = codigo;
	End$$
Delimiter ;

Delimiter $$
Create procedure sp_EliminarContactoUrgencia(IN codigo int)
	Begin
		Delete from contactoUrgencia
			Where codigoContactoUrgencia = codigo;
    End $$
Delimiter ;

-- -----------------PROCEDIMIENTOS ESPECIALIDADES
Delimiter $$
Create procedure sp_AgregarEspecialidad(IN nombre varchar (45))
	Begin
		Insert into Especialidades(nombreEspecialidad)
			values(nombre);
    End$$
Delimiter ; 



Delimiter $$
Create procedure sp_ListarEspecialidad()
	Begin
		Select
			Especialidades.codigoEspecialidad,
			Especialidades.nombreEspecialidad
			From Especialidades;
    End$$
Delimiter ; 


Delimiter $$
Create procedure sp_BuscarEspecialidad(IN codigo int)
	Begin
		Select 
			Especialidades.codigoEspecialidad,
			Especialidades.nombreEspecialidad
            From Especialidades
				Where codigoEspecialidad = codigo;
    End$$
Delimiter ;

Delimiter $$
Create procedure sp_EditarEspecialidad(IN nuevoNombre varchar(45), IN codigo int)
	Begin
		Update Especialidades set nombreEspecialidad = nuevoNombre
			Where codigoEspecialidad = codigo;
    End$$
Delimiter ;

Delimiter $$
Create procedure sp_EliminarEspecialidad(IN codigo int)
	Begin
		Delete from Especialidades
			Where codigoEspecialidad = codigo;
    End$$
Delimiter ;

-- -----------------PROCEDIMIENTOS HORARIOS
Delimiter $$
Create procedure sp_AgregarHorario(IN hInicio varchar(8), IN hSalida varchar(8), IN monday tinyint, IN tuesday tinyint, IN wednesday tinyint, 
	IN thursday tinyint, IN friday tinyint)
    Begin
		Insert into Horarios(horarioInicio, horarioSalida, lunes, martes, miercoles, jueves, viernes)
			values(hInicio, hSalida, monday, tuesday, wednesday, thursday, friday);
    End$$
Delimiter ;


Delimiter $$
Create procedure sp_ListarHorario()
	Begin
		Select 
			Horarios.codigoHorario, 
			Horarios.horarioInicio, 
            Horarios.horarioSalida, 
            Horarios.lunes, 	
            Horarios.martes, 	
            Horarios.miercoles,	
            Horarios.jueves, 	
            Horarios.viernes
			From Horarios; 
    End$$
Delimiter ;

Delimiter $$
Create procedure sp_BuscarHorariohorarioInicio(IN codigo int)
	Begin
		Select 
			Horarios.codigoHorario, 
			Horarios.horarioInicio, 
            Horarios.horarioSalida, 
            Horarios.lunes, 	
            Horarios.martes, 	
            Horarios.miercoles,	
            Horarios.jueves, 	
            Horarios.viernes
			From Horarios     
				Where codigoHorario = codigo;
    End$$
Delimiter ;

Delimiter $$
Create procedure sp_EditarHorario(IN nuevoHorarioInicio varchar(8), IN nuevoHorarioSalida varchar(8), IN monday tinyint, IN tuesday tinyint, IN wednesday tinyint, 
	IN thursday tinyint, IN friday tinyint, IN codigo int)
    Begin
		Update Horarios set horarioInicio = nuevoHorarioInicio, horarioSalida = nuevoHorarioSalida, lunes = monday, martes = tuesday, 
			miercoles = wednesday, jueves = thursday, vieres = friday 
				Where codigoHorario = codigo; 
    End$$
Delimiter ;

Delimiter $$
Create procedure sp_EliminarHorario(IN codigo int)
	Begin
		Delete from Horarios
			Where codigoHorario = codigo;
    End$$
Delimiter ;

-- -----------------PROCEDIMIENTOS MEDICO-ESPECIALIDAD
Delimiter $$
Create procedure sp_AgregarMedicoEspecialidad(IN cMedico int, IN cEspecialidad int, IN cHorario int)
	Begin
		Insert into Medico_Especialidad(codigoMedico, codigoEspecialidad, codigoHorario)
			values(cMedico, cEspecialidad, cHorario);
    End$$
Delimiter ;

Delimiter $$
Create procedure sp_ListarMedicoEspecialidad()
	Begin
		Select
			Medico_Especialidad.codigoMedicoEspecialidad,
			Medico_Especialidad.codigoMedico,
			Medico_Especialidad.codigoEspecialidad,
			Medico_Especialidad.codigoHorario
            From Medico_Especialidad;
    End$$
Delimiter ;

Delimiter $$
Create procedure sp_BuscarMedicoEspecialidad(IN codigo int)
	Begin
		Select
			Medico_Especialidad.codigoMedicoEspecialidad,
			Medico_Especialidad.codigoMedico,
			Medico_Especialidad.codigoEspecialidad,
			Medico_Especialidad.codigoHorario
            From Medico_Especialidad
				Where codigoMedicoEspecialidad = codigo;
    End$$
Delimiter ;

Delimiter $$
Create procedure sp_EditarMedicoEspecialidad(IN nuevoCodigoMedico int, IN nuevoCodigoEspecialidad int, IN nuevoCodigoHorario int, IN codigo int)
	Begin
		Update Medico_Especialidad set codigoMedico = nuevoCodigoMedico, codigoEspecialidad = nuevoCodigoEspecialidad, codigoHorario = nuevoCodigoHorario
			Where codigoMedicoEspecialidad = codigo;
    End$$
Delimiter ;

Delimiter $$
Create procedure sp_ElminarMedicoEspecialidad(IN codigo int)
	Begin
		Delete from Medico_Especialidad 
			Where codigoMedicoEspecialidad = codigo;
    End$$
Delimiter ;

-- -----------------PROCEDIMIENTOS RESPONSABLE-TURNO
Delimiter  $$
Create procedure sp_AgregarResponsableTurno(IN nResponsable varchar(75), IN aResponsable varchar(75), IN telPersonal varchar(10), IN cArea int, IN cCargo int)
    Begin
		Insert into ResponsableTurno(nombreResponsable, apellidosResponsable, telefonoPersonal, codigoArea, codigoCargo)
			values(nResponsable, aResponsable, telPersonal, cArea, cCargo);
    ENd$$
Delimiter ;


Delimiter $$
Create procedure sp_ListarResponsableTurno()
	Begin
		Select 
			responsableTurno.codigoResponsableTurno, 
			responsableTurno.nombreResponsable,
            responsableTurno.apellidosResponsable, 
            responsableTurno.telefonoPersonal, 
            responsableTurno.codigoArea,
            responsableTurno.codigoCargo
            From responsableTurno;
    End$$
Delimiter ;


Delimiter $$
Create procedure sp_BuscarResponsableTurno(IN codigo int)
	Begin
		Select 
			responsableTunro.nombreResponsable,
            responsableTurno.apellidosResponsable, 
            responsableTurno.telefonoPersonal, 
            responsableTurno.codigoArea,
            responsableTurno.codigoCargo
            From responsableTurno
				where codigoResponsableTurno = codigo;
    End$$
Delimiter ;

Delimiter $$
Create procedure sp_EditarResponsableTurno(IN nuevoNombre varchar(75), IN nuevoApellido varchar(75), IN nuevoTelefono varchar(10), IN codigo int)
	Begin
		Update responsableTurno set nombreResponsable = nuevoNombre, apellidosResponsable = nuevoApellido, telefonoPersonal = nuevoTelefono 
            Where codigoResponsableTurno = codigo;
    End$$
Delimiter ;

drop procedure sp_EditarResponsableTurno;

Delimiter $$
Create procedure sp_EliminarResponsableTurno(IN codigo int)
	Begin
		Delete from responsableTurno
				where codigoResponsabe = codigo;
    End$$
Delimiter ;

-- tunro

Delimiter  $$
Create procedure sp_AgregarTurno(IN fTurno date, IN fCita date, IN valor decimal(10,2), IN medicoEspecialidad int, IN turnoResponsable int, 
	IN Paciente int)
    Begin
		Insert into Turno(fechaTurno, fechaCita, valorCita, codigoMedicoEspecialidad, codigoTurnoResponsable, codigoPaciente)
			values(fTurno, fCita, valor, medicoEspecialidad, turnoResponsable, Paciente);
    ENd$$
Delimiter ;

Delimiter $$
Create procedure sp_ListarTurno()
	Begin
		Select 
			Turno.codigoTurno, 
            Turno.fechaTurno, 
            Turno.fechaCita, 
            Turno.valorCita, 
            Turno.codigoMedicoEspecialidad, 
            Turno.codigoTurnoResponsable, 
            Turno.codigoPaciente
            From Turno;
    End$$
Delimiter ;

Delimiter $$
Create procedure sp_BuscarTurno(IN codigo int)
	Begin
		Select 
			Turno.codigoTurno, 
            Turno.fechaTurno, 
            Turno.fechaCita, 
            Turno.valorCita, 
            Turno.codigoMedicoEspecialidad, 
            Turno.codigoTurnoResponsable, 
            Turno.codigoPaciente
            From Turno
				Where codigoTurno = codigo;
    End$$
Delimiter ;

Delimiter $$
Create procedure sp_EditarTurno(IN nuevaFechaTurno date, IN nuevaFechaCita date, IN nuevoValorCita int, IN nuevoCodigoMedicoEspecialidad int, 
	IN nuevoCodigoTurnoResponsable int, IN nuevoCodigoPaciente int, IN codigo int)
    Begin
		Update Turno Set fechaTurno = nuevaFechaTurno, fechaCita = nuevaFechaCita, valorCita = nuevoValorCita, 
			codigoMedicoEspecialidad = nuevoCodigoMedicoEspecialidad, codigoTurnoResponsable = nuevoCodigoTurnoResponsable, codigoPaciente = nuevoCodigoPaciente
			Where codigoTurno = codigo;
    End$$
Delimiter ;

Delimiter $$
Create procedure sp_EliminarTurno(IN codigo int)
	Begin
		Delete from Turno 
			Where codigoTurno = codigo;
    End$$
Delimiter ; 


call sp_AgregarMedico(51515, 'Manuel', 'Barrios Bonilla', '08:00:00', '17:00:00', 'M');
call sp_AgregarMedico(54997, 'Estuardo', 'Santos', '07:00:00', '20:00:00', 'M');
call sp_AgregarMedico(54997, 'Fernanda', 'Solares', '10:00:00', '17:00:00', 'F');

call sp_AgregarEspecialidad('Cirujano');
call sp_AgregarEspecialidad('Doctor');

call sp_AgregarHorario('08:00:00', '17:00:00', 1, 1, 1, 0, 0);
call sp_AgregarHorario('10:00:00', '18:00:00', 1, 1, 1, 1, 0);
call sp_AgregarHorario('07:00:00', '20:00:00', 1, 1, 1, 1, 1);

call sp_AgregarMedicoEspecialidad(1, 1, 1);
call sp_AgregarMedicoEspecialidad(2, 2, 2);

call sp_AgregarPaciente('8568489514521', 'Sicay Cumes', 'Brandon', '2002-04-29', '11 Calle D', 'Estudiante', 'M');
call sp_AgregarPaciente('9568489514561', 'Juares Perez', 'Jose', '2000-04-29','11 calle E', 'Maestro', 'M');

call sp_AgregarAreas('Infectologia');
call sp_AgregarAreas('Operaciones');
call sp_AgregarAreas('Cardiologia');

call sp_AgregarCargos('Supervisor');

call sp_AgregarResponsableTurno('Andres', 'Santos', '8789-9874', 1, 1);
call sp_AgregarResponsableTurno('Estuardo', 'Chutan', '1549-9874', 2, 1);



call sp_AgregarTurno('2019-05-02', '2019-05-02', 200.00, 1, 1, 1);
call sp_AgregarTurno('2019-10-15', '2019-10-15', 150.00, 2, 2, 2);

Delimiter $$
Create procedure sp_ReporteFinal(IN cMedico int)
Begin
Select M.codigoMedico, M.licenciaMedica, M.nombres, M.apellidos, M.horaEntrada, M.horaSalida, M.turnoMaximo, M.sexo, H.lunes, H.martes,
	H.miercoles, H.Jueves, H.viernes, E.nombreEspecialidad, P.apellido, P.nombre, P.direccion, CU.numeroContacto, T.fechaTurno, T.fechaCita, 
    T.valorCita, R.nombreResponsable, R.apellidosResponsable,  A.nombreArea, C.nombreCargo
    From Medicos M INNER JOIN Medico_Especialidad ME ON ME.codigoMedico = M.codigoMedico INNER JOIN
		Horarios H ON H.codigoHorario = ME.codigoHorario INNER JOIN Pacientes P INNER JOIN Turno T on T.codigoMedicoEspecialidad = ME.codigoMedicoEspecialidad and T.codigoPaciente 
		= P.idPaciente INNER JOIN ResponsableTurno R ON R.codigoResponsableTurno = T.codigoTurnoResponsable INNER JOIN Areas A 
        ON A.codigoArea = R.codigoArea INNER JOIN Cargos C ON C.codigoCargo = R.codigoCargo INNER JOIN Especialidades E ON E.codigoEspecialidad = ME.codigoEspecialidad
        INNER JOIN ContactoUrgencia CU ON CU.codigoPaciente = P.idPaciente where M.codigoMedico = cMedico group by M.codigoMedico;
End$$
Delimiter ; 

-- drop procedure sp_ReporteFinal;
-- call sp_ReporteFinal(1);       
        
ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY 'admin';