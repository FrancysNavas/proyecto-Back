/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.proyectoBack.BackEnd.controller;

import com.proyectoBack.BackEnd.dto.DtoProyectos;
import com.proyectoBack.BackEnd.entity.Proyectos;
import com.proyectoBack.BackEnd.security.controller.Mensaje;
import com.proyectoBack.BackEnd.service.SProyectos;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/proyectos")
@CrossOrigin(origins = "http://localhost:4200")
public class ProyectosController {
    
    @Autowired
    SProyectos sProyectos;
    
        @GetMapping("/lista")
    public ResponseEntity<List<Proyectos>> lista(){
        List<Proyectos> list = sProyectos.list();
        return new ResponseEntity(list, HttpStatus.OK);
    }
    
    @GetMapping("/detail/{id}")
    public ResponseEntity<Proyectos> getById(@PathVariable("id") int id){
        if(!sProyectos.existsById(id))
            return new ResponseEntity(new Mensaje("no existe"), HttpStatus.NOT_FOUND);
        Proyectos proyectos = sProyectos.getOne(id).get();
        return new ResponseEntity(proyectos, HttpStatus.OK);
    }
   
    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody DtoProyectos dtoProyectos){      
        if(StringUtils.isBlank(dtoProyectos.getNombre()))
            return new ResponseEntity(new Mensaje("El nombre es obligatorio"), HttpStatus.BAD_REQUEST);
        if(StringUtils.isBlank(dtoProyectos.getDescripcion()))
            return new ResponseEntity(new Mensaje("La descripción es obligatoria"), HttpStatus.BAD_REQUEST);
        
        Proyectos proyectos = new Proyectos(dtoProyectos.getNombre(), dtoProyectos.getDescripcion(), dtoProyectos.getImg());
        sProyectos.save(proyectos);
        
        return new ResponseEntity(new Mensaje("Proyecto agregado"), HttpStatus.OK);
    }
    
    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable ("id") int id, @RequestBody DtoProyectos dtoProyectos){
        //validamos si existe en id
        if(!sProyectos.existsById(id))
            return new ResponseEntity(new Mensaje("El ID no existe."), HttpStatus.BAD_REQUEST);
        
        Proyectos proyectos = sProyectos.getOne(id).get();
        proyectos.setNombre(dtoProyectos.getNombre());
        proyectos.setDescripcion(dtoProyectos.getDescripcion());
        proyectos.setDescripcion(dtoProyectos.getImg());
        
        sProyectos.save(proyectos);
        return new ResponseEntity(new Mensaje("Proyecto actualizado"), HttpStatus.OK);
            
    }
    
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable ("id") int id){
        
         //validamos si existe en id
        if(!sProyectos.existsById(id))
            return new ResponseEntity(new Mensaje("El ID no existe."), HttpStatus.BAD_REQUEST);
        
        sProyectos.delete(id);
        
        return new ResponseEntity(new  Mensaje("Proyecto eliminado"), HttpStatus.OK);
    }
}

