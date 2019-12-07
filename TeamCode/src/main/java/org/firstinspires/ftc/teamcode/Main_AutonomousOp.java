package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous

public class Main_AutonomousOp extends LinearOpMode {

    private DcMotor frontRightMotor;
    private DcMotor frontLeftMotor;
    private DcMotor backRightMotor;
    private DcMotor backLeftMotor;

    BNO055IMU imu;
    Init init = new Init();
    Motors motors = new Motors();

    ElapsedTime timer = new ElapsedTime();

    public void setTimer() {
        timer = new ElapsedTime();
    }

    @Override
    public void runOpMode() throws InterruptedException {
        init.wheels();

        while(!opModeIsActive() && !isStopRequested()) {
            telemetry.addData("Waiting for ", "start.");
            telemetry.update();
        }

        if(opModeIsActive()){

            motors.forward(0.5, 800);

            motors.left(0.3, 1500);

            motors.forward(0.5, 2000);
        }

    }
    class Init {
        public void wheels() {

            frontRightMotor = hardwareMap.dcMotor.get("frontRightMotor");
            frontLeftMotor = hardwareMap.dcMotor.get("frontLeftMotor");
            backRightMotor = hardwareMap.dcMotor.get("backRightMotor");
            backLeftMotor = hardwareMap.dcMotor.get("backLeftMotor");

            frontLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
            backLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
            frontRightMotor.setDirection(DcMotorSimple.Direction.FORWARD);
            backRightMotor.setDirection(DcMotorSimple.Direction.FORWARD);

            resetEncoder();
        }

        public void resetEncoder (){
            frontLeftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            frontRightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            backLeftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            backRightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            frontLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            frontRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            backLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            backRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }

        public void imu(){

            BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
            parameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
            parameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
            parameters.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibration sample opmode
            parameters.loggingEnabled      = true;
            parameters.loggingTag          = "IMU";
            parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();

            imu.initialize(parameters);
        }
    }
    class Motors {

        Init mInit = new Init();

        private void showEncoders(){
            telemetry.addData("Front Right Motor: ", frontRightMotor.getCurrentPosition());
            telemetry.addData("Back Right Motor: ", backRightMotor.getCurrentPosition());
            telemetry.addData("Front Left Motor: ", frontLeftMotor.getCurrentPosition());
            telemetry.addData("Back Left Motor: ", backLeftMotor.getCurrentPosition());
            telemetry.update();
        }

        public void stop (){
            frontRightMotor.setPower(0);
            frontLeftMotor.setPower(0);
            backRightMotor.setPower(0);
            backLeftMotor.setPower(0);
            showEncoders();

            mInit.resetEncoder();

            sleep(200);
        }
        public void forward (double power, double target) {
            frontRightMotor.setPower(power);
            frontLeftMotor.setPower(power);
            backRightMotor.setPower(power);
            backLeftMotor.setPower(power);

            while(Math.abs(frontRightMotor.getCurrentPosition()) <= target && opModeIsActive()){
                showEncoders();
            }
            stop();
        }
        public void left (double power, double target){
            frontLeftMotor.setPower(-power);
            frontRightMotor.setPower(power);
            backLeftMotor.setPower(-power);
            backRightMotor.setPower(power);
            while(Math.abs(frontRightMotor.getCurrentPosition()) <= target && opModeIsActive()){
                showEncoders();
            }
            stop();
        }
        public void right (double power, double target){
            frontLeftMotor.setPower(power);
            frontRightMotor.setPower(-power);
            backLeftMotor.setPower(power);
            backRightMotor.setPower(-power);

            while(Math.abs(frontRightMotor.getCurrentPosition()) <= target && opModeIsActive()){
                showEncoders();
            }
            stop();
        }
    }
}
