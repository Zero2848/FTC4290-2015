package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.ftcrobotcontroller.opmodes.navx.NavXSensorTest;
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
        if (robot.equals("4290")) {
            manager.register("Teleop", TeleOp.class);
            manager.register("Auto Red", AutoRed.class);
            manager.register("Auto Blue", AutoBlue.class);
            manager.register("NavX Test", NavXTest.class);
            manager.register("NavX Reader", NavXSensorTest.class);
        }
    }
}
