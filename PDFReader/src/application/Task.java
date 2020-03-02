package application;

import java.io.File;
import java.util.ArrayList;

import javax.swing.SwingWorker;

public class Task extends SwingWorker<Void, Void> {
	
	@Override
	public Void doInBackground() {
		int progress = 0;
		// Initialize progress property.
		setProgress(0);
		File[] selectedFiles = PopUp.fileDialog();
		int countSelectetFiels = selectedFiles.length;
		if (countSelectetFiels > 0) {
			ArrayList<Invoice> invoices = new ArrayList<Invoice>();

			for (File file : selectedFiles) {
				String text = ReadPDF.readPDF(file);
				String[] docxLines = text.split(System.lineSeparator());
				setProgress((progress * 100) / countSelectetFiels);
				try {
					invoices.add(InvoiceLineExtractor.extractAccLines(docxLines, file.getName()));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				progress++;
			}
			POIforgfgWrite.createXLSX(invoices);
			setProgress((progress * 100) / countSelectetFiels);
		}
		return null;
	}



    	
}
