package com.qualcomm.ftcrobotcontroller;

import android.util.Log;

import com.kauailabs.navx.ftc.AHRS;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
/*
*
* LABEL SERVO WIRES IN A COLOR-CODED WAY
*
* */

public class Hardware {
    public DcMotor leftWheel, rightWheel, winch1, winch2, angler;
    public Servo climberLeft, climberRight, stopper, climber, leftGrabber, rightGrabber;
    public AHRS navx;
    public static boolean navxenabled = true;
    public static final double
            LEFT_SERVO_TOP = .95, LEFT_SERVO_BOTTOM = .25, LEFT_SERVO_LOWEST = 0.0,
            RIGHT_SERVO_TOP = .05, RIGHT_SERVO_BOTTOM = .75, RIGHT_SERVO_LOWEST = 1.0,
            CLIMBER_TOP = .01, CLIMBER_BOTTOM = .99,
            STOPPER_ON = .4, STOPPER_OFF = .1,
            LEFT_GRABBER_UP = .08, LEFT_GRABBER_DOWN = .9,
            RIGHT_GRABBER_UP = 1, RIGHT_GRABBER_DOWN = .2;
    private final int NAVX_DIM_I2C_PORT = 2;
    private final byte NAVX_DEVICE_UPDATE_RATE_HZ = 50;

    public Hardware(HardwareMap hardwareMap) {
        this.leftWheel = hardwareMap.dcMotor.get("l");
        this.leftWheel.setDirection(DcMotor.Direction.REVERSE);
        this.rightWheel = hardwareMap.dcMotor.get("r");
        this.winch1 = hardwareMap.dcMotor.get("w1");
        this.winch1.setDirection(DcMotor.Direction.REVERSE);
        this.winch2 = hardwareMap.dcMotor.get("w2");
        this.angler = hardwareMap.dcMotor.get("a");
        this.angler.setDirection(DcMotor.Direction.REVERSE);
        this.climberLeft = hardwareMap.servo.get("6");
        this.climberRight = hardwareMap.servo.get("2");
        this.stopper = hardwareMap.servo.get("1");
        this.climber = hardwareMap.servo.get("3");
        this.leftGrabber = hardwareMap.servo.get("5");
        this.rightGrabber = hardwareMap.servo.get("4");
        this.winch2.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        this.angler.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        //THIS CODE CAUSED AN INFINITE LOOP IN TELEOP
        //while (this.rightWheel.getCurrentPosition() != 0) {
        //    this.rightWheel.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        //}//gives Hardware the time to reset
        this.rightWheel.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        this.climber.setPosition(CLIMBER_BOTTOM);
        this.climberRight.setPosition(RIGHT_SERVO_TOP);
        this.climberLeft.setPosition(LEFT_SERVO_TOP);
        this.leftGrabber.setPosition(LEFT_GRABBER_UP);
        this.rightGrabber.setPosition(RIGHT_GRABBER_UP);
        this.stopper.setPosition(STOPPER_OFF);
        if(navxenabled){
            navx = AHRS.getInstance(hardwareMap.deviceInterfaceModule.get("dim"),
                    NAVX_DIM_I2C_PORT,
                    AHRS.DeviceDataType.kProcessedData,
                    NAVX_DEVICE_UPDATE_RATE_HZ);
        }
    }

}
