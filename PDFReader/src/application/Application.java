package application;

import java.io.File;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.apache.log4j.BasicConfigurator;

public class Application {

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				BasicConfigurator.configure();
				createAndShowGUI();
			}
		});
	}

	public static void runApp() {
		File[] selectedFiles = PopUp.fileDialog();
		int countSelectetFiels = selectedFiles.length;
		if (countSelectetFiels > 0) {
			ArrayList<Invoice> invoices = new ArrayList<Invoice>();

			for (File file : selectedFiles) {
				String text = ReadPDF.readPDF(file);
				String[] docxLines = text.split(System.lineSeparator());

				try {
					invoices.add(InvoiceLineExtractor.extractAccLines(docxLines, file.getName()));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			POIforgfgWrite.createXLSX(invoices);
		}
		System.exit(0);
	}

	private static void createAndShowGUI() {
		// Create and set up the window.
		JFrame frame = new JFrame("PDF to XLSX reader");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Create and set up the content pane.
		JComponent newContentPane = new ProgressBarDemo();
		newContentPane.setOpaque(true); // content panes must be opaque
		frame.setContentPane(newContentPane);

		// Display the window.
		frame.pack();
		frame.setVisible(true);
	}

}
