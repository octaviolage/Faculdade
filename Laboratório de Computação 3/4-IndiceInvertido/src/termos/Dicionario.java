package termos;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Dicionario {
	
	private static final String ARQ_STOPWORDS = "stopwords.txt";
	
	private List<Termo> termos;
	
	public Dicionario(String path) throws FileNotFoundException {
		
		termos = new ArrayList<>();
		preencherDicionario(path);
		
	}
	
	public Dicionario(File arquivo) throws IOException, ClassNotFoundException {
		ObjectInputStream dicionario = new ObjectInputStream(new FileInputStream(arquivo));
		
		termos = new ArrayList<>();
		while(true) {
			try {
				Termo aux = (Termo) dicionario.readObject();
				termos.add(aux);
			}
			catch(EOFException e) {
				break;
			}
			
		}
		
		
	}
	
	private boolean stopWord(String palavra) throws FileNotFoundException {
		File stopWords = new File(ARQ_STOPWORDS);
		Scanner arquivo = new Scanner(stopWords, "UTF-8");
		
		while(arquivo.hasNextLine()) {
			String stopWord = arquivo.nextLine().replace(" ", "");
			
			if(palavra.compareToIgnoreCase(stopWord) == 0) {
				arquivo.close();
				return true;
			}
				
		}

		arquivo.close();
		return false;
	}
	
	private Termo buscar(String palavra) {
		for (Termo termo : termos)
			if(termo.getPalavra().compareToIgnoreCase(palavra) == 0)
				return termo;
		
		return null;
	}
	
	public String pesquisaUniao(String linha) {
		String[] palavras = linha.split(" ");
		String resposta = "Nenhum documento contém essas palavra(s)";
		List<Integer> documentos = new ArrayList<>();
		for(String palavra : palavras)
			for (Termo termo : termos)
				if(termo.getPalavra().compareToIgnoreCase(palavra) == 0) {
					for(int i : termo.getOcorrencias())
						if(!documentos.contains(i))
							documentos.add(i);
					break;
				}
		
		if(!documentos.isEmpty()) {
			resposta = "Ao menos uma dessas palavras podem ser econtradas\nnos documentos:";
			documentos.sort(null);
			for(int i = 0; i < documentos.size(); i++)
				resposta += " " + documentos.get(i) + " |";
		}

		return resposta;
	}
	
	public String pesquisaInter(String linha) throws FileNotFoundException {
		String[] palavras = linha.split(" ");
		String resposta = "A sentenca nao foi encontrada nos documentos!";
		List<Termo> termos = new ArrayList<>();
		List<Integer> documentos = new ArrayList<>();
		Termo aux = null;
		int contador = 0;

		for(int i = 0; i < palavras.length; i++) {
			if(!stopWord(palavras[i])) {
				aux = buscar(palavras[i]);
				contador++;
				if(aux == null)
					return resposta;
				
				termos.add(aux);
			}
			
		}
		
		if(aux != null) {
			for(Termo termo : termos) {
				for(int i = 0; i < termo.getOcorrencias().size(); i++) {
						documentos.add(termo.getOcorrencias().get(i));
				}
			}
			
			documentos.sort(null);

			if(!documentos.isEmpty()) {
				resposta = "Os documentos que contem a sentenca:";
				for(int i = 0; i < documentos.size()-contador+1; i++)
					if(documentos.get(i).equals(documentos.get(i+contador-1)))
						resposta += " " + documentos.get(i) + " |";
			}
		}

		return resposta;
	}
	
	private void preencherDicionario(String path) throws FileNotFoundException {
		
		File[] arquivos;
		File diretorio = new File(path);
		StringTokenizer parse = null;
		arquivos = diretorio.listFiles();
		
		for(int i = 0; i < arquivos.length; i++){
			Scanner arquivo = new Scanner(arquivos[i], "UTF-8");
			
			while(arquivo.hasNextLine()) {
				String linha = arquivo.nextLine();
				parse = new StringTokenizer(linha, ". , : ! ? ( ) \"  - – — [ ] | / * ' = @ _ ; & { } + “");
				
				while(parse.hasMoreTokens()) {
					String palavra = parse.nextToken().replace(" ", "");

					if(!stopWord(palavra)) {
						Termo aux = buscar(palavra);
						
						if(aux == null) {
							aux = new Termo(palavra.toUpperCase());
							termos.add(aux);
						}
						
						aux.addOcorrencia(i+1);
					}
				}
			}
			arquivo.close();
		}
	}
	
	public void saveToFile(String nome) throws IOException {
		termos.sort(null);
		ObjectOutputStream saida = new ObjectOutputStream(new FileOutputStream(nome));
		for(Termo termo : termos)
			termo.saveToFile(saida);
		
		saida.close();
	}
	
	//Feito para testar as stopwords e tokens
	public void imprimir() {
		for (Termo termo : termos) {
			System.out.println(termo.toString());
		}
	}

}
