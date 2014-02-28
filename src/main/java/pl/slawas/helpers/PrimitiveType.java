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

import java.util.Arrays;
import java.util.Hashtable;

/**
 * 
 * PrimitiveType - klasa wspierajaca operacje na typach prymitywnych.
 * 
 * @author Slawomir Cichy &lt;slawas@slawas.pl&gt;
 * @version $Revision: 1.1 $
 * 
 */
public enum PrimitiveType {

	INT, BOOLEAN, BYTE, CHAR, DOUBLE, FLOAT, LONG, SHORT;

	/**
	 * Pobieranie obiektu enum na podstawie jego nazwy typu prymitywnego
	 * 
	 * @param typeName
	 *           nazwa typu
	 * @return obiekt PrimitiveType
	 */
	public static PrimitiveType get(String typeName) {
		return PrimitiveType.valueOf(typeName.toUpperCase());
	}

	/**
	 * Sprawdzenie czy typ o podanej nazwie jest typem prostym
	 * 
	 * @param typeName
	 *           nazwa typu
	 * @return [true|false]
	 */
	public static boolean isPrimitiveType(String typeName) {
		try {
			return (PrimitiveType.valueOf(typeName.toUpperCase()) != null);
		} catch (Exception e) {
		}
		return false;

	}

	/**
	 * Metoda zmieniająca tablice prymitywnych typow (array) do hashtable. Metoda
	 * niezbedna do poprawnego zamienienia tablic do właściwości.
	 * 
	 * @param array
	 * @return tablica z zamienionymi typami prymitywnymi do obiektow
	 */
	public Hashtable<Object, Object> primitiveArray2Hashtable(Object array) {

		Hashtable<Object, Object> hashtable = new Hashtable<Object, Object>();
		switch (this) {
		case INT:
			for (int indx = 0; indx < ((int[]) array).length; indx++) {
				hashtable.put(
						Integer.valueOf(indx),
						Integer.valueOf(((int[]) array)[indx]));
			}
			return hashtable;
		case BOOLEAN:
			for (int indx = 0; indx < ((boolean[]) array).length; indx++) {
				hashtable.put(
						Integer.valueOf(indx),
						Boolean.valueOf(((boolean[]) array)[indx]));
			}
			return hashtable;
		case BYTE:
			for (int indx = 0; indx < ((byte[]) array).length; indx++) {
				hashtable.put(
						Integer.valueOf(indx),
						Integer.valueOf(((byte[]) array)[indx]));
			}
			return hashtable;
		case CHAR:
			for (int indx = 0; indx < ((char[]) array).length; indx++) {
				hashtable.put(
						Integer.valueOf(indx),
						Integer.valueOf(((char[]) array)[indx]));
			}
			return hashtable;
		case DOUBLE:
			for (int indx = 0; indx < ((double[]) array).length; indx++) {
				hashtable.put(
						Integer.valueOf(indx),
						Double.valueOf(((double[]) array)[indx]));
			}
			return hashtable;
		case FLOAT:
			for (int indx = 0; indx < ((float[]) array).length; indx++) {
				hashtable.put(
						Integer.valueOf(indx),
						Float.valueOf(((float[]) array)[indx]));
			}
			return hashtable;
		case LONG:
			for (int indx = 0; indx < ((long[]) array).length; indx++) {
				hashtable.put(
						Integer.valueOf(indx),
						Long.valueOf(((long[]) array)[indx]));
			}
			return hashtable;
		case SHORT:
			for (int indx = 0; indx < ((short[]) array).length; indx++) {
				hashtable.put(
						Integer.valueOf(indx),
						Integer.valueOf(((short[]) array)[indx]));
			}
			return hashtable;
		default:
			return null;
		}
	}

	/**
	 * Metoda do kopiowania macierzy z prymitywnymi typami
	 * 
	 * @param sourceArray
	 *           macierz zrodlowa
	 * @return skopiowana nowa macierz
	 */
	public Object copyOf(Object sourceArray) {

		Object targetArray = null;
		int arrayLen = 0;

		switch (this) {
		case INT:
			arrayLen = ((int[]) sourceArray).length;
			targetArray = Arrays.copyOf((int[]) sourceArray, arrayLen);
			break;
		case BOOLEAN:
			arrayLen = ((boolean[]) sourceArray).length;
			targetArray = Arrays.copyOf((boolean[]) sourceArray, arrayLen);
			break;
		case BYTE:
			arrayLen = ((byte[]) sourceArray).length;
			targetArray = Arrays.copyOf((byte[]) sourceArray, arrayLen);
			break;
		case CHAR:
			arrayLen = ((char[]) sourceArray).length;
			targetArray = Arrays.copyOf((char[]) sourceArray, arrayLen);
			break;
		case DOUBLE:
			arrayLen = ((double[]) sourceArray).length;
			targetArray = Arrays.copyOf((double[]) sourceArray, arrayLen);
			break;
		case FLOAT:
			arrayLen = ((float[]) sourceArray).length;
			targetArray = Arrays.copyOf((float[]) sourceArray, arrayLen);
			break;
		case LONG:
			arrayLen = ((long[]) sourceArray).length;
			targetArray = Arrays.copyOf((long[]) sourceArray, arrayLen);
			break;
		case SHORT:
			arrayLen = ((short[]) sourceArray).length;
			targetArray = Arrays.copyOf((short[]) sourceArray, arrayLen);
			break;
		default:
			return null;
		}

		return targetArray;
	}

	public Object createArray(int arrayLen) {

		Object targetArray = null;

		switch (this) {
		case INT:
			targetArray = new int[arrayLen];
			break;
		case BOOLEAN:
			targetArray = new int[arrayLen];
			break;
		case BYTE:
			targetArray = new byte[arrayLen];
			break;
		case CHAR:
			targetArray = new char[arrayLen];
			break;
		case DOUBLE:
			targetArray = new double[arrayLen];
			break;
		case FLOAT:
			targetArray = new float[arrayLen];
			break;
		case LONG:
			targetArray = new long[arrayLen];
			break;
		case SHORT:
			targetArray = new short[arrayLen];
			break;
		default:
			return null;
		}

		return targetArray;
	}

	public void set(Object sourceArray, int indx, String value) {

		switch (this) {
		case INT:
			((int[]) sourceArray)[indx] = Integer.valueOf(value).intValue();
			break;
		case BOOLEAN:
			((boolean[]) sourceArray)[indx] = Boolean.valueOf(value).booleanValue();
			break;
		case BYTE:
			((byte[]) sourceArray)[indx] = Byte.valueOf(value).byteValue();
			break;
		case CHAR:
			((char[]) sourceArray)[indx] = (char) Integer.valueOf(value).intValue();
			break;
		case DOUBLE:
			((double[]) sourceArray)[indx] = Double.valueOf(value).doubleValue();
			break;
		case FLOAT:
			((float[]) sourceArray)[indx] = Float.valueOf(value).floatValue();
			break;
		case LONG:
			((long[]) sourceArray)[indx] = Long.valueOf(value).longValue();
			break;
		case SHORT:
			((short[]) sourceArray)[indx] = Short.valueOf(value).shortValue();
			break;
		default:
			return;
		}

	}

	public Object setFromString(String value) {

		switch (this) {
		case INT:
			return (int) Integer.valueOf(value).intValue();
		case BOOLEAN:
			return (boolean) Boolean.valueOf(value).booleanValue();
		case BYTE:
			return (byte) Byte.valueOf(value).byteValue();
		case CHAR:
			return (char) Integer.valueOf(value).intValue();
		case DOUBLE:
			return (double) Double.valueOf(value).doubleValue();
		case FLOAT:
			return (float) Float.valueOf(value).floatValue();
		case LONG:
			return (long) Long.valueOf(value).longValue();
		case SHORT:
			return (short) Short.valueOf(value).shortValue();
		default:
			return null;
		}

	}

	public static Class<?> getPrimitiveClass(Object obj){
		if (obj instanceof Integer)
			return int.class;
		if (obj instanceof Boolean)
			return boolean.class;
		if (obj instanceof Byte)
			return byte.class;
		if (obj instanceof Character)
			return char.class;
		if (obj instanceof Double)
			return double.class;
		if (obj instanceof Float)
			return float.class;
		if (obj instanceof Long)
			return long.class;
		if (obj instanceof Short)
			return short.class;
	
		return null;
	}
	

}
