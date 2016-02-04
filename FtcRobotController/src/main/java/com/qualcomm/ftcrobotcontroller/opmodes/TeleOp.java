package com.qualcomm.ftcrobotcontroller.opmodes;

import com.lasarobotics.library.controller.ButtonState;
import com.lasarobotics.library.controller.Controller;
import com.lasarobotics.library.drive.Tank;
import com.qualcomm.ftcrobotcontroller.Hardware;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

public class TeleOp extends OpMode {
    Hardware config;
    Controller driver, operator;

    double winchPower = 0, leftPosition = Hardware.LEFT_SERVO_TOP, rightPosition = Hardware.RIGHT_SERVO_TOP, climberPosition = Hardware.CLIMBER_BOTTOM,
            anglerPower = 0, stopperPosition = Hardware.STOPPER_OFF, leftGPosition = Hardware.LEFT_GRABBER_UP, rightGPosition = Hardware.RIGHT_GRABBER_UP;

    public void grabbers() {
        if (driver.x == ButtonState.PRESSED && leftGPosition != Hardware.LEFT_GRABBER_DOWN) {
            leftGPosition = Hardware.LEFT_GRABBER_DOWN;
            rightGPosition = Hardware.RIGHT_GRABBER_DOWN;
        } else if (driver.x == ButtonState.PRESSED) {
            leftGPosition = Hardware.LEFT_GRABBER_UP;
            rightGPosition = Hardware.RIGHT_GRABBER_UP;
        }
    }

    public void climberDump() {
        if (operator.y == ButtonState.PRESSED && climberPosition != Hardware.CLIMBER_TOP) {
            climberPosition = Hardware.CLIMBER_TOP;
        } else if (operator.y == ButtonState.PRESSED) {
            climberPosition = Hardware.CLIMBER_BOTTOM;
        }

    }

    public void leftServo() {
        if (operator.x == ButtonState.PRESSED && leftPosition != Hardware.LEFT_SERVO_TOP) {
            leftPosition = Hardware.LEFT_SERVO_TOP;
        } else if (operator.x == ButtonState.PRESSED) {
            leftPosition = Hardware.LEFT_SERVO_BOTTOM;
        }
    }

    public void rightServo() {
        if (operator.b == ButtonState.PRESSED && rightPosition != Hardware.RIGHT_SERVO_TOP) {
            rightPosition = Hardware.RIGHT_SERVO_TOP;
        } else if (operator.b == ButtonState.PRESSED) {
            rightPosition = Hardware.RIGHT_SERVO_BOTTOM;
        }
    }

    public void stopper() {
        if (operator.a == ButtonState.PRESSED && stopperPosition != Hardware.STOPPER_ON) {
            stopperPosition = Hardware.STOPPER_ON;
        } else if (operator.a == ButtonState.PRESSED) {
            stopperPosition = Hardware.STOPPER_OFF;
        }
    }

    public void winch() {
        if (Math.abs(operator.right_stick_y) > .1 && stopperPosition != Hardware.STOPPER_ON) {
            winchPower = operator.right_stick_y;
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

        grabbers();
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
        if (leftPosition == Hardware.LEFT_SERVO_TOP) {
            telemetry.addData("Left Servo Is", "Off");
        } else if (leftPosition == Hardware.LEFT_SERVO_BOTTOM) {
            telemetry.addData("Left Servo Is", "On");
        } else {
            telemetry.addData("Left Servo Is", "Not Connected!");
        }

        if (leftGPosition == Hardware.LEFT_GRABBER_UP) {
            telemetry.addData("Left Grabber Is", "Up");
        } else if (leftGPosition == Hardware.LEFT_GRABBER_DOWN) {
            telemetry.addData("Left Grabber Is", "Down");
        } else {
            telemetry.addData("Left Grabber Is", "Not Connected!");
        }
        if (rightGPosition == Hardware.RIGHT_GRABBER_DOWN) {
            telemetry.addData("Right Grabber Is", "Down");
        } else if (rightGPosition == Hardware.RIGHT_GRABBER_UP) {
            telemetry.addData("Right Grabber Is", "Up");
        } else {
            telemetry.addData("Left Grabber Is", "Not Connected!");
        }

        if (rightPosition == Hardware.RIGHT_SERVO_TOP) {
            telemetry.addData("Right Servo Is", "Off");
        } else if (rightPosition == Hardware.RIGHT_SERVO_BOTTOM) {
            telemetry.addData("Right Servo Is", "On");
        } else {
            telemetry.addData("Right Servo Is", "Not Connected!");
        }

        if (climberPosition == Hardware.CLIMBER_BOTTOM) {
            telemetry.addData("Climber Servo Is", "Off");
        } else if (climberPosition == Hardware.CLIMBER_TOP) {
            telemetry.addData("Climber Servo Is", "On");
        } else {
            telemetry.addData("Climber Servo Is", "Not Connected!");
        }
        if (stopperPosition == Hardware.STOPPER_ON) {
            telemetry.addData("Stopper Servo Is", "On");
        } else if (stopperPosition == Hardware.STOPPER_OFF) {
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