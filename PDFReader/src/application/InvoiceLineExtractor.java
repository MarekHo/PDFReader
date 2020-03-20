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

		invoice.setPdfName(fileName);
		ArrayList<String> invoiceAccLinesArray = new ArrayList<String>();

		if (lang == Language.DE_SELF) {
			invoiceAccLinesArray = readLinesForSelf(docxLines, invoice);
			invoice.setAccLines(createAccLinesSelf(invoiceAccLinesArray));
		} else {
			invoiceAccLinesArray = readLinesForLangu1(docxLines, invoice);
			invoice.setAccLines(createAccLines(invoiceAccLinesArray));
		}

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

	private static ArrayList<String> readLinesForSelf(String[] docxLines, Invoice invoice) {
		ArrayList<String> invoiceAccLinesArray = new ArrayList<String>();
		for (String line : docxLines) {
			if (line.contains(invNrText)) {
				invoice.setInvNr(substringAfterText(line, invNrText, 16));
			} else if (line.contains(invDateText)) {
				invoice.setInvDate(substringAfterText(line, invDateText, 10).replace("-", "."));
			} else if (line.contains(serviceDateText)) {
				invoice.setServiceDate(substringAfterText(line, serviceDateText, 10).replace("-", "."));
			} else if (line.contains(totalText)) {
				invoicePosition = false;
				invoice.setTotalGross(line.substring(line.indexOf(totalText) + totalText.length(), line.length() - 4)
						.replace(" ", "").replace(".", ""));
				invoice.setCurrency(line.substring(line.length() - 3, line.length()));
			}

			if (invoicePosition) {
				invoiceAccLinesArray.add(line);
			}

			if (line.contains(nettoText)) {
				invoicePosition = true;
			}

		}
		return invoiceAccLinesArray;
	}

	private static void setLanguage(String line) {

		if (line.toLowerCase().contains("from to")) {
			lang = Language.EN_BJ;
		} else if (line.toLowerCase().contains("von an")) {
			lang = Language.DE_BJ;
		} else if (line.toLowerCase().contains("raben trans european germany gmbh")) {
			lang = Language.DE_SELF;
		}

		if (lang == Language.EN_BJ) {
			invNrText = "INVOICE VAT NR: ";
			custText = "CUSTOMER: ";
			invDateText = "Invoice date: ";
			serviceDateText = "Sales date: ";
			totalText = "Total:";
			amountText = "Total invoice amount (";
			nettoText = "                     w/o VAT";
			invCorrectNrText = "ORG. INVOICE NR: ";
		} else if (lang == Language.DE_SELF) {
			invNrText = "Gutschriftsnummer: ";
			custText = "NONE!";
			invDateText = "Belegdatum ";
			serviceDateText = "Leistungsdatum ";
			totalText = "ENDBETRAG ZU IHREN GUNSTEN /(ZU IHREN LASTEN)";
			amountText = "ENDBETRAG ZU IHREN GUNSTEN /(ZU IHREN LASTEN)";
			nettoText = "Betrag netto MwSt.-Satz MwSt. Betrag Betrag brutto";
			invCorrectNrText = "NONE!";
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

	private static HashMap<Integer, String[]> createAccLinesSelf(ArrayList<String> invoiceAccLinesArray) {
		HashMap<Integer, String[]> accLines = new HashMap<Integer, String[]>();

		for (Integer i = 0; i < invoiceAccLinesArray.size(); i++) {
			String[] linePositions = new String[5];
			String line = invoiceAccLinesArray.get(i);
			linePositions[0] = "ND";
			linePositions[1] = line.substring(0, line.indexOf(" ")).replace(" ", "");
			line = line.substring(line.indexOf(" ") + 1);
			if (line.indexOf("%") == -1) {
				linePositions[2] = line.substring(0, line.indexOf(" ")).replace(" ", "");
				line = line.substring(line.indexOf(" ") + 1);
			} else {
				linePositions[2] = line.substring(0, line.indexOf("%")+1).replace(" ", "");
				line = line.substring(line.indexOf("%") + 2);
			}
			linePositions[3] = line.substring(0, line.indexOf(" ")).replace(" ", "");
			line = line.substring(line.indexOf(" ") + 1);
			linePositions[4] = line.replace(" ", "");
			accLines.put(i, linePositions);
		}

		return accLines;
	};

	public enum Language {
		DE_BJ, EN_BJ, IT, DE_SELF, EN_SELF,
	}
}
