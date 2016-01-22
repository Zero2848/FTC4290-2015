package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.AnalogInput;

/**
 * Created by Ethan on 1/20/16.
 */
public class sensor extends OpMode {
    double leftP = .5, rightP = .5;

    @Override
    public void init() {
        hardware.touchSensor = hardwareMap.analogInput.get("touch");
        hardware.rightGrabber = hardwareMap.servo.get("rg");
        hardware.leftGrabber = hardwareMap.servo.get("lg");
    }

    @Override
    public void loop() {
        telemetry.addData("Touch", hardware.touchSensor.getValue());
        if(gamepad1.a){
            leftP += .01;
            rightP -= .01;
        }
        if(gamepad1.b){
            leftP -= .01;
            rightP += .01;
        }
        if(leftP > 1){
            leftP = 1;
        } else if(leftP < 0){
            leftP = 0;
        }
        if(rightP > 1){
            rightP = 1;
        } else if(rightP < 0){
            rightP = 0;
        }

        hardware.leftGrabber.setPosition(leftP);
        hardware.rightGrabber.setPosition(rightP);
        telemetry.addData("Left", leftP);
        telemetry.addData("Right", rightP);


    }
}
