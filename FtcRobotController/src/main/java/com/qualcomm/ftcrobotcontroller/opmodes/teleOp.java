package com.qualcomm.ftcrobotcontroller.opmodes;

import com.lasarobotics.library.controller.Controller;
import com.lasarobotics.library.drive.Tank;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by Ethan on 1/9/16.
 */
public class teleOp extends OpMode {
    DcMotor leftWheel, rightWheel, winch1, winch2, angler;
    Servo climberLeft, climberRight;
    Controller driver, operator;

    @Override
    public void init() {
        leftWheel = hardwareMap.dcMotor.get("l");
        leftWheel.setDirection(DcMotor.Direction.REVERSE);
        rightWheel = hardwareMap.dcMotor.get("r");
        winch1 = hardwareMap.dcMotor.get("w1");
        winch1.setDirection(DcMotor.Direction.REVERSE);
        winch2 = hardwareMap.dcMotor.get("w2");
        angler = hardwareMap.dcMotor.get("a");
        climberLeft = hardwareMap.servo.get("cl");
        climberRight = hardwareMap.servo.get("c");
        driver = new Controller(gamepad1);
        operator = new Controller(gamepad2);
    }

    @Override
    public void loop() {
        Tank.Motor2(leftWheel, rightWheel, driver.left_stick_y, operator.right_stick_y);

        if(gamepad2.a){
            climberLeft.setPosition(.95);
            climberRight.setPosition(.05);
        } else if(gamepad2.y){
            climberLeft.setPosition(.05);
            climberRight.setPosition(.95);
        }

        if(gamepad1.y){
            winch1.setPower(1);
            winch2.setPower(1);
        } else if(gamepad1.a){
            winch1.setPower(-1);
            winch2.setPower(-1);
        } else {
            winch1.setPower(0);
            winch2.setPower(0);
        }

        if(gamepad1.b){
            angler.setPower(1);
        } else if(gamepad1.x){
            angler.setPower(-1);
        } else {
            angler.setPower(0);
        }

    }
}