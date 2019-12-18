import java.io.Serializable;
import java.util.Random;
import java.math.BigInteger;
import java.util.Arrays;

public class LargeInteger implements Serializable{

	private final byte[] ONE = {(byte) 1};
	private final byte[] ZERO = {(byte) 0};
	private final byte[] NEG = {(byte) -1};
	private final byte bNEG = (byte) -1;
	private final byte bONE = (byte) 1;
	private final byte bZERO = (byte) 0;

	private byte[] val;

	/**
	* Constructor to make a LargeInteger equal to 0
	*/
	public LargeInteger(){
		val = ZERO;
	}

	/**
	 * Construct the LargeInteger from a given byte array
	 * @param b the byte array that this LargeInteger should represent
	 */
	public LargeInteger(byte[] b) {
		val = b;
	}

	/**
	 * Construct the LargeInteger by generatin a random n-bit number that is
	 * probably prime (2^-100 chance of being composite).
	 * @param n the bitlength of the requested integer
	 * @param rnd instance of java.util.Random to use in prime generation
	 */
	public LargeInteger(int n, Random rnd) {
		val = BigInteger.probablePrime(n, rnd).toByteArray();
	}

	/**
	 * Return this LargeInteger's val
	 * @return val
	 */
	public byte[] getVal() {
		return val;
	}

	/**
	 * Return the number of bytes in val
	 * @return length of the val byte array
	 */
	public int length() {
		return val.length;
	}

	/**
	* Return the zero state of the byte array
	* @return true if byte array is all zeros
	*/
	public boolean isZero(){
		for(byte bte : val){
			if(bte != 0)
				return false;
		}
		return true;
	}

	/**
	 * Add a new byte as the most significant in this LargeInteger
	 * @param extension the byte to place as most significant
	 */
	public void extend(byte extension) {
		byte[] newv = new byte[val.length + 1];
		newv[0] = extension;
		for (int i = 0; i < val.length; i++) {
			newv[i + 1] = val[i];
		}
		val = newv;
	}



	/**
	 * If this is negative, most significant bit will be 1 meaning most
	 * significant byte will be a negative signed number
	 * @return true if this is negative, false if positive
	 */
	public boolean isNegative() {
		return (val[0] < 0);
	}

	/**
	 * Computes the sum of this and other
	 * @param other the other LargeInteger to sum with this
	 */
	public LargeInteger add(LargeInteger other) {
		byte[] a, b;
		// If operands are of different sizes, put larger first ...
		if (val.length < other.length()) {
			a = other.getVal();
			b = val;
		}
		else {
			a = val;
			b = other.getVal();
		}

		// ... and normalize size for convenience
		if (b.length < a.length) {
			int diff = a.length - b.length;

			byte pad = (byte) 0;
			if (b[0] < 0) {
				pad = (byte) 0xFF;
			}

			byte[] newb = new byte[a.length];
			for (int i = 0; i < diff; i++) {
				newb[i] = pad;
			}

			for (int i = 0; i < b.length; i++) {
				newb[i + diff] = b[i];
			}

			b = newb;
		}

		// Actually compute the add
		int carry = 0;
		byte[] res = new byte[a.length];
		for (int i = a.length - 1; i >= 0; i--) {
			// Be sure to bitmask so that cast of negative bytes does not
			//  introduce spurious 1 bits into result of cast
			carry = ((int) a[i] & 0xFF) + ((int) b[i] & 0xFF) + carry;

			// Assign to next byte
			res[i] = (byte) (carry & 0xFF);

			// Carry remainder over to next byte (always want to shift in 0s)
			carry = carry >>> 8;
		}

		LargeInteger res_li = new LargeInteger(res);

		// If both operands are positive, magnitude could increase as a result
		//  of addition
		if (!this.isNegative() && !other.isNegative()) {
			// If we have either a leftover carry value or we used the last
			//  bit in the most significant byte, we need to extend the result
			if (res_li.isNegative()) {
				res_li.extend((byte) carry);
			}
		}
		// Magnitude could also increase if both operands are negative
		else if (this.isNegative() && other.isNegative()) {
			if (!res_li.isNegative()) {
				res_li.extend((byte) 0xFF);
			}
		}

		// Note that result will always be the same size as biggest input
		//  (e.g., -127 + 128 will use 2 bytes to store the result value 1)
		return res_li;
	}

	/**
	 * Negate val using two's complement representation
	 * @return negation of this
	 */
	public LargeInteger negate() {
		byte[] neg = new byte[val.length];
		int offset = 0;

		// Check to ensure we can represent negation in same length
		//  (e.g., -128 can be represented in 8 bits using two's
		//  complement, +128 requires 9)
		if (val[0] == (byte) 0x80) { // 0x80 is 10000000
			boolean needs_ex = true;
			for (int i = 1; i < val.length; i++) {
				if (val[i] != (byte) 0) {
					needs_ex = false;
					break;
				}
			}
			// if first byte is 0x80 and all others are 0, must extend
			if (needs_ex) {
				neg = new byte[val.length + 1];
				neg[0] = (byte) 0;
				offset = 1;
			}
		}

		// flip all bits
		for (int i  = 0; i < val.length; i++) {
			neg[i + offset] = (byte) ~val[i];
		}

		LargeInteger neg_li = new LargeInteger(neg);

		// add 1 to complete two's complement negation
		return neg_li.add(new LargeInteger(ONE));
	}

	/**
	 * Implement subtraction as simply negation and addition
	 * @param other LargeInteger to subtract from this
	 * @return difference of this and other
	 */
	public LargeInteger subtract(LargeInteger other) {
		return this.add(other.negate());
	}
/*///////////////////////////////////////////////////////*/
/*                   Added Methods                       */
/*///////////////////////////////////////////////////////*/

 /**
 * Add a new byte to the beginning of the array
 * @param arr the byte array in which to extend
 * @param type either extend with 0s or 1s
 */
	public byte[] extendByteArr(byte[] arr, byte type){
		byte[] newv = new byte[arr.length + 1];
		newv[0] = type;
		for (int i = 0; i < arr.length; i++) {
			newv[i + 1] = arr[i];
		}
		arr = newv;
		return arr;
	}

	/**
	*	shift all bits in the byte array right n times
	* @param n perform right shift n times
	* @return shifted LargeInteger
	*/
	public LargeInteger rightShift(int n){
		byte[] copyArr = new byte[val.length];
		for(int i = 0; i < n; i++){
			// find first bit's LSB
			int first = val[0] & 0x01;
			// perform right shift
			copyArr[0] = (byte) (val[0] >> 1);

			for(int j = 1; j < val.length; j++){
				// carry bit
				int LSB = val[j] & 0x01;
				byte mask;
				if(first == 1){
					mask = (byte) 0x80;
				}
				else{
					mask = (byte) 0;
				}
				copyArr[j] = (byte) (val[j] >> 1 & 0x7F | mask);
				// set carry
				first = LSB;
			}
		}
		return new LargeInteger(copyArr);
	}

	/**
	*	shift all bits in the byte array left n times
	* @param n perform left shift n times
	* @return shifted LargeInteger
	*/
	public LargeInteger leftShift(int n, boolean extendOnes){
		if(n == 0){
			return this;
		}
		byte[] vCopy = new byte[val.length];
		System.arraycopy(val, 0, vCopy, 0, val.length);
		// perform left shift n times
		byte neg = (byte) (vCopy[0] & 0x80);
		for(int i = 0; i < n; i++){
			// if the most significant bit is a 1 (negative), sign extend

			boolean carry = false;
			int expand = vCopy[0] & 0x80;

			if (expand == 0){
				System.arraycopy(vCopy, 0, vCopy, 0, vCopy.length);
				vCopy = extendByteArr(vCopy, bZERO);
			}
			else{
				if(neg == (byte) 0x80)
					vCopy = extendByteArr(vCopy, bNEG);
				else
					vCopy = extendByteArr(vCopy, bZERO);
			}
			// go through each byte
			for(int j = vCopy.length - 1; j >= 0; j--){
				// save a copy of current byte to determine carry
				byte bCopy = vCopy[j];
				// left shift 1
				vCopy[j] = (byte) (vCopy[j] << 1);
				if(carry){
					// if carry is true, most significant bit of previous byte was 1, OR 1
					vCopy[j] = (byte) (vCopy[j] | 0x01);
				}
				// & a bit mask of 0b10000000 to determine MSB value
				bCopy = (byte) (bCopy & 0x80);
				if(bCopy == (byte) 0x80){
					// if MSB = 1, carry will be performed on next byte
					carry = true;
				}else{
					carry = false;
				}
			}
			if(extendOnes){
				vCopy[vCopy.length - 1] |= 0x01;
			}
		}
		return new LargeInteger(vCopy).dePad();
	}

	/**
	* Remove extra padding
	* @return correct sign LargeInteger without extra bytes
	*/
	public LargeInteger dePad(){
		int bit = 7;
		int pad = 0;
		byte cur = val[0];
		byte bPad = (byte) 0;
		int first = cur >> 1;

		if(first == 1){
			bPad = (byte) 0xFF;
		}
		for(int i = 1; (cur == bPad) && (i < val.length); i++){
			pad += 8;
			cur = val[i];
		}

		while((cur >> (bit & 1)) == first && bit >= 0){
			bit--;
			pad++;
		}
		if((pad - 1) > 8){
			byte[] copy = new byte[val.length - (pad - 1) / 8];
			for(int i = 0; i < val.length - (pad - 1) / 8; i++){
				copy[i] = val[((pad - 1) / 8) + i];
			}
			return new LargeInteger(copy);
		}
		return this;
	}

	/**
	* Compute the product of this and other
	* @param other LargeInteger to multiply by this
	* @return product of this and other
	*/
	public LargeInteger multiply(LargeInteger other) {
		// YOUR CODE HERE (replace the return, too...)
		// mult by 0:
		if(this.isZero() || other.isZero())
			return new LargeInteger();

		LargeInteger x, y, result;
		/* determine the larger number in terms of byte size. x is "on top", as x
		*		x
		*	 *y
		*	____
		* result
		*/
		if(this.length() < other.length()){
			x = other;
			y = this;
		}
		else{
			// also this set up if same size
			x = this;
			y = other;
		}

		// set a boolean to come back and negate answer if needed
		boolean negResult = ((!x.isNegative() && y.isNegative()) || (x.isNegative() && !y.isNegative()));
		// set both values to positive for easy multiplication
		if(x.isNegative()){
			x = x.negate();
		}
		if(y.isNegative()){
			y = y.negate();
		}
			// define an int to keep track of how many times we need to shift over
			int bitShift = 0;
			byte[] yBytes = y.getVal();
			// set result to a byte value of 0
			result = new LargeInteger();
			//LargeInteger z = new LargeInteger(zx2);

			// gradeschool algorithm
			for(int i = yBytes.length - 1; i >= 0; i--){
				byte _byte = yBytes[i];
				for(int j = 7; j >= 0; j--){
					//int bit = (_byte >> i) & 0x01;
					int bit = byteToBitArr(yBytes[i])[j];
					if(bit == 1){
						LargeInteger temp = x.leftShift(bitShift, false);
						result = result.add(temp);
					}
					else{
					}
					// else add 0
					bitShift++;
				}
			}

			if(negResult){
				result = result.negate();
			}
		return result.dePad();
	}

	/**
	* Divide this by other || x / y
	*	@param other is the divisor (y)
	* @return array of [result, remainder] useful for mod
	*/
	public LargeInteger[] divide(LargeInteger other){
		LargeInteger x, y, mod, result = new LargeInteger();
		x = this;
		y = other;
		// modulus will be negative is x is negative
		boolean modIsNeg = x.isNegative();
		// set a boolean to come back and negate answer if needed
		boolean negResult = ((!x.isNegative() && y.isNegative()) || (x.isNegative() && !y.isNegative()));
		// set both values to positive for easy multiplication
		if(x.isNegative()){
			x = x.negate();
		}
		if(y.isNegative()){
			y = y.negate();
		}

		// div by 0 error, still returns 0
		if(y.isZero()){
			System.out.println("Div/0 error, returning 0");
			return new LargeInteger[] {new LargeInteger(ZERO), new LargeInteger()};
		}
		// if x == y, result is 1, mod 0
		if(x.compareTo(y) == 0){
			if(negResult)
				return new LargeInteger[] {new LargeInteger(NEG), new LargeInteger()};
			else
				return new LargeInteger[] {new LargeInteger(ONE), new LargeInteger()};
		}
		// if x is less than y, will only be modulus
		if(x.compareTo(y) < 0){
			if(modIsNeg)
				x = x.negate();
			return new LargeInteger[] {new LargeInteger(), x};
		}

		int bits = x.length() * 8 - 1;
		y = y.leftShift(bits, false);
		result = new LargeInteger(ZERO);
		mod = x;
		// binary division alg
		for(int i = 0; i <= bits; i++){
			LargeInteger dif = mod.subtract(y);
			if(dif.isNegative()){
				result = result.leftShift(1, false);
			}
			else{
				result = result.leftShift(1, true);
				mod = dif;
			}
			y = y.rightShift(1);
		}
		result = result.dePad();
		mod = mod.dePad();
		// negations
		if(negResult && !result.isZero())
			result = result.negate();
		if(modIsNeg && !mod.isZero())
			mod = mod.negate();
		result = result.dePad();
		return new LargeInteger[] {result, mod};
	}

	/**
	* Modulus method
	*	@param other y in the x % y equation
	* @return the second index of division's array
	*/
	public LargeInteger mod(LargeInteger other){
		return this.divide(other)[1];
	}

	/**
	* Compares one LargeInteger to another LargeInteger
	* @param other the LargeInteger to compare
	* @return -1 if this < other || 1 if this > other || 0 if this == other
	*/
	public int compareTo(LargeInteger other){
		LargeInteger a = this.dePad();
		LargeInteger b = other.dePad();
		// cannot compare decimal values, since we could have overflow with RSA
		int isNeg = 1; // if 1, both numbers are positive, if -1, both numbers are negative
		if(a.isNegative() && !b.isNegative()) return -1;
		if(!a.isNegative() && b.isNegative()) return 1;
		if(a.isNegative() && b.isNegative()) isNeg = -1;
		if(a.length() < b.length()) return -1;
		if(a.length() > b.length()) return 1;

		// look at significant bytes instead of going through costly subtraction arithmetic
		a = a.negate();
		b = b.negate();
		int abyte, bbyte = 0;
		for(int i = 0; i < a.length(); i++){
			abyte = a.getVal()[i] & 0xFF;
			bbyte = b.getVal()[i] & 0xFF;
			if(abyte < bbyte) return (1 * isNeg);
			else if(bbyte < abyte) return (-1 * isNeg);
		}
		// else both values are the same
		return 0;
	}

	/**
	 * Convert a byte into array of int bits
	 * @param b byte to convert
	 * @return int[] of bits
	 */
	public int[] byteToBitArr(byte b){
		int[] bitArr = new int[8];
		for(int i = 0; i < 8; i++) {
			byte bit = (byte)(b << i);
			bit = (byte) (-1 * (byte) (bit >> 7));
			bitArr[i] = (int)bit;
    }
		return bitArr;
	}

	/**
	 * Run the extended Euclidean algorithm on this and other
	 * @param other another LargeInteger
	 * @return an array structured as follows:
	 *   0:  the GCD of this and other
	 *   1:  a valid x value
	 *   2:  a valid y value
	 * such that this * x + other * y == GCD in index 0
	 */
	 public LargeInteger[] XGCD(LargeInteger other) {
		// YOUR CODE HERE (replace the return, too...)
		// modified textbook code from https://introcs.cs.princeton.edu/java/99crypto/ExtendedEuclid.java.html
		if(other.isZero())
			return new LargeInteger[] {this, new LargeInteger(ONE), new LargeInteger(ZERO)};

		LargeInteger[] vals = other.XGCD(this.mod(other));
		LargeInteger d = vals[0];
		LargeInteger x = vals[2];
		LargeInteger y = vals[1].subtract((this.divide(other)[0]).multiply(x));

		return new LargeInteger[] {d, x, y};
	 }

  /**
 	* Compute the result of raising this to the power of y mod n
 	* @param y exponent to raise this to
 	* @param n modulus value to use
 	* @return this^y mod n
 	*/
	 public LargeInteger modularExp(LargeInteger y, LargeInteger n){
		// YOUR CODE HERE (replace the return, too...)
		LargeInteger result = new LargeInteger(ONE);
		byte[] copy = y.getVal();
		for(int i = 0; i < copy.length; i++){
			for(int j = 7; j >= 0; j--){
				result = result.multiply(result).mod(n);
				int bit = (copy[i] >> j) & 1;
				if(bit == 1){
					result = this.multiply(result).mod(n);
				}
			}
		}
		return result.dePad();
	 }

	 /**
	 * WARNING Use for debugging only, will overflow for large LIs
	 * @return decimal equivalent of LargeInteger
	 */
	 public double toDecimal(){
		 double result = 0;
		 int place = 0;
		 int MSB = (val.length * 8) - 1;
		 for(int i = val.length - 1; i >= 0; i--){
			 for(int j = 7; j >= 0; j--){
				 if(byteToBitArr(val[i])[j] == 1){
					 double cur = Math.pow(2,place);
					 if(place == MSB)
					 	result -= cur;
					 else
					 	result += cur;
				 }
				 place++;
			 }
		 }
		 return result;
	 }

	 /**
	 * Get the total number of bits (including padding that hasn't been deleted)
	 * @return int bits
	 */
	 public int getBits(){
		 return this.length() * 8;
	 }
	 /**
	 * toString to print out single byte
	 * @return a String of the byte with leading 0s
	 */
	 public String byteToString(byte b){
			 return Integer.toBinaryString((b & 0xFF) + 0x100).substring(1);
	 }

	 /**
	 * toString to print out a byte array
	 * @param arr the byte array to be printed
	 * @return String of the byte array
	 */
	 public String byteArrToString(byte[] arr){
	 	String s = "";
		for(int i = 0; i < arr.length; i++){
			String addition = Integer.toBinaryString((arr[i] & 0xFF) + 0x100).substring(1);
			s +=  addition + " ";
		 }
		return s;
	 }

	 /**
	 * toString to print out byte array val
	 * @return a String of bits with all leading 0s
	 */
	 @Override
	 public String toString(){
		 String s = "";
		 for(int i = 0; i < val.length; i++){
			 String addition = Integer.toBinaryString((val[i] & 0xFF) + 0x100).substring(1);
			 s +=  addition + " ";
			}
		 return s;
	 }
}
