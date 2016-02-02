package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.ftcrobotcontroller.Hardware;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

public class SlowServo extends LinearOpMode {
    Hardware config;

    public void moveArm() {
        for (double i = Hardware.climberBottom; i > Hardware.climberTop; i -= .01) {
            config.climber.setPosition(i);
        }
    }

    @Override
    public void runOpMode() throws InterruptedException {
        config = new Hardware(hardwareMap);
        config.climber = hardwareMap.servo.get("c");
        waitForStart();
        moveArm();
    }
}
