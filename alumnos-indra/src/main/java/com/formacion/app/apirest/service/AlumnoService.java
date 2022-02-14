package com.formacion.app.apirest.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.formacion.app.apirest.entity.Alumno;

@Service
public interface AlumnoService {
	List<Alumno> getAlumnos();
	
	Alumno getAlumno(long id);
	
	Alumno postAlumno(Alumno alumno);
	
	
	Alumno putAlumno(Alumno alumno, long id);	
	
	Alumno deleteAlumno(long id);
	
}
