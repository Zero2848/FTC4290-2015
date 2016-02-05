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

    public static double grabberL(Controller controller, double pos) {
        if (controller.x == ButtonState.PRESSED && pos != Hardware.LEFT_GRABBER_DOWN) {
            return Hardware.LEFT_GRABBER_DOWN;
        } else if (controller.x == ButtonState.PRESSED) {
            return Hardware.LEFT_GRABBER_UP;
        }
        return pos;
    }
    public static double grabberR(Controller controller, double pos) {
        if (controller.x == ButtonState.PRESSED && pos != Hardware.RIGHT_GRABBER_DOWN) {
            return Hardware.RIGHT_GRABBER_DOWN;
        } else if (controller.x == ButtonState.PRESSED) {
            return Hardware.RIGHT_GRABBER_UP;
        }
        return pos;
    }
    public static double climberDump(Controller controller, double pos) {
        if (controller.y == ButtonState.PRESSED && pos != Hardware.CLIMBER_TOP) {
            return Hardware.CLIMBER_TOP;
        } else if (controller.y == ButtonState.PRESSED) {
            return Hardware.CLIMBER_BOTTOM;
        }
        return pos;
    }
    public static double leftServo(Controller controller, double pos) {
        if (controller.x == ButtonState.PRESSED && pos != Hardware.LEFT_SERVO_TOP) {
            return Hardware.LEFT_SERVO_TOP;
        } else if (controller.x == ButtonState.PRESSED) {
            return Hardware.LEFT_SERVO_BOTTOM;
        }
        return pos;
    }
    public static double rightServo(Controller controller, double pos) {
        if (controller.b == ButtonState.PRESSED && pos != Hardware.RIGHT_SERVO_TOP) {
            return Hardware.RIGHT_SERVO_TOP;
        } else if (controller.b == ButtonState.PRESSED) {
            return Hardware.RIGHT_SERVO_BOTTOM;
        }
        return pos;
    }
    public static double stopper(Controller controller, double pos) {
        if (controller.a == ButtonState.PRESSED && pos != Hardware.STOPPER_ON) {
            return Hardware.STOPPER_ON;
        } else if (controller.a == ButtonState.PRESSED) {
            return Hardware.STOPPER_OFF;
        }
        return pos;
    }
    public static double winch(Controller controller) {
        if (Math.abs(controller.right_stick_y) > .1) {
            return controller.right_stick_y;
        } else {
            return 0;
        }
    }
    public static double angler(Controller controller) {
        if (controller.y == ButtonState.HELD) {//go up
            return  .75;
        } else if (controller.a == ButtonState.HELD) {//go down
            return -.75;
        } else {
            return 0;
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

        config.leftGrabber.setPosition(grabberL(driver, config.leftGrabber.getPosition()));
        config.rightGrabber.setPosition(grabberR(driver, config.rightGrabber.getPosition()));

        config.climber.setPosition(climberDump(operator, config.climber.getPosition()));

        config.climberLeft.setPosition(leftServo(operator, config.climberLeft.getPosition()));
        config.climberRight.setPosition(rightServo(operator, config.climberRight.getPosition()));

        config.stopper.setPosition(stopper(operator, config.stopper.getPosition()));

        config.winch1.setPower(winch(operator));
        config.winch2.setPower(winch(operator));

        config.angler.setPower(angler(driver));

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