package prueba;

public class DataSubject {
	public String name;
	public String date;
	public int totalPag, remainingPag, assignedPag;
	
	public DataSubject(String name, String date, int totalPag,
			int remainingPag, int assignedPag) {
		
		this.name = name;
		this.date = date;
		this.totalPag = totalPag;
		this.remainingPag = remainingPag;
		this.assignedPag = assignedPag;
	}
}
