package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.ftcrobotcontroller.Hardware;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;

public class RedDump extends LinearOpMode {
    int wheelPosition;
    Hardware config;

    public void resetEncoder(DcMotor m) {
        while (m.getCurrentPosition() != 0) {
            m.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        }
        m.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
    }

    public void driveTo(int ticks, double powerLeft, double powerRight) throws InterruptedException {
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
        stopDrive();
    }

    public void driveAng(int angle, double leftPower, double rightPower) throws InterruptedException {
        double mod = 34;
        driveTo((int) (angle * mod), leftPower, rightPower);
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

        waitForStart();

        driveDist(-82, .2, .2);
        say("DRIVEN");

        driveAng(-50, -.2, .2);//left
        say("TURNED");

        config.climber.setPosition(Hardware.climberTop);
        sleep(200);
        config.climber.setPosition(Hardware.climberBottom);
        say("DUMPED");


        //UNTESTED CODE STARTS HERE
        driveAng(-50, .2, -.2);//right
        say("REVERSING, TURNING RIGHT");

        driveDist(-36, .2, .2);
        say("LINED UP WITH MOUNTAIN ");


    }
}
