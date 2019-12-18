import java.util.Arrays;
public class LITest{
  public static void main(String[] args){

    byte[] b1 = {(byte)0b101, (byte)0b00000100};
    byte[] b2 = {(byte)0b110, (byte)0b00101111};
    LargeInteger li1 = new LargeInteger(b1);
    LargeInteger li2 = new LargeInteger(b2);
    LargeInteger li3 = new LargeInteger();

    System.out.println("Li1: " + li1.toString());
    System.out.println("Li2: " + li2.toString());
    //System.out.println("Li2 left shifted " + li2.leftShift(2));
    //System.out.println("Li2 left shifted " + li2.leftShift(2));
    //System.out.println("Li1 negated: " +li1.negate().toString());
    //System.out.println("Li2 negated: " +li2.negate().toString());
    li3 = li1.multiply(li2);
    System.out.println("Li1 * Li2 = " + li3.toString());
    //System.out.println(Arrays.toString(li1.byteToBitArr(li1.getVal()[0])));
  }
}
