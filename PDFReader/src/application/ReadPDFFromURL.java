package application;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.URL;
import java.net.URLConnection;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

public class ReadPDFFromURL {

	public static String readPDFFromURL(String url) throws IOException {
		
		String text = "";
		URL url1 = new URL(url);

		byte[] ba1 = new byte[1024];
		int baLength;
		try {
			// Contacting the URL
			System.out.print("Connecting to " + url1.toString() + " ... ");
			URLConnection urlConn = url1.openConnection();

			// Checking whether the URL contains a PDF
			if (!urlConn.getContentType().equalsIgnoreCase("application/pdf")) {
				System.out.println("FAILED.\n[Sorry. This is not a PDF.]");
			} else {
				try {

					// Read the PDF from the URL and save to a local file
					InputStream in = new BufferedInputStream(url1.openStream());
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					while ((baLength = in.read(ba1)) != -1) {
						out.write(ba1, 0, baLength);
					}
					out.close();
					in.close();
					byte[] response = out.toByteArray();
					
					PDDocument pdDoc;
					pdDoc = PDDocument.load(response);
					
					PDFTextStripper pdfStripper = new PDFTextStripper();
					text = pdfStripper.getText(pdDoc);
					pdDoc.close();

				} catch (ConnectException ce) {
					System.out.println("FAILED.\n[" + ce.getMessage() + "]\n");
				}
			}

		} catch (NullPointerException npe) {
			System.out.println("FAILED.\n[" + npe.getMessage() + "]\n");
		}
		return text;
	}
}