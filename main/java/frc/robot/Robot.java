package frc.robot;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.TimedRobot;
import frc.robot.classes.ArcadeInput;
import frc.robot.classes.SensorData;

public class Robot extends TimedRobot {

  public static Arm arm;

  private static boolean initedArm;
  public static double slideDistanceFromCenter = 0;
  public static double ballisticsFiringSpeed = 0;

  public void robotInit() {

    CameraServer server = CameraServer.getInstance();
    server.startAutomaticCapture(0);

    OI.init();

    Shooter.init();

    DriveTrain.init();

    LinearSlide.init();

    Camera.init();

    initedArm = false;
    
    arm = new Arm(1, 0, 0, 0);

    arm.enable();

  }

  public void robotPeriodic() {

  }

  public void autonomousInit() {

    DriveTrain.resetValues();

    initArm();

  }

  public void autonomousPeriodic() {

    bothPeriodic();
    
  }

  public void teleopInit() {

    DriveTrain.resetValues();

    initArm();

  }

  private void initArm() {

    if(!initedArm) {

      arm.setSetpoint(arm.getPosition() - 320);

      initedArm = true;

    }

  }

  public void teleopPeriodic() {

    bothPeriodic();

  }

  private void bothPeriodic() {

    OI.updateInputs();

    OI.updateSensors();

    slideDistanceFromCenter = LinearSlide.getSide();

    ArcadeInput inputs = OI.getInputs();

    SensorData sensors = OI.getSensors();

    DriveTrain.drive(inputs, sensors);
    
    Camera.controlCam(inputs);

    Shooter.controlShooting(inputs);

    LinearSlide.controlLinearSlide(inputs);

    DiskControl.controlDiskManipulator(inputs);

    arm.controlArm(inputs);
    
  }

  public void testPeriodic() {

  }

}
