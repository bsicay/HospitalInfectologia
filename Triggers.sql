
-- TRIGGER EDAD
Delimiter $$
Create trigger tr_Paciente_Edad
	before insert on Pacientes
    For Each Row
    Begin
		 set new.edad = TIMESTAMPDIFF(year, NEW.fechaNacimiento, now());
    End$$
Delimiter ;


-- TRIGGER TURNO

Delimiter $$
Create function fn_TurnoMaximo(x int) returns int
reads sql data deterministic
Begin
	Declare suma int;
    Set suma = (select horarios.lunes from horarios where codigoHorario = x) + (select horarios.martes from horarios where codigoHorario = x)
		+ (select horarios.miercoles from horarios where codigoHorario = x) + (select horarios.jueves from horarios where codigoHorario = x)
			+ (select horarios.viernes from horarios where codigoHorario = x);
	return suma;
End$$
Delimiter ;

Delimiter $$
Create trigger tr_Medicos_TurnoMaximo
	after insert on Medico_Especialidad
    For Each Row
    Begin
		 Update Medicos set turnoMaximo = (select fn_TurnoMaximo(new.codigoHorario)) where codigoMedico = new.codigoMedico;
    End$$
Delimiter ;
