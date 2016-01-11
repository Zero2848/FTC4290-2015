package com.qualcomm.ftrightobotcontroller.opmodes;

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
    final double leftTop = .97, leftBottom = .25, rightTop = .75, rightBottom = .05;

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

        if(gamepad2.x && leftPosition !=leftTop){
            leftPosition=leftTop;
        } else if(gamepad2.b && leftPosition != leftBottom){
            leftPosition=leftBottom;
        }
        if(gamepad2.a && rightPosition != rightTop){
            rightPosition=rightTop;
        } else if(gamepad2.y && rightPosition != rightBottom){
            rightPosition=rightBottom;
        }

        telemetry.addData("1", "left Servo:" + leftPosition);
        telemetry.addData("2", "right Servo: " + rightPosition);
        telemetry.addData("3", "left motor: "+ leftWheel.getCurrentPosition());
        telemetry.addData("4", "right motor: " + rightWheel.getCurrentPosition());

        climberLeft.setPosition(leftPosition);
        climberRight.setPosition(rightPosition);


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