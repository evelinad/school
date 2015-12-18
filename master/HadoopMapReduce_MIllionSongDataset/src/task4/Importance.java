/*
Dumitrescu Evelina
SCPD
Assignment 3
*/

package task4;

import java.io.*;
import java.util.*;

import org.apache.hadoop.io.Writable;

import util.Genre;

public class Importance implements Writable {
    public ArrayList<Double> importance;

    public Importance() {
        importance =  new ArrayList<Double>();
        for (int i = 0; i < Genre.values().length; i++) {
            importance.add(0.0);
        }
    }

    public void write(DataOutput output) throws IOException {
        for (Double d : importance) {
            output.writeDouble(d);
        }
    }

    public void readFields(DataInput input) throws IOException {
        for (int i = 0; i < Genre.values().length; i++)
            importance.set(i, input.readDouble());
    }

    public void increase(Importance imp) {
        for (int i = 0; i < Genre.values().length; i++) {
            importance.set(i, importance.get(i) + imp.importance.get(i));
        }
    }

    public String toString() {
        String s = "";
        for (int i = 0; i < Genre.values().length; i++) {
            s += " " + importance.get(i);
        }
        return s;
    }

}


