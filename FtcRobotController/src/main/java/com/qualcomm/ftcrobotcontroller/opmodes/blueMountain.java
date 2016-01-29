package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by Ethan on 1/12/16.
 */

public class blueMountain extends LinearOpMode {
    int wheelPosition;
    public double leftPow = .45, rightPow = .4;

    public void resetEncoder(DcMotor m){
        while(m.getCurrentPosition()!=0){
            m.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);
        }
        m.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
    }
    public void declare() throws InterruptedException {
        hardware.leftWheel = hardwareMap.dcMotor.get("l");
        hardware.leftWheel.setDirection(DcMotor.Direction.REVERSE);
        resetEncoder(hardware.leftWheel);
        hardware.rightWheel = hardwareMap.dcMotor.get("r");
        resetEncoder(hardware.rightWheel);
        hardware.winch1 = hardwareMap.dcMotor.get("w1");
        hardware.winch1.setDirection(DcMotor.Direction.REVERSE);
        hardware.winch2 = hardwareMap.dcMotor.get("w2");
        resetEncoder(hardware.winch2);
        hardware.angler = hardwareMap.dcMotor.get("a");
        hardware.angler.setDirection(DcMotor.Direction.REVERSE);

        hardware.climberLeft = hardwareMap.servo.get("cl");
        hardware.climberLeft.setPosition(hardware.leftServoTop);

        hardware.climberRight = hardwareMap.servo.get("cr");
        hardware.climberRight.setPosition(hardware.rightServoTop);

        hardware.stopper = hardwareMap.servo.get("s");
        hardware.stopper.setPosition(hardware.stopperOff);

        hardware.climber = hardwareMap.servo.get("c");
        hardware.climber.setPosition(hardware.climberBottom);

        hardware.leftGrabber = hardwareMap.servo.get("lg");
        hardware.rightGrabber = hardwareMap.servo.get("rg");
        waitOneFullHardwareCycle();
    }
    public void driveTo(int ticks, double powerLeft, double powerRight) throws InterruptedException {
        while(wheelPosition != ticks) {
            wheelPosition = ((Math.abs(hardware.rightWheel.getCurrentPosition())+Math.abs(hardware.leftWheel.getCurrentPosition()))/2);
            telemetry.addData("wheel at", wheelPosition + " ticks.");
            if(wheelPosition < ticks) {
                telemetry.addData("tick goal:", ticks);
                hardware.leftWheel.setPower(powerLeft);
                hardware.rightWheel.setPower(powerRight);
            }
            else if (wheelPosition > ticks) {
                telemetry.addData("tick goal", ticks);
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
        while (hardware.leftWheel.getCurrentPosition() != 0){
            hardware.leftWheel.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);
        }//gives hardware the time to reset

        hardware.rightWheel.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        hardware.leftWheel.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        waitOneFullHardwareCycle();
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

    @Override
    public void runOpMode() throws InterruptedException {
        declare();
        hardware.rightGrabber.setPosition(hardware.rightGUp);
        hardware.leftGrabber.setPosition(hardware.leftGUp);
        waitForStart();

        driveDist(52, leftPow, rightPow);
        say("DRIVEN");

        sleep(100);

        driveDist(15, leftPow, -rightPow);
        say("TURNED");

        sleep(100);

        resetEncoder(hardware.leftWheel);
        resetEncoder(hardware.rightWheel);
        hardware.rightWheel.setPower(-.75);
        hardware.leftWheel.setPower(-.8);
        wheelPosition = hardware.leftWheel.getCurrentPosition()+hardware.rightWheel.getCurrentPosition()/2;

        while(wheelPosition > -2000){
            wheelPosition =hardware.leftWheel.getCurrentPosition()+hardware.rightWheel.getCurrentPosition()/2;
            say(""+wheelPosition);
        }
        hardware.rightGrabber.setPosition(hardware.rightGDown);
        hardware.leftGrabber.setPosition(hardware.leftGDown);
        waitOneFullHardwareCycle();
        say("GRABBED");

        sleep(500);
        stopDrive();
        say("DONE");

    }
}
