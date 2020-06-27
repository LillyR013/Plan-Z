package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.classes.ArcadeInput;
import frc.robot.classes.SensorData;

enum ControllerMode {
  triXbox, singleFlightStick, flightAndXbox, driverStation
}

public class OI {

  private static Joystick joy1;
  private static Joystick joy2;
  private static Joystick joy3;
  private static PowerDistributionPanel panel;
  private static SendableChooser<ControllerMode> controllerModeSelection;
  private static SendableChooser<Boolean> PIDEnabled;
  private static SendableChooser<Boolean> curvedTurning;
  private static ArcadeInput inputs;
  private static SensorData sensors;

  public static void init() {

    System.out.println("Ran OI init");

    controllerModeSelection = new SendableChooser<ControllerMode>();
    controllerModeSelection.addOption("Tri Xbox", ControllerMode.triXbox);
    controllerModeSelection.addOption("Single Flight Stick", ControllerMode.singleFlightStick);
    controllerModeSelection.addOption("Flight Stick and Xbox", ControllerMode.flightAndXbox);
    controllerModeSelection.addOption("Driver Station", ControllerMode.driverStation);

    curvedTurning = new SendableChooser<Boolean>();
    curvedTurning.addOption("x^2 Curve", true);
    curvedTurning.addOption("Linear Curve", false);

    PIDEnabled = new SendableChooser<Boolean>();
    PIDEnabled.addOption("Enable PID", true);
    PIDEnabled.addOption("Disable PID", false);

    SmartDashboard.putData("Turning Mode Selection", curvedTurning);
    SmartDashboard.putData("Control Selection", controllerModeSelection);
    SmartDashboard.putData("Control PID", PIDEnabled);

    joy1 = new Joystick(0);
    joy2 = new Joystick(1);
    joy3 = new Joystick(2);

    SmartDashboard.putBoolean("Linear Acc Enabled", false);

    panel = new PowerDistributionPanel();

    inputs = new ArcadeInput();

    sensors = new SensorData();

  }

  public static double getCurrent(int channel) {

    return panel.getCurrent(channel);

  }

  private static void getDriverStationIn() {

    int driver = 0;
    int manipL = 1;
    int manipR = 2;

    int accMode = 1;

    //driver functions
    double speed = -getJoyAxis(driver, 1);
    double rotation = getJoyAxis(driver, 2);
    boolean camSwitchPressed = getJoyButton(driver, 2);
    boolean shootSlowdown = getJoyButton(driver, 1);
    boolean allign = getJoyButton(driver, 6);


    //disc functions
    boolean openDisk = getJoyButton(manipR, 10);
    boolean closeDisk = getJoyButton(manipR, 4);

    //ball functions
    boolean shoot = OI.getJoyButton(manipR, 11);
    boolean load = OI.getJoyButton(manipR, 5);


    //slide functions
    boolean linearForward = OI.getJoyButton(manipL, 3);
    boolean linearBack = OI.getJoyButton(manipL, 1);
    double linearSlideSpeed = 0;

    //arm functions
    boolean autoBallistics = false;
    boolean midpoint = getJoyButton(manipL, 9);
    boolean forward = getJoyButton(manipL, 5);
    boolean backward = getJoyButton(manipL, 4);
    boolean forwardMid = getJoyButton(manipL, 7);
    boolean backwardMid = getJoyButton(manipL, 6);
    boolean manualForward = getJoyButton(manipL, 10);
    boolean manualBackwards = getJoyButton(manipL, 8);
    boolean[] manipulatorControls = {midpoint, forward, backward, forwardMid, backwardMid, manualForward, manualBackwards};

    boolean isCurvedTurning = true;

    boolean armPIDEnabled;


    if(PIDEnabled.getSelected() != null) {
    armPIDEnabled = PIDEnabled.getSelected();
    }
    else {
      armPIDEnabled = true;
    }


    //inputs.setAllign(allign);

    inputs.setShootSlowdown(shootSlowdown);

    inputs.setArmPIDEnabled(armPIDEnabled);

    if(curvedTurning.getSelected() != null) {
      isCurvedTurning = curvedTurning.getSelected();
    }

    if(getJoyButton(0, 11)) {
      SmartDashboard.putBoolean("Linear Acc Enabled", true);
      accMode = 2;
    }
    else {
      SmartDashboard.putBoolean("Linear Acc Enabled", false);
    }

    if(linearForward && !linearBack) {
      linearSlideSpeed = -1;
    }
    
    if(linearBack && !linearForward) {
      linearSlideSpeed = 1;
    }

    inputs.setCurvedTurning(isCurvedTurning);
    inputs.setAccMode(accMode);
    inputs.setManipulatorControls(manipulatorControls);
    inputs.setSpeed(speed);
    inputs.setRotation(rotation);
    inputs.setSwitchCam(camSwitchPressed);
    inputs.setShoot(shoot);
    inputs.setLoad(load);
    inputs.setLinearSlideSpeed(linearSlideSpeed);
    inputs.setCloseDiskControl(closeDisk);
    inputs.setOpenDiskControl(openDisk);
    inputs.setAutoBallistics(autoBallistics);
  }

  private static void getTriXboxIn() {

    double speed = getJoyAxis(0, 3) - getJoyAxis(0, 2);
    double rotation = getJoyAxis(0, 0);
    boolean camSwitchPressed = getJoyButton(0, 2);;
    int accMode = 1;
    boolean shoot = OI.getJoyButton(1, 1);
    boolean load = OI.getJoyButton(1, 2);
    double linearSlideSpeed = getJoyAxis(1, 4);
    boolean openDisk = getJoyButton(1, 5);
    boolean closeDisk = getJoyButton(1, 6);
    boolean isCurvedTurning = true;
    boolean armPIDEnabled = PIDEnabled.getSelected();
    boolean shootSlowdown = getJoyButton(0, 1);

    inputs.setShootSlowdown(shootSlowdown);


    inputs.setArmPIDEnabled(armPIDEnabled);

    if(curvedTurning.getSelected() != null) {
      isCurvedTurning = curvedTurning.getSelected();
    }

    double joyPos = getJoyAxis(1, 1);

    boolean midpoint = false;
    boolean forward = OI.getJoyButton(2, 3);
    boolean backward = OI.getJoyButton(2, 2);
    boolean forwardMid = OI.getJoyButton(2, 4);
    boolean backwardMid = OI.getJoyButton(2, 1);
    boolean manualForward = joyPos > 0.6;
    boolean manualBackwards = joyPos < 0.6;

    boolean allign = getJoyButton(0, 6);

    //inputs.setAllign(allign);

    if(getJoyButton(0, 2)) {
      SmartDashboard.putBoolean("Linear Acc Enabled", true);
      accMode = 2;
    }
    else {
      SmartDashboard.putBoolean("Linear Acc Enabled", false);
    }

    boolean[] manipulatorControls = {midpoint, forward, backward, forwardMid, backwardMid, manualForward, manualBackwards};

    inputs.setAccMode(accMode);
    inputs.setCurvedTurning(isCurvedTurning);
    inputs.setManipulatorControls(manipulatorControls);
    inputs.setSpeed(speed);
    inputs.setRotation(rotation);
    inputs.setSwitchCam(camSwitchPressed);
    inputs.setShoot(shoot);
    inputs.setLoad(load);
    inputs.setLinearSlideSpeed(linearSlideSpeed);
    inputs.setCloseDiskControl(closeDisk);
    inputs.setOpenDiskControl(openDisk);
  }

  private static void getFlightIn() {

    double speed = -getJoyAxis(0, 1);
    double rotation = getJoyAxis(0, 2);
    boolean camSwitchPressed = getJoyButton(0, 2);
    int accMode = 1;
    boolean shoot = false;
    boolean load = false;
    boolean closeDisk = false;
    boolean openDisk = false;
    double linearSlideSpeed = 0;
    boolean midpoint = OI.getJoyButton(0, 2);
    boolean forward = OI.getJoyButton(0, 6);
    boolean backward = OI.getJoyButton(0, 5);
    boolean forwardMid = OI.getJoyButton(0, 4);
    boolean backwardMid = OI.getJoyButton(0, 3);
    boolean manualForward = false;
    boolean manualBackwards = false;
    boolean isCurvedTurning = true;
    boolean armPIDEnabled = PIDEnabled.getSelected();
    boolean shootSlowdown = getJoyButton(0, 1);
    boolean allign = getJoyButton(0, 12);

    //inputs.setAllign(allign);

    inputs.setShootSlowdown(shootSlowdown);


    inputs.setArmPIDEnabled(armPIDEnabled);

    if(curvedTurning.getSelected() != null) {
      isCurvedTurning = curvedTurning.getSelected();
    }

    boolean[] manipulatorControls = {midpoint, forward, backward, forwardMid, backwardMid, manualForward, manualBackwards};

    int POV = getJoyPOV(0);

    if(getJoyButton(0, 11)) {
      SmartDashboard.putBoolean("Linear Acc Enabled", true);
      accMode = 2;
    }
    else {
      SmartDashboard.putBoolean("Linear Acc Enabled", false);
    }

    if(POV == 0) {
      shoot = true;
      openDisk = true;
    }
    else if(POV == 180) {
      load = true;
      closeDisk = true;
    }

    inputs.setAccMode(accMode);
    inputs.setCurvedTurning(isCurvedTurning);
    inputs.setSpeed(speed);
    inputs.setRotation(rotation);
    inputs.setSwitchCam(camSwitchPressed);
    inputs.setShoot(shoot);
    inputs.setLoad(load);
    inputs.setLinearSlideSpeed(linearSlideSpeed);
    inputs.setCloseDiskControl(closeDisk);
    inputs.setOpenDiskControl(openDisk);
    inputs.setManipulatorControls(manipulatorControls);
  }

  private static void getXboxFlightIn() {

    double speed = -getJoyAxis(0, 1);
    double rotation = getJoyAxis(0, 2);
    boolean camSwitchPressed = false;
    boolean shoot = OI.getJoyButton(1, 1);
    boolean load = OI.getJoyButton(1, 2);
    double linearSlideSpeed = -getJoyAxis(1, 4);
    boolean openDisk = getJoyButton(1, 6);
    boolean closeDisk = getJoyButton(1, 5);
    int accMode = 1;
    boolean isCurvedTurning = true;
    boolean armPIDEnabled = PIDEnabled.getSelected();
    boolean shootSlowdown = getJoyButton(0, 1);
    boolean allign = getJoyButton(0, 6);

    //inputs.setAllign(allign);

    inputs.setShootSlowdown(shootSlowdown);


    inputs.setArmPIDEnabled(armPIDEnabled);

    if(curvedTurning.getSelected() != null) {
      isCurvedTurning = curvedTurning.getSelected();
    }

    double joyPos = getJoyAxis(1, 1);
    int POV = getJoyPOV(1);

    boolean midpoint = false;
    boolean forward = OI.getJoyButton(2, 3);
    boolean backward = OI.getJoyButton(2, 2);
    boolean forwardMid = OI.getJoyButton(2, 4);
    boolean backwardMid = OI.getJoyButton(2, 1);
    boolean manualForward = joyPos > 0.6;
    boolean manualBackwards = joyPos < -0.6;

    if(getJoyButton(0, 11)) {
      SmartDashboard.putBoolean("Linear Acc Enabled", true);
      accMode = 2;
    }
    else {
      SmartDashboard.putBoolean("Linear Acc Enabled", false);
    }

    switch(POV) {
      case 0:
        forward = true;
        break;
      case 90:
        forwardMid = true;
        break;     
      case 180:
        backward = true;
        break;
      case 270:
        backwardMid = true;
        break;
    }

    boolean[] manipulatorControls = {midpoint, forward, backward, forwardMid, backwardMid, manualForward, manualBackwards};

    inputs.setAccMode(accMode);
    inputs.setCurvedTurning(isCurvedTurning);
    inputs.setManipulatorControls(manipulatorControls);
    inputs.setSpeed(speed);
    inputs.setRotation(rotation);
    inputs.setSwitchCam(camSwitchPressed);
    inputs.setShoot(shoot);
    inputs.setLoad(load);
    inputs.setLinearSlideSpeed(linearSlideSpeed);
    inputs.setCloseDiskControl(closeDisk);
    inputs.setOpenDiskControl(openDisk);

  }

  public static void updateSensors() {
/*
    double[] sensorData = UltrasonicSensors.getSensors();

    SmartDashboard.putNumber("UltraSonic FR", sensorData[0]);
    SmartDashboard.putNumber("UltraSonic FL", sensorData[1]);
    SmartDashboard.putNumber("UltraSonic BR", sensorData[2]);
    SmartDashboard.putNumber("UltraSonic BL", sensorData[3]);

    sensors.setUltrasonicData(sensorData);
*/
  }

  public static SensorData getSensors() {

    return sensors;

  }

  public static void updateInputs() {

    ControllerMode c = ControllerMode.driverStation;

    ControllerMode selected = controllerModeSelection.getSelected();

    if(selected != null) {
      c = selected;
    }
    
    if(c == ControllerMode.triXbox) {
      getTriXboxIn();
    }
    else if(c == ControllerMode.singleFlightStick){
      getFlightIn();
    }
    else if(c == ControllerMode.flightAndXbox) {
      getXboxFlightIn();
    }
    else {
      getDriverStationIn();
    }

  }

  public static ArcadeInput getInputs() {
    return inputs;
  }

  public static int getJoyPOV(int joy) {
    
    if(joy == 0) {
        return joy1.getPOV();
    }
    else if(joy == 1) {
      return joy2.getPOV();
    }
    else {
      return joy3.getPOV();
    }

  }

  private static double getJoyAxis(int joy, int axis) {

    if(joy == 0) {
        return joy1.getRawAxis(axis);
    }
    else if(joy == 1) {
      return joy2.getRawAxis(axis);
    }
    else {
      return joy3.getRawAxis(axis);
    }

  }

  static boolean getJoyButton(int joy, int button) {

    if(joy == 0) {
        return joy1.getRawButton(button);
    }
    else if(joy == 1) {
      return joy2.getRawButton(button);
    }
    else {
      return joy3.getRawButton(button);
    }

  }

  public static double map(double x, double in_min, double in_max, double out_min, double out_max){
		return (x-in_min) * (out_max-out_min) / (in_max - in_min) + out_min;
	}

}