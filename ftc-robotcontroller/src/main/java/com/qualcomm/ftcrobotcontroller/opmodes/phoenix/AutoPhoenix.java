package com.qualcomm.ftcrobotcontroller.opmodes.phoenix;

import com.lasarobotics.library.sensor.kauailabs.navx.NavXDevice;
import com.lasarobotics.library.util.MathUtil;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;


import org.lasarobotics.vision.android.Cameras;
import org.lasarobotics.vision.detection.objects.Rectangle;
import org.lasarobotics.vision.ftc.resq.Beacon;
import org.lasarobotics.vision.opmode.LinearVisionOpMode;
import org.lasarobotics.vision.util.ScreenOrientation;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Size;

import android.util.Log;

/**
 * Pheonix (TM) Autonomous
 */
public class AutoPhoenix extends LinearVisionOpMode {
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
    private void initVision() throws InterruptedException {
        waitForVisionStart();

        this.setCamera(Cameras.PRIMARY);
        this.setFrameSize(new Size(900, 900));

        enableExtension(Extensions.BEACON);
        enableExtension(Extensions.ROTATION);
        //enableExtension(Extensions.CAMERA_CONTROL);
        rotation.setDefaultOrientation(ScreenOrientation.PORTRAIT);
        beacon.setAnalysisMethod(Beacon.AnalysisMethod.FAST);
        //Rectangle bounds = new Rectangle(new Point(width / 2, height / 2), width - 200, 200);
        //beacon.setAnalysisBounds(bounds);
    }
    @Override
    public void runOpMode() throws InterruptedException {
        left = hardwareMap.dcMotor.get("l");
        right = hardwareMap.dcMotor.get("r");
        left.setDirection(DcMotor.Direction.REVERSE);
        navx = new NavXDevice(hardwareMap, "dim",NAVX_DIM_I2C_PORT);
        initVision();
        waitForStart();
        driveTo(500, 1, 1);
        turnToDegNavX(320, -1);
        driveTo(7600, 1, 1);
        turnToDegNavX(315, -1);
        sleep(1000);
        driveTo(800, 1, 1);
        sleep(1000);
        while (opModeIsActive()) {
            //Log a few things
            telemetry.addData("Beacon Color", beacon.getAnalysis().getColorString());
            telemetry.addData("Beacon Location (Center)", beacon.getAnalysis().getLocationString());
            telemetry.addData("Beacon Confidence", beacon.getAnalysis().getConfidenceString());
            telemetry.addData("Rotation Compensation", rotation.getRotationCompensationAngle());
            telemetry.addData("Frame Rate", fps.getFPSString() + " FPS");
            telemetry.addData("Frame Size", "Width: " + width + " Height: " + height);
            telemetry.addData("Frame Counter", frameCount);
            //You can access the most recent frame data and modify it here using getFrameRgba() or getFrameGray()
            //Vision will run asynchronously (parallel) to any user code so your programs won't hang
            //You can use hasNewFrame() to test whether vision processed a new frame
            //Once you copy the frame, discard it immediately with discardFrame()
            if (hasNewFrame()) {
                //Get the frame
                Mat rgba = getFrameRgba();
                Mat gray = getFrameGray();
                //Discard the current frame to allow for the next one to render
                discardFrame();
                //Do all of your custom frame processing here
                //For this demo, let's just add to a frame counter
                frameCount++;
            }
            //Wait for a hardware cycle to allow other processes to run
            waitOneFullHardwareCycle();
        }
    }
}
