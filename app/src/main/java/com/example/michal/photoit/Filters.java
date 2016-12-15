import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import ij.ImagePlus;
import ij.plugin.filter.GaussianBlur;
import ij.plugin.filter.UnsharpMask;
import ij.process.FloatProcessor;
import ij.process.ImageProcessor;
import ij.process.ImageStatistics;

public class Filters {
	
    private static GaussianBlur gb;

	/*public static void main(String[] args) throws IOException {
		JFrame frame = new JFrame("Filter test");
		File img = new File("lena.jpg");
		BufferedImage image = ImageIO.read(img);
		image=Sfunction(image, 2);
		JLabel lblimage = new JLabel(new ImageIcon(image));
		JPanel mainPanel = new JPanel(new BorderLayout());

		mainPanel.add(lblimage);
		frame.add(mainPanel);
		JScrollPane jsp = new JScrollPane(mainPanel);

		frame.add(jsp);
		frame.setSize(image.getWidth(), image.getHeight());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}*/

	
	public static BufferedImage sepia(BufferedImage img){
		
		BufferedImage output = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
	    int sepiaDepth = 20;

	    int w = img.getWidth();
	    int h = img.getHeight();

	    int[] pixels = new int[w * h * 3];
	    img.getRaster().getPixels(0, 0, w, h, pixels);

	    for (int x = 0; x < img.getWidth(); x++) {
	        for (int y = 0; y < img.getHeight(); y++) {

	            int rgb = img.getRGB(x, y);
	            Color color = new Color(rgb, true);
	            int r = color.getRed();
	            int g = color.getGreen();
	            int b = color.getBlue();
	            int gry = (r + g + b) / 3;

	            r = g = b = gry;
	            r = r + (sepiaDepth * 2);
	            g = g + sepiaDepth;

	            if (r > 255) {
	                r = 255;
	            }
	            if (g > 255) {
	                g = 255;
	            }
	            if (b > 255) {
	                b = 255;
	            }
	            b -= sepiaDepth;

	            if (b < 0) {
	                b = 0;
	            }
	            if (b > 255) {
	                b = 255;
	            }

	            color = new Color(r, g, b, color.getAlpha());
	            output.setRGB(x, y, color.getRGB());

	        }
	    }
        return output;

	}

	public static BufferedImage gray(BufferedImage img) {
        BufferedImage output = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);

        // Automatic converstion....
        ColorConvertOp op = new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY), null);
        op.filter(img, output);

        return output;
	}

	public static BufferedImage negative(BufferedImage img){
		BufferedImage output = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
		
		for (int i=0; i<img.getWidth(); i++)
			for (int j=0; j<img.getHeight(); j++){
				int value=img.getRGB(i, j);
	            Color color = new Color(value, true);
				
	            int r = 255-color.getRed();
	            int g = 255-color.getGreen();
	            int b = 255-color.getBlue();

				color = new Color(r, g, b, color.getAlpha());
	            output.setRGB(i, j, color.getRGB());
			}
		return output;
		
	}

	public static BufferedImage unsharp(BufferedImage image, double radius)
	{
		float weight= (float) 0.5;
		ImagePlus img = new ImagePlus("",image);
		FloatProcessor fp = img.getProcessor().convertToFloatProcessor();
		fp.snapshot();
		int width=img.getWidth();
		int height=img.getHeight();

		float[] pixel= (float[]) fp.getPixels();
		pixel=sharpenFloat(img.getProcessor().convertToFloatProcessor(), radius, weight, fp.getSnapshotPixels());
		int i=0;
		for (int y=0;y<height; y++)
			for (int x=0 ; x<width; x++,i++)
				img.getProcessor().putPixelValue(x, y, pixel[i]);
		
		
		return image=img.getBufferedImage();
	}
	
	public static BufferedImage blur(BufferedImage img, double radius){
		if (gb == null) gb = new GaussianBlur();
		ImagePlus image = new ImagePlus("",img);
        gb.blurGaussian(image.getProcessor(), radius, radius, 0.02);
        
        return image.getBufferedImage();
	}
	
	public static float[] sharpenFloat(FloatProcessor fp, double radius, float weight, Object snap) {		
		if (gb == null) gb = new GaussianBlur();
        gb.blurGaussian(fp, radius, radius, 0.01);
        if (Thread.currentThread().isInterrupted()) return null;
        float[] pixels = (float[])fp.getPixels();
        float[] pixels2 = new float[pixels.length];
        float[] snapshotPixels = (float[])snap;
        int width = fp.getWidth();
        Rectangle roi = fp.getRoi();
        for (int y=roi.y; y<roi.y+roi.height; y++)
            for (int x=roi.x, p=width*y+x; x<roi.x+roi.width; x++,p++)
                pixels2[p] =(int) (snapshotPixels[p] - weight*pixels[p])/(1f - weight);
        return pixels2;
       
    }

	public static BufferedImage Sfunction(BufferedImage image, double radius){
		ImagePlus imp = new ImagePlus("", image);
		double S[], MAX_PEAK[], U[];
		
        int  LUT_R[], LUT_B[], LUT_G[];
        
        ImagePlus RGB [];
        
        MAX_PEAK = new double[3];
        S = new double[3];
		
		
		
		LUT_R = new int[256];
		LUT_G = new int[256];
		LUT_B = new int[256];
		U = new double[256];
		
		//---------------------------------------------------------------------------------------//
		
       double r=radius;
		
		
        //pobranie rozdzielczosci ekranu
		int y_count=imp.getHeight();
		int x_count=imp.getWidth();
		
        //rozdzielenie ekranu na poszczegolne kanalu
		RGB = ij.plugin.ChannelSplitter.split(imp);
	
        //pobieranie danych z obrazu dla warstwy koloru Blue
		ImageStatistics stat_b = RGB[2].getStatistics(0,256);
		MAX_PEAK[2]=0;
		int max=0;
		//wyliczanie MAX_PEAK z histogramu statystyk obrazu
		for (int i=0; i<stat_b.histogram.length; ++i)
		{
			if (stat_b.histogram[i]>max)
			{
				max=stat_b.histogram[i];
				MAX_PEAK[2]=i;
			}
		}

        //pobieranie danych z obrazu dla warstwy koloru Green
		ImageStatistics stat_g = RGB[1].getStatistics(0,256);
		MAX_PEAK[1]=0;
		max=0;
        //wyliczanie MAX_PEAK z histogramu statystyk obrazu
		for (int i=0; i<stat_g.histogram.length; ++i)
		{
			if (stat_g.histogram[i]>max)
			{
				max=stat_g.histogram[i];
				MAX_PEAK[1]=i;
			}
		}

        //pobieranie danych z obrazu dla warstwy koloru Red
		ImageStatistics stat_r = RGB[0].getStatistics(0,256);
		MAX_PEAK[0]=0;
		max=0;
        //wyliczanie MAX_PEAK z histogramu statystyk obrazu
		for (int i=0; i<stat_r.histogram.length; ++i)
		{
			if (stat_r.histogram[i]>max)
			{
				max=stat_r.histogram[i];
				MAX_PEAK[0]=i;
			}
		}

        //wyliczanie S dla poszczeg—lnego koloru
		S[0]=MAX_PEAK[0]/255;
		S[1]=MAX_PEAK[1]/255;
		S[2]=MAX_PEAK[2]/255;
		
		
	
        //obliczanie tablicy LUT
		for (int i=0; i<256; i++)
		{
			U[i]=(double)i/255;
			
			//RED
			if (U[i]<=S[0])
			{
				LUT_R[i]=(int)(255*(Math.pow(U[i],r)/Math.pow(S[0],r-1)));
			
			}
			else 
			{
				LUT_R[i]=(int)(255*(1-(Math.pow(1-U[i],r)/Math.pow(1-S[0],r-1))));
			}
			
				//GREEN
			if (U[i]<=S[1])
			{
				LUT_G[i]=(int)(255*(Math.pow(U[i],r)/Math.pow(S[1],r-1)));
			}
			else 
			{
				LUT_G[i]=(int)(255*(1-(Math.pow(1-U[i],r)/Math.pow(1-S[1],r-1))));
			}
				
				//BLUE
			if (U[i]<=S[2])
			{
				LUT_B[i]=(int)(255*(Math.pow(U[i],r)/Math.pow(S[2],r-1)));
			}
			else 
			{
				LUT_B[i]=(int)(255*(1-(Math.pow(1-U[i],r)/Math.pow(1-S[2],r-1))));
			}
			
			
		}
	
        //tworzenie obiektu procesora, do ustawienia pikseli
		ImageProcessor proc_r= RGB[0].getProcessor();
		ImageProcessor proc_g= RGB[1].getProcessor();
		ImageProcessor proc_b= RGB[2].getProcessor();
		ImageProcessor proc = imp.getProcessor();
		
		for (int x=0; x<x_count; x++)
			for (int y=0; y<y_count; y++)
			{
				int red=proc_r.getPixel(x,y);
				int green=proc_g.getPixel(x,y);
				int blue=proc_b.getPixel(x,y);
				
				
				int pixel = LUT_R[red]*65536+LUT_G[green]*256+LUT_B[blue];
				
				proc.putPixel(x,y,pixel);
			}
		
		return imp.getBufferedImage();
	}
}
