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
package pl.slawas.helpers;

/**
 * 
 * ConfigFactory
 * 
 * @author Slawomir Cichy &lt;slawas@slawas.pl&gt;
 * @version $Revision: 1.2 $
 * 
 * @param <Obj>
 *           klasa obiektu konfiguracji
 */
public interface ConfigFactory<Obj> {

	/**
	 * Właściwa metoda tworząca obiekt podany w parametrze &lt;Obj> z pliku
	 * konfiguracyjnego.
	 * 
	 * @return obiekt typu &lt;Obj>
	 * @throws ConfigException
	 *            błąd parsowania pliku konfiguracyjnego
	 */
	Obj makeIndexConfig() throws ConfigException;

	/**
	 * @return klasa pomocnicza pozwalająca na odszukanie pliku konfiguracji w
	 *         pakiecie jar
	 */
	public Class<?> getResourceClass();

	/**
	 * 
	 * @param resourceClass
	 *           klasa pomocnicza pozwalająca na odszukanie pliku konfiguracji w
	 *           pakiecie jar
	 */
	public void setResourceClass(Class<?> resourceClass);
}
