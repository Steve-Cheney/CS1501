import java.io.Serializable;

// use Serializable to take the key and use it as a object to write and read

public class PublicKey implements Serializable {
  public LargeInteger n;
  public LargeInteger e;

  public PublicKey(LargeInteger n, LargeInteger e) {
    this.n = n;
    this.e = e;
  }
}
