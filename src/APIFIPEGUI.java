//------------------------------------------------------------
//Ivan Mateus de Lima Azevedo
//10525602
//Gabriel de Andrade Dezan
//10525706
//------------------------------------------------------------

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.border.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.net.*;

import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.ui.RefineryUtilities;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class APIFIPEGUI extends JFrame implements ActionListener {
    private JComboBox<String> veiculos;
    private JComboBox<JComboItem> marcas;
    private JComboBox<JComboItem> modelos;
    private JComboBox<JComboItem> ano;
    private JButton pesquisar;
    private JPanel panel;
    private JPanel boxPanel;
    private JPanel veicPanel;
    private JSONArray jsonArray;
    private String qualVeic;
    private String qualMarca;
    private String qualModelo;
    private String qualAno;
    private JComboItem emptyItem;
    private ArrayList<JComboBox> boxes;
    private JMenuItem marcasPorVeic;
    private JMenuItem caminPorMarca;
    private PieChart graficoPizza;
    private BarChart graficoBarra;
    
    /*private JMenuItem carro;
    private JMenuItem caminhao;
    private JMenuItem moto;
    */


    public APIFIPEGUI() {
        super("API FIPE");
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        JMenu opcoes = new JMenu("Gráficos...");
        menuBar.add(opcoes);
        marcasPorVeic = new JMenuItem("Quantidade de marcas por veículo...");
        marcasPorVeic.addActionListener(this);
        opcoes.add(marcasPorVeic);
        caminPorMarca = new JMenuItem("Quantidade de caminhões por marca...");
        caminPorMarca.addActionListener(this);
        opcoes.add(caminPorMarca);

        setLayout(new BorderLayout());
        String[] quaisVeic = new String[]{"", "Carros", "Motos", "Caminhões"};
        boxes = new ArrayList<>();
        veiculos = new JComboBox<String>(quaisVeic);
        veiculos.addActionListener(this);
        marcas = new JComboBox<JComboItem>();
        marcas.addActionListener(this);
        modelos = new JComboBox<JComboItem>();
        modelos.addActionListener(this);
        ano = new JComboBox<JComboItem>();
        pesquisar = new JButton("Pesquisar");
        pesquisar.addActionListener(this);
        emptyItem = new JComboItem(" ", "empty");
        boxPanel = new JPanel(new GridBagLayout());
        veicPanel = new JPanel();
        panel = new JPanel(new GridLayout(4, 2, 20, 10));

        boxes.add(veiculos);
        boxes.add(marcas);
        boxes.add(modelos);
        boxes.add(ano);

        panel.add(new JLabel("Veículos", SwingConstants.CENTER));
        panel.add(new JLabel("Marcas", SwingConstants.CENTER));
        panel.add(boxes.get(0));
        panel.add(boxes.get(1));
        panel.add(new JLabel("Modelos", SwingConstants.CENTER));
        panel.add(new JLabel("Ano", SwingConstants.CENTER));
        panel.add(boxes.get(2));
        panel.add(boxes.get(3));
        panel.setBorder(new EmptyBorder(20, 20, 30, 20));

        GridBagConstraints bagConst = new GridBagConstraints();
        bagConst.weightx = 1.0;
        bagConst.fill = GridBagConstraints.CENTER;

        bagConst.ipadx = 600;
        bagConst.gridx = 0;
        bagConst.gridy = 0;
        boxPanel.add(panel, bagConst);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(pesquisar);

        bagConst.ipadx = 30;
        bagConst.gridx = 0;
        bagConst.gridy = 1;
        boxPanel.add(buttonPanel, bagConst);

        add(boxPanel, BorderLayout.NORTH);
        add(veicPanel, BorderLayout.CENTER);
        setPreferredSize(new Dimension(800, 600));
        pack();
        RefineryUtilities.centerFrameOnScreen(this);
        setVisible(true);
    }

    /**
     * Method to toggle the cursor between a loading and a default one
     * @param inProgress
     */
    public void loading(boolean inProgress) {
        if (inProgress) {
            this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        } else {
            this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }

    /**
     * Checks what selection was chosen and acts accordingly
     * @param e
     */
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        loading(true);
        if (source == veiculos) {
            String veic = (String) veiculos.getSelectedItem();
            if (veic.equals("Carros")) {
                qualVeic = new String("carros");
            } else if (veic.equals("Motos")) {
                qualVeic = new String("motos");
            } else if (veic.equals("Caminhões")) {
                qualVeic = new String("caminhoes");
            } else {
                clearSelection(veiculos);
            }
            getJSONMarca("marcas");
        } else if (source == marcas) {
            if (marcas.getSelectedItem() == emptyItem) {
                clearSelection(marcas);
            } else {
                JComboItem marc = (JComboItem) marcas.getSelectedItem();
                qualMarca = marc.getValue();
                getJSONModelo("modelos");
            }
        } else if (source == modelos) {
            if (modelos.getSelectedItem() == emptyItem) {
                clearSelection(modelos);
            } else {
                JComboItem marc = (JComboItem) modelos.getSelectedItem();
                qualModelo = marc.getValue();
                getJSONVeiculo("veiculo");
            }
        } else if (source == pesquisar) {
            try {
                JComboItem marc = (JComboItem) ano.getSelectedItem();
                qualAno = marc.getValue();
                getJSONAno("ano");
            } catch (Exception searchException) {
                JOptionPane.showMessageDialog(this, "Preencha todos os valores antes de pesquisar", "Pesquisa Inválida", JOptionPane.WARNING_MESSAGE);
            }
        } else if (source == marcasPorVeic) {
            qualVeic = new String("carros");
            int sizeCarros = ((JSONArray) getJSONArray("marcas")).size();

            qualVeic = new String("motos");
            int sizeMotos = ((JSONArray) getJSONArray("marcas")).size();

            qualVeic = new String("caminhoes");
            int sizeCaminhoes = ((JSONArray) getJSONArray("marcas")).size();

            fazGrafico("marcasPorVeic", sizeCarros, sizeMotos, sizeCaminhoes, null);
        } else if (source == caminPorMarca) {
            qualVeic = new String("caminhoes");
            JSONArray marcasArray = (JSONArray) getJSONArray("marcas");
            ArrayList<Integer> quantModelos = new ArrayList<Integer>();
            for (int i = 0; i < marcasArray.size(); ++i) {
                JSONObject aux = (JSONObject) marcasArray.get(i);
                qualMarca = String.valueOf(aux.get("id"));
                JSONArray modelosArray = (JSONArray) getJSONArray("modelos");
                quantModelos.add(modelosArray.size());
            }
            jsonArray = marcasArray;
            fazGrafico("", 0, 0, 0, quantModelos);
        }
        loading(false);
    }

    /**
     * Method to create the charts on the program
     * @param qualGraf
     * What type of graph it is
     * @param sizeCar
     * The size of the Cars Array
     * @param sizeMot
     * The size of the Motorcycles Array
     * @param sizeCami
     * The size of the Trucks Array
     * @param quantMod
     * An array with the number of models per manufacturer
     */
    public void fazGrafico(String qualGraf, int sizeCar, int sizeMot, int sizeCami, ArrayList<Integer> quantMod) {
        if (qualGraf.equals("marcasPorVeic")) {
            DefaultPieDataset result = new DefaultPieDataset();
            result.setValue("Carros", sizeCar);
            result.setValue("Caminhões", sizeCami);
            result.setValue("Motos", sizeMot);
            graficoPizza = new PieChart("Gráfico", "Quantidade de marcas por veículo", result);
            graficoPizza.pack();
            RefineryUtilities.centerFrameOnScreen(graficoPizza);
            graficoPizza.setVisible(true);
        } else {
            DefaultCategoryDataset dataSet = new DefaultCategoryDataset();
            for (int i = 0; i < quantMod.size(); ++i) {
                JSONObject auxJSON = (JSONObject) jsonArray.get(i);
                dataSet.addValue((double) quantMod.get(i), "Marcas", (String) auxJSON.get("fipe_name"));
            }
            graficoBarra = new BarChart("Gráfico", "Quantidade de caminhões por marca", dataSet);
            graficoBarra.pack();
            RefineryUtilities.centerFrameOnScreen(graficoBarra);
            graficoBarra.setVisible(true);
        }
    }

    /**
     * Clears the inputs after the one that was chosen if the user has clicked on an empty item
     * on the chosen input
     * @param item
     * The item that was selected
     */
    public void clearSelection(JComboBox item) {
        for (int i = boxes.indexOf(item) + 1; i < boxes.size(); i++) {
            boxes.get(i).removeActionListener(this);
            boxes.get(i).removeAllItems();
        }

        veicPanel.removeAll();
        veicPanel.add(new JLabel(""));
        veicPanel.revalidate();
        veicPanel.repaint();
        pack();
    }

    /**
     * Gets a URL to make an API call and returns a object from this call
     * @param qualUrl
     * The URL to make the API call
     * @return A JSON parsed into a Java Object
     */
    public Object getJSONArray(String qualUrl) {
        String urlString = "";
        if (qualUrl.equals("marcas")) {
            urlString = "https://fipeapi.appspot.com/api/1/" + qualVeic + "/marcas.json";
        } else if (qualUrl.equals("modelos")) {
            urlString = "https://fipeapi.appspot.com/api/1/" + qualVeic + "/veiculos/" + qualMarca + ".json";
        } else if (qualUrl.equals("veiculo")) {
            urlString = "https://fipeapi.appspot.com/api/1/" + qualVeic + "/veiculo/" + qualMarca + "/" + qualModelo + ".json";
        } else if (qualUrl.equals("ano")) {
            urlString = "https://fipeapi.appspot.com/api/1/" + qualVeic + "/veiculo/" + qualMarca + "/" + qualModelo + "/" + qualAno + ".json";
        }
        String inLine = "";
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            int responseCode = conn.getResponseCode();

            if (responseCode != 200) {
                throw new RuntimeException("HttpResponseCode: " + responseCode);
            } else {
                Scanner sc = new Scanner(url.openStream());
                while (sc.hasNext()) {
                    inLine = inLine + sc.nextLine();
                }
                sc.close();
            }

            JSONParser parser = new JSONParser();
            Object obj = parser.parse(inLine);
            conn.disconnect();
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Um erro inesperado ocorreu", "Erro", JOptionPane.ERROR_MESSAGE);
            loading(false);
        }
        return null;
    }

    /**
     * When the final input was filled and the user has clicked on the search
     * button, this method shows the vehicle info
     * @param qualUrl
     * URL to make the API Call
     */
    public void getJSONAno(String qualUrl) {
        JSONObject jObjAux = (JSONObject) getJSONArray(qualUrl);
        veicPanel = new JPanel(new GridLayout(7, 1));
        String aux = "Nome: " + jObjAux.get("name");
        JLabel nome = new JLabel(aux, SwingConstants.CENTER);
        aux = "Marca: " + jObjAux.get("marca");
        JLabel marca = new JLabel(aux, SwingConstants.CENTER);
        aux = "Ano: " + jObjAux.get("ano_modelo");
        JLabel ano = new JLabel(aux, SwingConstants.CENTER);
        aux = "Preço: " + jObjAux.get("preco");
        JLabel preco = new JLabel(aux, SwingConstants.CENTER);
        aux = "Combustível: " + jObjAux.get("combustivel");
        JLabel comb = new JLabel(aux, SwingConstants.CENTER);
        aux = "Referência: " + jObjAux.get("referencia");
        JLabel ref = new JLabel(aux, SwingConstants.CENTER);
        aux = "Código FIPE: " + jObjAux.get("fipe_codigo");
        JLabel codFipe = new JLabel(aux, SwingConstants.CENTER);

        veicPanel.add(nome);
        veicPanel.add(marca);
        veicPanel.add(ano);
        veicPanel.add(preco);
        veicPanel.add(comb);
        veicPanel.add(ref);
        veicPanel.add(codFipe);
        veicPanel.setBorder(new EmptyBorder(0, 20, 20, 20));

        add(veicPanel, SwingConstants.CENTER);
        pack();
    }

    /**
     * Populates the "year" input with all the manufacturing years of the chosen model
     * @param qualUrl
     * URL to make the API Call
     */
    public void getJSONVeiculo(String qualUrl) {
        jsonArray = (JSONArray) getJSONArray(qualUrl);
        int i = 0;
        clearSelection(ano);
        ano.removeActionListener(this);
        ano.removeAllItems();
        ano.addItem(emptyItem);
        while (i < jsonArray.size()) {
            JSONObject jObjAux = (JSONObject) jsonArray.get(i);
            String nomeAno = (String) jObjAux.get("name");
            String idAno = (String) jObjAux.get("id");
            ano.addItem(new JComboItem(nomeAno, idAno));
            ++i;
        }
        ano.addActionListener(this);
        pack();
    }

    /**
     * Populates the "models" input with all the models from the chosen manufacturer
     * @param qualUrl
     * URL to make the API Call
     */
    public void getJSONModelo(String qualUrl) {
        jsonArray = (JSONArray) getJSONArray(qualUrl);
        int i = 0;
        clearSelection(marcas);
        modelos.addItem(emptyItem);
        while (i < jsonArray.size()) {
            JSONObject jObjAux = (JSONObject) jsonArray.get(i);
            String nomeModelo = (String) jObjAux.get("fipe_name");
            String idModelo = (String) jObjAux.get("id");
            modelos.addItem(new JComboItem(nomeModelo, idModelo));
            ++i;
        }
        modelos.addActionListener(this);
        ano.addActionListener(this);
        pack();
    }

    /**
     * Populates the "manufacturers" input with all the manufacturers of the chosen vehicle
     * @param qualUrl
     * URL to make the API Call
     */
    public void getJSONMarca(String qualUrl) {
        jsonArray = (JSONArray) getJSONArray(qualUrl);
        int i = 0;
        clearSelection(veiculos);
        marcas.addItem(emptyItem);
        while (i < jsonArray.size()) {
            JSONObject jObjAux = (JSONObject) jsonArray.get(i);
            String nomeMarca = (String) jObjAux.get("fipe_name");
            String idMarca = String.valueOf((long) jObjAux.get("id"));
            marcas.addItem(new JComboItem(nomeMarca, idMarca));
            ++i;
        }
        marcas.addActionListener(this);
        modelos.addActionListener(this);
        ano.addActionListener(this);
        pack();
    }
}















