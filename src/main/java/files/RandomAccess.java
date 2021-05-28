package files;

import javax.print.DocFlavor;
import java.awt.*;
import java.io.IOException;
import java.io.RandomAccessFile;

public class RandomAccess {
	
	/**
	 * Treat the file as an array of (unsigned) 8-bit values and sort them 
	 * in-place using a bubble-sort algorithm.
	 * You may not read the whole file into memory! 
	 * @param file
	 */
	public static void sortBytes(RandomAccessFile file) throws IOException {
		boolean sorted = false;
		boolean swap;
		int current = 0;
		int next = 0;
		try {
			for (int i = 0; i < file.length(); i++) {
				if (sorted == true){
					return;
				}
				swap = false;
				for (int j = 1; j < file.length() - i; j++) {
						/* read 2 bytes (-from byte j-1 from the beginning)
						*  the first byte to current . the second byte to next */
					file.seek(j - 1);
					current = file.read();
					next = file.read();
						/* if current unsigned value is bigger than next unsigned value
						 * then swap (- by overwrite) */
					if ((current & 0XFF) > (next & 0XFF)) {
						swap = true;
						file.seek(j - 1);
						file.write(next);
						file.write(current);
					}
				}
					/* if did not execute swap in the loop above then file is sorted
					* (- each byte is smaller than is next byte) */
				if (swap == false) {
					sorted = true;
				}
			}
		}catch(IOException e){
			System.err.println("Error:" + e);
		}
	}
	
	/**
	 * Treat the file as an array of unsigned 24-bit values (stored MSB first) and sort
	 * them in-place using a bubble-sort algorithm. 
	 * You may not read the whole file into memory! 
	 * @param file
	 */
	public static void sortTriBytes(RandomAccessFile file) throws IOException {
		boolean sorted = false;
		boolean swap;
		byte[] current = new byte[3];
		byte[] next = new byte[3];
		try {
			for (int i = 0; i < file.length(); i = i + 3) {
				if (sorted == true) {
					return;
				}
				swap = false;
				for (int j = 3; j < file.length() - i; j = j + 3) {
						/* read 6 bytes (-from byte j-3 from the beginning)
					 	*  the first 3-bytes to current . the second 3-bytes to next */
					file.seek(j - 3);
					file.readFully(current);
					file.readFully(next);
						/* if current unsigned value is bigger than next unsigned value
					 	* then swap (- by overwrite) */
					if (currentIsBigger(current, next)) {
						swap = true;
						file.seek(j - 3);
						file.write(next);
						file.write(current);
					}
				}
					/* if did not execute swap in the loop above then file is sorted
					 * (- each byte is smaller than is next byte) */
				if (swap == false) {
					sorted = true;
				}
			}
		}catch(IOException e){
			System.err.println("Error:" + e);
		}
	}

	/**
	 * check if the unsigned number value of current is bigger than next.
	 * @param current
	 * @param next
	 * @return true if the unsigned number value of current is bigger. And false otherwise.
	 */


	public static boolean currentIsBigger(byte[] current, byte[] next) {
		for (int i = 0; i < current.length; i++) {
				if ((current[i] & 0XFF) > (next[i] & 0XFF)) return true;
				if ((current[i] & 0XFF) < (next[i] & 0XFF)) return false;
		}
		/* current equals to next */
		return false;
	}
}

