package io.github.utplsql.api;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vinicius on 22/04/2017.
 */
public class OutputBufferLines {

    private List<String> bufferLines;
    private boolean isFinished;

    public OutputBufferLines() {
        this.bufferLines = new ArrayList<>();
        this.isFinished = false;
    }

    public List<String> getLines() {
        return this.bufferLines;
    }

    public void add(String s) {
        this.bufferLines.add(s);
    }

    public boolean isFinished() {
        return this.isFinished;
    }

    public void setFinished(boolean isFinished) {
        this.isFinished = isFinished;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(this.bufferLines.size());

        for (String bufferLine : this.bufferLines) {
            sb.append(bufferLine);
            sb.append("\n");
        }

        return sb.toString();
    }
}
