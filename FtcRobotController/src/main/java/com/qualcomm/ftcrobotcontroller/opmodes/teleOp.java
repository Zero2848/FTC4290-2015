package com.qualcomm.ftcrobotcontroller.opmodes;

import com.lasarobotics.library.controller.ButtonState;
import com.lasarobotics.library.controller.Controller;
import com.lasarobotics.library.drive.Tank;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

/*
 * created by Ethan on 1/9/16.
 */
public class teleOp extends OpMode {
    Controller driver, operator;
    float winchPower;
    final double leftServoTop = .97, leftServoBottom = .25, rightServoTop = .75, rightServoBottom = .05, climberTop = .25, climberBottom = .75, stopperOn = .05, stopperOff = .25;
    double leftPosition = leftServoTop, rightPosition = rightServoTop, climberPosition = climberTop, anglerPower, stopperPosition;

    public void telemetry(){
        if(leftPosition == leftServoTop){
            telemetry.addData("1", "left is off");
        } else if(leftPosition == leftServoBottom){
            telemetry.addData("1", "left is on");
        } else {
            telemetry.addData("1", "no left servo");
        }

        if(rightPosition == rightServoTop){
            telemetry.addData("2", "right is off");
        } else if(rightPosition == rightServoBottom){
            telemetry.addData("2", "right is on");
        } else {
            telemetry.addData("2", "no right servo");
        }

        if(climberPosition == climberBottom){
            telemetry.addData("3", "climber is off");
        } else if(climberPosition == climberTop){
            telemetry.addData("3", "climber is on");
        } else {
            telemetry.addData("3", "no climber servo");
        }
        if(stopperPosition == stopperOn){
            telemetry.addData("4", "stopper is on");
        } else if(stopperPosition == stopperOff){
            telemetry.addData("4", "stopper is off");
        }
        telemetry.addData("5", "left motor: " + hardware.leftWheel.getCurrentPosition());
        telemetry.addData("6", "right motor: " + hardware.rightWheel.getCurrentPosition());
        telemetry.addData("7", "winch power: " + winchPower);
        telemetry.addData("8", "angler power:" + anglerPower);

    }

    @Override
    public void init() {
        hardware.leftWheel = hardwareMap.dcMotor.get("l");
        hardware.leftWheel.setDirection(DcMotor.Direction.REVERSE);
        hardware.rightWheel = hardwareMap.dcMotor.get("r");
        hardware.winch1 = hardwareMap.dcMotor.get("w1");
        hardware.winch1.setDirection(DcMotor.Direction.REVERSE);
        hardware.winch2 = hardwareMap.dcMotor.get("w2");
        hardware.angler = hardwareMap.dcMotor.get("a");
        hardware.climberLeft = hardwareMap.servo.get("cl");
        hardware.climberRight = hardwareMap.servo.get("cr");
        hardware.stopper = hardwareMap.servo.get("s");
        hardware.climber = hardwareMap.servo.get("c");
        driver = new Controller(gamepad1);
        operator = new Controller(gamepad2);
    }

    @Override
    public void loop() {
        driver.update(gamepad1);
        operator.update(gamepad2);

        Tank.Motor2(hardware.leftWheel, hardware.rightWheel, driver.left_stick_y, driver.right_stick_y);

        if(operator.y == ButtonState.PRESSED && climberPosition != climberTop){
            climberPosition=climberTop;
        } else if(operator.y == ButtonState.PRESSED && climberPosition != climberBottom){
            climberPosition=climberBottom;
        }

        if(operator.x  == ButtonState.PRESSED && leftPosition != leftServoTop){
            leftPosition=leftServoTop;
        } else if(operator.x == ButtonState.PRESSED && leftPosition != leftServoBottom){
            leftPosition=leftServoBottom;
        }
        if(operator.b == ButtonState.PRESSED && rightPosition != rightServoTop){
            rightPosition=rightServoTop;
        } else if(operator.b == ButtonState.PRESSED && rightPosition != rightServoBottom){
            rightPosition=rightServoBottom;
        }

        if(Math.abs(operator.right_stick_y) > .05){
            winchPower = operator.right_stick_y;
        }else{
            winchPower = 0;
        }

        if(operator.a == ButtonState.PRESSED && stopperPosition != stopperOn){
            stopperPosition = stopperOn;
        } else if(operator.a == ButtonState.PRESSED && stopperPosition != stopperOff){
            stopperPosition = stopperOff;
        }

        if(operator.dpad_up == ButtonState.PRESSED){
            anglerPower = 1;
        } else if(operator.dpad_down == ButtonState.PRESSED){
            anglerPower = -1;
        } else {
            anglerPower = 0;
        }

        hardware.angler.setPower(anglerPower);

        hardware.stopper.setPosition(stopperPosition);

        hardware.climber.setPosition(climberPosition);

        hardware.climberLeft.setPosition(leftPosition);
        hardware.climberRight.setPosition(rightPosition);

        hardware.winch1.setPower(winchPower);
        hardware.winch2.setPower(winchPower);

        telemetry();
    }
}