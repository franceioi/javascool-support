all:
	make import
	make compile

import:
	rm -rf /tmp/java-scanner
	git clone https://github.com/franceioi/java-scanner /tmp/java-scanner
	cp /tmp/java-scanner/src/Scanner.java .

compile:
	gcj --encoding=utf8 --main=FranceIOIJvs2Java -o FranceIOIJvs2Java ../src/trans/FranceIOIJvs2Java.java	

	./FranceIOIJvs2Java Main.jvs Main.java Stdout Stdin Misc

	gcj --encoding=utf8 --main=Main -o Main Main.java Scanner.java ../src/lib/*.java

	./Main < 01.in

clean:
	rm -f Scanner.java FranceIOIJvs2Java Main.java Main
