package com.qualcomm.ftcrobotcontroller.opmodes;

import com.lasarobotics.library.controller.ButtonState;
import com.lasarobotics.library.controller.Controller;
import com.lasarobotics.library.drive.Tank;
import com.qualcomm.ftcrobotcontroller.Hardware;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

public class TeleOp extends OpMode {
    Hardware config;
    Controller driver, operator;
    boolean grabberdown = false, leftS = false, rightS = false, climberB = false;

    public static double climberDump(Controller cont, double pos) {
        if (cont.y == ButtonState.PRESSED && pos != Hardware.CLIMBER_TOP) {
            return Hardware.CLIMBER_TOP;
        } else if (cont.y == ButtonState.PRESSED) {
            return Hardware.CLIMBER_BOTTOM;
        }
        return pos;
    }
    public static double leftServo(Controller cont, double pos) {
        if (cont.x == ButtonState.PRESSED && pos != Hardware.LEFT_SERVO_TOP) {
            return Hardware.LEFT_SERVO_TOP;
        } else if (cont.x == ButtonState.PRESSED) {
            return Hardware.LEFT_SERVO_BOTTOM;
        }
        return pos;
    }
    public static double rightServo(Controller cont, double pos) {
        if (cont.b == ButtonState.PRESSED && pos != Hardware.RIGHT_SERVO_TOP) {
            return Hardware.RIGHT_SERVO_TOP;
        } else if (cont.b == ButtonState.PRESSED) {
            return Hardware.RIGHT_SERVO_BOTTOM;
        }
        return pos;
    }
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
    }

    @Override
    public void loop() {
        housekeeping();

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
            }
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

//        config.climber.setPosition(climberDump(operator, config.climber.getPosition()));

//        config.climberLeft.setPosition(leftServo(operator, config.climberLeft.getPosition()));
//        config.climberRight.setPosition(rightServo(operator, config.climberRight.getPosition()));

        config.stopper.setPosition(stopper(operator, config.stopper.getPosition()));

        if(operator.right_bumper == ButtonState.HELD) {
            config.winch1.setPower(winch(operator) / 4);
            config.winch2.setPower(winch(operator) / 4);
        } else {
            config.winch1.setPower(winch(operator));
            config.winch2.setPower(winch(operator));
        }

        config.angler.setPower(angler(driver));
        telemetry.addData("gl",config.leftGrabber.getPosition());
        telemetry.addData("gr", config.rightGrabber.getPosition());

        if(driver.left_bumper == ButtonState.HELD || driver.right_bumper == ButtonState.HELD) {
            Tank.motor2(config.leftWheel, config.rightWheel, driver.left_stick_y / 4, driver.right_stick_y / 4);
        } else {
            Tank.motor2(config.leftWheel, config.rightWheel, driver.left_stick_y, driver.right_stick_y);
        }

    }
}