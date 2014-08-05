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
package pl.slawas.test.pageing;

import junit.framework.TestCase;

import org.junit.Test;

import pl.slawas.paging.Page;
import pl.slawas.paging.PagingParams;
import pl.slawas.paging.PagingParamsReadOnly;
import pl.slawas.paging.PaginigParamsException;
import pl.slawas.twl4j.Logger;
import pl.slawas.twl4j.LoggerFactory;

public class PagingParamsTest extends TestCase {

	final protected static Logger logger = LoggerFactory
			.getLogger(PagingParamsTest.class);

	@Test
	public void testPagingParams() {

		String result = "OK";

		Page page = new Page();
		PagingParams testedPagingParams = new PagingParams(page);
		assertEquals("Bledny offset", 0, testedPagingParams.getCursorOfPage()
				.intValue());
		assertEquals("Bledny pageSize", page.getSize(),
				testedPagingParams.getPageSize());

		testedPagingParams = new PagingParams(200, 2000L);
		assertEquals("Bledne ustawienie parametru stronicowania pageSize", 200,
				testedPagingParams.getPageSize());

		page = testedPagingParams.getPage();
		assertEquals("Bledny numer strony", 11, page.getNumber());
		assertEquals("Bledny count", 200, page.getSize());

		try {
			page = testedPagingParams.getFirstPage();
			assertFalse(
					"Nie powinno sie udac bo offset ustawiony jest na 2000, wiec strona pierwsza nie jest dostepna.",
					true);
		} catch (PaginigParamsException e) {
			assertFalse("Nie powinno sie udac: " + e.getMessage(), false);
		}

		testedPagingParams = new PagingParams(200, 0L);
		page = testedPagingParams.getFirstPage();
		assertEquals("Bledny numer strony", Page.MIN_PAGE_NR, page.getNumber());
		assertEquals("Bledny count", 200, page.getSize());

		page = testedPagingParams.getPage();
		assertEquals("Bledny numer strony", Page.MIN_PAGE_NR, page.getNumber());
		assertEquals("Bledny count", 200, page.getSize());

		logger.debug("Test strony niepowodzenia ustawienia strony o numerze ujemnym...");
		assertFalse("Nie powinno sie udac ustawienie strony o numerze ujemnym",
				testedPagingParams.setPage(-1));
		logger.debug("Test strony ujemnej zakonczony powodzeniem.");

		logger.debug("Test strony niepowodzenia ustawienia strony o numerze zerowym...");
		assertFalse("Nie powinno sie udac ustawienie strony o numerze ujemnym",
				testedPagingParams.setPage(0));
		logger.debug("Test strony zerowej zakonczony powodzeniem.");

		page = testedPagingParams.getPage();
		assertEquals("Bledny numer strony", Page.MIN_PAGE_NR, page.getNumber());
		assertEquals("Bledny count", 200, page.getSize());

		testedPagingParams = new PagingParams(10, 0L, 10, 50L);
		logger.debug("Powodzenie ustawienia strony nr 2: {}",
				testedPagingParams.setPage(2));

		page = testedPagingParams.getPage();
		assertEquals("Bledny numer strony", 2, page.getNumber());
		assertEquals("Bledny count", 10, page.getSize());

		testedPagingParams = new PagingParams(new Page());
		testedPagingParams.setMaxCount(966L);
		logger.debug("Maksymalna liczba stron: {}",
				testedPagingParams.getMaxPages());
		assertEquals("Bledna oczekiwana liczba stron", 17,
				testedPagingParams.getMaxPages());

		testedPagingParams.setPage(new Page(20, 3));
		logger.debug("Maksymalna liczba stron: {}",
				testedPagingParams.getMaxPages());
		assertEquals("Bledna oczekiwana liczba stron", 49,
				testedPagingParams.getMaxPages());
		assertEquals("Bledna oczekiwana numer strony", 3, testedPagingParams
				.getPage().getNumber());
		logger.debug("Offset: {}", testedPagingParams.getCursorOfPage());
		assertEquals("Bledny oczekiwany offset", 40, testedPagingParams
				.getCursorOfPage().intValue());
		testedPagingParams.setPageSize(30);
		logger.debug("Numer strony: {}", testedPagingParams.getPage()
				.getNumber());
		logger.debug("Maksymalna liczba stron: {}",
				testedPagingParams.getMaxPages());
		assertEquals("Bledna oczekiwana liczba stron", 33,
				testedPagingParams.getMaxPages());
		assertEquals("Bledna oczekiwana numer strony", 2, testedPagingParams
				.getPage().getNumber());
		logger.debug("Offset: {}", testedPagingParams.getCursorOfPage());
		assertEquals("Bledny oczekiwany offset", 30, testedPagingParams
				.getCursorOfPage().intValue());

		testedPagingParams.setPage(testedPagingParams.getMinimalPage());
		PagingParams readOnly = new PagingParamsReadOnly(testedPagingParams);
		logger.debug("Parametry stronicowania tylko do odczytu: {}",
				readOnly.toString());
		try {
			readOnly.setOffset(200L);
			assertFalse("Nie powinno sie udac", true);
		} catch (PaginigParamsException ignore) {
			logger.debug("Operacja nie powinna sie udac: {}",
					ignore.getMessage());
		}
		try {
			readOnly.setPage(new Page(20, 3));
			assertFalse("Nie powinno sie udac", true);
		} catch (PaginigParamsException ignore) {
			logger.debug("Operacja nie powinna sie udac: {}",
					ignore.getMessage());
		}
		try {
			readOnly.setPage(new Page(30, 3));
			assertFalse("Nie powinno sie udac", true);
		} catch (PaginigParamsException ignore) {
			logger.debug("Operacja nie powinna sie udac: {}",
					ignore.getMessage());
		}
		try {
			readOnly.setMaxCount(1000L);
			assertFalse("Nie powinno sie udac", true);
		} catch (PaginigParamsException ignore) {
			logger.debug("Operacja nie powinna sie udac: {}",
					ignore.getMessage());
		}
		try {
			readOnly.setMaxPageSize(2000);
			assertFalse("Nie powinno sie udac", true);
		} catch (PaginigParamsException ignore) {
			logger.debug("Operacja nie powinna sie udac: {}",
					ignore.getMessage());
		}
		testedPagingParams = readOnly.copy();
		try {
			testedPagingParams.setMaxCount(1000L);
			assertTrue("Powinno sie udac", true);
		} catch (PaginigParamsException ignore) {
			logger.debug("Operacja powinna sie udac: {}", ignore.getMessage());
			assertFalse("Powinno sie udac", true);
		}
		try {
			readOnly.copy(testedPagingParams);
		} catch (PaginigParamsException ignore) {
			logger.debug("Operacja nie powinna sie udac: {}",
					ignore.getMessage());
		}

		assert result.equals("OK") : "Test zakonczyl sie porazka";

	}

}
