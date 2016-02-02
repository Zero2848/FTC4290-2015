package com.qualcomm.ftcrobotcontroller;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;


public class Hardware {
    //final static int winchCap = 5000, winchLow = 80;
    public static final int winchCap = 100000000, winchLow = -100000000;
    public static final int anglerCap = -8400, anglerLow = 450;
    public DcMotor leftWheel, rightWheel, winch1, winch2, angler;
    public Servo climberLeft, climberRight, stopper, climber, leftGrabber, rightGrabber;
    public static final double leftServoTop = .97, leftServoBottom = .25,
            rightServoTop = .05, rightServoBottom = .75,
            climberTop = .01, climberBottom = .99,
            stopperOn = .4, stopperOff = .9,
            leftGUp = 1.0, leftGDown = 0.0,
            rightGUp = 0.0, rightGDown = 1.0;

    public Hardware(HardwareMap hardwareMap) {
        this.leftGrabber = hardwareMap.servo.get("lg");
        this.rightGrabber = hardwareMap.servo.get("rg");
        this.leftWheel = hardwareMap.dcMotor.get("l");
        this.rightWheel = hardwareMap.dcMotor.get("r");
        this.rightWheel.setDirection(DcMotor.Direction.REVERSE);
        this.winch1 = hardwareMap.dcMotor.get("w1");
        this.winch1.setDirection(DcMotor.Direction.REVERSE);
        this.winch2 = hardwareMap.dcMotor.get("w2");
        this.angler = hardwareMap.dcMotor.get("a");
        this.angler.setDirection(DcMotor.Direction.REVERSE);
        this.climberLeft = hardwareMap.servo.get("cl");
        this.climberRight = hardwareMap.servo.get("cr");
        this.stopper = hardwareMap.servo.get("s");
        this.climber = hardwareMap.servo.get("c");
        this.winch2.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        this.angler.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        while (this.rightWheel.getCurrentPosition() != 0) {
            this.rightWheel.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        }//gives Hardware the time to reset
        this.rightWheel.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        this.climber.setPosition(climberBottom);
        this.climberRight.setPosition(rightServoTop);
        this.climberLeft.setPosition(leftServoTop);
        this.stopper.setPosition(stopperOff);
    }

}
