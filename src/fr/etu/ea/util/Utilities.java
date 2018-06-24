package fr.etu.ea.util;

import java.util.Calendar;
import java.util.Random;

public class Utilities {
	
	public static Random random = new Random(Calendar.getInstance().getTimeInMillis());
	
	/**
	 *  Return true or false en fonction d'une probabilité "percent" du type 80 pour 80%
	 * @param percent
	 * @return un booleen
	 */
	public static boolean probabilite(Integer percent) {
		if(percent != null) {
			int i = random.nextInt(100);
	        if (i < percent) {
	            return true;
	        } else {
	            return false;
	        }
		} else
			return true;
	}
}
