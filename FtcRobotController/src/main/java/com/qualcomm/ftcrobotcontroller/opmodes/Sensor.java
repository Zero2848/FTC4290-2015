package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.ftcrobotcontroller.Hardware;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

public class Sensor extends OpMode {
    double leftP = .5, rightP = .5;
    Hardware config;

    @Override
    public void init() {
        config = new Hardware(hardwareMap);
        config.rightGrabber = hardwareMap.servo.get("rg");
        config.leftGrabber = hardwareMap.servo.get("lg");
    }

    @Override
    public void loop() {
        if (gamepad1.a) {
            leftP += .01;
            rightP -= .01;
        }
        if (gamepad1.b) {
            leftP -= .01;
            rightP += .01;
        }
        if (leftP > 1) {
            leftP = 1;
        } else if (leftP < 0) {
            leftP = 0;
        }
        if (rightP > 1) {
            rightP = 1;
        } else if (rightP < 0) {
            rightP = 0;
        }

        config.leftGrabber.setPosition(leftP);
        config.rightGrabber.setPosition(rightP);
        telemetry.addData("Left", leftP);
        telemetry.addData("Right", rightP);


    }
}
