package com.qualcomm.ftcrobotcontroller.opmodes;

import com.kauailabs.navx.ftc.navXPIDController;
import com.lasarobotics.library.util.Log;
import com.lasarobotics.library.util.MathUtil;
import com.lasarobotics.library.util.Timers;
import com.qualcomm.ftcrobotcontroller.Auto;
import com.qualcomm.ftcrobotcontroller.Hardware;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;


public class AutoDriveStraight extends LinearOpMode {

    Hardware config;

    @Override
    public void runOpMode() throws InterruptedException {
        config = new Hardware(hardwareMap);

        waitForStart();
        Auto.driveTo(config, telemetry, 8000, 1, 1);
        config.navx.close();
    }
}
