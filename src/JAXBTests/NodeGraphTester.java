package JAXBTests;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

/**
 * Unmarshals an xml file containing a node map.
 * @author Alex Maskovyak
 *
 */
public class NodeGraphTester {
	
	public NodeGraphTester() {
		
	}
	
	public <T> T unmarshal(Class<T> docClass, InputStream inputStream)
		throws JAXBException {
		
		String packageName = docClass.getPackage().getName();
		JAXBContext jc = JAXBContext.newInstance( packageName );
		Unmarshaller u = jc.createUnmarshaller();
		//JAXBElement<T> doc = (JAXBElement<T>)u.unmarshal(inputStream);
		//return doc.getValue();
		return (T)u.unmarshal(inputStream);
	}
	
	public void marshal(Object jaxbElement, OutputStream outputStream) 
		throws JAXBException {
		
		String packageName = jaxbElement.getClass().getPackage().getName();
		JAXBContext jc = JAXBContext.newInstance(packageName);
		Marshaller m = jc.createMarshaller();
		m.marshal(jaxbElement, outputStream);
	}
	
	public static void main(String... args)
		throws JAXBException, FileNotFoundException {
		ObjectFactory of = new ObjectFactory();
		Node n1 = of.createNode();
		n1.setId("1");
		Node n2 = of.createNode();
		n2.setId("2");
		Node n3 = of.createNode();
		n3.setId("3");
		
		n1.connected = new ArrayList<Node.Connected>();
		Node.Connected temp = of.createNodeConnected();
		temp.setNode(n2);
		n1.connected.add(temp);
		n2.connected = new ArrayList<Node.Connected>();
		temp = of.createNodeConnected();
		temp.setNode(n3);
		n2.connected.add(temp);
		
		
		NodeGraph graph = of.createNodeGraph();
		graph.node = new ArrayList<Node>();
		graph.node.add(n1);
		graph.node.add(n2);
		graph.node.add(n3);
		

		
		
		NodeGraphTester tester = new NodeGraphTester();
		tester.marshal(graph, new FileOutputStream(new File("nodetest.o")));
		NodeGraph unmarshalled = tester.unmarshal(NodeGraph.class, new FileInputStream(new File("C:/Users/user/workspaces/gradproject/hardware-varying-network-simulator/nodetestin2.xml")));
		if( unmarshalled.node != null ) {
			System.out.println("not null!");
			if( unmarshalled.node.size() > 0 ) {
				System.out.println(unmarshalled.node.size());
				System.out.println(unmarshalled.node.get(0).getClass());
				Node un1 = unmarshalled.node.get(0);
				Node un2 = unmarshalled.node.get(1);
				Node un3 = unmarshalled.node.get(2);
				
				System.out.println(un1.getId());
				System.out.println(un2.getId());
				System.out.println(un3.getId());
				
				if(un1.connected.get(0).getNode() == un2) {
					System.out.println("HOLY CRAP!");
				}
				if(un1.connected.get(1).getNode() == un3) {
					System.out.println("HOLY CRAP!");
				}
				
				
//				if( ((Node)un1.connected.get(0).getValue()) == un2 ) {
//					System.out.println("HOLY CRAP!");
			}
		}
		tester.marshal(unmarshalled, new FileOutputStream(new File("nodetest.o")));
	}
}
