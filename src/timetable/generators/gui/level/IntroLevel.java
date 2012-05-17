package timetable.generators.gui.level;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import timetable.generators.gui.RegistrationDetails;
import timetable.generators.gui.main.UserApp;
import timetable.generators.gui.main.UserApp.Option;

public class IntroLevel extends AbstractLevel implements ActionListener {


	
	
	private JPanel mainContentPane;
	private UserApp userApp;
	
	private JPanel buttonArea;

	public IntroLevel(RegistrationDetails model, JFrame frame, JPanel buttonArea,UserApp userApp) {
		super(model, frame, buttonArea);
		this.userApp = userApp;
		this.buttonArea = buttonArea;
	}

	@Override
	public void setUpLevel() {
		frame.remove(buttonArea);
		previousButton.setEnabled(false);
		nextButton.setEnabled(false);
		previousButton.setVisible(false);
		nextButton.setVisible(false);
		mainContentPane = new JPanel(new GridLayout(1, 3));
		mainContentPane.add(new JLabel());

		JPanel middlePanel = new JPanel(new GridLayout(3,1));
		
		
		JPanel middleTopPanel = new JPanel(new GridLayout(3,1));
		JPanel middleCenterPanel = new JPanel(new GridLayout(5,1));
		
		
		
		middleTopPanel.add(new JLabel());
		
		JLabel welcomeDialog = new JLabel("Welcome!", JLabel.CENTER);
		welcomeDialog.setFont(new Font("Dialog", Font.BOLD, 36));
		middleTopPanel.add(welcomeDialog);
		middleTopPanel.add(new JLabel());
		
		
		JButton createButton = new JButton("Create New Timetable");
		createButton.setActionCommand("create");
		JButton manageButton = new JButton("Manage Existing Timetable");
		manageButton.setActionCommand("manage");
		JButton exitButton = new JButton("Exit");
		createButton.addActionListener(this);
		manageButton.addActionListener(this);
		exitButton.addActionListener(this);
		
		middleCenterPanel.add(createButton);
		middleCenterPanel.add(new JLabel());
		middleCenterPanel.add(manageButton);
		middleCenterPanel.add(new JLabel());
		middleCenterPanel.add(exitButton);
		middlePanel.add(middleTopPanel);
		middlePanel.add(middleCenterPanel);
		middlePanel.add(new JLabel());
		
		mainContentPane.add(middlePanel);
		mainContentPane.add(new JLabel());
		frame.add(mainContentPane);
	}

	@Override
	public void clearUp() {
		frame.add(buttonArea, BorderLayout.NORTH);
		mainContentPane.removeAll();

		previousButton.setEnabled(true);
		nextButton.setEnabled(true);
		previousButton.setVisible(true);
		nextButton.setVisible(true);
	}
	
	

	@Override
	public void actionPerformed(ActionEvent event) {
	
		String trigger = event.getActionCommand();
		
		if(trigger.equalsIgnoreCase("create")){
			this.clearUp();
			userApp.initializeLevels(Option.CREATE);
			
		}else if(trigger.equalsIgnoreCase("manage")){
			this.clearUp();
			userApp.initializeLevels(Option.MANAGE);
		}else {
			this.clearUp();
			frame.dispose();
		}
		
	}
}