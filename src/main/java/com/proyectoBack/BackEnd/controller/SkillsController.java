/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.proyectoBack.BackEnd.controller;

import com.proyectoBack.BackEnd.dto.DtoSkills;
import com.proyectoBack.BackEnd.entity.Skills;
import com.proyectoBack.BackEnd.security.controller.Mensaje;
import com.proyectoBack.BackEnd.service.SSkills;
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
@RequestMapping("/skills")
@CrossOrigin(origins = "http://localhost:4200")
public class SkillsController {
    
    @Autowired
    SSkills sSkills;
    
    @GetMapping("/lista")
    public ResponseEntity<List<Skills>> lista(){
        List<Skills> list = sSkills.list();
        return new ResponseEntity(list, HttpStatus.OK);
    }
    
    @GetMapping("/detail/{id}")
    public ResponseEntity<Skills> getById(@PathVariable("id") int id){
        if(!sSkills.existById(id))
            return new ResponseEntity(new Mensaje("no existe"), HttpStatus.NOT_FOUND);
        Skills skills = sSkills.getOne(id).get();
        return new ResponseEntity(skills, HttpStatus.OK);
    }
   
    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody DtoSkills dtoSkills){      
        if(StringUtils.isBlank(dtoSkills.getNombre()))
            return new ResponseEntity(new Mensaje("El nombre es obligatorio"), HttpStatus.BAD_REQUEST);
        if(sSkills.existsByNombre(dtoSkills.getNombre()))
            return new ResponseEntity(new Mensaje("Ese Skill ya existe"), HttpStatus.BAD_REQUEST);
        
        Skills skills = new Skills(dtoSkills.getNombre(), dtoSkills.getPorcentaje());
        sSkills.save(skills);
        
        return new ResponseEntity(new Mensaje("Skill  agregada"), HttpStatus.OK);
    }
    
    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable ("id") int id, @RequestBody DtoSkills dtoSkills){
        //validamos si existe en id
        if(!sSkills.existById(id))
            return new ResponseEntity(new Mensaje("El ID no existe."), HttpStatus.BAD_REQUEST);
        //compara el nombre de experiencia
        if(sSkills.existsByNombre(dtoSkills.getNombre()) && sSkills.getByNombre(dtoSkills.getNombre()).get().getId() !=id)
            return new ResponseEntity(new Mensaje("Este skill ya existe."), HttpStatus.BAD_REQUEST);
        //no puede estar vacio
        if(StringUtils.isBlank(dtoSkills.getNombre()))
            return new ResponseEntity(new Mensaje("El nombre es obligatorio"), HttpStatus.BAD_REQUEST);
        
        Skills skills = sSkills.getOne(id).get();
        skills.setNombre(dtoSkills.getNombre());
        skills.setPorcentaje(dtoSkills.getPorcentaje());
        
        sSkills.save(skills);
        return new ResponseEntity(new Mensaje("Skill actualizada"), HttpStatus.OK);
            
    }
    
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable ("id") int id){
        
         //validamos si existe en id
        if(!sSkills.existById(id))
            return new ResponseEntity(new Mensaje("El ID no existe."), HttpStatus.BAD_REQUEST);
        
        sSkills.delete(id);
        
        return new ResponseEntity(new  Mensaje("Skill eliminada"), HttpStatus.OK);
    }
}

