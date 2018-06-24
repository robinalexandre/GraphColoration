package fr.etu.ea.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

import fr.etu.ea.model.graphe.Graphe;

public class Writer {
	
	public static void writeOutFile(Graphe graphe) {
		File file = new File(Reader.path.substring(0, Reader.path.lastIndexOf(".")) + ".out");
		if (file.exists()){
			file.delete();
		}
		
		appendText(file, "K = " + graphe.kColor + "\n");
		
		for (int i=0; i<graphe.noeuds.length; i++) {
			appendText(file, graphe.noeuds[i].couleur + " ");
		}
	}
	
	private static void appendText(File file, String text){
		try {
			if (!file.exists()){
				file.createNewFile();
			}
			Files.write(file.toPath(), text.getBytes(), StandardOpenOption.APPEND);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
