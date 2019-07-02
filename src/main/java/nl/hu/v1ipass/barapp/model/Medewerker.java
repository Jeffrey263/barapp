package nl.hu.v1ipass.barapp.model;

public class Medewerker {
	private int mnr;
	private String voorletters;
	private String achternaam;
	private String tussenvoegsel;	
	private String functie;
	
	public Medewerker(int mnr, String voorletters, String achternaam, String tussenvoegsel, String functie) {
		super();
		this.mnr = mnr;
		this.voorletters = voorletters;
		this.achternaam = achternaam;
		this.tussenvoegsel = tussenvoegsel;
		this.functie = functie;
	}

	public int getMnr() {
		return mnr;
	}

	public void setMnr(int mnr) {
		this.mnr = mnr;
	}

	public String getVoorletters() {
		return voorletters;
	}

	public void setVoorletters(String voorletters) {
		this.voorletters = voorletters;
	}

	public String getAchternaam() {
		return achternaam;
	}

	public void setAchternaam(String achternaam) {
		this.achternaam = achternaam;
	}

	public String getTussenvoegsel() {
		return tussenvoegsel;
	}

	public void setTussenvoegsel(String tussenvoegsel) {
		this.tussenvoegsel = tussenvoegsel;
	}

	public String getFunctie() {
		return functie;
	}

	public void setFunctie(String functie) {
		this.functie = functie;
	}

	@Override
	public String toString() {
		return "medewerker [mnr=" + mnr + ", voorletters=" + voorletters + ", achternaam=" + achternaam
				+ ", tussenvoegsel=" + tussenvoegsel + ", functie=" + functie + "]";
	}
		
}
