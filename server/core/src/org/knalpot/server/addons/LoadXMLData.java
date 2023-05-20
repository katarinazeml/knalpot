package org.knalpot.server.addons;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class LoadXMLData {
    public static Map<Integer, List<Integer>> getData(String file, String tag) {
        Map<Integer, List<Integer>> values = new HashMap<>();

        try {
            File xmlFile = new File(file);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(xmlFile);
            doc.getDocumentElement().normalize();
    
            doc.getDocumentElement();
    
            NodeList nodes = doc.getElementsByTagName(tag);
    
            for (int i = 0; i < nodes.getLength(); i++) {
                Node node = nodes.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element el = (Element) node;
                    List<Integer> coordinates = new ArrayList<>();
                    System.out.println(el.getAttribute("id"));
                    System.out.println(el.getElementsByTagName("x").item(0).getTextContent());
                    System.out.println(el.getElementsByTagName("y").item(0).getTextContent());

                    coordinates.add(Integer.parseInt(el.getElementsByTagName("x").item(0).getTextContent()));
                    coordinates.add(Integer.parseInt(el.getElementsByTagName("y").item(0).getTextContent()));

                    values.put(Integer.parseInt(el.getAttribute("id")), coordinates);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

        return values;
    }
}