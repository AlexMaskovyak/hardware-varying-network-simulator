tree grammar HVNSLanguageTreeEvaluator;

options {
  language = Java;
  tokenVocab = HVNSLanguage;
  ASTLabelType = CommonTree;
}

tokens {
  	JAVA_ASSIGN;
}

@header {
	package configuration.language;
	import java.lang.reflect.Method;
  	import java.util.Map;
  	import java.util.HashMap;
  	import java.util.TreeMap;
  	import simulation.simulator.ISimulator;
  	import simulation.simulator.DESimulator;
  	import simulation.simulator.ComputerNetworkSimulator;
  	import network.entities.INode;
  	import network.entities.Node;
  	import network.entities.IPublicCloneable;
  	import org.progeeks.util.MethodIndex;
}

@members {

	/** hold all variable mappings. */
	protected static Map<String, Object> variableMap = 
		new TreeMap<String, Object>();
	
	/** 
	* Obtains a variable object.
	* @param nameNode containing the name of the variable.
	* @return the object referenced by the name.
	*/
	public static Object getVariable( CommonTree nameNode )  {
		String name = nameNode.getText();
		return getVariable( name ); 
	}
	
	/**
	* Obtains a variable Objct.
	* @param name of the variable to which to obtain a value.
	* @return the object referenced by the name.
	*/
	public static Object getVariable( String name ) {
		Object value = variableMap.get( name );
		if( value == null ) {
			throw new RuntimeException( 
				String.format( "Variable \"\%s\" is not set.", name ) );
		}
		return value;		
	}
	
	/**
	 * Sets a variable name/value.
	 * @param name of the variable.
	 * @param object to assign to this variable name.
	 */
	public static void setVariable( String name, Object object ) {
		variableMap.put( name, object );
	}
	
	/**
	* Creates a double object from the string contained in the node.
	* @param numberNode containing the number value.
	* @return double of the value.
	* @throws RuntimeException if the value could not be converted to a Double.
	*/
	public static Double toDouble( CommonTree numberNode ) {
		Double result = 0.0;
		String value = numberNode.getText();
		try {
			result = Double.parseDouble( value );
	    } catch( NumberFormatException e ) {
			throw new RuntimeException( 
				String.format( "Cannot convert \"\%s\" to a double.", value ) );
	    }
		
		return result;
	}
	
	/**
	* Creates an integer object from the string contained in the node.
	* @param numberNode containing the number value.
	* @return int of the value.
	* @throws RuntimeException if the value could not be converted to a Double.
	*/ 
	public static Integer toInteger( CommonTree numberNode ) {  
		Integer result = 0;
		String value = numberNode.getText();
		try {
			result = Integer.parseInt( value );
	    } catch( NumberFormatException e ) {
			throw new RuntimeException( 
				String.format( "Cannot convert \"\%s\" to a double.", value ) );
	    }
		
		return result;
	}	
	
	/**
	 * Creates an instance of the class name specified.
	 * @param className to instantiate.
	 * @return new instance of the class specified.
	 */
	public static Object instantiate( CommonTree classNameNode ) {
		String className = classNameNode.getText();
		try {
			Class theClass = Class.forName( className );
			Object instance = theClass.newInstance();
			return instance;
		} catch( Exception e ) {
			throw new RuntimeException(
				String.format(
					"Cannot instantiate class named: \"\%s\".", 
					className ) ); 
		}
	}	
	
	/**
	 * Creates an instance of the class name specified.
	 * @param className to instantiate.
	 * @return new instance of the class specified.
	 */
	public Object clone( CommonTree nameNode ) {
		Object object = getVariable( nameNode );
		String name = nameNode.getText();
		try {
			if( object instanceof IPublicCloneable ) {
				Object result = ((IPublicCloneable)object).clone();
				return result;
			} 
			throw new RuntimeException(
				String.format(
					"Clone not supported by \"\%s\".  Invalid operation.", 
					name ) ); 
		} catch( Exception e ) {
			throw new RuntimeException(
				String.format(
					"Clone not supported by \"\%s\".  Invalid operation.", 
					name ) ); 
		}
	}
	
	/**
	 * Gets a list of objects referenced by the name nodes.
	 * @param nameNodes containing names of the variables
	 * @return list of variables.
	 */
	public List<Object> getVariables( List<Object> nameNodes ) {
		List<Object> variables = new ArrayList<Object>();
		for( Object nameNode : nameNodes ) {
			variables.add( getVariable( (CommonTree)nameNode ) );
		}
		return variables;
	}
	
	/**
	 * Gets a list of objects referenced by the name nodes.
	 * @param nameNodes containing names of the variables
	 * @return list of variables.
	 */
	public INode[] getNodes( List<Object> nameNodes ) {
		List<INode> nodes = new ArrayList<INode>();
		for( Object nameNode : nameNodes ) {
			nodes.add( (INode)getVariable( (CommonTree)nameNode ) );
		}
		
		INode[] nodeArray = new INode[ nodes.size() ];
		nodeArray = nodes.toArray( nodeArray );
			
		return nodeArray;
	}
	
	/**
	 *	Gets the simulator variable.
	 *  @return the simulator in use for this configuration file.
	 */
	public ComputerNetworkSimulator getSimulator() {
		return (ComputerNetworkSimulator)getVariable( "simulator" );
	}
	
		/**
	 * Invokes the method of the specified name with the provided parameter.
	 * @param target on which to invoke the method.
	 * @param name of the method to invoke.
	 * @param parameters to pass into the method.  
	 */
	public void invokeObjectMethod( Object target, String name, Object... parameters ) {
		try {
			List<Class> parameterClasses = new ArrayList<Class>();
		
			if( parameters != null ) {
				for( Object parameter : parameters ) {
					parameterClasses.add( parameter.getClass() );
				}
			}
			Class[] classArray = new Class[ parameterClasses.size() ];
			classArray = parameterClasses.toArray( classArray );
			MethodIndex index = MethodIndex.getMethodIndex( target.getClass(), true );
			Method method = index.findMethod( name, classArray );
			//Method method = target.getClass().getMethod( name, classArray );
			method.invoke( target, parameters );
		} catch( Exception e ) {
			e.printStackTrace();
			throw new RuntimeException( 
				String.format( 
					"Cannot invoke method \"\%s\" on type \"\%s\".", 
					name, target.getClass().getName() ) );
		}
	}
}

/** Top-level grouping of statements which are to be evaluated. */
script returns [ ComputerNetworkSimulator simulator ]
	:	statement* EOF { simulator=getSimulator(); }
	;

/** All statements supported by this TreeEvaluator. */
statement returns [ Object result ]
	:	e=assign { $result = e; }		// all assignments
	|	e=expression { $result = e; }	// expression/value statements
	|	e=connect { $result = e; } 		// orders to connect Nodes
	;

/** Handles assignments, including the internal simulator assignment. */
assign returns [ Object result ]
	:	^(ASSIGN NAME v=expression ) {
			setVariable( $NAME.text, $v.result ); 
			$result = v;
		} 
	;

/** Connection requests for groupings of nodes. */
connect returns [ Object result ]
	:	^(SERIES_CONNECT_OP (names+=NAME)+) {
			getSimulator().connectAsSeries( getNodes( $names ) ); 
		}
	| 	^(BUS_CONNECT_OP (names+=NAME)+) { 
			getSimulator().connectAsBus( getNodes( $names ) );  
		}
	| 	^(MESH_CONNECT_OP (names+=NAME)+) { 
			getSimulator().connectAsMesh( getNodes( $names ) );  
		}
	| 	^(RANDOM_CONNECT_OP (names+=NAME)+) { 
			getSimulator().connectRandomly( getNodes( $names ) );  
		}
	| 	^(RING_CONNECT_OP (names+=NAME)+) { 
			getSimulator().connectAsRing( getNodes( $names ) );   
		}
	;

/** All numeric and object related expressions occur here.  */
expression returns [ Object result ]
	scope {
  		List expressionList;
  		List argList;
	}
	@init { $expression::expressionList = new ArrayList(); $expression::argList = new ArrayList(); }	
	:	^('+' op1=expression op2=expression) { try { $result = (Double)op1 + (Double)op2; } catch( Exception exception ) { $result = (Integer)op1 + (Integer)op2;  } } 
	| 	^('-' op1=expression op2=expression) { try { $result = (Double)op1 - (Double)op2; } catch( Exception exception ) { $result = (Integer)op1 - (Integer)op2;  } }
	|   ^('*' op1=expression op2=expression) { try { $result = (Double)op1 * (Double)op2; } catch( Exception exception ) { $result = (Integer)op1 * (Integer)op2;  } }
	|	^('/' op1=expression op2=expression) { try { $result = (Double)op1 / (Double)op2; } catch( Exception exception ) { $result = (Integer)op1 / (Integer)op2;  } }
	|	^('%' op1=expression op2=expression) { try { $result = (Double)op1 \% (Double)op2; } catch( Exception exception ) { $result = (Integer)op1 \% (Integer)op2;  } }
	|	^(NEGATION e=expression) { try { $result = -(Double)e; } catch( Exception exception ) { $result = -(Integer)e; }  }  
	|	obj=object 	{ $result = obj; }
	| 	val=value 	{ $result = val; }
	|	cre=creation{ $result = cre; }
	; 

/** object creation and variable returns. */
value returns [ Object result ] 
	:	NAME	{ $result = getVariable( $NAME ); }
	|	FLOAT 	{ $result = toDouble( $FLOAT ); } 
	|	INTEGER { $result = toInteger( $INTEGER); }
	;
	
/** handles java object instantiation, cloning, and method invocation. */
object returns [ Object result ] 
	:	^(JAVA_INSTANTIATE NAME) { $result = instantiate( $NAME ); }   
	|	^(CLONE NAME) { $result = clone( $NAME ); }	
	|	^(JAVA_INVOKE target=expression ( n=NAME e=expression { $expression::expressionList.add($n.text); $expression::argList.add(e); } )+ ) {
			for( int i = 0 ; i < $expression::expressionList.size(); ++i ) {
				invokeObjectMethod( 
					target, 
					(String)$expression::expressionList.get(i),  
					$expression::argList.get(i) );
			}
			$result = target;
		}
	;

/** Access to factory methods for easy network entity creation. */
creation returns [ Object result ]
	:	^(SIMULATOR_CREATE NODE) { $result = getSimulator().createNode(); }
	|	^(SIMULATOR_CREATE MEDIUM) { $result = getSimulator().createConnectionMedium(); }
	|	^(SIMULATOR_CREATE ADAPTOR) { $result = getSimulator().createAdaptor(); }
	;