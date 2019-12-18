import java.io.Serializable;
import java.util.Random;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

class RsaKey{
  public LargeInteger p;
  public LargeInteger q;
  public LargeInteger phi;
  public LargeInteger n;
  public LargeInteger e;
  public LargeInteger d;

  private final byte[] ONE = {(byte) 1};
  private LargeInteger LI1 = new LargeInteger(ONE);


  /**
  * Create a new random RsaKey object using the Random LargeInteger method
  * @param rnd a random object used for LargeInteger(n,rnd)
  */
  public RsaKey(Random rnd){

    // n = 256 to create the 512 bit key needed
    // p and q are two random coprime numbers
    p = new LargeInteger(256, rnd);
    q = new LargeInteger(256, rnd);
    n = p.multiply(q);

    // generate phi, (p-1) * (q-1)
    LargeInteger p1 = p.subtract(LI1);
    LargeInteger q1 = q.subtract(LI1);
    phi = p1.multiply(q1);
    e = new LargeInteger(phi.length()-1, rnd);

    // XGCD method of computing needed values
    LargeInteger[] gcd = phi.XGCD(e);
    // compute e so that it's coprime (GCD of 1)
    while(gcd[0].compareTo(LI1) != 0 || gcd[2].isNegative()) {
      e = new LargeInteger(phi.length(), rnd);
      gcd = phi.XGCD(e);
    }
    d = gcd[2].mod(phi);
  }

  public PublicKey getPublicKey(){
      return new PublicKey(n, e);
  }

  public PrivateKey getPrivateKey(){
      return new PrivateKey(n, d);
  }
  /**
  * Print info about the key's properties
  */
  public void info(){
    System.out.println("Key:");
    System.out.println("n:");
    System.out.println(n);
    System.out.println("p:");
    System.out.println(p);
    System.out.println("q:");
    System.out.println(q);
    System.out.println("phi:");
    System.out.println(phi);
    System.out.println("e:");
    System.out.println(e);
    System.out.println("d:");
    System.out.println(d);
  }
}


public class RsaKeyGen{
  public static void main(String[] args){
    Random rnd = new Random();
    RsaKey key = new RsaKey(rnd);

    //key.info();
    try{
      FileOutputStream fileout = new FileOutputStream("pubkey.rsa");
      ObjectOutputStream objectout = new ObjectOutputStream(fileout);

      objectout.writeObject(key.getPublicKey());
      objectout.close();

      fileout = new FileOutputStream("privkey.rsa");
      objectout = new ObjectOutputStream(fileout);

      objectout.writeObject(key.getPrivateKey());
      objectout.close();
    }
    catch(Exception e){
      System.out.println("\nSomething unexpected happened while generating keys.\n");
      System.exit(1);
    }
  }
}
