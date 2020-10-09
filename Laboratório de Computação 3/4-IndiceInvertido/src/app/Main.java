package app;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import termos.Dicionario;

public class Main {
	
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		
		Dicionario dicionario = new Dicionario("TXT/");
		dicionario.saveToFile("dicionario.dat");
		
		File arquivo = new File("dicionario.dat");
		dicionario = new Dicionario(arquivo);

		String pesquisa = "";
		Scanner teclado = new Scanner(System.in);
		String opcao = "";
		while(true) {
			System.out.println("=====================================================");
			System.out.println("1- Pesquisar por termos independentemente (uniao)");
			System.out.println("2- Pesquisar por conjunto de termos (intersecao) ");
			System.out.println("9- Para sair: ");
			System.out.println("Escolha uma opcao: ");
			
			opcao = teclado.next();
			switch(opcao) {
			
			case "1":
				System.out.println("Digite as palavras separadas por espaco: ");
				teclado.nextLine();
				pesquisa = teclado.nextLine();
				pesquisa = dicionario.pesquisaUniao(pesquisa);
				System.out.println(pesquisa);
				break;
			
			
			case "2":
				System.out.println("Digite a frase: ");
				teclado.nextLine();
				pesquisa = teclado.nextLine();
				pesquisa = dicionario.pesquisaInter(pesquisa);
				System.out.println(pesquisa);
				break;
				
			case "9":
				System.out.println("Volte sempre!");
				System.exit(1);
				break;
				
			default:
				System.out.println("Opcao invalida!!!");
				break;
			}
		}
		
	}

}
