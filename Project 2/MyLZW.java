/*************************************************************************
*  Compilation:  javac LZW.java
*  Execution:    java LZW - < input.txt   (compress)
*  Execution:    java LZW + < input.txt   (expand)
*  Dependencies: BinaryIn.java BinaryOut.java
*
*  Compress or expand binary input from standard input using LZW.
*
*  WARNING: STARTING WITH ORACLE JAVA 6, UPDATE 7 the SUBSTRING
*  METHOD TAKES TIME AND SPACE LINEAR IN THE SIZE OF THE EXTRACTED
*  SUBSTRING (INSTEAD OF CONSTANT SPACE AND TIME AS IN EARLIER
*  IMPLEMENTATIONS).
*
*  See <a href = "http://java-performance.info/changes-to-string-java-1-7-0_06/">this article</a>
*  for more details.
*
*************************************************************************/

public class MyLZW {
  private static int R = 256;        // number of input chars
  private static int W = 9;         // codeword width
  private static int L = (int) Math.pow(2, W);       // number of codewords = 2^W
  private static char mode = 'n';

  public static void compress() {
    String input = BinaryStdIn.readString();
    TST<Integer> st = new TST<Integer>();
    for (int i = 0; i < R; i++)
    st.put("" + (char) i, i);
    int code = R+1;  // R is codeword for EOF

    BinaryStdOut.write((byte) mode); // write the mode descriptor at the beginning of the file

    int unCompData = 0; // how much data is left to be gone through
    int compData = 0; // how much data has been parsed
    double oldRatio = 0.0; // old compression ratio
    double newRatio = 0.0; // new compression ratio

    while (input.length() > 0) {
      String s = st.longestPrefixOf(input);  // Find max prefix match s.
      BinaryStdOut.write(st.get(s), W);      // Print s's encoding.
      int t = s.length();

      // set values to compare ratios
      unCompData += t * 16; // data read * sizeof char
      compData += W; // data to write is sizeof codeword

      if (t < input.length() && code < L){    // Add s to symbol table.
        st.put(input.substring(0, t + 1), code++);
        oldRatio = (double) unCompData / compData; // ratio for monitor
      }
      else if((t < input.length()) && (code >= L)){ // current width codebook is full
        if(W < 16){ // can add one byte to width
          W++;
          L = (int) Math.pow(2, W);
          st.put(input.substring(0, t + 1), code++);
        }
        else if(W == 16) { // completely full, now look at reset vs. monitor
          if(mode == 'r'){ // reset mode
            // reset the TST
            st = new TST<Integer>();
            for (int i = 0; i < R; i++)
            st.put("" + (char) i, i);
            W = 9;
            L = (int) Math.pow(2, W);
            code = R + 1;
            // add the next codeword
            st.put(input.substring(0, t + 1), code++);
          }
          else if(mode == 'm'){ // monitor mode
            newRatio = (double) unCompData / compData;
            double monitorRatio = oldRatio / newRatio;
            if(monitorRatio > 1.1){ // reset if ratio is above threshold
              st = new TST<Integer>();
              for (int i = 0; i < R; i++)
              st.put("" + (char) i, i);
              W = 9;
              L = (int) Math.pow(2, W);
              code = R + 1;
              // add the next codeword
              st.put(input.substring(0, t + 1), code++);
            }
          }
        }
      }
      input = input.substring(t);            // Scan past s in input.
    }
    BinaryStdOut.write(R, W);
    BinaryStdOut.close();
  }


  public static void expand() {
    String[] st = new String[(int) Math.pow(2, 16)]; //This is the maximum number of codewords there could be
    int q;
    for (q = 0; q < R; q++)
    st[q] = "" + (char) q;
    st[q++] = "";
    int i = R+1; // next available codeword value

    mode = BinaryStdIn.readChar(); //Read in the mode, the first byte of the file


    int codeword = BinaryStdIn.readInt(W);
    if (codeword == R) return;           // expanded message is empty string
    String val = st[codeword];

    int unCompData = 0;
    int compData = 0;
    double oldRatio = 0.0;
    double newRatio = 0.0;

    while (true) {
      // data needs to be expanded back to its length from bits
      unCompData += (val.length() * 16);
      compData += W;
      BinaryStdOut.write(val); // write the codeword

      if(i >= L){ // check to see if the current codeword value is outside of the current codebook
        if(W < 16){ // can expand width
          W++;
          L = (int) Math.pow(2, W);
          oldRatio = (double) unCompData/compData; // set ratio to current data info
        }
        else if(W == 16){ // codebook is full
          if(mode == 'r'){ // reset mode
            st = new String[(int) Math.pow(2, 16)]; // st is now max codewords there could be
            int j;
            for (j = 0; j < R; j++)
              st[j] = "" + (char) j;
            st[j++] = "";
            // reset the codebook to init
            W = 9;
            L = (int) Math.pow(2, W);
            i = R+1;

          }
          else if(mode == 'm'){ // monitor mode
            newRatio = (double) unCompData/compData;
            double monitorRatio = oldRatio/newRatio;
            if(monitorRatio > 1.1){ // reset the codebook if past threshold

              st = new String[(int) Math.pow(2, 16)];  // st is now max codewords there could be
              int j;
              for (j = 0; j < R; j++)
                st[j] = "" + (char) j;
              st[j++] = "";
              // reset the codebook to init
              W = 9;
              L = (int) Math.pow(2, W);
              i = R+1;
              oldRatio = 0.0;
            }
          }
          // else do nothing while in do nothing mode
        }
      }

      codeword = BinaryStdIn.readInt(W);
      if (codeword == R) break;
      String s = st[codeword];

      if (i == codeword) s = val + val.charAt(0);   // special case hack
      if (i < L){
        st[i++] = val + s.charAt(0);
        oldRatio = (double) unCompData/compData;
      }

      val = s;
    }
    BinaryStdOut.close();
  }



  public static void main(String[] args) {
    if(args.length >= 2){
      if(args[1].equals("r")) mode = 'r';
      if(args[1].equals("m")) mode = 'm';
      if(args[1].equals("n")) mode = 'n';
    }
    if      (args[0].equals("-")) compress();
    else if (args[0].equals("+")) expand();
    else throw new IllegalArgumentException("Illegal command line argument");
  }

}
