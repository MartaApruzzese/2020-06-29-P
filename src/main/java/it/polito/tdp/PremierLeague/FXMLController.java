/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.PremierLeague;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.PremierLeague.model.Adiacenza;
import it.polito.tdp.PremierLeague.model.Match;
import it.polito.tdp.PremierLeague.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {

	Model model;
	
    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnConnessioneMassima"
    private Button btnConnessioneMassima; // Value injected by FXMLLoader

    @FXML // fx:id="btnCollegamento"
    private Button btnCollegamento; // Value injected by FXMLLoader

    @FXML // fx:id="txtMinuti"
    private TextField txtMinuti; // Value injected by FXMLLoader

    @FXML // fx:id="cmbMese"
    private ComboBox<Integer> cmbMese; // Value injected by FXMLLoader

    @FXML // fx:id="cmbM1"
    private ComboBox<Match> cmbM1; // Value injected by FXMLLoader

    @FXML // fx:id="cmbM2"
    private ComboBox<Match> cmbM2; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doConnessioneMassima(ActionEvent event) {
    	txtResult.clear();
    	List<Adiacenza> maxConn= new ArrayList<>();
    	maxConn.addAll(this.model.getConnessioneMax());
    	if(maxConn.isEmpty()) {
    		txtResult.setText("ERRORE");
    		return;
    	}
    	txtResult.setText("Coppie con connessione massima: \n");
    	
    	for(Adiacenza a: maxConn) {
    		txtResult.appendText(a.getM1().toString()+" - "+a.getM2().toString()+" "+a.getPeso()+"\n");
    	}
    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	txtResult.clear();
    	
    	int mese;
    	try{
    		mese= cmbMese.getValue();
    	}catch(NullPointerException e) {
    		txtResult.setText("Selezionare un mese dall'apposita box");
    		return;
    	}
    	int minuti;
    	try {
    		minuti= Integer.parseInt(txtMinuti.getText());
    		if(minuti<0 || minuti>90) {
    			txtResult.setText("Inserire un valore compreso tra 0 e 90 minuti.");
    			return;
    		}
    	}catch(NumberFormatException e) {
    		txtResult.setText("Inserire un valore compreso tra 0 e 90 minuti.");
    		return;
    	}
    	
    	
    	this.model.creaGrafo(minuti, mese);
    	txtResult.setText("Creato grafo con "+this.model.getNVertici()+" vertici e "+this.model.getNArchi()+" archi.\n");
 
    	cmbM1.getItems().clear();
    	cmbM2.getItems().clear();
    	for(Match m: this.model.getVertici()) {
    		cmbM1.getItems().add(m);
    		cmbM2.getItems().add(m);
    	}
    }

    @FXML
    void doCollegamento(ActionEvent event) {
    	Match m1;
    	Match m2;
    	try {
    		m1=cmbM1.getValue();
    		m2=cmbM2.getValue();
    	}catch(NullPointerException e) {
    		txtResult.setText("Inserire un match di arrivo e uno di partenza dall'apposita box.\n");
    		return;
    	}
    	
    	List<Match> percorso=new ArrayList<>();
    	percorso.addAll(this.model.calcolaPercorso(m1, m2));
    	if(percorso.isEmpty()) {
    		txtResult.setText("ERRORE");
    		return;
    	}
    	
    	txtResult.clear();
    	txtResult.setText("Il percorso con peso maggiore da "+m1.toString()+" a "+m2.toString()+" é: \n");
    	for(Match m: percorso) {
    		txtResult.appendText(m.toString()+"\n");
    	}
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnConnessioneMassima != null : "fx:id=\"btnConnessioneMassima\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnCollegamento != null : "fx:id=\"btnCollegamento\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtMinuti != null : "fx:id=\"txtMinuti\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbMese != null : "fx:id=\"cmbMese\" was not injected: check your FXML file 'Scene.fxml'.";        assert cmbM1 != null : "fx:id=\"cmbM1\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbM2 != null : "fx:id=\"cmbM2\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
  
    	cmbMese.getItems().clear();
    	for(int i=1; i<13; i++) {
    		cmbMese.getItems().add(i);
    	}
    }
    
    
}
