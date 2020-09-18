package app;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import classes.Indice;
import classes.Pessoa;

public class Funcionalidades {

	static int contaLinha = 0;
	static List<Indice> indices = null;

	private static Pessoa criarPessoa(String dados[]) {
		Pessoa nova = null;
		try {
			int rg = Integer.parseInt(dados[0]);
			String nome = dados[1];
			String nasc = dados[2];
			
			nova = new Pessoa(rg, nome, nasc);
			
		} catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
			System.out.println("Erro no cadastro da linha " + contaLinha + " do arquivo de entrada");
		}
		return nova;
	}
	
	public static void converterArquivo(String arqTexto, String arqBin) throws IOException {

		File arqEntrada = new File(arqTexto);
		Scanner leitor = new Scanner(arqEntrada);
		
		int contador = 0;
		try {
			File arqSaida = new File(arqBin);
			RandomAccessFile dadosSaida = new RandomAccessFile(arqSaida, "rw");
			ObjectOutputStream indices = new ObjectOutputStream(new FileOutputStream("indices.bin"));
			dadosSaida.setLength(0);
			dadosSaida.writeInt(0);

			while (leitor.hasNextLine()) {
				contaLinha++;
				String linha = leitor.nextLine();
				String dados[] = linha.split(";");
				Pessoa nova = criarPessoa(dados);
				long pos = dadosSaida.getFilePointer();
				if (nova != null) {
					Indice indice = new Indice(pos, nova.getRg());
					nova.saveToFile(dadosSaida);
					indice.saveToFile(indices);
					contador++;
				}
				
			}
			leitor.close();
			
			dadosSaida.seek(0);
			dadosSaida.writeInt(contador);
			dadosSaida.close();

		} catch (FileNotFoundException e) {
			System.out.println("Arquivo nao encontrado!");
		}
	}
	
	private static Indice buscaPosicao(int chave)  {
		Indice aux = null;
		if(!indices.isEmpty()) {
			for(Indice indice : indices) {
				if (indice.getChave() == chave) {
					aux = indice;
					break;
				}
			}
		}
		
		return aux;
	}
	
	public static void carregaIndice(String nome) throws IOException, ClassNotFoundException {
		if (indices == null) {
			indices = new ArrayList<Indice>();
			Indice indice = null;
			ObjectInputStream arqIndices = new ObjectInputStream(new FileInputStream(nome));
			
	        while(true){
	            try{
	                indice = (Indice)arqIndices.readObject();
	                indices.add(indice);
	            }
	            catch(EOFException e){
	                return;
	            }
	        }
		}
	}
	
	public static Pessoa buscarPessoaIndice(String nomeArq, int chave) throws ClassNotFoundException, IOException {
		RandomAccessFile dadosLeitura = new RandomAccessFile(new File(nomeArq), "rw");
		Pessoa aux = null;
		Indice indice = buscaPosicao(chave);
		if (indice != null) {
			try {
				dadosLeitura.seek(indice.getPos());
				int rg = dadosLeitura.readInt();
				String nome = dadosLeitura.readUTF();
				String nasc = dadosLeitura.readUTF();
				
				aux = new Pessoa (rg, nome, nasc);
				
			} catch (IOException e) {
				System.out.println("Erro na leitura: " + e.getMessage());
			}
		}
		dadosLeitura.close();
		
		return aux;
	}
	
	public static Pessoa buscarPessoa(String nomeArq, int chave) throws IOException {
		RandomAccessFile dadosLeitura = new RandomAccessFile(new File(nomeArq), "rw");
		Pessoa aux = null;
		dadosLeitura.seek(Integer.BYTES);
		boolean fimArquivo = false;
		int rg = 0;
		String nome = "";
		String nasc = "";
		try {
			while(!fimArquivo) {
				rg = dadosLeitura.readInt();
				nome = dadosLeitura.readUTF();
				nasc = dadosLeitura.readUTF();
					if (rg == chave) {
					aux = new Pessoa(rg, nome, nasc);
					break;
				}
			}
		} catch (EOFException e) {
			fimArquivo = true;
		}
		return aux;
	}
}
