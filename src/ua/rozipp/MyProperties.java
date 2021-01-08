package ua.rozipp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

class MyProperties {

	Properties proper = null;
	public Window window = null;
	
	String date = null;
	String session = null;
	String sessionSK = null;
	String sessionN = null;
	int firstNumber = 0;
	int lastNumber = 0;
	String[] rishennyaNames = null;
	File folder = null;
	File[] files = null;
	File folder2 = null;
	File[] files2 = null;

	public MyProperties() {
		try {
			loadProperties();
		} catch (Exception e) {
			createProperties();
		}
	}

//	public void setDate(String date) {
//		this.date = date;
//	}
//
//	public void setSession(String session) {
//		this.session = session;
//	}
//
//	public void setSessionN(String sessionN) {
//		this.sessionN = sessionN;
//	}
//
//	public void setFirstNumber(int i) {
//		this.firstNumber = i;
//	}
//
//	public void setLastNumber(int i) {
//		this.lastNumber = i;
//	}

	public void setFolder(String dir) {
		this.folder = new File(dir);
		files = folder.listFiles();
		window.files_text.setText("В папці знайдено " + files.length + " файлів");
	}

	public void setFolder2(String dir) {
		this.folder2 = new File(dir);
		files2 = folder2.listFiles();
		window.files2_text.setText("В папці знайдено " + files2.length + " файлів");
	}
	
	public void checkCreateGolosuvannaWarning() throws MyException {
		if (files == null) {
			throw new MyException("Виберіть папку з файлами");
		}
		if (files.length != rishennyaNames.length) {
			throw new MyException("Файлів в папці " + files.length + ", а рядків в іменах рішень " + rishennyaNames.length);
		}
	}
	
	public void checkCreateRishennaWarning() throws MyException {
		if (files2 == null) {
			throw new MyException("Виберіть папку з файлами");
		}
		if (files2.length != rishennyaNames.length) {
			throw new MyException("Файлів в папці " + files2.length + ", а рядків в іменах рішень " + rishennyaNames.length);
		}
	}

	public void readFromWindowProperties() {
		this.date = window.date_text.getText();
		this.session = window.session_text.getText();
		this.sessionSK = window.sessionSK_text.getText();
		this.sessionN = window.sessionN_text.getText();
		this.firstNumber = Integer.parseInt(window.beginN_text.getText());
		this.rishennyaNames = window.list.getText().split("\n");
		this.lastNumber = this.firstNumber + rishennyaNames.length-1;
		window.endN_text.setText("" + this.lastNumber);
		try {
			saveProperties();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void writeToWindowProperties() {
		window.date_text.setText(this.date);
		window.session_text.setText(this.session);
		window.sessionSK_text.setText(this.sessionSK);
		window.sessionN_text.setText(this.sessionN);
		window.beginN_text.setText("" + this.firstNumber);
		window.endN_text.setText("" + this.lastNumber);
	}

	public void createProperties() {
		firstNumber = 1;
		sessionN = "-46/VII";
		date = "01.01.2020";
		session = "Сорок шоста сесія сьоме скликання";
		proper = new Properties();
		try {
			saveProperties();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void loadProperties() throws FileNotFoundException, IOException {
		File f = new File("properties.xml");
		try {
			InputStream is = new FileInputStream(f);
			proper = new Properties();
			proper.loadFromXML(is);
		} catch (FileNotFoundException e) {
			System.out.println("Файл настроек properties.xml не найден");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Нет доступа к файлу настроек properties.xml");
			e.printStackTrace();
		}
		firstNumber = Integer.parseInt(proper.getProperty("firstNumber"));
		sessionN = proper.getProperty("sesiaNumber");
		sessionSK = proper.getProperty("sesiaSK");
		date = proper.getProperty("from_date");
		session = proper.getProperty("doc_session");
	}

	public void saveProperties() throws FileNotFoundException, IOException {
		proper.setProperty("firstNumber", "" + firstNumber);
		proper.setProperty("sesiaNumber", "" + sessionN);
		proper.setProperty("sesiaSK", "" + sessionSK);
		proper.setProperty("from_date", "" + date);
		proper.setProperty("doc_session", "" + session);

		File f = new File("properties.xml");
		OutputStream os = new FileOutputStream(f);
		proper.storeToXML(os, null);
	}

}