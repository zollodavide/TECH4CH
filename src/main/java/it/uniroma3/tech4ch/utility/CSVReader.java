package it.uniroma3.tech4ch.utility;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import it.uniroma3.tech4ch.model.Visitor;

public class CSVReader {
	
	public CSVReader() {}
	
	
	public List<Visitor> read() throws IOException, URISyntaxException, ParseException {
		String pathToCsv = "Visitorsgrouping.csv";
		URL url = getClass().getResource(pathToCsv);
		String row = "";
		BufferedReader csvReader = new BufferedReader(new FileReader(new File(url.toURI())));
		List<Visitor> visitors = new ArrayList<Visitor>();
		while ((row = csvReader.readLine()) != null) {
		    Visitor v = parseRow(row);
		    if (v != null)
		    	visitors.add(v);
		}
		csvReader.close();
		return visitors;
	}
	
	public Visitor parseRow(String row) {
		Visitor curr = new Visitor();
		String[] parts = row.split(";");
		try {
			
			curr.setId(Integer.parseInt(parts[VisitorFileConstants.ID]));
			curr.setGroup(Integer.parseInt(parts[VisitorFileConstants.GROUP]));
			if(parts[VisitorFileConstants.GROUPSIZE].isEmpty())
				curr.setGroupSize(1);
			else
				curr.setGroupSize(Integer.parseInt(parts[VisitorFileConstants.GROUPSIZE]));
			
			SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
			Date date = format.parse(parts[VisitorFileConstants.DATE]);
			curr.setDate(date);
			
			String[] st1 = parts[VisitorFileConstants.START].split(":");
			LocalTime l1 = LocalTime.of(Integer.parseInt(st1[0]), Integer.parseInt(st1[1]));
			curr.setStartTime(l1);

			String[] st2 = parts[VisitorFileConstants.END].split(":");
			LocalTime l2 = LocalTime.of(Integer.parseInt(st2[0]), Integer.parseInt(st2[1]));
			curr.setEndTime(l2);
		}
		catch (NumberFormatException e) {
			e.printStackTrace();
			return null;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return curr;
	}
	
}


