import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.RecursiveTask;

public class SiteParser extends RecursiveTask<Node> {

    private final Node node;
    private final Set<String> nodeNames;

    public SiteParser(Node node) {
        this.node = node;
        nodeNames = Collections.synchronizedSet(new LinkedHashSet<>());
    }

    public SiteParser(Node node, Set<String> nodeNames) {
        this.node = node;
        this.nodeNames = nodeNames;
    }

    @Override
    protected Node compute() {
        List<SiteParser> subTasks = new LinkedList<>();

        for (String link : node.getLinks()) {
            if (nodeNames.contains(link)){
                continue;
            }
            nodeNames.add(link);
            Node child = new Node(link);
            node.addChild(child);
            SiteParser task = new SiteParser(child, nodeNames);
            task.fork();
            subTasks.add(task);
        }

        for (SiteParser sp : subTasks) {
            node.getNodes().add(sp.join());
        }
        return node;
    }
}