package com.qualcomm.ftcrobotcontroller.opmodes;

import android.util.Log;

import com.lasarobotics.library.controller.ButtonState;
import com.lasarobotics.library.controller.Controller;
import com.lasarobotics.library.drive.Tank;
import com.qualcomm.ftcrobotcontroller.Auto;
import com.qualcomm.ftcrobotcontroller.Hardware;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

public class TeleOp extends OpMode {
    Hardware config;
    Controller driver, operator;
    boolean grabberdown = false, leftS = false, rightS = false, climberB = false;

    public static double stopper(Controller cont, double pos) {
        if (cont.a == ButtonState.PRESSED && pos != Hardware.STOPPER_ON) {
            return Hardware.STOPPER_ON;
        } else if (cont.a == ButtonState.PRESSED) {
            return Hardware.STOPPER_OFF;
        }
        return pos;
    }
    public static double winch(Controller cont) {
        if (Math.abs(cont.right_stick_y) > .1) {
            return cont.right_stick_y;
        } else {
            return 0;
        }
    }
    public static double angler(Controller cont) {
        if (cont.y == ButtonState.HELD) {//go up
            return  .75;
        } else if (cont.a == ButtonState.HELD) {//go down
            return -.75;
        } else {
            return 0;
        }
    }
    public void housekeeping() {
        driver.update(gamepad1);
        operator.update(gamepad2);
    }

    @Override
    public void init() {
        config = new Hardware(hardwareMap);
        driver = new Controller();
        operator = new Controller();

        config.leftGrabber.setPosition(Hardware.LEFT_GRABBER_UP);
        config.rightGrabber.setPosition(Hardware.RIGHT_GRABBER_UP);
        config.navx.zeroYaw();
    }

    @Override
    public void loop() {
        housekeeping();

        if(driver.guide == ButtonState.PRESSED){
            if(config.rightWheel.getDirection() == DcMotor.Direction.FORWARD){
                config.rightWheel.setDirection(DcMotor.Direction.REVERSE);
            } else {
                config.rightWheel.setDirection(DcMotor.Direction.FORWARD);
            }
            if(config.leftWheel.getDirection() == DcMotor.Direction.FORWARD){
                config.leftWheel.setDirection(DcMotor.Direction.REVERSE);
            } else {
                config.leftWheel.setDirection(DcMotor.Direction.FORWARD);
            }
        }

        if(Hardware.navxenabled){

            telemetry.addData("NavX Yaw:", Auto.convertDegNavX(config.navx.getYaw()));
            telemetry.addData("NavX", config.navx.toString());
        }

        if (driver.x == ButtonState.PRESSED){
            if (grabberdown){
                config.leftGrabber.setPosition(Hardware.LEFT_GRABBER_UP);
                config.rightGrabber.setPosition(Hardware.RIGHT_GRABBER_UP);
                grabberdown = false;
            }
            else{
                config.leftGrabber.setPosition(Hardware.LEFT_GRABBER_DOWN);
                config.rightGrabber.setPosition(Hardware.RIGHT_GRABBER_DOWN);
                grabberdown = true;
            }
        }

        if (operator.x == ButtonState.PRESSED){
            if (leftS){
                config.climberLeft.setPosition(Hardware.LEFT_SERVO_TOP);
                leftS = false;
            }
            else{
                config.climberLeft.setPosition(Hardware.LEFT_SERVO_BOTTOM);
                leftS = true;
            }} else if(operator.dpad_left == ButtonState.PRESSED){
            config.climberLeft.setPosition(Hardware.LEFT_SERVO_LOWEST);
        }


        if (operator.b == ButtonState.PRESSED){
            if (rightS){
                config.climberRight.setPosition(Hardware.RIGHT_SERVO_TOP);
                rightS = false;
            }
            else{
                config.climberRight.setPosition(Hardware.RIGHT_SERVO_BOTTOM);
                rightS = true;
            }
        } else if(operator.dpad_right == ButtonState.PRESSED){
            config.climberRight.setPosition(Hardware.RIGHT_SERVO_LOWEST);
        }


        if (operator.y == ButtonState.PRESSED){
            if (climberB){
                config.climber.setPosition(Hardware.CLIMBER_BOTTOM);
                climberB = false;
            }
            else{
                config.climber.setPosition(Hardware.CLIMBER_TOP);
                climberB = true;
            }
        }
        config.stopper.setPosition(stopper(operator, config.stopper.getPosition()));

        if(operator.right_bumper == ButtonState.HELD) {
            telemetry.addData("POWER", "slow");
            config.winch1.setPower(operator.right_stick_y/2);
            config.winch2.setPower(operator.right_stick_y/2);
        } else {
            telemetry.addData("POWER", "fast");
            config.winch1.setPower(operator.right_stick_y);
            config.winch2.setPower(operator.right_stick_y);
        }

        config.angler.setPower(angler(driver));
        telemetry.addData("gl",config.leftGrabber.getPosition());
        telemetry.addData("gr", config.rightGrabber.getPosition());

        if(driver.left_bumper == ButtonState.HELD || driver.right_bumper == ButtonState.HELD) {
            telemetry.addData("DRIVE", "SLOW");
            Tank.motor2(config.leftWheel, config.rightWheel, driver.left_stick_y / 4, driver.right_stick_y / 4);
        } else {
            telemetry.addData("DRIVE", "FAST");
            Tank.motor2(config.leftWheel, config.rightWheel, driver.left_stick_y, driver.right_stick_y);
        }

    }
}