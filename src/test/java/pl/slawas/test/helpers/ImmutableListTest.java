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

import pl.slawas.helpers.ImmutableList;
import pl.slawas.twl4j.Logger;
import pl.slawas.twl4j.LoggerFactory;


public class ImmutableListTest extends TestCase {
	
	final private static Logger log = LoggerFactory.getLogger(ImmutableListTest.class);
	
	private ImmutableList<String> propertyList;

	public ImmutableListTest() {

		this.propertyList = new ImmutableList<String>();
		String currVal;
		
		currVal = (new StringBuilder("testValue1")).toString();
		this.propertyList.pAdd(currVal);

		currVal = (new StringBuilder("testValue2")).toString();
		this.propertyList.pAdd(currVal);

		currVal = (new StringBuilder("testValue3")).toString();
		this.propertyList.pAdd(currVal);

		currVal = (new StringBuilder("testValue4")).toString();
		this.propertyList.pAdd(currVal);

		currVal = (new StringBuilder("testValue5")).toString();
		this.propertyList.pAdd(currVal);
	}
	
	private List<String> getPropertyList() {
		return this.propertyList;
	}
	
	@Test
	public void testReadImmutableList() {
		
		List<String> testList = getPropertyList();
		int i = 0;
		for (String testVal : testList) {
			log.debug("Value[{}]: {}", new Object[]{i, testVal});
			i++;
		}
		
		Assert.assertTrue(5 == i);
		
	}

	@Test
	public void testAddImmutableList() {
		try {
			List<String> testList = getPropertyList();
			testList.add("Test");
			Assert.fail("Udalo sie dodac obiekt do listy");
		} catch (UnsupportedOperationException e) {
			log.debug("{}", e.getMessage());
			Assert.assertTrue(true);
		}
	}
	
}
