package xPreparation.JAXBTests;

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
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.SAXException;

/**
 * Performs JAXB related tests.
 * @author Alex Maskovyak
 *
 */
public class NodeGraphTester {
	
	/** responsible for programmatic object creation. */
	protected ObjectFactory _of;
	
	/** expected output from unmarshalling test, and output for marshalling tests */
	protected NodeGraph _expected;
	
	/**
	 * Default constructor.
	 */
	public NodeGraphTester() {
		_of = new ObjectFactory();
	}
	
	/**
	 * Perform an unmarshalling procedures (Java objects instantiated).
	 * @param <T> Java type to be unmarshalled from the file.
	 * @param docClass JAXB unmarshallable class.
	 * @param inputStream containing the XML.
	 * @return unmarshalled Java object of type T.
	 * @throws JAXBException in the event that a problem occurs.
	 */
	public <T> T unmarshal(Class<T> docClass, InputStream inputStream)
			throws JAXBException, SAXException {
		
		String packageName = docClass.getPackage().getName();
		JAXBContext jc = JAXBContext.newInstance( packageName );
		Unmarshaller u = jc.createUnmarshaller();
		SchemaFactory sf = SchemaFactory.newInstance(javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI);
		Schema schema = sf.newSchema(new StreamSource(ClassLoader.getSystemResourceAsStream("resources/JAXBTestsSchema.xsd")));
		u.setSchema(schema);
		return (T)u.unmarshal(inputStream);
	}
	
	/**
	 * Performs a marshalling procedure (Java objects serialized to XML).
	 * @param jaxbElement to be marshalled to the outputstream.
	 * @param outputStream where to send the marshalled object.
	 * @throws JAXBException in the event that a problem occurs.
	 */
	public void marshal(Object jaxbElement, OutputStream outputStream) 
			throws JAXBException {
		
		String packageName = jaxbElement.getClass().getPackage().getName();
		JAXBContext jc = JAXBContext.newInstance(packageName);
		Marshaller m = jc.createMarshaller();
		m.marshal(jaxbElement, outputStream);
	}
	
	/**
	 * Unmarshalls from a resource and performs a test to ensure that the IDREF/object reference
	 * functionality is working. 
	 */
	public void performUnmarshallingTest() {
		try {
			InputStream inputStream = ClassLoader.getSystemResourceAsStream("resources/JAXBTestsinput.xml");
			NodeGraph unmarshalled = unmarshal(NodeGraph.class, inputStream);
			if( getExpectedNodeGraph().equals(unmarshalled) ) {
				System.out.println("UnmarshallingTest completed successfully.");
			} else {
				System.out.println("UnmarshallingTest completed unsuccessfully.");				
			}
		} catch( Exception e ) {
			e.printStackTrace();
		}
		
	}
	

	
	/**
	 * Marshalls to the file system and then unmarshalls and compares the result.
	 */
	public void performMarshallingRountTripTest() {
		try {
			marshal(getExpectedNodeGraph(), new FileOutputStream(new File("nodetest.o")));
			NodeGraph unmarshalled = unmarshal(NodeGraph.class, new FileInputStream(new File("nodetest.o")));
			if( getExpectedNodeGraph().equals(unmarshalled) ) {
				System.out.println("MarshallingRountTripTest completed successfully.");
			} else {
				System.out.println("MarshallingRountTripTest completed unsuccessfully.");				
			}
		} catch( Exception e ) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Programmatically creates the expected NodeGraph.
	 * @return expected NodeGraph from unmarshalling.
	 */
	protected NodeGraph getExpectedNodeGraph() {
		// build it
		if( _expected == null ) {
			// create nodegraph
			_expected = _of.createNodeGraph();
			
			// create nodes and assign ids
			Node n1 = _of.createNode();
			n1.setId("n1");
			Node n2 = _of.createNode();
			n2.setId("n2");
			Node n3 = _of.createNode();
			n3.setId("n3");
			
			// assign connections to nodes
			Node.Connected temp = _of.createNodeConnected();
			temp.setNode(n2);
			n1.getConnected().add(temp);
			temp = _of.createNodeConnected();
			temp.setNode(n3);
			n1.getConnected().add(temp);
			n2.getConnected().add(temp);
			
			// add them to the graph
			_expected.getNode().add(n1);
			_expected.getNode().add(n2);
			_expected.getNode().add(n3);
		} 
		
		return _expected;
	}
	
	/**
	 * Ensures that JAXB is upholding proper ID restrictions.
	 */
	public void performInvalidIdTest() {
		try {
			unmarshal(NodeGraph.class, ClassLoader.getSystemResourceAsStream("resources/JAXBTestsInputIdFail.xml"));
			System.out.println("Invalid id unmarshalling test completex unsuccessfully.");
		} catch( Exception e ) {
			// the saxparseexception is a linked exception...figure out how to check for that.
			System.out.println("Invalid id unmarshalling test completed successfully.");
		}
	}
	
	/**
	 * Driver for tests.
	 * @param args N/A
	 * @throws JAXBException
	 * @throws FileNotFoundException
	 */
	public static void main(String... args)
		throws JAXBException, FileNotFoundException {

		NodeGraphTester tester = new NodeGraphTester();
		tester.performUnmarshallingTest();
		tester.performMarshallingRountTripTest();
		tester.performInvalidIdTest();
	}
}
