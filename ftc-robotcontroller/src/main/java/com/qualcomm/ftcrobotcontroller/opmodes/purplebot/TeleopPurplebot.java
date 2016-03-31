package com.qualcomm.ftcrobotcontroller.opmodes.purplebot;

import com.lasarobotics.library.controller.Controller;
import com.lasarobotics.library.drive.Tank;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * PurpleBot (TM) Test Teleop
 */
public class TeleopPurplebot extends OpMode {

    DcMotor leftFront, rightFront, leftBack, rightBack;
    Controller one;

    @Override
    public void init() {
        leftFront = hardwareMap.dcMotor.get("lf");
        rightFront = hardwareMap.dcMotor.get("rf");
        leftBack = hardwareMap.dcMotor.get("lb");
        rightBack = hardwareMap.dcMotor.get("rb");

        leftFront.setDirection(DcMotor.Direction.REVERSE);
        leftBack.setDirection(DcMotor.Direction.REVERSE);
    }

    @Override
    public void loop() {
        one.update(gamepad1);

        Tank.motor4(leftFront, rightFront, leftBack, rightBack, one.left_stick_y, one.right_stick_y);
    }

    @Override
    public void stop() {
    }
}