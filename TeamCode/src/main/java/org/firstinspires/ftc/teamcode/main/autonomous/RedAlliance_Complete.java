package org.firstinspires.ftc.teamcode.main.autonomous;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.List;

@Autonomous(name = "Complete: RED ALLIANCE", group = "1red")

public class RedAlliance_Complete extends LinearOpMode {

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

    private TFObjectDetector tfod;

    private BNO055IMU imu;

    float skyStone1 = -100;
    float skyStone2 = -100;

    int position = -2;

    private Camera camera = new Camera();
    private Movement motors = new Movement();

    int refGyro = 0;

    @Override
    public void runOpMode() throws InterruptedException {

        new Init().vuforia();
        new Init().imu();
        new Init().foundation();

        refGyro = (int)imu.getAngularOrientation(AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES).thirdAngle;

        if (ClassFactory.getInstance().canCreateTFObjectDetector()) {
            new Init().tfod();
        } else {
            telemetry.addData("Sorry!", "This device is not compatible with TFOD");
        }

        if (tfod != null) {
            tfod.activate();
        }

        new Init().motorInit();
        new Init().autoServos();

        int encoderSkystone = 0;

        while(!opModeIsActive() && !isStopRequested()) {
            telemetry.addData("Waiting for ", "start.");
            telemetry.update();
        }

        if(opModeIsActive()){

            motors.front(0.2, 100);
            positioningServo1.setPosition(0);
            positioningServo2.setPosition(0);
            motors.stop();

            camera.skystonePosition();
            sleep(500);

            while(position == -2 && !isStopRequested()) {
                position = camera.skystonePosition();
            }

            switch (position){
                case -1:
                    telemetry.addData("left", " left");
                    while(Math.abs(new Movement().new Encoders().overallWheelEnc()) <= 750 && !isStopRequested()){
                        motors.sideways(0.5);
                    }
                    encoderSkystone = (int)Math.abs(new Movement().new Encoders().overallWheelEnc());
                    break;
                case 0: telemetry.addData("middle", " middle"); break;
                case 1:
                    telemetry.addData("right", " right");
                    while(Math.abs(new Movement().new Encoders().overallWheelEnc()) <= 750 && !isStopRequested()){
                        motors.sideways(-0.5);
                    }
                    encoderSkystone = (int) -Math.abs(new Movement().new Encoders().overallWheelEnc());
                    break;
            }

            motors.stop();

            motors.front(0.5, 2650);
            motors.stop();
            autoServo1.setPosition(1);

            sleep(300);

            motors.front(-0.5, 850);
            motors.stop();
            while(new Movement().new Encoders().overallWheelEnc() <= 4900 + encoderSkystone && !isStopRequested()) {
                motors.sideways(-0.8);
            }

            encoderSkystone = (int)new Movement().new Encoders().overallWheelEnc();
            motors.stop();
            autoServo1.setPosition(0);
            sleep(200);
            telemetry.update();

            while(new Movement().new Encoders().overallWheelEnc() <= 2050 + encoderSkystone && !isStopRequested()){
                motors.sideways(0.8);
            }

            encoderSkystone = (int)new Movement().new Encoders().overallWheelEnc();

            motors.stop();

            motors.front(0.5, 850);
            motors.stop();
            autoServo1.setPosition(1);
            sleep(200);

            motors.front(-0.5, 850);
            motors.stop();

            while(new Movement().new Encoders().overallWheelEnc() <= 800 + encoderSkystone && !isStopRequested()) {
                motors.sideways(-0.8);
            }

            motors.stop();
            autoServo1.setPosition(0);

            /**while(new Movement().new Encoders().overallWheelEnc() <= 2000 && !isStopRequested()){
             motors.sideways(-0.8);
             }*/

            while(new Movement().new Encoders().overallWheelEnc() <= 2300 && !isStopRequested()) {
                motors.sideways(-0.8);
            }
            motors.stop();

            motors.front(0.8, 500);
            motors.stop();

            sleep(200);

            positioningServo2.setPosition(1);
            positioningServo1.setPosition(1);
            sleep(1500);

            motors.stop();

            motors.front(-0.8, 300);
            motors.rotate(-0.5, 5);

            motors.front(-0.8, 1550);
            motors.rotate(-0.5, 90);

            positioningServo1.setPosition(0);
            positioningServo2.setPosition(0);
            motors.stop();

            motors.front(-0.5, 3000);

            /**while(new Movement().new Encoders().overallWheelEnc() <= 4000 && !isStopRequested()) {
             motors.sideways(-0.8);
             }*/

            motors.stop();
        }
    }

    class Init{

        private void motorInit(){

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

        private void vuforia(){
            VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

            parameters.vuforiaLicenseKey = VUFORIA_KEY;
            parameters.cameraName = hardwareMap.get(WebcamName.class, "Webcam 1");

            vuforia = ClassFactory.getInstance().createVuforia(parameters);
        }

        private void tfod(){
            int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                    "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
            TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
            tfodParameters.minimumConfidence = 0.84;
            tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
            tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_FIRST_ELEMENT, LABEL_SECOND_ELEMENT);
        }

        private void autoServos(){

            autoServo1 = hardwareMap.servo.get("autoServo1");
            autoServo2 = hardwareMap.servo.get("autoServo2");

            autoServo1.setDirection(Servo.Direction.REVERSE);
            autoServo2.setDirection(Servo.Direction.FORWARD);

            autoServo1.scaleRange(1-0.6, 1);
            autoServo2.scaleRange(0, 0.6);
        }
    }

    class Camera{
        private int skystonePosition(){
            List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();

            int stone1  = -1000;
            int stone2 = -1000;
            int skystone = -1000;

            if(updatedRecognitions != null) {
                for (Recognition recognition : updatedRecognitions) {
                    if(recognition.getLabel().equals(LABEL_FIRST_ELEMENT)) {
                        if (stone1 == -1000)
                            stone1 = (int)recognition.getRight();
                        else if (stone2 == -1000)
                            stone2 = (int)recognition.getRight();
                    }
                    else skystone = (int)recognition.getRight();
                }

                telemetry.addData("Stone1: ", stone1);
                telemetry.addData("Stone2: ", stone2);
                telemetry.addData("Skystone: ", skystone);
                telemetry.update();

                if(skystone == -1000)
                    return -1;
                else if(skystone < stone1)
                    return 0;
                else if(skystone > stone1)
                    return 1;
            }

            return -2;
        }

        private void tfodDetector(){
            List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();

            skyStone1 = -100;
            skyStone2 = -100;

            if(updatedRecognitions != null){
                for(Recognition recognition : updatedRecognitions){
                    if(recognition.getLabel().equals(LABEL_SECOND_ELEMENT)){
                        if(skyStone1 == -100)
                            skyStone1 = recognition.getLeft();
                        else skyStone2 = recognition.getLeft();
                    }
                }
                if(skyStone1 > skyStone2 && skyStone2 != -100){
                    float aux = skyStone1;
                    skyStone1 = skyStone2;
                    skyStone2 = aux;
                }

                telemetry.addData("Skystone 1 ", skyStone1);
                telemetry.addData("Skystone 2 ", skyStone2);
                telemetry.update();
            }
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

            double speedChange = new Correction().gyroCorrection();

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
