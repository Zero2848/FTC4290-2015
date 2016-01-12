package com.qualcomm.ftcrobotcontroller.opmodes;

import com.lasarobotics.library.controller.Controller;
import com.lasarobotics.library.drive.Tank;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * created by Ethan on 1/9/16.
 */
public class teleOp extends OpMode {
    DcMotor leftWheel, rightWheel, winch1, winch2, angler;
    Servo climberLeft, climberRight;
    Controller driver, operator;
    double leftPosition =.5, rightPosition = .5;
    float winchPower;
    final double leftServoTop = .97, leftServoBottom = .25, rightServoTop = .75, rightServoBottom = .05;

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
        climberRight = hardwareMap.servo.get("right");
        driver = new Controller(gamepad1);
        operator = new Controller(gamepad2);
    }

    @Override
    public void loop() {
        driver.update(gamepad1);
        operator.update(gamepad2);
        Tank.Motor2(leftWheel, rightWheel, driver.left_stick_y, driver.right_stick_y);

        if(gamepad2.x && leftPosition != leftServoTop){
            leftPosition=leftServoTop;
        } else if(gamepad2.b && leftPosition != leftServoBottom){
            leftPosition=leftServoBottom;
        }
        if(gamepad2.a && rightPosition != rightServoTop){
            rightPosition=rightServoTop;
        } else if(gamepad2.y && rightPosition != rightServoBottom){
            rightPosition=rightServoBottom;
        }

        telemetry.addData("1", "left Servo:" + leftPosition);
        telemetry.addData("2", "right Servo: " + rightPosition);
        telemetry.addData("3", "left motor: "+ leftWheel.getCurrentPosition());
        telemetry.addData("4", "right motor: " + rightWheel.getCurrentPosition());

        climberLeft.setPosition(leftPosition);
        climberRight.setPosition(rightPosition);


        
        winchPower = gamepad2.right_stick_y;
        if(Math.abs(winchPower) > 5){
            winch1.setPower(winchPower);
            winch2.setPower(winchPower);
        }


        /*
        if(Math.abs(gamepad2.left_stick_y) > 5){
            angler.setPower(gamepad2.left_stick_y);
        }
        */


        if(gamepad1.b){
            angler.setPower(1);
        } else if(gamepad1.x){
            angler.setPower(-1);
        } else {
            angler.setPower(0);
        }

    }
}