package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.ftcrobotcontroller.Hardware;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotorController;


public class Auto extends LinearOpMode {
    Hardware config;

    public void moveArm() {
        for (double i = Hardware.climberBottom; i > Hardware.climberTop; i -= .01) {
            config.climber.setPosition(i);
        }
    }


    public void driveTo(int ticks, double powerLeft, double powerRight) throws InterruptedException {
        double wheelPosition = 0;
        while (wheelPosition != ticks) {
            wheelPosition = config.rightWheel.getCurrentPosition();
            telemetry.addData("1", "wheel at: " + wheelPosition + " ticks.");
            if (wheelPosition < ticks) {
                telemetry.addData("2", "tick goal: " + (ticks));
                telemetry.addData("3", "near");
                config.leftWheel.setPower(powerLeft);
                config.rightWheel.setPower(powerRight);
            } else if (wheelPosition > ticks) {
                telemetry.addData("2", "tick goal: " + (ticks));
                telemetry.addData("3", "far");
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
        config.rightWheel.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        waitOneFullHardwareCycle();
    }

    public void driveAng(int angle, double leftPower, double rightPower) throws InterruptedException {
        double mod = 34;
        driveTo((int) (angle * mod), leftPower, rightPower);
    }

    public void driveDist(int inches, double leftPower, double rightPower) throws InterruptedException {
        double mod = 80;
        driveTo((int) (inches * mod), leftPower, rightPower);
    }

    public void stopDrive() {
        config.rightWheel.setPower(0);
        config.leftWheel.setPower(0);
    }

    @Override
    public void runOpMode() throws InterruptedException {
        config = new Hardware(hardwareMap);
        config.rightWheel.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        waitOneFullHardwareCycle();
        waitForNextHardwareCycle();
        config.rightWheel.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        waitForStart();
        moveArm();
    }

}
