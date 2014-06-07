package org.muffinapps.onlyaweek;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class StudyingRatioUtils {
	
	public static float calculateRatio(int remainingPages, GregorianCalendar examDate, int revisionDays){
		int remainingDays;
		GregorianCalendar today = (GregorianCalendar) GregorianCalendar.getInstance();
		
		remainingDays = getJulianDay(examDate) - getJulianDay(today) - revisionDays;
		
		if(remainingDays <= 0){
			return -1;
		}else{
			return remainingPages / (float) remainingDays;
		}
	}
	
	private static int getJulianDay(GregorianCalendar calendar){
		int year, month, day;
		int a, b, c, e, f;
		
		year = calendar.get(Calendar.YEAR);
		month = calendar.get(Calendar.MONTH) + 1;
		day = calendar.get(Calendar.DAY_OF_MONTH);
		
		if(month < 3){
			year--;
			month += 12;
		}
		
		a = year/100;
		b = a/4;
		c = 2-a+b;
		e = (int) 365.25 * (year + 4716);
		f = (int) 30.6001 * (month + 1);
		
		return c + day + e + f - 1524;
	}
	
	public static String getRatioString(float ratio){
		if(ratio < 0)
			return "Repasando...";
		
		int ratioInt = (int) ratio;
		String result = "" + ratioInt;
		
		if((ratio - ratioInt) > 0){
			result = result + " - " + (ratioInt+1);
		}
		
		return result + " pags/dia";
	}
	
	public static String getRatioComment(float ratio, int remainingPages){
		if(ratio < 0){
			if(remainingPages > 0){
				return "¿En días de repaso y aún con páginas por estudiar? Más te vale terminar con lo que te queda cuanto antes.";
			}else{
				return "Repasar no hace milagros, pero tampoco hace daño a nadie.";				
			}
		}
		
		if(ratio < 10.0f)
			return "Vas muy bien. Sal a tomarte algo. Nosotros no diremos nada.";
					
		if(ratio < 20.f)
			return "Vas bien. Hasta incluso puede que apruebes y todo.";
		
		if(ratio < 30.0f)
			return "No llevas un mal ritmo pero te esperán unos dias de duro estudio si no lo mejoras";
		
		if(ratio < 40.0f)
			return "Vas mal. No te separes de los libros si quieres sacar algo de este exámen.";
		
		return "¿Desde cuando vas tan mál? A nosotros no nos eches las culpas. Espero que tengas café suficiente porque lo tienes crudo si quieres aprobar.";
	}
}
