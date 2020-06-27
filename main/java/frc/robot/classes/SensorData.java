package frc.robot.classes;

public class SensorData {

    private double[] ultrasonicData = {-1, -1, -1, -1};

    public void setUltrasonicData(double[] data) {

        ultrasonicData = data;

    }

    public double[] getUltrasonicData() {

        return ultrasonicData;

    }

}
