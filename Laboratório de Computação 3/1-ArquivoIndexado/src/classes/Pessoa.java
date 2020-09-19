package classes;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.Serializable;

public class Pessoa implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int rg;
	private String nome;
	private String nasc;
	
	public Pessoa(int rg, String nome, String nasc) {
		
		this.rg = rg;
		this.nome = nome;
		this.nasc = nasc;
	}
	
	public int getRg() {
		return rg;
	}
	
	@Override
	public String toString() {
		return this.rg + "; " + this.nome + "; " + this.nasc;
	}
	
	public boolean saveToFile(RandomAccessFile file) throws IOException {
		file.seek(file.length());
		file.writeInt(this.rg);
		file.writeUTF(this.nome);
		file.writeUTF(this.nasc);
		return true;
	}
}
