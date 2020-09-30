package classes;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class HashLinear { //Escolhi o Hash Linear por questão de simplicidade apesar do desempenho

	private static final String ARQ_INDICES = "indices.bin";
	private static final int TAM_INDICE = 12; //Tamanho do objeto em bytes com um int (4 bytes) e um long (8 bytes)
	private int tamanhoTabela = 0;
	public long colisoes = 0;

	public HashLinear(int tamanho, boolean zerar) throws IOException {

		tamanhoTabela = tamanho;

		if(zerar) {
			Indice indice = new Indice(-1, -1);
			
			RandomAccessFile arqIndice = new RandomAccessFile(new File(ARQ_INDICES), "rw");
			arqIndice.setLength(0);

			for (int i = 0; i < tamanho; i++)
				indice.saveToFile(arqIndice);

			arqIndice.close();
		}

	}

	private int funcaoHash(int chave) {
		chave *= 7.13;  //Melhor tratativa encontrada para o hash linear
		return (chave % tamanhoTabela);
	}

//	public boolean cheia() throws IOException {
//
//		RandomAccessFile arqIndice = new RandomAccessFile(new File(ARQ_INDICES), "rw");
//		Indice aux = null;
//		
//		arqIndice.seek(0);
//		boolean fimArquivo = false;
//		
//		try {
//			while(!fimArquivo) {
//				aux = Indice.readFromFile(arqIndice);
//				if (aux.getChave() <= 0) {
//					return false;
//				}
//			}
//		} catch (EOFException e) {
//			fimArquivo = true;
//		}
//
//		return true;
//	}

	public void inserir(long posArquivo, int chave) throws IOException {

		RandomAccessFile arqIndice = new RandomAccessFile(new File(ARQ_INDICES), "rw");
		Indice aux = null;
		
//		if (cheia()) {
//			System.out.println("Tabela cheia!");
//			return;
//		}
		long pos = funcaoHash(chave);
		arqIndice.seek(pos * TAM_INDICE);
		aux = Indice.readFromFile(arqIndice);

		if (aux.getChave() > 0) {
			if (chave == aux.getChave()) {

				System.out.println("Esse RG já esta na tabela");
				arqIndice.close();
				return;
			}
		
			while (pos < tamanhoTabela) {
				if (pos == tamanhoTabela - 1)
					pos = -1;
				colisoes++;
				pos++;
				arqIndice.seek(pos * TAM_INDICE);
				aux = Indice.readFromFile(arqIndice);
				if (aux.getChave() <= 0)
					break;
			}
		}
		
		aux = new Indice(posArquivo, chave);
		aux.saveToFile(arqIndice, pos * TAM_INDICE);
		System.out.println("PosHash: " + pos + " | PosArq: " + pos * TAM_INDICE);
		arqIndice.close();
	}

	public long busca(int chave) throws IOException {

		RandomAccessFile arqIndice = new RandomAccessFile(new File(ARQ_INDICES), "rw");
		Indice aux = null;
		try {
			long pos = funcaoHash(chave);
			arqIndice.seek(pos * TAM_INDICE);
			aux = Indice.readFromFile(arqIndice);
			
			if (aux.getChave() > 0) {
				if (aux.getChave() == chave) {
					arqIndice.close();
					return aux.getPos();
				}
					
				long posInicial = pos;
				while (pos < tamanhoTabela) {
					if (pos == tamanhoTabela - 1)
						pos = -1;

					pos++;
					arqIndice.seek(pos * TAM_INDICE);
					aux = Indice.readFromFile(arqIndice);
					if (aux.getChave() == chave) {
						arqIndice.close();
						return aux.getPos();
					}
						

					if (pos == posInicial)
						break;
				}
			}
		}
		catch (NullPointerException e) {
			System.out.println("ERRO: " + e.getMessage());
		}
		
		arqIndice.close();
		return -1;
	}
}
