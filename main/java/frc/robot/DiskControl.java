package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import frc.robot.classes.ArcadeInput;

public class DiskControl {

    private static VictorSPX diskController = new VictorSPX(4);

    private static int forcedHold = 0;

    public static void open() {

        diskController.set(ControlMode.PercentOutput, -0.2);

    }

    public static void close() {

        diskController.set(ControlMode.PercentOutput, 0.2);

    }

    public static void stop() {

        diskController.set(ControlMode.PercentOutput, 0);

    }

    public static void controlDiskManipulator(ArcadeInput inputs) {

        int mode = getDiskManipulatorMode(inputs);

        forcedHold++;

        if(forcedHold > 100) {

            if(mode == 0) {
                open();
            }
            else if(mode == 1) {
                close();
            }
            else {
                stop();
            }
        }

        else {

            close();

        }

    }

	public static int getDiskManipulatorMode(ArcadeInput inputs) {

        if(inputs.getOpenDiskControl() && !inputs.getCloseDiskControl()) {
            return 0;
        }
        
        else if(!inputs.getOpenDiskControl() && inputs.getCloseDiskControl()) {
            return 1;
        }
        
        else {
            return 2;
        }

	}

}
