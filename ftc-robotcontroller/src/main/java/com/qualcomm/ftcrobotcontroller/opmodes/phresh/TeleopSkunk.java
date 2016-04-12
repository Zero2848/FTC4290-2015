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

    DcMotor leftFront, rightFront, leftBack, rightBack, lift, winchA, winchB, intake;
    Servo leftHook,rightHook, leftRaise, rightRaise;
    Controller one,two;
    boolean hooked;
    boolean intakeBool;

    double DEADBAND = 0.05;

    @Override
    public void init() {
        leftFront = hardwareMap.dcMotor.get("rf");
        rightFront = hardwareMap.dcMotor.get("lf");
        leftBack = hardwareMap.dcMotor.get("rb");
        rightBack = hardwareMap.dcMotor.get("lb");
        winchA = hardwareMap.dcMotor.get("winchA");
        winchB = hardwareMap.dcMotor.get("winchB");
        lift = hardwareMap.dcMotor.get("lift");
        intake = hardwareMap.dcMotor.get("intake");

        leftHook = hardwareMap.servo.get("leftHook");
        rightHook = hardwareMap.servo.get("rightHook");
        leftRaise = hardwareMap.servo.get("leftRaise");
        rightRaise = hardwareMap.servo.get("rightRaise");

        rightFront.setDirection(DcMotor.Direction.REVERSE);
        rightBack.setDirection(DcMotor.Direction.REVERSE);

        one = new Controller(gamepad1);
        two = new Controller(gamepad2);

        leftHook.setPosition(0);
        rightHook.setPosition(1);

        leftRaise.setPosition(0.6);
        rightRaise.setPosition(0.4);
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

        //Angler
        if (one.a == ButtonState.PRESSED){
            leftRaise.setPosition(0.6);
            rightRaise.setPosition(0.4);
        }
        else if (one.y == ButtonState.PRESSED) {
            leftRaise.setPosition(0);
            rightRaise.setPosition(1);
        }

        //Winch
        winchA.setPower(MathUtil.deadband(DEADBAND, two.right_stick_y));
        winchB.setPower(MathUtil.deadband(DEADBAND, -two.right_stick_y));

        //Lift
        lift.setPower(MathUtil.deadband(DEADBAND, two.left_stick_y));

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


        Tank.motor4(leftFront, rightFront, leftBack, rightBack,
                MathUtil.deadband(DEADBAND, one.left_stick_y), MathUtil.deadband(DEADBAND, one.right_stick_y));
    }
}
