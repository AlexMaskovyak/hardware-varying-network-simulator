package JAXBTests;

import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Test Node class.  Contains references to nodes to which it is connected.
 * 
 * @author Alex Maskovyak
 *
 */
/**
 * <p>Java class for node complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="node">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="nodes" type="{}node" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "node", propOrder = {
    "nodes"
})
@XmlRootElement()
public class Node {

	/** nodes to which we are connected. */
	@XmlElement(nillable = true)
    public Set<Node> nodes;
	
	/**
	 * Default constructor.
	 */
	public Node() {
		nodes = new HashSet<Node>();
	}
	
	/**
	 * Adds a node to our set of connections.
	 * @param node to which to connect.
	 * @return true if the node was connected successfully, false otherwise.
	 */
	public boolean connect(Node node) {
		return nodes.add(node);
	}
	
	/**
	 * Removes a node from our set of connections.
	 * @param node from which to disconnect.
	 * @return true if the node was disconnected successfully, false otherwise.
	 */
	public boolean disconnect(Node node) {
		return nodes.remove(node);
	}
}
