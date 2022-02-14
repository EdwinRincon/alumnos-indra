package com.formacion.app.apirest.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.formacion.app.apirest.entity.Comunidad;
import com.formacion.app.apirest.service.ComunidadServiceImpl;

@RestController
@RequestMapping("/comunidades")
public class ComunidadController {

	@Autowired
	ComunidadServiceImpl comunidadServiceImpl;
	
	
	@GetMapping("/all")
	public ResponseEntity<List<Comunidad>> getAllComunidades() {
		return new ResponseEntity<>(comunidadServiceImpl.getComunidades(), HttpStatus.OK);
	}
	
	
	@GetMapping("/{id}")
	public ResponseEntity<?> getComunidad(@PathVariable Long id) {
		Comunidad comunidad= null;
		Map<String, Object> response = new HashMap<>();
		try {
			comunidad = comunidadServiceImpl.getComunidad(id);
		} catch (DataAccessException e) {
			// TODO: handle exception
			response.put("mensaje", "Error al realizar consulta a la base de datos");
			response.put("error", e.getMessage().concat("_ ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		if (comunidad == null) {
			response.put("mensaje", "La comunidad con ID: " + id.toString() + " no existe en la base de datos");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Comunidad>(comunidad, HttpStatus.OK);
	}
	
	
	
	@PostMapping("")
	public ResponseEntity<?> postComunidad(@RequestBody Comunidad comunidad) {
		Comunidad newComunidad = null;
		Map<String, Object> response = new HashMap<>();
		try {
			newComunidad = comunidadServiceImpl.postComunidad(comunidad);
		} catch (DataAccessException e) {
			// TODO: handle exception
			response.put("mensaje", "Error al guardar en la base de datos");
			response.put("error", e.getMessage().concat("_ ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		if (newComunidad == null) {
			response.put("mensaje", "La comunidad: " + comunidad.getNombreComunidad() + " no se ha guardado en la base de datos");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CONFLICT);
		}
		response.put("comunidad", newComunidad);
		response.put("mensaje", "Se ha guardado exitosamente!");
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
	
	@PutMapping("/{id}")
	public ResponseEntity<?> putComunidad(@RequestBody Comunidad comunidad, @PathVariable long id) {
		Comunidad editComunidad = null;
		Map<String, Object> response = new HashMap<>();
		try {
			editComunidad = comunidadServiceImpl.putComunidad(comunidad, id);
		} catch (DataAccessException e) {
			// TODO: handle exception
			response.put("mensaje", "Error al editar la comunidad");
			response.put("error", e.getMessage().concat("_ ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		if (editComunidad == null) {
			response.put("mensaje", "No se han hecho cambios para esta comunidad");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CONFLICT);
		}
		response.put("comunidad", editComunidad);
		response.put("mensaje", "Se ha editado exitosamente!");
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);

	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteComunidad(@PathVariable long id) {
		Comunidad deleteComunidad = null;
		Map<String, Object> response = new HashMap<>();
		try {
			deleteComunidad = comunidadServiceImpl.deleteComunidad(id);
		} catch (DataAccessException e) {
			// TODO: handle exception
			if (deleteComunidad == null) {
				response.put("error2", "No existe la comunidad: " + id);
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CONFLICT);
			}
			response.put("mensaje", "Error al eliminar la comunidad");
			response.put("error", e.getMessage().concat("_ ").concat(e.getMostSpecificCause().getMessage()));

			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("comunidad", deleteComunidad);
		response.put("mensaje", "Se ha eliminado exitosamente!");
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);

	}
}
