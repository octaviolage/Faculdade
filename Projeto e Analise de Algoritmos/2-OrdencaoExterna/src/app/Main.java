package app;

import java.io.IOException;

public class Main {

	public static void main(String[] args) throws IOException {
		
		System.out.println("Convertendo arquivo de pessoas...");
		Conversor.converterArquivo("pessoas.txt", "pessoas.bin");
		
		System.out.println("\nFeito!");
		

	}

}
