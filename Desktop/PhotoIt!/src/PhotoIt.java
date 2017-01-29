import org.bytedeco.javacv.*;
import org.bytedeco.javacv.FrameGrabber.Exception;

import static org.bytedeco.javacpp.opencv_core.IplImage;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.FlowLayout;


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
		mainFrame.setVisible(true);
    }

    public void run() {
    	
        OpenCVFrameConverter.ToIplImage converter = new OpenCVFrameConverter.ToIplImage();
        IplImage img;
        BufferedImage image;
        try {
            grabber.start();
            width=grabber.getImageWidth();
            height=grabber.getImageHeight();
            mainFrame.setSize(width+150, height+200);
            grid.setBounds(10, 20, width, height);
            choicePanel.setBounds(width+30, 20, 120, height);
            while (true) {
                Frame frame = grabber.grab();
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
}
