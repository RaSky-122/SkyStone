package org.firstinspires.ftc.teamcode;

import android.graphics.Camera;
import android.hardware.camera2.CameraDevice;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.Base64;
import java.util.List;

@Disabled
@Autonomous(name = "WORK IN PROGRESS", group = "test")

public class TensorFlowTest extends LinearOpMode {

    private DcMotor frontRight;
    private DcMotor frontLeft;
    private DcMotor backRight;
    private DcMotor backLeft;

    private Servo autoServo1;
    private Servo autoServo2;

    private DistanceSensor rightDistance;
    private DistanceSensor leftDistance;

    private static final String TFOD_MODEL_ASSET = "Skystone.tflite";
    private static final String LABEL_FIRST_ELEMENT = "Stone";
    private static final String LABEL_SECOND_ELEMENT = "Skystone";

    private static final String VUFORIA_KEY =
            "Ab7Zg4v/////AAABmZKeB/V4p0DYmJFYkJRpaeou+GcJbvvo75+A7Spuy3TFuR7rC0nOwuzdeXJ7XvtxlPAkZYwkdO/EW6CU6mfG8AEqTsAxy//INcvlqdvdnkZeWG3qKsllZorOFWuKfCcrIM3TyA7iOsG2gl0jPG7+PF2S6kpYhHhJ4h1B+9l1B/dx/pQi96ktSn5L8Df3/sAn9WIPlcmRtcByc7N1/cA7liBfwaeiY+HVJZbtuJrxUg+LRJumxU6xjh0Cgq+OcyvXeMXA15i7i/5js/jFa+AqV8jIDNGZpdOyVlgrpSV+/bZsLcnFFY9GSIgYXHbLpFtkv6tQpUd/4kamH3qyxvFRhh5w9uXPVa1Q0xMiLQhUxiuD";

    private VuforiaLocalizer vuforia;

    private TFObjectDetector tfod;

    float skyStone1 = -100;
    float skyStone2 = -100;

    @Override
    public void runOpMode() throws InterruptedException {

        new Init().vuforia();

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
        new Init().distanceSensors();

        while(!opModeIsActive() && !isStopRequested()) {
            tfodDetector();
        }

        tfodDetector();

        if(opModeIsActive()){

            new Movement().sideways(-0.3);
            while((skyStone1 >= 280 || skyStone1 == -100) && !isStopRequested()){
                tfodDetector();
            }

            new Movement().stop();

            tfodDetector();

            new Movement().sideways(0.3);

            while((skyStone1 <= 280 || skyStone1 == -100) && !isStopRequested()){
                tfodDetector();
            }

            new Movement().stop();
            while (rightDistance.getDistance(DistanceUnit.MM) >= 20 || leftDistance.getDistance(DistanceUnit.MM) >= 20)
                new Movement().front(0.5, 0);

            new Movement().stop();

            autoServo2.setPosition(1);
            sleep(300);

            new Movement().front(-0.5, 600);

            new Movement().stop();

            while(!isStopRequested());
        }
    }

    class Init{

        private void motorInit(){

            backLeft = hardwareMap.dcMotor.get("backLeft");
            backRight = hardwareMap.dcMotor.get("backRight");
            frontLeft = hardwareMap.dcMotor.get("frontLeft");
            frontRight = hardwareMap.dcMotor.get("frontRight");

            backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

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
            tfodParameters.minimumConfidence = 0.8;
            tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
            tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_FIRST_ELEMENT, LABEL_SECOND_ELEMENT);
        }

        private void autoServos(){

            autoServo1 = hardwareMap.servo.get("autoServo1");
            autoServo2 = hardwareMap.servo.get("autoServo2");

            autoServo1.setDirection(Servo.Direction.REVERSE);
            autoServo2.setDirection(Servo.Direction.FORWARD);

            autoServo1.scaleRange(1-0.66, 1);
            autoServo2.scaleRange(0, 0.66);
        }

        private void distanceSensors(){
            leftDistance = hardwareMap.get(DistanceSensor.class, "leftDistance");
            rightDistance = hardwareMap.get(DistanceSensor.class, "rightDistance");
        }
    }

    private double detectSkystone(){
        List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();

        if(updatedRecognitions != null){
            for(Recognition recognition : updatedRecognitions){
                if(recognition.getLabel().equals(LABEL_SECOND_ELEMENT))
                    return recognition.getRight();
            }
        }
        return -100;
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

            private double overallWheelEnc(){
                double average =
                        frontLeft.getCurrentPosition() + frontRight.getCurrentPosition()
                                + backLeft.getCurrentPosition() + backRight.getCurrentPosition();
                return average/4;
            }
        }

        class Correction{

            double error2 = 0;

            private double wheel(double leftEnc, double rightEnc){

                double kp = 0;
                double ki = 0;

                double error = leftEnc - rightEnc;
                double speedChange = kp*error + ki*(error + error2);

                error2 = error;

                return speedChange;
            }
        }

        private void sideways(double power){
            frontRight.setPower(power);
            frontLeft.setPower(-power);
            backRight.setPower(-power);
            backLeft.setPower(power);
        }

        private void front(double power, long target){

            if(target < 0)
                target *= -1;

            while (((Math.abs(encoders.overallWheelEnc())) <= Math.abs(target) || target == 0) && !isStopRequested()) {

                double rightEnc = frontRight.getCurrentPosition() + backRight.getCurrentPosition();
                double leftEnc = frontLeft.getCurrentPosition() + backLeft.getCurrentPosition();

                double speedChange = correction.wheel(leftEnc, rightEnc);

                frontRight.setPower(power + speedChange);
                frontLeft.setPower(power - speedChange);
                backRight.setPower(power + speedChange);
                backLeft.setPower(power - speedChange);
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
