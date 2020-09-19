package app;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Scanner;

import classes.Pessoa;

public class Conversor {
	
	private static int LINHA_ATUAL = 0;
	
	private static Pessoa criarPessoa(String dados[]) {
		Pessoa novo = null;
		try {
			
			int codigo = Integer.parseInt(dados[0]);
			String nome = dados[1];
			String nasc = dados[2];
				
			novo = new Pessoa(codigo, nome, nasc);
			
			
		} catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
			System.out.println("Erro no cadastro da linha " + LINHA_ATUAL + " do arquivo de entrada");
		}
		return novo;
	}

	public static void converterArquivo(String arqTexto, String arqBin) throws IOException {

		File arqEntrada = new File(arqTexto);
		Scanner leitor = new Scanner(arqEntrada);
		
		int contador = 0;
		try {
			File arqSaida = new File(arqBin);
			RandomAccessFile dadosSaida = new RandomAccessFile(arqSaida, "rw");
			dadosSaida.setLength(0);
			dadosSaida.writeInt(0);

			while (leitor.hasNextLine()) {
				LINHA_ATUAL++;
				String linha = leitor.nextLine();
				String dados[] = linha.split(";");
				Pessoa novo = criarPessoa(dados);
				if (novo != null) {
					novo.saveToFile(dadosSaida);
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
	
}
