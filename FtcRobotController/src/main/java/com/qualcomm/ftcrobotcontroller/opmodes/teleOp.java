package com.qualcomm.ftcrobotcontroller.opmodes;

import com.lasarobotics.library.controller.ButtonState;
import com.lasarobotics.library.controller.Controller;
import com.lasarobotics.library.drive.Tank;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by Ethan on 1/12/16.
 */

public class teleOp extends OpMode {
    Controller driver, operator;
    double winchPower = 0, leftPosition = hardware.leftServoTop, rightPosition = hardware.rightServoTop, climberPosition = hardware.climberTop,
            anglerPower = 0, stopperPosition = hardware.stopperOff, leftGPosition = hardware.leftGUp, rightGPosition = hardware.rightGUp;

    public void displayTelemetry(){
        try{
            telemetry.addData("Touch", hardware.touchSensor.getValue());
        } catch (Exception e){
            telemetry.addData("Touch", "Not attached!");
        }
        try{
            telemetry.addData("Potentiometer", hardware.potentiometer.getValue());
        } catch (Exception e){
            telemetry.addData("Potentiometer", "Not attached!");
        }

        if(leftPosition == hardware.leftServoTop){
            telemetry.addData("Left Servo Is", "Off");
        } else if(leftPosition == hardware.leftServoBottom){
            telemetry.addData("Left Servo Is", "On");
        } else {
            telemetry.addData("Left Servo Is", "Not Connected!");
        }

        if(leftGPosition == hardware.leftGUp){
            telemetry.addData("Left Grabber Is", "Up");
        } else if(leftGPosition == hardware.leftGDown){
            telemetry.addData("Left Grabber Is", "Down");
        } else {
            telemetry.addData("Left Grabber Is", "Not Connected!");
        }
        if(rightGPosition == hardware.rightGDown){
            telemetry.addData("Right Grabber Is", "Down");
        } else if(rightGPosition == hardware.rightGUp){
            telemetry.addData("Right Grabber Is", "Up");
        } else {
            telemetry.addData("Left Grabber Is", "Not Connected!");
        }

        if(rightPosition == hardware.rightServoTop){
            telemetry.addData("Right Servo Is", "Off");
        } else if(rightPosition == hardware.rightServoBottom){
            telemetry.addData("Right Servo Is", "On");
        } else {
            telemetry.addData("Right Servo Is", "Not Connected!");
        }

        if(climberPosition == hardware.climberBottom){
            telemetry.addData("Climber Servo Is", "Off");
        } else if(climberPosition == hardware.climberTop){
            telemetry.addData("Climber Servo Is", "On");
        } else {
            telemetry.addData("Climber Servo Is", "Not Connected!");
        }
        if(stopperPosition == hardware.stopperOn){
            telemetry.addData("Stopper Servo Is", "On");
        } else if(stopperPosition == hardware.stopperOff){
            telemetry.addData("Stopper Servo Is", "Off");
        }
        telemetry.addData("Left Motor Encoder: ", hardware.leftWheel.getCurrentPosition());
        telemetry.addData("Right Motor Encoder: ", hardware.rightWheel.getCurrentPosition());
        telemetry.addData("Winch2 Encoder: ", hardware.winch2.getCurrentPosition());
        telemetry.addData("Angler Encoder: ", hardware.angler.getCurrentPosition());
        telemetry.addData("Left Motor Power: ", driver.left_stick_y);
        telemetry.addData("Right Motor Power: ", driver.right_stick_y);
        telemetry.addData("Winch Power: ", winchPower);
        telemetry.addData("Angler Power:", anglerPower);
    }

    @Override
    public void init(){
        hardware.touchSensor = hardwareMap.analogInput.get("touch");
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
        hardware.climberRight.setPosition(hardware.rightServoTop);
        hardware.climberLeft.setPosition(hardware.leftServoTop);
        hardware.angler.setDirection(DcMotor.Direction.REVERSE);
        try {
            hardware.potentiometer = hardwareMap.analogInput.get("pot");
        } catch (Exception e){
            telemetry.addData("potentiometer", "potentiometer is off!");
        }
        try {
            hardware.touchSensor = hardwareMap.analogInput.get("touch");
        } catch (Exception e){
            telemetry.addData("touch", "touch is off!");
        }
        hardware.rightGrabber = hardwareMap.servo.get("rg");
        hardware.leftGrabber = hardwareMap.servo.get("lg");
        driver = new Controller(gamepad1);
        operator = new Controller(gamepad2);
    }

    public void grabbers(){
        if(driver.a == ButtonState.PRESSED && leftGPosition != hardware.leftGDown) {
            leftGPosition=hardware.leftGDown;
            rightGPosition=hardware.rightGDown;
        } else if(driver.a == ButtonState.PRESSED && leftGPosition != hardware.leftGUp) {
            leftGPosition=hardware.leftGUp;
            rightGPosition=hardware.rightGUp;
        }
    }
    public void climberDump(){
        if(operator.y == ButtonState.PRESSED && climberPosition != hardware.climberTop) {
            climberPosition=hardware.climberTop;
        } else if(operator.y == ButtonState.PRESSED && climberPosition != hardware.climberBottom) {
            climberPosition=hardware.climberBottom;
        }

    }
    public void leftServo(){
        if(operator.x == ButtonState.PRESSED && leftPosition != hardware.leftServoTop) {
            leftPosition=hardware.leftServoTop;
        } else if(operator.x == ButtonState.PRESSED && leftPosition != hardware.leftServoBottom) {
            leftPosition=hardware.leftServoBottom;
        }
    }
    public void rightServo(){
        if(operator.b == ButtonState.PRESSED && rightPosition != hardware.rightServoTop) {
            rightPosition=hardware.rightServoTop;
        } else if(operator.b == ButtonState.PRESSED && rightPosition != hardware.rightServoBottom) {
            rightPosition=hardware.rightServoBottom;
        }
    }
    public void stopper(){
        if(operator.a == ButtonState.PRESSED && stopperPosition != hardware.stopperOn) {
            stopperPosition = hardware.stopperOn;
        } else if(operator.a == ButtonState.PRESSED && stopperPosition != hardware.stopperOff) {
            stopperPosition = hardware.stopperOff;
        }
    }
    public void winch(){
        if(Math.abs(operator.right_stick_y) > .1 && stopperPosition != hardware.stopperOn) {
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
    }
    public void angler(){
        if(driver.right_bumper == ButtonState.HELD){
            anglerPower = .75;
        } else if(driver.left_bumper == ButtonState.HELD && hardware.touchSensor.getValue() == 0){
            anglerPower = -.75;
        } else {
            anglerPower = 0;
        }
    }


    @Override
    public void loop() {
        driver.update(gamepad1);
        operator.update(gamepad2);

        grabbers();
        climberDump();
        leftServo();
        rightServo();
        stopper();
        winch();
         angler();

        hardware.stopper.setPosition(stopperPosition);
        hardware.climber.setPosition(climberPosition);
        hardware.climberLeft.setPosition(leftPosition);
        hardware.climberRight.setPosition(rightPosition);

        hardware.rightGrabber.setPosition(rightGPosition);
        hardware.leftGrabber.setPosition(leftGPosition);

        hardware.winch1.setPower(winchPower);
        hardware.winch2.setPower(winchPower);
        hardware.angler.setPower(anglerPower);
        Tank.Motor2(hardware.leftWheel, hardware.rightWheel, driver.left_stick_y, driver.right_stick_y);


        displayTelemetry();
    }

}