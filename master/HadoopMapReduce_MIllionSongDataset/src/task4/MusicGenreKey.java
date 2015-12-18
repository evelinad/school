/*
Dumitrescu Evelina
SCPD
Assignment 3
*/

package task4;

import org.apache.hadoop.io.*;

import java.io.*;

import util.Genre;

public class MusicGenreKey implements WritableComparable<MusicGenreKey> {
    public Genre genre;
    public MusicGenreKey(Genre genre) {

        this.genre = genre;
    }

    public MusicGenreKey() {}

    public void readFields(DataInput input) throws IOException {
        genre = Genre.values()[input.readInt()];
    }

    public void write(DataOutput output) throws IOException {
        output.writeInt(genre.ordinal());
    }

    public int compareTo(MusicGenreKey t) {
        if (t.genre.ordinal() > this.genre.ordinal()) {
            return -1;
        }

        if (t.genre.ordinal() < this.genre.ordinal()) {
            return 1;
        }

        return 0;
    }

    public String toString() {
        return genre + "";
    }
}

