package org.objectweb.celtix.servicemodel;

import java.util.Iterator;
import java.util.List;

import javax.wsdl.Binding;
import javax.wsdl.BindingInput;
import javax.wsdl.BindingOperation;
import javax.wsdl.BindingOutput;
import javax.wsdl.Definition;
import javax.wsdl.Input;
import javax.wsdl.Message;
import javax.wsdl.Operation;
import javax.wsdl.Output;
import javax.wsdl.Port;
import javax.wsdl.extensions.soap.SOAPBinding;
import javax.wsdl.extensions.soap.SOAPBody;
import javax.wsdl.extensions.soap.SOAPOperation;

public final class WSDLServiceBuilder {
    
    public static final String WSDL_DEFINITION = WSDLServiceBuilder.class.getName() + ".DEFINITION";
    public static final String WSDL_PORT = WSDLServiceBuilder.class.getName() + ".PORT";
    public static final String WSDL_BINDING = WSDLServiceBuilder.class.getName() + ".BINDING";
    
    public static final String WSDL_OPERATION = WSDLServiceBuilder.class.getName() + ".OPERATION";
    public static final String WSDL_BINDING_OPERATION = WSDLServiceBuilder.class.getName()
                                                        + ".BINDING_OPERATION";
    
    private WSDLServiceBuilder() {
        //utility class - never contructed
    }
    
    
    public static Service buildService(Definition def, Port p) {
        Service service = new Service();
        service.setProperty(WSDL_DEFINITION, def);
        service.setProperty(WSDL_PORT, p);
        
        Binding bind = p.getBinding();
        if (bind == null) {
            throw new IllegalArgumentException("WSDL binding cannot be found for " + p.getName());
        }
        service.setProperty(WSDL_BINDING, bind);
        
        service.setTargetNamespace(bind.getPortType().getQName().getNamespaceURI());
        
        
        SOAPBinding soapBinding = getExtensibilityElement(bind.getExtensibilityElements(),
                                                          SOAPBinding.class);
        if (soapBinding != null) {
            service.setDefaultRPC("RPC".equals(soapBinding.getStyle().toUpperCase()));
        }
        
        for (Iterator it = bind.getBindingOperations().iterator(); it.hasNext();) {
            final BindingOperation bindingOperation = (BindingOperation)it.next();
            if (bindingOperation.getOperation() != null) {
                addOperation(service, bindingOperation);
            }
        }
        
        return service;
    }
 
    
    private static void addOperation(Service service, BindingOperation bindingOp) {
        OperationInfo opInfo = service.addOperation(bindingOp.getName());
        opInfo.setProperty(WSDL_BINDING_OPERATION, bindingOp);
        opInfo.setProperty(WSDL_OPERATION, bindingOp.getOperation());
        
        SOAPOperation soapOperation = getExtensibilityElement(bindingOp.getExtensibilityElements(),
                                                              SOAPOperation.class);
        if (soapOperation != null
            && soapOperation.getStyle() != null
            && !"".equals(soapOperation.getStyle())) {
            
            opInfo.setRPC("RPC".equalsIgnoreCase(soapOperation.getStyle()));
        } else {
            opInfo.setRPC(service.isDefaultRPC());
        }
        if (soapOperation != null) {
            String soapAction = soapOperation.getSoapActionURI();
            if (soapAction != null) {
                opInfo.setSOAPAction(soapAction);
            }
        }

        Message msg = getMessage(bindingOp, true);
        SOAPBody body = getSOAPBody(bindingOp, true);
        
        MessageInfo minfo = opInfo.createMessage(msg.getQName());
        opInfo.setInput(minfo);
        if (body != null) {
            minfo.setTargetNamespace(body.getNamespaceURI());
            minfo.setLiteral(!"ENCODED".equalsIgnoreCase(body.getUse()));
        }
        
        msg = getMessage(bindingOp, true);
        if (msg != null) {
            body = getSOAPBody(bindingOp, true);
            minfo = opInfo.createMessage(msg.getQName());
            opInfo.setOutput(minfo);
            if (body != null) {
                minfo.setTargetNamespace(body.getNamespaceURI());
                minfo.setLiteral(!"ENCODED".equalsIgnoreCase(body.getUse()));
            }
        }
        
        //REVISIT - must fill in all the parts, determine wrapped doc/lit, etc...

    }
    
    private static <T> T getExtensibilityElement(List<?> elements, Class<T> type) {
        for (Object element : elements) {
            if (type.isInstance(element)) {
                return type.cast(element);
            }
        }
        return null;
    }
    private static Message getMessage(BindingOperation bindingOp, boolean isInput) {
        Operation operation = bindingOp.getOperation();
        if (operation == null) {
            return null;
        }

        if (isInput) {
            final Input input = operation.getInput();
            return input == null ? null : input.getMessage();
        }
        final Output output = operation.getOutput();
        return output == null ? null : output.getMessage();
    }
    
    private static SOAPBody getSOAPBody(BindingOperation bindingOp, boolean input) {
        List elements = null;
        if (input) {
            BindingInput bindingInput = bindingOp.getBindingInput();
            if (bindingInput == null) {
                return null;
            }
            elements = bindingInput.getExtensibilityElements();
        } else {
            BindingOutput bindingOutput = bindingOp.getBindingOutput();
            if (bindingOutput == null) {
                return null;
            }
            elements = bindingOutput.getExtensibilityElements();
        }
        return getExtensibilityElement(elements, SOAPBody.class);
    }     
}
