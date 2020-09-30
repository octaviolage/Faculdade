package app;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.List;
import java.util.Scanner;

import classes.HashLinear;
import classes.Indice;
import classes.Pessoa;

public class ArquivoBin {

	static int contaLinha = 0;

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
	
	public static void converterArquivo(String arqTexto, HashLinear tabelaHash) throws IOException {

		File arqEntrada = new File(arqTexto + ".txt");
		Scanner leitor = new Scanner(arqEntrada);
		
		int contador = 0;
		try {
			File arqSaida = new File(arqTexto + ".bin");
			RandomAccessFile dadosSaida = new RandomAccessFile(arqSaida, "rw");
			dadosSaida.setLength(0);
			dadosSaida.writeInt(0);

			while (leitor.hasNextLine()) {
				contaLinha++;
				String linha = leitor.nextLine();
				String dados[] = linha.split(";");
				Pessoa nova = criarPessoa(dados);
				long pos = dadosSaida.getFilePointer();
				if (nova != null) {
					tabelaHash.inserir(pos, nova.getRg());
					nova.saveToFile(dadosSaida);
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
	
	public static Pessoa buscarPessoaIndice(String nomeArq, long pos) throws IOException {
		RandomAccessFile dadosLeitura = new RandomAccessFile(new File(nomeArq), "rw");
		Pessoa aux = null;
		if (pos >= 0) {
			try {
				dadosLeitura.seek(pos);			
				aux = Pessoa.readFromFile(dadosLeitura);

			} catch (NullPointerException | IOException e) {
				System.out.println("Erro na leitura: " + e.getMessage());
			}
		}
		dadosLeitura.close();
		
		return aux;
	}
	
}
