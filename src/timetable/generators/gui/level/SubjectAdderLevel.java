package timetable.generators.gui.level;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import timetable.core.Subject;
import timetable.generators.Generator;
import timetable.generators.gui.RegistrationDetails;

public class SubjectAdderLevel extends AbstractLevel implements
		ListSelectionListener, ActionListener {

	JPanel topPanel;
	JPanel bottomPanel;
	JPanel leftTopPanel;
	Set<String> interestedSubjects;
	Set<String> availableSubjects;
	Generator generator;
	private JPanel mainContentPane;
	private JList availableSubjectList;
	private JList chosenSubjectList;
	private JPanel centerButtonPanel;
	private JButton addSubject;
	private JButton removeSubject;
	private Set<Subject> subjectToBeAdded;
	private Set<Subject> subjectToBeRemoved;
	private JTextArea selectedSubjectDescription;

	public SubjectAdderLevel(RegistrationDetails details, JFrame frame,
			JPanel buttonArea) {
		super(details, frame, buttonArea);

		this.generator = details.getGenerator();
		interestedSubjects = new HashSet<String>();
		availableSubjects = new HashSet<String>(details.getSubjectDatabase()
				.getAllSubjectNames());

		subjectToBeAdded = new HashSet<Subject>();
		subjectToBeRemoved = new HashSet<Subject>();

	}

	@Override
	public void setUpLevel() {
		this.generator = details.getGenerator();
		interestedSubjects = new HashSet<String>();
		
		Set<Subject> alreadyInterestedSubjects = new HashSet<Subject>(generator.getInterestedSubjects());
		for(Subject subject: alreadyInterestedSubjects){
			System.out.println("here");
			interestedSubjects.add(subject.getCode()+"\t"+subject.getName());
		}
		
		
		availableSubjects = new HashSet<String>(details.getSubjectDatabase()
				.getAllSubjectNamesWithCodes());
		availableSubjects.removeAll(interestedSubjects);
		
		

		mainContentPane = new JPanel(new BorderLayout());
		
		topPanel = new JPanel(new GridLayout(1, 3));
		bottomPanel = new JPanel();
		leftTopPanel = new JPanel(new BorderLayout());

//		this.previousButton.setEnabled(false);
		this.nextButton.setText("Plan");
		this.nextButton.setActionCommand("next");
		this.nextButton.setEnabled(true);
//		this.previousButton.setVisible(false);

		List<String> categories = new ArrayList<String>(details
				.getSubjectDatabase().getCategories());
		categories.add(0, "All");

		JComboBox categoryChoice = new JComboBox(categories.toArray());
		categoryChoice.setSelectedIndex(0);
		categoryChoice.setActionCommand("categoryChoice");
		categoryChoice.addActionListener(this);
		leftTopPanel.add(categoryChoice, BorderLayout.NORTH);

		availableSubjectList = new JList();
		availableSubjectList
				.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		availableSubjectList.setListData(this.availableSubjects.toArray());

		availableSubjectList.setLayoutOrientation(JList.VERTICAL);
		availableSubjectList.setVisibleRowCount(-1);
		JScrollPane listScroller = new JScrollPane(availableSubjectList);
		// listScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		availableSubjectList.addListSelectionListener(this);
		leftTopPanel.add(listScroller, BorderLayout.CENTER);
		topPanel.add(leftTopPanel);

		centerButtonPanel = new JPanel(new GridLayout(9, 1));
		for (int i = 0; i < 3; i++) {
			centerButtonPanel.add(new JLabel());
		}

		addSubject = new JButton("Add ->");
		addSubject.setActionCommand("add");
		addSubject.addActionListener(this);
		addSubject.setEnabled(false);
		centerButtonPanel.add(addSubject);
		centerButtonPanel.add(new JLabel());

		removeSubject = new JButton("<- Remove");
		removeSubject.setActionCommand("remove");
		removeSubject.addActionListener(this);
		removeSubject.setEnabled(false);
		centerButtonPanel.add(removeSubject);

		for (int i = 0; i < 3; i++) {
			centerButtonPanel.add(new JLabel());
		}
		topPanel.add(centerButtonPanel);

		chosenSubjectList = new JList();
		chosenSubjectList
				.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		chosenSubjectList.setLayoutOrientation(JList.VERTICAL);
		chosenSubjectList.setVisibleRowCount(-1);
		chosenSubjectList.setListData(this.interestedSubjects.toArray());
		JScrollPane secondListScroller = new JScrollPane(chosenSubjectList);
		

		chosenSubjectList.addListSelectionListener(this);
		topPanel.add(secondListScroller);

		selectedSubjectDescription = new JTextArea(20, 72);

		selectedSubjectDescription.setLineWrap(true);
		selectedSubjectDescription.setWrapStyleWord(true);
		selectedSubjectDescription.setEditable(false);
		selectedSubjectDescription.setMargin(new Insets(5, 15, 0, 0));
		// selectedSubjectDescription.setEnabled(false);

		// timeSlotsDescription.setMinimumSize(new Dimension(40, 200));

		JScrollPane timeSlotsDescriptionScroll = new JScrollPane(
				selectedSubjectDescription);
		bottomPanel.add(timeSlotsDescriptionScroll);

		mainContentPane.add(topPanel, BorderLayout.NORTH);
		mainContentPane.add(bottomPanel, BorderLayout.SOUTH);
		frame.add(mainContentPane);

		Set<Subject> tempSet = new HashSet<Subject>(
				generator.getInterestedSubjects());
		for (Subject subject : tempSet) {
			generator.removeInterestedSubject(subject);
			this.addSubject(subject);
		}
	}

	@Override
	public void clearUp() {
		// System.out.println(generator.getInterestedSubjects());
//		this.previousButton.setVisible(true);
		mainContentPane.removeAll();
	}

	@Override
	public void valueChanged(ListSelectionEvent event) {
		boolean flag = false;
		List<String> subjectNames = new ArrayList<String>(
				this.availableSubjects);

		if (event.getSource().equals(availableSubjectList)) {
			subjectToBeAdded.clear();
			for (int i = 0; i < subjectNames.size(); i++) {
				StringTokenizer tempString = new StringTokenizer(
						subjectNames.get(i));
				String code = tempString.nextToken();

				if (this.availableSubjectList.isSelectedIndex(i)) {
					// System.out.println(i);
					Subject subject = details.getSubjectDatabase()
							.getSubjectForCode(code);

					subjectToBeAdded.add(subject);

					this.updateBottomPanel(subject);
					this.addSubject.setEnabled(true);

					flag = true;
				}

			}
			if (!flag) {
				this.addSubject.setEnabled(false);
			}
//			System.out.println(subjectToBeAdded);
		}
		subjectNames = new ArrayList<String>(this.interestedSubjects);
		if (event.getSource().equals(chosenSubjectList)) {
			subjectToBeRemoved.clear();
			for (int i = 0; i < this.interestedSubjects.size(); i++) {
				StringTokenizer tempString = new StringTokenizer(
						subjectNames.get(i));
				String code = tempString.nextToken();
				if (this.chosenSubjectList.isSelectedIndex(i)) {
					Subject subject = details.getSubjectDatabase()
							.getSubjectForCode(code);
					subjectToBeRemoved.add(subject);
					this.updateBottomPanel(subject);
					this.removeSubject.setEnabled(true);

					flag = true;

				}

			}
			if (!flag) {
				this.removeSubject.setEnabled(false);

			}
		}

	}

	private void updateBottomPanel(Subject currentSubject) {
		this.selectedSubjectDescription.setText(currentSubject
				.getFormattedDetails());
		selectedSubjectDescription.validate();

	}

	@Override
	public void actionPerformed(ActionEvent event) {
		String trigger = event.getActionCommand();

		if (trigger.equalsIgnoreCase("add")) {
//			System.out.println(subjectToBeAdded.size());
			String result = "";

			for (Subject subject : subjectToBeAdded) {
				String tempResult = addSubject(subject);
				if (!tempResult.isEmpty()) {
					result = result + tempResult + "\n";
				} else {
					availableSubjects.remove(subject.getCode() + " :\t "
							+ subject.getName());
					interestedSubjects.add(subject.getCode() + " :\t "
							+ subject.getName());
				}

			}
			if (!result.isEmpty()) {
				JOptionPane.showMessageDialog(frame, result,
						"Subject cannot be added", JOptionPane.ERROR_MESSAGE);
			}
			subjectToBeAdded.clear();
			availableSubjectList.setListData(availableSubjects.toArray());
			chosenSubjectList.setListData(interestedSubjects.toArray());
			frame.validate();
		} else if (trigger.equalsIgnoreCase("remove")) {

			for (Subject subject : subjectToBeRemoved) {
				availableSubjects.add(subject.getCode() + " :\t "
						+ subject.getName());
				interestedSubjects.remove(subject.getCode() + " :\t "
						+ subject.getName());
				generator.removeInterestedSubject(subject);
				
			}

			subjectToBeRemoved.clear();
			availableSubjectList.setListData(availableSubjects.toArray());
			chosenSubjectList.setListData(interestedSubjects.toArray());
			frame.validate();
		} else if (trigger.equalsIgnoreCase("categoryChoice")) {
			JComboBox cb = (JComboBox) event.getSource();
			String currentCategory = (String) cb.getSelectedItem();
			if (currentCategory.equalsIgnoreCase("all")) {
				availableSubjects = new HashSet<String>(details
						.getSubjectDatabase().getAllSubjectNames());
				availableSubjects.removeAll(interestedSubjects);
				availableSubjectList.setListData(this.availableSubjects
						.toArray());
				chosenSubjectList.setListData(interestedSubjects.toArray());
			} else {

				availableSubjects = new HashSet<String>(
						details.getSubjectDatabase()
								.getAllSubjectNamesWithCodesForCategory(
										currentCategory));
				availableSubjects.removeAll(interestedSubjects);
				availableSubjectList.setListData(this.availableSubjects
						.toArray());
				chosenSubjectList.setListData(interestedSubjects.toArray());
			}

		}

	}

	private String addSubject(Subject subject) {
		// System.out.println("adding subject" + subject.getName());
		return generator.addIntrestedSubject(subject);

	}

}
