package com.github.jgenetics.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.github.jgenetics.exception.GeneticsException;

public class GlobalReport {
	
	private File reportFile;
	
	private DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
	
	public GlobalReport() throws GeneticsException {
		String date = dateFormat.format(new Date());
		reportFile =new File("report_" + date + ".txt");
		
		if(!reportFile.exists()){
			try {
				reportFile.createNewFile();
			} catch (IOException e) {
				throw new GeneticsException("Problem with save information to global report", e);
			}
		}
	}
	
	public void updateGlobalReport(int iteration, double minValue, double avgValue, double maxValue) throws GeneticsException {
		
		String line = String.format("%s %s %s %s%n", iteration, minValue, avgValue, maxValue); 
		
		FileWriter fileWritter;
		try {
			fileWritter = new FileWriter(reportFile.getName(),true);
			BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
	        bufferWritter.write(line);
	        bufferWritter.close();
		} catch (IOException e) {
			throw new GeneticsException("Problem with save information to global report", e);
		}
		
		System.out.println(line);
        
	}
}
