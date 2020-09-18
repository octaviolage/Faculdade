package classes;

import java.io.IOException;
import java.io.RandomAccessFile;

public class Pessoa implements Comparable<Pessoa> {

	private int rg;
	private String nome;
	private String nasc;
	
	public Pessoa(int rg, String nome, String nasc) {
		
        this.rg = rg;
		this.nome = nome;
		this.nasc = nasc;
	}
	
	public static Pessoa separador() {
		return new Pessoa(-1, " ", " ");
	}
	
	public boolean saveToFile(RandomAccessFile file) throws IOException {
		
		file.seek(file.length());
		file.writeInt(this.rg);
		file.writeUTF(this.nome);
		file.writeUTF(this.nasc);
		
		return true;
	}
	
	public static Pessoa readFromFile(RandomAccessFile data) throws IOException {
		
		Pessoa nova = null;
		
		int rg = data.readInt();
		String nome = data.readUTF();
		String nasc = data.readUTF();
		
		nova = new Pessoa(rg, nome, nasc);
		
		return nova;
	}

	public int compareTo(Pessoa o) {
		Pessoa outro = o;
		int resultado = this.nome.compareToIgnoreCase(outro.nome);
		if (resultado > 0) {
			return 1;
		}
		else if (resultado < 0){
			return -1;
		}
		
		return 0;
	}
	
	public String toString() {
		return this.nome;
	}
}
