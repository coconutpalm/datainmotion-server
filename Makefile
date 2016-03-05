# Makefile making rebuilding/testing faster

ALL: package unpack run

package:
	mvn clean package

unpack: remove-old-server-build
	tar -xzvf target/du-jour-server-1.0.tar.gz

clean: remove-old-server-build
	mvn clean

run:
	du-jour-server-1.0/bin/karaf

remove-old-server-build:
	rm -fr du-jour-server-1.0

