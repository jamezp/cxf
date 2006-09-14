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

package org.apache.cxf.systest.dispatch;

import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.Future;

import javax.xml.bind.JAXBContext;
import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.ws.AsyncHandler;
import javax.xml.ws.Dispatch;
import javax.xml.ws.Endpoint;
import javax.xml.ws.Response;
import javax.xml.ws.Service;

import org.xml.sax.InputSource;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.cxf.helpers.XMLUtils;
import org.apache.cxf.systest.common.ClientServerSetupBase;
import org.apache.cxf.systest.common.ClientServerTestBase;
import org.apache.cxf.systest.common.TestServerBase;
import org.apache.hello_world_soap_http.GreeterImpl;
import org.apache.hello_world_soap_http.SOAPService;
import org.apache.hello_world_soap_http.types.GreetMe;
import org.apache.hello_world_soap_http.types.GreetMeResponse;

public class DispatchClientServerTest extends ClientServerTestBase {

    private final QName serviceName = new QName("http://apache.org/hello_world_soap_http",
                                                "SOAPDispatchService");
    private final QName portName = new QName("http://apache.org/hello_world_soap_http", "SoapDispatchPort");

    public static class Server extends TestServerBase {

        protected void run() {
            Object implementor = new GreeterImpl();
            String address = "http://localhost:9006/SOAPDispatchService/SoapDispatchPort";
            Endpoint.publish(address, implementor);

        }

        public static void main(String[] args) {
            try {
                Server s = new Server();
                s.start();
            } catch (Exception ex) {
                ex.printStackTrace();
                System.exit(-1);
            } finally {
                System.out.println("done!");
            }
        }
    }

    public static Test suite() throws Exception {
        TestSuite suite = new TestSuite(DispatchClientServerTest.class);
        return new ClientServerSetupBase(suite) {
            public void startServers() throws Exception {
                assertTrue("server did not launch correctly", launchServer(Server.class));
            }
        };
    }

    public void testSOAPMessage() throws Exception {

        URL wsdl = getClass().getResource("/wsdl/hello_world.wsdl");
        assertNotNull(wsdl);

        SOAPService service = new SOAPService(wsdl, serviceName);
        assertNotNull(service);

        Dispatch<SOAPMessage> disp = service
            .createDispatch(portName, SOAPMessage.class, Service.Mode.MESSAGE);

        // Test request-response
        InputStream is = getClass().getResourceAsStream("resources/GreetMeDocLiteralReq.xml");
        SOAPMessage soapReqMsg = MessageFactory.newInstance().createMessage(null, is);
        assertNotNull(soapReqMsg);
        SOAPMessage soapResMsg = disp.invoke(soapReqMsg);
        assertNotNull(soapResMsg);
        String expected = "Hello TestSOAPInputMessage";
        assertEquals("Response should be : Hello TestSOAPInputMessage", expected, soapResMsg.getSOAPBody()
            .getTextContent());

        // Test oneway
        InputStream is1 = getClass().getResourceAsStream("resources/GreetMeDocLiteralReq1.xml");
        SOAPMessage soapReqMsg1 = MessageFactory.newInstance().createMessage(null, is1);
        assertNotNull(soapReqMsg1);
        disp.invokeOneWay(soapReqMsg1);

        // Test async polling
        InputStream is2 = getClass().getResourceAsStream("resources/GreetMeDocLiteralReq2.xml");
        SOAPMessage soapReqMsg2 = MessageFactory.newInstance().createMessage(null, is2);
        assertNotNull(soapReqMsg2);

        Response response = disp.invokeAsync(soapReqMsg2);
        SOAPMessage soapResMsg2 = (SOAPMessage)response.get();
        assertNotNull(soapResMsg2);
        String expected2 = "Hello TestSOAPInputMessage2";
        assertEquals("Response should be : Hello TestSOAPInputMessage2", expected2, soapResMsg2.getSOAPBody()
            .getTextContent());

        // Test async callback
        InputStream is3 = getClass().getResourceAsStream("resources/GreetMeDocLiteralReq3.xml");
        SOAPMessage soapReqMsg3 = MessageFactory.newInstance().createMessage(null, is3);
        assertNotNull(soapReqMsg3);
        TestSOAPMessageHandler tsmh = new TestSOAPMessageHandler();
        Future f = disp.invokeAsync(soapReqMsg3, tsmh);
        assertNotNull(f);
        while (!f.isDone()) {
            // wait
        }
        String expected3 = "Hello TestSOAPInputMessage3";
        assertEquals("Response should be : Hello TestSOAPInputMessage3", expected3, tsmh.getReplyBuffer());

    }

    public void testDOMSourceMESSAGE() throws Exception {
        URL wsdl = getClass().getResource("/wsdl/hello_world.wsdl");
        assertNotNull(wsdl);

        SOAPService service = new SOAPService(wsdl, serviceName);
        assertNotNull(service);

        InputStream is = getClass().getResourceAsStream("resources/GreetMeDocLiteralReq.xml");
        SOAPMessage soapReqMsg = MessageFactory.newInstance().createMessage(null, is);
        DOMSource domReqMsg = new DOMSource(soapReqMsg.getSOAPPart());
        assertNotNull(domReqMsg);

        Dispatch<DOMSource> disp = service.createDispatch(portName, DOMSource.class, Service.Mode.MESSAGE);
        DOMSource domResMsg = disp.invoke(domReqMsg);
        assertNotNull(domResMsg);
        String expected = "Hello TestSOAPInputMessage";

        assertEquals("Response should be : Hello TestSOAPInputMessage", expected, domResMsg.getNode()
            .getFirstChild().getTextContent());

        // Test invoke oneway
        InputStream is1 = getClass().getResourceAsStream("resources/GreetMeDocLiteralReq1.xml");
        SOAPMessage soapReqMsg1 = MessageFactory.newInstance().createMessage(null, is1);
        DOMSource domReqMsg1 = new DOMSource(soapReqMsg1.getSOAPPart());
        assertNotNull(domReqMsg1);
        disp.invokeOneWay(domReqMsg1);

        // Test async polling
        InputStream is2 = getClass().getResourceAsStream("resources/GreetMeDocLiteralReq2.xml");
        SOAPMessage soapReqMsg2 = MessageFactory.newInstance().createMessage(null, is2);
        DOMSource domReqMsg2 = new DOMSource(soapReqMsg2.getSOAPPart());
        assertNotNull(domReqMsg2);

        Response response = disp.invokeAsync(domReqMsg2);
        DOMSource domRespMsg2 = (DOMSource)response.get();
        assertNotNull(domReqMsg2);
        String expected2 = "Hello TestSOAPInputMessage2";
        assertEquals("Response should be : Hello TestSOAPInputMessage2", expected2, domRespMsg2.getNode()
            .getFirstChild().getTextContent());

        // Test async callback
        InputStream is3 = getClass().getResourceAsStream("resources/GreetMeDocLiteralReq3.xml");
        SOAPMessage soapReqMsg3 = MessageFactory.newInstance().createMessage(null, is3);
        DOMSource domReqMsg3 = new DOMSource(soapReqMsg3.getSOAPPart());
        assertNotNull(domReqMsg3);

        TestDOMSourceHandler tdsh = new TestDOMSourceHandler();
        Future fd = disp.invokeAsync(domReqMsg3, tdsh);
        assertNotNull(fd);
        while (!fd.isDone()) {
            // wait
        }
        String expected3 = "Hello TestSOAPInputMessage3";
        assertEquals("Response should be : Hello TestSOAPInputMessage3", expected3, tdsh.getReplyBuffer());
    }

    public void testDOMSourcePAYLOAD() throws Exception {
        URL wsdl = getClass().getResource("/wsdl/hello_world.wsdl");
        assertNotNull(wsdl);

        SOAPService service = new SOAPService(wsdl, serviceName);
        assertNotNull(service);

        Dispatch<DOMSource> disp = service.createDispatch(portName, DOMSource.class, Service.Mode.PAYLOAD);

        InputStream is = getClass().getResourceAsStream("resources/GreetMeDocLiteralReq.xml");
        SOAPMessage soapReqMsg = MessageFactory.newInstance().createMessage(null, is);
        DOMSource domReqMsg = new DOMSource(soapReqMsg.getSOAPBody().extractContentAsDocument());
        assertNotNull(domReqMsg);

        // invoke
        DOMSource domResMsg = disp.invoke(domReqMsg);
        assertNotNull(domResMsg);
        String expected = "Hello TestSOAPInputMessage";
        assertEquals("Response should be : Hello TestSOAPInputMessage", expected, domResMsg.getNode()
            .getFirstChild().getTextContent());

        InputStream is1 = getClass().getResourceAsStream("resources/GreetMeDocLiteralReq1.xml");
        SOAPMessage soapReqMsg1 = MessageFactory.newInstance().createMessage(null, is1);
        DOMSource domReqMsg1 = new DOMSource(soapReqMsg1.getSOAPBody().extractContentAsDocument());
        assertNotNull(domReqMsg1);
        // invokeOneWay
        disp.invokeOneWay(domReqMsg1);

        InputStream is2 = getClass().getResourceAsStream("resources/GreetMeDocLiteralReq2.xml");
        SOAPMessage soapReqMsg2 = MessageFactory.newInstance().createMessage(null, is2);
        DOMSource domReqMsg2 = new DOMSource(soapReqMsg2.getSOAPBody().extractContentAsDocument());
        assertNotNull(domReqMsg2);
        // invokeAsync
        Response response = disp.invokeAsync(domReqMsg2);
        DOMSource domRespMsg2 = (DOMSource)response.get();
        assertNotNull(domRespMsg2);
        String expected2 = "Hello TestSOAPInputMessage2";
        assertEquals("Response should be : Hello TestSOAPInputMessage2", expected2, domRespMsg2.getNode()
            .getFirstChild().getTextContent());

        InputStream is3 = getClass().getResourceAsStream("resources/GreetMeDocLiteralReq3.xml");
        SOAPMessage soapReqMsg3 = MessageFactory.newInstance().createMessage(null, is3);
        DOMSource domReqMsg3 = new DOMSource(soapReqMsg3.getSOAPBody().extractContentAsDocument());
        assertNotNull(domReqMsg3);
        // invokeAsync with AsyncHandler
        TestDOMSourceHandler tdsh = new TestDOMSourceHandler();
        Future fd = disp.invokeAsync(domReqMsg3, tdsh);
        assertNotNull(fd);
        while (!fd.isDone()) {
            // wait
        }
        String expected3 = "Hello TestSOAPInputMessage3";
        assertEquals("Response should be : Hello TestSOAPInputMessage3", expected3, tdsh.getReplyBuffer());
    }

    public void testJAXBObjectPAYLOAD() throws Exception {
        URL wsdl = getClass().getResource("/wsdl/hello_world.wsdl");
        assertNotNull(wsdl);

        SOAPService service = new SOAPService(wsdl, serviceName);
        assertNotNull(service);

        JAXBContext jc = JAXBContext.newInstance("org.apache.hello_world_soap_http.types");
        Dispatch<Object> disp = service.createDispatch(portName, jc, Service.Mode.PAYLOAD);

        String expected = "Hello Jeeves";
        GreetMe greetMe = new GreetMe();
        greetMe.setRequestType("Jeeves");

        Object response = disp.invoke(greetMe);
        assertNotNull(response);
        String responseValue = ((GreetMeResponse)response).getResponseType();
        assertTrue("Expected string, " + expected, expected.equals(responseValue));

        // Test oneway
        disp.invokeOneWay(greetMe);

        // Test async polling
        Response response2 = disp.invokeAsync(greetMe);
        assertNotNull(response2);
        GreetMeResponse greetMeResponse = (GreetMeResponse)response2.get();
        String responseValue2 = greetMeResponse.getResponseType();
        assertTrue("Expected string, " + expected, expected.equals(responseValue2));

        // Test async callback
        TestJAXBHandler tjbh = new TestJAXBHandler();
        Future fd = disp.invokeAsync(greetMe, tjbh);
        assertNotNull(fd);
        while (!fd.isDone()) {
            // wait
        }
        String responseValue3 = ((GreetMeResponse)tjbh.getResponse()).getResponseType();
        assertTrue("Expected string, " + expected, expected.equals(responseValue3));
    }

    public void testSAXSourceMESSAGE() throws Exception {

        URL wsdl = getClass().getResource("/wsdl/hello_world.wsdl");
        assertNotNull(wsdl);

        SOAPService service = new SOAPService(wsdl, serviceName);
        assertNotNull(service);

        InputStream is = getClass().getResourceAsStream("resources/GreetMeDocLiteralReq.xml");
        InputSource inputSource = new InputSource(is);
        SAXSource saxSourceReq = new SAXSource(inputSource);
        assertNotNull(saxSourceReq);

        Dispatch<SAXSource> disp = service.createDispatch(portName, SAXSource.class, Service.Mode.MESSAGE);
        SAXSource saxSourceResp = disp.invoke(saxSourceReq);
        assertNotNull(saxSourceResp);
        String expected = "Hello TestSOAPInputMessage";
        assertTrue("Expected: " + expected, XMLUtils.toString(saxSourceResp).contains(expected));

        // Test oneway
        InputStream is1 = getClass().getResourceAsStream("resources/GreetMeDocLiteralReq1.xml");
        InputSource inputSource1 = new InputSource(is1);
        SAXSource saxSourceReq1 = new SAXSource(inputSource1);
        assertNotNull(saxSourceReq1);
        disp.invokeOneWay(saxSourceReq1);

        // Test async polling
        InputStream is2 = getClass().getResourceAsStream("resources/GreetMeDocLiteralReq2.xml");
        InputSource inputSource2 = new InputSource(is2);
        SAXSource saxSourceReq2 = new SAXSource(inputSource2);
        assertNotNull(saxSourceReq2);

        Response response = disp.invokeAsync(saxSourceReq2);
        SAXSource saxSourceResp2 = (SAXSource)response.get();
        assertNotNull(saxSourceResp2);
        String expected2 = "Hello TestSOAPInputMessage2";
        assertTrue("Expected: " + expected, XMLUtils.toString(saxSourceResp2).contains(expected2));

        // Test async callback
        InputStream is3 = getClass().getResourceAsStream("resources/GreetMeDocLiteralReq3.xml");
        InputSource inputSource3 = new InputSource(is3);
        SAXSource saxSourceReq3 = new SAXSource(inputSource3);
        assertNotNull(saxSourceReq3);
        TestSAXSourceHandler tssh = new TestSAXSourceHandler();
        Future fd = disp.invokeAsync(saxSourceReq3, tssh);
        assertNotNull(fd);
        while (!fd.isDone()) {
            //wait
        }
        String expected3 = "Hello TestSOAPInputMessage3";
        SAXSource saxSourceResp3 = tssh.getSAXSource();
        assertNotNull(saxSourceResp3);
        assertTrue("Expected: " + expected, XMLUtils.toString(saxSourceResp3).contains(expected3));
    }

    public void testSAXSourcePAYLOAD() throws Exception {

        URL wsdl = getClass().getResource("/wsdl/hello_world.wsdl");
        assertNotNull(wsdl);

        SOAPService service = new SOAPService(wsdl, serviceName);
        assertNotNull(service);

        Dispatch<SAXSource> disp = service.createDispatch(portName, SAXSource.class, Service.Mode.PAYLOAD);

        // Test request-response
        InputStream is = getClass().getResourceAsStream("resources/GreetMeDocLiteralSOAPBodyReq.xml");
        InputSource inputSource = new InputSource(is);
        SAXSource saxSourceReq = new SAXSource(inputSource);
        assertNotNull(saxSourceReq);
        SAXSource saxSourceResp = disp.invoke(saxSourceReq);
        assertNotNull(saxSourceResp);
        String expected = "Hello TestSOAPInputMessage";
        assertTrue("Expected: " + expected, XMLUtils.toString(saxSourceResp).contains(expected));

        // Test oneway
        InputStream is1 = getClass().getResourceAsStream("resources/GreetMeDocLiteralSOAPBodyReq1.xml");
        InputSource inputSource1 = new InputSource(is1);
        SAXSource saxSourceReq1 = new SAXSource(inputSource1);
        assertNotNull(saxSourceReq1);
        disp.invokeOneWay(saxSourceReq1);

        // Test async polling
        InputStream is2 = getClass().getResourceAsStream("resources/GreetMeDocLiteralSOAPBodyReq2.xml");
        InputSource inputSource2 = new InputSource(is2);
        SAXSource saxSourceReq2 = new SAXSource(inputSource2);
        assertNotNull(saxSourceReq2);
        Response response = disp.invokeAsync(saxSourceReq2);
        SAXSource saxSourceResp2 = (SAXSource)response.get();
        assertNotNull(saxSourceResp2);
        String expected2 = "Hello TestSOAPInputMessage2";
        assertTrue("Expected: " + expected, XMLUtils.toString(saxSourceResp2).contains(expected2));

        // Test async callback
        InputStream is3 = getClass().getResourceAsStream("resources/GreetMeDocLiteralSOAPBodyReq3.xml");
        InputSource inputSource3 = new InputSource(is3);
        SAXSource saxSourceReq3 = new SAXSource(inputSource3);
        assertNotNull(saxSourceReq3);

        TestSAXSourceHandler tssh = new TestSAXSourceHandler();
        Future fd = disp.invokeAsync(saxSourceReq3, tssh);
        assertNotNull(fd);
        while (!fd.isDone()) {
            //wait
        }
        String expected3 = "Hello TestSOAPInputMessage3";
        SAXSource saxSourceResp3 = tssh.getSAXSource();
        assertNotNull(saxSourceResp3);
        assertTrue("Expected: " + expected, XMLUtils.toString(saxSourceResp3).contains(expected3));
    }

    public void testStreamSourceMESSAGE() throws Exception {
        URL wsdl = getClass().getResource("/wsdl/hello_world.wsdl");
        assertNotNull(wsdl);

        SOAPService service = new SOAPService(wsdl, serviceName);
        assertNotNull(service);
        Dispatch<StreamSource> disp = service.createDispatch(portName, StreamSource.class,
                                                             Service.Mode.MESSAGE);

        InputStream is = getClass().getResourceAsStream("resources/GreetMeDocLiteralReq.xml");
        StreamSource streamSourceReq = new StreamSource(is);
        assertNotNull(streamSourceReq);
        StreamSource streamSourceResp = disp.invoke(streamSourceReq);
        assertNotNull(streamSourceResp);
        String expected = "Hello TestSOAPInputMessage";
        assertTrue("Expected: " + expected, XMLUtils.toString(streamSourceResp).contains(expected));

        InputStream is1 = getClass().getResourceAsStream("resources/GreetMeDocLiteralReq1.xml");
        StreamSource streamSourceReq1 = new StreamSource(is1);
        assertNotNull(streamSourceReq1);
        disp.invokeOneWay(streamSourceReq1);

        InputStream is2 = getClass().getResourceAsStream("resources/GreetMeDocLiteralReq2.xml");
        StreamSource streamSourceReq2 = new StreamSource(is2);
        assertNotNull(streamSourceReq2);
        Response response = disp.invokeAsync(streamSourceReq2);
        StreamSource streamSourceResp2 = (StreamSource)response.get();
        assertNotNull(streamSourceResp2);
        String expected2 = "Hello TestSOAPInputMessage2";
        assertTrue("Expected: " + expected, XMLUtils.toString(streamSourceResp2).contains(expected2));

        InputStream is3 = getClass().getResourceAsStream("resources/GreetMeDocLiteralReq3.xml");
        StreamSource streamSourceReq3 = new StreamSource(is3);
        assertNotNull(streamSourceReq3);
        TestStreamSourceHandler tssh = new TestStreamSourceHandler();
        Future fd = disp.invokeAsync(streamSourceReq3, tssh);
        assertNotNull(fd);
        while (!fd.isDone()) {
            //wait
        }
        String expected3 = "Hello TestSOAPInputMessage3";
        StreamSource streamSourceResp3 = tssh.getStreamSource();
        assertNotNull(streamSourceResp3);
        assertTrue("Expected: " + expected, XMLUtils.toString(streamSourceResp3).contains(expected3));
    }

    public void testStreamSourcePAYLOAD() throws Exception {

        URL wsdl = getClass().getResource("/wsdl/hello_world.wsdl");
        assertNotNull(wsdl);

        SOAPService service = new SOAPService(wsdl, serviceName);
        assertNotNull(service);
        Dispatch<StreamSource> disp = service.createDispatch(portName, StreamSource.class,
                                                             Service.Mode.PAYLOAD);

        InputStream is = getClass().getResourceAsStream("resources/GreetMeDocLiteralSOAPBodyReq.xml");
        StreamSource streamSourceReq = new StreamSource(is);
        assertNotNull(streamSourceReq);
        StreamSource streamSourceResp = disp.invoke(streamSourceReq);
        assertNotNull(streamSourceResp);
        String expected = "Hello TestSOAPInputMessage";
        assertTrue("Expected: " + expected, XMLUtils.toString(streamSourceResp).contains(expected));

        InputStream is1 = getClass().getResourceAsStream("resources/GreetMeDocLiteralSOAPBodyReq1.xml");
        StreamSource streamSourceReq1 = new StreamSource(is1);
        assertNotNull(streamSourceReq1);
        disp.invokeOneWay(streamSourceReq1);

        // Test async polling
        InputStream is2 = getClass().getResourceAsStream("resources/GreetMeDocLiteralSOAPBodyReq2.xml");
        StreamSource streamSourceReq2 = new StreamSource(is2);
        assertNotNull(streamSourceReq2);
        Response response = disp.invokeAsync(streamSourceReq2);
        StreamSource streamSourceResp2 = (StreamSource)response.get();
        assertNotNull(streamSourceResp2);
        String expected2 = "Hello TestSOAPInputMessage2";
        assertTrue("Expected: " + expected, XMLUtils.toString(streamSourceResp2).contains(expected2));

        // Test async callback
        InputStream is3 = getClass().getResourceAsStream("resources/GreetMeDocLiteralSOAPBodyReq3.xml");
        StreamSource streamSourceReq3 = new StreamSource(is3);
        assertNotNull(streamSourceReq3);

        TestStreamSourceHandler tssh = new TestStreamSourceHandler();
        Future fd = disp.invokeAsync(streamSourceReq3, tssh);
        assertNotNull(fd);
        while (!fd.isDone()) {
            //wait
        }
        String expected3 = "Hello TestSOAPInputMessage3";
        StreamSource streamSourceResp3 = tssh.getStreamSource();
        assertNotNull(streamSourceResp3);
        assertTrue("Expected: " + expected, XMLUtils.toString(streamSourceResp3).contains(expected3));
    }

    class TestSOAPMessageHandler implements AsyncHandler<SOAPMessage> {

        String replyBuffer;

        public void handleResponse(Response<SOAPMessage> response) {
            try {
                SOAPMessage reply = response.get();
                replyBuffer = reply.getSOAPBody().getTextContent();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public String getReplyBuffer() {
            return replyBuffer;
        }
    }

    class TestDOMSourceHandler implements AsyncHandler<DOMSource> {

        String replyBuffer;

        public void handleResponse(Response<DOMSource> response) {
            try {
                DOMSource reply = response.get();
                replyBuffer = reply.getNode().getFirstChild().getTextContent();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public String getReplyBuffer() {
            return replyBuffer;
        }
    }

    class TestJAXBHandler implements AsyncHandler<Object> {

        Object reply;

        public void handleResponse(Response<Object> response) {
            try {
                reply = response.get();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public Object getResponse() {
            return reply;
        }
    }

    class TestSAXSourceHandler implements AsyncHandler<SAXSource> {

        SAXSource reply;

        public void handleResponse(Response<SAXSource> response) {
            try {
                reply = response.get();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public SAXSource getSAXSource() {
            return reply;
        }
    }

    class TestStreamSourceHandler implements AsyncHandler<StreamSource> {

        StreamSource reply;

        public void handleResponse(Response<StreamSource> response) {
            try {
                reply = response.get();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public StreamSource getStreamSource() {
            return reply;
        }
    }
}
