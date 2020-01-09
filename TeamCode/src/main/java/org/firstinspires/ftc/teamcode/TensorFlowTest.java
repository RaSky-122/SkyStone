package org.firstinspires.ftc.teamcode;

import android.graphics.Camera;
import android.hardware.camera2.CameraDevice;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.List;

@TeleOp (name = "Webcam TensorFlow", group = "test")

public class TensorFlowTest extends LinearOpMode {

    private DcMotor frontRight;
    private DcMotor frontLeft;
    private DcMotor backRight;
    private DcMotor backLeft;

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

        while(!opModeIsActive() && !isStopRequested()) {
            tfodDetector();
        }

        tfodDetector();

        while(opModeIsActive()){
            while(skyStone1 <= 210 && !isStopRequested()){
                tfodDetector();
                new Movement().sideways(0.5);
            }
            new Movement().stop();
            tfodDetector();

            if(detectSkystone() >= 150 && !isStopRequested()){
                new Movement().sideways(-0.5);
            }
            new Movement().stop();
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
                        skyStone1 = recognition.getRight();
                    else skyStone2 = recognition.getRight();
                }
            }
            if(skyStone1 < skyStone2){
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

        private void sideways(double power){
            frontRight.setPower(power);
            frontLeft.setPower(-power);
            backRight.setPower(-power);
            backLeft.setPower(power);
        }

        private void front(double power){
            frontRight.setPower(power);
            frontLeft.setPower(power);
            backRight.setPower(power);
            backLeft.setPower(power);
        }

        private void stop(){
            frontRight.setPower(0);
            frontLeft.setPower(0);
            backRight.setPower(0);
            backLeft.setPower(0);
        }
    }
}
