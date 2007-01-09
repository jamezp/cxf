/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.cxf.wsdl;

import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;

import javax.wsdl.Definition;
import javax.wsdl.WSDLException;
import javax.wsdl.extensions.ExtensibilityElement;
import javax.wsdl.extensions.ExtensionDeserializer;
import javax.wsdl.extensions.ExtensionRegistry;
import javax.wsdl.extensions.ExtensionSerializer;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamWriter;

import org.w3c.dom.Element;

import org.apache.cxf.common.util.PackageUtils;


/**
 * JAXBExtensionHelper
 * @author dkulp
 *
 */
public class JAXBExtensionHelper implements ExtensionSerializer, ExtensionDeserializer {
    final JAXBContext context;
    final Class<? extends TExtensibilityElementImpl> typeClass;
      
    public JAXBExtensionHelper(JAXBContext c, Class<? extends TExtensibilityElementImpl> cls) {
        context = c;
        typeClass = cls;
    }
    
    public static void addExtensions(ExtensionRegistry registry, String parentType, String elementType,
                                     ClassLoader cl) throws JAXBException, ClassNotFoundException {
        Class<?> parentTypeClass = Class.forName(parentType, true, cl);

        Class<? extends TExtensibilityElementImpl> elementTypeClass = Class.forName(elementType, true, cl)
            .asSubclass(TExtensibilityElementImpl.class);
        addExtensions(registry, parentTypeClass, elementTypeClass);
    }
    
    public static void addExtensions(ExtensionRegistry registry,
                                     Class<?> parentType,
                                     Class<? extends TExtensibilityElementImpl> cls) throws JAXBException {
        
        JAXBContext context = JAXBContext.newInstance(PackageUtils.getPackageName(cls), cls.getClassLoader());
        JAXBExtensionHelper helper = new JAXBExtensionHelper(context, cls);
        
        try {
            Class<?> objectFactory = Class.forName(PackageUtils.getPackageName(cls) + ".ObjectFactory");
            Method methods[] = objectFactory.getDeclaredMethods();
            for (Method method : methods) {
                if (method.getParameterTypes().length == 1
                    && method.getParameterTypes()[0].equals(cls)) {
                    
                    XmlElementDecl elementDecl = method.getAnnotation(XmlElementDecl.class);
                    if (null != elementDecl) {
                        QName elementType = new QName(elementDecl.namespace(), elementDecl.name());
                        registry.registerDeserializer(parentType, elementType, helper); 
                        registry.registerSerializer(parentType, elementType, helper);                         
                        registry.mapExtensionTypes(parentType, elementType, cls);                        
                        registry.createExtension(parentType, elementType);
                    }                    
                }
            }        
            
        } catch (WSDLException we) {
            // TODO
            we.printStackTrace();            

        } catch (ClassNotFoundException ex) {
            // TODO
            ex.printStackTrace();            
        }        
    }

    /* (non-Javadoc)
     * @see javax.wsdl.extensions.ExtensionSerializer#marshall(java.lang.Class,
     *  javax.xml.namespace.QName, javax.wsdl.extensions.ExtensibilityElement,
     *   java.io.PrintWriter, javax.wsdl.Definition, javax.wsdl.extensions.ExtensionRegistry)
     */
    public void marshall(Class parent, QName qname, ExtensibilityElement obj, PrintWriter pw,
                         final Definition wsdl, ExtensionRegistry registry) throws WSDLException {
        // TODO Auto-generated method stub
        try {
            Marshaller u = context.createMarshaller();
            u.setProperty("jaxb.encoding", "UTF-8");
            u.setProperty("jaxb.fragment", Boolean.TRUE);
            u.setProperty("jaxb.formatted.output", Boolean.TRUE);
            
            Object mObj = obj;
            
            Class<?> objectFactory = Class.forName(PackageUtils.getPackageName(typeClass) + ".ObjectFactory");
            Method methods[] = objectFactory.getDeclaredMethods();
            for (Method method : methods) {
                if (method.getParameterTypes().length == 1
                    && method.getParameterTypes()[0].equals(typeClass)) {
                    
                    mObj = method.invoke(objectFactory.newInstance(), new Object[] {obj});
                }
            }

            javax.xml.stream.XMLOutputFactory fact = javax.xml.stream.XMLOutputFactory.newInstance();
            XMLStreamWriter writer =
                new PrettyPrintXMLStreamWriter(fact.createXMLStreamWriter(pw), parent);
            writer.setNamespaceContext(new javax.xml.namespace.NamespaceContext() {
                
                public String getNamespaceURI(String arg) {
                    return wsdl.getNamespace(arg);
                }
                                
                public String getPrefix(String arg) {
                    for (Object ent : wsdl.getNamespaces().entrySet()) {
                        Map.Entry entry = (Map.Entry)ent;
                        if (arg.equals(entry.getValue())) {
                            return (String)entry.getKey();
                        }
                    }
                    return null;
                }
                
                public Iterator getPrefixes(String arg) {
                    return wsdl.getNamespaces().keySet().iterator();
                }
            });
            
            u.marshal(mObj, writer);
            writer.flush();            
        } catch (Exception ex) {
            throw new WSDLException(WSDLException.PARSER_ERROR,
                                    "",
                                    ex);
        }

    }

    /* (non-Javadoc)
     * @see javax.wsdl.extensions.ExtensionDeserializer#unmarshall(java.lang.Class,
     *  javax.xml.namespace.QName, org.w3c.dom.Element,
     *   javax.wsdl.Definition,
     *   javax.wsdl.extensions.ExtensionRegistry)
     */
    public ExtensibilityElement unmarshall(Class parent, QName qname, Element element, Definition wsdl,
                                           ExtensionRegistry registry) throws WSDLException {
        try {
            Unmarshaller u = context.createUnmarshaller();
        
            Object o = u.unmarshal(element);
            if (o instanceof JAXBElement<?>) {
                JAXBElement<?> el = (JAXBElement<?>)o;
                o = el.getValue();
            }
            
            ExtensibilityElement el = o instanceof ExtensibilityElement ? (ExtensibilityElement)o : null;
            if (null != el) {
                el.setElementType(qname);
            }
            return el;
        } catch (Exception ex) {
            throw new WSDLException(WSDLException.PARSER_ERROR,
                                    "Error reading element " + qname,
                                    ex);
        }
    }
    
    


    
    

}
