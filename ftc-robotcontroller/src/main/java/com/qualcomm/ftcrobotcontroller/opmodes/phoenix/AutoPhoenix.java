package com.qualcomm.ftcrobotcontroller.opmodes.phoenix;

import com.lasarobotics.library.sensor.kauailabs.navx.NavXDevice;
import com.lasarobotics.library.util.MathUtil;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;


import android.util.Log;

/**
 * Pheonix (TM) Autonomous
 */
public class AutoPhoenix extends LinearOpMode {
    //Frame counter
    int frameCount = 0;
    DcMotor left,right;
    NavXDevice navx;
    private static final int TOLERANCE_DEGREES = 1;
    private final int NAVX_DIM_I2C_PORT = 2;


    public void resetEncoder(DcMotor m) {
        while (m.getCurrentPosition() != 0) {
            m.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        }
        m.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
    }
    public float convertDegNavX(float deg) {
        if (deg < 0)
            deg = 360 - Math.abs(deg);
        return deg;
    }
    public void turnToDegNavX(int deg, double power) throws InterruptedException {
        navx.reset();
        while (!MathUtil.inBounds(-1,1,navx.getRotation().x)){}
        left.setPower(-power);
        right.setPower(power);

        float yaw;

        boolean arrived = false;

        while(!arrived) {
            yaw = navx.getRotation().x;
            navx.displayTelemetry(telemetry);
            if (MathUtil.inBounds(convertDegNavX(deg) - TOLERANCE_DEGREES, convertDegNavX(deg) + TOLERANCE_DEGREES, convertDegNavX(yaw)))
                arrived = true;
        }

        left.setPower(0);
        right.setPower(0);
    }
    public void driveTo(int ticks, double powerl, double powerr) throws InterruptedException {
        double wheelPosition = 0;
        resetEncoder(right);
        while (Math.abs(wheelPosition) < ticks) {
            wheelPosition = right.getCurrentPosition();
            telemetry.addData("Wheel at", wheelPosition + " ticks.");
            left.setPower(powerl);
            right.setPower(powerr);
            waitOneFullHardwareCycle();
        }
        left.setPower(0);
        right.setPower(0);
        waitOneFullHardwareCycle();
        resetEncoder(right);
        waitOneFullHardwareCycle();
    }

    @Override
    public void runOpMode() throws InterruptedException {
        left = hardwareMap.dcMotor.get("l");
        right = hardwareMap.dcMotor.get("r");
        left.setDirection(DcMotor.Direction.REVERSE);
        navx = new NavXDevice(hardwareMap, "dim",NAVX_DIM_I2C_PORT);
        waitForStart();
        driveTo(500, 1, 1);
        turnToDegNavX(320, -1);
        driveTo(8000, 1, 1);
        turnToDegNavX(315, -1);
        sleep(1000);
        driveTo(1500, 1, 1);
        sleep(1000);
    }
}
