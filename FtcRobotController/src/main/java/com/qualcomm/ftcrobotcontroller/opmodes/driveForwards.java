package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;

/**
 * Created by 4290 on 1/21/16.
 */
public class driveForwards extends LinearOpMode {
    DcMotor left, right;

    @Override
    public void runOpMode() throws InterruptedException {
        left = hardwareMap.dcMotor.get("leftMotor");
        right = hardwareMap.dcMotor.get("rightMotor");
        waitForStart(); //this makes sure that the robot
                        // does not move until the play
                        // button is pressed
        telemetry.addData("Status:", "Robot Stopped");
        while(left.getCurrentPosition() != 0) {
            left.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);
        }
        /*
        this being in a while loop makes sure that if
        the motor has moved to some position already,
        you don't base your movement on that.
        We had some issues with this,
        before implementing the while loop here.
        */
        double targetDistance = 5000; //target distance in encoder ticks. 1440 ticks = 1 rotation of a motor.

        while(left.getCurrentPosition() < targetDistance) {
            left.setPower(.50);
            right.setPower(.50);
            telemetry.addData("Left Motor is at", left.getCurrentPosition());
        }
        telemetry.addData("Status:", "Robot Stopped");
        left.setPower(0);
        right.setPower(0);
    }
}
