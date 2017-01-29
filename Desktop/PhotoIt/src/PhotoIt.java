import org.bytedeco.javacv.*;
import org.bytedeco.javacv.FrameGrabber.Exception;
import org.bytedeco.javacv.OpenCVFrameConverter.ToIplImage;

import static org.bytedeco.javacpp.opencv_core.cvFlip;
import static org.bytedeco.javacpp.opencv_imgcodecs.cvSaveImage;

import static org.bytedeco.javacpp.opencv_core.IplImage;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.Image;


public class PhotoIt implements Runnable {

	JFrame mainFrame = new JFrame("PhotoIt!");
	JLabel preview;
	FrameGrabber grabber;
	int width;
	int height;
	JPanel grid;
	JPanel choicePanel;

	boolean isGray, isSepia, isS, isNegative;
	private final JButton btnSepia = new JButton("Sepia");
    public PhotoIt() throws Exception {
		grabber = FrameGrabber.createDefault(0);
    	mainFrame.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
		preview = new JLabel();
		
		grid = new JPanel(new FlowLayout());
		grid.add(preview);

		
        choicePanel = new JPanel();
		mainFrame.getContentPane().setLayout(null);
		
		mainFrame.getContentPane().add(grid);
		
		mainFrame.getContentPane().add(choicePanel);
		
				JButton btnGrayScale = new JButton("Gray scale");
				btnGrayScale.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						isSepia=false;
						isGray=true;
						isNegative=false;
						isS=false;
					}
				});
				
				JButton btnNormal = new JButton("Normal");
				btnNormal.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						isSepia=false;
						isGray=false;
						isNegative=false;
						isS=false;
					}
				});
				
				JButton btnNegative = new JButton("Negative");
				btnNegative.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						isSepia=false;
						isGray=false;
						isNegative=true;
						isS=false;
					}
				});
				
				JButton btnS = new JButton("S function");
				btnS.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						isSepia=false;
						isGray=false;
						isNegative=false;
						isS=true;
					}
				});
				
				choicePanel.add(btnS);
				choicePanel.add(btnNegative);
				choicePanel.add(btnNormal);
				choicePanel.add(btnGrayScale);
				choicePanel.add(btnSepia);
				
				btnSepia.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						isSepia=true;
						isGray=false;
						isNegative=false;
						isS=false;
					}
				});
				
				JButton takePhoto = new JButton("Take Photo");
				choicePanel.add(takePhoto);
				takePhoto.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						
						takePicture=true;
		                

					}
				});
		mainFrame.setVisible(true);
    }

    OpenCVFrameConverter.ToIplImage converter ;
    IplImage img;
    BufferedImage image;
    Frame frame;
    boolean takePicture=false;
    public void run() {
    	
        converter = new OpenCVFrameConverter.ToIplImage();
        try {
            grabber.start();
            width=grabber.getImageWidth();
            height=grabber.getImageHeight();
            mainFrame.setSize(width+150, height+200);
            grid.setBounds(10, 20, width, height);
            choicePanel.setBounds(width+30, 20, 120, height);
            while (true) {
                frame = grabber.grab();
               // System.out.println("Width: "+frame.imageWidth);
                //System.out.println("Height: "+frame.imageHeight);
                
                img = converter.convert(frame);
                
                image = IplImageToBufferedImage(img);
                if (isSepia){
                	image=Filters.sepia(image);
                }
                else if (isGray){
                	image=Filters.gray(image);
                }
                else if (isNegative){
                	image=Filters.negative(image);
                }
                else if (isS){
                	image=Filters.Sfunction(image, 10);
                }          
                
                if (takePicture){
                	
	                Date date = new Date();
	                String fileName = Long.toString(date.getTime())+".png";
	                try {
						ImageIO.write(image,"png",new File(fileName));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

	                takePicture=false;
                }
                ImageIcon icon = new ImageIcon(image); 

                preview.setIcon(icon);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        PhotoIt gs=null;
    	try{
        	gs = new PhotoIt();
        
        } catch (Exception e) {
        	e.printStackTrace();
        }
        Thread th = new Thread(gs);
        th.start();
    }
    
    public static BufferedImage IplImageToBufferedImage(IplImage src) {
        OpenCVFrameConverter.ToIplImage grabberConverter = new OpenCVFrameConverter.ToIplImage();
        Java2DFrameConverter paintConverter = new Java2DFrameConverter();
        Frame frame = grabberConverter.convert(src);
        return paintConverter.getBufferedImage(frame,1);
    }
    
    IplImage toIplImage(BufferedImage bufImage) {

        ToIplImage iplConverter = new OpenCVFrameConverter.ToIplImage();
        Java2DFrameConverter java2dConverter = new Java2DFrameConverter();
        IplImage iplImage = iplConverter.convert(java2dConverter.convert(bufImage));
        return iplImage;
    }
    
    public static BufferedImage toBufferedImage(Image img)
    {
        if (img instanceof BufferedImage)
        {
            return (BufferedImage) img;
        }

        // Create a buffered image with transparency
        BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

        // Draw the image on to the buffered image
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();

        // Return the buffered image
        return bimage;
    }
}
