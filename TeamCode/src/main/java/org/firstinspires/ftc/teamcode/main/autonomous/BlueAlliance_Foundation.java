package org.firstinspires.ftc.teamcode.main.autonomous;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;

import static android.graphics.ColorSpace.Model.XYZ;

@Autonomous(name = "Foundation BLUE ALLIANCE", group = "1blue")

public class BlueAlliance_Foundation extends LinearOpMode{

    private DcMotor frontRight;
    private DcMotor frontLeft;
    private DcMotor backRight;
    private DcMotor backLeft;

    private Servo positioningServo1;
    private Servo positioningServo2;

    private BNO055IMU imu;

    int refGyro = 0;

    private  Movement motors = new Movement();

    @Override
    public void runOpMode() throws InterruptedException {

        new Init().imu();
        new Init().motorInit();
        new Init().foundation();

        refGyro = (int)imu.getAngularOrientation(AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES).thirdAngle;

        while(!opModeIsActive() && !isStopRequested()){
            telemetry.addData("Waiting for ", "start.");
            telemetry.update();
        }

        if(opModeIsActive()){

            motors.front(0.8, 2300);

            sleep(200);

            positioningServo1.setPosition(1);
            positioningServo2.setPosition(1);
            motors.stop();

            sleep(1500);

            motors.front(-0.5, 2600);
            motors.rotate(0.5, 15);

            positioningServo1.setPosition(0);
            positioningServo2.setPosition(0);
            motors.stop();

            while(new Movement().new Encoders().overallWheelEnc() <= 3500 && !isStopRequested()) {
                motors.sideways(-0.8);
            }

            motors.stop();
        }

    }

    class Init {

        private void motorInit() {

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

    class Movement{

        Encoders encoders = new Encoders();
        Correction correction = new Correction();

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

                refEnc = new Encoders().frontRightEnc();

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
