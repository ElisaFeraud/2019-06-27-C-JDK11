package it.polito.tdp.crimes.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;


import it.polito.tdp.crimes.db.EventsDao;

public class Model {
	EventsDao dao;
	Map<String,TipoReato> idMap;
	Graph<TipoReato,DefaultWeightedEdge> grafo;
	List<TipoReato> best;
	int pesoMax;
	public Model() {
		dao = new EventsDao();
	}
	public List<String> getTipiDiReato() {
		return dao.getTipiDiReato();
	}
	public List<Integer> getGiorni(){
		return dao.getGiorni();
	}
	public void creaGrafo(int giorni, String categoria) {
		idMap = new HashMap<>();
		dao.getVertici(idMap, giorni, categoria);
		this.grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
    	Graphs.addAllVertices(grafo, idMap.values());
    	for(Collegamento c: dao.getArchi(idMap,categoria,giorni)) {
    		if(this.grafo.containsVertex(c.getTp1()) && this.grafo.containsVertex(c.getTp2())) {
    			DefaultWeightedEdge e = this.grafo.getEdge(c.getTp1(),c.getTp2());
    			if(e==null) {
    				Graphs.addEdgeWithVertices(grafo,c.getTp1(),c.getTp2(),c.getPeso());
    			}
    		}
    	}
	}
	public String infoGrafo() {
		 return "Grafo creato con "+ this.grafo.vertexSet().size()+ " vertici e " + this.grafo.edgeSet().size()+" archi\n";
	 }
	public double getMediano() {
		int min=1000;
		for(DefaultWeightedEdge d: this.grafo.edgeSet()) {
			if(this.grafo.getEdgeWeight(d)<min)
				min=(int) this.grafo.getEdgeWeight(d);
		}
		int max =-1;
		for(DefaultWeightedEdge d: this.grafo.edgeSet()) {
			if(this.grafo.getEdgeWeight(d)>max)
				max=(int) this.grafo.getEdgeWeight(d);
		}
		double mediano = (min+max)/2;
		System.out.println(""+ mediano);
		return mediano;
	}
	 public List<Collegamento> getArchiMediano(){
		 double mediano = this.getMediano();
		 List<Collegamento> result = new LinkedList<>();
		 for(DefaultWeightedEdge d : this.grafo.edgeSet()) {
			 if(this.grafo.getEdgeWeight(d)<mediano) {
				 TipoReato tp= this.grafo.getEdgeSource(d);
				 TipoReato tp2 = this.grafo.getEdgeTarget(d);
				 int peso = (int) this.grafo.getEdgeWeight(d);
				 Collegamento collegamento = new Collegamento(tp,tp2,peso);
				 result.add(collegamento);
			 }
		 }
		 System.out.println(""+result.size());
		 return result;
	 }
	  public List<TipoReato> camminoPesoMax(Collegamento collegamento){
		    best = null;	
			  pesoMax=0;
			  List<TipoReato> parziale = new LinkedList<TipoReato>();
			  TipoReato tp1 = collegamento.getTp1();
			  TipoReato tp2 = collegamento.getTp2();
			  parziale.add(tp1);
		      doCammino(parziale,tp2);
		    	
		    	
		    	return best;
		    }
		  public int stampaPeso() {
			  return pesoMax;
		  }
		  private void doCammino(List<TipoReato> parziale, TipoReato tp2) {
				// TODO Auto-generated method stub
			  TipoReato ultimo = parziale.get(parziale.size()-1);
				if(ultimo.equals(tp2) ) {
					
					if(calcolaPeso(parziale)>pesoMax) {
						
						pesoMax =  calcolaPeso(parziale);
						best=new LinkedList<>(parziale);
					}
					return;
					}
				
				
				List<TipoReato> adiacenti = Graphs.neighborListOf(this.grafo, ultimo);
				for(TipoReato t : adiacenti) {
					if(!parziale.contains(t)) {
						parziale.add(t);
						
						doCammino(parziale,tp2);
						parziale.remove(parziale.size()-1);
						}
				}
				
			}
		  private int calcolaPeso(List<TipoReato> parziale) {
				// TODO Auto-generated method stub
				int peso=0;
				for(TipoReato t : parziale) {
					for(TipoReato t2 : parziale) {
						if(this.grafo.containsVertex(t) && this.grafo.containsVertex(t2) && this.grafo.containsEdge(t, t2)) {
							DefaultWeightedEdge e = this.grafo.getEdge(t, t2);
							double n=  grafo.getEdgeWeight(e);
							peso +=n;
						}
							
						}
					}
				
				return peso;
			}
}
