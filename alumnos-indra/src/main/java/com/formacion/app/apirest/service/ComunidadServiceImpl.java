package com.formacion.app.apirest.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.formacion.app.apirest.dao.ComunidadDAO;
import com.formacion.app.apirest.entity.Comunidad;

@Service
public class ComunidadServiceImpl implements ComunidadService {

	@Autowired
	ComunidadDAO comunidadDAO;

	@Transactional(readOnly=true)
	@Override
	public List<Comunidad> getComunidades() {
		return (List<Comunidad>) this.comunidadDAO.findAll();
	}

	@Transactional(readOnly=true)
	@Override
	public Comunidad getComunidad(long id) {
		return this.comunidadDAO.findById(id).orElse(null);
	}

	@Transactional
	@Override
	public Comunidad postComunidad(Comunidad comunidad) {
		return this.comunidadDAO.save(comunidad);
	}

	@Transactional
	@Override
	public Comunidad putComunidad(Comunidad comunidad, long id) {
		Comunidad toUpdateComunidad = getComunidad(id);
		
		if (toUpdateComunidad==null) return null;
		
		toUpdateComunidad.setNombreComunidad(comunidad.getNombreComunidad());
		return this.comunidadDAO.save(toUpdateComunidad);
	}

	@Override
	public Comunidad deleteComunidad(long id) {
		Comunidad deletedComunidad = this.comunidadDAO.findById(id).orElse(null);
		this.comunidadDAO.deleteById(id);
		return deletedComunidad;
	}

}
