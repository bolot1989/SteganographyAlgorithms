import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.*;
import java.math.*;
import java.util.*;
import javax.imageio.*;
import java.security.MessageDigest;
import java.util.Arrays;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.IvParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import java.*;

import java.security.Key;


public class ImageCypher {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		
		String fileName;// = sc.nextLine();
		
		int choice = -1;
		boolean encrypt = false;
		String strChoice = "";
		
		System.out.println("Choose an encrypting algorithm: ");
		System.out.println("1 - RSA");
		System.out.println("2 - AES");
		System.out.println("3 - DES");
			
		int ch = sc.nextInt();
		
		System.out.println("What do you want to do?");
		System.out.println("1. Cypher file");
		System.out.println("2. Decypher file");
		System.out.println("0. Exit");
		
		while ((choice < 0 || choice > 2)) {
			
			strChoice = sc.nextLine();
			
			if (strChoice.length() == 0) {			
				strChoice = "a";
			}
			
			choice = Character.getNumericValue(strChoice.charAt(0));

			
			if (strChoice.length() > 1 || strChoice.length() <= 0) {
				
				choice = -1;
			}
			
			//System.out.println(choice);
			
		}
		
		if (choice == 0) {
			
			return;
		}
		else if (choice == 1)  {
			encrypt = true;
		}
		else {
			encrypt = false;
		}
		
		
		//int pixel = 0;
		int newpixel = 0;
		int r1 = 0;
		int g1 = 0;
		int b1 = 0;
		
		String r1Binary = "";
		String g1Binary = "";
		String b1Binary = "";
		
		String newR1Binary = "";
		String newG1Binary = "";
		String newB1Binary = "";
		
		String fullBinaryString = "";
		int currentElement = 0;
		
		int currentX = 0;
		int currentY = 0;
		
		int iWidth = 0;
		int iHeight = 0;
		
		int keyIndex = 0;
		String key = "";
		String normalKey = "";
		
		if (encrypt == true) {
			
			System.out.println("Please, enter input image name.");
			fileName = sc.nextLine();//"input.jpg";
			File inputFile = new File(fileName);
			
			
			while (key.length() < 3 || key.length() > 50) {
				System.out.println("Please, enter key for encryption. (It must be 4 characters long): ");
				key = sc.nextLine();
				
				if (key.length() < 3) {
					System.out.println("Your key is too short.");
					System.out.println("It should be not less than 3 symbols");
				}
				else if (key.length() > 50) {
					System.out.println("Your key is too long.");
					System.out.println("It should be not more than 8 symbols");
				}
			}
			
			for (int i = 0; i < key.length(); i++) {
				
				//System.out.println((int) key.charAt(i));
				
				if ((int) key.charAt(i) == 49 ||
					(int) key.charAt(i) == 52 ||
					(int) key.charAt(i) == 55) {
					 
					normalKey += "1";
					
					
				}
				else if ((int) key.charAt(i) == 50 ||
						(int) key.charAt(i) == 53 ||
						(int) key.charAt(i) == 56) {
					
					normalKey += "2";
				}
				else if ((int) key.charAt(i) == 51 ||
						(int) key.charAt(i) == 54 ||
						(int) key.charAt(i) == 57) {
					
					normalKey += "3";
				}
				else if ((int) key.charAt(i) % 2 == 0 ) {
					
					normalKey += "1";
				}
				else if ((int) key.charAt(i) % 3 == 0 ) {
					
					normalKey += "2";
				}
				else {
					
					normalKey += "3";
				}
				
			}
			
			try {
				

				// *** DIVIDING FILE INTO BIT SEGMENTS ***
				System.out.println("Please, enter name of file which you want to cypher.");	
				fileName = sc.nextLine(); //"cypher.jpg";
				File cypherFile = new File(fileName);
				
				String filePath = "C:\\Users\\Bolot\\Documents\\RSA\\src\\"+fileName;
				
				StringBuffer fileData = new StringBuffer(10000000);
				BufferedReader reader = new BufferedReader(new FileReader(filePath));
				char[] buf = new char[1024];

				int numRead=0;
				while((numRead=reader.read(buf)) != -1){
					String readData = String.valueOf(buf, 0, numRead);
					fileData.append(readData);
					buf = new char[1024];
				}

				reader.close();
								
				FileInputStream cypherFileStream = new FileInputStream(cypherFile);
								
				BufferedImage inputImage = ImageIO.read(inputFile);
				
				iWidth = inputImage.getWidth();
				iHeight = inputImage.getHeight();
				
				System.out.println("Input file width: " + iWidth);
				System.out.println("Input file height: " + iHeight);
				System.out.println("Size of file to cypher: " + cypherFile.length() + " bytes");
				
				double availableSize = (((iWidth * iHeight)/ (double) 2) - 2);
								
				int availableSizeInt = (int) availableSize;
								
				String availableSizeBits = Integer.toBinaryString((int) cypherFile.length());
				
				while (availableSizeBits.length() < 32) {
					
					availableSizeBits = "0" + availableSizeBits;
				}
				
				System.out.println(availableSizeInt + " bytes available");
				
				if (cypherFile.length() > availableSizeInt) {
					System.out.println("File is too big!");
					return;
				}
								
				String byteString = "";		
				String[] byteStringArray = new String[10000000];		
				int indexOfElement = 0;
				
				
				for (int i = 0; i < 32; i += 2) {		
					byteStringArray[indexOfElement] = availableSizeBits.substring(i, i+2);		
					indexOfElement++;		
				}	
				
			
				byte[] buffer = new byte[1];
				int bufsize = 0;
				long tStart = 0, tEnd = 0;
				if(ch == 1)
				{
					tStart = System.currentTimeMillis();
					while ((bufsize = cypherFileStream.read(buffer)) != -1) {
						int temp = (int) buffer[0]& 0xff;
						
						byteString = Integer.toBinaryString(temp);
						
						while (byteString.length() < 4) {			
							byteString = "0" + byteString;
						}
						
						for (int i = 0; i < 4; i += 2) {		
							byteStringArray[indexOfElement] = byteString.substring(i, i+2);		
							indexOfElement++;		
						}
						
					}
					tEnd = System.currentTimeMillis();
				}
				else if(ch == 2) {
	
					String finalKey = "";
					for(int i = 0; i < 4; i++) {
						finalKey += normalKey;
					}
					
					Key aesKey = new SecretKeySpec(finalKey.getBytes("UTF-8"), "AES");
					Cipher cipher = Cipher.getInstance("AES");

					cipher.init(Cipher.ENCRYPT_MODE, aesKey);
					byte[] encrypted = cipher.doFinal(fileData.toString().getBytes());
					
					tStart = System.currentTimeMillis();
					for(int i = 0; i < (int) cypherFile.length(); i++) {
						byteString = Integer.toBinaryString(encrypted[i]);
						
						while (byteString.length() < 4) {			
							byteString = "0" + byteString;
						}
						
						 for (int j = 0; j < 4; j += 2) {		
							byteStringArray[indexOfElement] = byteString.substring(j, j+2);		
							indexOfElement++;		
						}
					}
					tEnd = System.currentTimeMillis();
				
				}
				else if(ch == 3) {
					String finalKey = "";
					finalKey += normalKey + normalKey;
						
					Key aesKey = new SecretKeySpec(finalKey.getBytes("UTF-8"), "DES");
					Cipher cipher = Cipher.getInstance("DES");

					cipher.init(Cipher.ENCRYPT_MODE, aesKey);
					byte[] encrypted = cipher.doFinal(fileData.toString().getBytes());
					
					tStart = System.currentTimeMillis();
					for(int i = 0; i < (int) cypherFile.length(); i++) {
						byteString = Integer.toBinaryString(encrypted[i]);
						
						while (byteString.length() < 4) {			
							byteString = "0" + byteString;
						}
						
						 for (int j = 0; j < 4; j += 2) {		
							byteStringArray[indexOfElement] = byteString.substring(j, j+2);		
							indexOfElement++;		
						}
					}
					tEnd = System.currentTimeMillis();
				}
				
				while (currentElement < indexOfElement) {
					
					newR1Binary = "";
					newG1Binary = "";
					newB1Binary = "";
	
					Color originalColor = new Color(inputImage.getRGB(currentX, currentY));	
					r1 = originalColor.getRed();
					g1 = originalColor.getGreen();
					b1 = originalColor.getBlue();
					
					r1Binary = Integer.toBinaryString(r1);
					g1Binary = Integer.toBinaryString(g1);
					b1Binary = Integer.toBinaryString(b1);
					
					while (r1Binary.length() < 8 || g1Binary.length() < 8 || b1Binary.length() < 8) {
						
						if (r1Binary.length() < 8) {
							r1Binary = "0" + r1Binary;
						}
						
						if (g1Binary.length() < 8) {
							g1Binary = "0" + g1Binary;
						}
						
						if (b1Binary.length() < 8) {
							b1Binary = "0" + b1Binary;
						}
					}
					
					for (int i = 0; i < 6; i++) {
						
						newR1Binary += r1Binary.charAt(i);
						newG1Binary += g1Binary.charAt(i);
						newB1Binary += b1Binary.charAt(i);
					}
					
					
					newR1Binary += byteStringArray[currentElement].charAt(0);
					newR1Binary += byteStringArray[currentElement].charAt(1);
					
					newG1Binary += byteStringArray[currentElement].charAt(0);
					newG1Binary += byteStringArray[currentElement].charAt(1);
					
					newB1Binary += byteStringArray[currentElement].charAt(0);
					newB1Binary += byteStringArray[currentElement].charAt(1);
				
					
					if (normalKey.charAt(keyIndex) == '1') {
						fullBinaryString = "11111111" + newR1Binary + g1Binary + b1Binary;
					}
					else if (normalKey.charAt(keyIndex) == '2') {
						fullBinaryString = "11111111" + r1Binary + newG1Binary + b1Binary;
					}
					else {
						fullBinaryString = "11111111" + r1Binary + g1Binary + newB1Binary;
					}
					
					keyIndex++;
					
					if (keyIndex == normalKey.length()) {
						keyIndex = 0;
					}

					newpixel = new BigInteger(fullBinaryString, 2).intValue();
					
					inputImage.setRGB(currentX, currentY, newpixel);
					
					
					//Next element
					currentElement++;
					
					//Next pixel
					if (currentX < iWidth - 1) {
						
						currentX++;
					}
					else {
						
						currentX = 0;
						currentY++;
					}
				}
				
				System.out.println("Please, enter name of the output file.");
				System.out.println("(it's extension should be .png)");
				fileName = sc.nextLine(); //"output.png"
				
				ImageIO.write(inputImage, "png", new File(fileName));
				
				System.out.println("File is cyphered.");
				long tDelta = tEnd - tStart;
				double elapsedSeconds = tDelta / 1000.0;
				System.out.println("Elapsed seconds: " + elapsedSeconds);

				
			}
			catch (Exception e) {
				System.out.println("Error of reading file." + e);
			}
		
		
		}
		else {
			
			while (key.length() < 3 || key.length() > 8) {
				System.out.println("Please, enter key for decryption.");
				key = sc.nextLine();
				
				if (key.length() < 3) {
					System.out.println("Your key is too short.");
					System.out.println("It should be not less than 3 symbols");
				}
				else if (key.length() > 8) {
					System.out.println("Your key is too long.");
					System.out.println("It should be not more than 8 symbols");
				}
			}
			
			for (int i = 0; i < key.length(); i++) {
				
				//System.out.println((int) key.charAt(i));
				
				if ((int) key.charAt(i) == 49 ||
					(int) key.charAt(i) == 52 ||
					(int) key.charAt(i) == 55) {
					 
					normalKey += "1";
					
					
				}
				else if ((int) key.charAt(i) == 50 ||
						(int) key.charAt(i) == 53 ||
						(int) key.charAt(i) == 56) {
					
					normalKey += "2";
				}
				else if ((int) key.charAt(i) == 51 ||
						(int) key.charAt(i) == 54 ||
						(int) key.charAt(i) == 57) {
					
					normalKey += "3";
				}
				else if ((int) key.charAt(i) % 2 == 0 ) {
					
					normalKey += "1";
				}
				else if ((int) key.charAt(i) % 3 == 0 ) {
					
					normalKey += "2";
				}
				else {
					
					normalKey += "3";
				}
				
			}

			
			System.out.println("Please, enter cyphered image name.");
			fileName = sc.nextLine();//"output.png";
			
			File decypherFile = new File(fileName);
			
			String fullDecypherString = "";
			
			try {
				BufferedImage decypherImage = ImageIO.read(decypherFile);
				
				iWidth = decypherImage.getWidth();
				iHeight = decypherImage.getHeight();
				
				
				
				while (currentElement < 16) {
				
					Color originalColor = new Color(decypherImage.getRGB(currentX, currentY));	
					//pixel = decypherImage.getRGB(currentX, currentY);
					r1 = originalColor.getRed();
					g1 = originalColor.getGreen();
					b1 = originalColor.getBlue();
					
					r1Binary = Integer.toBinaryString(r1);
					g1Binary = Integer.toBinaryString(g1);
					b1Binary = Integer.toBinaryString(b1);
					
					while (r1Binary.length() < 8 || g1Binary.length() < 8 || b1Binary.length() < 8) {
						
						if (r1Binary.length() < 8) {
							r1Binary = "0" + r1Binary;
						}
						
						if (g1Binary.length() < 8) {
							g1Binary = "0" + g1Binary;
						}
						
						if (b1Binary.length() < 8) {
							b1Binary = "0" + b1Binary;
						}
					}

					if (normalKey.charAt(keyIndex) == '1') {
						fullBinaryString = fullBinaryString + r1Binary.charAt(6) + r1Binary.charAt(7);
					}
					else if (normalKey.charAt(keyIndex) == '2') {
						fullBinaryString = fullBinaryString + g1Binary.charAt(6) + g1Binary.charAt(7);
					}
					else {
						fullBinaryString = fullBinaryString + b1Binary.charAt(6) + b1Binary.charAt(7);
					}
					
					keyIndex++;
					
					if (keyIndex == normalKey.length()) {
						keyIndex = 0;
					}
					
					if (currentX < iWidth - 1) {
						
						currentX++;
					}
					else {
						
						currentX = 0;
						currentY++;
					}
					
					currentElement++;
				}
				
				int sizeToWork = Integer.parseInt(fullBinaryString, 2);
				
				fullBinaryString = "";
				
				System.out.println("Please, enter name of new file.");
				fileName = sc.nextLine();//"decypher.jpg";
				File outputFile = new File(fileName);
				FileOutputStream outputFileStream = new FileOutputStream(outputFile);

				while (currentElement < (16 + (sizeToWork * 4))) {

					
					Color originalColor = new Color(decypherImage.getRGB(currentX, currentY));	
					//pixel = decypherImage.getRGB(currentX, currentY);
					r1 = originalColor.getRed();
					g1 = originalColor.getGreen();
					b1 = originalColor.getBlue();
					

					r1Binary = Integer.toBinaryString(r1);
					g1Binary = Integer.toBinaryString(g1);
					b1Binary = Integer.toBinaryString(b1);
					
					while (r1Binary.length() < 8 || g1Binary.length() < 8 || b1Binary.length() < 8) {
						
						if (r1Binary.length() < 8) {
							r1Binary = "0" + r1Binary;
						}
						
						if (g1Binary.length() < 8) {
							g1Binary = "0" + g1Binary;
						}
						
						if (b1Binary.length() < 8) {
							b1Binary = "0" + b1Binary;
						}
					}


					
					if (normalKey.charAt(keyIndex) == '1') {
						fullBinaryString = fullBinaryString + r1Binary.charAt(6) + r1Binary.charAt(7);
					}
					else if (normalKey.charAt(keyIndex) == '2') {
						fullBinaryString = fullBinaryString + g1Binary.charAt(6) + g1Binary.charAt(7);
					}
					else {
						fullBinaryString = fullBinaryString + b1Binary.charAt(6) + b1Binary.charAt(7);
					}
					
					keyIndex++;
					
					if (keyIndex == normalKey.length()) {
						keyIndex = 0;
					}
					
					
					if (fullBinaryString.length() == 8) {
						outputFileStream.write(Integer.parseInt(fullBinaryString, 2));
						fullBinaryString = "";
					}
					
					if (currentX < iWidth - 1) {
						
						currentX++;
					}
					else {
						
						currentX = 0;
						currentY++;
					}
					
					
					currentElement++;
				}
				
				
				System.out.println("Your file is decyphered.");
				
			}
			catch (Exception e) {
				System.out.println("Error of reading file.");
			}
			
			
			
		}

	}

}
