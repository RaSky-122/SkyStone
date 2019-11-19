package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

@TeleOp(name = "Webcam", group = "wip")

public class Logitech_C270_Webcam extends LinearOpMode {

    private WebcamName webcamName;

    private VuforiaLocalizer vuforia;

    @Override
    public void runOpMode() throws InterruptedException {
        initVuforia();
        VuforiaTrackables skystoneTrackables = this.vuforia.loadTrackablesFromAsset("Skystone");
        VuforiaTrackable skystone = skystoneTrackables.get(4);
        skystone.setName("Sky Stone");

        VuforiaTrackableDefaultListener stoneListener = new VuforiaTrackableDefaultListener();
        stoneListener.addTrackable(skystone);

        telemetry.addData(">", "Press Play to start");
        telemetry.update();
        waitForStart();

        skystoneTrackables.activate();

        while (opModeIsActive()) {

            telemetry.addData("Stone Listener ", stoneListener.isVisible());
            telemetry.update();
        }

        telemetry.update();
    }

    public void initVuforia() {
        webcamName = hardwareMap.get(WebcamName.class, "Webcam 1");
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());

        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);

        parameters.vuforiaLicenseKey = "Ab7Zg4v/////AAABmZKeB/V4p0DYmJFYkJRpaeou+GcJbvvo75+A7Spuy3TFuR7rC0nOwuzdeXJ7XvtxlPAkZYwkdO/EW6CU6mfG8AEqTsAxy//INcvlqdvdnkZeWG3qKsllZorOFWuKfCcrIM3TyA7iOsG2gl0jPG7+PF2S6kpYhHhJ4h1B+9l1B/dx/pQi96ktSn5L8Df3/sAn9WIPlcmRtcByc7N1/cA7liBfwaeiY+HVJZbtuJrxUg+LRJumxU6xjh0Cgq+OcyvXeMXA15i7i/5js/jFa+AqV8jIDNGZpdOyVlgrpSV+/bZsLcnFFY9GSIgYXHbLpFtkv6tQpUd/4kamH3qyxvFRhh5w9uXPVa1Q0xMiLQhUxiuD";
        parameters.cameraName = webcamName;
        parameters.cameraMonitorFeedback = VuforiaLocalizer.Parameters.CameraMonitorFeedback.AXES;

        vuforia = ClassFactory.getInstance().createVuforia(parameters);
    }
}

