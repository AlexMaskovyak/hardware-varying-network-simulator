package JAXBTests;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

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
		JAXBElement<T> doc = (JAXBElement<T>)u.unmarshal(inputStream);
		return doc.getValue();
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
		
		n1.node.add(of.createNodeNode(n2));
		n2.node.add(of.createNodeNode(n1));
		n2.node.add(of.createNodeNode(n3));
		n3.node.add(of.createNodeNode(n2));
		
		NodeGraph graph = of.createNodeGraph();
		graph.node.add(of.createNodeNode(n1));
		graph.node.add(of.createNodeNode(n2));
		graph.node.add(of.createNodeNode(n3));
		
		
		NodeGraphTester tester = new NodeGraphTester();
		tester.marshal(graph, new FileOutputStream(new File("nodetest.o")));
	}
}
