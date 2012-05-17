package timetable;

import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;

import timetable.core.TimeTable;
import timetable.database.SubjectDatabase;
import timetable.generators.gui.TimeTableDisplayPanel;

public class SimpleTimeTableDisplay extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6677405736659688302L;
	private TimeTable timeTable;

	public SimpleTimeTableDisplay() {
		this.timeTable = new TimeTable("tutu", new SubjectDatabase(new File(
				"tutuDatabase.txt")));

		if (!timeTable.hasImage()) {
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
			timeTable.createImage(directory + File.separator
					+ "Tutu.png");
		}

		this.add(new TimeTableDisplayPanel(timeTable));

		this.repaint();

		this.setVisible(true);
		this.setSize(800,600);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}

	public static void main(String[] args) {
		new SimpleTimeTableDisplay();
	}

}
