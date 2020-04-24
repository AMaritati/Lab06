package it.polito.tdp.meteo.model;

import java.util.Date;

public class Rilevamento {
	
	private String localita;
	private Date data;
	private int umidita;
	private double media;

	public Rilevamento(String localita, Date data, int umidita) {
		this.localita = localita;
		this.data = data;
		this.umidita = umidita;
	}

	public Rilevamento(String localita, double media) {
		this.localita = localita;
		this.media = media;
	} 
	
	public String getLocalita() {
		return localita;
	}

	public void setLocalita(String localita) {
		this.localita = localita;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public int getUmidita() {
		return umidita;
	}

	public void setUmidita(int umidita) {
		this.umidita = umidita;
	}
	
	

	 @Override
	 public String toString() {
	 return "\t" + localita + "\t" + data + "\t" + umidita+ "\n";
	 }

	
	 //@Override
	 //public String toString() {
	 //	return String.valueOf(umidita);
	 //}

	public String descriviMedia() {
		return localita+" - " + media;
	}

}
