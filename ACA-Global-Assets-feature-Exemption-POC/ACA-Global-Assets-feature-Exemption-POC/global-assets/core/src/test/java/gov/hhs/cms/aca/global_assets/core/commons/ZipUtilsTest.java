package gov.hhs.cms.aca.global_assets.core.commons;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

public class ZipUtilsTest {

	String workingDir = System.getProperty("user.dir").concat("/target/test");

	@Test
	public void testZipFolder() throws IOException {

		File testDir = new File("src/test/resources");

		String path = Primitives.createMissingFolder(workingDir);

		ZipUtils.compressZipfile(testDir.getAbsolutePath(), path + "ziputils-testfolder.zip");

	}

}
