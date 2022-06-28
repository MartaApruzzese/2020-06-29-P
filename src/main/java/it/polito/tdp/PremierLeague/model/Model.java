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
	
	public Model() {
		this.dao= new PremierLeagueDAO();
	}
	
	public void creaGrafo(int minuti, int mese) {
		this.grafo= new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		this.idMap= new HashMap<>();
		this.vertici= new ArrayList<>();
		
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
}
