package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.AnalogInput;

/**
 * Created by Ethan on 1/20/16.
 */
public class sensor extends OpMode {

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
            hardware.leftGrabber.setPosition(hardware.leftGDown);
            hardware.rightGrabber.setPosition(hardware.rightGDown);
        }
        if(gamepad1.b){
            hardware.leftGrabber.setPosition(hardware.leftGUp);
            hardware.rightGrabber.setPosition(hardware.rightGUp);
        }

    }
}
