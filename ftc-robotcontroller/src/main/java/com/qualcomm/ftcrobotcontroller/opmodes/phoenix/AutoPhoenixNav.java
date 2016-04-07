package com.qualcomm.ftcrobotcontroller.opmodes.phoenix;

import com.lasarobotics.library.nav.EncodedMotor;
import com.lasarobotics.library.nav.MotorInfo;
import com.lasarobotics.library.nav.navigator.TankNav;
import com.lasarobotics.library.sensor.kauailabs.navx.NavXDevice;
import com.lasarobotics.library.util.Units;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Phoenix (TM) Autonomous with Nav lib
 */
public class AutoPhoenixNav extends LinearOpMode {
    TankNav nav;
    NavXDevice navx;
    EncodedMotor left;
    EncodedMotor right;

    @Override
    public void runOpMode() throws InterruptedException {

        MotorInfo info = new MotorInfo(1000, Units.Distance.FEET);
        left = new EncodedMotor(hardwareMap.dcMotor.get("l"), info);
        right = new EncodedMotor(hardwareMap.dcMotor.get("r"), info);
        left.setDirection(DcMotor.Direction.REVERSE);
        navx = new NavXDevice(hardwareMap, "dim", 2);
        nav = new TankNav(navx, new EncodedMotor[]{left}, new EncodedMotor[]{right}, telemetry);

        navx.reset();
//        navx.waitForReset();
        waitForStart();

        nav.rotateInPlace(90.0, 1);
        nav.stop();
        while (this.opModeIsActive()) {
            telemetry.addData("Done", "Complete!");
            navx.displayTelemetry(telemetry);
            waitOneFullHardwareCycle();
        }
    }
}
