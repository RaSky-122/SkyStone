package org.firstinspires.ftc.teamcode.main.tests;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.*;

import org.firstinspires.ftc.teamcode.tests.MotorMovement;

import static org.firstinspires.ftc.robotcore.external.navigation.AngleUnit.DEGREES;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.XYZ;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesReference.EXTRINSIC;

public class VuforiaAutonomous extends LinearOpMode {

    private static final float mmPerInch        = 25.4f;
    private static final float mmTargetHeight   = (6) * mmPerInch;

    private static final float stoneZ = 2.00f * mmPerInch;

    private DcMotor frontRight;
    private DcMotor frontLeft;
    private DcMotor backRight;
    private DcMotor backLeft;

    private Servo positioningServo1;
    private Servo positioningServo2;

    private Servo autoServo1;
    private Servo autoServo2;

    private static final String TFOD_MODEL_ASSET = "Skystone.tflite";
    private static final String LABEL_FIRST_ELEMENT = "Stone";
    private static final String LABEL_SECOND_ELEMENT = "Skystone";

    private static final String VUFORIA_KEY =
            "Ab7Zg4v/////AAABmZKeB/V4p0DYmJFYkJRpaeou+GcJbvvo75+A7Spuy3TFuR7rC0nOwuzdeXJ7XvtxlPAkZYwkdO/EW6CU6mfG8AEqTsAxy//INcvlqdvdnkZeWG3qKsllZorOFWuKfCcrIM3TyA7iOsG2gl0jPG7+PF2S6kpYhHhJ4h1B+9l1B/dx/pQi96ktSn5L8Df3/sAn9WIPlcmRtcByc7N1/cA7liBfwaeiY+HVJZbtuJrxUg+LRJumxU6xjh0Cgq+OcyvXeMXA15i7i/5js/jFa+AqV8jIDNGZpdOyVlgrpSV+/bZsLcnFFY9GSIgYXHbLpFtkv6tQpUd/4kamH3qyxvFRhh5w9uXPVa1Q0xMiLQhUxiuD";

    private VuforiaLocalizer vuforia;

    private BNO055IMU imu;

    Init ini = new Init();
    MotorMovement motors = new MotorMovement();

    int refGyro = 0;

    @Override
    public void runOpMode() throws InterruptedException {

        ini.vuforia();
        ini.imu();
        ini.motor();
        ini.foundation();

        while (!opModeIsActive() && !isStopRequested()){
            telemetry.addData("Waiting for ", "start.");
            telemetry.update();
        }

        if (opModeIsActive()){

            while(!isStopRequested()){
                telemetry.addData("Skystone Y axis: ", new Camera().skystoneTranslation());
                telemetry.update();
            }
        }

    }
    class Init {

        VuforiaTrackables gameElements = vuforia.loadTrackablesFromAsset("Skystone");
        VuforiaTrackable skystone = gameElements.get(0);

        private void setSkystone() {
            skystone.setName("Skystone");
            skystone.setLocation(OpenGLMatrix
                    .translation(0, 0, stoneZ)
                    .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0, -90)));

        }

        private void motor() {

            backLeft = hardwareMap.dcMotor.get("backLeft");
            backRight = hardwareMap.dcMotor.get("backRight");
            frontLeft = hardwareMap.dcMotor.get("frontLeft");
            frontRight = hardwareMap.dcMotor.get("frontRight");

            backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

            frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
            backLeft.setDirection(DcMotorSimple.Direction.REVERSE);
            frontRight.setDirection(DcMotorSimple.Direction.FORWARD);
            backRight.setDirection(DcMotorSimple.Direction.FORWARD);

            frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }

        private void foundation() {

            positioningServo1 = hardwareMap.servo.get("positioningServo1");
            positioningServo2 = hardwareMap.servo.get("positioningServo2");

            positioningServo1.setDirection(Servo.Direction.FORWARD);
            positioningServo2.setDirection(Servo.Direction.REVERSE);

            positioningServo1.scaleRange(0.2, 1);
            positioningServo2.scaleRange(0, 1 - 0.2);
        }

        private void imu() {
            BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
            parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
            parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
            parameters.calibrationDataFile = "BNO055IMUCalibration.json";
            parameters.loggingEnabled = true;
            parameters.loggingTag = "IMU";
            parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();

            imu = hardwareMap.get(BNO055IMU.class, "imu");
            imu.initialize(parameters);
        }

        private void vuforia() {

            int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
            VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

            parameters.vuforiaLicenseKey = VUFORIA_KEY;
            parameters.cameraName = hardwareMap.get(WebcamName.class, "Webcam 1");

            setSkystone();

            vuforia = ClassFactory.getInstance().createVuforia(parameters);

            gameElements.activate();
        }
    }

    class Camera {
        VuforiaTrackable skystone = new Init().skystone;

        private double skystoneTranslation() {
            return ((OpenGLMatrix)skystone).getTranslation().get(1);
        }
    }

    class Movement{

        Movement.Encoders encoders = new Movement.Encoders();
        Movement.Correction correction = new Movement.Correction();

        class Encoders{

            private void resetWheelEnoders(){

                frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

                frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
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

        class Correction{

            double kp = 0.025;
            double ki = 0.008;
            double kd = 0;

            int refEnc = 0;

            int gyroError = 0;
            int gyroError2 = 0;

            private double backLeftChange(){

                refEnc = new Movement.Encoders().frontRightEnc();

                return 0;
            }

            private double gyroCorrection(){

                int currentGyro = (int)imu.getAngularOrientation(AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES).thirdAngle;
                gyroError = currentGyro - refGyro;

                double speedChange = kp*gyroError + ki*(gyroError + gyroError2) + kd*(gyroError - gyroError2);

                gyroError2 = gyroError;

                return speedChange;
            }
        }

        private void rotate(double power, int target){
            int initialGyro = (int)imu.getAngularOrientation(AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES).thirdAngle;
            int currentGyro = initialGyro;

            while(Math.abs(currentGyro - initialGyro) <= target && !isStopRequested()){
                frontLeft.setPower(-power);
                backLeft.setPower(-power);
                frontRight.setPower(power);
                backRight.setPower(power);

                currentGyro = (int)imu.getAngularOrientation(AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES).thirdAngle;
            }

            motors.stop();
        }

        private void sideways(double power) {

            double speedChange = new Movement.Correction().gyroCorrection();

            backLeft.setPower(power);
            frontRight.setPower(power);
            backRight.setPower(-power - speedChange);
            frontLeft.setPower(-power + speedChange);
        }

        private void front(double power, long target){

            while (((Math.abs(encoders.overallWheelEnc())) <= Math.abs(target) || target == 0) && !isStopRequested()) {

                double rightEnc = frontRight.getCurrentPosition() + backRight.getCurrentPosition();
                double leftEnc = frontLeft.getCurrentPosition() + backLeft.getCurrentPosition();


                frontRight.setPower(power);
                frontLeft.setPower(power);
                backRight.setPower(power);
                backLeft.setPower(power);
            }
        }

        private void stop(){
            frontRight.setPower(0);
            frontLeft.setPower(0);
            backRight.setPower(0);
            backLeft.setPower(0);

            encoders.resetWheelEnoders();
        }
    }
}
