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
import java.util.Scanner;
import java.net.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class APIFIPEGUI extends JFrame implements ActionListener {
    private JComboBox<String> veiculos;
    private JComboBox<JComboItem> marcas;
    private JComboBox<JComboItem> modelos;
    private JComboBox<String> ano;
    private JComboBox<String> preco;
    private JButton pesquisar;
    private JPanel panel;
    private JSONArray jsonArray;
    private String qualVeic;
    private String qualMarca;
    private String qualModelo;

    public APIFIPEGUI(){
        super("API FIPE");
        setLayout(new BorderLayout());
        String[] quaisVeic = new String[]{"Carros", "Motos", "Caminhões"};
        veiculos = new JComboBox<String>(quaisVeic);
        veiculos.addActionListener(this);
        marcas = new JComboBox<JComboItem>();
        marcas.addActionListener(this);
        modelos = new JComboBox<JComboItem>();
        modelos.addActionListener(this);
        ano = new JComboBox<String>();
        preco = new JComboBox<String>();
        pesquisar = new JButton("Pesquisar");

        panel = new JPanel(new GridLayout(2, 6, 10, 10));
        panel.setBorder(new EmptyBorder(20,20,20,20));
        panel.add(new JLabel("Veículos", SwingConstants.CENTER));
        panel.add(new JLabel("Marcas", SwingConstants.CENTER));
        panel.add(new JLabel("Modelos", SwingConstants.CENTER));
        panel.add(new JLabel("Ano", SwingConstants.CENTER));
        panel.add(new JLabel("Preço", SwingConstants.CENTER));
        panel.add(new JLabel("", SwingConstants.CENTER));
        panel.add(veiculos);
        panel.add(marcas);
        panel.add(modelos);
        panel.add(ano);
        panel.add(preco);
        panel.add(pesquisar);
        add(panel, BorderLayout.NORTH);

        pack();
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e){
        if(e.getSource() == veiculos){
            String veic = (String) veiculos.getSelectedItem();
            if(veic.equals("Carros")){
                qualVeic = new String("carros");
            }else if(veic.equals("Motos")){
                qualVeic = new String("motos");
            }else if(veic.equals("Caminhões")){
                qualVeic = new String("caminhoes");
            }
            getJSONMarca("marcas");
        }else if(e.getSource() == marcas){
            JComboItem marc = (JComboItem) marcas.getSelectedItem();
            long value = marc.getValue();
            qualMarca = String.valueOf(value);
            getJSONModelo("modelos");
        }else if(e.getSource() == modelos){
            JComboItem marc = (JComboItem) modelos.getSelectedItem();
            long value = marc.getValue();
            qualModelo = String.valueOf(value);
            getJSONVeiculo("veiculo");
        }
    }

    public JSONArray getJSONArray(String qualUrl){
        String urlString = "";
        if(qualUrl.equals("marcas")) {
            urlString = "https://fipeapi.appspot.com/api/1/" + qualVeic + "/marcas.json";
        }else if(qualUrl.equals("modelos")){
            urlString = "https://fipeapi.appspot.com/api/1/" + qualVeic + "/veiculos/" + qualMarca + ".json";
        }else if(qualUrl.equals("veiculo")){
            urlString = "https://fipeapi.appspot.com/api/1/" + qualVeic + "/veiculo/" + qualMarca + "/" + qualModelo +  ".json";
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

            return (JSONArray) obj;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public void getJSONVeiculo(String qualUrl){
        jsonArray = getJSONArray(qualUrl);
        int i = 0;
        while(i < jsonArray.size()) {
            JSONObject jObjAux = (JSONObject) jsonArray.get(i);
            String nomeModelo = (String) jObjAux.get("name");
            String idModelo = (String) jObjAux.get("id");
            System.out.println(nomeModelo + " " + idModelo);
            ++i;
        }
    }

    public void getJSONModelo(String qualUrl){
        jsonArray = getJSONArray(qualUrl);
        int i = 0;
        modelos.removeActionListener(this);
        modelos.removeAllItems();
        while(i < jsonArray.size()) {
            JSONObject jObjAux = (JSONObject) jsonArray.get(i);
            String nomeModelo = (String) jObjAux.get("fipe_name");
            long idModelo = Long.valueOf((String) jObjAux.get("id"));
            modelos.addItem(new JComboItem(nomeModelo, idModelo));
            ++i;
        }
        modelos.addActionListener(this);
    }

    public void getJSONMarca(String qualUrl){
        jsonArray = getJSONArray(qualUrl);
        int i = 0;
        marcas.removeActionListener(this);
        modelos.removeActionListener(this);
        marcas.removeAllItems();
        modelos.removeAllItems();
        while(i < jsonArray.size()){
            JSONObject jObjAux = (JSONObject) jsonArray.get(i);
            String nomeMarca = (String) jObjAux.get("fipe_name");
            long idMarca = (long) jObjAux.get("id");
            marcas.addItem(new JComboItem(nomeMarca, idMarca));
            ++i;
        }
        marcas.addActionListener(this);
    }
}















