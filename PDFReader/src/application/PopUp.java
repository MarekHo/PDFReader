package application;

import java.awt.FileDialog;
import java.awt.Frame;
import java.io.File;

import javax.swing.JOptionPane;

public class PopUp {

	public static void infoBox(String infoMessage, String titleBar) {
		JOptionPane.showMessageDialog(null, infoMessage, "InfoBox: " + titleBar, JOptionPane.INFORMATION_MESSAGE);
	}

	public static File[] fileDialog(Boolean multipleMode, String fileExtension) {
		
		FileDialog dialog = new FileDialog((Frame) null, "Select Files to Open");	
		dialog.setMultipleMode(multipleMode);
		dialog.setFile("*." + fileExtension + ";");
		dialog.setMode(FileDialog.LOAD);
		dialog.setVisible(true);
		File[] selectedFiles = dialog.getFiles();
		return selectedFiles;
	}
}