package com.qualcomm.ftcrobotcontroller.opmodes;


import com.lasarobotics.library.controller.ButtonState;
import com.lasarobotics.library.controller.Controller;
import com.lasarobotics.library.drive.Tank;
import com.lasarobotics.library.monkeyc.MonkeyC;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;

public class monkeyWrite extends OpMode {
    Controller driver, operator;
    double winchPower = 0, leftPosition = hardware.leftServoTop, rightPosition = hardware.rightServoTop, climberPosition = hardware.climberBottom,
            anglerPower = 0, stopperPosition = hardware.stopperOff, leftGPosition = hardware.leftGUp, rightGPosition = hardware.rightGUp;
    MonkeyC writer;

    public void resetEncoder(DcMotor m){
        while(m.getCurrentPosition() != 0){
            m.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);
        }
        m.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
    }
    public void declare() {
        hardware.leftWheel = hardwareMap.dcMotor.get("l");
        hardware.leftWheel.setDirection(DcMotor.Direction.REVERSE);
        resetEncoder(hardware.leftWheel);
        hardware.rightWheel = hardwareMap.dcMotor.get("r");
        resetEncoder(hardware.rightWheel);
        hardware.winch1 = hardwareMap.dcMotor.get("w1");
        hardware.winch1.setDirection(DcMotor.Direction.REVERSE);
        hardware.winch2 = hardwareMap.dcMotor.get("w2");
        resetEncoder(hardware.winch2);
        hardware.angler = hardwareMap.dcMotor.get("a");
        hardware.angler.setDirection(DcMotor.Direction.REVERSE);

        hardware.climberLeft = hardwareMap.servo.get("cl");
        hardware.climberLeft.setPosition(hardware.leftServoTop);

        hardware.climberRight = hardwareMap.servo.get("cr");
        hardware.climberRight.setPosition(hardware.rightServoTop);

        hardware.stopper = hardwareMap.servo.get("s");
        hardware.stopper.setPosition(hardware.stopperOff);

        hardware.climber = hardwareMap.servo.get("c");
        hardware.climber.setPosition(hardware.climberBottom);

        hardware.leftGrabber = hardwareMap.servo.get("lg");
        hardware.rightGrabber = hardwareMap.servo.get("rg");
    }
    public void grabbers(){
        if(driver.x == ButtonState.PRESSED && leftGPosition != hardware.leftGDown) {
            leftGPosition = hardware.leftGDown;
            rightGPosition = hardware.rightGDown;
        } else if(driver.x == ButtonState.PRESSED && leftGPosition != hardware.leftGUp) {
            leftGPosition = hardware.leftGUp;
            rightGPosition = hardware.rightGUp;
        }
    }
    public void climberDump(){
        if(operator.y == ButtonState.PRESSED && climberPosition != hardware.climberTop) {
            climberPosition=hardware.climberTop;
        } else if(operator.y == ButtonState.PRESSED && climberPosition != hardware.climberBottom) {
            climberPosition=hardware.climberBottom;
        }

    }
    public void leftServo(){
        if(operator.x == ButtonState.PRESSED && leftPosition != hardware.leftServoTop) {
            leftPosition=hardware.leftServoTop;
        } else if(operator.x == ButtonState.PRESSED && leftPosition != hardware.leftServoBottom) {
            leftPosition=hardware.leftServoBottom;
        }
    }
    public void rightServo(){
        if(operator.b == ButtonState.PRESSED && rightPosition != hardware.rightServoTop) {
            rightPosition=hardware.rightServoTop;
        } else if(operator.b == ButtonState.PRESSED && rightPosition != hardware.rightServoBottom) {
            rightPosition=hardware.rightServoBottom;
        }
    }
    public void stopper(){
        if(operator.a == ButtonState.PRESSED && stopperPosition != hardware.stopperOn) {
            stopperPosition = hardware.stopperOn;
        } else if(operator.a == ButtonState.PRESSED && stopperPosition != hardware.stopperOff) {
            stopperPosition = hardware.stopperOff;
        }
    }
    public void winch(){
        if(Math.abs(operator.right_stick_y) > .1 && stopperPosition != hardware.stopperOn) {
            if(hardware.winch2.getCurrentPosition() < hardware.winchCap && operator.right_stick_y > 0){
                winchPower = operator.right_stick_y;
            }
            else if(hardware.winch2.getCurrentPosition() > hardware.winchLow && operator.right_stick_y < 0){
                winchPower = operator.right_stick_y;
            } else {
                winchPower = 0;
            }
        } else {
            winchPower = 0;
        }
    }
    public void angler(){
        if(driver.y == ButtonState.HELD){//go up
            anglerPower = .75;
        } else if(driver.a == ButtonState.HELD){//go down
            anglerPower = -.75;
        } else {
            anglerPower = 0;
        }
    }
    public void displayTelemetry(){

        if(leftPosition == hardware.leftServoTop){
            telemetry.addData("Left Servo Is", "Off");
        } else if(leftPosition == hardware.leftServoBottom){
            telemetry.addData("Left Servo Is", "On");
        } else {
            telemetry.addData("Left Servo Is", "Not Connected!");
        }

        if(leftGPosition == hardware.leftGUp){
            telemetry.addData("Left Grabber Is", "Up");
        } else if(leftGPosition == hardware.leftGDown){
            telemetry.addData("Left Grabber Is", "Down");
        } else {
            telemetry.addData("Left Grabber Is", "Not Connected!");
        }
        if(rightGPosition == hardware.rightGDown){
            telemetry.addData("Right Grabber Is", "Down");
        } else if(rightGPosition == hardware.rightGUp){
            telemetry.addData("Right Grabber Is", "Up");
        } else {
            telemetry.addData("Left Grabber Is", "Not Connected!");
        }

        if(rightPosition == hardware.rightServoTop){
            telemetry.addData("Right Servo Is", "Off");
        } else if(rightPosition == hardware.rightServoBottom){
            telemetry.addData("Right Servo Is", "On");
        } else {
            telemetry.addData("Right Servo Is", "Not Connected!");
        }

        if(climberPosition == hardware.climberBottom){
            telemetry.addData("Climber Servo Is", "Off");
        } else if(climberPosition == hardware.climberTop){
            telemetry.addData("Climber Servo Is", "On");
        } else {
            telemetry.addData("Climber Servo Is", "Not Connected!");
        }
        if(stopperPosition == hardware.stopperOn){
            telemetry.addData("Stopper Servo Is", "On");
        } else if(stopperPosition == hardware.stopperOff){
            telemetry.addData("Stopper Servo Is", "Off");
        }
        telemetry.addData("Left Motor Encoder: ", hardware.leftWheel.getCurrentPosition());
        telemetry.addData("Right Motor Encoder: ", hardware.rightWheel.getCurrentPosition());
        telemetry.addData("Winch2 Encoder: ", hardware.winch2.getCurrentPosition());
        telemetry.addData("Left Motor Power: ", driver.left_stick_y);
        telemetry.addData("Right Motor Power: ", driver.right_stick_y);
        telemetry.addData("Winch Power: ", winchPower);
        telemetry.addData("Angler Power:", anglerPower);
    }
    public void housekeeping(){
        driver.update(gamepad1);
        operator.update(gamepad2);
        displayTelemetry();
    }

    @Override
    public void init() {
        declare();
        driver = new Controller(gamepad1);
        operator = new Controller(gamepad2);
    }

    @Override
    public void start() {
        writer = new MonkeyC();
    }

    @Override
    public void loop() {
        housekeeping();

        grabbers();//source of error
        hardware.rightGrabber.setPosition(rightGPosition);
        hardware.leftGrabber.setPosition(leftGPosition);

        climberDump();
        hardware.climber.setPosition(climberPosition);

        leftServo();
        hardware.climberLeft.setPosition(leftPosition);
        rightServo();
        hardware.climberRight.setPosition(rightPosition);
        stopper();
        hardware.stopper.setPosition(stopperPosition);

        winch();
        hardware.winch1.setPower(winchPower);
        hardware.winch2.setPower(winchPower);

        angler();
        hardware.angler.setPower(anglerPower);

        Tank.Motor2(hardware.leftWheel, hardware.rightWheel, driver.left_stick_y, driver.right_stick_y);
    }

    @Override
    public void stop() {
        writer.write("test.txt", hardwareMap.appContext);
    }
}