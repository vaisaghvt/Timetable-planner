package timetable.generators.gui;

import javax.swing.DefaultListSelectionModel;

public class ToggleSelectionModel extends DefaultListSelectionModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4691271764859145912L;

	boolean gestureStarted = false;

	public void setSelectionInterval(int index0, int index1) {
		if (isSelectedIndex(index0) && !gestureStarted) {
			super.removeSelectionInterval(index0, index1);
		} else {
			super.setSelectionInterval(index0, index1);
			
		}
		gestureStarted = true;
	}

	public void setValueIsAdjusting(boolean isAdjusting) {
		if (isAdjusting == false) {
			gestureStarted = false;
		}
	}
}
