package timetable.database.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.joda.time.IllegalFieldValueException;
import org.joda.time.LocalTime;

import timetable.core.Index;
import timetable.core.TimeSlot;
import timetable.core.TimeSlot.Day;
import timetable.core.TimeSlot.SessionType;

public class AddIndexFrame extends JFrame implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	AddSubjectFrame subjectFrame;
	JButton createButton;
	JButton cancelButton;

	JPanel buttonPanel;
	JTextField indexCode;
	JTextArea timeSlotsDescription;

	Set<TimeSlot> timeSlots;

	public AddIndexFrame(AddSubjectFrame subjectFrame) {
		this.subjectFrame = subjectFrame;

		timeSlots = new TreeSet<TimeSlot>();

		timeSlotsDescription = new JTextArea(5, 15);

		indexCode = new JTextField(15);

		timeSlotsDescription.setLineWrap(true);
		timeSlotsDescription.setWrapStyleWord(true);
		timeSlotsDescription.setEditable(false);
		timeSlotsDescription.setEnabled(false);

		// timeSlotsDescription.setMinimumSize(new Dimension(40, 200));

		JScrollPane listScroller = new JScrollPane(timeSlotsDescription);

		buttonPanel = new JPanel(new GridLayout(2, 2));

		createButton = new JButton("Create");
		createButton.setActionCommand("create");
		createButton.addActionListener(this);

		cancelButton = new JButton("Cancel");
		cancelButton.setActionCommand("cancel");
		cancelButton.addActionListener(this);

		this.setTitle("Index Description Area");
		this.setLayout(new BorderLayout());

		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(2, 2));

		panel.add(new JLabel("Code  :"));
		panel.add(this.indexCode);

		panel.add(new JLabel("TimeSlots  :"));
		panel.add(listScroller);

		JButton addTimeSlot = new JButton("Add Timeslot");
		addTimeSlot.setActionCommand("addTimeslot");
		addTimeSlot.addActionListener(this);

		JButton removeTimeSlot = new JButton("Remove Timeslot");
		removeTimeSlot.setActionCommand("removeTimeslot");
		removeTimeSlot.addActionListener(this);

		buttonPanel.add(addTimeSlot);
		buttonPanel.add(removeTimeSlot);
		buttonPanel.add(createButton);
		buttonPanel.add(cancelButton);

		this.add(panel, BorderLayout.NORTH);
		this.add(buttonPanel, BorderLayout.SOUTH);

		this.setResizable(false);
		this.setSize(300, 325);
		this.setLocation(920, 250);
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
			if (indexCode.getText().isEmpty()) {
				JOptionPane.showMessageDialog(this,
						"Please enter an index code", "No Index!",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			if (this.timeSlots.isEmpty()) {
				JOptionPane.showMessageDialog(this,
						"At least one time slot needed per index!",
						"No Session!", JOptionPane.ERROR_MESSAGE);
				return;
			}
			Index currentIndex = new Index(this.timeSlots,
					this.indexCode.getText());

			this.subjectFrame.addIndex(currentIndex);
			this.dispose();
		} else if (trigger.equalsIgnoreCase("cancel")) {
			this.dispose();
		} else if (trigger.equalsIgnoreCase("addTimeSlot")) {

			SessionType[] types = SessionType.values();
			types = Arrays.copyOfRange(types, 0, types.length - 2);
			SessionType type = null;

			type = (SessionType) JOptionPane.showInputDialog(this, "TYPE",
					"Type Input", JOptionPane.PLAIN_MESSAGE, null, types,
					SessionType.LECTURE);
			if (type == null) {
				return;
			}

			Day[] days = Day.values();

			Day day = (Day) JOptionPane
					.showInputDialog(this, "TYPE", "Day Input",
							JOptionPane.PLAIN_MESSAGE, null, days, Day.MON);
			if (day == null) {
				return;
			}

			Scanner sc = null;
			int startHour, startMinute;
			do {
				String startTimeString = (String) JOptionPane.showInputDialog(
						this, "Start Time?", "Start Time Input",
						JOptionPane.PLAIN_MESSAGE, null, null, "08:30");
				if (startTimeString == null) {
					return;
				}

				sc = new Scanner(startTimeString).useDelimiter(":");
				startHour = sc.nextInt();
				startMinute = sc.nextInt();
			} while (startHour < 0 || startHour > 23 || startMinute < 0
					|| startMinute > 59);
			LocalTime startTime = new LocalTime(startHour, startMinute);
			LocalTime endTime = null;
			int endHour, endMinute;
			do {
				String endTimeString = (String) JOptionPane.showInputDialog(
						this, "End Time?", "End Time Input",
						JOptionPane.PLAIN_MESSAGE, null, null, "10:25");
				if (endTimeString == null) {
					return;
				}

				sc = new Scanner(endTimeString).useDelimiter(":");
				endHour = sc.nextInt();
				endMinute = sc.nextInt();
				try{
					endTime = new LocalTime(startHour, startMinute);
				}catch(IllegalFieldValueException ifv){
					
				}
			} while (endHour < 0 || endHour > 23 || endMinute < 0
					|| endMinute > 59 ||endTime.isBefore(startTime));

			this.timeSlots.add(new TimeSlot(day.toString(), new LocalTime(
					startHour, startMinute), new LocalTime(endHour, endMinute),
					type, new String()));
			String result = "";
			for (TimeSlot timeSlot : this.timeSlots) {
				result = result + timeSlot.getFormattedDetails();
			}
			this.timeSlotsDescription.setText(result);
			this.validate();

		} else if (trigger.equalsIgnoreCase("removeTimeSlot")) {
			if (!this.timeSlots.isEmpty()) {
				TimeSlot timeSlotToBeDeleted = (TimeSlot) JOptionPane
						.showInputDialog(this,
								"Which Time Slot is to be removed?",
								"Remove Time Slot", JOptionPane.PLAIN_MESSAGE,
								null, timeSlots.toArray(),
								timeSlots.toArray()[0]);

				timeSlots.remove(timeSlotToBeDeleted);
				String result = "";
				for (TimeSlot timeSlot : this.timeSlots) {
					result = result + timeSlot.getFormattedDetails();
				}
				this.timeSlotsDescription.setText(result);
				this.validate();
			}
		}

	}

}
