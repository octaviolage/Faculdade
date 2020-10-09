package termos;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Termo implements Comparable<Object>, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String palavra;
	private List<Integer> ocorrencias;
		
	public Termo(String palavra) {

		this.palavra = palavra;
		this.ocorrencias = new ArrayList();
	}
	
	public String getPalavra() {
		return palavra;
	}
	
	public void addOcorrencia(int numArquivo) {
		
		for(int ocorrencia : ocorrencias)
			if (ocorrencia == numArquivo)
				return;
		
		ocorrencias.add(numArquivo);
	}
	
	public List<Integer> getOcorrencias() {
		return ocorrencias;
	}

	public boolean saveToFile(ObjectOutputStream file) throws IOException {
		file.writeObject(this);
		return true;
	}
	
	public Termo readFromFile(ObjectInputStream file) throws IOException, ClassNotFoundException {
		Termo aux = (Termo) file.readObject();
		return aux;
	}
	
	@Override
	public int compareTo(Object o) {
		Termo outro = (Termo) o;
		int resultado = this.palavra.compareToIgnoreCase(outro.palavra);
		if (resultado > 0) {
			return 1;
		}
		else if (resultado < 0){
			return -1;
		}
		
		return 0;
	}
	
	public String toString() {
		String aux = palavra;
		for(int ocorrencia : ocorrencias)
			aux += " | " + Integer.toString(ocorrencia);
		
		return aux;
	}
	
}
