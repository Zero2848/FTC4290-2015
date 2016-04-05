package com.qualcomm.ftcrobotcontroller.opmodes.purplebot;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;

import org.lasarobotics.vision.android.Cameras;
import org.lasarobotics.vision.detection.objects.Rectangle;
import org.lasarobotics.vision.ftc.resq.Beacon;
import org.lasarobotics.vision.opmode.LinearVisionOpMode;
import org.lasarobotics.vision.util.ScreenOrientation;
import org.opencv.core.Point;
import org.opencv.core.Size;

/**
 * Purplebot (TM) Autonomous
 */
public class AutoPurplebot extends LinearVisionOpMode {
    //Frame counter
    int frameCount = 0;
    DcMotor leftFront,leftBack,rightBack,rightFront;

    public static void resetEncoder(DcMotor m) {
        while (m.getCurrentPosition() != 0) {
            m.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        }
        m.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
    }

    public void driveTo(int ticks,double powerl,double powerr) throws InterruptedException {
        double wheelPosition = 0;
        while (Math.abs(wheelPosition) < ticks) {
            wheelPosition = leftFront.getCurrentPosition();
            telemetry.addData("Wheel at", wheelPosition + " ticks.");
            leftFront.setPower(powerl);
            leftBack.setPower(powerl);
            rightFront.setPower(powerr);
            rightBack.setPower(powerr);
            waitOneFullHardwareCycle();
        }
        leftBack.setPower(0);
        leftFront.setPower(0);
        rightFront.setPower(0);
        rightBack.setPower(0);
        waitOneFullHardwareCycle();
        resetEncoder(leftFront);
        waitOneFullHardwareCycle();
    }

    private void initVision() throws InterruptedException {
        //Wait for vision to initialize - this should be the first thing you do
        waitForVisionStart();

        //Set the camera used for detection
        this.setCamera(Cameras.PRIMARY);

        //Set the frame size
        //Larger = sometimes more accurate, but also much slower
        //For Testable OpModes, this might make the image appear small - it might be best not to use this
        //After this method runs, it will set the "width" and "height" of the frame
        this.setFrameSize(new Size(900, 900));

        //Enable extensions. Use what you need.
        enableExtension(Extensions.BEACON);     //Beacon detection
        enableExtension(Extensions.ROTATION);   //Automatic screen rotation correction

        //UNCOMMENT THIS IF you're using a SECONDARY (facing toward screen) camera
        //or when you rotate the phone, sometimes the colors swap
        //rotation.setRotationInversion(true);

        //Set this to the default orientation of your program (it's probably PORTRAIT)
        //Also, it's recommended to turn OFF Auto Rotate
        //If you can't get any readings or swap red and blue, try changing this
        rotation.setDefaultOrientation(ScreenOrientation.PORTRAIT);

        //Set the beacon analysis method
        //Try them all and see what works!
        beacon.setAnalysisMethod(Beacon.AnalysisMethod.FAST);

        //Set analysis boundary
        //You should comment this to use the entire screen and uncomment only if
        //you want faster analysis at the cost of not using the entire frame.
        //This is also particularly useful if you know approximately where the beacon is
        //as this will eliminate parts of the frame which may cause problems
        //This will not work on some methods, such as COMPLEX
        Rectangle bounds = new Rectangle(new Point(width / 2, height / 2), width - 200, 200);
        beacon.setAnalysisBounds(bounds);
        //Or you can just use the entire screen
        //beacon.setAnalysisBounds(new Rectangle(0, 0, height, width));
    }


    private void initRobot() {

    }

    @Override
    public void runOpMode() throws InterruptedException {
        leftFront = hardwareMap.dcMotor.get("lf");
        rightFront = hardwareMap.dcMotor.get("rf");
        leftBack = hardwareMap.dcMotor.get("lb");
        rightBack = hardwareMap.dcMotor.get("rb");
        leftBack.setDirection(DcMotor.Direction.REVERSE);
        leftFront.setDirection(DcMotor.Direction.REVERSE);

        //Initialize Vision
        //initVision();

        //Initialize robot control
        initRobot();

        //Wait for the match to begin
        waitForStart();
        driveTo(8500,-1,-1);
        sleep(1000);
        driveTo(600,-1,1);
        sleep(1000);
        driveTo(500,-1,-1);
        //Main loop
        //Camera frames and OpenCV analysis will be delivered to this method as quickly as possible
        //This loop will exit once the opmode is closed
        /**while (opModeIsActive()) {
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
         }**/
    }
}
