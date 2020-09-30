package app;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

import classes.HashLinear;
import classes.Pessoa;

public class App {
	
	public static void main(String[] args) throws IOException {
		
//		System.out.println("Criando indice hash com dados nulos");
//		HashLinear tabelaHash = new HashLinear(700001, true); //Num primo maior para diminuir o numero de colisoes
//		
//		System.out.println("Convertendo arquivo texto em binario...");
//		ArquivoBin.converterArquivo("pessoas", tabelaHash);
//		
//		System.out.println("Quantidade de colisoes: " + tabelaHash.colisoes);
		
		//O boolean é para indicar se o arquivo deve ser zerado ou usar um pronto
		HashLinear tabelaHash = new HashLinear(700001, false);
		
		Scanner teclado = new Scanner(System.in);
		Pessoa aux = null;
		int rg = 0;
		while(true) {
			System.out.println("=====================================================");
			System.out.println("\nDigite o RG de quem quer buscar ou 0 para sair: ");
			
			rg = teclado.nextInt();
			if (rg == 0)
				break;
			
			long pos = tabelaHash.busca(rg);
			System.out.println("Na posicao: " + pos);
			aux = ArquivoBin.buscarPessoaIndice("pessoas.bin", pos);
			if (aux != null)
				System.out.println("Pessoa: " + aux.toString());
			else
				System.out.println("Nao encontrado!");
			System.out.println();
			
		}
		
	}

}
