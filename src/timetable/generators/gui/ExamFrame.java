package timetable.generators.gui;

import java.awt.BorderLayout;
import java.util.Collection;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import timetable.core.Subject;

public class ExamFrame extends JFrame {

	public ExamFrame(Collection<? extends Subject> subjects) {

		String[] columnNames = { "Subect", "Date", "Start Time", "End Time" };
		Object[][] data = new Object[subjects.size()][4];
		int index = 0;
		for (Subject subject : subjects) {
			if (subject.hasExam()) {
				Object[] row = {
						subject.getName(),
						subject.getExaminationDateTime().getDate()
								.toString("dd-MMM-yyyy"),
						subject.getExaminationDateTime().getStartTime()
								.toString("HH:mm"),
						subject.getExaminationDateTime().getEndTime()
								.toString("HH:mm") };
				data[index] = row;
			} else {
				Object[] row = { subject.getName(), "", "", "" };
				data[index] = row;
			}
			index++;
		}
		JTable table = new JTable(data, columnNames);
		table.setFillsViewportHeight(true);
		JScrollPane scrollPane = new JScrollPane(table);
		table.setFillsViewportHeight(true);

		this.getContentPane().setLayout(new BorderLayout());
		// this.getContentPane().add(table.getTableHeader(),
		// BorderLayout.PAGE_START);
		this.getContentPane().add(scrollPane, BorderLayout.CENTER);

		this.setResizable(false);
		this.setSize(900, 300);
		this.setVisible(true);
		this.setAlwaysOnTop(true);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 7742138912103044134L;

}
