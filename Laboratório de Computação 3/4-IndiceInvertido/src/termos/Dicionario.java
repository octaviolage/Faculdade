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
	
	//constante com o nome do arquivo de stopword, coloquei por organizacao
	private static final String ARQ_STOPWORDS = "stopwords.txt";
	
	//Lista com os termos
	private List<Termo> termos;
	
	//Construtor para criar um dicionario, o parametro e o caminho onde ta os arquivos txt
	public Dicionario(String path) throws FileNotFoundException {
		
		termos = new ArrayList<>();
		preencherDicionario(path);
		
	}
	
	//Construtor para ler um arquivo de dicionario exitente, passando o arquivo de dicionario como parametro
	public Dicionario(File arquivo) throws IOException, ClassNotFoundException {
		
		//Abre o arquivo binario do dicionario
		ObjectInputStream dicionario = new ObjectInputStream(new FileInputStream(arquivo));
		
		//loop para ler todos os objetos no arquivo
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
	
	//Verifica se a palavra passado no parametro é stopword
	private boolean stopWord(String palavra) throws FileNotFoundException {
		File stopWords = new File(ARQ_STOPWORDS);
		//Abre o arquivo de stopwords como UTF-8
		Scanner arquivo = new Scanner(stopWords, "UTF-8");
		//Loop para ler cada liha do arquivo
		while(arquivo.hasNextLine()) {
			String stopWord = arquivo.nextLine().replace(" ", "");
			//compara a palavra do parametro com a stopword atual
			if(palavra.compareToIgnoreCase(stopWord) == 0) {
				arquivo.close();
				return true;
			}
				
		}

		arquivo.close();
		return false;
	}
	
	//Busca um termo na lista de termos da memoria
	private Termo buscar(String palavra) {
		for (Termo termo : termos)
			if(termo.getPalavra().compareToIgnoreCase(palavra) == 0)
				return termo;
		
		return null;
	}
	
	//Pesquisa em união de arquivos
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
		//Se o documento nao estiver vazio vai mudar a resposta
		if(!documentos.isEmpty()) {
			resposta = "Ao menos uma dessas palavras podem ser econtradas\nnos documentos:";
			documentos.sort(null);
			for(int i = 0; i < documentos.size(); i++)
				resposta += " " + documentos.get(i) + " |";
		}

		return resposta;
	}
	
	//Pesquisa em interseção
	public String pesquisaInter(String linha) throws FileNotFoundException {
		//quebra a frase em um array de palavras
		String[] palavras = linha.split(" ");
		//defino a resposta de erro
		String resposta = "A sentenca nao foi encontrada nos documentos!";
		//lista para os termos que forem encontrados
		List<Termo> termos = new ArrayList<>();
		//lista com o numero dos documentos que os termos aparecem
		List<Integer> documentos = new ArrayList<>();
		
		Termo aux = null;
		int contador = 0;

		//loop para pegar os termos pelas palavras pesquisadas
		for(int i = 0; i < palavras.length; i++) {
			//Verifica se é stopword
			if(!stopWord(palavras[i])) {
				//busca a palavra
				aux = buscar(palavras[i]);
				contador++;
				//Se nao achar a palavra da a resposta la de cima
				if(aux == null)
					return resposta;
				
				termos.add(aux);
			}
			
		}
		//se tiver pelo menos uma palavra encontrada
		if(aux != null) {
			//Pra cada termo
			for(Termo termo : termos) {
				//adiciona as ocorrencias de cada termo na lista
				for(int i = 0; i < termo.getOcorrencias().size(); i++) {
						documentos.add(termo.getOcorrencias().get(i));
				}
			}
			//Ordena a lista de documentos
			documentos.sort(null);
			//Se lista nao for vazia
			if(!documentos.isEmpty()) {
				//muda a resposta para listar os arquivos
				resposta = "Os documentos que contem a sentenca:";
				//loop percorrendo lista de documentos
				for(int i = 0; i < documentos.size()-contador+1; i++)
					//compara a posicao com a quantidade de termos validos -1   Ex:["1"1111"1"2222555566]
					if(documentos.get(i).equals(documentos.get(i+contador-1)))
						resposta += " " + documentos.get(i) + " |";
			}
		}

		return resposta;
	}
	
	//Metodo para criar dicionario a partir de documentos txt
	private void preencherDicionario(String path) throws FileNotFoundException {
		//Cria array de arquivos
		File[] arquivos;
		//Cria uma especie de lista de arquivos
		File diretorio = new File(path);
		//A lista vira um item no array para cada item
		arquivos = diretorio.listFiles();

		//atributo para quebrar frases
		StringTokenizer parse = null;
		
		//Para cada arquivo
		for(int i = 0; i < arquivos.length; i++){
			//abre arquivo com UTF-8 para aceitar acentuação portuguesa
			Scanner arquivo = new Scanner(arquivos[i], "UTF-8");
			//Enquanto tiver linha no arquivo aberto
			while(arquivo.hasNextLine()) {
				//pega linha
				String linha = arquivo.nextLine();
				//quebra liga pra cada item
				parse = new StringTokenizer(linha, " .,:!?()\"-–—[]|/*'=@_;&{}+“");
				//Enquanto tiver palavras
				while(parse.hasMoreTokens()) {
					String palavra = parse.nextToken();
					//Verifica se e stopword
					if(!stopWord(palavra)) {
						//Busca a palvra no dicionario
						Termo aux = buscar(palavra);
						//Se a palavra nao for encontrada, cria ela no dicionario
						if(aux == null) {
							aux = new Termo(palavra.toUpperCase());
							termos.add(aux);
						}
						//adiciona a ocorrencia
						aux.addOcorrencia(i+1);
					}
				}
			}
			arquivo.close();
		}
	}
	
	//Salvar cada termo no arquivo
	public void saveToFile(String nome) throws IOException {
		ObjectOutputStream saida = new ObjectOutputStream(new FileOutputStream(nome));
		for(Termo termo : termos)
			termo.saveToFile(saida);
		
		saida.close();
	}

}
