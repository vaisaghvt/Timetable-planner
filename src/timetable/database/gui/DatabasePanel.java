package timetable.database.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import timetable.core.Subject;
import timetable.database.SubjectDatabase;

public class DatabasePanel extends JPanel implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private DatabaseApp mainApp;

	private SubjectDetailPanel subjectDetailPanel;

	private SubjectDatabase localDatabase;

	JList subjectList;

	private JPanel buttonPanel;

	JButton addSubject;

	JButton editButton;

	JButton removeSubject;

	JFrame addSubjectFrame;

	public DatabasePanel(DatabaseApp databaseApp,
			SubjectDetailPanel subjectDetailPanel) {
		this.setLayout(new BorderLayout());
		mainApp = databaseApp;
		this.subjectDetailPanel = subjectDetailPanel;
		localDatabase = null;
		subjectList = new JList();
		subjectList
				.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		subjectList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		// subjectList.setVisibleRowCount();
		// subjectList.setPreferredSize(new Dimension(20,400));

		JScrollPane listScroller = new JScrollPane(subjectList);

		listScroller.setMaximumSize(new Dimension(40, 400));

		subjectList.addListSelectionListener(subjectDetailPanel);

		this.add(listScroller);
		buttonPanel = new JPanel(new GridLayout(1, 3));

		addSubject = new JButton("Add");
		addSubject.setActionCommand("add");
		addSubject.addActionListener(this);
		addSubject.setEnabled(false);
		buttonPanel.add(addSubject);

		editButton = new JButton("Edit");
		editButton.setActionCommand("edit");
		editButton.addActionListener(this);
		editButton.setEnabled(false);
		buttonPanel.add(editButton);

		removeSubject = new JButton("Remove");
		removeSubject.setActionCommand("remove");
		removeSubject.addActionListener(this);
		removeSubject.setEnabled(false);
		buttonPanel.add(removeSubject);

		this.add(buttonPanel, BorderLayout.NORTH);
		this.setVisible(true);
		this.subjectDetailPanel.setDatabasePanel(this);

	}

	public void loadNewDatabase(SubjectDatabase localDatabase) {
		this.localDatabase = localDatabase;
		this.subjectDetailPanel.setDatabase(localDatabase);
		addSubject.setEnabled(true);
		updateDisplay();

	}

	void updateDisplay() {
		this.subjectList.setListData(localDatabase.getAllSubjectNames()
				.toArray());
		this.validate();
	}

	public void closeOpenFile() {
		clearDisplay();
		addSubject.setEnabled(false);
		removeSubject.setEnabled(false);
		this.subjectDetailPanel.header.setText("Subject Detail Pane");
		this.localDatabase = null;

	}

	private void clearDisplay() {
		this.subjectDetailPanel.clear();
		this.subjectList.setListData(new String[0]);

	}

	@Override
	public void actionPerformed(ActionEvent event) {
		String trigger = event.getActionCommand();
		int earlySize = this.localDatabase.getAllSubjectNames().size();
		if (trigger.equalsIgnoreCase("edit")) {

			Subject currentSubject = this.subjectDetailPanel
					.getCurrrentSubject();

			if (currentSubject != null) {
				mainApp.setToBeSaved();

				new AddSubjectFrame(this, currentSubject);
				this.subjectDetailPanel.deleteCurrentSubject();
				this.subjectDetailPanel.clear();

			}

		} else if (trigger.equalsIgnoreCase("add")) {
			
			addSubjectFrame = new AddSubjectFrame(this);
			

		} else if (trigger.equalsIgnoreCase("remove")) {
			this.subjectDetailPanel.deleteCurrentSubject();
			this.subjectDetailPanel.clear();
			this.updateDisplay();

			mainApp.setToBeSaved();
		}
		int finalSize = this.localDatabase.getAllSubjectNames().size();
		if (finalSize != earlySize)
			mainApp.setToBeSaved();

	}

	public void addSubject(Subject currentSubject) {
		this.localDatabase.addSubject(currentSubject);
		this.updateDisplay();

	}

}
