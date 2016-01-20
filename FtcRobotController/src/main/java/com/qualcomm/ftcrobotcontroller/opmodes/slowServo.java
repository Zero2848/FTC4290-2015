package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

/**
 * Created by Ethan on 1/19/16.
 */
public class slowServo extends LinearOpMode {
    double pos;

    @Override
    public void runOpMode() throws InterruptedException {
        hardware.climber = hardwareMap.servo.get("c");
        waitForStart();
        hardware.climber.setPosition(.45);
    }
}
