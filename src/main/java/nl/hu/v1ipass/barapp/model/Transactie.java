package nl.hu.v1ipass.barapp.model;

import java.sql.Date;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nl.hu.v1ipass.barapp.persistence.KassasysteemPostgressDaoImpl;
import nl.hu.v1ipass.barapp.persistence.MedewerkerPostgressDaoImpl;
import nl.hu.v1ipass.barapp.persistence.ProductPostgressDaoImpl;

public class Transactie {
	private double winst;
	private Date datum;
	private Kassasysteem kassanummer;
	private Medewerker medewerker;
	private List<Product> mijnProducten;
	private List<Integer> hoeveelheden;
	
	private KassasysteemPostgressDaoImpl kDao = new KassasysteemPostgressDaoImpl();
	private MedewerkerPostgressDaoImpl mDao = new MedewerkerPostgressDaoImpl();
	private ProductPostgressDaoImpl pDao = new ProductPostgressDaoImpl();
	
	public Transactie() {
		
	}
	
	public Transactie(double winst, Date datum, int kassanummer, int medewerker,
			int mijnProduct, int hoeveelheid) throws SQLException {
		super();
		this.winst = winst;
		this.datum = datum;
		this.kassanummer = kDao.findByKassa(kassanummer);
		this.medewerker = mDao.findbyMnr(medewerker);
		this.mijnProducten = new ArrayList<Product>();
		this.mijnProducten.add(pDao.findByProduct(mijnProduct));
		this.hoeveelheden = new ArrayList<Integer>();
		this.hoeveelheden.add(hoeveelheid);
		

	}

	public Transactie(int kassanummer, int medewerker,List<Integer> hoeveelheden, int[] mijnproducten) throws SQLException {
		super();
		//this.transactienummer = transactienummer;
		//his.winst = winst;
		
		SimpleDateFormat formatter= new SimpleDateFormat("dd-MM-yyyy");  
		Date date = new Date(System.currentTimeMillis());  
		this.datum = date;
		
		this.hoeveelheden = hoeveelheden;
		this.kassanummer = kDao.findByKassa(kassanummer);
		this.medewerker = mDao.findbyMnr(medewerker);
		
		mijnProducten = new ArrayList<Product>();
		for (int nummer : mijnproducten) {
			mijnProducten.add(pDao.findByProduct(nummer));
		}
	}
	
	public void addP(int p) throws SQLException {
		mijnProducten.add(pDao.findByProduct(p));
	}
	
	public void addH(int h) {
		hoeveelheden.add(h);
	}

	public Kassasysteem getKassanummer() {
		return kassanummer;
	}

	public void setKassanummer(int kassanummer) throws SQLException {
		this.kassanummer = kDao.findByKassa(kassanummer);
	}

	public List<Integer> getHoeveelheden() {
		return hoeveelheden;
	}

	public void setHoeveelheden(List<Integer> hoeveelheden) {
		this.hoeveelheden = hoeveelheden;
	}


	public Medewerker getMedewerker() {
		return medewerker;
	}



	public void setMedewerker(int medewerker) throws SQLException {
		this.medewerker = mDao.findbyMnr(medewerker);
	}

	public double getWinst() {
		return winst;
	}

	public void setWinst(double winst) {
		this.winst = winst;
	}

	public Date getDatum() {
		return datum;
	}

	public void setDatum(Date datum) {
		this.datum = datum;
	}

	public List<Product> getMijnproducten() {
		return mijnProducten;
	}

	public void setMijnproducten(int[] mijnproducten) throws SQLException {
		mijnProducten = new ArrayList<Product>();
		for (int nummer : mijnproducten) {
			mijnProducten.add(pDao.findByProduct(nummer));
		}
	}

	@Override
	public String toString() {
		return "Transactie [winst=" + winst + ", datum=" + datum + ", kassanummer=" + kassanummer + ", medewerker="
				+ medewerker + ", mijnProducten=" + mijnProducten + ", hoeveelheden=" + (hoeveelheden)
				+ "]";
	}
	
}
