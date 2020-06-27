package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.SensorCollection;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import frc.robot.classes.ArcadeInput;

public class LinearSlide {

  private static TalonSRX leftSlide;
  private static TalonSRX rightSlide;
  private static SensorCollection leftSensors;
  private static SensorCollection rightSensors;

  public static void init() {

    leftSlide = new TalonSRX(3);
    rightSlide = new TalonSRX(12);
    leftSensors = leftSlide.getSensorCollection();
    rightSensors = rightSlide.getSensorCollection();
    leftSlide.follow(rightSlide);

  }

  public static double getSide() {

    if(leftSensors.isFwdLimitSwitchClosed() || rightSensors.isFwdLimitSwitchClosed()) {

      return 1;

    }

    else if(rightSensors.isRevLimitSwitchClosed() || leftSensors.isRevLimitSwitchClosed()) {

      return -1;

    }

    else {

      return 0;

    }

  }

  public static double getSlideSpeed(ArcadeInput inputs) {

    double speed = inputs.getLinearSlideSpeed();

    if(Math.abs(speed) < 0.2) {
      speed = 0;
    }

    return speed;

  }

  public static void controlLinearSlide(ArcadeInput inputs) {

      double speed = getSlideSpeed(inputs);

      slide(speed);

  }

  public static void slide(double speed) {

    if(OI.getCurrent(3) < 5 && OI.getCurrent(12) < 5) {

      rightSlide.set(ControlMode.PercentOutput, speed);

    }

    else {

      System.out.println("Current Too High - Linear Slide: " + OI.getCurrent(3) + " " + OI.getCurrent(12));
      
      rightSlide.set(ControlMode.PercentOutput, 0);

    }

  }

}
