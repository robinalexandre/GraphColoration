package fr.etu.ea.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

import javax.swing.JFileChooser;

import fr.etu.ea.Constants;
import fr.etu.ea.model.graphe.Graphe;

public class Reader {

	public static String path = "";

	public static Graphe AskGraph() {

		JFileChooser file = new JFileChooser();

//		file.setCurrentDirectory(new File("/Users/Alex/Documents/Cours/M2/Metaheuristique/"));
		file.showOpenDialog(null);
		Graphe graph = null;

		BufferedReader reader;
		try {
			path = file.getSelectedFile().getPath();
			reader = new BufferedReader(new FileReader(new File(file.getSelectedFile().getPath())));
		
			String line = reader.readLine();
			while(line.charAt(0) == 'c')
			{
				line = reader.readLine();
			}
			if(line.charAt(0) != 'p')
			{
				System.out.println("Input File Format Not Understood.");
				System.exit(1);
			}
	
			StringTokenizer token = new StringTokenizer(line, " ");
			token.nextToken();
			token.nextToken();
			Constants.NUMBER_NODES = Integer.parseInt(token.nextToken().trim());
			graph = new Graphe();
			line = reader.readLine();
			while(line != null)
			{
				token = new StringTokenizer(line, " ");
				token.nextToken();
				int sv = Integer.parseInt(token.nextToken().trim());
				int ev = Integer.parseInt(token.nextToken().trim());
				sv--;
				ev--;
				graph.ajouteArrete(sv, ev);
				line = reader.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return graph;
	}

}
