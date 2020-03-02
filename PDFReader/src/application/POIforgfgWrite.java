package application;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class POIforgfgWrite {

	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd_HH.mm.ss");

	public static void createXLSX(ArrayList<Invoice> invoices) {

		Map<String, Object[]> dataToExcel = new TreeMap<String, Object[]>();
		dataToExcel.put("1",
				new Object[] { "PDF file name", "Inv nr", "Original correct nr", "Customer", "Inv date", "Service date",
						"Total netto", "Total tax", "Total gross", "End currency", "Line item", "Line currency",
						"Line netto", "Line tax rate", "Line tax", "Line gross" });
		int j = 2;
		for (Integer i = 0; i < invoices.size(); i++) {
			Map<Integer, Object[]> wholeInvLines = invoices.get(i).getWholeInvLines();
			if (wholeInvLines.size() != 0) {
				for (int k = 0; k < wholeInvLines.size(); k++) {
					dataToExcel.put(Integer.toString(j), wholeInvLines.get(k));
					j++;
				}
			} else {
				Object[] noInvoiceFile = new Object[] { invoices.get(i).getPdfName(), "Not found" };
				dataToExcel.put(Integer.toString(j), noInvoiceFile);
				j++;
			}
		}
		createAndSaveXLSX(dataToExcel);
	}

	private static void createAndSaveXLSX(Map<String, Object[]> data) {
		// Blank workbook
		@SuppressWarnings("resource")
		XSSFWorkbook workbook = new XSSFWorkbook();

		// Create a blank sheet
		XSSFSheet sheet = workbook.createSheet("PDF invoices");

		// This data needs to be written (Object[])

		// Iterate over data and write to sheet
		Set<String> keyset = data.keySet();
		int rownum = 0;
		for (String key : keyset) {
			// this creates a new row in the sheet
			Row row = sheet.createRow(rownum++);
			Object[] objArr = data.get(key);
			int cellnum = 0;
			for (Object obj : objArr) {
				// this line creates a cell in the next column of that row
				Cell cell = row.createCell(cellnum++);
				if (obj instanceof String)
					cell.setCellValue((String) obj);
				else if (obj instanceof Integer)
					cell.setCellValue((Integer) obj);
			}
		}
		try {
			// this Writes the workbook
			Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			String outputFileName = "PDF to Excel " + sdf.format(timestamp) + ".xlsx";
			FileOutputStream out = new FileOutputStream(new File(outputFileName));
			workbook.write(out);
			out.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	};

}
