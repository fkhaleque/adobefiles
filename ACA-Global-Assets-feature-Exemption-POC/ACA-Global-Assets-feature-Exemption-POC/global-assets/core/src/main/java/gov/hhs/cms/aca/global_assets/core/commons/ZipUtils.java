package gov.hhs.cms.aca.global_assets.core.commons;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.IOUtils;

public class ZipUtils {

	public static void compressZipfile(String sourceDir, String outputFile) throws IOException, FileNotFoundException {
		ZipOutputStream zipFile = new ZipOutputStream(new FileOutputStream(outputFile));
		compressDirectoryToZipfile(sourceDir, sourceDir, zipFile);
		IOUtils.closeQuietly(zipFile);
	}

	private static void compressDirectoryToZipfile(String rootDir, String sourceDir, ZipOutputStream out) throws IOException, FileNotFoundException {
		for (File file : new File(sourceDir).listFiles()) {
			if (file.isDirectory()) {
				compressDirectoryToZipfile(rootDir, sourceDir + File.separator + file.getName() , out);
			} else {
				String zipInternalPath = sourceDir.replace(rootDir, "");
				if (zipInternalPath.length()>0) zipInternalPath += File.separator;
				ZipEntry entry = new ZipEntry( zipInternalPath + file.getName());
				out.putNextEntry(entry);

				FileInputStream in = new FileInputStream(sourceDir + File.separator + file.getName());
				IOUtils.copy(in, out);
				IOUtils.closeQuietly(in);
			}
		}
	}

	public static File[] extractZipFile(String zipFile, String outputFolder) throws IOException{

		ArrayList<File> zipContents = new ArrayList<File>();
		FileOutputStream fos = null;
		final byte[] buffer = new byte[1024];

		try(ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile));){

			//create output directory if not exists
			Primitives.createMissingFolder(outputFolder);

			//get the zipped file list entry
			ZipEntry ze = zis.getNextEntry();

			while(ze!=null){

				String fileName = ze.getName();
				File newFile = new File(outputFolder + File.separator + fileName);

				//create all non exists folders
				//else you will hit FileNotFoundException for compressed folder
				new File(newFile.getParent()).mkdirs();

				fos = new FileOutputStream(newFile);

				//Stream from ZIP into file
				int len;
				while ((len = zis.read(buffer)) > 0) {
					fos.write(buffer, 0, len);
				}

				fos.close();
				ze = zis.getNextEntry();

				zipContents.add(newFile);
			}

			zis.closeEntry();

		} finally {
			if (null != fos) {
				fos.close();
			}
		}

		return zipContents.toArray(new File[zipContents.size()]);

	}
}
