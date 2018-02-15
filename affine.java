import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.PriorityQueue;

/**
 * An affine cypher program. Can encrypt, decrypt, and 
 * decipher plain text documents. 
 * 
 * @author Ben Bole
 * @version v3 2/10/18
 * 
 * All the code except euclideanAlgorithm(int, int) was 
 * written by myself.
 *
 */
public class affine {
	
	/**
	 * Main of the program
	 * @param args = command line arguments
	 */
    public static void main(String[] args) {
		
		
        if (args[0].equals("encrypt")) {
			
			
		    File plainText = new File(args[1]);
		    File outputTo = new File(args[2]);
		    int keyA = Integer.parseInt(args[3]);
		    int keyB = Integer.parseInt(args[4]);
			encrypt(plainText, outputTo, keyA, keyB);
			
			
		} else if (args[0].equals("decrypt")) {
		    
		    
		    File encryptText = new File(args[1]);
		    File outputTo = new File(args[2]);
		    int keyA = Integer.parseInt(args[3]);
		    int keyB = Integer.parseInt(args[4]);
		    decrypt(encryptText, outputTo, keyA, keyB, false);
			
			
			
		} else if (args[0].equals("decipher")) {
			
			File encryptText = new File(args[1]);
			File outputTo = new File(args[2]);
			File dictionary = new File(args[3]);
			decipher(encryptText, outputTo, dictionary);
			
			
			
			
		}
	}
    
    /**
     * Takes a plain text file and encrypts the message to
     * the outputTo file. This is done with the help of the 
     * keyA and keyB. If keyA is not relatively prime with 128
     * the encryption process cannot work and an error statement is
     * printed.  
     * 
     * Formula:
     * 
     * encryption = ((int(message) * a) + b) % 128
     * 
     * 
     * @param plainText = text to encrypt
     * @param outputTo = where to put encryption
     * @param keyA = key to encrypt with
     * @param keyB = another key to encrypt with
     */
    private static void encrypt(File plainText, File outputTo, int keyA, int keyB) {
        
        
        int gcd = euclideanAlgorithm(128, keyA)[0];
        
        int intK = keyB / gcd;
        double doubleK = (double) keyB / (double) gcd;
        
        
        
        //If intK and doubleK are not the same then that means !(b|gcd)
        //This means the keys are invalid
        if (intK != doubleK) {
            
            System.out.println("The key pair ({" + keyA + "},{ "
                    + keyB + "}) is invalid, please select another key.");
        } else {
            try {
                FileReader fr = new FileReader(plainText);
                BufferedReader br = new BufferedReader(fr);
                FileWriter fw = new FileWriter(outputTo);
                String curLine;
            
                //Loop encrypts
                while((curLine = br.readLine()) != null) {
                    curLine += "\n";
                 
                    char[] curChars = curLine.toCharArray();
                
                    for(char c : curChars) {
                    
                        int curAcii = (int) c;
                        int encyrpt = ((curAcii * keyA) + keyB) % 128;
                        char encryptChar = (char) encyrpt;
                        fw.write(encryptChar);
                    }
                }
            
                fr.close();
                br.close();
                fw.close();
                
                
            } catch(IOException e) {
                System.err.println("An error has occured in FileIO");
            }
        }
    }
    
    /**
     * Takes an encrypted text file in encryptText 
     * and decrypts to outputTo using the two keys keyA
     * and keyB. If keyA is not relativley prime to 128,
     * the inverse of keyA cannot be produced, and the
     * algorithm will print an error statement.
     * 
     * Formula:
     * 
     * Message = ((int(encryption) * keyA^-1) - keyB) % 128 
     * 
     * @param encryptText
     * @param outputTo
     * @param keyA
     * @param keyB
     */
    private static ArrayList<String> decrypt(File encryptText, File outputTo, int keyA, int keyB, boolean ifReturn) {
        
        
        int[] valuableInfo = euclideanAlgorithm(keyA, 128);
        int intK = keyB / valuableInfo[0];
        double doubleK = (double) keyB / (double) valuableInfo[0];
        ArrayList<String> toReturn = new ArrayList<>();
        
        //If intK and doubleK are not the same then that means !(b|gcd)
        //This means the keys are invalid
        if (intK != doubleK) {
            
            System.err.println("The key pair ({" + keyA + "},{ "
                    + keyB + "}) is invalid, please select another key.");
        } else {
            
            //Loop to find the inverse of keyA
            //At this point we already checked that keyA
            //and 128 are relatively prime, so an inverse
            //for keyA exists
            int keyAInverse = 0;
            for (int i = 0; i < 128; i++) {
                
                if ((i * keyA) % 128 == 1) {
                    
                    keyAInverse = i;
                    break;
                }
            }
            
            try {
                
                FileReader fr = new FileReader(encryptText);
                BufferedReader br = new BufferedReader(fr);
                FileWriter fw = new FileWriter(outputTo);
                String curLine;
                String curWord = "";
                
                while((curLine = br.readLine()) != null) {
                    
                    char[] curChars = curLine.toCharArray();
                
                    for(char c : curChars) {
                    
                        int curAcii = (int) c;
                        int decyrpt = Math.floorMod(((curAcii * keyAInverse) - keyB), 128);
                        char decryptChar = (char) decyrpt;
                        
                        if (!ifReturn) {
                                                
                            fw.write(decryptChar);
                            
                        } else {
                            
                            if (decryptChar == ' ') {
                                toReturn.add(curWord.toLowerCase());
                                curWord = "";
                            } else {
                                
                                curWord += decryptChar;
                                
                            }
                            
                        }
                    }
                }
            
                fr.close();
                br.close();
                fw.close();
                
                
                
            } catch (IOException e) {
                System.err.println("An error has occurred with File IO.");                
            }
            
        }
        
        if(ifReturn) {
            
            return toReturn;
            
        }
        
        return null;
    }
    
    
    
    
    /**
     * Takes a "dictionary" file and puts all of the words into
     * a HashSet, and then returns it.
     * 
     * @param dictionary = dictionary to extract
     * @return a hashset of dictionary Strings
     */
    private static HashSet<String> textToHashMap(File dictionary) {
        
        HashSet<String> dict= new HashSet<>();
        
        
        try {
        
            FileReader fr = new FileReader(dictionary);
            BufferedReader br = new BufferedReader(fr);
        
            String curLine;
            
            while ((curLine = br.readLine()) != null) {
                
                if (!curLine.replaceAll("\\s+","").equals("")) {
                
                    dict.add(curLine.replaceAll("\\s+",""));
                }
            }
            
            fr.close();
            br.close();
            
        } catch (IOException e) {
            
            System.err.println("A problem has occurred with File IO. Please try again.");
            return dict;
        }
        
        return dict;
        
    }
    
    /**
     * Private class used to allow deciphering to use a 
     * priorityQueue, because priorityQueues are awesome
     * and really helpful.
     * 
     * @author Ben Bole
     * @version v1 2/10/18
     *
     */
    private static class DecipherNodes implements Comparable<DecipherNodes> {

        private Integer weight;
        private String possibleSolution;
        private int keyA;
        private int keyB;
        
        /**
         * Constructor 
         * @param possibleSolution = string of the node
         * @param weight = how many right words in the node
         * @param keyA = keyA of the node
         * @param keyB = keyB of the node
         */
        public DecipherNodes(String possibleSolution, Integer weight, int keyA, int keyB) {
            
            this.weight = weight;
            this.possibleSolution = possibleSolution;
            this.keyA = keyA;
            this.keyB = keyB;
            
        }
        
        /**
         * returns weight
         * @return weight
         */
        public Integer getWeight() {
            
            return this.weight;
            
        }

        /**
         * returns possible solution
         * @return possibleSolution
         */
        public String getPossibleSolution() {
            
            return this.possibleSolution;
        }
        
        
        @Override
        public int compareTo(DecipherNodes o) {
            
            return o.getWeight().compareTo(weight);
            
        }
        
        /**
         * returns keyA
         * @return keyA
         */
        public int getkeyA() {
            return this.keyA;
        }
        
        /**
         * returns keyB
         * @return
         */
        public int getKeyB() {
            
            return this.keyB;
            
        }
    }
    
    /**
     * Description of the algorithm: 
     * 
     * 1) First I use textToHashMap to put the dictionary file into a hashSet.
     * 
     * 2) Then I make an arrayList of all of the possible keyA, which are numbers which are
     *    relatively prime to 128.
     * 
     * 3) Then I go loop through all 128 possible keyB and all possible keyA decrypt with
     *    the brute force keys, and see if anything that actually makes sense shows up.
     * 
     * 4) If something makes sense, a counter goes up, and this counter is what is used
     *    in the compareTo method of DecipherNodes to allow me to use a PriorityQueue.
     * 
     * 5) Then once I've gone through all possible keys, I pop the top entry of the PriorityQueue
     *    and wrote the proper stuff to the requested file.
     *    
     * @param encryptText = encrypted text file
     * @param outputTo = where the decipher should be written to
     * @param dictionary = dictionary of valid words
     */
    private static void decipher(File encryptText, File outputTo, File dictionary) {
        
        HashSet<String> dict = textToHashMap(dictionary);
        ArrayList<Integer> validAKeys = new ArrayList<>();
        PriorityQueue<DecipherNodes> possibleSolutions = new PriorityQueue<>();
        
        //Only valid aKeys are relatively prime with 128
        //This loop finds all of them
        for (int i = 0; i < 128; i++) {
            
            if (euclideanAlgorithm(i, 128)[0] == 1) {
                
                validAKeys.add(i);
                
            }
            
        }
        
        //Going through all 127 possible keyB and all the keyA.
        for (int keyB = 0; keyB < 128; keyB++) {
            for (int keyA : validAKeys) {
            
                ArrayList<String> possibleDecrypt = decrypt(encryptText, outputTo, keyA, keyB, true);
                int wordMatchCounter = 0;
                String wordAsAString = "";
                
                for (int i = 0; i < possibleDecrypt.size(); i++) {
                    
                    String curWord = possibleDecrypt.get(i);
                    
                    
                    if (i != possibleDecrypt.size() - 1) {
                        wordAsAString += curWord + " ";
                    }
                    
                    if (dict.contains(curWord)) {
                        wordMatchCounter++;
                    }
                }
                
                DecipherNodes toPut = new DecipherNodes(wordAsAString, wordMatchCounter, keyA, keyB);
                possibleSolutions.add(toPut);
            }
        }
        
        try {
            
            FileWriter fw = new FileWriter(outputTo);
            
            DecipherNodes highestValue = possibleSolutions.poll();
            
            fw.write("Deciphered a-key: " + highestValue.getkeyA() + 
                    "\t\tDeciphered b-key: " + highestValue.getKeyB() + "\n");
            fw.write("DECRYPTED MESSAGE:\n");
            fw.write(highestValue.getPossibleSolution());
            fw.close();
            
        } catch (IOException e) {
            
            System.err.println("A problem has occurred with File IO. Please try again.");
            
        }
    }
    
    
    /**
     * 
     * Runs the extended euclidean algorithm.
     * Professor's code.
     * 
     * @param a = to find gcd and s
     * @param b = to find gcd and t
     * @return an [] consisting of [gcd(a,b), s, t]
     */
    private static int[] euclideanAlgorithm(int a, int b) {
        
        int s = 1;
        int t = 0;
        int u = 0;
        int v = 1;
        
        while (0 < b) {
            
            int q = a/b; 
            int temp1 = b;
            int temp4 = s;
            int temp5 = t;
            
            b = a % b;
            a = temp1;
            s = u;
            t = v;
            u = temp4 - (u*q);
            v = temp5- (v*q);  
        }
        
        int[] toReturn = {a, s, t};
        return toReturn;
    }
}
