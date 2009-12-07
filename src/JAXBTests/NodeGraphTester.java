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
		Node n1 = new Node();
		Node n2 = new Node();
		Node n3 = new Node();
		n1.connect(n2);
		//n2.connect(n1);
		n2.connect(n3);
		//n3.connect(n2);
		
		NodeGraphTester tester = new NodeGraphTester();
		tester.marshal(n1, new FileOutputStream(new File("nodetest.o")));
	}
}
