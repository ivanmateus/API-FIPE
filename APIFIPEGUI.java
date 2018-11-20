//------------------------------------------------------------
//Ivan Mateus de Lima Azevedo
//10525602
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
	private JComboBox<String> marcas;
	private JComboBox<String> modelos;
	private JComboBox<String> ano;
	private JComboBox<String> preco;
	private JButton pesquisar;
	private JPanel panel;

	public APIFIPEGUI(){
		super("API FIPE");
		setLayout(new BorderLayout());
		String[] quaisVeic = new String[]{"Carros", "Motos", "Caminhões"};
		veiculos = new JComboBox<String>(quaisVeic);
		veiculos.addActionListener(this);
		marcas = new JComboBox<String>();
		modelos = new JComboBox<String>();
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
				getJSON("carros");
			}else if(veic.equals("Motos")){
				getJSON("motos");
			}else if(veic.equals("Caminhões")){
				getJSON("caminhoes");
			}
		}
	}

	public void getJSON(String veiculo){
		String urlString = "https://fipeapi.appspot.com/api/1/" + veiculo + "/marcas.json";
		String inLine = "";
		try{
			URL url = new URL(urlString);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.connect();
			int responseCode = conn.getResponseCode();

			if(responseCode != 200){
				throw new RuntimeException("HttpResponseCode: " + responseCode);
			}else{
				Scanner sc = new Scanner(url.openStream());
				while(sc.hasNext()){
					inLine = inLine + sc.nextLine();
				}
				sc.close();
			}

			JSONParser parse = new JSONParser();
			JSONObject jObj = (JSONObject) parse.parse(inLine);
			JSONArray jArray = (JSONArray) jObj.get("fipe_name");
		
			int i = 0;
			while(i < jArray.size()){
				JSONObject jObjAux = (JSONObject) jArray.get(i);
				System.out.println("Marca: " + jObjAux);
				++i;
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}
















