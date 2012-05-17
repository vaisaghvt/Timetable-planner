package timetable.database.gui;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import timetable.core.Subject;
import timetable.database.SubjectDatabase;

public class SubjectDetailPanel extends JPanel implements ListSelectionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Subject currentSubject;

	private SubjectDatabase subjectDatabase;

	private DatabasePanel databasePanel = null;

	private JTextArea innerTextField;
	
	JLabel header;

	public SubjectDetailPanel(SubjectDatabase database) {
		currentSubject = null;
		this.subjectDatabase = database;

		innerTextField = new JTextArea();
		innerTextField.setEditable(false);
		innerTextField.setLineWrap(true);
		innerTextField.setWrapStyleWord(true);
		this.add(innerTextField);

		JScrollPane listScroller = new JScrollPane(innerTextField);
		//listScroller.setPreferredSize(new Dimension(300, 400));
		
		this.setLayout(new BorderLayout());
		this.add(listScroller);
header = new JLabel();
		this.setDefaultHeader();

		this.add(header, BorderLayout.NORTH);
		
		// this.setPreferredSize(new Dimension(450, 400));
		this.setVisible(true);
	}

	/**
	 * Set the value of the current Subject
	 */
	public void clear() {
		this.innerTextField.setText("");
		currentSubject = null;
		this.databasePanel.removeSubject.setEnabled(false);
	}

	@Override
	public void valueChanged(ListSelectionEvent event) {
		boolean flag = false;
		for (int i = 0; i < subjectDatabase.getAllSubjects().size(); i++) {
			if (this.databasePanel.subjectList.isSelectedIndex(i)) {
				String subjectName = subjectDatabase.getAllSubjectNames().get(i);

				this.currentSubject = subjectDatabase
						.getSubjectForName(subjectName);
				this.updateDisplay();
				this.databasePanel.removeSubject.setEnabled(true);
				this.databasePanel.editButton.setEnabled(true);
				flag = true;
			}

		}
		if(!flag){
			this.databasePanel.removeSubject.setEnabled(false);
			this.databasePanel.editButton.setEnabled(false);
		}

	}

	private void updateDisplay() {
		this.innerTextField.setText(this.currentSubject.getFormattedDetails());
		this.header.setText(currentSubject.getName());
		this.validate();

	}

	public void setDatabasePanel(DatabasePanel dbp) {
		this.databasePanel = dbp;
	}

	public void deleteCurrentSubject() {
		this.subjectDatabase.remove(currentSubject);
		this.setDefaultHeader();

		this.validate();

	}

	public void setDatabase(SubjectDatabase database) {
		this.subjectDatabase = database;
	}

	public Subject getCurrrentSubject() {
		// TODO Auto-generated method stub
		return this.currentSubject;
	}

	public void setDefaultHeader() {
		// TODO Auto-generated method stub
		
		this.header.setText("Subject Detail Pane");
	}

}
