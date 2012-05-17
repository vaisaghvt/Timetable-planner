package timetable.generators.gui.level;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import timetable.core.TimeTable;
import timetable.generators.gui.ExamFrame;
import timetable.generators.gui.RegistrationDetails;
import timetable.generators.gui.TimeTableDisplayPanel;

public class PlanSaveLevel extends AbstractLevel implements ActionListener {

	private JPanel timeTablePane;
	private TimeTable currentTimeTable;
	private TimeTable selectedTimeTable;
	private JButton addButton;
	private JButton overwriteButton;
	private JComboBox planChoice;
	private JButton removeButton;
	private JButton renameButton;
	private Container mainContentPane;
	private Boolean saveAsStatus;
	private JPanel buttonArea;
	private boolean savedStatus;
	private JButton examButton;

	public PlanSaveLevel(RegistrationDetails details, JFrame frame,
			JPanel buttonArea, Boolean saveAsStatus) {
		super(details, frame, buttonArea);
		this.saveAsStatus = saveAsStatus;
		this.buttonArea = buttonArea;

	}

	private boolean saveToFile(String fileName) {
		return this.currentTimeTable.saveTimeTableToFile(details.getUserName(),
				fileName);
	}

	@Override
	public void actionPerformed(ActionEvent event) {

		String trigger = event.getActionCommand();
		if (trigger.equalsIgnoreCase("add")) {
			String existCheck = details
					.timeTableExistsAlready(this.currentTimeTable);
			if (existCheck.isEmpty()) {
				int j = 1;
				File directory = new File(details.getUserName());
				if (directory.exists()) {

					for (int i = 0; i < directory.list().length; i++) {
						if (directory.list()[i].equals("Plan " + j)) {
							j++;
							i = 0;
						}
					}
				}
				String planName = (String) JOptionPane.showInputDialog(frame,
						"Plan Name?", "Save As...", JOptionPane.PLAIN_MESSAGE,
						null, null, "Plan " + j);
				if (planName != null) {

					if (!saveToFile(planName)) {
						JOptionPane.showMessageDialog(frame,
								"Sorry, could not be created!");
					} else {
						planChoice.addItem(planName);
						planChoice.setSelectedItem(planName);
						savedStatus = true;
						savedStatus = false;
						planChoice.removeItem("current*");
						planChoice.addItem("current");
						addButton.setEnabled(false);
						overwriteButton.setEnabled(false);
					}

				}
			}
		} else if (trigger.equalsIgnoreCase("overwrite")) {
			String existCheck = details
					.timeTableExistsAlready(this.currentTimeTable);
			if (existCheck.isEmpty()) {
				String chosenPlan = (String) planChoice.getSelectedItem();
				int n = -1;
				n = JOptionPane.showConfirmDialog(frame,
						"Are you sure you want to overwrite " + chosenPlan,
						"Overwrite Warning", JOptionPane.YES_NO_CANCEL_OPTION);

				if (n == 0) {

					System.out.println("saving");
					if (!saveToFile(chosenPlan)) {
						JOptionPane.showMessageDialog(frame,
								"Sorry, could not be created!");
					} else {
						updateChosenPlan(chosenPlan);
						savedStatus = true;
						addButton.setEnabled(false);
						savedStatus = true;
						planChoice.removeItem("current*");
						planChoice.addItem("current");
					}

				} else if (n == 1) {
					this.addButton.doClick();
				}
			}
		} else if (trigger.equalsIgnoreCase("remove")) {
			String chosenPlan = (String) planChoice.getSelectedItem();
			int n = -1;
			n = JOptionPane.showConfirmDialog(frame,
					"Are you sure you want to remove " + chosenPlan,
					"Remove Warning", JOptionPane.YES_NO_OPTION);
			if (n == 0) {
				System.out.println("removing");
				removePlan(chosenPlan);
				if (saveAsStatus) {
					this.currentTimeTable = details.getChosenTimeTable();

					String existCheck = details
							.timeTableExistsAlready(this.currentTimeTable);
					if (existCheck.isEmpty()) {
						savedStatus = false;
						planChoice.removeItem("current");
						planChoice.addItem("current*");
						if (addButton != null) {
							this.addButton.setEnabled(true);
						}if (overwriteButton != null) {
							this.overwriteButton.setEnabled(true);
						}
					} else {
						savedStatus = true;
					}
				}
				planChoice.removeItem(chosenPlan);
				this.timeTablePane.removeAll();
				if (planChoice.getSelectedIndex() != -1) {
					updateChosenPlan((String) planChoice.getItemAt(planChoice
							.getSelectedIndex()));
					if (((String) planChoice.getItemAt(planChoice
							.getSelectedIndex())).equalsIgnoreCase("current")
							|| ((String) planChoice.getItemAt(planChoice
									.getSelectedIndex()))
									.equalsIgnoreCase("current*")) {
						overwriteButton.setEnabled(false);
						removeButton.setEnabled(false);
						renameButton.setEnabled(false);
					}
				} else {
					examButton.setEnabled(false);
					removeButton.setEnabled(false);
					renameButton.setEnabled(false);

				}
				timeTablePane.repaint();
				timeTablePane.validate();

			}
		} else if (trigger.equalsIgnoreCase("rename")) {
			String chosenPlan = (String) planChoice.getSelectedItem();
			boolean result = false;
			do {
				String newName = (String) JOptionPane.showInputDialog(frame,
						"New Name?", "Rename existing plan",
						JOptionPane.PLAIN_MESSAGE, null, null, chosenPlan);
				if (newName != null) {
					System.out.println("renaming");
					result = renamePlan(chosenPlan, newName);
					if (result) {
						planChoice.removeItem(chosenPlan);
						planChoice.addItem(newName);
						this.timeTablePane.removeAll();
						timeTablePane.repaint();
						timeTablePane.validate();
					}
				}else{
					break;
				}
			} while (!result);
		} else if (trigger.equalsIgnoreCase("exam")) {
			if (selectedTimeTable != null) {
				new ExamFrame(this.selectedTimeTable.getSubjects());
			} else {
				new ExamFrame(this.currentTimeTable.getSubjects());
			}
		}

		else if (trigger.equalsIgnoreCase("planChoice")) {
			if (this.planChoice.getSelectedIndex() != -1) {
				String chosenPlan = (String) this.planChoice.getSelectedItem();
				if (chosenPlan.equalsIgnoreCase("current*")) {
					this.addButton.setEnabled(true);
					updateChosenPlan(chosenPlan);

				} else if (chosenPlan.equalsIgnoreCase("current")) {
					this.addButton.setEnabled(false);
					updateChosenPlan(chosenPlan);

				} else {

					// Set time table to the one loaded from that file
					// update time atble pane
					// change text of save to overwrite
					updateChosenPlan(chosenPlan);
				}

			}
		}
	}

	private void updateChosenPlan(String chosenPlan) {
		if (chosenPlan.equalsIgnoreCase("current")
				|| chosenPlan.equalsIgnoreCase("current*")) {
			selectedTimeTable = currentTimeTable;
			this.removeButton.setEnabled(false);
			this.renameButton.setEnabled(false);
			if (overwriteButton != null) {
				this.overwriteButton.setEnabled(false);
			}
		} else {
			String fileName = details.getUserName() + File.separator
					+ chosenPlan;
			this.selectedTimeTable = new TimeTable(fileName,
					details.getSubjectDatabase());
			this.removeButton.setEnabled(true);
			this.renameButton.setEnabled(true);
			if (overwriteButton!= null&&savedStatus) {
				this.overwriteButton.setEnabled(true);
			}
		}
		if (!selectedTimeTable.hasImage()) {
			System.out.println("creating image");
			File directory = new File("TempPictureFolder");
			if (!directory.exists()) {
				if (!directory.mkdir()) {
					System.out
							.println("Type Directory could not be created for "
									+ directory);
					// TODO : MAKE CLEANER
					try {
						throw new IOException();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

			selectedTimeTable.createImage(directory + File.separator
					+ details.getUserName().replace(" ", "")
					+ chosenPlan.replace(" ", "") + ".png");
		}

		timeTablePane.removeAll();
		timeTablePane.add(new TimeTableDisplayPanel(selectedTimeTable));
		timeTablePane.repaint();
		timeTablePane.validate();
		mainContentPane.validate();

	}

	private boolean renamePlan(String chosenPlan, String newName) {
		File fileToBeRenamed = new File(details.getUserName() + File.separator
				+ chosenPlan);

		File directory = new File(details.getUserName());

		for (String file : directory.list()) {
			if (file.equalsIgnoreCase(newName)) {
				JOptionPane.showMessageDialog(frame,
						"Already Exists : Rename unsucessful");
				return false;
			}
		}

		fileToBeRenamed.renameTo(new File(details.getUserName()
				+ File.separator + newName));

		return true;

	}

	private void removePlan(String chosenPlan) {
		File fileToBeRemoved = new File(details.getUserName() + File.separator
				+ chosenPlan);

		assert fileToBeRemoved.exists();
		fileToBeRemoved.delete();
		assert !fileToBeRemoved.exists();

	}

	@Override
	public void setUpLevel() {
		if (saveAsStatus) {
			this.currentTimeTable = details.getChosenTimeTable();

			String existCheck = details
					.timeTableExistsAlready(this.currentTimeTable);
			if (existCheck.isEmpty()) {
				savedStatus = false;
			} else {
				savedStatus = true;
			}
		}
		this.selectedTimeTable = null;
		mainContentPane = new JPanel(new BorderLayout());
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(1, 5));

		examButton = new JButton("Exam");
		examButton.addActionListener(this);
		buttonArea.add(examButton, 1);

		File directory = new File(details.getUserName());
		ArrayList<String> plans = new ArrayList<String>();
		if (!directory.exists()) {
			if (!directory.mkdir()) {
				System.out.println("Type Directory could not be created for "
						+ directory);
			}
		} else {

			for (File containedFile : directory.listFiles()) {
				if (!containedFile.isHidden())
					plans.add(containedFile.getName());
			}
		}
		if (saveAsStatus) {

			addButton = new JButton("Save As...");
			addButton.setActionCommand("add");
			addButton.addActionListener(this);
			overwriteButton = new JButton("Overwrite");
			overwriteButton.setEnabled(false);
			overwriteButton.addActionListener(this);

			if (savedStatus) {
				plans.add(0, "current");
				addButton.setEnabled(false);
			} else {
				plans.add(0, "current*");
			}
		}

		removeButton = new JButton("Remove");
		removeButton.setEnabled(false);
		removeButton.addActionListener(this);

		renameButton = new JButton("Rename");
		renameButton.setEnabled(false);
		renameButton.addActionListener(this);

		planChoice = new JComboBox(plans.toArray());
		plans.size();
		if (!plans.isEmpty()) {

			planChoice.setSelectedIndex(0);
		} else {
			examButton.setEnabled(false);
		}
		planChoice.setActionCommand("planChoice");
		planChoice.addActionListener(this);
		timeTablePane = new JPanel(new GridLayout(1, 1));
		if (!plans.isEmpty()) {

			updateChosenPlan(plans.get(0));
		}
		if (saveAsStatus) {
			buttonPanel.add(addButton);
			buttonPanel.add(overwriteButton);
		}
		this.nextButton.setText("Upload To STARS");
		this.nextButton.setActionCommand("Upload");
		buttonPanel.add(planChoice);

		buttonPanel.add(renameButton);
		buttonPanel.add(removeButton);

		mainContentPane.add(buttonPanel, BorderLayout.NORTH);
		mainContentPane.add(timeTablePane, BorderLayout.CENTER);

		frame.add(mainContentPane);

	}

	@Override
	public void clearUp() {
		if (!saveAsStatus) {
			System.out.println("add " + selectedTimeTable.getSubjects()
					+ "to stars");
		}
		buttonArea.remove(1);
		mainContentPane.removeAll();

	}
}
