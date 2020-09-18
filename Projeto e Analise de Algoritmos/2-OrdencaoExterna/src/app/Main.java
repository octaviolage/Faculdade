package app;

import java.io.IOException;

public class Main {

	public static void main(String[] args) throws IOException {
		long tmp = System.currentTimeMillis();
		System.out.println("Convertendo arquivo de pessoas...");
		//Conversor.converterArquivo("pessoas.txt", "pessoas.bin");
		
		Blocos.ordenarPorBlocosFixos(6, 10000);
		
		System.out.println("\nFeito!");

	}

}
