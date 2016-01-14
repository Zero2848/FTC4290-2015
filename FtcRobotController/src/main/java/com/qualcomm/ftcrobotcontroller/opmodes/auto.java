package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by Ethan on 1/12/16.
 */
public class auto extends LinearOpMode {
    DcMotor leftWheel, rightWheel, winch1, winch2, angler;
    Servo climberLeft, climberRight;

    public void declare(){
        //make global
        leftWheel = hardwareMap.dcMotor.get("l");
        leftWheel.setDirection(DcMotor.Direction.REVERSE);
        rightWheel = hardwareMap.dcMotor.get("r");
        winch1 = hardwareMap.dcMotor.get("w1");
        winch1.setDirection(DcMotor.Direction.REVERSE);
        winch2 = hardwareMap.dcMotor.get("w2");
        angler = hardwareMap.dcMotor.get("a");
        climberLeft = hardwareMap.servo.get("cl");
        climberRight = hardwareMap.servo.get("cr");
    }

    int wheelPosition;
    final int WHEEL_DIAMETER = 4;
    final double WHEEL_CIRCUMFERENCE = WHEEL_DIAMETER * Math.PI;
    double distance;

    public void driveTo(double ticks, double powerLeft, double powerRight, int accuracy){
        leftWheel.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);
        leftWheel.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        wheelPosition = leftWheel.getCurrentPosition();
        while(wheelPosition < ticks + accuracy || wheelPosition > ticks - accuracy) {
            wheelPosition = leftWheel.getCurrentPosition();
            if(wheelPosition < ticks + accuracy) {
                leftWheel.setPower(powerLeft);
                rightWheel.setPower(powerRight);
            }
            if (leftWheel.getCurrentPosition() > ticks - accuracy) {
                leftWheel.setPower(-powerLeft);
                rightWheel.setPower(-powerRight);

            }
        }
    }
    public void driveDistance(double dist, double powerL, double powerR, int accuracy){
        distance = dist * WHEEL_CIRCUMFERENCE * 360;
        if(powerL > 1){
            powerL/=100;
        }
        if(powerR > 1){
            powerR/=100;
        }
        driveTo(distance, powerL, powerR, accuracy);

    }
    public void driveDistance(double dist, double powerL, double powerR){
        distance = dist * WHEEL_CIRCUMFERENCE * 360;
        if(powerL > 1){
            powerL/=100;
        }
        if(powerL > 1){
            powerL/=100;
        }
        driveTo(distance, powerL, powerR, 10);

    }
    public void driveDistance(double dist, double power){
        distance = dist * WHEEL_CIRCUMFERENCE * 360;
        if(power > 1){
            power/=100;
        }
        driveTo(distance, power, power, 10);

    }
    public void driveDistance(double dist){
        distance = dist * WHEEL_CIRCUMFERENCE * 360;
        driveTo(distance, .75, .75, 10);

    }


    @Override
    public void runOpMode() throws InterruptedException {
        driveDistance(12 * 5, -.75,- .75, 10); //backwards towards the mountain
//        driveDistance(1, -.75, 75, 10); //turn left
    }
}
