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

import java.util.List;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.junit.Test;

import pl.slawas.helpers.MockLoader;
import pl.slawas.twl4j.Logger;
import pl.slawas.twl4j.LoggerFactory;


public class MockLoaderTest extends TestCase {

	final private static Logger log = LoggerFactory
			.getLogger(MockLoaderTest.class);

	@Test
	public void testReadMockFile() {

		log.debug("Loading data....");

		List<String[]> fileData = MockLoader.loadCSV(MockLoaderTest.class,
				"/sampleCSV.data");

		log.debug("Verify loaded data....");

		int lineNumber = 0;
		for (String[] line : fileData) {
			int cellNumber = 0;
			for (String cell : line) {
				if (lineNumber == 0) {
					switch (cellNumber) {
					case 0:
						Assert.assertTrue(cell.equals("1"));
						break;
					case 1:
						Assert.assertTrue(cell.equals("Basia"));
						break;
					case 2:
						Assert.assertTrue(cell.equals("Dominika"));
						break;
					case 3:
						Assert.assertTrue(cell.equals("Karolinka"));
						break;
					case 4:
						Assert.assertTrue(cell.equals("Ola"));
						break;
					case 5:
						Assert.assertTrue(cell.equals("Slawek"));
						break;
					}
				} else {
					switch (cellNumber) {
					case 0:
						Assert.assertTrue(cell.equals("2"));
						break;
					case 1:
						Assert.assertTrue(cell.equals("Mama"));
						break;
					case 2:
						Assert.assertTrue(cell.equals("Tata"));
						break;
					case 3:
						Assert.assertTrue(cell.equals("Maciek"));
						break;
					case 4:
						Assert.assertTrue(cell.equals("Wojtek"));
						break;
					case 5:
						Assert.assertTrue(cell.equals("Slawek"));
						break;
					}
				}
				cellNumber++;
			}
			lineNumber++;
		}

	}

}
