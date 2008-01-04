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

package org.apache.schemas.yoko.idl.parammodes;

import javax.jws.WebParam.Mode;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding;
import javax.jws.WebMethod;
import javax.jws.WebResult;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

/**
 * This class was generated by the Celtix 1.1-SNAPSHOT
 * Mon Sep 18 15:00:01 NDT 2006
 * Generated source version: 1.1-SNAPSHOT
 * 
 */

@WebService(wsdlLocation = "/home/dmiddlem/dev/yoko/trunk/bindings/src/test/resources/wsdl/ParamModes.wsdl", targetNamespace = "http://schemas.apache.org/yoko/idl/ParamModes", name = "ParamModes")

public interface ParamModes {

    @ResponseWrapper(targetNamespace = "http://schemas.apache.org/yoko/idl/ParamModes", className = "org.apache.schemas.yoko.idl.parammodes.TestSingleInParamResponse", localName = "testSingleInParamResponse")
    @RequestWrapper(targetNamespace = "http://schemas.apache.org/yoko/idl/ParamModes", className = "org.apache.schemas.yoko.idl.parammodes.TestSingleInParam", localName = "testSingleInParam")
    @WebMethod(operationName = "testSingleInParam")
    public void testSingleInParam(
        @WebParam(targetNamespace = "http://schemas.apache.org/yoko/idl/ParamModes", name = "p")
        short p
    );

    @ResponseWrapper(targetNamespace = "http://schemas.apache.org/yoko/idl/ParamModes", className = "org.apache.schemas.yoko.idl.parammodes.TestSingleOutParamResponse", localName = "testSingleOutParamResponse")
    @RequestWrapper(targetNamespace = "http://schemas.apache.org/yoko/idl/ParamModes", className = "org.apache.schemas.yoko.idl.parammodes.TestSingleOutParam", localName = "testSingleOutParam")
    @WebResult(targetNamespace = "http://schemas.apache.org/yoko/idl/ParamModes", name = "p")
    @WebMethod(operationName = "testSingleOutParam")
    public short testSingleOutParam();

    @ResponseWrapper(targetNamespace = "http://schemas.apache.org/yoko/idl/ParamModes", className = "org.apache.schemas.yoko.idl.parammodes.TestSingleInOutParamResponse", localName = "testSingleInOutParamResponse")
    @RequestWrapper(targetNamespace = "http://schemas.apache.org/yoko/idl/ParamModes", className = "org.apache.schemas.yoko.idl.parammodes.TestSingleInOutParam", localName = "testSingleInOutParam")
    @WebMethod(operationName = "testSingleInOutParam")
    public void testSingleInOutParam(
        @WebParam(targetNamespace = "http://schemas.apache.org/yoko/idl/ParamModes", mode = Mode.INOUT, name = "p")
        javax.xml.ws.Holder<java.lang.Short> p
    );

    @ResponseWrapper(targetNamespace = "http://schemas.apache.org/yoko/idl/ParamModes", className = "org.apache.schemas.yoko.idl.parammodes.TestMultipleInParamsResponse", localName = "testMultipleInParamsResponse")
    @RequestWrapper(targetNamespace = "http://schemas.apache.org/yoko/idl/ParamModes", className = "org.apache.schemas.yoko.idl.parammodes.TestMultipleInParams", localName = "testMultipleInParams")
    @WebMethod(operationName = "testMultipleInParams")
    public void testMultipleInParams(
        @WebParam(targetNamespace = "http://schemas.apache.org/yoko/idl/ParamModes", name = "p1")
        short p1,
        @WebParam(targetNamespace = "http://schemas.apache.org/yoko/idl/ParamModes", name = "p2")
        short p2
    );

    @ResponseWrapper(targetNamespace = "http://schemas.apache.org/yoko/idl/ParamModes", className = "org.apache.schemas.yoko.idl.parammodes.TestMultipleOutParamsResponse", localName = "testMultipleOutParamsResponse")
    @RequestWrapper(targetNamespace = "http://schemas.apache.org/yoko/idl/ParamModes", className = "org.apache.schemas.yoko.idl.parammodes.TestMultipleOutParams", localName = "testMultipleOutParams")
    @WebMethod(operationName = "testMultipleOutParams")
    public void testMultipleOutParams(
        @WebParam(targetNamespace = "http://schemas.apache.org/yoko/idl/ParamModes", mode = Mode.OUT, name = "p1")
        javax.xml.ws.Holder<java.lang.Short> p1,
        @WebParam(targetNamespace = "http://schemas.apache.org/yoko/idl/ParamModes", mode = Mode.OUT, name = "p2")
        javax.xml.ws.Holder<java.lang.Short> p2
    );

    @ResponseWrapper(targetNamespace = "http://schemas.apache.org/yoko/idl/ParamModes", className = "org.apache.schemas.yoko.idl.parammodes.TestMultipleInOutParamsResponse", localName = "testMultipleInOutParamsResponse")
    @RequestWrapper(targetNamespace = "http://schemas.apache.org/yoko/idl/ParamModes", className = "org.apache.schemas.yoko.idl.parammodes.TestMultipleInOutParams", localName = "testMultipleInOutParams")
    @WebMethod(operationName = "testMultipleInOutParams")
    public void testMultipleInOutParams(
        @WebParam(targetNamespace = "http://schemas.apache.org/yoko/idl/ParamModes", mode = Mode.INOUT, name = "p1")
        javax.xml.ws.Holder<java.lang.Short> p1,
        @WebParam(targetNamespace = "http://schemas.apache.org/yoko/idl/ParamModes", mode = Mode.INOUT, name = "p2")
        javax.xml.ws.Holder<java.lang.Short> p2
    );

    @ResponseWrapper(targetNamespace = "http://schemas.apache.org/yoko/idl/ParamModes", className = "org.apache.schemas.yoko.idl.parammodes.TestMultipleMixedParamsResponse", localName = "testMultipleMixedParamsResponse")
    @RequestWrapper(targetNamespace = "http://schemas.apache.org/yoko/idl/ParamModes", className = "org.apache.schemas.yoko.idl.parammodes.TestMultipleMixedParams", localName = "testMultipleMixedParams")
    @WebMethod(operationName = "testMultipleMixedParams")
    public void testMultipleMixedParams(
        @WebParam(targetNamespace = "http://schemas.apache.org/yoko/idl/ParamModes", name = "p1")
        short p1,
        @WebParam(targetNamespace = "http://schemas.apache.org/yoko/idl/ParamModes", mode = Mode.OUT, name = "p2")
        javax.xml.ws.Holder<java.lang.Short> p2,
        @WebParam(targetNamespace = "http://schemas.apache.org/yoko/idl/ParamModes", mode = Mode.INOUT, name = "p3")
        javax.xml.ws.Holder<java.lang.Short> p3
    );

    @ResponseWrapper(targetNamespace = "http://schemas.apache.org/yoko/idl/ParamModes", className = "org.apache.schemas.yoko.idl.parammodes.TestSingleInParamWithReturnResponse", localName = "testSingleInParamWithReturnResponse")
    @RequestWrapper(targetNamespace = "http://schemas.apache.org/yoko/idl/ParamModes", className = "org.apache.schemas.yoko.idl.parammodes.TestSingleInParamWithReturn", localName = "testSingleInParamWithReturn")
    @WebResult(targetNamespace = "http://schemas.apache.org/yoko/idl/ParamModes", name = "return")
    @WebMethod(operationName = "testSingleInParamWithReturn")
    public short testSingleInParamWithReturn(
        @WebParam(targetNamespace = "http://schemas.apache.org/yoko/idl/ParamModes", name = "p")
        short p
    );

    @ResponseWrapper(targetNamespace = "http://schemas.apache.org/yoko/idl/ParamModes", className = "org.apache.schemas.yoko.idl.parammodes.TestSingleOutParamWithReturnResponse", localName = "testSingleOutParamWithReturnResponse")
    @RequestWrapper(targetNamespace = "http://schemas.apache.org/yoko/idl/ParamModes", className = "org.apache.schemas.yoko.idl.parammodes.TestSingleOutParamWithReturn", localName = "testSingleOutParamWithReturn")
    @WebResult(targetNamespace = "http://schemas.apache.org/yoko/idl/ParamModes", name = "return")
    @WebMethod(operationName = "testSingleOutParamWithReturn")
    public short testSingleOutParamWithReturn(
        @WebParam(targetNamespace = "http://schemas.apache.org/yoko/idl/ParamModes", mode = Mode.OUT, name = "p")
        javax.xml.ws.Holder<java.lang.Short> p
    );

    @ResponseWrapper(targetNamespace = "http://schemas.apache.org/yoko/idl/ParamModes", className = "org.apache.schemas.yoko.idl.parammodes.TestSingleInOutParamWithReturnResponse", localName = "testSingleInOutParamWithReturnResponse")
    @RequestWrapper(targetNamespace = "http://schemas.apache.org/yoko/idl/ParamModes", className = "org.apache.schemas.yoko.idl.parammodes.TestSingleInOutParamWithReturn", localName = "testSingleInOutParamWithReturn")
    @WebResult(targetNamespace = "http://schemas.apache.org/yoko/idl/ParamModes", name = "return")
    @WebMethod(operationName = "testSingleInOutParamWithReturn")
    public short testSingleInOutParamWithReturn(
        @WebParam(targetNamespace = "http://schemas.apache.org/yoko/idl/ParamModes", mode = Mode.INOUT, name = "p")
        javax.xml.ws.Holder<java.lang.Short> p
    );

    @ResponseWrapper(targetNamespace = "http://schemas.apache.org/yoko/idl/ParamModes", className = "org.apache.schemas.yoko.idl.parammodes.TestMultipleInParamsWithReturnResponse", localName = "testMultipleInParamsWithReturnResponse")
    @RequestWrapper(targetNamespace = "http://schemas.apache.org/yoko/idl/ParamModes", className = "org.apache.schemas.yoko.idl.parammodes.TestMultipleInParamsWithReturn", localName = "testMultipleInParamsWithReturn")
    @WebResult(targetNamespace = "http://schemas.apache.org/yoko/idl/ParamModes", name = "return")
    @WebMethod(operationName = "testMultipleInParamsWithReturn")
    public short testMultipleInParamsWithReturn(
        @WebParam(targetNamespace = "http://schemas.apache.org/yoko/idl/ParamModes", name = "p1")
        short p1,
        @WebParam(targetNamespace = "http://schemas.apache.org/yoko/idl/ParamModes", name = "p2")
        short p2
    );

    @ResponseWrapper(targetNamespace = "http://schemas.apache.org/yoko/idl/ParamModes", className = "org.apache.schemas.yoko.idl.parammodes.TestMultipleOutParamsWithReturnResponse", localName = "testMultipleOutParamsWithReturnResponse")
    @RequestWrapper(targetNamespace = "http://schemas.apache.org/yoko/idl/ParamModes", className = "org.apache.schemas.yoko.idl.parammodes.TestMultipleOutParamsWithReturn", localName = "testMultipleOutParamsWithReturn")
    @WebResult(targetNamespace = "http://schemas.apache.org/yoko/idl/ParamModes", name = "return")
    @WebMethod(operationName = "testMultipleOutParamsWithReturn")
    public short testMultipleOutParamsWithReturn(
        @WebParam(targetNamespace = "http://schemas.apache.org/yoko/idl/ParamModes", mode = Mode.OUT, name = "p1")
        javax.xml.ws.Holder<java.lang.Short> p1,
        @WebParam(targetNamespace = "http://schemas.apache.org/yoko/idl/ParamModes", mode = Mode.OUT, name = "p2")
        javax.xml.ws.Holder<java.lang.Short> p2
    );

    @ResponseWrapper(targetNamespace = "http://schemas.apache.org/yoko/idl/ParamModes", className = "org.apache.schemas.yoko.idl.parammodes.TestMultipleInOutParamsWithReturnResponse", localName = "testMultipleInOutParamsWithReturnResponse")
    @RequestWrapper(targetNamespace = "http://schemas.apache.org/yoko/idl/ParamModes", className = "org.apache.schemas.yoko.idl.parammodes.TestMultipleInOutParamsWithReturn", localName = "testMultipleInOutParamsWithReturn")
    @WebResult(targetNamespace = "http://schemas.apache.org/yoko/idl/ParamModes", name = "return")
    @WebMethod(operationName = "testMultipleInOutParamsWithReturn")
    public short testMultipleInOutParamsWithReturn(
        @WebParam(targetNamespace = "http://schemas.apache.org/yoko/idl/ParamModes", mode = Mode.INOUT, name = "p1")
        javax.xml.ws.Holder<java.lang.Short> p1,
        @WebParam(targetNamespace = "http://schemas.apache.org/yoko/idl/ParamModes", mode = Mode.INOUT, name = "p2")
        javax.xml.ws.Holder<java.lang.Short> p2
    );

    @ResponseWrapper(targetNamespace = "http://schemas.apache.org/yoko/idl/ParamModes", className = "org.apache.schemas.yoko.idl.parammodes.TestMultipleMixedParamsWithReturnResponse", localName = "testMultipleMixedParamsWithReturnResponse")
    @RequestWrapper(targetNamespace = "http://schemas.apache.org/yoko/idl/ParamModes", className = "org.apache.schemas.yoko.idl.parammodes.TestMultipleMixedParamsWithReturn", localName = "testMultipleMixedParamsWithReturn")
    @WebResult(targetNamespace = "http://schemas.apache.org/yoko/idl/ParamModes", name = "return")
    @WebMethod(operationName = "testMultipleMixedParamsWithReturn")
    public short testMultipleMixedParamsWithReturn(
        @WebParam(targetNamespace = "http://schemas.apache.org/yoko/idl/ParamModes", name = "p1")
        short p1,
        @WebParam(targetNamespace = "http://schemas.apache.org/yoko/idl/ParamModes", mode = Mode.OUT, name = "p2")
        javax.xml.ws.Holder<java.lang.Short> p2,
        @WebParam(targetNamespace = "http://schemas.apache.org/yoko/idl/ParamModes", mode = Mode.INOUT, name = "p3")
        javax.xml.ws.Holder<java.lang.Short> p3
    );

    @ResponseWrapper(targetNamespace = "http://schemas.apache.org/yoko/idl/ParamModes", className = "org.apache.schemas.yoko.idl.parammodes.TestReturnResponse", localName = "testReturnResponse")
    @RequestWrapper(targetNamespace = "http://schemas.apache.org/yoko/idl/ParamModes", className = "org.apache.schemas.yoko.idl.parammodes.TestReturn", localName = "testReturn")
    @WebResult(targetNamespace = "http://schemas.apache.org/yoko/idl/ParamModes", name = "return")
    @WebMethod(operationName = "testReturn")
    public short testReturn();
}
