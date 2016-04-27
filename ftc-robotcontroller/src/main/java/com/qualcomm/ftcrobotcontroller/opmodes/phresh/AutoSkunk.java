package com.qualcomm.ftcrobotcontroller.opmodes.phresh;

import com.lasarobotics.library.sensor.kauailabs.navx.NavXDevice;
import com.lasarobotics.library.util.MathUtil;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;

import org.lasarobotics.vision.android.Cameras;
import org.lasarobotics.vision.detection.objects.Rectangle;
import org.lasarobotics.vision.ftc.resq.Beacon;
import org.lasarobotics.vision.opmode.LinearVisionOpMode;
import org.lasarobotics.vision.opmode.extensions.CameraControlExtension;
import org.lasarobotics.vision.util.ScreenOrientation;
import org.opencv.core.Point;
import org.opencv.core.Size;

public class AutoSkunk extends LinearVisionOpMode {
    private static final float TOLERANCE_DEGREES = 1;
    private static final float OVERSHOOT_DEGREES = 5;
    private final int NAVX_DIM_I2C_PORT = 0;
    int frameCount = 0;
    DcMotor leftFront, rightFront, leftBack, rightBack, lift, intake, raiser;
    Servo leftHook,rightHook, climberDumper, climberReleaser, blockPusher;
    ColorSensor colorSensor;
    NavXDevice navx;

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
        while (!MathUtil.inBounds(-1, 1, navx.getRotation().x)){}
        leftFront.setPower(-power);
        leftBack.setPower(-power);
        rightFront.setPower(power);
        rightBack.setPower(power);

        float yaw;

        boolean arrived = false;

        while(!arrived) {
            yaw = navx.getRotation().x;
            navx.displayTelemetry(telemetry);
            if (MathUtil.inBounds(convertDegNavX(deg - TOLERANCE_DEGREES), convertDegNavX(deg + TOLERANCE_DEGREES), convertDegNavX(yaw)))
                arrived = true;
            /*else if (Math.signum(power) == 1)
            {
                //Rotating clockwise - degrees increasing
                if (convertDegNavX(deg + OVERSHOOT_DEGREES) < convertDegNavX(yaw))
                    power = -power;
            }
            else if (Math.signum(power) == -1)
            {
                //Rotating counterclockwise - degrees decreasing
                if (convertDegNavX(deg - OVERSHOOT_DEGREES) > convertDegNavX(yaw))
                    power = -power;
            }*/
        }

        leftFront.setPower(0);
        leftBack.setPower(0);
        rightFront.setPower(0);
        rightBack.setPower(0);
    }
    public void driveTo(int ticks, double powerl, double powerr) throws InterruptedException {
        double wheelPosition = 0;
        resetEncoder(rightBack);
        while (Math.abs(wheelPosition) < ticks) {
            wheelPosition = rightBack.getCurrentPosition();
            telemetry.addData("Wheel at", wheelPosition + " ticks.");
            leftFront.setPower(powerl);
            leftBack.setPower(powerl);
            rightFront.setPower(powerr);
            rightBack.setPower(powerr);
            waitOneFullHardwareCycle();
        }
        leftFront.setPower(0);
        leftBack.setPower(0);
        rightFront.setPower(0);
        rightBack.setPower(0);
        waitOneFullHardwareCycle();
        resetEncoder(rightBack);
        waitOneFullHardwareCycle();
    }
    private void initVision() throws InterruptedException {
        //Wait for vision to initialize - this should be the first thing you do
        waitForVisionStart();

        this.setCamera(Cameras.PRIMARY);
        this.setFrameSize(new Size(900, 900));

        enableExtension(Extensions.BEACON);         //Beacon detection
        enableExtension(Extensions.ROTATION);       //Automatic screen rotation correction
        enableExtension(Extensions.CAMERA_CONTROL); //Manual camera control

        beacon.setAnalysisMethod(Beacon.AnalysisMethod.FAST);
        beacon.setColorToleranceRed(0);
        beacon.setColorToleranceBlue(0);
        beacon.setAnalysisBounds(new Rectangle(new Point(width / 2, height / 2), width - 200, 200));

        rotation.setIsUsingSecondaryCamera(false);
        rotation.disableAutoRotate();
        rotation.setActivityOrientationFixed(ScreenOrientation.LANDSCAPE_REVERSE);

        cameraControl.setColorTemperature(CameraControlExtension.ColorTemperature.AUTO);
        cameraControl.setAutoExposureCompensation();

        //Wait for the match to begin
        waitForStart();
    }

    private void visionLoop() throws InterruptedException {
        //Log a few things
        telemetry.addData("Beacon Color", beacon.getAnalysis().getColorString());
        telemetry.addData("Beacon Center", beacon.getAnalysis().getLocationString());
        telemetry.addData("Beacon Confidence", beacon.getAnalysis().getConfidenceString());
        telemetry.addData("Beacon Buttons", beacon.getAnalysis().getButtonString());
        telemetry.addData("Screen Rotation", rotation.getScreenOrientationActual());
        telemetry.addData("Frame Rate", fps.getFPSString() + " FPS");
        telemetry.addData("Frame Size", "Width: " + width + " Height: " + height);
        telemetry.addData("Frame Counter", frameCount);

        discardFrame();

        //Wait for a hardware cycle to allow other processes to run
        waitOneFullHardwareCycle();
    }

    @Override
    public void runOpMode() throws InterruptedException {
        navx = new NavXDevice(hardwareMap, "dim",NAVX_DIM_I2C_PORT);
        leftFront = hardwareMap.dcMotor.get("lf");
        rightFront = hardwareMap.dcMotor.get("rf");
        leftBack = hardwareMap.dcMotor.get("lb");
        rightBack = hardwareMap.dcMotor.get("rb");

        leftFront.setDirection(DcMotor.Direction.REVERSE);
        leftBack.setDirection(DcMotor.Direction.REVERSE);
        leftBack.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);

        lift = hardwareMap.dcMotor.get("lift");
        intake = hardwareMap.dcMotor.get("intake");
        raiser = hardwareMap.dcMotor.get("raiser");

        leftHook = hardwareMap.servo.get("leftHook");
        rightHook = hardwareMap.servo.get("rightHook");
        climberDumper = hardwareMap.servo.get("climberDumper");
        climberReleaser = hardwareMap.servo.get("climberReleaser");
        blockPusher = hardwareMap.servo.get("blockPusher");

        colorSensor = hardwareMap.colorSensor.get("color");
        initVision();
        waitForStart();
        driveTo(500, 1, 1);
        turnToDegNavX(315, -.4);
        resetEncoder(rightBack);

        telemetry.clearData();

        while (colorSensor.alpha() < 30 && Math.abs(rightBack.getCurrentPosition()) < 6000){
            telemetry.addData("white val",colorSensor.alpha());
            leftFront.setPower(.75);
            leftBack.setPower(.75);
            rightFront.setPower(.75);
            rightBack.setPower(.75);
        }
        waitOneFullHardwareCycle();
        leftFront.setPower(0);
        leftBack.setPower(0);
        rightFront.setPower(0);
        rightBack.setPower(0);
        sleep(5000);
        waitOneFullHardwareCycle();
        driveTo(100, .2, .2);
        waitOneFullHardwareCycle();
        sleep(5000);
        while (colorSensor.alpha() < 30){
            telemetry.addData("white val",colorSensor.alpha());
            leftFront.setPower(.5);
            leftBack.setPower(.5);
            rightFront.setPower(-.5);
            rightBack.setPower(-.5);
        }
        waitOneFullHardwareCycle();
        leftFront.setPower(0);
        leftBack.setPower(0);
        rightFront.setPower(0);
        rightBack.setPower(0);
        waitOneFullHardwareCycle();
        sleep(5000);
        //Camera frames and OpenCV analysis will be delivered to this method as quickly as possible
        //This loop will exit once the opmode is closed

        while (opModeIsActive()) {
            visionLoop();
        }
    }
}
