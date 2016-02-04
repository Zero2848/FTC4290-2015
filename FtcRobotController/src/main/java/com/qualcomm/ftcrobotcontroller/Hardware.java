package com.qualcomm.ftcrobotcontroller;

import com.kauailabs.navx.ftc.AHRS;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;


public class Hardware {
    public DcMotor leftWheel, rightWheel, winch1, winch2, angler;
    public Servo climberLeft, climberRight, stopper, climber, leftGrabber, rightGrabber;
    public AHRS navx;
    public static final double LEFT_SERVO_TOP = .97, LEFT_SERVO_BOTTOM = .25,
            RIGHT_SERVO_TOP = .05, RIGHT_SERVO_BOTTOM = .75,
            CLIMBER_TOP = .01, CLIMBER_BOTTOM = .99,
            STOPPER_ON = .4, STOPPER_OFF = .9,
            LEFT_GRABBER_UP = 1.0, LEFT_GRABBER_DOWN = 0.0,
            RIGHT_GRABBER_UP = 0.0, RIGHT_GRABBER_DOWN = 1.0;
    private final int NAVX_DIM_I2C_PORT = 1;
    private final byte NAVX_DEVICE_UPDATE_RATE_HZ = 50;

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
        this.climber.setPosition(CLIMBER_BOTTOM);
        this.climberRight.setPosition(RIGHT_SERVO_TOP);
        this.climberLeft.setPosition(LEFT_SERVO_TOP);
        this.stopper.setPosition(STOPPER_OFF);
        AHRS.getInstance(hardwareMap.deviceInterfaceModule.get("dim"),
                NAVX_DIM_I2C_PORT,
                AHRS.DeviceDataType.kProcessedData,
                NAVX_DEVICE_UPDATE_RATE_HZ);
    }

}
