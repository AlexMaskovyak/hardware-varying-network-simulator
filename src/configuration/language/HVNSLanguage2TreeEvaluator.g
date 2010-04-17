tree grammar HVNSLanguage2TreeEvaluator;

options {
  language = Java;
  tokenVocab = HVNSLanguage2;
  ASTLabelType = CommonTree;
}

tokens {
  	JAVA_ASSIGN;
}

@header {
  package configuration.language;
  import java.util.Map;
  import java.util.HashMap;
  import java.util.TreeMap;
  import simulation.simulator.DESimulator;
  import network.entities.Node;
  import network.entities.PublicCloneable;
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
		Object value = variableMap.get( name );
		if( value == null ) {
			throw new RuntimeException( 
				String.format( "Variable \"\%s\" is not set.", name ) );
		}
		return value;
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
					"Cannot convert \"\%s\" to a double.", 
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
			if( object instanceof PublicCloneable ) {
				Object result = ((PublicCloneable)object).clone();
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
}

script returns [ Object result ]
	:	e=statement* EOF { $result = e; }
	;

statement returns [ Object result ]
	:	e=assign { $result = e; }
	;

assign returns [ Object result ]
	:	^(ASSIGN NAME v=value) {
			variableMap.put( $NAME.text, $v.result );
			$result = v;
		}
	;


value returns [ Object result ]
	:	^(JAVA_INSTANTIATE NAME) { $result = instantiate( $NAME ); }
	|	^(CLONE NAME) { $result = clone( $NAME ); }
	|	NUMBER { $result = toDouble( $NUMBER ); }
	|	NAME { $result = getVariable( $NAME ); }
	;
	

/*evaluator returns [ DESimulator result ]
	:	
	;
*/
/*assignment
	:	 ^(':=' IDENT e=expression)
		{ variables.put( $IDENT.text, e ); }
	;
*/
//singleAssignmentDecl returns [ String name ]
//	:	'var' IDENTIFIER { $name=$IDENTIFIER.text; }
//	;
	
//arrayAssignmentDecl returns [ String name ]
//	:	'var' IDENTIFIER '[' INTEGER '..' INTEGER ']' { $name=$IDENTIFIER.text; }
//	;
//	

	

