package org.firstinspires.ftc.teamcode.main.tests;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

public class PIDMovement extends LinearOpMode {

    private DcMotor frontRight;
    private DcMotor frontLeft;
    private DcMotor backRight;
    private DcMotor backLeft;

    private BNO055IMU imu;

    @Override
    public void runOpMode() throws InterruptedException {

        new Init().imu();
        new Init().wheels();

        while(!opModeIsActive() && !isStopRequested()){
            telemetry.addData("Waiting for ", "start.");
            telemetry.update();
        }

        if(opModeIsActive()){

        }
    }

    class Init{

        private void wheels(){

            frontLeft = hardwareMap.dcMotor.get("frontLeft");
            frontRight = hardwareMap.dcMotor.get("frontRight");
            backLeft = hardwareMap.dcMotor.get("backLeft");
            backRight = hardwareMap.dcMotor.get("backRight");

            ////////////////////////////////////////////////////

            frontRight.setDirection(DcMotorSimple.Direction.FORWARD);
            backRight.setDirection(DcMotorSimple.Direction.FORWARD);
            frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
            backLeft.setDirection(DcMotorSimple.Direction.REVERSE);

            ////////////////////////////////////////////////////

            frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

            ////////////////////////////////////////////////////
        }

        private void imu(){
            BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
            parameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
            parameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
            parameters.calibrationDataFile = "BNO055IMUCalibration.json";
            parameters.loggingEnabled      = true;
            parameters.loggingTag          = "IMU";
            parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();

            imu = hardwareMap.get(BNO055IMU.class, "imu");
            imu.initialize(parameters);
        }

    }

    class Encoders{

        private void resetWheelEnc(){

            frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }

        private int backleftEnc() {
            return backLeft.getCurrentPosition();
        }

        private int backEightEnc() {
            return backRight.getCurrentPosition();
        }

        private int frontLeftEnc() {
            return frontLeft.getCurrentPosition();
        }

        private int frontRightEnc() {
            return frontRight.getCurrentPosition();
        }

        private double overallWheelEnc(){
            double average =
                    Math.abs(frontLeft.getCurrentPosition()) + Math.abs(frontRight.getCurrentPosition())
                            + Math.abs(backLeft.getCurrentPosition()) + Math.abs(backRight.getCurrentPosition());
            return average/4;
        }
    }

    class Movement{

        Encoders enc = new Encoders();

        private void stop(){

            frontRight.setPower(0);
            frontLeft.setPower(0);
            backRight.setPower(0);
            backLeft.setPower(0);

            enc.resetWheelEnc();
        }

        private void beeline(int degrees){


        }
    }
}
