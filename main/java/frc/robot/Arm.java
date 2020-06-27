package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.SensorCollection;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.command.PIDSubsystem;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.classes.ArcadeInput;
import frc.robot.classes.BallisticsProfile;
import frc.robot.classes.UltrasonicReadings;

public class Arm extends PIDSubsystem {

  private double maxSpeed = 0.5;

  //if we change encoder magnets, these are the only numbers to change
  private int realBack = -1464; //4242
  private int realFwd = 579; //2164

  public TalonSRX motor;
  private double zeroOffset;
  private boolean PIDOverride;

  public Arm(double P, double I, double D, double F) {

    super("Arm", P, I, D, F);

    motor = new TalonSRX(10);

    PIDOverride = false;

    PIDController pidController = getPIDController();
    pidController.setOutputRange(-1023,1023);
    pidController.setContinuous(false);

  }

  public void initDefaultCommand() {

  }

  protected double returnPIDInput() {



    SensorCollection sensors = motor.getSensorCollection();
    double encoder = sensors.getPulseWidthPosition();
    SmartDashboard.putNumber("encoder", encoder);
    zeroOffset = OI.map(encoder, realFwd, realBack, 2048, 0);
    SmartDashboard.putNumber("Zero offset", zeroOffset);
    return zeroOffset;

  }

  protected void usePIDOutput(double output) {

    if(getPosition() > 2048 + 200 || getPosition() < 0 - 200) {

      System.out.println("WARNING: ENCODER GIVING BAD VALUES. PID PROTECTION ACTIVE");

      PIDOverride = true;

    }

    else {

      double motorSpeed = OI.map(output, -1023, 1023, -maxSpeed, maxSpeed);
      motor.set(ControlMode.PercentOutput, motorSpeed);

    }

  }

  public void controlArm(ArcadeInput inputs) {

    if(inputs.getAutoBallistics()){
      autoBallisticsControl(inputs);
    } else {
      if(inputs.getArmPIDEnabled() && !PIDOverride) {
  
        PIDControl(inputs);

      }

      else {

        manualControl(inputs);

      }
    }
  }

  private void autoBallisticsControl(ArcadeInput inputs){
    UltrasonicReadings usReadings = UltrasonicSensors.getSensors();
    BallisticsProfile bf;
    boolean isFwd;
    double side = LinearSlide.getSide();
    if (zeroOffset > 1024) {
      isFwd = true;

      double distanceOffset;

      if(side == -1) {
        distanceOffset = -1;
      }
      else if(side == 0) {
        distanceOffset = 11;
      }
      else {
        distanceOffset = 23;
      }

      bf = CalculateFiringAngle(usReadings.frontInches + distanceOffset, isFwd);
      Robot.ballisticsFiringSpeed = bf.firingSpeed/100;
      setSetpoint(OI.map(bf.angle,0,90,2048,1024));
    } else {  

      double distanceOffset;

      if(side == -1) {
        distanceOffset = 23;
      }
      else if(side == 0) {
        distanceOffset = 11;
      }
      else {
        distanceOffset = -1;
      }

      isFwd = false;
      bf = CalculateFiringAngle(usReadings.backInches  + distanceOffset, isFwd);
      Robot.ballisticsFiringSpeed = bf.firingSpeed/100;
      setSetpoint(OI.map(bf.angle,0,90,0,1024));
    }
    


  }

  private void PIDControl(ArcadeInput inputs) {

    enable();

      int mode = getArmMode(inputs);

      switch(mode) {

        case 1:
          setSetpoint(843); 
          break;
        case 2:
          setSetpoint(2120);
          break;
        case 3:
          setSetpoint(-60);
          break;
        case 4:
          setSetpoint(1203);
          break;
        case 5:
          setSetpoint(545);
          break;
        case 6:
          if(getSetpoint() < 2200) {
            setSetpoint(getSetpoint() + 5);
          }
          break;
        case 7:
          if(getSetpoint() > -100) {
            setSetpoint(getSetpoint() - 5);
          }
          break;
      }

      SmartDashboard.putNumber("Arm Position", 2048 - getPosition());

  }

  private void manualControl(ArcadeInput inputs) {

    disable();

    boolean[] manipulatorControls = inputs.getManipulatorControls();

    if(manipulatorControls[5] && !manipulatorControls[6]) {

      motor.set(ControlMode.PercentOutput, 0.4);

    } else if(!manipulatorControls[5] && manipulatorControls[6]) {

      motor.set(ControlMode.PercentOutput, -0.4);

    } else {

      motor.set(ControlMode.PercentOutput, 0);

    }

  }

  public static int getArmMode(ArcadeInput inputs) {

    boolean[] manipulatorControls = inputs.getManipulatorControls();

    boolean midpoint = manipulatorControls[0];
    boolean forward = manipulatorControls[1];
    boolean backward = manipulatorControls[2];
    boolean forwardMid = manipulatorControls[3];
    boolean backwardMid = manipulatorControls[4];
    boolean manualForward = manipulatorControls[5];
    boolean manualBackwards = manipulatorControls[6];

    if(midpoint^forward^backward^forwardMid^backwardMid^manualForward^manualBackwards) {

      if(midpoint) {
        return 1;
      }
      else if(forward) {
        return 2;
      }
      else if(backward) {
        return 3;
      }
      else if(forwardMid) {
        return 4;
      }
      else if(backwardMid) {
        return 5;
      }
      else if(manualForward) {
        return 6;
      }
      else {
        return 7;
      }
      
    }

    else {
      return 0;
    }

  }

  public BallisticsProfile CalculateFiringAngle(double inchesToTarget, boolean isFwd){
    BallisticsProfile result = new BallisticsProfile();

    double minInches = 0; //Ultrasonic will never report less than 11
    double maxInches = 48; //Most of the tuning will go here


    if(inchesToTarget > maxInches) {

      inchesToTarget = maxInches;

    }

    SmartDashboard.putNumber("Inches to Target", inchesToTarget);

    result.firingSpeed = OI.map(inchesToTarget,minInches,maxInches,0,100);

    double inchesFromGround_shooter = (isFwd) ? 28 : 41;
    double inchesFromGround_bullseye = 39.625; //if we are targeting multiple positions, we will want to drive this externally
    double alt = -(inchesFromGround_shooter - inchesFromGround_bullseye);

    double g = 9.81;

    double dis2 = inchesToTarget * inchesToTarget;
    double vel2 = result.firingSpeed * result.firingSpeed;
    double vel4 = result.firingSpeed * result.firingSpeed * result.firingSpeed * result.firingSpeed;
    
    double sqrt = vel4 - g * ((g*dis2) + (2* alt * vel2));
    
    SmartDashboard.putNumber("sqrt", sqrt);
    if (sqrt < 0){
      result.angle = 0; //maybe default to 45 with max power?
    } else {
      double num = vel2 - Math.sqrt(vel4 - g * ((g * dis2) + (2 * alt * vel2)));
      double dom = g * inchesToTarget;
      result.angle = Math.toDegrees((Math.atan(num / dom)));
    }

    SmartDashboard.putNumber("angle", result.angle);
    SmartDashboard.putNumber("angleScaled", (180-(result.angle)));
    SmartDashboard.putNumber("firingSpeed", result.firingSpeed);


    return result;
  }
}
