package frc.robot;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import frc.robot.classes.ArcadeOutput;
import frc.robot.classes.SensorData;
import frc.robot.classes.ArcadeInput;

public class OITest{

    ArcadeInput input = new ArcadeInput();

    SensorData sensors = new SensorData();

    @Test   
    public void MapWorks(){
        assertEquals(OI.map(1024, 0, 2048, -1, 1),0,0);
    }

    @Test
    public void ultrasonicCollisonDetect() {

        input.setCollisionDetection(true);

        input.setSpeed(1);

        double[] sensorArray = {0, 0, 0, 0};

        sensors.setUltrasonicData(sensorArray);

        DriveTrain.getDriveSpeed(1, input, sensors);
        DriveTrain.getDriveSpeed(1, input, sensors);
        ArcadeOutput ao = DriveTrain.getDriveSpeed(1, input, sensors);

        assertTrue(ao.xSpeed == 0.5);

        double[] secondSensorArray = {7, 7, 7, 7};

        sensors.setUltrasonicData(secondSensorArray);

        DriveTrain.getDriveSpeed(1, input, sensors);
        DriveTrain.getDriveSpeed(1, input, sensors);
        ao = DriveTrain.getDriveSpeed(1, input, sensors);

        assertTrue(ao.xSpeed == 1);

        double[] thirdSensorArray = {7, 4, 7, 4};

        sensors.setUltrasonicData(thirdSensorArray);

        ao = DriveTrain.getDriveSpeed(1, input, sensors);

        assertTrue(ao.xSpeed == 0.5);

    }

    @Test
    public void CanMoveLinearSlide() {

        double linearSlideSpeed = 1;
        input.setLinearSlideSpeed(linearSlideSpeed);

        assertTrue(LinearSlide.getSlideSpeed(input) == 1);

    }

    @Test
    public void CanControlArm() {

        boolean[] manipulatorControls = {false, false, false, false, false, false, false};
        input.setManipulatorControls(manipulatorControls);
        assertTrue(Arm.getArmMode(input) == 0);

        manipulatorControls[0] = true;
        input.setManipulatorControls(manipulatorControls);
        assertTrue(Arm.getArmMode(input) == 1);

        manipulatorControls[1] = true;
        input.setManipulatorControls(manipulatorControls);
        assertTrue(Arm.getArmMode(input) == 0);

        manipulatorControls[0] = false;
        input.setManipulatorControls(manipulatorControls);
        assertTrue(Arm.getArmMode(input) == 2);

        manipulatorControls[1] = false;
        manipulatorControls[2] = true;
        input.setManipulatorControls(manipulatorControls);
        assertTrue(Arm.getArmMode(input) == 3);

        manipulatorControls[2] = false;
        manipulatorControls[3] = true;
        input.setManipulatorControls(manipulatorControls);
        assertTrue(Arm.getArmMode(input) == 4);

        manipulatorControls[3] = false;
        manipulatorControls[4] = true;
        input.setManipulatorControls(manipulatorControls);
        assertTrue(Arm.getArmMode(input) == 5);

        manipulatorControls[4] = false;
        manipulatorControls[5] = true;
        input.setManipulatorControls(manipulatorControls);
        assertTrue(Arm.getArmMode(input) == 6);

        manipulatorControls[5] = false;
        manipulatorControls[6] = true;
        input.setManipulatorControls(manipulatorControls);
        assertTrue(Arm.getArmMode(input) == 7);

    }

    @Test
    public void CanShootAndLoad() {
        
        boolean shoot = true;
        boolean load = false;
        input.setLoad(load);
        input.setShoot(shoot);

        assertTrue(Shooter.getShootState(input) == 1);

        shoot = false;
        load = true;
        input.setLoad(load);
        input.setShoot(shoot);

        assertTrue(Shooter.getShootState(input) == 2);

        shoot = true;
        input.setShoot(shoot);

        assertTrue(Shooter.getShootState(input) == 0);

        shoot = false;
        load = false;
        input.setLoad(load);
        input.setShoot(shoot);

        assertTrue(Shooter.getShootState(input) == 0);

    }

    @Test
    public void canControlCamera() {

        Camera.init();
        assertTrue(Camera.getCamState() == 0);

        boolean camSwitchPressed = true;
        input.setSwitchCam(camSwitchPressed);

        Camera.controlCam(input);

        assertTrue(Camera.getCamState() == 1);

        Camera.controlCam(input);

        assertTrue(Camera.getCamState() == 1);

        camSwitchPressed = false;
        input.setSwitchCam(camSwitchPressed);
        Camera.controlCam(input);

        camSwitchPressed = true;
        input.setSwitchCam(camSwitchPressed);
        Camera.controlCam(input);

        assertTrue(Camera.getCamState() == 0);

        Camera.init();

    }

    @Test
    public void CanControlDiskControl() {

        boolean openDisk = false;
        boolean closeDisk = false;
        input.setOpenDiskControl(openDisk);
        input.setCloseDiskControl(closeDisk);

        assertTrue(DiskControl.getDiskManipulatorMode(input) == 2);

        openDisk = true;
        closeDisk = false;
        input.setOpenDiskControl(openDisk);
        input.setCloseDiskControl(closeDisk);

        assertTrue(DiskControl.getDiskManipulatorMode(input) == 0);

        openDisk = false;
        closeDisk = true;
        input.setOpenDiskControl(openDisk);
        input.setCloseDiskControl(closeDisk);

        assertTrue(DiskControl.getDiskManipulatorMode(input) == 1);

        openDisk = true;
        closeDisk = true;
        input.setOpenDiskControl(openDisk);
        input.setCloseDiskControl(closeDisk);

        assertTrue(DiskControl.getDiskManipulatorMode(input) == 2);

    }

    @Test
    public void CanTurnRight() {

        double turnMultiplier = 1;
        boolean isCurvedTurning = true;
        int accMode = 1;
        double speed = 0;
        double rotation = 1;

        input.setSpeed(speed);
        input.setRotation(rotation);
        input.setAccMode(accMode);
        input.setCurvedTurning(isCurvedTurning);

        ArcadeOutput ao = DriveTrain.getDriveSpeed(turnMultiplier, input);
        
        assertTrue(ao.zRotation > 0);

        isCurvedTurning = false;
        input.setCurvedTurning(isCurvedTurning);

        ao = DriveTrain.getDriveSpeed(turnMultiplier, input);
        
        assertTrue(ao.zRotation > 0);

        DriveTrain.resetValues();
    }

    @Test
    public void CanTurnLeft() {

        double turnMultiplier = 1;
        boolean isCurvedTurning = true;
        int accMode = 1;
        double speed = 0;
        double rotation = -1;

        input.setSpeed(speed);
        input.setRotation(rotation);
        input.setAccMode(accMode);
        input.setCurvedTurning(isCurvedTurning);

        ArcadeOutput ao = DriveTrain.getDriveSpeed(turnMultiplier, input);
        
        assertTrue(ao.zRotation < 0);

        isCurvedTurning = false;
        input.setCurvedTurning(isCurvedTurning);

        ao = DriveTrain.getDriveSpeed(turnMultiplier, input);
        
        assertTrue(ao.zRotation < 0);

        DriveTrain.resetValues();
    }

    @Test
    public void CanDriveForward() {

        double turnMultiplier = 1;
        boolean isCurvedTurning = true;
        int accMode = 1;
        double speed = 1;
        double rotation = 0;

        input.setSpeed(speed);
        input.setRotation(rotation);
        input.setAccMode(accMode);
        input.setCurvedTurning(isCurvedTurning);

        ArcadeOutput ao = DriveTrain.getDriveSpeed(turnMultiplier, input);
        
        assertTrue(ao.xSpeed > 0);

        accMode = 2;
        input.setAccMode(accMode);

        ao = DriveTrain.getDriveSpeed(turnMultiplier, input);
        
        assertTrue(ao.xSpeed > 0);

        DriveTrain.resetValues();
    }

    @Test
    public void CanDriveBackwards() {

        double turnMultiplier = 1;
        boolean isCurvedTurning = true;
        int accMode = 1;
        double speed = -1;
        double rotation = 0;

        input.setSpeed(speed);
        input.setRotation(rotation);
        input.setAccMode(accMode);
        input.setCurvedTurning(isCurvedTurning);

        ArcadeOutput ao = DriveTrain.getDriveSpeed(turnMultiplier, input);
        
        assertTrue(ao.xSpeed < 0);

        accMode = 2;
        input.setAccMode(accMode);

        ao = DriveTrain.getDriveSpeed(turnMultiplier, input);
        
        assertTrue(ao.xSpeed < 0);

        DriveTrain.resetValues();
    }

}