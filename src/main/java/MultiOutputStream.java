import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MultiOutputStream extends FilterOutputStream {

    private List<OutputStream> streams = new ArrayList<OutputStream>();
    private List<Boolean> autoFlushs = new ArrayList<Boolean>();

    //@author A0134704M-reused
    public MultiOutputStream(OutputStream out) {
        super(out);
        streams.add(out);
        autoFlushs.add(false);
    }

    //@author A0134704M-reused
    public MultiOutputStream(OutputStream out, boolean bAutoFlush) {
        super(out);
        streams.add(out);
        autoFlushs.add(bAutoFlush);
    }

    //@author A0134704M-reused
    public void addOutputStream(OutputStream out) {
        this.addOutputStream(out, false);
    }

    //@author A0134704M-reused
    public void addOutputStream(OutputStream out, boolean bAutoFlush) {
        streams.add(out);
        autoFlushs.add(bAutoFlush);
    }

    //@author A0134704M-reused
    @Override
    public void write(int b) throws IOException {
        for (int i = 0; i < streams.size(); i++) {
            streams.get(i).write(b);
            if (autoFlushs.get(i)) {
                streams.get(i).flush();
            }
        }
    }

    //@author A0134704M-reused
    @Override
    public void write(byte[] data, int offset, int length) throws IOException {
        for (int i = 0; i < streams.size(); i++) {
            streams.get(i).write(data, offset, length);
            if (autoFlushs.get(i)) {
                streams.get(i).flush();
            }
        }
    }

    //@author A0134704M-reused
    @Override
    public void flush() throws IOException {
        Iterator<OutputStream> iterator = streams.iterator();
        while (iterator.hasNext()) {
            OutputStream out = iterator.next();
            out.flush();
        }
    }

    //@author A0134704M-reused
    @Override
    public void close() throws IOException {
        Iterator<OutputStream> iterator = streams.iterator();
        while (iterator.hasNext()) {
            OutputStream out = iterator.next();
            out.close();
        }
    }
}
