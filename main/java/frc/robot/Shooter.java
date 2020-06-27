package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import frc.robot.classes.ArcadeInput;

public class Shooter {

  private static TalonSRX leftWheel = new TalonSRX(2);
  private static TalonSRX rightWheel = new TalonSRX(13);

  public static void init() {

    leftWheel.follow(rightWheel);
    rightWheel.setInverted(true);

  }

  public static void controlShooting(ArcadeInput inputs) {

    int state = getShootState(inputs);

    if(state == 0) {
      hold();
    }
    else if(state == 1) {
      shoot(inputs);
    }
    else {
      load();
    }

  }

  public static int getShootState(ArcadeInput inputs) {
    if(inputs.getShoot() && !inputs.getLoad()) {
      return 1;
    }

    else if(inputs.getLoad() && !inputs.getShoot()) {
      return 2;
    }

    else {
      return 0;
    }
  }

  public static void load() {

    if(OI.getCurrent(13) > 15 || OI.getCurrent(2) > 15) {

      System.out.println("LOAD: Current over 15 amps for shooter");

      stop();

    }

    else {

      rightWheel.set(ControlMode.PercentOutput, -0.5);

    }

  }
  public static void hold() {

    if(OI.getCurrent(13) > 15 || OI.getCurrent(2) > 15) {

      System.out.println("HOLD: Current over 15 amps for shooter");

      stop();

    }

    else {

      rightWheel.set(ControlMode.PercentOutput, -0.1);

    }

  }
  public static void shoot(ArcadeInput inputs) {

    System.out.println("A SHOT HAS BEEN TAKEN! Current angle: " + Robot.arm.getPosition() + " and current setpoint: " + Robot.arm.getSetpoint());
    System.out.println("The ultrasonic is at " + UltrasonicSensors.getSensors().frontInches + " " + UltrasonicSensors.getSensors().backInches);

    if (inputs.getAutoBallistics()){

      rightWheel.set(ControlMode.PercentOutput, Robot.ballisticsFiringSpeed);
      
    } else {
      if(inputs.getShootSlowdown()) {

        rightWheel.set(ControlMode.PercentOutput, 0.7);

      }

      else {

        if(Robot.arm.getPosition() < 400 || Robot.arm.getPosition() > 1350) {

          rightWheel.set(ControlMode.PercentOutput, 0.5);

        }

        else {

          rightWheel.set(ControlMode.PercentOutput, 0.5);

        }

      }
    }
  }

  public static void stop() {

    rightWheel.set(ControlMode.PercentOutput, 0);

  }

}
