package com.trybe.conversorcsv;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Conversor {

  /**
   * Função utilizada apenas para validação da solução do desafio.
   *
   * @param args Não utilizado.
   * @throws IOException Caso ocorra algum problema ao ler os arquivos de entrada ou
   *                     gravar os arquivos de saída.
   */
  public static void main(String[] args) throws IOException {
    File pastaDeEntradas = new File("./entradas/");
    File pastaDeSaidas = new File("./saidas/");
    new Conversor().converterPasta(pastaDeEntradas, pastaDeSaidas);
  }

  /**
   * Converte todos os arquivos CSV da pasta de entradas. Os resultados são gerados
   * na pasta de saídas, deixando os arquivos originais inalterados.
   *
   * @param pastaDeEntradas Pasta contendo os arquivos CSV gerados pela página web.
   * @param pastaDeSaidas Pasta em que serão colocados os arquivos gerados no formato
   *                      requerido pelo subsistema.
   *
   * @throws IOException Caso ocorra algum problema ao ler os arquivos de entrada ou
   *                     gravar os arquivos de saída.
   */
  public void converterPasta(File pastaDeEntradas, File pastaDeSaidas) throws IOException {
    if (pastaDeEntradas.isDirectory() && pastaDeEntradas.canRead()) {
      pastaDeSaidas.mkdir();
      for (File f : pastaDeEntradas.listFiles()) {
        FileReader leitorArquivo = new FileReader(f);
        BufferedReader bufferedLeitor = new BufferedReader(leitorArquivo);
        String conteudoLinha = bufferedLeitor.readLine();
        File novoArquivo = new File(pastaDeSaidas.getPath() + File.separator + f.getName());
        novoArquivo.createNewFile();
        
        FileWriter escritorArquivo = new FileWriter(novoArquivo);
        BufferedWriter bufferedEscritor = new BufferedWriter(escritorArquivo);
        
        
        while (conteudoLinha != null) {
          String content = null;
          if (conteudoLinha.contains("Nome")) {
            content = conteudoLinha;
          } else {
            content = formatarConteudo(conteudoLinha);
          }
          bufferedEscritor.write(content + "\n");
          bufferedEscritor.flush(); 
       
          conteudoLinha = bufferedLeitor.readLine();
        }
        
        leitorArquivo.close();
        bufferedLeitor.close();
        escritorArquivo.close();
        bufferedEscritor.close();
      }
    }
  }
  
  /**
   * Realiza a transformação necessária em cada linha do arquivo.
   * @param conteudo 
   * Representa a leitura de uma linha do arquivo.
   * @return 
   * Conteúdo do arquivo transformado.
   */
  public String formatarConteudo(String conteudo) {
    String nome = conteudo.split(",")[0].toUpperCase();
    String[] date = conteudo.split(",")[1].split("/");
    String dateFormater = date[2] + "-" + date[1] + "-" + date[0];
    String email = conteudo.split(",")[2];
    String cpf = conteudo.split(",")[3];
    String cpfFormater = cpf.substring(0,3) + "." 
        + cpf.substring(3,6) + "." + cpf.substring(6,9) + "-" + cpf.substring(9,11);
    return nome + "," + dateFormater + "," + email + "," + cpfFormater;
  }
}