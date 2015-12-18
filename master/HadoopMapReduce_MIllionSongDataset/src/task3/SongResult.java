/*
Dumitrescu Evelina
SCPD
Assignment 3
*/

package task3;

public class SongResult {
    public String genre;
    public float loudness;
    public float tempo;
    public int count;

    public SongResult(String genre, float loudness, float tempo, int count) {
        this.genre = genre;
        this.loudness = loudness;
        this.tempo = tempo;
        this.count = count;
    }

    public String toString() {
        return " " + genre + " " + loudness + " " + tempo + " " + count;
    }
}

