package application;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

public class Invoice {

	private String pdfName;
	private String invNr;
	private String originalCorrNr;
	private String customerNr;
	private String invDate;
	private String serviceDate;
	private String totalNetto;
	private String totalTax;
	private String totalGross;
	private String currency;
	private String paymentAmount;

	private HashMap<Integer, String[]> accLines = new HashMap<Integer, String[]>();

	public String getPdfName() {
		return pdfName;
	}

	public void setPdfName(String pdfName) {
		this.pdfName = pdfName;
	}

	public String getInvNr() {
		return invNr;
	}

	public void setInvNr(String invNr) {
		this.invNr = invNr;
	}

	public String getOriginalCorrNr() {
		return originalCorrNr;
	}

	public void setOriginalCorrNr(String originalCorrNr) {
		this.originalCorrNr = originalCorrNr;
	}

	public String getCustomerNr() {
		return customerNr;
	}

	public void setCustomerNr(String customerNr) {
		this.customerNr = customerNr;
	}

	public String getInvDate() {
		return invDate;
	}

	public void setInvDate(String invDate) {
		this.invDate = invDate;
	}

	public String getServiceDate() {
		return serviceDate;
	}

	public void setServiceDate(String serviceDate) {
		this.serviceDate = serviceDate;
	}

	public String getTotalNetto() {
		return totalNetto;
	}

	public void setTotalNetto(String totalNetto) {
		this.totalNetto = totalNetto;
	}

	public String getTotalTax() {
		return totalTax;
	}

	public void setTotalTax(String totalTax) {
		this.totalTax = totalTax;
	}

	public String getTotalGross() {
		return totalGross;
	}

	public void setTotalGross(String totalGross) {
		this.totalGross = totalGross;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}
	
	public String getPaymentAmount() {
		return paymentAmount;
	}

	public void setPaymentAmount(String paymentAmount) {
		this.paymentAmount = paymentAmount;
	}

	public HashMap<Integer, String[]> getAccLines() {
		return accLines;
	}

	public void setAccLines(HashMap<Integer, String[]> accLines) {
		this.accLines = accLines;
	}

	public Map<Integer, Object[]> getWholeInvLines() {
		Map<Integer, Object[]> wholeInvLines = new TreeMap<Integer, Object[]>();
//		Object[] wholeInvLines = new Object[accLines.size()];
		@SuppressWarnings("rawtypes")
		Iterator it = accLines.entrySet().iterator();
		int i = 0;
		while (it.hasNext()) {
			@SuppressWarnings({ "rawtypes", "unused" })
			Map.Entry pair = (Map.Entry) it.next();
			String[] strArray = (String[]) accLines.get(i);
			wholeInvLines.put(i,
					new Object[] {pdfName, invNr, originalCorrNr, customerNr, invDate, serviceDate, totalNetto, totalTax,
							totalGross, currency, paymentAmount, i + 1, strArray[0], strArray[1], strArray[2], strArray[3],
							strArray[4] });
			i++;
			it.remove(); // avoids a ConcurrentModificationException
		}

		return wholeInvLines;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public String toString() {
		String invoiceDetails = "PDF file name: " +pdfName;
		invoiceDetails = invoiceDetails + "\nInvoice number: " + invNr;
		invoiceDetails = invoiceDetails + "\nOriginal correct number: " + originalCorrNr;
		invoiceDetails = invoiceDetails + "\nCustomer number: " + customerNr;
		invoiceDetails = invoiceDetails + "\nInvoice date: " + invDate;
		invoiceDetails = invoiceDetails + "\nService date: " + serviceDate;
		invoiceDetails = invoiceDetails + "\nTotal netto: " + totalNetto;
		invoiceDetails = invoiceDetails + "\nTotal tax: " + totalTax;
		invoiceDetails = invoiceDetails + "\nTotal gross: " + totalGross;
		invoiceDetails = invoiceDetails + "\nCurrency: " + currency;
		invoiceDetails = invoiceDetails + "\nTotal payment: " + paymentAmount;

		Iterator it = accLines.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry) it.next();
			String lineItems = convertArrayToStringMethod((String[]) pair.getValue());
			invoiceDetails = invoiceDetails + "\n Line " + pair.getKey() + " " + lineItems;
			it.remove(); // avoids a ConcurrentModificationException
		}

		return invoiceDetails;
	}

	public static String convertArrayToStringMethod(String[] strArray) {
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < strArray.length; i++) {
			stringBuilder.append(" " + strArray[i]);
		}
		return stringBuilder.toString();
	}
}
