package classes;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
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
	
	public boolean saveToFile(RandomAccessFile file) throws IOException {
		file.seek(file.length());
		file.writeLong(pos);
		file.writeInt(chave);
		return true;
	}
	
	public boolean saveToFile(RandomAccessFile file, long filePos) throws IOException {
		file.seek(filePos);
		file.writeLong(pos);
		file.writeInt(chave);
		return true;
	}
	
	public static Indice readFromFile(RandomAccessFile data) throws IOException {

		Indice novo = null;

		long pos = data.readLong();
		int rg = data.readInt();

		novo = new Indice(pos, rg);

		return novo;
	}
}
