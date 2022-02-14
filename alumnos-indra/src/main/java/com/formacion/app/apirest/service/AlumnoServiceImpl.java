package com.formacion.app.apirest.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.formacion.app.apirest.dao.AlumnoDAO;
import com.formacion.app.apirest.entity.Alumno;

@Service
public class AlumnoServiceImpl implements AlumnoService {
	
	@Autowired
	AlumnoDAO alumnoDAO;

	@Transactional(readOnly=true)
	@Override
	public List<Alumno> getAlumnos() {
		return (List<Alumno>) this.alumnoDAO.findAll();
	}

	@Transactional(readOnly=true)
	@Override
	public Alumno getAlumno(long id) {
		return this.alumnoDAO.findById(id).orElse(null);
	}

	@Transactional
	@Override
	public Alumno postAlumno(Alumno alumno) {
		return this.alumnoDAO.save(alumno);
	}

	@Transactional
	@Override
	public Alumno putAlumno(Alumno alumno, long id) {
		Alumno toUpdateAlumno = getAlumno(id);
		
		if (toUpdateAlumno==null) return null;
		
		toUpdateAlumno.setNombre(alumno.getNombre());
		toUpdateAlumno.setApellido(alumno.getApellido());
		toUpdateAlumno.setDni(alumno.getDni());
		toUpdateAlumno.setEmail(alumno.getEmail());
		toUpdateAlumno.setTelefono(alumno.getTelefono());
		toUpdateAlumno.setDireccion(alumno.getDireccion());
		toUpdateAlumno.setCp(alumno.getCp());
		toUpdateAlumno.setImgAvatar(alumno.getImgAvatar());
		return this.alumnoDAO.save(toUpdateAlumno);
	}

	@Transactional
	@Override
	public Alumno deleteAlumno(long id) {
		Alumno deletedArticulo = this.alumnoDAO.findById(id).orElse(null);
		this.alumnoDAO.deleteById(id);
		return deletedArticulo;
	}

}
