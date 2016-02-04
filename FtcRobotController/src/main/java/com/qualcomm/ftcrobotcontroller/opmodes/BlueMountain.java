package com.qualcomm.ftcrobotcontroller.opmodes;

import com.kauailabs.navx.ftc.AHRS;
import com.qualcomm.ftcrobotcontroller.Auto;
import com.qualcomm.ftcrobotcontroller.Hardware;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

public class BlueMountain extends LinearOpMode {
    public final double leftPow = .45;
    public final double rightPow = .4;
    int wheelPosition;
    Hardware config;

    public void say(String message) {
        telemetry.addData("STATUS", message);
    }

    @Override
    public void runOpMode() throws InterruptedException {
        config = new Hardware(hardwareMap);
        config.rightGrabber.setPosition(Hardware.RIGHT_GRABBER_UP);
        config.leftGrabber.setPosition(Hardware.LEFT_GRABBER_UP);

        waitForStart();

        Auto.driveTo(config, telemetry,4000, leftPow, rightPow);
        say("DRIVEN");
        sleep(100);
        sleep(100);

        Auto.resetEncoder(config.leftWheel);
        Auto.resetEncoder(config.rightWheel);

        config.rightWheel.setPower(-.75);
        config.leftWheel.setPower(-.8);
        wheelPosition = config.leftWheel.getCurrentPosition() + config.rightWheel.getCurrentPosition() / 2;

        while (wheelPosition > -2000) {
            wheelPosition = config.leftWheel.getCurrentPosition() + config.rightWheel.getCurrentPosition() / 2;
            say("" + wheelPosition);
        }

        config.rightGrabber.setPosition(Hardware.RIGHT_GRABBER_DOWN);
        config.leftGrabber.setPosition(Hardware.LEFT_GRABBER_DOWN);

        waitOneFullHardwareCycle();
        say("GRABBED");

        sleep(500);
        config.leftWheel.setPower(0);
        config.rightWheel.setPower(0);
        say("DONE");

    }
}
