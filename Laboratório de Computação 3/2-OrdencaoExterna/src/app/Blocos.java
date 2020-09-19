package app;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.Scanner;

import classes.Pessoa;

public class Blocos {
    
    private static final String NOME_ARQUIVO_TMP = "temp.tmp";
    private static int QUANTIDADE_OBJETOS_LIDOS;
    private static int QUANTIDADE_TROCA_ARQUIVOS;
    private static int QUANTIDADE_PASSADAS;
    
    private static void gravarBloco(Pessoa[] bloco, RandomAccessFile arq) throws IOException{
        Arrays.sort(bloco);             
                                                
        for(Pessoa p : bloco){
           p.saveToFile(arq);
        }

        Pessoa.separador().saveToFile(arq);
    }

    private static int  gerarBlocosFixos(int qntdArquivos, int tamanhoBloco) throws IOException {
    	
    	QUANTIDADE_OBJETOS_LIDOS = 0;
    	
    	int contaBlocos = 0;
    	Pessoa blocoPessoas[] = new Pessoa[tamanhoBloco];
        int posAtual = 0;
        int arqAtual = 0;

        RandomAccessFile arqDados = new RandomAccessFile(new File("pessoas.bin"), "r");
        RandomAccessFile[] arqTemp = new RandomAccessFile[qntdArquivos];
        
        for(int i =0; i <qntdArquivos; ++i){
        	
            arqTemp[i] = new RandomAccessFile(new File(i+NOME_ARQUIVO_TMP), "rw");
            arqTemp[i].setLength(0);
        }

        int qtdPessoas = arqDados.readInt();

        for(int i=0; i< qtdPessoas; i++){
        	
        	Pessoa aux = Pessoa.readFromFile(arqDados);
            blocoPessoas[posAtual] = aux;
            posAtual++;
            
            if(posAtual == tamanhoBloco){
            	
            	gravarBloco(blocoPessoas, arqTemp[arqAtual]);
            	contaBlocos++;
            	posAtual=0;
                Arrays.fill(blocoPessoas, Pessoa.separador());
                    
                arqAtual++; 
                arqAtual = (arqAtual % qntdArquivos);
            }
            
            QUANTIDADE_OBJETOS_LIDOS++;
            
        }
        
        gravarBloco(blocoPessoas, arqTemp[arqAtual]);
        arqDados.close();
        for(int i =0; i <qntdArquivos; ++i){
            arqTemp[i].close();
        }
        
        return contaBlocos;
    }
    
  //controlar fonte e saida de dados, sempre a entrada é a primeira metade do array
    private static RandomAccessFile[] abrirArquivosTemp(RandomAccessFile[] arqTemp, int passo, int qntdArquivos) throws IOException { 

    	if (passo != 0) {
    		for(int i = 0; i < (qntdArquivos * 2); ++i){
                arqTemp[i].close();
            }
    	}
    	
    	arqTemp = new RandomAccessFile[qntdArquivos * 2];
    	
    	int indiceLeitura = 0;
    	int indiceEscrita = 0;
    	
		if (passo % 2 == 0) {
			indiceLeitura = 0;
			indiceEscrita = qntdArquivos;
		}
		else {
    		indiceLeitura = qntdArquivos;
    		indiceEscrita = 0;
		}
		
		for(int i = indiceLeitura; i < (indiceLeitura + qntdArquivos); ++i){
            arqTemp[i] = new RandomAccessFile(new File(i+NOME_ARQUIVO_TMP), "r");
            arqTemp[i].seek(0);
        }
    	
		for(int i = indiceEscrita; i < (indiceEscrita + qntdArquivos); ++i){
            arqTemp[i] = new RandomAccessFile(new File(i+NOME_ARQUIVO_TMP), "rw");
            arqTemp[i].setLength(0);
        }
    	
    	return arqTemp;
    }
    
    private static void intercalarBlocosFixos(int qntdArquivos) throws IOException {
    	QUANTIDADE_PASSADAS = 0;
    	
    	RandomAccessFile[] arqTemp = null;
    	Scanner leitor = new Scanner(System.in);
    	Pessoa pessoas[] = new Pessoa[qntdArquivos]; 
    	int arqSaidaAtual = qntdArquivos;
    	int passo = 0;
    	int tamanhoBloco = 0;
    	
    	//Inicializar arquivos
    	arqTemp = abrirArquivosTemp(arqTemp, passo, qntdArquivos);
    	
    	//Eqto bloco gerado < arquivo original
    	while (tamanhoBloco < QUANTIDADE_OBJETOS_LIDOS) {
    		QUANTIDADE_PASSADAS++;
    		
    		//Eh o final de todos os arquivos?
    		int controlaPasso = 0;
    		for (int i = 0; i < qntdArquivos * 2; i++) {
    			
    			if (arqTemp[i].getFilePointer() == arqTemp[i].length()) {
    				controlaPasso++;
    			}
    		
    		}
    		
    		//Se sim definir novas fontes e saidas de dados
    		if (controlaPasso == qntdArquivos * 2) {
    			passo++;
    			
    			//Resetar posicao do arquivo de saida
    			if (passo % 2 != 0)
    				arqSaidaAtual = 0;
    			else
    				arqSaidaAtual = qntdArquivos;
    			
        		arqTemp = abrirArquivosTemp(arqTemp, passo, qntdArquivos);
    		}
    		
    		//Zerar tamanho do bloco atual
    		tamanhoBloco = 0;
    		
    		//Verifica em qual o passo atual
    		int controlaLeitura = 0;
    		int controlaEscrita = qntdArquivos;
    		
    		if(passo % 2 != 0) {
    			controlaLeitura = qntdArquivos;
    			controlaEscrita = 0;
    		}
        	
    		//Ler um dado de cada fonte verificando se arquivo nao esta no fim
    		for (int i = 0; i < pessoas.length; i++) {
    			
    			if (arqTemp[i + controlaLeitura].getFilePointer() < arqTemp[i + controlaLeitura].length()) {
    				pessoas[i] = Pessoa.readFromFile(arqTemp[i + controlaLeitura]);
    			}
    			
        	}
    		
    		//Enquanto dos dados lidos for diferente de separador
        	while (true) {
        		boolean quebraLoop = true; //Controla loop
        		
        		Pessoa menor = pessoas[0];
        		int origemMenor = 0;
        		
        		//Decidir "menor" dos dados lidos
            	for (int i = 0; i < pessoas.length; i++) {
            		
            		if (pessoas[i].compareTo(Pessoa.separador()) != 0) {
            			quebraLoop = false; //Lido um dado nao separador, o loop vai continuar
            			
            			if (menor.compareTo(pessoas[i]) > 0 || menor.compareTo(Pessoa.separador()) == 0) {
            				menor = pessoas[i];
                			origemMenor = i; //guarda de onde vem o menor
                		}
            		
            		}
            		
            	}
            	
            	//Finaliza o laco se todos os dados sao separadores
            	if (quebraLoop) 
            		break;
            	
            	//Se não for um separador, escrever esse menor no arquivo de saida atual
            	if (menor.compareTo(Pessoa.separador()) != 0) {

            		menor.saveToFile(arqTemp[arqSaidaAtual]);
                	tamanhoBloco++;
                	
                	//Se não for o final do arquivo, ler o proximo dado da fonte do menor arquivo
                	if (arqTemp[origemMenor + controlaLeitura].getFilePointer() < arqTemp[origemMenor + controlaLeitura].length())
                        pessoas[origemMenor] = Pessoa.readFromFile(arqTemp[origemMenor + controlaLeitura]);
            	}
            	
        	}

        	if (tamanhoBloco != 0) {
        		//Colocar separador no arquivo de saida atual
        		Pessoa.separador().saveToFile(arqTemp[arqSaidaAtual]);
        		
        		//Mudar arquivo de saida atual
            	arqSaidaAtual = ((arqSaidaAtual + 1) % qntdArquivos) + controlaEscrita;
        	}
        	
    	}
    	
    	QUANTIDADE_TROCA_ARQUIVOS = passo;
    }
    
    
    public static void ordenarPorBlocosFixos(int qntdArquivos, int tamanhoBloco) throws IOException {
    	long tempo = System.currentTimeMillis();
    	
    	System.out.println("==============================================");
    	System.out.println("Gerando primeiros blocos...");
    	
    	int qntdBlocosGerados = gerarBlocosFixos(qntdArquivos, tamanhoBloco);

    	System.out.println();
    	System.out.println("Tempo gasto na primeira parte " + (System.currentTimeMillis() - tempo)/1000 + " segundos.");
    	System.out.println("Foram gerados " + qntdBlocosGerados + " na primeira fase.");
    	
    	tempo = System.currentTimeMillis();
    	
    	System.out.println();
    	System.out.println("Ordenando blocos com intercalacao...");
    	intercalarBlocosFixos(qntdArquivos);
    	
    	System.out.println();
    	System.out.println("Tempo gasto na segunda parte " + (System.currentTimeMillis() - tempo)/1000 + " segundos.");
    	System.out.println("O metodo executou o laco principal " + QUANTIDADE_PASSADAS + " vezes.");
    	System.out.println("A fonte e a saida de arquivos foram redefinidas: " + QUANTIDADE_TROCA_ARQUIVOS + " vezes.");
    	
    }


    
}