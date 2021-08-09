import java.util.concurrent.ForkJoinPool;

public class Main {
    final static int threads = Runtime.getRuntime().availableProcessors();
    private static final Writer writer = new Writer();

    public static void main(String[] args) {
        //"https://lenta.ru"
        String siteURL = "https://skillbox.ru";
        Node root = new Node(siteURL);
        Node node = new ForkJoinPool(threads).invoke(new SiteParser(root));
        write(node);
    }

    private static void write(Node node) {
        writer.getWriting(node.toString());
    }
}