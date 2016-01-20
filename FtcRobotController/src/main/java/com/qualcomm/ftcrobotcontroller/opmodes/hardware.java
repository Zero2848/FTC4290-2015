package com.qualcomm.ftcrobotcontroller.opmodes;

import android.os.Environment;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by Ethan on 1/12/16.
 */

public class hardware extends OpMode{
    public final static String FILE_DIR = Environment.getExternalStorageDirectory() + "/Encoder Settings/";
    public static DcMotor leftWheel, rightWheel, winch1, winch2, angler;
    public static Servo climberLeft, climberRight, stopper, climber;
    public static double leftServoTop = .97, leftServoBottom = .25, rightServoTop = .05, rightServoBottom = .75, climberTop = .35, climberBottom = .95, stopperOn = .4, stopperOff = .9;

    //final static int winchCap = 5000, winchLow = 80;
    final static int winchCap = 100000000, winchLow = -100000000;

    final static int anglerCap = 450, anglerLow = -8400;



    public void declare() throws InterruptedException {
        hardware.leftWheel = hardwareMap.dcMotor.get("l");
        hardware.rightWheel = hardwareMap.dcMotor.get("r");
        hardware.rightWheel.setDirection(DcMotor.Direction.REVERSE);
        hardware.winch1 = hardwareMap.dcMotor.get("w1");
        hardware.winch1.setDirection(DcMotor.Direction.REVERSE);
        hardware.winch2 = hardwareMap.dcMotor.get("w2");
        hardware.angler = hardwareMap.dcMotor.get("a");
        hardware.angler.setDirection(DcMotor.Direction.REVERSE);
        hardware.climberLeft = hardwareMap.servo.get("cl");
        hardware.climberRight = hardwareMap.servo.get("cr");
        hardware.stopper = hardwareMap.servo.get("s");
        hardware.climber = hardwareMap.servo.get("c");
        hardware.winch2.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        hardware.angler.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        while (hardware.rightWheel.getCurrentPosition() != 0){
            hardware.rightWheel.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);
        }//gives hardware the time to reset
        hardware.rightWheel.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        hardware.climber.setPosition(hardware.climberBottom);
        hardware.climberRight.setPosition(hardware.rightServoTop);
        hardware.climberLeft.setPosition(hardware.leftServoTop);
        hardware.stopper.setPosition(hardware.stopperOff);
    }

    @Override
    public void init() {
        try {
            declare();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void loop() {

    }

}
