package application;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.SwingWorker;

public class TaskUrl extends SwingWorker<Void, Void> {
	
	@Override
	public Void doInBackground() {
		int progress = 0;
		// Initialize progress property.
		setProgress(0);
		File[] selectedFiles = PopUp.fileDialog(false, "xlsx");
		ArrayList<String> urlList = new ArrayList<String>();
		urlList = ReadXLSX.read(selectedFiles[0]);
		
		
		int countSelectetFiels = urlList.size();
		if (countSelectetFiels > 0) {
			ArrayList<Invoice> invoices = new ArrayList<Invoice>();

			for (String url : urlList) {
				String text;
				try {
					text = ReadPDFFromURL.readPDFFromURL(url);
					String[] docxLines = text.split(System.lineSeparator());
					setProgress((progress * 100) / countSelectetFiels);
					try {
						invoices.add(InvoiceLineExtractor.extractAccLines(docxLines, url));
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				progress++;
			}
			POIforgfgWrite.createXLSX(invoices);
			setProgress((progress * 100) / countSelectetFiels);
		}
		return null;
	}



    	
}
