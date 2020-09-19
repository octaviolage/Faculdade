package classes;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Indice implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private long pos;
	private int chave;
	
	public Indice(long pos, int chave) {
		this.pos = pos;
		this.chave = chave;
	}
	
	public long getPos() {
		return this.pos;
	}
	
	public int getChave() {
		return this.chave;
	}
	
	public boolean saveToFile(ObjectOutputStream file) throws IOException {
		file.writeObject(this);
		return true;
	}
}
