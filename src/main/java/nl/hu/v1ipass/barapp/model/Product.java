package nl.hu.v1ipass.barapp.model;

public class Product {
	private int productnummer;
	private String naam;
	private double prijs;
	private int minVoorraad;
	private String Categorie;
	private int voorraad;
	private double inkoop;
	
	public Product(int productnummer, String naam, double prijs, int minVoorraad, String categorie, int voorraad, double inkoop) {
		super();
		this.productnummer = productnummer;
		this.naam = naam;
		this.prijs = prijs;
		this.minVoorraad = minVoorraad;
		Categorie = categorie;
		this.voorraad = voorraad;
		this.inkoop = inkoop;
	}

	public int getProductnummer() {
		return productnummer;
	}

	public void setProductnummer(int productnummer) {
		this.productnummer = productnummer;
	}

	public String getNaam() {
		return naam;
	}

	public void setNaam(String naam) {
		this.naam = naam;
	}

	public double getPrijs() {
		return prijs;
	}

	public void setPrijs(double prijs) {
		this.prijs = prijs;
	}

	public int getMinVoorraad() {
		return minVoorraad;
	}

	public void setMinVoorraad(int minVoorraad) {
		this.minVoorraad = minVoorraad;
	}

	public String getCategorie() {
		return Categorie;
	}

	public void setCategorie(String categorie) {
		Categorie = categorie;
	}
	

	public int getVoorraad() {
		return voorraad;
	}

	public void setVoorraad(int voorraad) {
		this.voorraad = voorraad;
	}

	public double getInkoop() {
		return inkoop;
	}

	public void setInkoop(double inkoop) {
		this.inkoop = inkoop;
	}

	@Override
	public String toString() {
		return "Product [productnummer=" + productnummer + ", naam=" + naam + ", prijs=" + prijs + ", minVoorraad="
				+ minVoorraad + ", Categorie=" + Categorie + ", voorraad=" + voorraad + ", inkoop=" + inkoop + "]";
	}
	
}
