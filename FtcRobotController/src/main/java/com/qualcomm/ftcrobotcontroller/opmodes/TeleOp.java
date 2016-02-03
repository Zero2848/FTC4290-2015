package com.qualcomm.ftcrobotcontroller.opmodes;

import com.lasarobotics.library.controller.ButtonState;
import com.lasarobotics.library.controller.Controller;
import com.lasarobotics.library.drive.Tank;
import com.qualcomm.ftcrobotcontroller.Hardware;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;

public class TeleOp extends OpMode {
    Hardware config;
    Controller driver, operator;

    double winchPower = 0, leftPosition = Hardware.leftServoTop, rightPosition = Hardware.rightServoTop, climberPosition = Hardware.climberBottom,
            anglerPower = 0, stopperPosition = Hardware.stopperOff, leftGPosition = Hardware.leftGUp, rightGPosition = Hardware.rightGUp;

    public void grabbers() {
        if (driver.x == ButtonState.PRESSED && leftGPosition != Hardware.leftGDown) {
            leftGPosition = Hardware.leftGDown;
            rightGPosition = Hardware.rightGDown;
        } else if (driver.x == ButtonState.PRESSED) {
            leftGPosition = Hardware.leftGUp;
            rightGPosition = Hardware.rightGUp;
        }
    }

    public void climberDump() {
        if (operator.y == ButtonState.PRESSED && climberPosition != Hardware.climberTop) {
            climberPosition = Hardware.climberTop;
        } else if (operator.y == ButtonState.PRESSED) {
            climberPosition = Hardware.climberBottom;
        }

    }

    public void leftServo() {
        if (operator.x == ButtonState.PRESSED && leftPosition != Hardware.leftServoTop) {
            leftPosition = Hardware.leftServoTop;
        } else if (operator.x == ButtonState.PRESSED) {
            leftPosition = Hardware.leftServoBottom;
        }
    }

    public void rightServo() {
        if (operator.b == ButtonState.PRESSED && rightPosition != Hardware.rightServoTop) {
            rightPosition = Hardware.rightServoTop;
        } else if (operator.b == ButtonState.PRESSED) {
            rightPosition = Hardware.rightServoBottom;
        }
    }

    public void stopper() {
        if (operator.a == ButtonState.PRESSED && stopperPosition != Hardware.stopperOn) {
            stopperPosition = Hardware.stopperOn;
        } else if (operator.a == ButtonState.PRESSED) {
            stopperPosition = Hardware.stopperOff;
        }
    }

    public void winch() {
        if (Math.abs(operator.right_stick_y) > .1 && stopperPosition != Hardware.stopperOn) {
            if (config.winch2.getCurrentPosition() < Hardware.winchCap && operator.right_stick_y > 0) {
                winchPower = operator.right_stick_y;
            } else if (config.winch2.getCurrentPosition() > Hardware.winchLow && operator.right_stick_y < 0) {
                winchPower = operator.right_stick_y;
            } else {
                winchPower = 0;
            }
        } else {
            winchPower = 0;
        }
    }

    public void angler() {
        if (driver.y == ButtonState.HELD) {//go up
            anglerPower = .75;
        } else if (driver.a == ButtonState.HELD) {//go down
            anglerPower = -.75;
        } else {
            anglerPower = 0;
        }
    }

    public void housekeeping() {
        driver.update(gamepad1);
        operator.update(gamepad2);
        displayTelemetry();
    }

    @Override
    public void init() {
        config = new Hardware(hardwareMap);
        driver = new Controller(gamepad1);
        operator = new Controller(gamepad2);
    }

    @Override
    public void loop() {
        housekeeping();

        grabbers();//source of error
        config.rightGrabber.setPosition(rightGPosition);
        config.leftGrabber.setPosition(leftGPosition);

        climberDump();
        config.climber.setPosition(climberPosition);

        leftServo();
        config.climberLeft.setPosition(leftPosition);

        rightServo();
        config.climberRight.setPosition(rightPosition);

        stopper();
        config.stopper.setPosition(stopperPosition);

        winch();
        config.winch1.setPower(winchPower);
        config.winch2.setPower(winchPower);

        angler();
        config.angler.setPower(anglerPower);

        Tank.motor2(config.leftWheel, config.rightWheel, driver.left_stick_y, driver.right_stick_y);
    }

    public void displayTelemetry() {
        if (leftPosition == Hardware.leftServoTop) {
            telemetry.addData("Left Servo Is", "Off");
        } else if (leftPosition == Hardware.leftServoBottom) {
            telemetry.addData("Left Servo Is", "On");
        } else {
            telemetry.addData("Left Servo Is", "Not Connected!");
        }

        if (leftGPosition == Hardware.leftGUp) {
            telemetry.addData("Left Grabber Is", "Up");
        } else if (leftGPosition == Hardware.leftGDown) {
            telemetry.addData("Left Grabber Is", "Down");
        } else {
            telemetry.addData("Left Grabber Is", "Not Connected!");
        }
        if (rightGPosition == Hardware.rightGDown) {
            telemetry.addData("Right Grabber Is", "Down");
        } else if (rightGPosition == Hardware.rightGUp) {
            telemetry.addData("Right Grabber Is", "Up");
        } else {
            telemetry.addData("Left Grabber Is", "Not Connected!");
        }

        if (rightPosition == Hardware.rightServoTop) {
            telemetry.addData("Right Servo Is", "Off");
        } else if (rightPosition == Hardware.rightServoBottom) {
            telemetry.addData("Right Servo Is", "On");
        } else {
            telemetry.addData("Right Servo Is", "Not Connected!");
        }

        if (climberPosition == Hardware.climberBottom) {
            telemetry.addData("Climber Servo Is", "Off");
        } else if (climberPosition == Hardware.climberTop) {
            telemetry.addData("Climber Servo Is", "On");
        } else {
            telemetry.addData("Climber Servo Is", "Not Connected!");
        }
        if (stopperPosition == Hardware.stopperOn) {
            telemetry.addData("Stopper Servo Is", "On");
        } else if (stopperPosition == Hardware.stopperOff) {
            telemetry.addData("Stopper Servo Is", "Off");
        }
        telemetry.addData("Left Motor Encoder: ", config.leftWheel.getCurrentPosition());
        telemetry.addData("Right Motor Encoder: ", config.rightWheel.getCurrentPosition());
        telemetry.addData("Winch2 Encoder: ", config.winch2.getCurrentPosition());
        telemetry.addData("Left Motor Power: ", driver.left_stick_y);
        telemetry.addData("Right Motor Power: ", driver.right_stick_y);
        telemetry.addData("Winch Power: ", winchPower);
        telemetry.addData("Angler Power:", anglerPower);
    }
}