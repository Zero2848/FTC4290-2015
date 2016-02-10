package com.qualcomm.ftcrobotcontroller.opmodes;

import com.lasarobotics.library.drive.Tank;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by Ethan on 2/5/16.
 */
public class Purplebot extends OpMode {
    DcMotor leftFront, rightFront, leftBack, rightBack;
    double mod;

    @Override
    public void init() {
        leftFront = hardwareMap.dcMotor.get("lf");
        leftBack = hardwareMap.dcMotor.get("lb");
        rightFront = hardwareMap.dcMotor.get("rf");
        rightFront.setDirection(DcMotor.Direction.REVERSE);
        rightBack = hardwareMap.dcMotor.get("rb");
        rightBack.setDirection(DcMotor.Direction.REVERSE);
    }

    @Override
    public void loop() {
        if(gamepad1.left_bumper || gamepad1.right_bumper){
            mod = 3;
        } else {
            mod = 1;
        }
        Tank.motor4(leftFront, rightFront, leftBack, rightBack, gamepad1.left_stick_y/mod, gamepad1.right_stick_y/mod);
    }
}
