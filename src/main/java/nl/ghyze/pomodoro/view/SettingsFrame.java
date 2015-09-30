package nl.ghyze.pomodoro.view;

import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import nl.ghyze.pomodoro.model.Settings;
import nl.ghyze.pomodoro.model.Settings.Position;

public class SettingsFrame extends JFrame {
    private static int LABEL_WIDTH = 200;

    private Settings settings;

    private SpringLayout layout = new SpringLayout();

    private JLabel lbScreen = new JLabel("Screen");
    private JComboBox cbScreen = new JComboBox();
    private JLabel lbPosition = new JLabel("Position");
    private ButtonGroup bgPosition = new ButtonGroup();
    private JRadioButton rbTopLeft = new JRadioButton("Top left");
    private JRadioButton rbTopRight = new JRadioButton("Top right");
    private JRadioButton rbBottomLeft = new JRadioButton("Bottom left");
    private JRadioButton rbBottomRight = new JRadioButton("Bottom right");

    private JButton btOk = new JButton("OK");
    private JButton btCancel = new JButton("Cancel");

    private JLabel lbPomoTime = new JLabel("Time of pomodoros");
    private JLabel lbShortBreakTime = new JLabel("Time of short break");
    private JLabel lbLongBreakTime = new JLabel("Time of long break");
    private JLabel lbNrOfPomos = new JLabel("Pomodoros between long breaks");

    private JTextField tfPomoTime = new JTextField();
    private JTextField tfShortBreakTime = new JTextField();
    private JTextField tfLongBreakTime = new JTextField();
    private JTextField tfNrOfPomos = new JTextField();

    public SettingsFrame(Settings settings) {
	this.settings = settings;
	initUI();
    }

    private void initUI() {
	this.setPreferredSize(new Dimension(400, 400));

	this.getContentPane().setLayout(layout);

	initPosition();
	initButtons();
	initTimes();

	pack();

	Rectangle winSize = GraphicsEnvironment.getLocalGraphicsEnvironment()
		.getMaximumWindowBounds();

	int midVerticalPoint = (winSize.x / 2) + (winSize.width / 2);
	int midHorizontalPoint = (winSize.y / 2) + (winSize.height / 2);
	this.setLocation(midVerticalPoint - (this.getWidth() / 2),
		midHorizontalPoint - (this.getHeight() / 2));
    }

    private void initPosition() {
	bgPosition.add(rbTopLeft);
	bgPosition.add(rbTopRight);
	bgPosition.add(rbBottomLeft);
	bgPosition.add(rbBottomRight);

	layout.putConstraint(SpringLayout.WEST, lbPosition, 5,
		SpringLayout.WEST, getContentPane());
	layout.putConstraint(SpringLayout.EAST, lbPosition, LABEL_WIDTH,
		SpringLayout.WEST, getContentPane());
	layout.putConstraint(SpringLayout.NORTH, lbPosition, 5,
		SpringLayout.NORTH, getContentPane());
	layout.putConstraint(SpringLayout.SOUTH, lbPosition, 25,
		SpringLayout.NORTH, getContentPane());
	this.add(lbPosition);

	layout.putConstraint(SpringLayout.WEST, rbTopLeft, 5,
		SpringLayout.EAST, lbPosition);
	layout.putConstraint(SpringLayout.EAST, rbTopLeft, 100,
		SpringLayout.EAST, lbPosition);
	layout.putConstraint(SpringLayout.NORTH, rbTopLeft, 5,
		SpringLayout.NORTH, getContentPane());
	layout.putConstraint(SpringLayout.SOUTH, rbTopLeft, 25,
		SpringLayout.NORTH, getContentPane());
	this.add(rbTopLeft);
	rbTopLeft.setSelected(settings.getPosition() == Position.TOP_LEFT);

	layout.putConstraint(SpringLayout.WEST, rbTopRight, 5,
		SpringLayout.EAST, lbPosition);
	layout.putConstraint(SpringLayout.EAST, rbTopRight, 100,
		SpringLayout.EAST, lbPosition);
	layout.putConstraint(SpringLayout.NORTH, rbTopRight, 5,
		SpringLayout.SOUTH, rbTopLeft);
	layout.putConstraint(SpringLayout.SOUTH, rbTopRight, 25,
		SpringLayout.SOUTH, rbTopLeft);
	this.add(rbTopRight);
	rbTopRight.setSelected(settings.getPosition() == Position.TOP_RIGHT);

	layout.putConstraint(SpringLayout.WEST, rbBottomLeft, 5,
		SpringLayout.EAST, lbPosition);
	layout.putConstraint(SpringLayout.EAST, rbBottomLeft, 100,
		SpringLayout.EAST, lbPosition);
	layout.putConstraint(SpringLayout.NORTH, rbBottomLeft, 5,
		SpringLayout.SOUTH, rbTopRight);
	layout.putConstraint(SpringLayout.SOUTH, rbBottomLeft, 25,
		SpringLayout.SOUTH, rbTopRight);
	this.add(rbBottomLeft);
	rbBottomLeft
		.setSelected(settings.getPosition() == Position.BOTTOM_LEFT);

	layout.putConstraint(SpringLayout.WEST, rbBottomRight, 5,
		SpringLayout.EAST, lbPosition);
	layout.putConstraint(SpringLayout.EAST, rbBottomRight, 100,
		SpringLayout.EAST, lbPosition);
	layout.putConstraint(SpringLayout.NORTH, rbBottomRight, 5,
		SpringLayout.SOUTH, rbBottomLeft);
	layout.putConstraint(SpringLayout.SOUTH, rbBottomRight, 25,
		SpringLayout.SOUTH, rbBottomLeft);
	this.add(rbBottomRight);
	rbBottomRight
		.setSelected(settings.getPosition() == Position.BOTTOM_RIGHT);
    }

    private void initButtons() {
	layout.putConstraint(SpringLayout.WEST, btCancel, -105,
		SpringLayout.EAST, getContentPane());
	layout.putConstraint(SpringLayout.EAST, btCancel, -5,
		SpringLayout.EAST, getContentPane());
	layout.putConstraint(SpringLayout.NORTH, btCancel, -25,
		SpringLayout.SOUTH, getContentPane());
	layout.putConstraint(SpringLayout.SOUTH, btCancel, -5,
		SpringLayout.SOUTH, getContentPane());
	this.getContentPane().add(btCancel);

	layout.putConstraint(SpringLayout.WEST, btOk, -105, SpringLayout.WEST,
		btCancel);
	layout.putConstraint(SpringLayout.EAST, btOk, -5, SpringLayout.WEST,
		btCancel);
	layout.putConstraint(SpringLayout.NORTH, btOk, -25, SpringLayout.SOUTH,
		getContentPane());
	layout.putConstraint(SpringLayout.SOUTH, btOk, -5, SpringLayout.SOUTH,
		getContentPane());
	this.getContentPane().add(btOk);

	// btCancel.addActionListener(e -> {
	// this.setVisible(false);
	// });
	btCancel.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent arg0) {
		setVisible(false);
	    }

	});

	// btOk.addActionListener(e -> {
	// updateSettings();
	// this.setVisible(false);
	// });
	btOk.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent arg0) {
		updateSettings();
		setVisible(false);
	    }
	});
    }

    private void initTimes() {
	layout.putConstraint(SpringLayout.WEST, lbPomoTime, 5,
		SpringLayout.WEST, getContentPane());
	layout.putConstraint(SpringLayout.EAST, lbPomoTime, LABEL_WIDTH,
		SpringLayout.WEST, getContentPane());
	layout.putConstraint(SpringLayout.NORTH, lbPomoTime, 5,
		SpringLayout.SOUTH, rbBottomRight);
	layout.putConstraint(SpringLayout.SOUTH, lbPomoTime, 25,
		SpringLayout.SOUTH, rbBottomRight);
	this.add(lbPomoTime);

	layout.putConstraint(SpringLayout.WEST, lbShortBreakTime, 5,
		SpringLayout.WEST, getContentPane());
	layout.putConstraint(SpringLayout.EAST, lbShortBreakTime, LABEL_WIDTH,
		SpringLayout.WEST, getContentPane());
	layout.putConstraint(SpringLayout.NORTH, lbShortBreakTime, 5,
		SpringLayout.SOUTH, lbPomoTime);
	layout.putConstraint(SpringLayout.SOUTH, lbShortBreakTime, 25,
		SpringLayout.SOUTH, lbPomoTime);
	this.add(lbShortBreakTime);

	layout.putConstraint(SpringLayout.WEST, lbLongBreakTime, 5,
		SpringLayout.WEST, getContentPane());
	layout.putConstraint(SpringLayout.EAST, lbLongBreakTime, LABEL_WIDTH,
		SpringLayout.WEST, getContentPane());
	layout.putConstraint(SpringLayout.NORTH, lbLongBreakTime, 5,
		SpringLayout.SOUTH, lbShortBreakTime);
	layout.putConstraint(SpringLayout.SOUTH, lbLongBreakTime, 25,
		SpringLayout.SOUTH, lbShortBreakTime);
	this.add(lbLongBreakTime);

	layout.putConstraint(SpringLayout.WEST, lbNrOfPomos, 5,
		SpringLayout.WEST, getContentPane());
	layout.putConstraint(SpringLayout.EAST, lbNrOfPomos, LABEL_WIDTH,
		SpringLayout.WEST, getContentPane());
	layout.putConstraint(SpringLayout.NORTH, lbNrOfPomos, 5,
		SpringLayout.SOUTH, lbLongBreakTime);
	layout.putConstraint(SpringLayout.SOUTH, lbNrOfPomos, 25,
		SpringLayout.SOUTH, lbLongBreakTime);
	this.add(lbNrOfPomos);

	layout.putConstraint(SpringLayout.WEST, tfPomoTime, 5,
		SpringLayout.EAST, lbPosition);
	layout.putConstraint(SpringLayout.EAST, tfPomoTime, -5,
		SpringLayout.EAST, getContentPane());
	layout.putConstraint(SpringLayout.NORTH, tfPomoTime, 5,
		SpringLayout.SOUTH, rbBottomRight);
	layout.putConstraint(SpringLayout.SOUTH, tfPomoTime, 25,
		SpringLayout.SOUTH, rbBottomRight);
	this.add(tfPomoTime);
	tfPomoTime.setText("" + settings.getPomoMinutes());

	layout.putConstraint(SpringLayout.WEST, tfShortBreakTime, 5,
		SpringLayout.EAST, lbPosition);
	layout.putConstraint(SpringLayout.EAST, tfShortBreakTime, -5,
		SpringLayout.EAST, getContentPane());
	layout.putConstraint(SpringLayout.NORTH, tfShortBreakTime, 5,
		SpringLayout.SOUTH, lbPomoTime);
	layout.putConstraint(SpringLayout.SOUTH, tfShortBreakTime, 25,
		SpringLayout.SOUTH, lbPomoTime);
	this.add(tfShortBreakTime);
	tfShortBreakTime.setText("" + settings.getShortBreakMinutes());

	layout.putConstraint(SpringLayout.WEST, tfLongBreakTime, 5,
		SpringLayout.EAST, lbPosition);
	layout.putConstraint(SpringLayout.EAST, tfLongBreakTime, -5,
		SpringLayout.EAST, getContentPane());
	layout.putConstraint(SpringLayout.NORTH, tfLongBreakTime, 5,
		SpringLayout.SOUTH, lbShortBreakTime);
	layout.putConstraint(SpringLayout.SOUTH, tfLongBreakTime, 25,
		SpringLayout.SOUTH, lbShortBreakTime);
	this.add(tfLongBreakTime);
	tfLongBreakTime.setText("" + settings.getLongBreakMinutes());

	layout.putConstraint(SpringLayout.WEST, tfNrOfPomos, 5,
		SpringLayout.EAST, lbPosition);
	layout.putConstraint(SpringLayout.EAST, tfNrOfPomos, -5,
		SpringLayout.EAST, getContentPane());
	layout.putConstraint(SpringLayout.NORTH, tfNrOfPomos, 5,
		SpringLayout.SOUTH, lbLongBreakTime);
	layout.putConstraint(SpringLayout.SOUTH, tfNrOfPomos, 25,
		SpringLayout.SOUTH, lbLongBreakTime);
	this.add(tfNrOfPomos);
	tfNrOfPomos.setText("" + settings.getPomosBeforeLongBreak());
    }

    private void updateSettings() {
	Position position = Position.BOTTOM_RIGHT;
	if (rbTopLeft.isSelected()) {
	    position = Position.TOP_LEFT;
	} else if (rbTopRight.isSelected()) {
	    position = Position.TOP_RIGHT;
	} else if (rbBottomLeft.isSelected()) {
	    position = Position.BOTTOM_LEFT;
	}
	settings.setPosition(position);

	int pomoMinutes = getNumber(tfPomoTime.getText());
	if (pomoMinutes > 0) {
	    settings.setPomoMinutes(pomoMinutes);
	}

	int shortBreakMinutes = getNumber(tfShortBreakTime.getText());
	if (shortBreakMinutes > 0) {
	    settings.setShortBreakMinutes(shortBreakMinutes);
	}

	int longBreakMinutes = getNumber(tfLongBreakTime.getText());
	if (longBreakMinutes > 0) {
	    settings.setLongBreakMinutes(longBreakMinutes);
	}

	int pomosBeforeLongBreak = getNumber(tfNrOfPomos.getText());
	if (pomosBeforeLongBreak > 0) {
	    settings.setPomosBeforeLongBreak(pomosBeforeLongBreak);
	}
	settings.save();
    }

    private int getNumber(String numberString) {
	try {
	    int number = Integer.parseInt(numberString);
	    return number;
	} catch (NumberFormatException ex) {
	    ;
	}
	return -1;
    }

}
