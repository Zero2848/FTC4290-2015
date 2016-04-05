package com.qualcomm.ftcrobotcontroller.opmodes;

<<<<<<< HEAD
import com.qualcomm.ftcrobotcontroller.opmodes.first.ColorSensorDriver;
=======
import com.qualcomm.ftcrobotcontroller.opmodes.navigation.EncoderTest;
>>>>>>> beae459aa7f655e5a558c8c811a441a8e6ddb83a
import com.qualcomm.ftcrobotcontroller.opmodes.phresh.TeleopSkunk;
import com.qualcomm.ftcrobotcontroller.opmodes.phresh.Testing;
import com.qualcomm.ftcrobotcontroller.opmodes.purplebot.AutoPurplebot;
import com.qualcomm.ftcrobotcontroller.opmodes.vision.LinearVisionSample;
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
        manager.register("* Teleop Skunk (v3)", TeleopSkunk.class);
        manager.register("test", Testing.class);
        manager.register("Auto Purplebot", AutoPurplebot.class);
        manager.register("Basic Vision Test", BasicVisionSample.class);
        manager.register("Linear Vision Test", LinearVisionSample.class);
        manager.register("Manual Vision Test", ManualVisionSample.class);
        manager.register("color test", ColorSensorDriver.class);
    }
}
