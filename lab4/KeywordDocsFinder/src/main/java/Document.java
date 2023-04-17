import java.util.ArrayList;
import java.util.List;

public class Document {
    private final String documentName;
    private final List<String> lines;

    public Document(String documentName) {
        this.documentName = documentName;
        this.lines = new ArrayList<>();
    }

    public void addLine(String line) {
        this.lines.add(line);
    }

    public List<String> getLines() {
        return lines;
    }

    public String getDocumentName() {
        return documentName;
    }
}
