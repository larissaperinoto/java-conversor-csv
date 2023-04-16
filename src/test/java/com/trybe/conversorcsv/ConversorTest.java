package com.trybe.conversorcsv;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Comparator;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@SuppressWarnings({"ConstantConditions", "ResultOfMethodCallIgnored"})
public class ConversorTest {

  private static final String CABECALHO = "Nome completo,Data de nascimento,Email,CPF";

  private static final String[] ENTRADA_SP = new String[] {
      "araci ribas,31/03/1994,araci.ribas94@gmail.com,06041586455",
      "ubiratã vilas-boas,20/01/1988,u.vilas-boas@yahoo.com,58968367167",
      "ROSA GOMES,04/04/1991,gomes@gmail.com,91288026404",
      "Jacinto Lustosa,11/02/1984,lustosa@msn.com,47093691510",
      "Antônio Gomes,11/02/2003,antoniogomes@wanadoo.fr,19545731389"
  };

  private static final String[] SAIDA_SP = new String[] {
      "ARACI RIBAS,1994-03-31,araci.ribas94@gmail.com,060.415.864-55",
      "UBIRATÃ VILAS-BOAS,1988-01-20,u.vilas-boas@yahoo.com,589.683.671-67",
      "ROSA GOMES,1991-04-04,gomes@gmail.com,912.880.264-04",
      "JACINTO LUSTOSA,1984-02-11,lustosa@msn.com,470.936.915-10",
      "ANTÔNIO GOMES,2003-02-11,antoniogomes@wanadoo.fr,195.457.313-89"
  };

  private static final String[] ENTRADA_RJ = new String[] {
      "afonso alancastro,20/10/2003,aalancastro@gmail.com,17856330406",
      "IBERÊ AZEREDO,15/03/2001,ibere.azeredo@hotmail.com,07618920443"
  };

  private static final String[] SAIDA_RJ = new String[] {
      "AFONSO ALANCASTRO,2003-10-20,aalancastro@gmail.com,178.563.304-06",
      "IBERÊ AZEREDO,2001-03-15,ibere.azeredo@hotmail.com,076.189.204-43"
  };

  private static final File pastaDeTeste = new File("./testeconversor/");
  private static final File pastaDeEntradas = new File(pastaDeTeste, "entradas/");
  private static final File pastaDeSaidas = new File(pastaDeTeste, "saidas/");

  @BeforeAll
  public static void antes() throws IOException {
    pastaDeEntradas.mkdirs();
    criarArquivoCsv(pastaDeEntradas, "sp.csv", ENTRADA_SP);
    criarArquivoCsv(pastaDeEntradas, "rj.csv", ENTRADA_RJ);

    Conversor conversor = new Conversor();
    conversor.converterPasta(pastaDeEntradas, pastaDeSaidas);
  }

  @AfterAll
  public static void depois() throws IOException {
    Files.walk(pastaDeTeste.toPath())
        .map(Path::toFile)
        .sorted(Comparator.reverseOrder())
        .forEachOrdered(File::delete);
  }

  @Test
  @DisplayName("Pasta `saidas` existe e contém os mesmos arquivos que a pasta `entradas`")
  public void pastasDeEntradaSaidaPossuemOsMesmosArquivos() {
    assertTrue(pastaDeSaidas.exists());
    assertArrayEquals(
        nomesDosArquivosEm(pastaDeEntradas),
        nomesDosArquivosEm(pastaDeSaidas)
    );
  }

  @Test
  @DisplayName("Arquivos de saída possuem cabeçalho")
  public void arquivosDeSaidaPossuemCabecalho() throws IOException {
    for (File arquivo : pastaDeSaidas.listFiles()) {
      String primeiraLinha = lerPrimeiraLinha(arquivo);
      assertEquals(CABECALHO, primeiraLinha);
    }
  }

  @Test
  @DisplayName(
      "Arquivos de saída contêm o conteúdo esperado na mesma ordem dos arquivos de entrada"
  )
  public void arquivosDeSaidaContemConteudoEsperado() throws IOException {
    String[] conteudoSp = carregarConteudo(pastaDeSaidas, "sp.csv");
    String[] conteudoRj = carregarConteudo(pastaDeSaidas, "rj.csv");

    assertArrayEquals(SAIDA_SP, conteudoSp);
    assertArrayEquals(SAIDA_RJ, conteudoRj);
  }

  private static void criarArquivoCsv(File pasta, String nome, String[] linhas) throws IOException {
    File arquivo = new File(pasta, nome);
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(arquivo))) {
      writer.write(CABECALHO);
      for (String linha : linhas) {
        writer.newLine();
        writer.write(linha);
      }
    }
  }

  private static String[] nomesDosArquivosEm(File pasta) {
    return Arrays.stream(pasta.listFiles())
        .map(File::getName)
        .toArray(String[]::new);
  }

  private static String lerPrimeiraLinha(File arquivo) throws IOException {
    try (BufferedReader reader = new BufferedReader(new FileReader(arquivo))) {
      return reader.readLine();
    }
  }

  private static String[] carregarConteudo(File pasta, String nome) throws IOException {
    File arquivo = new File(pasta, nome);
    try (BufferedReader reader = new BufferedReader(new FileReader(arquivo))) {
      reader.readLine(); // Descarta o cabeçalho.
      return reader.lines().toArray(String[]::new);
    }
  }
}
