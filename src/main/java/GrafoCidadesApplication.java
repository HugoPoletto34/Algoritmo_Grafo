import java.time.LocalDate;
import java.util.Scanner;

import models.Cidade;
import models.GrafoCidades;
import models.Vertice;
import utilLib.ArquivoTextoEscrita;

public class GrafoCidadesApplication {
	public static void main(String[] args) {
		Scanner leitor = new Scanner(System.in);
		GrafoCidades grafoCidades = new GrafoCidades();
		System.out.println("Gerando Grafo...");
		grafoCidades.gerarGrafoComCidades();
		grafoCidades.criarLigacoesTresCidadesProximas();
		System.out.println("Grafo gerado com sucesso!");

		ArquivoTextoEscrita arq = new ArquivoTextoEscrita();
		System.out.println("Gerando Relatório...");
		arq.abrirArquivo("./Relatórios/Grafo_Cidades_" + LocalDate.now() + ".md");
		arq.escrever("# Relatório de Grafo de Cidades - v1");
		arq.escrever(grafoCidades.toString());
		arq.fecharArquivo();

		System.out.println("Relatório gerado com sucesso!");
		
		System.out.println("\nBusca em largura:\n");
		
		grafoCidades.buscaEmLargura();

		System.out.println("\nDescubra se há ciclo a partir de uma cidade tendo outra cidade como intermediário.");
		Vertice<Cidade> verticeInicial = null;
		Vertice<Cidade> verticeIntermediario = null;
		boolean sair = false;
		while (!sair) {
		while (verticeInicial == null) {
				System.out.println("Insira o nome da cidade de partida: ");
				String nomeVerticeInicial = leitor.nextLine();
				verticeInicial = grafoCidades.getCidadePeloNome(nomeVerticeInicial);
				if (nomeVerticeInicial == null)
					System.out.println("Nome da cidade inválida ou não está presente no grafo.");
			}
			while (verticeIntermediario == null) {
				System.out.println("Insira a cidade que deverá ser intermediária");
				String nomeVerticeIntermediario = leitor.nextLine();
				verticeIntermediario = grafoCidades.getCidadePeloNome(nomeVerticeIntermediario);
				if (verticeIntermediario == null)
					System.out.println("Nome da cidade inválida ou não está presente no grafo.");
			}

			System.out.println("Buscando ciclo...");
			boolean resposta = grafoCidades.temCicloComIntermediario(verticeInicial, verticeIntermediario);
			System.out.println("Finalizado!");
			System.out.println("Há ciclo possível iniciando em " + verticeInicial.getItem().getNome() + " e com intermediário " + verticeIntermediario.getItem().getNome() + "?");
			if (resposta)
				System.out.println("Sim!");
			else
				System.out.println("Não!");

			System.out.println("Deseja fazer outra busca? (escreva 'sim' ou aperte o botão enter para sair)");
			sair = leitor.nextLine().equals("");
			if (!sair) {
				verticeInicial = null;
				verticeIntermediario = null;
			}
		}
	}
	
	
}
