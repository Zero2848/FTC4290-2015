package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.ftcrobotcontroller.Hardware;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;

public class RedMountain extends LinearOpMode {
    public final double leftPow = .45;
    public final double rightPow = .4;
    Hardware config;
    int wheelPosition;

    public void resetEncoder(DcMotor m) {
        while (m.getCurrentPosition() != 0) {
            m.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        }
        m.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
    }

    public void driveTo(int ticks, double powerLeft, double powerRight) throws InterruptedException {
        while (wheelPosition != ticks) {
            wheelPosition = ((Math.abs(config.rightWheel.getCurrentPosition()) + Math.abs(config.leftWheel.getCurrentPosition())) / 2);
            telemetry.addData("wheel at", wheelPosition + " ticks.");
            if (wheelPosition < ticks) {
                telemetry.addData("tick goal:", ticks);
                config.leftWheel.setPower(powerLeft);
                config.rightWheel.setPower(powerRight);
            } else if (wheelPosition > ticks) {
                telemetry.addData("tick goal", ticks);
                config.leftWheel.setPower(-powerLeft);
                config.rightWheel.setPower(-powerRight);
            } else {
                break;
            }
        }

        stopDrive();
        while (config.rightWheel.getCurrentPosition() != 0) {
            config.rightWheel.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        }//gives Hardware the time to reset
        while (config.leftWheel.getCurrentPosition() != 0) {
            config.leftWheel.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        }//gives Hardware the time to reset

        config.rightWheel.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        config.leftWheel.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        waitOneFullHardwareCycle();
        stopDrive();
    }

    public void driveDist(int inches, double leftPower, double rightPower) throws InterruptedException {
        double mod = 80;
        driveTo((int) (inches * mod), leftPower, rightPower);
        stopDrive();
    }

    public void stopDrive() {
        config.rightWheel.setPower(0);
        config.leftWheel.setPower(0);
    }

    public void say(String message) {
        telemetry.addData("STATUS", message);
    }

    @Override
    public void runOpMode() throws InterruptedException {
        config = new Hardware(hardwareMap);
        config.rightGrabber.setPosition(Hardware.rightGUp);
        config.leftGrabber.setPosition(Hardware.leftGUp);
        waitForStart();

        driveDist(52, leftPow, rightPow);
        say("DRIVEN");

        sleep(100);

        driveDist(15, -leftPow, rightPow);
        say("TURNED");

        sleep(100);

        resetEncoder(config.leftWheel);
        resetEncoder(config.rightWheel);
        config.rightWheel.setPower(-.75);
        config.leftWheel.setPower(-.8);
        wheelPosition = config.leftWheel.getCurrentPosition() + config.rightWheel.getCurrentPosition() / 2;

        while (wheelPosition > -2000) {
            wheelPosition = config.leftWheel.getCurrentPosition() + config.rightWheel.getCurrentPosition() / 2;
            say("" + wheelPosition);
        }
        config.rightGrabber.setPosition(Hardware.rightGDown);
        config.leftGrabber.setPosition(Hardware.leftGDown);
        waitOneFullHardwareCycle();
        say("GRABBED");

        sleep(500);
        stopDrive();
        say("DONE");

    }
}
