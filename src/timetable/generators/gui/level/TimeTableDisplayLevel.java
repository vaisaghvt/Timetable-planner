package timetable.generators.gui.level;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ToolTipManager;

import timetable.core.TimeTable;
import timetable.generators.gui.ExamFrame;
import timetable.generators.gui.RegistrationDetails;
import timetable.generators.gui.TimeTableDisplayPanel;

public class TimeTableDisplayLevel extends AbstractLevel implements
		ActionListener {

	private TimeTable timeTable;
	private JPanel mainContentPane;
	private JScrollPane listScroller;
	private JPanel buttonArea;


	public TimeTableDisplayLevel(RegistrationDetails details, JFrame frame,
			JPanel buttonArea) {
		super(details, frame, buttonArea);
		this.buttonArea = buttonArea;
	}

	@Override
	public void setUpLevel() {

		this.timeTable = details.getChosenTimeTable();
		
		JButton examButton = new JButton("Exam");
		examButton.addActionListener(this);
		buttonArea.add(examButton,1);

		mainContentPane = new TimeTableDisplayPanel(timeTable); 

		
		
	
		listScroller = new JScrollPane(mainContentPane);

		listScroller
				.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		this.previousButton.setEnabled(true);
		this.nextButton.setText("Save Timetable");
		this.nextButton.setActionCommand("Next");
		this.nextButton.setEnabled(true);
		this.nextButton.setVisible(true);

		mainContentPane.repaint();

		frame.add(listScroller);
		ToolTipManager.sharedInstance().registerComponent(mainContentPane);
		ToolTipManager.sharedInstance().setInitialDelay(0);
	}

	@Override
	public void clearUp() {
		this.nextButton.setText("Next");
		mainContentPane.removeAll();
		buttonArea.remove(1);
		
		frame.remove(listScroller);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		String trigger = event.getActionCommand();
		if (trigger.equalsIgnoreCase("exam")) {
			new ExamFrame(this.timeTable.getSubjects());
		}

	}

}
