package gov.hhs.cms.aca.global_assets.core.commons;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.adobe.aemfd.docmanager.Document;

/**
 * Collection of tools for conversion and manipulation of primitive objects.
 * 
 * @author Guillaume (William) Clement (<a href="mailto:guillaume.clement@adobe.com">guillaume.clement@adobe.com</a>)
 *
 */
public class Primitives {

	public static String stripNewlineCharacters(String stripMe) {
		if (stripMe!=null)
		{
			// put all on one line
			return stripMe.replaceAll("\n", "").replaceAll("\r", "");
		} 
		return stripMe;

	}
	/**
	 * Method to convert a string into a ByteArrayInputStream
	 * 
	 * @param string
	 * @return ByteArrayInputStream
	 * @throws IOException 
	 * @throws Exception 
	 */
	public static InputStream toInputStream(String string) throws IOException{

		try{
			return new ByteArrayInputStream(string.getBytes(StandardCharsets.UTF_8));	
		} catch(Exception e){
			throw new IOException("Failed to generate input stream from string.", e);
		}
	}

	/**
	 * Method to convert input stream to string
	 * 
	 * @param stream
	 * @return
	 * @throws IOException
	 */
	public static String inputStreamToString(InputStream stream) throws IOException{

		try(java.util.Scanner s = new java.util.Scanner(stream)) { 
			return s.useDelimiter("\\A").hasNext() ? s.next() : ""; 
		}
	}

	/**
	 * Method used to write document to disk by buffering input stream into a File
	 * output stream.
	 * 
	 * @param doc
	 * @param path
	 * @throws IOException
	 */
	public static void writeDoc(Document doc, String path) throws IOException{

		OutputStream outputStream = null;

		try{
			final File file = new File(path);
			outputStream = new FileOutputStream(file);

			//If the file doesn't exists, then create it but the constructor should now take care of it (post Java 1.7)
			if (!file.exists()) {
				file.createNewFile();
			}

			stream(doc.getInputStream(), outputStream);

			//Flush
			outputStream.flush();

		} finally{
			//Always close streams
			if(!Primitives.isNull(outputStream)){
				try {
					outputStream.close();
				} catch (IOException e) {
					System.out.println("Failed to close streams");
				}
			}
		}
	}

	public static Document readDoc(String path) throws FileNotFoundException, MalformedURLException{

		String filePrefix = "file:///";

		if(!path.startsWith(filePrefix)){
			path = filePrefix + path;
		}

		//Create document object to emulate LC/AEM process	
		final URL loc = new URL(path);
		return new Document(loc);
	}

	/**
	 * Method used to read the contents of a file as string.
	 * 
	 * @param path
	 * @param encoding
	 * @return fileString
	 * @throws IOException
	 */
	public static String readFile(String path, Charset encoding) throws IOException {
		final byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}

	/**
	 * Method to check if Object is null
	 * 
	 * @param toCheck
	 * @param ifNull
	 * @return toCheck
	 */
	public static <T> T ifNull(T toCheck, T ifNull) {
		if (toCheck == null) {
			return ifNull;
		}
		return toCheck;
	}

	/**
	 * Method to check if Object is null
	 * 
	 * @param toCheck
	 * @return boolean
	 */
	public static <T> boolean isNull(T toCheck) {
		if (toCheck == null) {
			return true;
		}
		return false;
	}

	/**
	 * Writes an input stream into an output stream. This can be used in servlets.
	 * 
	 * @param input
	 * @param output
	 * @return
	 * @throws IOException
	 * @see {@link <a href="http://stackoverflow.com/questions/10142409/write-an-inputstream-to-an-httpservletresponse">http://stackoverflow.com/questions/10142409/write-an-inputstream-to-an-httpservletresponse</a>}
	 */
	public static long stream(InputStream input, OutputStream output) throws IOException {
		try (
				ReadableByteChannel inputChannel = Channels.newChannel(input);
				WritableByteChannel outputChannel = Channels.newChannel(output);
				) {
			ByteBuffer buffer = ByteBuffer.allocateDirect(10240);
			long size = 0;

			while (inputChannel.read(buffer) != -1) {
				buffer.flip();
				size += outputChannel.write(buffer);
				buffer.clear();
			}

			return size;
		}
	}

	/**
	 * Create a folder tree if one does not exist
	 * 
	 * @param folderPathString
	 */
	public static String createMissingFolder(String folderPathString) {

		//Make sure folder name is finished with a path separator
		if(!folderPathString.endsWith(System.getProperty("file.separator"))){
			folderPathString = folderPathString + System.getProperty("file.separator");
		}

		//Create folder if it does not exist
		File dir = new File(folderPathString);
		if(!dir.exists()){
			dir.mkdirs();
		}

		return folderPathString;
	}

	/**
	 * Extension of the stream method to copy file streams
	 * @param source
	 * @param dest
	 * @throws IOException
	 */
	public static void copyFileUsingStream(File source, File dest) throws IOException {

		if(!dest.exists()){
			dest.createNewFile();
		}
		
		try (InputStream is = new FileInputStream(source);
				OutputStream os = new FileOutputStream(dest);
			){
			
			stream(is, os);
		}
	}

}
