package it.polito.esame.bean;

import java.util.*;

public class StudenteIdMap {
	private Map<Integer, Studente>map;
	
	public StudenteIdMap(){
		this.map= new HashMap<Integer,Studente>();
	}
	
	public Studente get(Integer id){
		return map.get(id);
	}
	
	public Studente put(Studente value){
		Studente old = map.get(value);
		if(old == null){
			map.put(value.getMatricola(), value);
			return value;
		} else 
			return old;
	}
	
	
}