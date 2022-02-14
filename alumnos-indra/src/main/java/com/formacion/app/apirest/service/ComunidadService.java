package com.formacion.app.apirest.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.formacion.app.apirest.entity.Comunidad;

@Service
public interface ComunidadService {

	List<Comunidad> getComunidades();

	Comunidad getComunidad(long id);

	Comunidad postComunidad(Comunidad comunidad);

	Comunidad putComunidad(Comunidad comunidad, long id);

	Comunidad deleteComunidad(long id);
}
