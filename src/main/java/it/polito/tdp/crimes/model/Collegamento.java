package it.polito.tdp.crimes.model;

public class Collegamento {
     TipoReato tp1;
     TipoReato tp2;
     int peso;
	public Collegamento(TipoReato tp1, TipoReato tp2, int peso) {
		this.tp1 = tp1;
		this.tp2 = tp2;
		this.peso = peso;
	}
	public TipoReato getTp1() {
		return tp1;
	}
	public void setTp1(TipoReato tp1) {
		this.tp1 = tp1;
	}
	public TipoReato getTp2() {
		return tp2;
	}
	public void setTp2(TipoReato tp2) {
		this.tp2 = tp2;
	}
	public int getPeso() {
		return peso;
	}
	public void setPeso(int peso) {
		this.peso = peso;
	}
	@Override
	public String toString() {
		return tp1 + "-" + tp2 + " " + peso + "\n";
	}
     
}
