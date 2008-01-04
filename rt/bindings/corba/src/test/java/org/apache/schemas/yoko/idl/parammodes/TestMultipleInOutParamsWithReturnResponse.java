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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for testMultipleInOutParamsWithReturnResponse element declaration.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;element name="testMultipleInOutParamsWithReturnResponse">
 *   &lt;complexType>
 *     &lt;complexContent>
 *       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *         &lt;sequence>
 *           &lt;element name="return" type="{http://www.w3.org/2001/XMLSchema}short"/>
 *           &lt;element name="p1" type="{http://www.w3.org/2001/XMLSchema}short"/>
 *           &lt;element name="p2" type="{http://www.w3.org/2001/XMLSchema}short"/>
 *         &lt;/sequence>
 *       &lt;/restriction>
 *     &lt;/complexContent>
 *   &lt;/complexType>
 * &lt;/element>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "_return",
    "p1",
    "p2"
})
@XmlRootElement(name = "testMultipleInOutParamsWithReturnResponse")
public class TestMultipleInOutParamsWithReturnResponse {

    @XmlElement(name = "return")
    protected short _return;
    protected short p1;
    protected short p2;

    /**
     * Gets the value of the return property.
     * 
     */
    public short getReturn() {
        return _return;
    }

    /**
     * Sets the value of the return property.
     * 
     */
    public void setReturn(short value) {
        this._return = value;
    }

    /**
     * Gets the value of the p1 property.
     * 
     */
    public short getP1() {
        return p1;
    }

    /**
     * Sets the value of the p1 property.
     * 
     */
    public void setP1(short value) {
        this.p1 = value;
    }

    /**
     * Gets the value of the p2 property.
     * 
     */
    public short getP2() {
        return p2;
    }

    /**
     * Sets the value of the p2 property.
     * 
     */
    public void setP2(short value) {
        this.p2 = value;
    }

}
