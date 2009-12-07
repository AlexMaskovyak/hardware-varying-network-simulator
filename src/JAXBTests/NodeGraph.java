package JAXBTests;

import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement()
public class NodeGraph {

	public Set<Node> nodes;
	
	public NodeGraph() {
		nodes = new HashSet<Node>();
	}
	
	public boolean add(Node node) {
		return nodes.add(node);
	}
	
	public boolean remove(Node node) {
		return nodes.remove(node);
	}
}
