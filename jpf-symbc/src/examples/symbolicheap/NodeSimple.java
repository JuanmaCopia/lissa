/*
 * Copyright (C) 2014, United States Government, as represented by the
 * Administrator of the National Aeronautics and Space Administration.
 * All rights reserved.
 *
 * Symbolic Pathfinder (jpf-symbc) is licensed under the Apache License, 
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0. 
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */

package symbolicheap;

import gov.nasa.jpf.symbc.Debug;

public class NodeSimple {

	int elem;
    NodeSimple next;
  
	
    public void traverse() {
    	NodeSimple current = next;
    	while(current != null) {
    		current = current.next;
    	}
    }
    
    
    public void traverse_no_cycle() {
    	NodeSimple current = next;
    	while(current != null) {
    		current = current.next;
    	}
    }
    
    public NodeSimple(int x) {
    	elem = x;
    	next = null;
    }
	
    public void test(NodeSimple n) {
    	if(n!=null && n.next!=null)
    		System.out.println("2 elements");
    }
	public static void main(String[] args) {	
		
//		NodeSimple X = new NodeSimple(5);
//        (new NodeSimple(0)).test(X);
		NodeSimple X = new NodeSimple(5);
		X = (NodeSimple) Debug.makeSymbolicRef("X", X);
	
		if(X!=null) X.traverse();
	}

}
