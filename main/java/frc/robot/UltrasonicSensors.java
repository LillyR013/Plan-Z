package frc.robot;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.classes.UltrasonicReadings;

public class UltrasonicSensors {

    private static AnalogInput frontLeftSensor = new AnalogInput(0);
    private static AnalogInput frontRightSensor = new AnalogInput(1);
    private static AnalogInput backLeftSensor = new AnalogInput(2);
    private static AnalogInput backRightSensor = new AnalogInput(3);


    public static UltrasonicReadings getSensors() {
        double analog2Inches = 40.5;
        //double minInches = 11; 
    
        UltrasonicReadings result = new UltrasonicReadings();
    
        result.frInches = frontRightSensor.getAverageVoltage() * analog2Inches;
        result.flInches = frontLeftSensor.getAverageVoltage() * analog2Inches;
        result.frontInches = (result.frInches + result.flInches) / 2;
        SmartDashboard.putNumber("frInches", result.frInches);
        SmartDashboard.putNumber("flInches", result.flInches);
        SmartDashboard.putNumber("frontInches", result.frontInches);
        
        result.brInches = backRightSensor.getAverageVoltage() * analog2Inches;
        result.blInches = backLeftSensor.getAverageVoltage() * analog2Inches;
        result.backInches = (result.brInches + result.blInches) / 2;
        SmartDashboard.putNumber("frInches", result.brInches);
        SmartDashboard.putNumber("flInches", result.blInches);
        SmartDashboard.putNumber("backInches", result.backInches);
    
        return result;
      }

    public static double[] getSensorsOld() {
        
        double analog2Inches = 0.02;

        double minValue = 0.3; //12 inches

        double fr = frontRightSensor.getAverageVoltage();
        double fl = frontLeftSensor.getAverageVoltage();
        double br = backRightSensor.getAverageVoltage();
        double bl = backLeftSensor.getAverageVoltage();

        if(fr < minValue) {

            fr = 11;

        }

        else {

            fr = 12 + ((fr - 0.3) / analog2Inches);

        }

        if(fl < minValue) {

            fl = 11;

        }

        else {

            fl = 12 + ((fl - 0.3) / analog2Inches);

        }

        if(br < minValue) {

            br = 11;

        }

        else {

            br = 12 + ((br - 0.3) / analog2Inches);

        }

        if(bl < minValue) {

            bl = 11;

        }

        else {

            bl = 12 + ((bl - 0.3) / analog2Inches);

        }

        double[] values = {fr, fl, br, bl}; 
        
        return values;

    }

}
