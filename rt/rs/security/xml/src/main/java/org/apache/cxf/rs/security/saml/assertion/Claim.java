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
package org.apache.cxf.rs.security.saml.assertion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Claim {
    public static final String DEFAULT_ROLE_NAME = 
        "http://schemas.xmlsoap.org/ws/2005/05/identity/claims/role"; 
    public static final String DEFAULT_NAME_FORMAT = 
        "http://schemas.xmlsoap.org/ws/2005/05/identity/claims";
    
    private String nameFormat;
    private String name;
    private String friendlyName;
    
    private List<String> values = new ArrayList<String>();

    public Claim() {
        
    }
    
    public Claim(String nameFormat, String name) {
        this.nameFormat = nameFormat;
        this.name = name;        
    }
    
    public Claim(String nameFormat, String name, String value) {
        this(nameFormat, name, Collections.singletonList(value));        
    }
    
    public Claim(String nameFormat, String name, List<String> values) {
        this.nameFormat = nameFormat;
        this.name = name;
        this.values = values;
    }
    
    public void setNameFormat(String nameFormat) {
        this.nameFormat = nameFormat;
    }

    public String getNameFormat() {
        return nameFormat;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setFriendlyName(String friendlyName) {
        this.friendlyName = friendlyName;
    }

    public String getFriendlyName() {
        return friendlyName;
    }

    public void setValues(List<String> values) {
        this.values = values;
    }

    public List<String> getValues() {
        return values;
    }
    
    
    
}
