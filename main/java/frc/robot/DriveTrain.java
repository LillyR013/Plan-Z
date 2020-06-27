package frc.robot;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import frc.robot.classes.ArcadeInput;
import frc.robot.classes.ArcadeOutput;
import frc.robot.classes.SensorData;

public class DriveTrain {

  private static final int ITERATIONS = 3;
  private static final double DEADZONE = 0.05;
  private static final double JOY_DEADZONE = 0.05;
  private static final double TURN_MULTIPLIER = 0.8;
  private static final double ULTRASONIC_MULTIPLIER = 0.10;
  private static final double COLLISION_DISTANCE = 11;

  private static double currentSpeed;
  private static double initSpeed;
  private static double setSpeed;
  private static double iterationNum;
  private static boolean reversed;

  private static WPI_TalonSRX frontRight;
  private static WPI_TalonSRX backRight;

  private static WPI_TalonSRX frontLeft;
  private static WPI_TalonSRX backLeft;

  private static SpeedControllerGroup leftSide;
  private static SpeedControllerGroup rightSide;
  private static DifferentialDrive driveTrain;

  public static void init() {

    resetValues();

    frontRight = new WPI_TalonSRX(14);
    backRight = new WPI_TalonSRX(15);

    frontLeft = new WPI_TalonSRX(1);
    backLeft = new WPI_TalonSRX(0);

    leftSide = new SpeedControllerGroup(frontLeft, backLeft);
    rightSide = new SpeedControllerGroup(frontRight, backRight);
    driveTrain = new DifferentialDrive(leftSide, rightSide);

    frontRight.setNeutralMode(NeutralMode.Coast);
    backRight.setNeutralMode(NeutralMode.Coast);
    frontLeft.setNeutralMode(NeutralMode.Coast);
    backLeft.setNeutralMode(NeutralMode.Coast);
    driveTrain.setSafetyEnabled(false);

  }

  public static void resetValues() {
    currentSpeed = 0;
    initSpeed = 0;
    setSpeed = 0;
    iterationNum = ITERATIONS;
    reversed = false;
  }

  public static void reverse() {

    reversed = !reversed;

  }

  public static void drive(ArcadeInput input, SensorData sensors) {

    ArcadeOutput output = getDriveSpeed(TURN_MULTIPLIER, input, sensors);

    System.out.println("Speed: " + output.xSpeed + " and rotation: " + output.zRotation);

    driveTrain.arcadeDrive(output.xSpeed, output.zRotation);

  }

  public static ArcadeOutput getDriveSpeed(double turnMultiplier, ArcadeInput input, SensorData sensors) {

    ArcadeOutput result = new ArcadeOutput();
    result.xSpeed = calculateSpeed(input);
    double axis;
    boolean isCurvedTurning = input.getCurvedTurning();

    axis = input.getRotation();

    if (Math.abs(axis) < JOY_DEADZONE) {
      axis = 0;
    }

    if (isCurvedTurning) {
      result.zRotation = (axis > 0) ? Math.pow(axis, 2) : -1 * Math.pow(axis, 2);
    } else {
      result.zRotation = axis;
    }

    if(!OI.getJoyButton(0, 12)) {

    result.zRotation *= turnMultiplier;

    }

    else {

      result.zRotation *= 0.65;

    }

    if (reversed) {
      result.xSpeed *= -1;
    }

    double[] ultrasonic = sensors.getUltrasonicData();

    if (input.getAllign()) {

      double angleOff = 0;

      if (result.xSpeed >= 0 && ultrasonic[0] != -1 && ultrasonic[1] != -1) {

        angleOff = ultrasonic[0] - ultrasonic[1];

      }

      else if (result.xSpeed <= 0 && ultrasonic[2] != -1 && ultrasonic[3] != -1) {

        angleOff = ultrasonic[3] - ultrasonic[2];

      }

      double rotationAdjustment = angleOff * ULTRASONIC_MULTIPLIER;

      result.zRotation += rotationAdjustment;

    }

    if (input.getCollisionDetection()) {

      if (result.xSpeed >= 0) {

        double rightSensor = ultrasonic[0];

        double leftSensor = ultrasonic[1];

        if (leftSensor == -1 && rightSensor != -1) {

          if (rightSensor < COLLISION_DISTANCE) {

            result.xSpeed *= 0.5;

          }

        }

        else if (rightSensor == -1 && leftSensor != -1) {

          if (leftSensor < COLLISION_DISTANCE) {

            result.xSpeed *= 0.5;

          }

        }

        else if (rightSensor != -1 && leftSensor != -1) {

          if ((leftSensor + rightSensor) / 2 < COLLISION_DISTANCE) {

            result.xSpeed *= 0.5;

          }

        }

      }

      else {

        double rightSensor = ultrasonic[2];

        double leftSensor = ultrasonic[3];

        if (leftSensor == -1 && rightSensor != -1) {

          if (rightSensor < COLLISION_DISTANCE) {

            result.xSpeed *= 0.5;

          }

        }

        else if (rightSensor == -1 && leftSensor != -1) {

          if (leftSensor < COLLISION_DISTANCE) {

            result.xSpeed *= 0.5;

          }

        }

        else if (rightSensor != -1 && leftSensor != -1) {

          if ((leftSensor + rightSensor) / 2 < COLLISION_DISTANCE) {

            result.xSpeed *= 0.5;

          }

        }

      }

    }

    if(OI.getJoyPOV(0) == 0) {
      result.xSpeed = 1;
      result.zRotation = 0;
    }

    if(OI.getJoyPOV(0) == 180) {
      result.xSpeed = -1;
      result.zRotation = 0;
    }

    return result;

  }

  private static double calculateSpeed(ArcadeInput input) {

    double desiredSpeed = input.getSpeed();
    int accMode = input.getAccMode();

    if (Math.abs(desiredSpeed) < JOY_DEADZONE) {
      desiredSpeed = 0;
    }

    if (Math.abs(setSpeed - desiredSpeed) > DEADZONE) {
      setSpeed = desiredSpeed;
      iterationNum = 0;
      initSpeed = currentSpeed;
    }

    if (accMode == 1) {
      // Curved Acceleration
      if (iterationNum == ITERATIONS) {
        currentSpeed = desiredSpeed;
        return desiredSpeed;
      } else {
        iterationNum++;
        currentSpeed = (setSpeed - initSpeed) * Math.sin(Math.PI / (2 * ITERATIONS) * iterationNum) + initSpeed;
        return currentSpeed;
      }
    }

    else if (accMode == 2) {
      // Linear Acceleration
      if (iterationNum == ITERATIONS) {
        currentSpeed = desiredSpeed;
        return desiredSpeed;
      } else {
        iterationNum++;
        currentSpeed = ((setSpeed - initSpeed) / ITERATIONS) * iterationNum + initSpeed;
        return currentSpeed;
      }
    }

    else {
      System.out.println("Invalid Acceleration Mode");
      System.exit(1);
      return 0;
    }

  }

}
