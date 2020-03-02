package application;

import java.io.File;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

public class ReadPDF {

	public static String readPDF(File input) {
		String text = "";

		PDDocument pdDoc;
		try {
			pdDoc = PDDocument.load(input);
			PDFTextStripper pdfStripper = new PDFTextStripper();
			text = pdfStripper.getText(pdDoc);
//			System.out.println(text);
			pdDoc.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return text;
	}
}
