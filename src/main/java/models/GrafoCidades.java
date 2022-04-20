package models;

import utilLib.ArquivoTextoLeitura;

import java.util.*;
import java.util.stream.Collectors;

public class GrafoCidades extends Grafo<Cidade> {
    public final static String NOME_ARQUIVO = "./BaseDados/br.csv";
    private final List<Node> listaKmCidades = new LinkedList<>();


    public GrafoCidades() {
        super();
    }

    // Método que lerá o arquivo de dados e criará todas as cidades.
    public void gerarGrafoComCidades() {
        ArquivoTextoLeitura arquivoEntrada = new ArquivoTextoLeitura(NOME_ARQUIVO);
        String leituraArquivo = arquivoEntrada.lerBuffer();
        int id = 1;
        leituraArquivo = arquivoEntrada.lerBuffer();
        while(leituraArquivo!=null) {

            String[] camposCidade = leituraArquivo.split(",");

            Cidade cidade = new Cidade(
                    camposCidade[0],
                    Double.parseDouble(camposCidade[1]),
                    Double.parseDouble(camposCidade[2])
            );
            Vertice<Cidade> vertice = new Vertice<>(id);
            vertice.setItem(cidade);

            this.vertices.add(vertice);
            id++;
            leituraArquivo = arquivoEntrada.lerBuffer();
        }
    }

    public void criarLigacoesTresCidadesProximas() {
        for (int j = 0; j < this.vertices.size(); j++) {
            Vertice<Cidade> vertice = this.vertices.get(j);
            for (Vertice<Cidade> prox : this.vertices) {
                String comp = "origem: " + prox + " - destino: " + vertice;
                if (!vertice.equals(prox) && !this.listaKmCidades.toString().contains(comp)) {
                    Node node = new Node(vertice, calcularDistanciaCidades(vertice.getItem(), prox.getItem()), prox);
                    this.listaKmCidades.add(node);
                }

            }
        }
        this.listaKmCidades.sort((e1, e2) -> e1.valor.compareTo(e2.valor));

        for (Vertice<Cidade> vertice : this.vertices) {
            List<Node> listFiltered = get3proximas(vertice.getId());

            vertice.addAresta(listFiltered.get(0).valor,
                listFiltered.get(0).origem.equals(vertice)
                ? listFiltered.get(0).destino
                : listFiltered.get(0).origem);
            vertice.addAresta(listFiltered.get(1).valor,
                    listFiltered.get(1).origem.equals(vertice)
                            ? listFiltered.get(1).destino
                            : listFiltered.get(1).origem);
            vertice.addAresta(listFiltered.get(2).valor,
                    listFiltered.get(2).origem.equals(vertice)
                            ? listFiltered.get(2).destino
                            : listFiltered.get(2).origem);

        }
    }

    private List<Node> get3proximas(int idVertice) {
        List<Node> listFiltered = this.listaKmCidades.stream().filter
                ((e) -> e.destino.getId() == idVertice || e.origem.getId() == idVertice)
                .collect(Collectors.toList());
        List<Node> listReturn = new ArrayList<>(3);
        listReturn.add(listFiltered.get(0));
        listReturn.add(listFiltered.get(1));
        listReturn.add(listFiltered.get(2));

        return listReturn;
    }

    // https://stackoverflow.com/questions/27928/calculate-distance-between-two-latitude-longitude-points-haversine-formula
    public static double calcularDistanciaCidades(Cidade v1, Cidade v2) {
        final double RAIO_DA_TERRA = 6371;
        double dLat = grausParaRadianos(v2.getLatitude() - v1.getLatitude());
        double dLng = grausParaRadianos(v2.getLongitude() - v1.getLongitude());

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(grausParaRadianos(v1.getLatitude()))
                * Math.cos(grausParaRadianos(v2.getLatitude())) * Math.sin(dLng / 2) * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return RAIO_DA_TERRA * c;
    }

    private static double grausParaRadianos(double deg) {
        return deg * (Math.PI / 180);
    }

    @Override
    public String toString(){
        StringBuilder aux = new StringBuilder("Cidades:  \n");

        for (Vertice<Cidade> vertex : this.vertices) {
            aux.append(vertex.toString());
        }

        return aux.toString();
    }
}
