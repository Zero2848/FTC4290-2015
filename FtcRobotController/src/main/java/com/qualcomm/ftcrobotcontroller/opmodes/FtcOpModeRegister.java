package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpModeManager;
import com.qualcomm.robotcore.eventloop.opmode.OpModeRegister;

/**
 * Register Op Modes
 */
public class FtcOpModeRegister implements OpModeRegister {
    public static String robot = "4290";

    /**
     * The Op Mode Manager will call this method when it wants a list of all
     * available op modes. Add your op mode to the list to enable it.
     *
     * @param manager op mode manager
     */

    public void register(OpModeManager manager) {
        if(robot == "4290") {
            manager.register("TeleOp", TeleOp.class);
            manager.register("Red", AutoRed.class);
            manager.register("Blue", AutoBlue.class);
            manager.register("Go to zone", AutoDriveStraight.class);
        }



        if(robot == "9875"){
            manager.register("TeleOp", Team9875.class);
        }
        if(robot == "Purplebot"){
            manager.register("TeleOp", Purplebot.class);
        }

        //manager.register("Drive", Team9875.class);
    }
}
