package com.qualcomm.ftcrobotcontroller.opmodes;

import com.lasarobotics.library.drive.Tank;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by Ethan on 2/5/16.
 */
public class Team9875 extends OpMode {
    DcMotor left, right, arm, caribiner;
    double mod;

    @Override
    public void init() {
        left = hardwareMap.dcMotor.get("left");
        left.setDirection(DcMotor.Direction.REVERSE);
        right = hardwareMap.dcMotor.get("right");
        arm = hardwareMap.dcMotor.get("arm");
        caribiner = hardwareMap.dcMotor.get("caribiner");
    }

    @Override
    public void loop() {
        if(gamepad1.left_bumper) {
            mod = 2;
        } else if(gamepad1.right_bumper){
            mod = 4;
        } else {
            mod = 1;
        }
        Tank.motor2(left, right, gamepad1.left_stick_y/mod, gamepad1.right_stick_y/mod);
        if(gamepad2.a){
            arm.setPower(1);
        } else if(gamepad2.y){
            arm.setPower(-1);
        } else {
            arm.setPower(0);
        }

        if(gamepad2.x){
            caribiner.setPower(1);
        } else if(gamepad2.b){
            caribiner.setPower(-1);
        } else {
            caribiner.setPower(0);
        }

    }
}
