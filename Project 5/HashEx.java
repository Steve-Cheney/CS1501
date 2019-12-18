import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.security.MessageDigest;

public class HashEx {

	/**
	* Take Hash of a file and convert to a LargeInteger
	* @param file the file name
	* @return LargeInteger of the new hash
	*/
	public static LargeInteger createHash(String file){
		// lazily catch all exceptions...
		try {
			// read in the file to hash
			Path path = Paths.get(file);
			byte[] data = Files.readAllBytes(path);

			// create class instance to create SHA-256 hash
			MessageDigest md = MessageDigest.getInstance("SHA-256");

			// process the file
			md.update(data);
			// generate a has of the file
			byte[] digest = md.digest();

			return new LargeInteger(digest);

		} catch(Exception e) {
			System.out.println(e.toString());
			return null;
		}
	}

	public static void main(String args[]) {

		// lazily catch all exceptions...
		try {
			// read in the file to hash
			Path path = Paths.get(args[0]);
			byte[] data = Files.readAllBytes(path);

			// create class instance to create SHA-256 hash
			MessageDigest md = MessageDigest.getInstance("SHA-256");

			// process the file
			md.update(data);
			// generate a has of the file
			byte[] digest = md.digest();

			// print each byte in hex
			for (byte b : digest) {
				System.out.print(String.format("%02x", b));
			}
			System.out.println();
		} catch(Exception e) {
			System.out.println(e.toString());
		}
	}
}
