package frc.robot.classes;

public class ArcadeInput {

    private double speed = 0;
    private double rotation = 0;
    private boolean switchCam = false;
    private boolean shoot = false;
    private boolean load = false;
    private double linearSlideSpeed = 0;
    private boolean openDiskControl = false;
    private boolean closeDiskControl = false;
    private int accMode = 1;
    private boolean isCurvedTurning = false;
    private boolean[] manipulatorControls = new boolean[7];
    private boolean armPIDEnabled = true;
    private boolean shootSlowdown = false;
    private boolean allign = false;
    private boolean collisionDetection = false;

    private boolean autoBallistics = false;

    public void setAllign(boolean allign) {

        this.allign = allign;

    }

    public boolean getAllign() {

        return allign;

    }

    public void setShootSlowdown(boolean value) {

        shootSlowdown = value;

    }

    public boolean getShootSlowdown() {

        return shootSlowdown;

    }

    public void setArmPIDEnabled(boolean value) {

        armPIDEnabled = value;

    }

    public boolean getArmPIDEnabled() {

        return armPIDEnabled;

    }

    public void setManipulatorControls(boolean[] values) {

        manipulatorControls = values;

    }

    public boolean[] getManipulatorControls() {

        return manipulatorControls;

    }

    public void setAccMode(int mode) {

        accMode = mode;

    }

    public int getAccMode() {

        return accMode;

    }
    
    public void setLinearSlideSpeed(double speed) {

        this.linearSlideSpeed = speed;

    }

    public double getLinearSlideSpeed() {

        return linearSlideSpeed;

    }

    public void setOpenDiskControl(boolean on) {

        openDiskControl = on;

    }

    public void setAutoBallistics(boolean on){
        autoBallistics = on;
    }

    public boolean getOpenDiskControl() {

        return openDiskControl;

    }

    public void setCloseDiskControl(boolean on) {

        closeDiskControl = on;

    }

    public boolean getCloseDiskControl() {

        return closeDiskControl;
    }

    public void setSpeed(double speed) {

        this.speed = speed;

    }

    public void setRotation(double rotation) {

        this.rotation = rotation;

    }

    public void setShoot(boolean shoot) {

        this.shoot = shoot;

    }

    public void setLoad(boolean load) {

        this.load = load;

    }

    public void setSwitchCam(boolean pressed) {

        switchCam = pressed;

    }

    public double getSpeed() {

        return speed;

    }

    public double getRotation() {

        return rotation;

    }

    public boolean getShoot() {

        return shoot;

    }

    public boolean getLoad() {

        return load;

    }

    public boolean getSwitchCam() {

        return switchCam;

    }

	public void setCurvedTurning(boolean isCurvedTurning) {

        this.isCurvedTurning = isCurvedTurning;

    }
    
    public boolean getCurvedTurning() {

        return isCurvedTurning;

    }

	public boolean getCollisionDetection() {

        return collisionDetection;
        
    }
    
    public void setCollisionDetection(boolean value) {

        collisionDetection = value;
        
    }
    
    public boolean getAutoBallistics(){
        return autoBallistics;
    }

}
