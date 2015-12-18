/*
Dumitrescu Evelina
SCPD
Assignment 3
*/

package task3;

import java.io.*;

import org.apache.hadoop.io.Writable;

public class MusicalNotes implements Writable {
    float loudness;
    float tempo;
    int count;

    public void write(DataOutput output) throws IOException {
        output.writeFloat(loudness);
        output.writeFloat(tempo);
        output.writeInt(count);
    }

    public void readFields(DataInput input) throws IOException {
        loudness = input.readFloat();
        tempo = input.readFloat();
        count = input.readInt();
    }

    public String toString() {
        return " " + loudness + " " + tempo + " " + count;
    }
}


