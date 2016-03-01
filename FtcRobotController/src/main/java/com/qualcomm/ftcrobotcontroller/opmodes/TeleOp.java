package com.qualcomm.ftcrobotcontroller.opmodes;

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
    boolean grabberdown = false, leftS = false, rightS = false, climberB = false, stopperIsOn = false;

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

        if (operator.a == ButtonState.PRESSED) {
            stopperIsOn = !stopperIsOn;
        }
        if(stopperIsOn){
            config.stopper.setPosition(Hardware.STOPPER_ON);
        } else {
            config.stopper.setPosition(Hardware.STOPPER_OFF);
        }
        telemetry.addData("Stopper", "Off");
        if(stopperIsOn)
            telemetry.addData("Stopper", "On");

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
            telemetry.addData("NON CONVERTED", config.navx.getYaw());
            telemetry.addData("NavX Yaw:", Auto.convertDegNavX(config.navx.getYaw()));
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

        if (operator.x == ButtonState.PRESSED) {
            if (leftS) {
                config.climberLeft.setPosition(Hardware.LEFT_SERVO_TOP);
                leftS = false;
                telemetry.addData("left", "top");
            } else {
                config.climberLeft.setPosition(Hardware.LEFT_SERVO_BOTTOM);
                leftS = true;
                telemetry.addData("right", "low");
            }
        }
        if(operator.dpad_left == ButtonState.PRESSED || operator.dpad_left == ButtonState.HELD){
            config.climberLeft.setPosition(Hardware.LEFT_SERVO_LOWEST);
            telemetry.addData("right", "bottom");
        }
        
        if (operator.b == ButtonState.PRESSED) {
            if (rightS) {
                config.climberRight.setPosition(Hardware.RIGHT_SERVO_TOP);
                telemetry.addData("right", "top");
                rightS = false;
            } else {
                config.climberRight.setPosition(Hardware.RIGHT_SERVO_BOTTOM);
                rightS = true;
                telemetry.addData("right", "low");
            }
        }
        if(operator.dpad_right == ButtonState.PRESSED || operator.dpad_right == ButtonState.HELD){
            config.climberRight.setPosition(Hardware.RIGHT_SERVO_LOWEST);
            telemetry.addData("right", "bottom");
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
        if(!stopperIsOn) {
            if (operator.right_bumper == ButtonState.HELD) {
                telemetry.addData("POWER", "slow");
                config.winch1.setPower(operator.right_stick_y / 3);
                config.winch2.setPower(operator.right_stick_y / 3);
            } else {
                telemetry.addData("POWER", "fast");
                config.winch1.setPower(operator.right_stick_y);
                config.winch2.setPower(operator.right_stick_y);
            }
        } else {
            config.winch1.setPower(0);
            config.winch2.setPower(0);
        }

        config.angler.setPower(angler(driver));

        telemetry.addData("gl", config.leftGrabber.getPosition());
        telemetry.addData("gr", config.rightGrabber.getPosition());

        if(driver.left_bumper == ButtonState.HELD || driver.right_bumper == ButtonState.HELD) {
            telemetry.addData("DRIVE", "SLOW");
            Tank.motor2(config.leftWheel, config.rightWheel, driver.left_stick_y / 4, driver.right_stick_y / 4);
        } else {
            telemetry.addData("DRIVE", "FAST");
            Tank.motor2(config.leftWheel, config.rightWheel, driver.left_stick_y, driver.right_stick_y);
        }

    }
    public void stop() {
        if (Hardware.navxenabled)
            config.navx.close();
    }

}