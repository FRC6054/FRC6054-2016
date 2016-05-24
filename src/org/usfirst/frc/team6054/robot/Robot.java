
package org.usfirst.frc.team6054.robot;


import edu.wpi.first.wpilibj.CameraServer;											//importing classes
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.vision.USBCamera;
import com.ni.vision.NIVision;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.TalonSRX;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */

public class Robot extends IterativeRobot {                                                      //naming all of the inputs to the computer and the roboRio

	DriverStation station = DriverStation.getInstance();
	
	
	Joystick Left = new Joystick(0);
	Joystick Right = new Joystick(1);
	
	Victor LeftMotor1 = new Victor(0);
	Victor LeftMotor2 = new Victor(1);
	Victor RightMotor1 = new Victor(2);
	Victor RightMotor2 = new Victor(3);
	
	TalonSRX LEDRing = new TalonSRX(7);
	
	VictorSP ballMotor = new VictorSP(4);
	
	VictorSP armMotor = new VictorSP(6);

	Servo cameraservo = new Servo(5);
	
	Ultrasonic FS = new Ultrasonic(0, 1);
	
	CameraServer server;	
	USBCamera camera = new USBCamera ("cam0");
	NIVision.Image frame = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_RGB, 0);
	
//	CameraServer server1;	
//	USBCamera camera1 = new USBCamera ("cam0");
//	NIVision.Image frame1 = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_RGB, 0);
	
	Timer Timer = new Timer(); 

	public void robotInit() {
		
		camera.openCamera();											//initializing the USBCamera
		camera.startCapture();
		camera.getImage(frame);
		CameraServer.getInstance().setImage(frame);
		server = CameraServer.getInstance();
		server.setQuality(50);
		server.startAutomaticCapture(camera);
	    	
			}


	public void disabledInit(){

    }
	
	public void disabledPeriodic() {
	
	}

	public void autonomousInit() {
    	
//		FS.setAutomaticMode(true);
//    	FS.updateTable();
//    	FS.startLiveWindowMode();
		
    	camera.setExposureManual(0);										//setting camera settings for green finder
    	camera.setWhiteBalanceManual(7366);
    	camera.updateSettings();
     	
     	Timer.start();														//starts timer for autonomous wait command

    }

	public void autonomousPeriodic() {
		
    	NetworkTable.setServerMode();										//sets networktables as server to send data
    	NetworkTable datatable = NetworkTable.getTable("datatable");		//naming the COG capture variables from roborealm
		double x = datatable.getNumber("COG_X", 0.0);
    	double y = datatable.getNumber("COG_Y", 0.0);
//    	double sonarTimer = 0.0;
    	double whileTimer = 0.0;
    	double timer1 = 0.0;
    	cameraservo.set(.9);

    	LeftMotor1.set(0.8);
    	LeftMotor2.set(0.8);
    	RightMotor1.set(-0.8);			// get over ramp
    	RightMotor2.set(-0.8);
    	Timer.delay(3);
    	
//    	LeftMotor1.set(0.5);
//    	LeftMotor2.set(0.5);
//    	RightMotor1.set(-0.5);			// get over ramp
//    	RightMotor2.set(-0.5);
//    	Timer.delay(2);
    		
    		
    	LeftMotor1.set(0);
    	LeftMotor2.set(0);
    	RightMotor1.set(0);
    	RightMotor2.set(0);		                           //temp wait
    		
    	LEDRing.set(1);
    	
        Timer.delay(.5);	
    	cameraservo.set(.35);
    	Timer.delay(2);
		
    	LeftMotor1.set(-0.4);
    	LeftMotor2.set(-0.4);
    	RightMotor1.set(-0.4);                                          //going left
    	RightMotor2.set(-0.4);
    	Timer.delay(.5);
    	
//		LeftMotor1.set(0.4);
//    	LeftMotor2.set(0.4);
//    	RightMotor1.set(0);                                          //going right
//    	RightMotor2.set(0);
//    	Timer.delay(1);
    	
    	while (((x>380 || x<340) && x>0) && isAutonomous()) {
    		x = datatable.getNumber("COG_X", 0.0);                             //getting lined up
    		if (x>380) {
    			LeftMotor1.set(0.4);
            	LeftMotor2.set(0.4);
            	RightMotor1.set(0);                                          //going right
            	RightMotor2.set(0);
    		}
    		if (x<340) {
    			LeftMotor1.set(0);
            	LeftMotor2.set(0);
            	RightMotor1.set(-0.4);                                        //going left
            	RightMotor2.set(-0.4);
    		}
    	}
    
    	while (y<450 && isAutonomous() && whileTimer < 5) {
    		x = datatable.getNumber("COG_X", 0.0);                             // towards the goal
    		y = datatable.getNumber("COG_Y", 0.0);
    		if (x<380 && x>340) {
    			LeftMotor1.set(0.4);
            	LeftMotor2.set(0.4);
            	RightMotor1.set(-0.35);                                            // go straight
            	RightMotor2.set(-0.35);
    		}
    		if (x>380) {
    			LeftMotor1.set(0.4);
            	LeftMotor2.set(0.4);
            	RightMotor1.set(-0.2);                                         //if it becomes unaligned, turn right
            	RightMotor2.set(-0.2);
    		}
    		else if (x<340 && x>0) {
    			LeftMotor1.set(0.2);
            	LeftMotor2.set(0.2);                                        //if it becomes unaligned, turn left
            	RightMotor1.set(-0.4);
            	RightMotor2.set(-0.4);
    		}
    		else if (y == 0) {
    			Timer.delay(0.01);                                          //wait
    			whileTimer += 0.01;
    		}
    	}
    	
    	LeftMotor1.set(0);
    	LeftMotor2.set(0);                                       //stop motors
    	RightMotor1.set(0);
    	RightMotor2.set(0);
 
    	while (y > 450) {
    		x = datatable.getNumber("COG_X", 0.0);                             // towards the goal
    		y = datatable.getNumber("COG_Y", 0.0);
    		if (x<380 && x>340) {
    			LeftMotor1.set(0);
            	LeftMotor2.set(0);
            	RightMotor1.set(0);                                            // go straight
            	RightMotor2.set(0);
    		}
    		if (x>380) {
    			LeftMotor1.set(0.4);
            	LeftMotor2.set(0.4);
            	RightMotor1.set(0);                                         //if it becomes unaligned, turn right
            	RightMotor2.set(0);
    		}
    		else if (x<340 && x>0) {
    			LeftMotor1.set(0);
            	LeftMotor2.set(0);                                        //if it becomes unaligned, turn left
            	RightMotor1.set(-0.4);
            	RightMotor2.set(-0.4);
    		}
    		else {
    			timer1 += 0.01;
    			Timer.delay(.01);
    		
    		}
    	}
    	
    	LeftMotor1.set(0);
    	LeftMotor2.set(0);                                       //stop motors
    	RightMotor1.set(0);
    	RightMotor2.set(0);
    	
    	ballMotor.set(1);
    	Timer.delay(5);
    
    	ballMotor.set(0);
    	
    	while (isAutonomous()) {
    		Timer.delay(0.1);
    	}
    	
    	LEDRing.set(0);
    	
    }
    		   	



	public void teleopInit() {

		camera.setBrightness(50);										//sets new camera settings for comfortable driving
		camera.setWhiteBalanceAuto();
		camera.setExposureAuto();
		camera.updateSettings();

	}

    
	public void teleopPeriodic() {									//sets variables on the joysticks for controlling the robot
    	double Leftaxis = Left.getRawAxis(0);                   
    	double Lefttrigger = Left.getRawAxis(2);
    	double Rightspeed = Left.getRawAxis(3);
    	double Leftspeed = Left.getRawAxis(3);
    	double RightspeedReverse = Left.getRawAxis(2);        
    	double LeftspeedReverse = Left.getRawAxis(2);
    	boolean rbON = true;
    	boolean bbON = true;
    	boolean abON = true;
    	boolean xbON = true;
    	boolean sucky = true;
    	boolean pew = true;
    	boolean estop = true;
    	double armspeed = (Right.getRawAxis(1) * -1);
    	boolean port = true;
    	boolean cameraset = true;
    	boolean cameraset2 = true;
    	boolean cameraon = true;
    			
//    	if (cameraon = Right.getRawButton(7)) {
//    	
//    		camera1.openCamera();											//initializing the USBCamera
//    		camera1.startCapture();
//    		camera1.getImage(frame1);
//    		CameraServer.getInstance().setImage(frame1);
//    		server1 = CameraServer.getInstance();
//    		server1.setQuality(50);
//    		server1.startAutomaticCapture(camera1);
//    		camera1.setBrightness(50);										//sets new camera settings for comfortable driving
//    		camera1.setWhiteBalanceAuto();
//    		camera1.setExposureAuto();
//    		camera1.updateSettings();
//    	
//    	}
		
    	if (rbON = Left.getRawButton(5)) {
    		cameraservo.set(.35);
    	}
    	
    	if (cameraset = Right.getRawButton(11)) {
    		camera.setExposureManual(0);										//setting camera settings for green finder
        	camera.setWhiteBalanceManual(7366);
        	camera.updateSettings();
    	}
    	
    	if (cameraset2 = Right.getRawButton(12)) {
    		camera.setBrightness(50);										//sets new camera settings for comfortable driving
    		camera.setWhiteBalanceAuto();
    		camera.setExposureAuto();
    		camera.updateSettings();
    	}
    	
    	if (port = Left.getRawButton(8)) {
    		armMotor.set(-1);
    		
    		LeftMotor1.set(-.5);												//sets motors for forward driving
        	LeftMotor2.set(-.5);                  
        	
    	    RightMotor1.set(.5);
        	RightMotor2.set(.5);  
        	Timer.delay(.25);
    		
    		LeftMotor1.set(.5);												//sets motors for forward driving
        	LeftMotor2.set(.5);                  
        	
    	    RightMotor1.set(-.5);
        	RightMotor2.set(-.5);  
        	Timer.delay(.25);
        	
        	LeftMotor1.set(-.5);												//sets motors for forward driving
        	LeftMotor2.set(-.5);                  
        	
    	    RightMotor1.set(.5);
        	RightMotor2.set(.5);  
        	Timer.delay(.25);
        	
        	LeftMotor1.set(.5);												//sets motors for forward driving
        	LeftMotor2.set(.5);                  
        	
    	    RightMotor1.set(-.5);
        	RightMotor2.set(-.5);  
        	Timer.delay(.25);
        	
        	LeftMotor1.set(-.5);												//sets motors for forward driving
        	LeftMotor2.set(-.5);                  
        	
    	    RightMotor1.set(.5);
        	RightMotor2.set(.5);  
        	Timer.delay(1);

    	}
    	
    	if (armspeed < 0.2 && armspeed > -0.2) {
    		armMotor.set(0);
    	}
    	
    	if (armspeed > 0.20 || armspeed < -0.20) {
    		armMotor.set(armspeed);
    	}
    	
    	if (pew = Right.getRawButton(1)) {
    		ballMotor.set(1);
    	}
	
    	if (sucky = Right.getRawButton(2)) {
    		ballMotor.set(-1);
    	}
    	
    	if (estop = Right.getRawButton(4)) {
    		ballMotor.set(0);
    	}
    	
    	while (Left.getRawAxis(4) > 0.2 || Left.getRawAxis(4) < -0.2)  {					//zeroturn command
    		LeftMotor1.set((Left.getRawAxis(4)));
        	LeftMotor2.set((Left.getRawAxis(4)));
        	                                                    
    		RightMotor1.set((Left.getRawAxis(4)));
        	RightMotor2.set((Left.getRawAxis(4)));
        	
        	armspeed = (Right.getRawAxis(1) * -1);
        	
        	if (armspeed < 0.2 && armspeed > -0.2) {
        		armMotor.set(0);
        	}
        	
        	if (armspeed > 0.20 || armspeed < -0.20) {
        		armMotor.set(armspeed);
        	}
    	}
    	
    	if (Leftaxis > 0.1) {																//forward turning
    		Leftaxis *= -1;                     
    		Rightspeed *= Leftaxis + 1;
    	}
    	
    	else if (Leftaxis < -0.1) {
    		Leftspeed *= Leftaxis + 1;          
    	}
    	
    	while (Lefttrigger > 0.1) {												//reverse command
    		Lefttrigger = Left.getRawAxis(2);
    		Leftaxis = Left.getRawAxis(0);
    		RightspeedReverse = Left.getRawAxis(2);        
        	LeftspeedReverse = Left.getRawAxis(2);
        	armspeed = (Right.getRawAxis(1) * -1);
        	
        	if (rbON = Left.getRawButton(6)) {										//setting camera servo for left and right
        		cameraservo.set(1);
        	}
        
        	if (bbON = Left.getRawButton(2)) {
        		cameraservo.set(0.55);
        	}
        	
        	if (port = Left.getRawButton(8)) {
        		armMotor.set(-1);
        		
        		LeftMotor1.set(-.5);												//sets motors for forward driving
            	LeftMotor2.set(-.5);                  
            	
        	    RightMotor1.set(.5);
            	RightMotor2.set(.5);  
            	Timer.delay(.25);
        		
        		LeftMotor1.set(.5);												//sets motors for forward driving
            	LeftMotor2.set(.5);                  
            	
        	    RightMotor1.set(-.5);
            	RightMotor2.set(-.5);  
            	Timer.delay(.1);
            	
            	LeftMotor1.set(-.5);												//sets motors for forward driving
            	LeftMotor2.set(-.5);                  
            	
        	    RightMotor1.set(.5);
            	RightMotor2.set(.5);  
            	Timer.delay(.25);
            	
            	LeftMotor1.set(.5);												//sets motors for forward driving
            	LeftMotor2.set(.5);                  
            	
        	    RightMotor1.set(-.5);
            	RightMotor2.set(-.5);  
            	Timer.delay(.1);
            	
            	LeftMotor1.set(-.6);												//sets motors for forward driving
            	LeftMotor2.set(-.6);                  
            	
        	    RightMotor1.set(.6);
            	RightMotor2.set(.6);  
            	Timer.delay(2);

        	}
        	
        	
        	if (armspeed < 0.2 && armspeed > -0.2) {
        		armMotor.set(0);
        	}
        	
        	if (armspeed > 0.20 || armspeed < -0.20) {
        		armMotor.set(armspeed);
        	}
        	
    		if (Leftaxis > 0.1) {   											//reverse turning
    			Leftaxis *= -1; 
    			RightspeedReverse *= Leftaxis + 1;
    	}
    	
    		else if (Leftaxis < -0.1) {
    			LeftspeedReverse *= Leftaxis + 1;          
    	}
    		LeftMotor1.set(((LeftspeedReverse) * -1));							//travelling in reverse with respect to the left trigger
    		LeftMotor2.set(((LeftspeedReverse) * -1));
    		
    		RightMotor1.set((RightspeedReverse));
    		RightMotor2.set((RightspeedReverse));
    	}
	
    	LeftMotor1.set(Leftspeed);												//sets motors for forward driving
    	LeftMotor2.set(Leftspeed);                  
    	
	    RightMotor1.set(Rightspeed * -1);
    	RightMotor2.set(Rightspeed * -1);    
 
    	if (rbON = Left.getRawButton(6)) {										//setting camera servo for left and right
    		cameraservo.set(1);
    	}
    	
    	if (bbON = Left.getRawButton(2)) {
    		cameraservo.set(0.55);
    	}
    	
    	if (Left.getRawAxis(3) < 0.1 && Left.getRawAxis(2) < 0.1) {  			//turning while coasting
    		if (Left.getRawAxis(0) > 0.1){
    			LeftMotor1.set(Left.getRawAxis(0)/2);	
    			LeftMotor2.set(Left.getRawAxis(0)/2);								
    		}
        	if (Left.getRawAxis(0) < -0.1) {
        		RightMotor1.set(Left.getRawAxis(0)/2);	
    			RightMotor2.set(Left.getRawAxis(0)/2);
        	}
    	}
    	
    	if (abON = Left.getRawButton(1)) {
    		LEDRing.set(1);
    	}
    	
    	if (xbON = Left.getRawButton(3)) {
    		LEDRing.set(0);
    	}
    	
    }
   

	public void testPeriodic() {
		
		camera.setExposureManual(0);										//setting camera settings for green finder
    	camera.setWhiteBalanceManual(7366);
    	camera.updateSettings();
		
		LEDRing.set(1);
		
		ballMotor.set(Right.getRawAxis(1));
	}	
}	
	