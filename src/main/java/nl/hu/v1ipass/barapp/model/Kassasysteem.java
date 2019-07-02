package nl.hu.v1ipass.barapp.model;

import java.sql.SQLException;
import java.util.List;

import nl.hu.v1ipass.barapp.persistence.MedewerkerPostgressDaoImpl;
import nl.hu.v1ipass.barapp.persistence.ProductPostgressDaoImpl;

public class Kassasysteem {
	private int kassanummer;
	private String naam;
	private String locatie;
	private double budget;
	private List<Medewerker> mijnMedewerkers;
	private List<Product> mijnProducten;
	private MedewerkerPostgressDaoImpl mDao = new MedewerkerPostgressDaoImpl();
	private ProductPostgressDaoImpl pDao = new ProductPostgressDaoImpl();
	
	public Kassasysteem(int kassanummer, String naam, String locatie, double budget) throws SQLException {
		super();
		this.kassanummer = kassanummer;
		this.naam = naam;
		this.locatie = locatie;
		this.budget = budget;
		setMijnMedewerkers(mDao.findByKassa(kassanummer));
		setMijnProducten(pDao.findByKassa(kassanummer));
	}

	public int getKassanummer() {
		return kassanummer;
	}

	public void setKassanummer(int kassanummer) {
		this.kassanummer = kassanummer;
	}

	public String getNaam() {
		return naam;
	}

	public void setNaam(String naam) {
		this.naam = naam;
	}

	public String getLocatie() {
		return locatie;
	}

	public void setLocatie(String locatie) {
		this.locatie = locatie;
	}

	public double getBudget() {
		return budget;
	}

	public void setBudget(double budget) {
		this.budget = budget;
	}

	public List<Medewerker> getMijnMedewerkers() {
		return mijnMedewerkers;
	}

	public void setMijnMedewerkers(List<Medewerker> mijnMedewerkers) {
		this.mijnMedewerkers = mijnMedewerkers;
	}

	public List<Product> getMijnProducten() {
		return mijnProducten;
	}

	public void setMijnProducten(List<Product> mijnProducten) {
		this.mijnProducten = mijnProducten;
	}

	@Override
	public String toString() {
		return "Kassasysteem [kassanummer=" + kassanummer + ", naam=" + naam + ", locatie=" + locatie + ", budget="
				+ budget + ", mijnMedewerkers=" + mijnMedewerkers + ", mijnProducten=" + mijnProducten + "]";
	}
	
	
}
