package it.polito.tdp.crimes.model;

public class TipoReato {
    String tipo;

	public TipoReato(String tipo) {
		this.tipo = tipo;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	@Override
	public String toString() {
		return tipo ;
	}
    
}
