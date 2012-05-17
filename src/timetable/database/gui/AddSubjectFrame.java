package timetable.database.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.StringTokenizer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.joda.time.IllegalFieldValueException;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;

import timetable.core.Index;
import timetable.core.Subject;
import timetable.core.TimeSlot;
import timetable.core.TimeSlot.SessionType;

public class AddSubjectFrame extends JFrame implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	DatabasePanel databasePanel;
	JButton createButton;
	JButton cancelButton;

	JPanel buttonPanel;
	JTextField name;
	JTextField category;
	JTextField code;
	JTextField preRequisites;
	JTextField examDate;
	JTextField startTime;
	JTextField endTime;
	JTextArea description;
	JLabel indexNames;

	Set<Index> indices;
	Subject orinigalSubject = null;

	public AddSubjectFrame(DatabasePanel databasePanel) {
		this.databasePanel = databasePanel;
		indices = new HashSet<Index>();
		name = new JTextField(40);
		name.setText("Default");

		code = new JTextField(100);

		category = new JTextField(40);
		

		preRequisites = new JTextField(100);
		preRequisites.setToolTipText("Seperate by commas");

		examDate = new JTextField(40);
		examDate.setText(LocalDate.now().toString(
				DateTimeFormat.forPattern("dd/MM/yyyy")));

		startTime = new JTextField(40);
		endTime = new JTextField(40);
		startTime.setText(LocalTime.now().toString(
				DateTimeFormat.forPattern("HH:mm")));
		endTime.setText(LocalTime.now().plusHours(1)
				.toString(DateTimeFormat.forPattern("HH:mm")));

		description = new JTextArea(2, 15);

		description.setLineWrap(true);
		description.setWrapStyleWord(true);

		indexNames = new JLabel();
		JScrollPane listScroller = new JScrollPane(description);

		buttonPanel = new JPanel();

		createButton = new JButton("Done");
		createButton.setActionCommand("create");
		createButton.addActionListener(this);
		buttonPanel.add(createButton);

		cancelButton = new JButton("Cancel");
		cancelButton.setActionCommand("cancel");
		cancelButton.addActionListener(this);
		buttonPanel.add(cancelButton);

		this.setTitle("Subject Description Area");
		this.setLayout(new BorderLayout());

		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(10, 2));

		panel.add(new JLabel(" Name  :"));
		panel.add(name);

		panel.add(new JLabel(" Description  :"));
		panel.add(listScroller);

		panel.add(new JLabel(" Category :"));
		panel.add(category);
		
		panel.add(new JLabel(" Code  :"));
		panel.add(code);

		panel.add(new JLabel(" Pre-requisites  :"));
		panel.add(preRequisites);

		panel.add(new JLabel(" Exam Date  :"));
		panel.add(examDate);

		panel.add(new JLabel(" Start Time  :"));
		panel.add(startTime);

		panel.add(new JLabel(" End Time  :"));
		panel.add(endTime);

		panel.add(new JLabel(" Indices :"));
		panel.add(indexNames);

		JButton addIndexButton = new JButton("Add Index");
		addIndexButton.setActionCommand("addIndex");
		addIndexButton.addActionListener(this);

		JButton removeIndexButton = new JButton("Remove Index");
		removeIndexButton.setActionCommand("removeIndex");
		removeIndexButton.addActionListener(this);

		panel.add(addIndexButton);
		panel.add(removeIndexButton);

		this.add(panel, BorderLayout.NORTH);
		this.add(buttonPanel, BorderLayout.SOUTH);

		this.setResizable(false);
		this.setSize(340, 400);
		this.setLocation(900, 200);
		buttonPanel.setVisible(true);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.setAlwaysOnTop(true);
		this.validate();
	}

	public AddSubjectFrame(DatabasePanel databasePanel, Subject currentSubject) {
		this.databasePanel = databasePanel;
		this.orinigalSubject = currentSubject;
		indices = new HashSet<Index>();
		name = new JTextField(40);
		name.setText(currentSubject.getName());
		
		category = new JTextField(40);
		category.setText(currentSubject.getCategory());

		code = new JTextField(40);
		code.setText(currentSubject.getCode());

		preRequisites = new JTextField(100);
		preRequisites.setToolTipText("Seperate by commas");
		String result = "";
		int count = 0;
		for (String preReq : currentSubject.getPreRequisites()) {

			result = result + preReq;
			if (count != currentSubject.getPreRequisites().size() - 1) {

				result = result + ", ";
			}
			count++;
		}
		this.preRequisites.setText(result);

		examDate = new JTextField(40);

		startTime = new JTextField(40);
		endTime = new JTextField(40);

		if (currentSubject.hasExam()) {
			examDate.setText(currentSubject.getExaminationDateTime().getDate()
					.toString(DateTimeFormat.forPattern("dd-MM-yyyy")));
			startTime.setText(currentSubject.getExaminationDateTime()
					.getStartTime()
					.toString(DateTimeFormat.forPattern("HH:mm")));
			endTime.setText(currentSubject.getExaminationDateTime()
					.getEndTime().toString(DateTimeFormat.forPattern("HH:mm")));
		}
		description = new JTextArea(2, 15);

		description.setLineWrap(true);
		description.setWrapStyleWord(true);
		description.setText(currentSubject.getDescription());

		indexNames = new JLabel();
		result = "";
		count = 0;
		for (Index index : currentSubject.getIndices()) {

			result = result + index.getIndexCode();
			if (count != currentSubject.getIndices().size() - 1) {

				result = result + ", ";
			}
			count++;
		}
		this.indexNames.setText(result);
		this.indices = currentSubject.getIndices();

		JScrollPane listScroller = new JScrollPane(description);

		buttonPanel = new JPanel();

		createButton = new JButton("Done");
		createButton.setActionCommand("create");
		createButton.addActionListener(this);
		buttonPanel.add(createButton);

		cancelButton = new JButton("Cancel");
		cancelButton.setActionCommand("cancel");
		cancelButton.addActionListener(this);
		buttonPanel.add(cancelButton);

		this.setTitle("Subject Description Area");
		this.setLayout(new BorderLayout());

		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(9, 2));

		panel.add(new JLabel(" Name  :"));
		panel.add(name);

		panel.add(new JLabel(" Description  :"));
		panel.add(listScroller);
		
		panel.add(new JLabel(" Category  :"));
		panel.add(category);

		panel.add(new JLabel(" Codes  :"));
		panel.add(code);

		panel.add(new JLabel(" Pre-requisites  :"));
		panel.add(preRequisites);

		panel.add(new JLabel(" Exam Date  :"));
		panel.add(examDate);

		panel.add(new JLabel(" Start Time  :"));
		panel.add(startTime);

		panel.add(new JLabel(" End Time  :"));
		panel.add(endTime);

		panel.add(new JLabel(" Indices :"));
		panel.add(indexNames);

		JButton addIndexButton = new JButton("Add Index");
		addIndexButton.setActionCommand("addIndex");
		addIndexButton.addActionListener(this);

		JButton removeIndexButton = new JButton("Remove Index");
		removeIndexButton.setActionCommand("removeIndex");
		removeIndexButton.addActionListener(this);

		panel.add(addIndexButton);
		panel.add(removeIndexButton);

		this.add(panel, BorderLayout.NORTH);
		this.add(buttonPanel, BorderLayout.SOUTH);

		this.setResizable(false);
		this.setSize(340, 400);
		this.setLocation(900, 200);
		buttonPanel.setVisible(true);
		this.setVisible(true);
		this.setAlwaysOnTop(true);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.validate();
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		String trigger = event.getActionCommand();
		if (trigger.equalsIgnoreCase("create")) {

			if (this.name.getText().isEmpty()) {
				JOptionPane.showMessageDialog(this, "Please enter a name",
						"Name needed", JOptionPane.ERROR_MESSAGE);
				return;
			}
			if (this.description.getText().isEmpty()) {
				JOptionPane.showMessageDialog(this,
						"Please enter a description", "Description needed",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			if (this.category.getText().isEmpty()) {
				JOptionPane.showMessageDialog(this,
						"Please enter a category", "Category needed",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			if (this.code.getText().isEmpty()) {
				JOptionPane.showMessageDialog(this,
						"At least one subject code needed", "Code needed",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			if (!examValid()) {
				JOptionPane.showMessageDialog(this, "Exam Date invalid",
						"Code needed", JOptionPane.ERROR_MESSAGE);
				return;
			}
			if (this.indices.isEmpty()) {
				JOptionPane.showMessageDialog(this,
						"At least one index needed per subject!", "No Index!",
						JOptionPane.ERROR_MESSAGE);
				return;
			}

			
			
			
			StringTokenizer st = new StringTokenizer(
					this.preRequisites.getText(), ",");
			Set<String> preReqSet = new HashSet<String>();
			while (st.hasMoreTokens()) {
				preReqSet.add(st.nextToken());
			}
			
			TimeSlot examDateSlot = null;
			if (!examDate.getText().isEmpty()) {
				Scanner sc = new Scanner(this.examDate.getText())
						.useDelimiter("-");
				int day = sc.nextInt();
				int month = sc.nextInt();
				int year = sc.nextInt();

				sc = new Scanner(this.startTime.getText()).useDelimiter(":");
				int startHour = sc.nextInt();
				int startMinute = sc.nextInt();

				sc = new Scanner(this.endTime.getText()).useDelimiter(":");
				int endHour = sc.nextInt();
				int endMinute = sc.nextInt();

				examDateSlot = new TimeSlot(new LocalDate(year, month, day),
						new LocalTime(startHour, startMinute), new LocalTime(
								endHour, endMinute), SessionType.EXAM, new String());
			}
			Subject currentSubject = new Subject(name.getText(),
					description.getText(),category.getText(), code.getText(), preReqSet, indices,
					examDateSlot);
			

			this.databasePanel.addSubject(currentSubject);
			this.databasePanel.updateDisplay();
			this.dispose();
		} else if (trigger.equalsIgnoreCase("cancel")) {
			this.dispose();
		} else if (trigger.equalsIgnoreCase("addIndex")) {

			new AddIndexFrame(this);

		} else if (trigger.equalsIgnoreCase("removeIndex")) {
			if (!indices.isEmpty()) {
				String[] possibilities = new String[indices.size()];
				int i = 0;
				for (Index index : indices) {
					possibilities[i] = index.getIndexCode();
					i++;
				}
				String indexToBeDeletedString = (String) JOptionPane
						.showInputDialog(this, "Which Index is to be removed?",
								"Remove Index Frame",
								JOptionPane.PLAIN_MESSAGE, null, possibilities,
								possibilities[0]);
				if (indexToBeDeletedString == null) {
					return;
				}

				Index indexToBeDeleted = getIndexForCode(indexToBeDeletedString);
				indices.remove(indexToBeDeleted);
			}
		}

	}

	private boolean examValid() {
		if (this.examDate.getText().isEmpty()
				&& this.startTime.getText().isEmpty()
				&& this.endTime.getText().isEmpty()) {
			System.out.println("here");
			return true;
		}
		if (!this.examDate.getText().isEmpty()
				&& !this.startTime.getText().isEmpty()
				&& !this.endTime.getText().isEmpty()) {
			StringTokenizer st = new StringTokenizer(this.examDate.getText(),
					"-");
			if (st.countTokens() != 3) {
				return false;
			} else {
				try {
					int day = Integer.parseInt(st.nextToken());
					if (day < 1 || day > 31) {
						return false;
					}
					int month = Integer.parseInt(st.nextToken());
					if (month < 1 || month > 12) {
						return false;
					}
					int year = Integer.parseInt(st.nextToken());
					if (year < 2000 || year > 3000) {
						return false;
					}

					LocalDate examDate = new LocalDate(year, month, day);
					if (examDate.isBefore(LocalDate.now())) {
						return false;
					}

				} catch (NumberFormatException nf) {
					return false;
				} catch (IllegalFieldValueException ifv) {
					return false;
				}
			}
			st = new StringTokenizer(this.startTime.getText(), ":");
			LocalTime startTime = null;
			LocalTime endTime = null;
			if (st.countTokens() != 2) {
				return false;
			} else {
				try {
					int hour = Integer.parseInt(st.nextToken());
					if (hour < 0 || hour > 23) {
						return false;
					}
					int minute = Integer.parseInt(st.nextToken());
					if (minute < 0 || minute > 59) {
						return false;
					}
					startTime = new LocalTime(hour, minute);
				} catch (NumberFormatException nf) {
					return false;
				}
			}
			st = new StringTokenizer(this.endTime.getText(), ":");
			if (st.countTokens() != 2) {
				return false;
			} else {
				try {
					int hour = Integer.parseInt(st.nextToken());
					if (hour < 0 || hour > 23) {
						return false;
					}
					int minute = Integer.parseInt(st.nextToken());
					if (minute < 0 || minute > 59) {
						return false;
					}
					endTime = new LocalTime(hour, minute);
					if (endTime.isBefore(startTime)) {
						return false;
					}
				} catch (NumberFormatException nf) {
					return false;
				}
			}
			return true;

		}
		return false;
	}

	private Index getIndexForCode(String indexToBeDeletedString) {
		Index result = null;
		for (Index index : indices) {
			if (indexToBeDeletedString.equalsIgnoreCase(index.getIndexCode())) {
				result = index;
				break;
			}
		}
		return result;
	}

	public void addIndex(Index currentIndex) {
		// TODO Auto-generated method stub
		this.indices.add(currentIndex);
		String indexList = "";
		for (Index index : this.indices) {
			indexList = indexList + index.getIndexCode();
		}
		this.indexNames.setText(indexList);
		this.validate();
	}

}
