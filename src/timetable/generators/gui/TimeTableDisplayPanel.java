package timetable.generators.gui;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Robot;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import timetable.core.Subject;
import timetable.core.TimeTable;
import timetable.generators.gui.level.TimeTableChooserLevel;

public class TimeTableDisplayPanel extends JPanel implements MouseListener, MouseMotionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7283069374492589327L;
	private int originalHeight;
	private int originalWidth;
	private TimeTable timeTable;
	private int x;
	private int y;
	

	public TimeTableDisplayPanel(TimeTable timeTable) {
		super();
//		System.out.println("constructing"+timeTable.getImageFile());
		assert timeTable.hasImage();
		this.timeTable = timeTable;
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		
		this.setVisible(true);

		this.validate();
		
	}

	@Override
	public void paint(Graphics g) {
	System.out.println("Painting");System.out.println(this.getHeight() +", "+ this.getWidth());
		File file = new File(timeTable.getImageFile());
		Image image = null;
		try {
			image = ImageIO.read(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		originalWidth = image.getWidth(null);
		originalHeight = image.getHeight(null);

		image = TimeTableChooserLevel.getScaledImage(image,
				this.getWidth(), this.getHeight());
		g.drawImage(image, 0, 0, new Color(0, 0, 0, 0), null);
	}


	@Override
	public void mouseClicked(MouseEvent event) {

	}

	@Override
	public void mouseEntered(MouseEvent arg0) {

	}

	@Override
	public void mouseExited(MouseEvent arg0) {

	}

	@Override
	public void mousePressed(MouseEvent arg0) {

	}

	@Override
	public void mouseReleased(MouseEvent event) {
		x = event.getX();
		y = event.getY();
		Subject subject = timeTable.findSubjectForLocation(x * originalWidth
				/ this.getWidth(), y * originalHeight
				/ this.getHeight());
		if (subject != null) {

			this.setToolTipText(subject.getName()+"    "+subject.getDescription());
			System.out.println(subject.getName());
			Robot r;
			try {
				r = new Robot();
				r.mouseMove(event.getXOnScreen(), event.getYOnScreen());
			} catch (AWTException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			

		}
		

	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if(Math.abs(e.getX()-x)>10 || Math.abs(e.getY() -y) >10){
			this.setToolTipText("");
		}
		
	}

}
