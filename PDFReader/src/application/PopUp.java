package application;

import java.awt.FileDialog;
import java.awt.Frame;
import java.io.File;

import javax.swing.JOptionPane;

public class PopUp {

	public static void infoBox(String infoMessage, String titleBar) {
		JOptionPane.showMessageDialog(null, infoMessage, "InfoBox: " + titleBar, JOptionPane.INFORMATION_MESSAGE);
	}

	public static File[] fileDialog() {
		
		FileDialog dialog = new FileDialog((Frame) null, "Select Files to Open");	
		dialog.setMultipleMode(true);
		dialog.setFile("*.pdf;");
		dialog.setMode(FileDialog.LOAD);
		dialog.setVisible(true);
		File[] selectedFiles = dialog.getFiles();
		return selectedFiles;
	}
	
//	public static File[] fileDialog() {
//		JFileChooser jFileChooser = new JFileChooser();
//		jFileChooser.setMultiSelectionEnabled(true);
//		jFileChooser.addChoosableFileFilter(new FileNameExtensionFilter("PDF Documents", "pdf"));
//        int result = jFileChooser.showOpenDialog(new JFrame());
//        File[] selectedFiles = null;
//        
//        if (result == JFileChooser.APPROVE_OPTION) {
//            selectedFiles = jFileChooser.getSelectedFiles();
//        }
//		
//
//		return selectedFiles;
//	}
}