# Makefile making rebuilding/testing faster

SERVER="datainmotion.server"
VERSION="1.0"

ALL: package unpack run

package:
	mvn clean package

unpack: remove-old-server-build
	tar -xzvf target/${SERVER}-${VERSION}.tar.gz

clean: remove-old-server-build
	mvn clean

run:
	${SERVER}-${VERSION}/bin/karaf

remove-old-server-build:
	rm -fr ${SERVER}-${VERSION}

boot:
	curl -fsSLo boot https://github.com/boot-clj/boot-bin/releases/download/latest/boot.sh && chmod 755 boot
