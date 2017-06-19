package it.polito.esame.bean;

import java.util.*;

import it.polito.esame.dao.IscrittiDAO;

public class Model {
	private StudenteIdMap mapS;
	private List<Corso> corsi;
	private int max;

	public StudenteIdMap getMappaStudenti(){
		if(mapS == null){
			this.mapS=new StudenteIdMap();
			IscrittiDAO dao = new IscrittiDAO();
			List<Studente> studenti= dao.getAllStudente();
			
			for(Studente s : studenti){
				mapS.put(s);
			}
		}
		return mapS;
	}
	
	public List<Corso> getTuttiCorsi(){
		if(corsi==null){
			IscrittiDAO dao = new IscrittiDAO();
			corsi = dao.getAllCorso();
		}
		return corsi;
	}
	
	
	public Set<Corso> getCorsiDelloStudente(Studente s){
		Set<Corso> caricoDidattico = s.getCaricodidattico();
		
		if(caricoDidattico.isEmpty()){
			IscrittiDAO dao = new IscrittiDAO();
			caricoDidattico =dao.getCorsiByStudente(s);
		}
		return caricoDidattico;
	}
	
	public Set<Studente> getStudentiCheCondividonoCorsiMax(Studente s){
	this.max=0;
	
		Set<Studente> amiciDiCorso = new HashSet<Studente>();
		
		IscrittiDAO dao = new IscrittiDAO();
		
		Map<Studente, Integer> amici = dao.getStudentiConCorsiInComune(s, mapS);
		
		//setto il max al massimo valore degli esami in comune che c'è nella mappa
		for(Integer i: amici.values()){			
			if(i>max && i!= max)
				max = i;
		}
		
		for(Studente amico: amici.keySet()){
			if(amici.get(amico)==max){
				amiciDiCorso.add(amico);
			}
		}
		
		
	return amiciDiCorso;	
	}

	public int getMax() {

		return max;
	}
}
