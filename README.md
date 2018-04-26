# Sample_Code

ENCODING TOOL
(affine.java, decipher.java, dictionary.txt, text.txt, toDecrypt.txt, toWrite.txt)

affine.java is a simple command line utility which uses an affine cypher algorithm to
encrypt plain text files, decrypt plain text files, and decipher encrypted plain
text files.

To encrypt a text file, write "encrypt, file_to_encrypt, file_to_output_to, a-key, b-key".
An a-key must be relatively prime with 128, b-key has no such requirement.
An example with the current files on this repository is as follows:

"encrypt, text.txt, toDecrypt.txt, 1, 52"

To decrypt a text file, write "decrypt, encrypted_text_file, file_to_output_to, a-key, b-key".
A encrypted text file must be decrypted with the same keys it was encrypted with.
An example with the current files on this repository is as follows:

"decrypt, toDecrypt.txt, toWrite.txt, 1, 52"

To decipher a text file, write "decipher, encrypted_text_file, file_to_output_to, dictionary_file".
In this context, a dictionary file is a file which contains possible words and/or digits the decrypted
version of the file could have, all seperated by a newline character.
An example with the current files on this repository is as follows:

"decipher, toDecrypt.txt, decipher.txt, dictionary.txt"

Details on the various algorithms can be found within method comments.

CALCULATOR TOOL
(Evaluator.java)

Evaluator.java is a simple calulator utility which can be used to do addition, subtraction, multiplication, division, and negation operations. Evaluator.java can also store and keep track of variable assignments over-one time operations.

Evaluator.java uses Deterministic Finite Automata and Push Down Automata in order to keep track of current tokens and operations. 

Sample input to Evaluator.java is as follows:

4 3 + (add 4 and 3)
a = 3 (assign the value 3 to the variable 3)
a 7 + (add the value of variable a to 3)
