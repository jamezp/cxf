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

package org.apache.yoko.tools.common.idltypes;

import java.io.*;

public final class IdlConst extends IdlDefnImplBase implements IdlType {
    private IdlType base;
    private String value;

    private IdlConst(IdlScopeBase parent, String name, IdlType basetype, String val) {
        super(parent, name);
        this.base = basetype;
        this.value = val;
    }
    
    public static IdlConst create(IdlScopeBase parent, String name, IdlType base, String value) {
        return new IdlConst(parent, name, base, value);
    }


    IdlType baseType() {
        return base;
    }
    
    String valueType() {
        return value;
    }


    public void write(PrintWriter pw) {
        pw.println(indent() + "const " + base.fullName(scopeName()) + " " 
                   + localName() + " = "  + value.toString() + ";");
    }
    
}
