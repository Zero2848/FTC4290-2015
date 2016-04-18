package com.qualcomm.ftcrobotcontroller.opmodes.phresh;

import com.lasarobotics.library.controller.ButtonState;
import com.lasarobotics.library.controller.Controller;
import com.lasarobotics.library.drive.Tank;
import com.lasarobotics.library.util.MathUtil;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Skunk (TM) Teleop
 */
public class TeleopSkunk extends OpMode {

    DcMotor leftFront, rightFront, leftBack, rightBack, lift, intake, raiser;
    Servo leftHook,rightHook, climberDumper, climberReleaser, blockPusher;
    Controller one,two;
    boolean hooked;
    boolean intakeBool;
    boolean isClimberDumperOut;
    boolean isClimberReleaserOut;

    double DEADBAND = 0.05;

    @Override
    public void init() {
        leftFront = hardwareMap.dcMotor.get("lf");
        rightFront = hardwareMap.dcMotor.get("rf");
        leftBack = hardwareMap.dcMotor.get("lb");
        rightBack = hardwareMap.dcMotor.get("rb");
        lift = hardwareMap.dcMotor.get("lift");
        intake = hardwareMap.dcMotor.get("intake");
        raiser = hardwareMap.dcMotor.get("raiser");

        leftHook = hardwareMap.servo.get("leftHook");
        rightHook = hardwareMap.servo.get("rightHook");
        climberDumper = hardwareMap.servo.get("climberDumper");
        climberReleaser = hardwareMap.servo.get("climberReleaser");
        blockPusher = hardwareMap.servo.get("blockPusher");

        rightFront.setDirection(DcMotor.Direction.REVERSE);
        rightBack.setDirection(DcMotor.Direction.REVERSE);

        one = new Controller(gamepad1);
        two = new Controller(gamepad2);

        leftHook.setPosition(0);
        rightHook.setPosition(1);

        climberDumper.setPosition(1);
        climberReleaser.setPosition(1);
        blockPusher.setPosition(0.5);

        isClimberDumperOut = false;
        isClimberReleaserOut = false;
    }

    @Override
    public void loop() {
        one.update(gamepad1);
        two.update(gamepad2);

        telemetry.addData("Controller One Connected", one.isConnected());
        telemetry.addData("Controller Two Connected", two.isConnected());

        //Hooks
        if (one.x == ButtonState.PRESSED){
            hooked = !hooked;
        }
        if (hooked){
            leftHook.setPosition(1);
            rightHook.setPosition(0);
        }
        else {
            leftHook.setPosition(0);
            rightHook.setPosition(1);
        }

        //Raiser
        if (one.a == ButtonState.HELD){
            raiser.setPower(1);
        }
        else if (one.y == ButtonState.HELD) {
            raiser.setPower(-1);
        }
        else {
            raiser.setPower(0);
        }

        //Lift
        lift.setPower(MathUtil.deadband(DEADBAND, two.right_stick_y));

        //Intake
        if (two.a == ButtonState.PRESSED){
            intakeBool = !intakeBool;
        }
        if (two.left_bumper == ButtonState.HELD || two.left_bumper == ButtonState.PRESSED){
            intake.setPower(-1);
        }
        else if(intakeBool) {
            intake.setPower(1);
        }
        else {
            intake.setPower(0);
        }

        if (one.b == ButtonState.PRESSED){
            if (!isClimberDumperOut){
                climberDumper.setPosition(0.5);
                isClimberDumperOut = true;
            }
            else{
                climberDumper.setPosition(0.75);
                isClimberDumperOut = false;
            }
        }

        if (two.b == ButtonState.PRESSED){
            if(!isClimberReleaserOut){
                climberReleaser.setPosition(0.25);
                isClimberReleaserOut = true;
            }
            else{
                climberReleaser.setPosition(0.75);
                isClimberReleaserOut = false;
            }
        }

        if(two.left_stick_x > DEADBAND){
            blockPusher.setPosition(0);
        }
        else if(two.left_stick_x < -DEADBAND){
            blockPusher.setPosition(1);
        }
        else{
            blockPusher.setPosition(0.5);
        }

        Tank.motor4(leftFront, rightFront, leftBack, rightBack,
                MathUtil.deadband(DEADBAND, -one.left_stick_y), MathUtil.deadband(DEADBAND, -one.right_stick_y));
    }
}
