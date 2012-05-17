package timetable.database.gui;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import timetable.database.SubjectDatabase;

/**
 * 
 * @author vaisagh
 */
public class DatabaseApp implements ActionListener {

	public JLabel statusBar = new JLabel();

	JFrame frame;
	private JFileChooser fileChooser;
	private SubjectDatabase localDatabase;

	private DatabasePanel databasePanel;
	private boolean currentFileSaved = true;
	MenuItem save;

	public DatabaseApp() {

		frame = new JFrame("Subject Database Creator");
		frame.setLayout(new GridLayout(1, 2));

		fileChooser = new JFileChooser(new File(".").getAbsolutePath());
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

		MenuBar menuBar = new MenuBar();
		Menu fileMenu = new Menu("File");

		MenuItem newFileMenuOption = new MenuItem("New");
		newFileMenuOption.setActionCommand("new");
		fileMenu.add(newFileMenuOption);
		newFileMenuOption.addActionListener(this);

		save = new MenuItem("Save");
		save.setActionCommand("save");
		fileMenu.add(save);
		save.addActionListener(this);

		MenuItem load = new MenuItem("Load");
		load.setActionCommand("load");
		fileMenu.add(load);
		load.addActionListener(this);

		MenuItem about = new MenuItem("About");
		about.setActionCommand("about");
		fileMenu.add(about);
		about.addActionListener(this);

		MenuItem close = new MenuItem("Close");
		close.setActionCommand("close");
		fileMenu.add(close);
		close.addActionListener(this);

		MenuItem exit = new MenuItem("Exit");
		exit.setActionCommand("exit");
		fileMenu.add(exit);
		exit.addActionListener(this);

		menuBar.add(fileMenu);

		frame.setMenuBar(menuBar);

		statusBar.setForeground(Color.BLUE);

		SubjectDetailPanel subjectDetailPanel = new SubjectDetailPanel(
				localDatabase);
		databasePanel = new DatabasePanel(this, subjectDetailPanel);

		frame.add(databasePanel);
		frame.add(subjectDetailPanel);

		frame.setTitle("Subject Database Creator");
		frame.setSize(610, 450);
		// frame.setLocation(10, 10);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		save.setEnabled(false);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		String trigger = event.getActionCommand();
		if (trigger.equalsIgnoreCase("load")) {
			if (localDatabase == null) {
				loadFile();
				frame.validate();
			} else {
				JOptionPane
						.showMessageDialog(
								frame,
								"Please close current open database before loading another one. ",
								"File already open", JOptionPane.ERROR_MESSAGE);
			}
		} else if (trigger.equalsIgnoreCase("new")) {
			if (localDatabase == null) {
				newFile();
				frame.validate();
			} else {
				JOptionPane
						.showMessageDialog(
								frame,
								"Please close current open database before loading another one. ",
								"File already open", JOptionPane.ERROR_MESSAGE);
			}
		} else if (trigger.equalsIgnoreCase("save")) {
			if (localDatabase != null) {
				saveCurrentFile();
				frame.validate();
			} else {
				JOptionPane.showMessageDialog(frame, "No file open to save",
						"Nothing savable", JOptionPane.ERROR_MESSAGE);
			}
		} else if (trigger.equalsIgnoreCase("close")) {
			if (localDatabase != null) {
				if (!this.currentFileSaved) {
					saveCurrentFile();
				}
				closeCurrentFile();
				frame.validate();

			}
		} else if (trigger.equalsIgnoreCase("exit")) {
			if (localDatabase != null) {
				if (!this.currentFileSaved) {
					saveCurrentFile();
				}
				closeCurrentFile();
				frame.validate();

			}
			frame.dispose();
			System.exit(0);
		} else if (trigger.equalsIgnoreCase("about")) {
			JOptionPane.showMessageDialog(frame,
					"This program facilitates helps manage subject database. ",
					"About", JOptionPane.INFORMATION_MESSAGE);
		}
	}

	private void closeCurrentFile() {
		this.databasePanel.closeOpenFile();
		this.localDatabase = null;
		this.currentFileSaved = true;
		frame.setTitle("Subject Database Creator");
		save.setEnabled(false);
		frame.validate();
	}

	private void saveCurrentFile() {
		int returnVal;

		returnVal = fileChooser.showSaveDialog(frame);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			localDatabase.saveToFile(file);
			frame.setTitle(file.getName());
			this.currentFileSaved = true;
			save.setEnabled(false);
		}

	}

	private void newFile() {
		assert localDatabase == null;
		localDatabase = new SubjectDatabase();
		databasePanel.loadNewDatabase(localDatabase);
		this.currentFileSaved = true;
		save.setEnabled(false);
		frame.setTitle("untitled");

	}

	private void loadFile() {
		assert localDatabase == null;
		File file = null;
		int returnVal;

		returnVal = fileChooser.showOpenDialog(frame);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			file = fileChooser.getSelectedFile();
			localDatabase = new SubjectDatabase(file);
			databasePanel.loadNewDatabase(localDatabase);
			this.currentFileSaved = true;
			save.setEnabled(false);
			frame.setTitle(file.getName());
		}

	}

	public static void main(String[] args) {
		new DatabaseApp();
	}

	public void setToBeSaved() {
		if (frame.getTitle().charAt(frame.getTitle().length() - 1) != '*')
			frame.setTitle(frame.getTitle() + "*");
		this.save.setEnabled(true);
		this.currentFileSaved = false;

	}



}
