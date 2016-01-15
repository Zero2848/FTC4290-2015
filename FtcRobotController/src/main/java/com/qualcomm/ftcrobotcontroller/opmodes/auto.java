package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;

/**
 * Created by Ethan on 1/12/16.
 */
public class auto extends LinearOpMode {
    int metric, wheelPosition;

    public void declare(){
        hardware.leftWheel = hardwareMap.dcMotor.get("l");
        hardware.rightWheel = hardwareMap.dcMotor.get("r");
        hardware.rightWheel.setDirection(DcMotor.Direction.REVERSE);
        hardware.winch1 = hardwareMap.dcMotor.get("w1");
        hardware.winch1.setDirection(DcMotor.Direction.REVERSE);
        hardware.winch2 = hardwareMap.dcMotor.get("w2");
        hardware.angler = hardwareMap.dcMotor.get("a");
        hardware.climberLeft = hardwareMap.servo.get("cl");
        hardware.climberRight = hardwareMap.servo.get("cr");
    }


    public void driveTo(double inches, double powerLeft, double powerRight){
        int speedMod = 1;
        String lastIn = "<";
        ticks*=1000;
        hardware.rightWheel.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);
        metric = hardware.rightWheel.getCurrentPosition();
        hardware.rightWheel.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        wheelPosition = hardware.rightWheel.getCurrentPosition() + metric;
        telemetry.addData("2", "wheel: " + wheelPosition);
        telemetry.addData("3", "feet goal: " + ticks);
        telemetry.addData("4", "metric:"+ metric);

        while(wheelPosition!=ticks) {
            wheelPosition = hardware.rightWheel.getCurrentPosition() + metric;
            telemetry.addData("1", "wheel: " + wheelPosition);
            telemetry.addData("3", "metric:" + metric);

            if(wheelPosition < ticks) {
                if(lastIn == ">"){
                    speedMod += speedModAmount;
                    lastIn = "<";
                }
                telemetry.addData("2", "inch goal: " + (inches));
                hardware.leftWheel.setPower(powerLeft/speedMod);
                hardware.rightWheel.setPower(powerRight/speedMod);
            }
            else if (wheelPosition > ticks) {
                if(lastIn == "<") {
                    speedMod += speedModAmount;
                    lastIn = ">";
                }
                telemetry.addData("2", "inch goal: " + (inches));
                hardware.leftWheel.setPower(-powerLeft/speedMod);
                hardware.rightWheel.setPower(-powerRight/speedMod);
            }
        }
        hardware.leftWheel.setPower(0);
        hardware.rightWheel.setPower(0);
    }

    @Override
    public void runOpMode() throws InterruptedException {
        declare();
        waitForStart();
        telemetry.addData("1", "start");
        driveTo(-75, .25, .25); //backwards towards the mountain
        telemetry.addData("5", "distance done");
    }
}
