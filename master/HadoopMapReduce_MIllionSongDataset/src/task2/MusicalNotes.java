/*
Dumitrescu Evelina
SCPD
Assignment 3
*/

package task2;

import java.io.*;

import org.apache.hadoop.io.*;

public class MusicalNotes implements Writable {
    float minorLoudness;
    float minorTempo;
    int minorCount;
    float majorLoudness;
    float majorTempo;
    int majorCount;

    public void write(DataOutput output) throws IOException {
        output.writeFloat(majorLoudness);
        output.writeFloat(majorTempo);
        output.writeInt(majorCount);
        output.writeFloat(minorLoudness);
        output.writeFloat(minorTempo);
        output.writeInt(minorCount);
    }

    public void readFields(DataInput input) throws IOException {
        minorLoudness = input.readFloat();
        minorTempo = input.readFloat();
        minorCount = input.readInt();
        majorLoudness = input.readFloat();
        majorTempo = input.readFloat();
        majorCount = input.readInt();
    }

    public String toString() {
        return " " + majorLoudness + " " + majorTempo + " " + minorLoudness + " " + minorTempo;
    }
}

