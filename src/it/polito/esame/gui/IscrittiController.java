/**
 * Sample Skeleton for 'iscrittiT1.fxml' Controller Class
 */

package it.polito.esame.gui;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Set;

import it.polito.esame.bean.Corso;
import it.polito.esame.bean.Model;
import it.polito.esame.bean.Studente;
import it.polito.esame.bean.StudenteIdMap;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

public class IscrittiController {
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="txtMatricola"
    private TextField txtMatricola; // Value injected by FXMLLoader

    @FXML // fx:id="x1"
    private Color x1; // Value injected by FXMLLoader

    @FXML // fx:id="btnElencoCorsi"
    private Button btnElencoCorsi; // Value injected by FXMLLoader

    @FXML // fx:id="btnStudentiSimili"
    private Button btnStudentiSimili; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doElencoCorsi(ActionEvent event) {
    	txtResult.clear();
    	String matricola = txtMatricola.getText();
    	
    	try{
    		int mat= Integer.parseInt(matricola);
    		if(mat<=0){
    			txtResult.appendText("Errore: inserire matricola, maggiore di 0.\n");
    			return;
    		}
    		
    		StudenteIdMap map= model.getMappaStudenti();
    		Studente s = map.get(mat);
    		
    		if(s==null){
    			txtResult.appendText("Studente non presente nel DB.\n");
    			return;
    		}
    		
    		txtResult.appendText("Studente: "+s.toString()+"\n");
    		
    		Set<Corso> corsi = model.getCorsiDelloStudente(map.get(mat));
    		for(Corso c : corsi){
    			txtResult.appendText(c.toString()+"\n");
    		}
    		
    	}catch(NumberFormatException e){
    		txtResult.appendText("Errore iserire matricola con sole cifre.\n");
    	}
    }

    @FXML
    void doStudentiSimili(ActionEvent event) {
    	txtResult.clear();
    	String matricola = txtMatricola.getText();
    	
    	try{
    		int mat= Integer.parseInt(matricola);
    		if(mat<=0){
    			txtResult.appendText("Errore: inserire matricola, maggiore di 0.\n");
    			return;
    		}
    		
    		StudenteIdMap map= model.getMappaStudenti();
    		Studente s = map.get(mat);
    		
    		if(s==null){
    			txtResult.appendText("Studente non presente nel DB.\n");
    			return;
    		}
    		  		
    		Set<Studente> studenti = model.getStudentiCheCondividonoCorsiMax(s);
    		txtResult.appendText("Massimo numero dei corsi comuni: "+model.getMax()+"\n");
    		
    		for(Studente stemp: studenti){
    			txtResult.appendText(stemp.toString()+"\n");
    		}
    		
    		
    	}catch(NumberFormatException e){
    		txtResult.appendText("Errore iserire matricola con sole cifre.\n");
    	}
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert txtMatricola != null : "fx:id=\"txtMatricola\" was not injected: check your FXML file 'iscrittiT1.fxml'.";
        assert x1 != null : "fx:id=\"x1\" was not injected: check your FXML file 'iscrittiT1.fxml'.";
        assert btnElencoCorsi != null : "fx:id=\"btnElencoCorsi\" was not injected: check your FXML file 'iscrittiT1.fxml'.";
        assert btnStudentiSimili != null : "fx:id=\"btnStudentiSimili\" was not injected: check your FXML file 'iscrittiT1.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'iscrittiT1.fxml'.";

    }

	public void setModel(Model model) {
		this.model=model;
		
	}
}
