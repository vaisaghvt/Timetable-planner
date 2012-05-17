package timetable.generators.gui.level;

import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import timetable.core.TimeTable;
import timetable.generators.Generator;
import timetable.generators.gui.RegistrationDetails;

public class TimeTableChooserLevel extends AbstractLevel implements
		ActionListener {

	private Generator generator;
	private List<TimeTable> timeTableList;
	private JPanel mainContentPane;
	private TimeTable currentTimeTable;
	private JScrollPane listScroller;

	public TimeTableChooserLevel(RegistrationDetails details, JFrame frame,
			JPanel buttonArea) {
		super(details, frame, buttonArea);
		this.generator = details.getGenerator();

	}

	@Override
	public void setUpLevel() {
		generator.generateTimeTables();
		int height = Math.max(2,
				generator.getPossibleTimeTableList().size() / 2 + 1);
		mainContentPane = new JPanel(new GridLayout(height, 2));

		listScroller = new JScrollPane(mainContentPane);

		listScroller
				.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		this.previousButton.setEnabled(true);
		this.nextButton.setText("Next");
		this.nextButton.setActionCommand("next");
		this.nextButton.setVisible(false);
		this.nextButton.setEnabled(false);

		this.timeTableList = new ArrayList<TimeTable>(
				generator.getPossibleTimeTableList());
		int index = 0;

		for (TimeTable timeTable : timeTableList) {
			mainContentPane.add(createButtonForTimeTable(timeTable, index));
			index++;
		}

		frame.add(listScroller);
		if (timeTableList.isEmpty()) {
			previousButton.doClick();
		}
	}

	@Override
	public void clearUp() {
		this.nextButton.setVisible(true);
		mainContentPane.removeAll();
		frame.remove(listScroller);
	}

	private JButton createButtonForTimeTable(TimeTable timeTable, int index) {
		// JButton timeTableButton = new JButton("timeTable"+index);
		if (!timeTable.hasImage()) {
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
			} else if (index == 0) {
				for (File containedFile : directory.listFiles()) {
					containedFile.delete();
				}
			}

			String fileLocation = "TempPictureFolder" + File.separator
					+ "Timetable " + index + ".png";
			timeTable.createImage(fileLocation);
		}
		BufferedImage bufferedImage = null;
		try {
			bufferedImage = ImageIO.read(new File(timeTable.getImageFile()));
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}

		ImageIcon timeTableAsIcon = new ImageIcon(bufferedImage);

		ImageIcon thumbnailIcon = new ImageIcon(getScaledImage(
				timeTableAsIcon.getImage(), 376, 220));

		// timeTableButton.setIcon(thumbnailIcon);
		JButton timeTableButton = new JButton(thumbnailIcon);
		timeTableButton.addActionListener(this);
		timeTableButton.setActionCommand("timetable" + index);
		return timeTableButton;
	}

	/**
	 * Resizes an image using a Graphics2D object backed by a BufferedImage.
	 * 
	 * @param srcImg
	 *            - source image to scale
	 * @param w
	 *            - desired width
	 * @param h
	 *            - desired height
	 * @return - the new resized image
	 */
	public static Image getScaledImage(Image srcImg, int w, int h) {
		BufferedImage resizedImg = new BufferedImage(w, h,
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = resizedImg.createGraphics();
		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
				RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2.drawImage(srcImg, 0, 0, w, h, null);
		g2.dispose();
		return resizedImg;
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		String trigger = event.getActionCommand();
		int index = Integer.parseInt("" + trigger.charAt(trigger.length() - 1));

		this.currentTimeTable = timeTableList.get(index);

		details.setChosenTimeTable(this.currentTimeTable);
		nextButton.setEnabled(true);
		nextButton.doClick();

	}

}
