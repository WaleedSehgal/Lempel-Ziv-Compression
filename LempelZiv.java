package LempelZiv;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.util.*;

/*This class has method for LempelZiv compression algorithm
 * The output file is named as compressed*/

public class LempelZiv {
	
	 public static void main(String[] args) throws IOException {
	        try {
	        	
	            LempelZiv lz = new LempelZiv();
	            
	            File f=new File("MidFile.txt");
				
	            //Calculates compression time
				 long Start = System.nanoTime();
				 
				      lz.compress(f); 
				      
				 long end = System.nanoTime();     
				
				 long time = (end - Start)/1000;
				 System.out.println("The compression time was: " +time+ " Milliseconds");
				 
				 //Calculates Compression ratio
				 double bytes1 = f.length();
		         double OMegaBytes = (bytes1/1024);
		         
		         File fc = new File("compressed File.txt");
		         
		         double bytes2 = fc.length();
		         double CMegaBytes = (bytes2/1024);
		         
		         double Cratio = CMegaBytes/OMegaBytes;
		      
		         
		         System.out.println("Compression ratio achieved = "+Cratio);
		         
				 
	        } catch (FileNotFoundException e) {
	            System.out.println("File was not found!");
	        }
	    }

   //Hash map to build dictionary
    Map<String, Integer> dict = new HashMap<>();
    
    int DSize = 256;
    
    String str = "";
    
    
    //Function to convert 8 bit to 12 bit for encoding 
    public String ConvET(int i) {
        String str = Integer.toBinaryString(i);
        while (str.length() < 12) {
            str = "0" + str;
        }
        return str;
    }
    
    
    public void compress(File f) throws IOException {
       
    	// build dictionary
        for (int i = 0; i < 256; i++) {
            dict.put(Character.toString((char) i), i);
        }
       

        // Read inputfile and produces a compressed file
        FileReader fr = new FileReader(f);
        RandomAccessFile r = new RandomAccessFile(f, "r");
        BufferedReader BR = new BufferedReader(fr);
        BufferedWriter BW = new BufferedWriter(new FileWriter("Compressed File.txt"));
         
       
        long Start = System.nanoTime();
        
        	try{
        		 
             byte Bytein;
             byte[] buf = new byte[3];
             boolean onleft = true;

        	// read byte input
        	Bytein = r.readByte();
        	
           
        	int i = new Byte(Bytein).intValue();
            if (i < 0) {
                i += 256;
            }
            char ch = (char) i;
            str = "" + ch;

           
            //read each character
            while (true) {
                Bytein = r.readByte();
                i = new Byte(Bytein).intValue();

                if (i < 0) {
                    i += 256;
                }
                System.out.print( i + ", ");
                ch = (char) i;

                //If dictionary contains string+character then set string to string+character
                if (dict.containsKey(str + ch)) {
                    str = str + ch;
                } 
                else {
                	//convert 8 to 12 bits and store it in output
                    String strTwelve = ConvET(dict.get(str));
                 
                    
                    if(onleft) 
                    {
                        buf[0] = (byte) Integer.parseInt(strTwelve.substring(0, 8), 2);
                        buf[1] = (byte) Integer.parseInt(strTwelve.substring(8, 12) + "0000", 2);
                    } 
                    else
                    {
                        buf[1]+= (byte) Integer.parseInt(strTwelve.substring(0, 4), 2);
                        buf[2] = (byte) Integer.parseInt(strTwelve.substring(4, 12), 2);
                        
                        //Write to buffer
                        for (int b = 0; b < buf.length; b++) {
                            BW.write(buf[b]);
                            buf[b] = 0;
                                                             }
                    }
                    
                    onleft = !onleft;

                    // add string and character to dictionary
                    if (DSize < 4096) {
                        dict.put(str + ch, DSize++);
                    }

                    // Set string to character
                    str = "" + ch;
            
                }
                
            }
           
        	}
                
            catch (IOException e) {
              
                r.close();
                BR.close();
                BW.close();
            }
        	long end = System.nanoTime();
        	long time = (end - Start)/1000;
        	System.out.println("Compression Time: " +time+ " Milliseconds");
    }
    }
    
    
    
    

   
