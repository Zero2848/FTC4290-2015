package com.qualcomm.ftcrobotcontroller;

import android.util.Log;

import com.lasarobotics.library.util.MathUtil;
import com.lasarobotics.library.util.Timers;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.robocol.Telemetry;


public class Auto {
    private static final int TOLERANCE_DEGREES = 5;

    public static void driveTo(Hardware config, Telemetry telemetry,int ticks, double powerLeft, double powerRight) throws InterruptedException {
        resetEncoder(config.leftWheel);
        resetEncoder(config.rightWheel);
        double wheelPosition = 0;
        while (Math.abs(wheelPosition) < ticks) {
            wheelPosition = config.rightWheel.getCurrentPosition();
            telemetry.addData("Wheel at", wheelPosition + " ticks.");
            config.leftWheel.setPower(powerLeft);
            config.rightWheel.setPower(powerRight);
        }
        config.rightWheel.setPower(0);
        config.leftWheel.setPower(0);
        resetEncoder(config.leftWheel);
        resetEncoder(config.rightWheel);
    }
    public static void resetEncoder(DcMotor m) {
        while (m.getCurrentPosition() != 0) {
            m.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        }
        m.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
    }

    public static float convertDegNavX(float deg) {
        if (deg < 0)
            deg = 360 - Math.abs(deg);
        return deg;
    }

    public static void block(int ms) throws InterruptedException {
        Timers mTimers = new Timers();
        mTimers.startClock("delay");
        while (mTimers.getClockValue("delay") < ms) {
        }
    }

    public static void turnToDegNavX(Hardware config, Telemetry telemetry, int deg, double power) throws InterruptedException {
        config.navx.reset();
        block(500);
        config.leftWheel.setPower(-power);
        config.rightWheel.setPower(power);

        float yaw;

        boolean arrived = false;

        while(!arrived) {
            yaw = config.navx.getRotation().x;
            Log.d("Yaw", yaw + "");
            telemetry.addData("Yaw", yaw + "");
            telemetry.addData("Target Yaw", deg);
            if (MathUtil.inBounds(convertDegNavX(deg) - TOLERANCE_DEGREES, convertDegNavX(deg) + TOLERANCE_DEGREES, convertDegNavX(yaw)))
                arrived = true;
            if (power > 0 && convertDegNavX(yaw) > deg - TOLERANCE_DEGREES) //clockwise
                arrived = true;
            if (power < 0 && convertDegNavX(yaw) < deg + TOLERANCE_DEGREES) //counterclockwise
                arrived = true;
        }

        config.leftWheel.setPower(0);
        config.rightWheel.setPower(0);
    }

}
