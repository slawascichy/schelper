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
package pl.slawas.test.diffs;

import pl.slawas.diffs.Property;
import pl.slawas.diffs.Trace;

/**
 * 
 * TracerChildMock - obiekt dziecko do testowania sledzenia zmian w obiektach
 *
 * @author Slawomir Cichy &lt;slawas@slawas.pl&gt;
 * @version $Revision: 1.1 $
 *
 */
public class DiffMockChild {

	@Property(key="child.name")
	@Trace(label = "Nazwa pracownika")
	private String childName;

	@Property(key="child.description")
	@Trace
	private String childDescription;

	@Property(key="child.id")
	@Trace(label = "Identyfikator", isDynamic = true, labelKey = "childId")
	private int id;

	@Property(key="child.boss")
	@Trace(label = "Szef")
	private DiffMockParent parent;
	
	/**
	 * @return the childName
	 */
	public String getChildName() {
		return childName;
	}

	/**
	 * @param childName the childName to set
	 */
	public void setChildName(String childName) {
		this.childName = childName;
	}

	/**
	 * @return the childDescription
	 */
	public String getChildDescription() {
		return childDescription;
	}

	/**
	 * @param childDescription the childDescription to set
	 */
	public void setChildDescription(String childDescription) {
		this.childDescription = childDescription;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the parent
	 */
	public DiffMockParent getParent() {
		return parent;
	}

	/**
	 * @param parent the parent to set
	 */
	public void setParent(DiffMockParent parent) {
		this.parent = parent;
	}
	
	
	

}
