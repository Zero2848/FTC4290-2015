package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.ftcrobotcontroller.Hardware;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by Ethan on 2/5/16.
 */
public class servo extends OpMode {
    Servo s;
    @Override
    public void init() {
        s = hardwareMap.servo.get("5");
    }

    @Override
    public void loop() {
        if(gamepad1.a){
            s.setPosition(Hardware.LEFT_GRABBER_UP);
        } else {
            s.setPosition(Hardware.LEFT_GRABBER_DOWN);
        }

    }
}
