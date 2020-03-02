package application;

import java.util.ArrayList;
import java.util.HashMap;

public class InvoiceLineExtractor {

	private static String invNrText;
	private static String custText;
	private static String invDateText;
	private static String serviceDateText;
	private static String totalText;
	private static String amountText;
	private static String nettoText;
	private static String invCorrectNrText;
	private static Language lang = null;
	private static Boolean invoicePosition = false;

	public static Invoice extractAccLines(String[] docxLines, String fileName) throws Exception {
		Invoice invoice = new Invoice();

		setLanguage(docxLines[0]);
//		if (lang == null) {
//			setLanguage(docxLines[25]); // Line for BJ IT
//		}
		invoice.setPdfName(fileName);
		ArrayList<String> invoiceAccLinesArray = new ArrayList<String>();

		if (lang == Language.DE || lang == Language.EN) {
			invoiceAccLinesArray = readLinesForLangu1(docxLines, invoice);
		} else {
//			invoiceAccLinesArray = readLinesForLangu1(docxLines, invoice);
		}

		invoice.setAccLines(createAccLines(invoiceAccLinesArray));

		return invoice;
	}

	private static ArrayList<String> readLinesForLangu1(String[] docxLines, Invoice invoice) {
		ArrayList<String> invoiceAccLinesArray = new ArrayList<String>();
		for (String line : docxLines) {
			if (line.contains(invNrText)) {
				invoice.setInvNr(substringAfterText(line, invNrText, 10));
			} else if (line.contains(custText)) {
				invoice.setCustomerNr(substringAfterText(line, custText, 10));
			} else if (line.contains(invDateText)) {
				invoice.setInvDate(substringAfterText(line, invDateText, 10).replace("-", "."));
			} else if (line.contains(serviceDateText)) {
				invoice.setServiceDate(substringAfterText(line, serviceDateText, 10).replace("-", "."));
			} else if (line.length() > 60 && line.substring(49, 59).contains(totalText)) {
				invoicePosition = false;
				invoice.setTotalNetto(line.substring(59, 71).replace(" ", "").replace(".", ""));
				invoice.setTotalTax(line.substring(72, 91).replace(" ", "").replace(".", ""));
				invoice.setTotalGross(line.substring(92, 108).replace(" ", "").replace(".", ""));
			} else if (line.contains(amountText)) {
				invoice.setCurrency(substringAfterText(line, amountText, 3));
			}

			if (invoicePosition) {
				invoiceAccLinesArray.add(line);
			}

			if (line.contains(nettoText)) {
				invoicePosition = true;
			}

			if (line.contains(invCorrectNrText)) {
				invoice.setOriginalCorrNr(substringAfterText(line, invCorrectNrText, 10));
			}
		}
		return invoiceAccLinesArray;
	}

//	private static ArrayList<String> readLinesForLangu2(String[] docxLines, Invoice invoice) {
//		ArrayList<String> invoiceAccLinesArray = new ArrayList<String>();
//		for (String line : docxLines) {
//			if (line.contains(invNrText)) {
//				invoice.setInvNr(substringAfterText(line, invNrText, 10));
//			} else if (line.contains(custText)) {
//				invoice.setCustomerNr(substringAfterText(line, custText, 10));
//			} else if (line.contains(invDateText)) {
//				invoice.setInvDate(substringAfterText(line, invDateText, 10).replace("-", "."));
//			} else if (line.contains(serviceDateText)) {
//				invoice.setServiceDate(substringAfterText(line, serviceDateText, 10).replace("-", "."));
//			} else if (line.length() > 60 && line.substring(49, 59).contains(totalText)) {
//				invoicePosition = false;
//				invoice.setTotalNetto(line.substring(59, 71).replace(" ", "").replace(".", ""));
//				invoice.setTotalTax(line.substring(72, 91).replace(" ", "").replace(".", ""));
//				invoice.setTotalGross(line.substring(92, 108).replace(" ", "").replace(".", ""));
//			} else if (line.contains(amountText)) {
//				invoice.setCurrency(substringAfterText(line, amountText, 3));
//			}
//
//			if (invoicePosition) {
//				invoiceAccLinesArray.add(line);
//			}
//
//			if (line.contains(nettoText)) {
//				invoicePosition = true;
//			}
//
//			if (line.contains(invCorrectNrText)) {
//				invoice.setOriginalCorrNr(substringAfterText(line, invCorrectNrText, 10));
//			}
//		}
//		return invoiceAccLinesArray;
//	}

	private static void setLanguage(String line) {

		if (line.toLowerCase().contains("from to")) {
			lang = Language.EN;
//		} else if (line.toLowerCase().contains("da: a:")){
//			lang = Language.IT;
		} else if (line.toLowerCase().contains("von an")) {
			lang = Language.DE;
		}

		if (lang == Language.EN) {
			invNrText = "INVOICE VAT NR: ";
			custText = "CUSTOMER: ";
			invDateText = "Invoice date: ";
			serviceDateText = "Sales date: ";
			totalText = "Total:";
			amountText = "Total invoice amount (";
			nettoText = "                     w/o VAT";
			invCorrectNrText = "ORG. INVOICE NR: ";
		} else if (lang == Language.IT){
			invNrText = "INVOICE VAT NR: ";
			custText = "CUSTOMER: ";
			invDateText = "Invoice date: ";
			serviceDateText = "Sales date: ";
			totalText = "Total:";
			amountText = "Total invoice amount (";
			nettoText = "                     w/o VAT";
			invCorrectNrText = "ORG. INVOICE NR: ";
		} else {
			invNrText = "Rg.-Nr. ";
			custText = "Kunde ";
			invDateText = "Rechnungsdatum: ";
			serviceDateText = "Leistungsdatum: ";
			totalText = "Gesamt";
			amountText = "Endbetrag (";
			nettoText = "                     netto";
			invCorrectNrText = "ORG. RECHNUNG NR: ";
		}
	}

	private static String substringAfterText(String line, String containText, int howLong) {
		String output = "";
		if (line.contains(containText)) {
			int beginIndex = line.indexOf(containText) + containText.length();
			output = line.substring(beginIndex, beginIndex + howLong);
		}
		return output;
	}
	
//	private static String substringBeforeText(String line, String containText, int howLong) {
//		String output = "";
//		if (line.contains(containText)) {
//			int beginIndex = line.indexOf(containText) - containText.length() - howLong;
//			output = line.substring(beginIndex, beginIndex + howLong);
//		}
//		return output;
//	}

	private static HashMap<Integer, String[]> createAccLines(ArrayList<String> invoiceAccLinesArray) {
		HashMap<Integer, String[]> accLines = new HashMap<Integer, String[]>();

		for (Integer i = 0; i < invoiceAccLinesArray.size(); i++) {
			String[] linePositions = new String[5];
			String line = invoiceAccLinesArray.get(i);
			linePositions[0] = line.substring(49, 59).replace(" ", "");
			linePositions[1] = line.substring(59, 72).replace(" ", "").replace(".", "");
			linePositions[2] = line.substring(72, 79).replace(" ", "");
			linePositions[3] = line.substring(79, 91).replace(" ", "").replace(".", "");
			linePositions[4] = line.substring(91, 108).replace(" ", "").replace(".", "");
			accLines.put(i, linePositions);
		}

		return accLines;
	};

	public enum Language {
		DE, EN, IT
	}
}
