package it.polito.tdp.meteo.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import it.polito.tdp.meteo.DAO.MeteoDAO;


public class Model {
	
	MeteoDAO m;
	private final static int COST = 100;
	private final static int NUMERO_GIORNI_CITTA_CONSECUTIVI_MIN = 3;
	private final static int NUMERO_GIORNI_CITTA_MAX = 6;
	private final static int NUMERO_GIORNI_TOTALI = 15;
	
	private int bestCosto;
	private List<Rilevamento> bestSoluzione;
	private List <Rilevamento> rilevamenti;
	
	

	public Model() {
		m = new MeteoDAO();
		bestSoluzione = new ArrayList<>();
		rilevamenti = new ArrayList<>();
	}

	/**
	 * Utilizzo questo metodo per racimolare una lista di rilevamenti
	 * di un certo mese con soli i primi 15 giorni
	 * @param mese mese che si vuole studiare
	 */
	public void rilevamentiMese(int mese) {
		rilevamenti = new ArrayList<>();
		for (String s : m.getAllCitta()) {
			for(Rilevamento r : m.getAllRilevamenti()) {
				if (r.getLocalita().equals(s) && r.getData().getMonth()==mese-1 && r.getData().getDate()>=1 && r.getData().getDate()<=NUMERO_GIORNI_TOTALI) {
					rilevamenti.add(r);
				}
			}
		}
	}
	 // questo metodo potevo usufruirne nel dao , cercando il mese che voglio limitando successivamente a 15 i giorni
	
	/**
	 * metodo per il completamento del primo esercizio
	 * @param mese mese passato come parametro di cu si vuole fare la media
	 * @return
	 */
	public String getUmiditaMedia(int mese) {
		return m.getMediaLocalitaMese(mese);
	}
	
	/**
	 * indica il metodo che chiamerò nel Controller per eseguire il controllo
	 * @param mese
	 * @return
	 */
public List<Rilevamento> sequenzaCitta(int mese) {
	    
		rilevamentiMese(mese);
		
		bestSoluzione = null;
		bestCosto = Integer.MAX_VALUE;
		
		List<Rilevamento> parziale = new ArrayList<>();
		
		cerca(parziale,1);  // devo partire da uno perchè i giorni non partono da 0
		

		return bestSoluzione;
	}
	
/**
 * E' il metodo per eccellenza della ricorsione
 * @param parziale
 * @param l
 */
private void cerca(List<Rilevamento> parziale, int l) {
	
	// casi terminali
	
	if (!calcolaGiorni(parziale)) {
		return;
	}
	
	if (!calcolaGiorniCons(parziale)) {
		return;
	}
	
	if (parziale.size() == NUMERO_GIORNI_TOTALI) {
		int costo = calcolaCosto(parziale) ;
		if(costo < bestCosto) {
			bestCosto = costo;
			bestSoluzione = new ArrayList<>(parziale);
		}
		
	}
		
	// generiamo i sottoproblemi
	
	
	List<Rilevamento> sottoProblema = get(l);
	
	for(Rilevamento r: sottoProblema) {
		parziale.add(r);
		cerca(parziale,l+1);
		parziale.remove(parziale.size()-1);
	}
}

/**
 * 	Utilizzo questo metodo per associare al livello il giorno del mese
 * @param livello livello della ricorsione
 * @return
 */
public List<Rilevamento> get(int livello){
	
	List<Rilevamento> sottoProblema = new ArrayList<>();
	
	for(Rilevamento r: rilevamenti) {
		if(r.getData().getDate()==livello) {
			sottoProblema.add(r);
		}
	}
	
	return sottoProblema;
}

/**
 * funzione per calcolare il costo
 * @param parziale indica la nostra soluzione
 * @return costo ritornerà il costo totale
 */
private int calcolaCosto(List<Rilevamento> parziale) {

	String loc = parziale.get(0).getLocalita();
	boolean x = false;
	int costo = 0;
	
	for(int i = 0; i<parziale.size(); i++) {
		costo += parziale.get(i).getUmidita(); 
		if(x == false) {
			if(parziale.get(i).getLocalita().equals(loc)) {
			
			}
			else {
				x = true;
			}
		}
		if(x == true) {
			loc = parziale.get(i).getLocalita();
			costo += COST;
			x = false;
		}
	}
	
	return costo;
	
	}
 // qui devo creare le funzioni vincoli

// 1. Count compreso tra 1 e 6
private boolean calcolaGiorni(List<Rilevamento> parziale) {
	int count = 0;
	
	for (String s : m.getAllCitta()) {
		count = 0;
		
		for (Rilevamento r : parziale) {
			if (r.getLocalita().equals(s)) {
				count++;
			}
			
		}
		
		if (count>6) {
			return false;
		}
	}
	
	return true;
}
// 2. count consecutivo maggiore_uguale di 3
private boolean calcolaGiorniCons(List<Rilevamento> parziale) {
	String citta = "";
	int count = 0;
	boolean x = true;
	
	for(int i = 0; i<parziale.size(); i++) {
		if(x==false) {
			if(parziale.get(i).getLocalita().equals(citta)) {
				count++;
			}
			else {
				x = true;
				if(count<3) {
					return false;
				}
			}
		}
		if(x == true) {
			citta = parziale.get(i).getLocalita();
			count = 1;
			x = false;
		}
	}
	if(parziale.size()==NUMERO_GIORNI_TOTALI && count<3) {
		return false;
	}
	
	
	return true;
}

public String descriviSequenza(List<Rilevamento> parziale) {
	String elenco = "";
	
	for (Rilevamento r : parziale) {
		elenco += r.getLocalita()+"\n";
	}
	return elenco;
}

}
