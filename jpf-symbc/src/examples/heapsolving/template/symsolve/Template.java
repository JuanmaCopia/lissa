/*
 * Copyright(C) 2008-2009 Dmitriy Kumshayev. <dq@mail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package heapsolving.template.symsolve;

import korat.finitization.IFinitization;
import korat.finitization.IObjSet;
import korat.finitization.impl.FinitizationFactory;

public class Template {

	public LinkedList parameters = new LinkedList();
	public HashMapStrPar parametersByName = new HashMapStrPar();

	public boolean repOK() {
		if (parameters == null)
			return false;
		if (!parameters.repOK())
			return false;
		if (parametersByName == null)
			return false;
		if (!parametersByName.repOK())
			return false;
		return true;
	}

	public static IFinitization finTemplate(int nodesNum) {

		IFinitization f = FinitizationFactory.create(Template.class);

		IObjSet ll = f.createObjSet(LinkedList.class, 1, true);
		f.set(Template.class, "parameters", ll);

		IObjSet hmap = f.createObjSet(HashMapStrPar.class, 1, true);
		f.set(Template.class, "parametersByName", hmap);

		IObjSet nodes = f.createObjSet(LinkedList.Entry.class, nodesNum, true);
		f.set(LinkedList.class, "header", nodes);
		f.set(LinkedList.Entry.class, "next", nodes);
		f.set(LinkedList.Entry.class, "previous", nodes);

		IObjSet entries = f.createObjSet(HashMapStrPar.EntrySP.class, nodesNum, true);
		f.set(HashMapStrPar.class, "e0", entries);
		f.set(HashMapStrPar.class, "e1", entries);
		f.set(HashMapStrPar.class, "e2", entries);
		f.set(HashMapStrPar.class, "e3", entries);
		f.set(HashMapStrPar.class, "e4", entries);
		f.set(HashMapStrPar.class, "e5", entries);
		f.set(HashMapStrPar.class, "e6", entries);
		f.set(HashMapStrPar.class, "e7", entries);
		f.set(HashMapStrPar.class, "e8", entries);
		f.set(HashMapStrPar.class, "e9", entries);
		f.set(HashMapStrPar.class, "e10", entries);
		f.set(HashMapStrPar.class, "e11", entries);
		f.set(HashMapStrPar.class, "e12", entries);
		f.set(HashMapStrPar.class, "e13", entries);
		f.set(HashMapStrPar.class, "e14", entries);
		f.set(HashMapStrPar.class, "e15", entries);

		f.set(HashMapStrPar.EntrySP.class, "next", entries);

		return f;
	}

}