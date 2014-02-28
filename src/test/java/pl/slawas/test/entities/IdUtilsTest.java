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
package pl.slawas.test.entities;

import javax.persistence.Id;

import junit.framework.TestCase;

import org.junit.Test;

import pl.slawas.entities.IDUtils;
import pl.slawas.entities.IDUtilsErrorException;
import pl.slawas.twl4j.Logger;
import pl.slawas.twl4j.LoggerFactory;


public class IdUtilsTest extends TestCase {
	
	private static Logger log = LoggerFactory.getLogger(IdUtilsTest.class);
	
	@Test
	public void testStringToId() throws IDUtilsErrorException {
		
		String result = "OK";
		Object id; 
		id = IDUtils.stringToId(IntegerId.class, "1");
		log.debug("Typ '{}' : {}", new Object[] {id.getClass(), id });
		assertEquals("Oczekiwałem typu ", Integer.class, id.getClass());
		Integer expected1 = 1;
		assertTrue("Powinny być równe", (expected1.equals(id)));
		
		id = IDUtils.stringToId(StringId.class, "01");
		log.debug("Typ '{}' : {}", new Object[] {id.getClass(), id });
		assertEquals("Oczekiwałem typu ", String.class, id.getClass());
		String expected2 = "01";
		assertTrue("Powinny być równe", (expected2.equals(id)));

		id = IDUtils.stringToId(IntId.class, "1");
		log.debug("Typ '{}' : {}", new Object[] {id.getClass(), id });
		assertEquals("Oczekiwałem typu ", Integer.class, id.getClass());
		int expected3 = 1;
		assertTrue("Powinny być równe", (expected3 == (Integer) id));

		id = IDUtils.stringToId(DoubId.class, "1.1");
		log.debug("Typ '{}' : {}", new Object[] {id.getClass(), id });
		assertEquals("Oczekiwałem typu ", Double.class, id.getClass());
		double expected4 = 1.1;
		assertTrue("Powinny być równe", (((Double) id).equals(expected4)));

		id = IDUtils.stringToId(DoubleId.class, "1.1");
		log.debug("Typ '{}' : {}", new Object[] {id.getClass(), id });
		assertEquals("Oczekiwałem typu ", Double.class, id.getClass());
		Double expected5 = 1.1;
		assertTrue("Powinny być równe", (expected5.equals(id)));

		IdUtilsEntity entity = new IdUtilsEntity();
		IDUtils.setObjectId(entity, "testId");
		assertEquals("Id powinno być równe", "testId", entity.getId());
		assertEquals("Id powinno być równe", "testId", IDUtils.getObjectId(entity));
		
		
		
		
		assertEquals("Oczekiwany rezultet", "OK", result);
}

	/**
	 * 
	 * IntegerId - testowa klasa z identyfikatorem typu {@link Integer}
	 *
	 * @author Slawomir Cichy &lt;slawas@slawas.pl&gt;
	 * @version $Revision: 1.1.2.2 $
	 *
	 */
	public class IntegerId {
	
		@Id
		Integer id;
		
	}

	/**
	 * 
	 * StringId - testowa klasa z identyfikatorem typu {@link String}
	 *
	 * @author Slawomir Cichy &lt;slawas@slawas.pl&gt;
	 * @version $Revision: 1.1.2.2 $
	 *
	 */
	public class StringId {
		
		@Id
		String id;
		
	}

	/**
	 * 
	 * IntId - testowa klasa z identyfikatorem typu int
	 *
	 * @author Slawomir Cichy &lt;slawas@slawas.pl&gt;
	 * @version $Revision: 1.1.2.2 $
	 *
	 */
	public class IntId {
		
		@Id
		int id;
		
	}

	/**
	 * 
	 * DoubId - testowa klasa z identyfikatorem typu double
	 *
	 * @author Slawomir Cichy &lt;slawas@slawas.pl&gt;
	 * @version $Revision: 1.1.2.2 $
	 *
	 */
	public class DoubId {
		
		@Id
		double id;
		
	}

	/**
	 * 
	 * DoubleId - testowa klasa z identyfikatorem typu {@link Double}
	 *
	 * @author Slawomir Cichy &lt;slawas@slawas.pl&gt;
	 * @version $Revision: 1.1.2.2 $
	 *
	 */
	public class DoubleId {
		
		@Id
		Double id;
		
	}

}
