package com.qualcomm.ftcrobotcontroller.opmodes;

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

    DcMotor leftFront, rightFront, leftBack, rightBack,liftA,liftB,winch,intake;
    Servo leftHook,rightHook;
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
        winch = hardwareMap.dcMotor.get("winch");
        liftA = hardwareMap.dcMotor.get("liftA");
        liftB = hardwareMap.dcMotor.get("liftB");
        intake = hardwareMap.dcMotor.get("intake");

        leftHook = hardwareMap.servo.get("leftHook");
        rightHook = hardwareMap.servo.get("rightHook");

        rightFront.setDirection(DcMotor.Direction.REVERSE);
        rightBack.setDirection(DcMotor.Direction.REVERSE);

        one = new Controller(gamepad1);
        two = new Controller(gamepad2);

        leftHook.setPosition(0);
        rightHook.setPosition(1);
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

        //Winch
        winch.setPower(MathUtil.deadband(DEADBAND, two.right_stick_y));

        //Lift
        liftA.setPower(MathUtil.deadband(DEADBAND, two.left_stick_y));
        liftB.setPower(MathUtil.deadband(DEADBAND, -two.left_stick_y));

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
