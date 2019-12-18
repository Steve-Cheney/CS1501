import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;

public class RsaSign{
  static String infile;
  static LargeInteger hash;

  /**
  * Create the signed file as "filename.[ext].sig"
  * @param infile the filename
  * @param sig the signature value
  */
  public static void sigFile(String infile, LargeInteger sig){
    byte val[] = sig.getVal();
    try{
      FileOutputStream fileout = new FileOutputStream(infile+".sig");
      fileout.write(val);
      fileout.close();
    }
    catch(Exception e){
      System.out.println("\nSomething unexpected happened when signing.\n");
      System.exit(1);
    }
  }

  /**
  * Read in the signed file
  * @param the filename
  * @return the byte value of the file
  */
  public static LargeInteger readSigFile(String file){
    try{
      Path fileLoc = Paths.get(infile+".sig");
      byte[] val = Files.readAllBytes(fileLoc);
      return new LargeInteger(val);
    }
    catch (Exception e){
      System.out.println("\nCannot find file\n");
      System.exit(1);
      return new LargeInteger(new byte[] {(byte) -1});
    }
  }

  /**
  * Read in the specified key
  * @param the key filename
  * @return the key object (thanks to Serializable and ObjectStream)
  */
  public static Object readKey(String file){
    try{
      ObjectInputStream objectin = new ObjectInputStream(new FileInputStream(file));
      return objectin.readObject();
    }
    catch(Exception e){
      System.out.println("\nCannot find file.\n");
      System.exit(1);
      return new LargeInteger(new byte[] {(byte) -1});
    }
  }

  /**
  * Verify the file's keys match
  */
  public static void verify(){
    PublicKey pub = (PublicKey) readKey("pubkey.rsa");
    LargeInteger sig = readSigFile(infile);
    LargeInteger m = sig.modularExp(pub.e, pub.n);
    boolean check = (hash.subtract(m).isZero());

    if(check){
      System.out.println("\nValid signature.\n");
    }
    else{
      System.out.println("\nInvalid signature.\n");
    }
  }

  /**
  * Sign the file
  */
  public static void sign(){
    PrivateKey priv = (PrivateKey) readKey("privkey.rsa");
    LargeInteger sig = hash.modularExp(priv.d, priv.n);
    sigFile(infile, sig);
    System.out.println("\nFile signed as " + infile + ".sig");
  }

  public static void main(String[] args){
      if(args.length != 2){
          System.out.println("\nWrong number of arguments.\n");
          System.exit(1);
      }
      char arg = args[0].charAt(0);
      infile = args[1];

      hash = HashEx.createHash(infile);
      if(arg == 's')
        sign();
      else if(arg == 'v')
        verify();
      else
        System.out.println("\nUnknown argument.\n");
  }
}
