package application;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingWorker;

public class ProgressBarDemo extends JPanel {

	/**
	* 
	*/
	private static final long serialVersionUID = 1L;
	private JProgressBar progressBar;
	JButton startButtonFiles;
	JButton startButtonURL;
	private JTextArea taskOutput;
	private Task task;
	private TaskUrl taskUrl;

	public ProgressBarDemo() {
		super(new BorderLayout());

		// Create the demo's UI.
		startButtonFiles = new JButton("Choose files");
		startButtonFiles.setActionCommand("start");
		startButtonFiles.addActionListener(new FilesAction());

		// Create the demo's UI.
		startButtonURL = new JButton("Choose URLs");
		startButtonURL.setActionCommand("start");
		startButtonURL.addActionListener(new URLAction());

		progressBar = new JProgressBar(0, 100);
		progressBar.setValue(0);
		progressBar.setStringPainted(true);

		taskOutput = new JTextArea(5, 20);
		taskOutput.setMargin(new Insets(5, 5, 5, 5));
		taskOutput.setEditable(false);

		JPanel panel = new JPanel();
		panel.add(startButtonFiles);
		panel.add(startButtonURL);
		panel.add(progressBar);

		add(panel, BorderLayout.PAGE_START);
		add(new JScrollPane(taskOutput), BorderLayout.CENTER);
		setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

	}

	class FilesAction implements ActionListener, PropertyChangeListener {

		public void actionPerformed(ActionEvent evt) {
			startButtonFiles.setEnabled(false);
			startButtonURL.setEnabled(false);
			setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			// Instances of javax.swing.SwingWorker are not reusuable, so
			// we create new instances as needed.
			task = new Task();
			task.addPropertyChangeListener(this);
			task.execute();
		}

		/**
		 * Invoked when task's progress property changes.
		 */
		public void propertyChange(PropertyChangeEvent evt) {
			if ("progress" == evt.getPropertyName()) {
				int progress = (Integer) evt.getNewValue();
				progressBar.setValue(progress);

			}
			if ("state".equals(evt.getPropertyName()) && (SwingWorker.StateValue.DONE.equals(evt.getNewValue()))) {
				startButtonFiles.setEnabled(true);
				startButtonURL.setEnabled(true);
				taskOutput.append("Done!\n");
				setCursor(null); // turn off the wait cursor
			}
		}
	}

	class URLAction implements ActionListener, PropertyChangeListener {

		public void actionPerformed(ActionEvent evt) {
			startButtonFiles.setEnabled(false);
			startButtonURL.setEnabled(false);
			setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			// Instances of javax.swing.SwingWorker are not reusuable, so
			// we create new instances as needed.
			taskUrl = new TaskUrl();
			taskUrl.addPropertyChangeListener(this);
			taskUrl.execute();
		}

		/**
		 * Invoked when task's progress property changes.
		 */
		public void propertyChange(PropertyChangeEvent evt) {
			if ("progress" == evt.getPropertyName()) {
				int progress = (Integer) evt.getNewValue();
				progressBar.setValue(progress);

			}
			if ("state".equals(evt.getPropertyName()) && (SwingWorker.StateValue.DONE.equals(evt.getNewValue()))) {
				startButtonFiles.setEnabled(true);
				startButtonURL.setEnabled(true);
				taskOutput.append("Done!\n");
				setCursor(null); // turn off the wait cursor
			}
		}
	}

}
