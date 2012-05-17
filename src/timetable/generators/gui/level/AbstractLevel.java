/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package timetable.generators.gui.level;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import timetable.generators.gui.RegistrationDetails;

/**
 *
 * @author vaisagh
 */
public abstract class AbstractLevel {

    protected RegistrationDetails details;
    protected JFrame frame;

    protected JButton previousButton;
    protected JButton nextButton;

    public AbstractLevel(RegistrationDetails model, JFrame frame, JPanel buttonArea) {
        this.details = model;
        this.frame = frame;

        this.previousButton = (JButton) buttonArea.getComponent(0);
        this.nextButton = (JButton) buttonArea.getComponent(buttonArea.getComponentCount()-1);

    }

    public abstract void setUpLevel();

    public abstract void clearUp();

    
}

