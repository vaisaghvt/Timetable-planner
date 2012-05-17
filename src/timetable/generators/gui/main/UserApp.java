package timetable.generators.gui.main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;

import timetable.database.SubjectDatabase;
import timetable.generators.Generator;
import timetable.generators.gui.RegistrationDetails;
import timetable.generators.gui.level.AbstractLevel;
import timetable.generators.gui.level.IntroLevel;
import timetable.generators.gui.level.PlanSaveLevel;
import timetable.generators.gui.level.SubjectAdderLevel;
import timetable.generators.gui.level.TimeTableChooserLevel;

public class UserApp implements ActionListener {

	public enum Option {
		CREATE, MANAGE, INITIAL
	};

	private JFrame frame;

	private RegistrationDetails details;

	// public JLabel statusBar = new JLabel();
	JPanel buttonArea = new JPanel();

	JButton nextButton;
	JButton previousButton;

	private int currentLevel;
	ArrayList<AbstractLevel> listOfLevels;

	public UserApp(SubjectDatabase subjectDatabase, Generator currentGenerator) {

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
		}
		String fileName = null;
		File file = null;
		Set<String> subjectsCompleted;

		do {
			// fileName = (String) JOptionPane.showInputDialog(frame,
			// "Enter your record name", "User Check",
			// JOptionPane.INFORMATION_MESSAGE);
			// if (fileName == null) {
			// System.exit(0);
			// }
			fileName = "VaisaghFile.txt";
			file = new File(fileName);
			// System.out.println(file.exists());
		} while (!file.exists());

		Scanner sc = null;
		try {
			sc = new Scanner(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		String userName = sc.nextLine();
		frame = new JFrame(userName + "-Subject Registration-");
		details = new RegistrationDetails(userName, subjectDatabase,
				currentGenerator);
		subjectsCompleted = new HashSet<String>();
		while (sc.hasNext()) {
			String subjectName = subjectDatabase.getSubjectNameForCode(sc
					.next());
			subjectsCompleted.add(subjectName);
		}

		// frame = new JFrame("STARS Planner");
		frame.setLayout(new BorderLayout());

		MenuBar menuBar = new MenuBar();
		Menu fileMenu = new Menu("File");
		MenuItem about = new MenuItem("About");

		about.setActionCommand("about");

		fileMenu.add(about);

		about.addActionListener(this);

		frame.setMenuBar(menuBar);

		nextButton = new JButton("Next");
		previousButton = new JButton("Previous");

		buttonArea.setLayout(new GridLayout(1, 3));
		buttonArea.setBackground(Color.lightGray);
		buttonArea.add(previousButton);
		buttonArea.add(nextButton);

		previousButton.setToolTipText("Move to previous step");
		nextButton.setToolTipText("Move to next step");

		nextButton.addActionListener(this);
		previousButton.addActionListener(this);

		// statusBar.setForeground(Color.BLUE);

		frame.add(buttonArea, BorderLayout.NORTH);
		// frame.add(statusBar, BorderLayout.SOUTH);

		initializeLevels(Option.INITIAL);

		frame.setSize(900, 700);
		// frame.setLocation(10, 10);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);

	}

	@Override
	public void actionPerformed(ActionEvent event) {
		String trigger = event.getActionCommand();
		if (trigger.equalsIgnoreCase("about")) {
			JOptionPane
					.showMessageDialog(
							frame,
							"This program facilitates the creation of environments for the crowd simulation test bed",
							"About", JOptionPane.INFORMATION_MESSAGE);
		} else if (trigger.equalsIgnoreCase("next")) {
			listOfLevels.get(currentLevel).clearUp();
			currentLevel++;
			assert currentLevel < listOfLevels.size();
			// statusBar.setText("Level updated to " + currentLevel);
			listOfLevels.get(currentLevel).setUpLevel();
			frame.validate();
		} else if (trigger.equalsIgnoreCase("previous")) {
			listOfLevels.get(currentLevel).clearUp();
			currentLevel--;
			assert currentLevel > -1;
			// statusBar.setText("Level updated to " + currentLevel);
			listOfLevels.get(currentLevel).setUpLevel();
			frame.validate();
		} else if (trigger.equalsIgnoreCase("finish")) {
			int n = JOptionPane.showConfirmDialog(frame,
					"Are you sure you are done?", "Are you done?",
					JOptionPane.YES_NO_OPTION);
			if (n == 0) {
				listOfLevels.get(currentLevel).clearUp();
				currentLevel++;
				assert currentLevel < listOfLevels.size();
				// statusBar.setText("Level updated to " + currentLevel);
				listOfLevels.get(currentLevel).setUpLevel();
				frame.validate();

			}

		} else if (trigger.equalsIgnoreCase("upload")){
			int n = JOptionPane.showConfirmDialog(frame,
					"Are you sure you want to add these subjects to STARS?", "STARS time",
					JOptionPane.YES_NO_OPTION);
			if (n == 0) {
				listOfLevels.get(currentLevel).clearUp();
				currentLevel++;
				assert currentLevel < listOfLevels.size();
				// statusBar.setText("Level updated to " + currentLevel);
				listOfLevels.get(currentLevel).setUpLevel();
				frame.validate();

			}
		}
	}

	public void initializeLevels(Option option) {
		if (option == Option.INITIAL) {
			listOfLevels = new ArrayList<AbstractLevel>();

			listOfLevels.add(new IntroLevel(details, frame, buttonArea, this));

			currentLevel = 0;
		} else if (option == Option.CREATE) {
			listOfLevels = new ArrayList<AbstractLevel>();

			listOfLevels.add(new IntroLevel(details, frame, buttonArea, this));
			listOfLevels.add(new SubjectAdderLevel(details, frame, buttonArea));

			listOfLevels.add(new TimeTableChooserLevel(details, frame,
					buttonArea));
//			listOfLevels.add(new TimeTableDisplayLevel(details, frame,
//					buttonArea));
			listOfLevels
					.add(new PlanSaveLevel(details, frame, buttonArea, true));
			listOfLevels.add(new IntroLevel(details, frame, buttonArea, this));
			currentLevel = 1;
		} else if (option == Option.MANAGE) {
			listOfLevels = new ArrayList<AbstractLevel>();

			listOfLevels.add(new IntroLevel(details, frame, buttonArea, this));
			listOfLevels
					.add(new PlanSaveLevel(details, frame, buttonArea, false));
			listOfLevels.add(new IntroLevel(details, frame, buttonArea, this));
			currentLevel = 1;
		}
		listOfLevels.get(currentLevel).setUpLevel();
		frame.validate();
	}

}
