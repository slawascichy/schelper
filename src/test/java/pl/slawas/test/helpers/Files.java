/*
 * Slawas.pl Copyright &copy; 2007-2012 
 * http://slawas.pl 
 * All rights reserved.
 * 
 * THIS SOFTWARE IS PROVIDED "AS IS" AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN
 * NO EVENT SHALL SŁAWOMIR CICHY BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package pl.slawas.test.helpers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import pl.slawas.twl4j.Logger;
import pl.slawas.twl4j.LoggerFactory;

/**
 * 
 * @author slawas
 * 
 */
public class Files {

	private static Logger log = LoggerFactory.getLogger(Files.class);

	private static String sep = File.separator;

	/**
	 * Usuwanie pliku z filesytem'u
	 * 
	 * @param path
	 * @return
	 */
	public static int removeFile(String path) {
		int error = 0;
		File f = new File(path);
		boolean success = f.delete();
		if (!success) {
			log.error("Can't remove file: {}", path);
			error = -1;
		} else
			log.debug("File removed: {}", path);
		return error;
	}

	/**
	 * Usuwanie katalogu z filesytem'u
	 * 
	 * @param path
	 * @return
	 */
	public static int removeDir(String path) {
		int error = 0;
		boolean success = true;
		File f = new File(path);
		if (f.isDirectory()) {
			String files[] = f.list();
			if (files != null)
				for (int i = 0; i < files.length; i++) {
					String childrenPath = path + "/" + files[i];
					error = removeDir(childrenPath);
				}
		}
		success = f.delete();
		if (!success) {
			log.error("Can't remove directory: {}", path);
			error = -1;
		} else {
			log.debug("Directory removed: {}", path);
			error = 0;
		}
		return error;
	}

	/**
	 * Przenoszenie plików pomiedzy katalogami
	 * 
	 * @param srcPath
	 * @param trgPath
	 * @param trgDir
	 * @return
	 */
	public static int moveFile(String srcPath, String trgPath, String trgDir) {
		int error = 0;
		boolean success = true;

		File targetDir = new File(trgDir);
		if (!targetDir.exists()) {
			success = targetDir.mkdirs();
			if (!success) {
				log.error("Can't create target directory: {}", trgDir);
				error = -1;
				return error;
			} else
				log.debug("Target directory created: {}", trgDir);
		}

		File srcFile = new File(srcPath);
		success = srcFile.renameTo(new File(trgPath));
		if (!success) {
			log.error("Can't move file from {} to {}", new Object[] { srcPath,
					trgPath });
			error = -1;
		} else
			log.debug("File moved from {} to {}", new Object[] { srcPath,
					trgPath });
		return error;
	}

	public static void copyFiles(String srcDir, String dstDir) throws Exception {

		File fdstDir = new File(dstDir);

		if (!fdstDir.exists()) {
			if (!fdstDir.mkdirs()) {
				throw new Exception((new StringBuilder(
						"Can't create destination directory")).append(dstDir)
						.toString());
			}
			log.debug("Target directory created: {}", dstDir);
		}

		String[] fileList = new File(srcDir).list();

		boolean afile;

		for (int i = 0; i < fileList.length; i++) {
			afile = new File(srcDir + sep + fileList[i]).isFile();
			if (afile) {
				log.debug("Copying {}{}{} to {}{}{} ...", new Object[] {
						srcDir, sep, fileList[i], dstDir, sep, fileList[i] });
				copyFile(srcDir + sep + fileList[i], dstDir + sep + fileList[i]);
			}
		}

	}

	public static void copyFile(String srcFile, String dstFile)
			throws java.io.IOException {

		FileInputStream inp = new FileInputStream(srcFile);
		FileOutputStream out = new FileOutputStream(dstFile);

		byte[] buff = new byte[8192];
		int count; // read up to "buff.length" bytes
		while ((count = inp.read(buff)) != -1) {
			// write "count" bytes
			out.write(buff, 0, count);
		}

		out.close();
		inp.close();

	}

}
