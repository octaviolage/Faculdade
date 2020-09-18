package app;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

import classes.Pessoa;

public class App {
	
	public static void menu(String nomeArq) throws ClassNotFoundException, IOException, InterruptedException {
		
		Scanner teclado = new Scanner(System.in);
		Pessoa aux = null;
		int rg = 0;
		long duracao = 0;
		
		System.out.println("====================MENU===================\n");
		System.out.println("1- Buscar pessoa utilizando indice");
		System.out.println("2- Buscar pessoa sem utilizar indice");
		System.out.println("0- Sair da aplicacao");
		System.out.print("Opcao: ");
		try {
			int opcao = teclado.nextInt();
			switch (opcao) {
			case 1:
				System.out.print("Digite o RG da pessoa que voce esta procurando: ");
				rg = teclado.nextInt();
				duracao = System.currentTimeMillis();
				aux = Funcionalidades.buscarPessoaIndice(nomeArq, rg);
				if (aux != null) {
					System.out.println(aux + "\n");
				}
				else {
					System.out.println("Pessoa nao encontrada");
				}
				System.out.println("Essa operacao durou " + (System.currentTimeMillis() - duracao) + "ms!\n\n");
				Thread.sleep(3000);
				menu(nomeArq);
				break;
				
			case 2:
				System.out.print("Digite o RG da pessoa que voce esta procurando: ");
				rg = teclado.nextInt();
				duracao = System.currentTimeMillis();
				aux = Funcionalidades.buscarPessoa(nomeArq, rg);
				if (aux != null) {
					System.out.println(aux + "\n");
				}
				else {
					System.out.println("Pessoa nao encontrada");
				}
				System.out.println("Essa operacao durou " + (System.currentTimeMillis() - duracao) + "ms!\n\n");
				Thread.sleep(3000);
				menu(nomeArq);
				break;
				
			case 0:
				System.exit(1);
				
			default:
				System.out.println("\nOpcao invalida! Voltando ao menu...\n");
				Thread.sleep(3000);
				menu(nomeArq);
			}
		} catch (InputMismatchException e) {
			System.out.println("\nO valor digitado nao e uma opcao valida!\n");
			Thread.sleep(3000);
			menu(nomeArq);
		}
	}

	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
		
		System.out.println("Convertendo arquivo texto em binario...");
		Funcionalidades.converterArquivo("pessoas.txt", "pessoas.bin");
		System.out.println("Carregando arquivo de indices...");
		Funcionalidades.carregaIndice("indices.bin");
		
		
		menu("pessoas.bin");
		
	}

}
