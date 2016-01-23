package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;

/**
 * Created by Ethan on 1/12/16.
 */

public class redDump extends LinearOpMode {
    int wheelPosition;
    public void resetEncoder(DcMotor m){
        while(m.getCurrentPosition()!=0){
            m.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);
        }
        m.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
    }
    public void declare() throws InterruptedException {
        hardware.leftWheel = hardwareMap.dcMotor.get("l");
        resetEncoder(hardware.leftWheel);
        hardware.rightWheel = hardwareMap.dcMotor.get("r");
        hardware.rightWheel.setDirection(DcMotor.Direction.REVERSE);
        resetEncoder(hardware.rightWheel);
        hardware.winch1 = hardwareMap.dcMotor.get("w1");
        hardware.winch1.setDirection(DcMotor.Direction.REVERSE);
        hardware.winch2 = hardwareMap.dcMotor.get("w2");
        resetEncoder(hardware.winch2);
        hardware.angler = hardwareMap.dcMotor.get("a");
        hardware.angler.setDirection(DcMotor.Direction.REVERSE);
        resetEncoder(hardware.angler);

        hardware.climberLeft = hardwareMap.servo.get("cl");
        hardware.climberLeft.setPosition(hardware.leftServoTop);

        hardware.climberRight = hardwareMap.servo.get("cr");
        hardware.climberRight.setPosition(hardware.rightServoTop);

        hardware.stopper = hardwareMap.servo.get("s");
        hardware.stopper.setPosition(hardware.stopperOff);

        hardware.climber = hardwareMap.servo.get("c");
        hardware.climber.setPosition(hardware.climberBottom);
        waitOneFullHardwareCycle();
    }
    public void driveTo(int ticks, double powerLeft, double powerRight) throws InterruptedException {
        while(wheelPosition != ticks) {
            wheelPosition = hardware.rightWheel.getCurrentPosition();
            telemetry.addData("1", "wheel at: " + wheelPosition + " ticks.");
            if(wheelPosition < ticks) {
                telemetry.addData("2", "tick goal: " + (ticks));
                telemetry.addData("3", "near");
                hardware.leftWheel.setPower(powerLeft);
                hardware.rightWheel.setPower(powerRight);
            }
            else if (wheelPosition > ticks) {
                telemetry.addData("2", "tick goal: " + (ticks));
                telemetry.addData("3", "far");
                hardware.leftWheel.setPower(-powerLeft);
                hardware.rightWheel.setPower(-powerRight);
            } else {
                break;
            }
        }
        stopDrive();
        while (hardware.rightWheel.getCurrentPosition() != 0){
            hardware.rightWheel.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);
        }//gives hardware the time to reset
        hardware.rightWheel.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
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
        driveTo((int)(inches*mod), leftPower, rightPower);
        stopDrive();
    }
    public void stopDrive(){
        hardware.rightWheel.setPower(0);
        hardware.leftWheel.setPower(0);
    }
    public void say(String message){
        telemetry.addData("STATUS", message);
    }
    public void say(String key, String message){
        telemetry.addData(key, message);
    }

    @Override
    public void runOpMode() throws InterruptedException {
        declare();
        waitForStart();

        driveDist(-82, .2, .2);
        say("DRIVEN");

        driveAng(-50, -.2, .2);//left
        say("TURNED");

        hardware.climber.setPosition(hardware.climberTop);
        sleep(200);
        hardware.climber.setPosition(hardware.climberBottom);
        say("DUMPED");


        //UNTESTED CODE STARTS HERE
        driveAng(-50, .2, -.2);//right
        say("REVERSING, TURNING RIGHT");

        driveDist(-36, .2, .2);
        say("LINED UP WITH MOUNTAIN ");


    }
}
