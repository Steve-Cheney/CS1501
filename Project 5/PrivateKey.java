import java.io.Serializable;

// use Serializable to take the key and use it as a object to write and read

public class PrivateKey implements Serializable {
  public LargeInteger n;
  public LargeInteger d;

  public PrivateKey(LargeInteger n, LargeInteger d) {
    this.n = n;
    this.d = d;
  }
}
