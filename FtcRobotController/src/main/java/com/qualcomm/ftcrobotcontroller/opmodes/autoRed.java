package com.qualcomm.ftcrobotcontroller.opmodes;

import com.kauailabs.navx.ftc.navXPIDController;
import com.lasarobotics.library.util.Log;
import com.lasarobotics.library.util.MathUtil;
import com.lasarobotics.library.util.Timers;
import com.qualcomm.ftcrobotcontroller.Auto;
import com.qualcomm.ftcrobotcontroller.Hardware;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;


public class AutoRed extends LinearOpMode {
    private static final int TOLERANCE_DEGREES = 2;

    Hardware config;
    final static double YAW_PID_P = 1;
    final static double YAW_PID_I = 1;
    final static double YAW_PID_D = 1;


    @Override
    public void runOpMode() throws InterruptedException {
        config = new Hardware(hardwareMap);

        waitForStart();
        Auto.driveTo(config, telemetry, 8000, .5, .5);
        turnToDegNavX(45, .5);
        Auto.driveTo(config, telemetry, 1000, .5, .5);
        waitOneFullHardwareCycle();
        waitForNextHardwareCycle();
        config.climber.setPosition(Hardware.CLIMBER_TOP);
        sleep(3000);
        config.climber.setPosition(Hardware.CLIMBER_BOTTOM);

        config.navx.close();
    }

    public  float convertDegNavX(float deg) {
        if (deg < 0)
            deg = 360 - Math.abs(deg);
        return deg;
    }

    public void turnToDegNavX(int deg, double power) throws InterruptedException {
        config.navx.zeroYaw();
        waitOneFullHardwareCycle();
        config.leftWheel.setPower(-power);
        config.rightWheel.setPower(power);

        float yaw;

        boolean arrived = false;

        while(!arrived) {
            yaw = convertDegNavX(config.navx.getYaw());
            telemetry.addData("Yaw", yaw);
            telemetry.addData("Target Yaw", deg);
            if (MathUtil.inBounds(deg - TOLERANCE_DEGREES, deg + TOLERANCE_DEGREES, yaw))
                arrived = true;
            waitOneFullHardwareCycle();
        }

        config.leftWheel.setPower(0);
        config.rightWheel.setPower(0);
        Auto.resetEncoder(config.leftWheel);
        Auto.resetEncoder(config.rightWheel);
    }
//    public void PIDWontWork(int time, double speed){
//        Auto.resetEncoder(config.leftWheel);
//        Auto.resetEncoder(config.rightWheel);
//        navXPIDController yawPIDController = new navXPIDController(config.navx, navXPIDController.navXTimestampedDataSource.YAW);
//        yawPIDController.setSetpoint(0);
//        yawPIDController.setContinuous(true);
//        yawPIDController.setOutputRange(-.5, .5);
//        yawPIDController.setTolerance(navXPIDController.ToleranceType.ABSOLUTE, TOLERANCE_DEGREES);
//        yawPIDController.setPID(YAW_PID_P, YAW_PID_I, YAW_PID_D);
//        yawPIDController.enable(true);
//        navXPIDController.PIDResult yawPIDResult = new navXPIDController.PIDResult();
//        Timers t = new Timers();
//        t.startClock("PID");
//        Log logger = new Log("test", "PID");
//        try{
//            while(t.getClockValue("PID") < time){
//                if(yawPIDController.waitForNewUpdate(yawPIDResult, 500)){
//                    if(yawPIDResult.isOnTarget()){
//                        config.leftWheel.setPower(speed);
//                        config.rightWheel.setPower(speed);
//                        telemetry.addData("output", "none");
//                    } else {
//                        double output = yawPIDResult.getOutput();
//                        config.leftWheel.setPower(MathUtil.coerce(-1, 1, speed-output));
//                        config.rightWheel.setPower(MathUtil.coerce(-1, 1, speed + output));
//                        telemetry.addData("output", output);
//                    }
//                }
//                telemetry.addData("yaw", config.navx.getYaw());
//                logger.add("yaw", ""+config.navx.getYaw());
//            }
//        } catch (InterruptedException e){
//            Thread.currentThread().interrupt();
//        }
//        logger.saveAs(Log.FileType.CSV);
//    }

}
