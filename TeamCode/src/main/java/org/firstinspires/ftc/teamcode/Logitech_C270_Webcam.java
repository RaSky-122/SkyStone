package org.firstinspires.ftc.teamcode;

import android.hardware.camera2.CameraDevice;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.Camera;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

import java.util.ArrayList;
import java.util.List;

import static org.firstinspires.ftc.robotcore.external.navigation.AngleUnit.DEGREES;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.XYZ;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.YZX;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesReference.EXTRINSIC;
import static org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection.BACK;

@TeleOp(group = "test", name = "Yeeeee")

public class Logitech_C270_Webcam extends LinearOpMode {

    private DcMotor frontRightMotor;
    private DcMotor frontLeftMotor;
    private DcMotor backRightMotor;
    private DcMotor backLeftMotor;

    private static final double ticksPerCM = 35.1;

    private static final VuforiaLocalizer.CameraDirection CAMERA_CHOICE = BACK;
    private static final boolean PHONE_IS_PORTRAIT = false;

    private static final String VUFORIA_KEY = "Ab7Zg4v/////AAABmZKeB/V4p0DYmJFYkJRpaeou+GcJbvvo75+A7Spuy3TFuR7rC0nOwuzdeXJ7XvtxlPAkZYwkdO/EW6CU6mfG8AEqTsAxy//INcvlqdvdnkZeWG3qKsllZorOFWuKfCcrIM3TyA7iOsG2gl0jPG7+PF2S6kpYhHhJ4h1B+9l1B/dx/pQi96ktSn5L8Df3/sAn9WIPlcmRtcByc7N1/cA7liBfwaeiY+HVJZbtuJrxUg+LRJumxU6xjh0Cgq+OcyvXeMXA15i7i/5js/jFa+AqV8jIDNGZpdOyVlgrpSV+/bZsLcnFFY9GSIgYXHbLpFtkv6tQpUd/4kamH3qyxvFRhh5w9uXPVa1Q0xMiLQhUxiuD";

    private static final float mmPerInch = 25.4f;
    private static final float mmTargetHeight = (6) * mmPerInch;

    private static final float stoneZ = 2.00f * mmPerInch;

    private static final float bridgeZ = 6.42f * mmPerInch;
    private static final float bridgeY = 23 * mmPerInch;
    private static final float bridgeX = 5.18f * mmPerInch;
    private static final float bridgeRotY = 59;
    private static final float bridgeRotZ = 180;

    private static final float halfField = 72 * mmPerInch;
    private static final float quadField = 36 * mmPerInch;

    private OpenGLMatrix lastLocation = null;
    private VuforiaLocalizer vuforia = null;

    WebcamName webcamName = null;

    private boolean targetVisible = false;
    private float phoneXRotate = 0;
    private float phoneYRotate = 0;
    private float phoneZRotate = 0;

    @Override
    public void runOpMode() throws InterruptedException {

        frontRightMotor = hardwareMap.dcMotor.get("frontRight");
        frontLeftMotor = hardwareMap.dcMotor.get("frontLeft");
        backLeftMotor = hardwareMap.dcMotor.get("backLeft");
        backRightMotor = hardwareMap.dcMotor.get("backRight");

        frontLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        backRightMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        backLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        frontRightMotor.setDirection(DcMotorSimple.Direction.FORWARD);

        frontRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

       // frontRightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
       // frontLeftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
       // backRightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
       // backLeftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

       // frontRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
       // frontLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
       // backRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
       // backLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        webcamName = hardwareMap.get(WebcamName.class, "Webcam 1");

        int cameraMonitorViewID = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewID);

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        //parameters.cameraDirection = CAMERA_CHOICE;
        parameters.cameraName = webcamName;

        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        VuforiaTrackables targetsSkyStone = this.vuforia.loadTrackablesFromAsset("Skystone");

        VuforiaTrackable stoneTarget = targetsSkyStone.get(0);
        stoneTarget.setName("Stone Target");
        //VuforiaTrackable.Listener stoneListener;
        //stoneTarget.setListener(stoneListener);

        VuforiaTrackable blueRearBridge = targetsSkyStone.get(1);
        blueRearBridge.setName("Blue Rear Bridge");
        VuforiaTrackable redRearBridge = targetsSkyStone.get(2);
        redRearBridge.setName("Red Rear Bridge");
        VuforiaTrackable redFrontBridge = targetsSkyStone.get(3);
        redFrontBridge.setName("Red Front Bridge");
        VuforiaTrackable blueFrontBridge = targetsSkyStone.get(4);
        blueFrontBridge.setName("Blue Front Bridge");
        VuforiaTrackable red1 = targetsSkyStone.get(5);
        red1.setName("Red Perimeter 1");
        VuforiaTrackable red2 = targetsSkyStone.get(6);
        red2.setName("Red Perimeter 2");
        VuforiaTrackable front1 = targetsSkyStone.get(7);
        front1.setName("Front Perimeter 1");
        VuforiaTrackable front2 = targetsSkyStone.get(8);
        front2.setName("Front Perimeter 2");
        VuforiaTrackable blue1 = targetsSkyStone.get(9);
        blue1.setName("Blue Perimeter 1");
        VuforiaTrackable blue2 = targetsSkyStone.get(10);
        blue2.setName("Blue Perimeter 2");
        VuforiaTrackable rear1 = targetsSkyStone.get(11);
        rear1.setName("Rear Perimeter 1");
        VuforiaTrackable rear2 = targetsSkyStone.get(12);
        rear2.setName("Rear Perimeter 2");

        List<VuforiaTrackable> allTrackables = new ArrayList<VuforiaTrackable>();
        allTrackables.addAll(targetsSkyStone);

        stoneTarget.setLocation(OpenGLMatrix
                .translation(0, 0, stoneZ)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0, -90)));

        //Set the position of the bridge support targets with relation to origin (center of field)
        blueFrontBridge.setLocation(OpenGLMatrix
                .translation(-bridgeX, bridgeY, bridgeZ)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 0, bridgeRotY, bridgeRotZ)));

        blueRearBridge.setLocation(OpenGLMatrix
                .translation(-bridgeX, bridgeY, bridgeZ)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 0, -bridgeRotY, bridgeRotZ)));

        redFrontBridge.setLocation(OpenGLMatrix
                .translation(-bridgeX, -bridgeY, bridgeZ)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 0, -bridgeRotY, 0)));

        redRearBridge.setLocation(OpenGLMatrix
                .translation(bridgeX, -bridgeY, bridgeZ)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 0, bridgeRotY, 0)));

        //Set the position of the perimeter targets with relation to origin (center of field)
        red1.setLocation(OpenGLMatrix
                .translation(quadField, -halfField, mmTargetHeight)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0, 180)));

        red2.setLocation(OpenGLMatrix
                .translation(-quadField, -halfField, mmTargetHeight)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0, 180)));

        front1.setLocation(OpenGLMatrix
                .translation(-halfField, -quadField, mmTargetHeight)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0 , 90)));

        front2.setLocation(OpenGLMatrix
                .translation(-halfField, quadField, mmTargetHeight)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0, 90)));

        blue1.setLocation(OpenGLMatrix
                .translation(-quadField, halfField, mmTargetHeight)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0, 0)));

        blue2.setLocation(OpenGLMatrix
                .translation(quadField, halfField, mmTargetHeight)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0, 0)));

        rear1.setLocation(OpenGLMatrix
                .translation(halfField, quadField, mmTargetHeight)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0 , -90)));

        rear2.setLocation(OpenGLMatrix
                .translation(halfField, -quadField, mmTargetHeight)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0, -90)));

        if (CAMERA_CHOICE == BACK)
            phoneYRotate = -90;
        else phoneYRotate = 90;

        /**Galbena gutuie
         *
         * Refren:
         * Galbena gutuie dulce, amaruie
         * Lampa la fereastra toata viata noastra
         * Galbena gutuie dulce, amaruie
         * Lampa la fereastra toata viatza noastra
         *
         * Cand eram copil acasa
         * Si craciunu'l asteptam
         * Era bucuria mare
         * Noi copii ne adunam x2
         *
         * Refren:
         * Galbena gutuie dulce, amaruie
         * Lampa la fereastra toata viata noastra
         * Galbena gutuie dulce, amaruie
         * Lampa la fereastra toata viatza noastra
         *
         * Mi'a pus mama la fereastra
         * In odaia de la drum
         * O gutuie'ngalbenita
         * https://Versuri.ro/w/0no1
         * S'o pastraz pentru Ajun x2
         *
         * Refren:
         * Galbena gutuie dulce, amaruie
         * Lampa la fereastra toata viata noastra
         * Galbena gutuie dulce, amaruie
         * Lampa la fereastra toata viatza noastra
         *
         * Luxul mamei cel mai mare
         * Cand copii ne adunam
         * Era painea de pe masa
         * Si gutuia de la geam x2
         *
         * Refren:
         * Galbena gutuie dulce, amaruie
         * Lampa la fereastra toata viata noastra
         * Galbena gutuie dulce, amaruie
         * Lampa la fereastra toata viatza noastra
         * */

        if(PHONE_IS_PORTRAIT)
            phoneXRotate = 90;

        final float CAMERA_FORWARD_DISPLACEMENT = 4.0f * mmPerInch;
        final float CAMERA_VERTICAL_DISPLACEMENT = 8.0f * mmPerInch;
        final float CAMERA_LEFT_DISPLACEMENT = 0;

        OpenGLMatrix robotFromCamera = OpenGLMatrix.
                translation(CAMERA_FORWARD_DISPLACEMENT, CAMERA_LEFT_DISPLACEMENT, CAMERA_VERTICAL_DISPLACEMENT)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, YZX, DEGREES, phoneYRotate, phoneZRotate, phoneXRotate));

        for(VuforiaTrackable trackable : allTrackables){
            ((VuforiaTrackableDefaultListener)trackable.getListener()).setPhoneInformation(robotFromCamera, parameters.cameraDirection);
        }

        targetsSkyStone.activate();

        int i=0,j=0;



        while (!opModeIsActive() && !isStopRequested()){
            telemetry.addData("Waiting for ", "start.");
            cameraDetection(allTrackables);
        }

        while(!isStopRequested()){

            while(!targetVisible){
                new Movement().horizontal(-0.3);
                cameraDetection(allTrackables);
            }
            new Movement().stop();
            if(i==1) {
                cameraDetection(allTrackables);
                sleep(300);
                if (lastLocation.getTranslation().get(1) >= -2.3)
                    new Movement().horizontal(0.25);
                else
                    new Movement().stop();
            }
        }
    }

    class Movement{

        public void stop(){
            frontLeftMotor.setPower(0);
            frontRightMotor.setPower(0);
            backLeftMotor.setPower(0);
            backRightMotor.setPower(0);
        }
        public void horizontal(double power){
            frontLeftMotor.setPower(power);
            frontRightMotor.setPower(-power);
            backLeftMotor.setPower(-power);
            backRightMotor.setPower(power);
        }
    }

    public void cameraDetection(List<VuforiaTrackable> allTrackables){
        targetVisible = false;

        if(((VuforiaTrackableDefaultListener)allTrackables.get(0).getListener()).isVisible()) {
            //telemetry.addData("Visible Target", allTrackables.get(0).getName());
            targetVisible = true;

            OpenGLMatrix robotLocationTransform = ((VuforiaTrackableDefaultListener) allTrackables.get(0).getListener()).getUpdatedRobotLocation();
            if (robotLocationTransform != null) {
                lastLocation = robotLocationTransform;
            }
        }

       // Provide feedback as to where the robot is located (if we know).
        if (targetVisible) {
            // express position (translation) of robot in inches.
            VectorF translation = lastLocation.getTranslation();

            telemetry.addData("Pos (in)", "{X, Y, Z} = %.1f, %.1f, %.1f",
                    translation.get(0) / mmPerInch, translation.get(1) / mmPerInch, translation.get(2) / mmPerInch);

            // express the rotation of the robot in degrees.
            Orientation rotation = Orientation.getOrientation(lastLocation, EXTRINSIC, XYZ, DEGREES);
            telemetry.addData("Rot (deg)", "{Roll, Pitch, Heading} = %.0f, %.0f, %.0f", rotation.firstAngle, rotation.secondAngle, rotation.thirdAngle);
            telemetry.addData("do you see? ", targetVisible );
        }

        telemetry.update();
    }
}
