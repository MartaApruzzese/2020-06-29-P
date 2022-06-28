package it.polito.tdp.PremierLeague.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.PremierLeague.db.PremierLeagueDAO;

public class Model {
	private PremierLeagueDAO dao;
	private Graph<Match, DefaultWeightedEdge> grafo;
	private Map<Integer, Match> idMap;
	private List<Match> vertici;
	private List<Adiacenza> adiacenze;
	private List<Match> best;
	private double pesoBest;
	
	public Model() {
		this.dao= new PremierLeagueDAO();
	}
	
	public void creaGrafo(int minuti, int mese) {
		this.grafo= new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		this.idMap= new HashMap<>();
		this.vertici= new ArrayList<>();
		this.adiacenze= new ArrayList<>();
		
		
		//Popolo idMap
		for(Match m: this.dao.listAllMatches()) {
			idMap.put(m.matchID, m);
		}
		
		//Creo i vertici
		for(Match m: this.dao.getVertici(mese, idMap)) {
			this.vertici.add(m);
		}
		Graphs.addAllVertices(this.grafo, this.vertici);
		
		
		//Creo gli archi
		for(Adiacenza a: this.dao.getAdiacenze(minuti, mese, idMap)) {
			this.adiacenze.add(a);
			Graphs.addEdgeWithVertices(this.grafo, a.getM1(), a.getM2(), a.getPeso());
		}
	}
	
	public int getNArchi() {
		return this.grafo.edgeSet().size();
	}
	
	public List<Match> getVertici(){
		return this.vertici;
	}
	
	
	public int getNVertici() {
		return this.vertici.size();
	}
	
	
	/**
	 * RISOLUZIONE PUNTO 1D
	 */
	
	public List<Adiacenza> getConnessioneMax(){
		double pesoMax=0.0;
		List<Adiacenza> connessioniMax= new ArrayList<>();
		
		for(Adiacenza a: this.adiacenze) {
			if(a.getPeso()>pesoMax) {
				pesoMax= a.getPeso();
			}
		}
		
		for(Adiacenza a: this.adiacenze) {
			if(a.getPeso()== pesoMax) {
				connessioniMax.add(a);
			}
		}
		return connessioniMax;
	}
	
	/**
	 * RISOLUZIONE PUNTO 2: RICORSIONE
	 */
	public List<Match> calcolaPercorso(Match source, Match target){
		this.best= new ArrayList<>();
		this.pesoBest=0.0;
		
		List<Match> parziale= new ArrayList<>();
		parziale.add(source);
		cerca(parziale, 0.0, target);
		
		return best;
	}

	private void cerca(List<Match> parziale, double peso, Match target) {
		//Condizioni di fine
		if(parziale.get(parziale.size()-1).equals(target)) {
			//controllo se sia il best
			if(peso>this.pesoBest) {
				this.pesoBest= peso;
				this.best= new ArrayList<>(parziale);
			}
			
			return;
		}
		
		//Condizioni di ricorsione
		Match precedente= parziale.get(parziale.size()-1);
		for(Match m: Graphs.neighborListOf(this.grafo, precedente)) {
			DefaultWeightedEdge e= this.grafo.getEdge(precedente, m);
			
			//Controllo non siano giocate dalle stesse squadre
			int home1=precedente.getTeamHomeID(); //Home1
			int away1=precedente.getTeamAwayID();
			int home2= m.getTeamHomeID();
			int away2=m.getTeamAwayID();
			if(home1==away2 && home2==away1) {
				return;
			}
			
			if(!parziale.contains(m)) {
				parziale.add(m);
				cerca(parziale, peso+this.grafo.getEdgeWeight(e), target);
				parziale.remove(parziale.size()-1);
			}
		}
	}
	
}
