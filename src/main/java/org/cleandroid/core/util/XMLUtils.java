/* 
 * Cleandroid Framework 
 * @author: Douraid Arfaoui <douraid.arfaoui@gmail.com>
 *
 * Copyright (c) 2015, Douraid Arfaoui, or third-party contributors as 
 * indicated by the @author tags or express copyright attribution 
 * statements applied by the authors. 
 * 
 * This copyrighted material is made available to anyone wishing to use, modify, 
 * copy, or redistribute it subject to the terms and conditions of the Apache 2 
 * License, as published by the Apache Software Foundation.  
 * 
 */ 
package org.cleandroid.core.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;



import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public class XMLUtils{
	
	private DocumentBuilderFactory factory;
	private DocumentBuilder parser;
	
	
	public XMLUtils() throws ParserConfigurationException{
		factory = DocumentBuilderFactory.newInstance();
		parser = factory.newDocumentBuilder();
	}
	
	public Document createDocument(String rootElement) throws ParserConfigurationException{
		Document doc = factory.newDocumentBuilder().newDocument();
		doc.appendChild(doc.createElement(rootElement));
		return doc;
	}
	
	public Document parseFromString(String content) throws SAXException, IOException{
		Document document = parser.parse(content);
		document.normalize();
		return document;
	}
	
	
	public Document parseFromInputStream(InputStream is) throws SAXException, IOException{
		Document document = parser.parse(is);
		document.normalize();
		return document;
	}
	
	public static List<Node> getDirectChilds(Node node){
		List<Node> directChilds = new ArrayList<Node>();
		
		for(int i=0;i<node.getChildNodes().getLength();i++){
			if(node.getChildNodes().item(i).getNodeType()==Node.ELEMENT_NODE)
				directChilds.add(node.getChildNodes().item(i));
		}
		
		return directChilds;
	}
}