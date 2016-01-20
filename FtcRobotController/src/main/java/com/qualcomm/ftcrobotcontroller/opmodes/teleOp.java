package com.qualcomm.ftcrobotcontroller.opmodes;

import com.lasarobotics.library.controller.ButtonState;
import com.lasarobotics.library.controller.Controller;
import com.lasarobotics.library.drive.Tank;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import com.qualcomm.ftcrobotcontroller.opmodes.hardware.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by Ethan on 1/12/16.
 */

public class teleOp extends OpMode {
    Controller driver, operator;
    float winchPower;
    double leftPosition = hardware.leftServoTop, rightPosition = hardware.rightServoTop, climberPosition = hardware.climberTop, anglerPower, stopperPosition = hardware.stopperOff;


    public void displayTelemetry(){
        if(leftPosition == hardware.leftServoTop){
            telemetry.addData("1", "left is off");
        } else if(leftPosition == hardware.leftServoBottom){
            telemetry.addData("1", "left is on");
        } else {
            telemetry.addData("1", "no left servo");
        }

        if(rightPosition == hardware.rightServoTop){
            telemetry.addData("2", "right is off");
        } else if(rightPosition == hardware.rightServoBottom){
            telemetry.addData("2", "right is on");
        } else {
            telemetry.addData("2", "no right servo");
        }

        if(climberPosition == hardware.climberBottom){
            telemetry.addData("3", "climber is off");
        } else if(climberPosition == hardware.climberTop){
            telemetry.addData("3", "climber is on");
        } else {
            telemetry.addData("3", "no climber servo");
        }
        if(stopperPosition == hardware.stopperOn){
            telemetry.addData("4", "stopper is on");
        } else if(stopperPosition == hardware.stopperOff){
            telemetry.addData("4", "stopper is off");
        } else {
            telemetry.addData("4", "no stopper");
        }
        telemetry.addData("5", "left motor enc: " + hardware.leftWheel.getCurrentPosition());
        telemetry.addData("6", "right motor enc: " + hardware.rightWheel.getCurrentPosition());
        telemetry.addData("7", "winch1 enc: " + hardware.winch1.getCurrentPosition());
        telemetry.addData("8", "winch2 enc: " + hardware.winch2.getCurrentPosition());
        telemetry.addData("9", "angler enc: " + hardware.angler.getCurrentPosition());
        telemetry.addData("10", "left motor power: " + driver.left_stick_y);
        telemetry.addData("11", "right motor power: " + driver.right_stick_y);
        telemetry.addData("12", "winch pow: " + winchPower);
        telemetry.addData("13", "angler pow:" + anglerPower);
    }
    /*public void displayTelemetry(){
        telemetry.addData("1", "power:" + winchPower);
    }*/

    @Override
    public void init() {
        hardware.leftWheel = hardwareMap.dcMotor.get("l");
        hardware.leftWheel.setDirection(DcMotor.Direction.REVERSE);
        hardware.rightWheel = hardwareMap.dcMotor.get("r");
        hardware.winch1 = hardwareMap.dcMotor.get("w1");
        hardware.winch1.setDirection(DcMotor.Direction.REVERSE);
        hardware.winch2 = hardwareMap.dcMotor.get("w2");
        hardware.angler = hardwareMap.dcMotor.get("a");
        hardware.angler.setDirection(DcMotor.Direction.REVERSE);
        hardware.climberLeft = hardwareMap.servo.get("cl");
        hardware.climberRight = hardwareMap.servo.get("cr");
        hardware.stopper = hardwareMap.servo.get("s");
        hardware.climber = hardwareMap.servo.get("c");
        driver = new Controller(gamepad1);
        operator = new Controller(gamepad2);
        hardware.angler.setDirection(DcMotor.Direction.REVERSE);

    }

    @Override
    public void loop() {
        driver.update(gamepad1);
        operator.update(gamepad2);

        if(operator.y == ButtonState.PRESSED && climberPosition != hardware.climberTop) {
            climberPosition=hardware.climberTop;
        } else if(operator.y == ButtonState.PRESSED && climberPosition != hardware.climberBottom) {
            climberPosition=hardware.climberBottom;
        }

        if(operator.x  == ButtonState.PRESSED && leftPosition != hardware.leftServoTop) {
            leftPosition=hardware.leftServoTop;
        } else if(operator.x == ButtonState.PRESSED && leftPosition != hardware.leftServoBottom) {
            leftPosition=hardware.leftServoBottom;
        }
        if(operator.b == ButtonState.PRESSED && rightPosition != hardware.rightServoTop) {
            rightPosition=hardware.rightServoTop;
        } else if(operator.b == ButtonState.PRESSED && rightPosition != hardware.rightServoBottom) {
            rightPosition=hardware.rightServoBottom;
        }

        if(operator.a == ButtonState.PRESSED && stopperPosition != hardware.stopperOn) {
            stopperPosition = hardware.stopperOn;
        } else if(operator.a == ButtonState.PRESSED && stopperPosition != hardware.stopperOff) {
            stopperPosition = hardware.stopperOff;
        }

        if(Math.abs(operator.right_stick_y) > .05 && stopperPosition != hardware.stopperOn) {
            if(hardware.winch2.getCurrentPosition() < hardware.winchCap && operator.right_stick_y > 0){
                winchPower = operator.right_stick_y;
            }
            else if(hardware.winch2.getCurrentPosition() > hardware.winchLow && operator.right_stick_y < 0){
                winchPower = operator.right_stick_y;
            } else {
                winchPower = 0;
            }
        } else {
            winchPower = 0;
        }
        if(driver.right_bumper == ButtonState.PRESSED) {
            if (hardware.angler.getCurrentPosition() < hardware.anglerCap) {
                anglerPower += 0.1;
            }
        }
        if(driver.right_trigger == ButtonState.PRESSED){
            if(hardware.angler.getCurrentPosition() > hardware.anglerLow) {
                anglerPower -= 0.1;
            }
        }
        if(driver.right_trigger == ButtonState.NOT_PRESSED && driver.right_bumper == ButtonState.NOT_PRESSED){
            anglerPower = 0;
        }

        hardware.stopper.setPosition(stopperPosition);
        hardware.climber.setPosition(climberPosition);
        hardware.climberLeft.setPosition(leftPosition);
        hardware.climberRight.setPosition(rightPosition);

        hardware.winch1.setPower(winchPower);
        hardware.winch2.setPower(winchPower);
        hardware.angler.setPower(anglerPower);
        Tank.Motor2(hardware.leftWheel, hardware.rightWheel, driver.left_stick_y, driver.right_stick_y);


        displayTelemetry();
    }

}