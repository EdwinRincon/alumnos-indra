package com.formacion.app.apirest.controller;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.formacion.app.apirest.entity.Alumno;
import com.formacion.app.apirest.service.AlumnoServiceImpl;

@RestController
@RequestMapping("/alumnos")
public class AlumnoController {
	
	@Autowired
	AlumnoServiceImpl alumnoServiceImpl;
	
	@GetMapping("/all")
	public ResponseEntity<List<Alumno>> getAllAlumnos() {
		return new ResponseEntity<>(alumnoServiceImpl.getAlumnos(), HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> getAlumno(@PathVariable Long id) {
		Alumno alumno= null;
		Map<String, Object> response = new HashMap<>();
		try {
			alumno = alumnoServiceImpl.getAlumno(id);
		} catch (DataAccessException e) {
			// TODO: handle exception
			response.put("mensaje", "Error al realizar consulta a la base de datos");
			response.put("error", e.getMessage().concat("_ ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		if (alumno == null) {
			response.put("mensaje", "El alumno ID: " + id.toString() + " no existe en la base de datos");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Alumno>(alumno, HttpStatus.OK);

	}
	
	
	
	@PostMapping("")
	public ResponseEntity<?> postAlumno(@RequestBody Alumno alumno) {
		Alumno newAlumno = null;
		Map<String, Object> response = new HashMap<>();
		try {
			newAlumno = alumnoServiceImpl.postAlumno(alumno);
		} catch (DataAccessException e) {
			// TODO: handle exception
			response.put("mensaje", "Error al guardar en la base de datos");
			response.put("error", e.getMessage().concat("_ ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		if (newAlumno == null) {
			response.put("mensaje", "El alumno: " + alumno.getNombre() + " no se ha guardado en la base de datos");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CONFLICT);
		}
		response.put("alumno", newAlumno);
		response.put("mensaje", "Se ha guardado exitosamente!");
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);

	}
	
	
	
	@PutMapping("/{id}")
	public ResponseEntity<?> putAlumno(@RequestBody Alumno alumno, @PathVariable long id) {
		Alumno editAlumno = null;
		Map<String, Object> response = new HashMap<>();
		try {
			editAlumno = alumnoServiceImpl.putAlumno(alumno, id);
		} catch (DataAccessException e) {
			// TODO: handle exception
			response.put("mensaje", "Error al editar al alumno");
			response.put("error", e.getMessage().concat("_ ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		if (editAlumno == null) {
			response.put("mensaje", "No se han hecho cambios para este alumno");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CONFLICT);
		}
		response.put("alumno", editAlumno);
		response.put("mensaje", "Se ha editado exitosamente!");
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);

	}	
	
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteAlumno(@PathVariable long id) {
		Alumno deleteAlumno = null;
		Map<String, Object> response = new HashMap<>();
		try {
			deleteAlumno = alumnoServiceImpl.deleteAlumno(id);
			deleteLocalImage(deleteAlumno);
		} catch (DataAccessException e) {
			// TODO: handle exception
			if (deleteAlumno == null) {
				response.put("error2", "No existe el alumno: " + id);
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CONFLICT);
			}
			response.put("mensaje", "Error al eliminar al alumno");
			response.put("error", e.getMessage().concat("_ ").concat(e.getMostSpecificCause().getMessage()));

			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("alumno", deleteAlumno);
		response.put("mensaje", "Se ha eliminado exitosamente!");
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);

	}
	
	
	// Subir Imagenes al servidor
	@PostMapping("/upload/img/{id}")
	public ResponseEntity<?> uploadImagen(@RequestParam("archivo") MultipartFile archivo,@PathVariable Long id){
		Alumno alumno = alumnoServiceImpl.getAlumno(id);
		Map<String, Object> response = new HashMap<>();
		
		if(!archivo.isEmpty()) {
			String nombreArchivo= UUID.randomUUID().toString()+"_"+archivo.getOriginalFilename().replace(" ","");
			Path rutaArchivo = Paths.get("uploads").resolve(nombreArchivo).toAbsolutePath();
			
			try {
				Files.copy(archivo.getInputStream(), rutaArchivo);
			} catch (IOException e) {
				// TODO: handle exception
				response.put("mensaje", "Error al subir el archivo");
				return new ResponseEntity<Map<String,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
				
			}			
			
			deleteLocalImage(alumno);
			alumno.setImgAvatar(nombreArchivo);
			alumnoServiceImpl.putAlumno(alumno, id);
			response.put("mensaje", "subida correcta de imagen "+ nombreArchivo);
		} else {
			response.put("Archivo", "archivo vacio");
		}
		return new ResponseEntity<Map<String,Object>>(response,HttpStatus.CREATED);
	}
	
	
	
	// Elimina las imagenes de la carpeta /uploads
	private void deleteLocalImage(Alumno alumno) {
		String oldImage = alumno.getImgAvatar();
		
		if(oldImage != null && oldImage.length()>0) {
			Path pathOldImage = Paths.get("uploads").resolve(oldImage).toAbsolutePath();
			File fileOldImage = pathOldImage.toFile();
			
			if(fileOldImage.exists() && fileOldImage.canRead()) {
				fileOldImage.delete();
			}
		}
	}
	
	// Devuelve una imagen
	@GetMapping("download/img/{nombreImagen:.+}")
	public ResponseEntity<Resource> getImagen(@PathVariable String nombreImagen){
		Path rutaArchivo = Paths.get("uploads").resolve(nombreImagen).toAbsolutePath();
		Resource recurso = null;
			
		try {
			recurso = new UrlResource(rutaArchivo.toUri());
		} catch (MalformedURLException e) {
			e.printStackTrace();
			// TODO: handle exception
		}
			
		if(!recurso.exists() && !recurso.isReadable()) {
			throw new RuntimeException("Error no se puede cargar la imagen "+ nombreImagen);
		}
			
		HttpHeaders cabecera = new HttpHeaders();
		cabecera.add(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=\""+recurso.getFilename());
			
			
		return new ResponseEntity<Resource>(recurso,cabecera,HttpStatus.OK);
		}
	

}
