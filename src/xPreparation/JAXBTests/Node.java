//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.3 in JDK 1.6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.12.07 at 03:19:27 PM EST 
//


package xPreparation.JAXBTests;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java class for Node complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Node">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Id" type="{http://www.w3.org/2001/XMLSchema}ID"/>
 *         &lt;element name="Connected" maxOccurs="unbounded">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;attribute name="node" type="{http://www.w3.org/2001/XMLSchema}IDREF" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Node", propOrder = {
    "id",
    "connected"
})
public class Node {

    @XmlElement(name = "Id", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    @XmlSchemaType(name = "ID")
    protected String id;
    @XmlElement(name = "Connected", required = true)
    protected List<Node.Connected> connected;

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }

    /**
     * Gets the value of the connected property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the connected property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getConnected().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Node.Connected }
     * 
     * 
     */
    public List<Node.Connected> getConnected() {
        if (connected == null) {
            connected = new ArrayList<Node.Connected>();
        }
        return this.connected;
    }

    /**
     * Override of equals for content equivalence.
     */
    public boolean equals(Object o) {
    	if(o instanceof Node) {
    		return equals((Node)o);
    	}
    	return false;
    }
    
    /**
     * Delegated to for node content comparison.
     * @param node to compare against.
     * @return true if the two nodes are value equivalent, false otherwise.
     */
    public boolean equals(Node node) {
    	List<Node.Connected> nc1 = getConnected();
    	List<Node.Connected> nc2 = node.getConnected(); 
    	
    	int size1 = nc1.size();
    	int size2 = nc2.size();
    	if( size1 != size2 ) {
    		return false;
    	}
    	    	
    	for(int i = 0; i < size1; ++i) {
    		if(!nc1.get(i).equals(nc2.get(i))) {
    			return false;
    		}
    	}
    	
    	return true;
    }

    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;attribute name="node" type="{http://www.w3.org/2001/XMLSchema}IDREF" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class Connected {

        @XmlAttribute
        @XmlIDREF
        @XmlSchemaType(name = "IDREF")
        protected Node node;

        /**
         * Gets the value of the node property.
         * 
         * @return
         *     possible object is
         *     {@link Object }
         *     
         */
        public Node getNode() {
            return node;
        }

        /**
         * Sets the value of the node property.
         * 
         * @param value
         *     allowed object is
         *     {@link Object }
         *     
         */
        public void setNode(Node value) {
            this.node = value;
        }
        
        /***
         * Override equals for content equivalence.
         */
        public boolean equals(Object o) {
        	if(o instanceof Node.Connected) {
        		return equals((Node.Connected)o);
        	}
        	return false;
        }
        
        /**
         * Determines if two Node.Connected objects have Nodes with the same id.
         * @param connected other node to test for equivalence.
         * @return true if both point to the same node, false otherwise.
         */
        public boolean equals(Node.Connected connected) {
        	return getNode().getId().equals(connected.getNode().getId());
        }
    }
}
