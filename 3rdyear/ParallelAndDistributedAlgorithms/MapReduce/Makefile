#!/bin/bash

default: build
build:
	javac Main.java Data.java WorkPool.java Worker.java MapChunk.java MapSolution.java Solution.java
clean:
	 $(RM) *.class
run:
	java Main 2 input.txt output.txt
	cat output.txt

