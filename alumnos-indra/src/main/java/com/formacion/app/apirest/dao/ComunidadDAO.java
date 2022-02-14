package com.formacion.app.apirest.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.formacion.app.apirest.entity.Comunidad;

@Repository
public interface ComunidadDAO extends CrudRepository<Comunidad,Long> {

}
