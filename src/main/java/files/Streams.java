package files;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class Streams {
	/**
	 * Read from an InputStream until a quote character (") is found, then read
	 * until another quote character is found and return the bytes in between the two quotes. 
	 * If no quote character was found return null, if only one, return the bytes from the quote to the end of the stream.
	 * @param in
	 * @return A list containing the bytes between the first occurrence of a quote character and the second.
	 */
	public static List<Byte> getQuoted(InputStream in) throws IOException {
		List<Byte> quote = new ArrayList<>();
		int data = 0;
		try {
				/* run until the first quote character */
			while ((data = in.read()) != (int) ('"') && data != -1) { }

				/* if there is no quote character*/
			if (data == -1) return null;

				/* add the bytes between the quotes to the list*/
			while ((data = in.read()) != (int) ('"') && data != -1) {
				quote.add((byte) data);
			}
		}catch(IOException e){
			System.err.println("Error:" + e);
		}

		return quote;
	}
	
	/**
	 * Read from the input until a specific string is read, return the string read up to (not including) the endMark.
	 * @param in the Reader to read from
	 * @param endMark the string indicating to stop reading. 
	 * @return The string read up to (not including) the endMark (if the endMark is not found, return up to the end of the stream).
	 */
	public static String readUntil(Reader in, String endMark) throws IOException {
		StringBuilder str = new StringBuilder();
		int data = 0;
		try{
				/* read the Reader (-in) to StringBuilder(-str) */
			while ((data = in.read()) != -1) {
				str.append((char) data);
			}
		}catch(IOException e){
			System.err.println("Error:" + e);
		}
			/* find the index of endMark*/
		int endIndex = str.toString().indexOf(endMark);
		/* if endMark exists in str
		 * delete from str the characters from endMark (include)*/
		if (endIndex != -1){
				str.delete(endIndex, str.length());
		}

		return str.toString();
	}
	
	/**
	 * Copy bytes from input to output, ignoring all occurrences of badByte.
	 * @param in
	 * @param out
	 * @param badByte
	 */
	public static void filterOut(InputStream in, OutputStream out, byte badByte) throws IOException {
		int data;
		try {
				/* read from inputStream and write to outputStream,
				*  all the bytes that different from badByte */
			while ((data = in.read()) != -1) {
				if ((byte) data != badByte) {
					out.write(data);
				}
			}
		}catch(IOException e){
			System.err.println("Error:" + e);
		}
	}
	
	/**
	 * Read a 40-bit (unsigned) integer from the stream and return it. The number is represented as five bytes, 
	 * with the most-significant byte first. 
	 * If the stream ends before 5 bytes are read, return -1.
	 * @param in
	 * @return the number read from the stream
	 */
	public static long readNumber(InputStream in) throws IOException {
		long val = 0;
		int[] arr = new int[5];
		int data =0;
		int iterator = 4;
		try {
			 /* read a 40-bit (8 bytes) to arr*/
			while ((data = in.read()) != -1 && iterator >= 0) {
				arr[iterator] = data;
				iterator--;
			}
		}catch(IOException e){
			System.err.println("Error:" + e);
		}
			/* if there number of bytes is not 5 */
		if(iterator != -1) return -1;

			/* compute value of the 40-bit*/
		for (int i = 4; i >= 0; i--) {
			val <<= 8;
			val += arr[i];
		}

		return val;
	}
}
