package heapsolving.sqlfilterclauses.symsolve;
/*
 * Copyright (C) 2003-2004 Maury Hammel
 * mjhammel@users.sourceforge.net
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */


import korat.finitization.IFinitization;
import korat.finitization.IObjSet;
import korat.finitization.impl.FinitizationFactory;

/**
 * This class contains the information used to build the Where and Order By
 * clauses used by the database query triggered by selecting the Contents tab in
 * the Session internal frame.
 *
 */
public class SQLFilterClauses {

	/** The container for the SQL filter information */
	HashMapStrHmap _sqlClauseInformation;

	/**
	 * Creates a new instance of SQLFilterClauses.
	 */
	public SQLFilterClauses() {
		_sqlClauseInformation = new HashMapStrHmap();
	}

	public static IFinitization finSQLFilterClauses(int nodesNum) {
		IFinitization f = FinitizationFactory.create(SQLFilterClauses.class);

		IObjSet hashmap = f.createObjSet(HashMapStrHmap.class, 1, true);
		f.set(SQLFilterClauses.class, "_sqlClauseInformation", hashmap);

		IObjSet entries = f.createObjSet(HashMapStrHmap.EntrySH.class, nodesNum, true);
		f.set(HashMapStrHmap.class, "e0", entries);
		f.set(HashMapStrHmap.class, "e1", entries);
		f.set(HashMapStrHmap.class, "e2", entries);
		f.set(HashMapStrHmap.class, "e3", entries);
		f.set(HashMapStrHmap.class, "e4", entries);
		f.set(HashMapStrHmap.class, "e5", entries);
		f.set(HashMapStrHmap.class, "e6", entries);
		f.set(HashMapStrHmap.class, "e7", entries);
	
		IObjSet subhashmaps = f.createObjSet(HashMapStrStr.class, nodesNum, true);
		f.set(HashMapStrHmap.EntrySH.class, "value", subhashmaps);
		f.set(HashMapStrHmap.EntrySH.class, "next", entries);

		IObjSet entries2 = f.createObjSet(HashMapStrStr.EntrySS.class, nodesNum, true);
		f.set(HashMapStrStr.class, "e0", entries2);
		f.set(HashMapStrStr.class, "e1", entries2);
		f.set(HashMapStrStr.class, "e2", entries2);
		f.set(HashMapStrStr.class, "e3", entries2);
		f.set(HashMapStrStr.class, "e4", entries2);
		f.set(HashMapStrStr.class, "e5", entries2);
		f.set(HashMapStrStr.class, "e6", entries2);
		f.set(HashMapStrStr.class, "e7", entries2);
		
		f.set(HashMapStrStr.EntrySS.class, "next", entries2);
		return f;
	}
	
	public boolean repOK() {
		if (_sqlClauseInformation == null)
			return true;

		if (!_sqlClauseInformation.repOK())
			return false;

		HashMapStrStr h;
		for (HashMapStrHmap.EntrySH e : _sqlClauseInformation.entrySet()) {
			h = e.getValue();
			if (h != null && !h.repOK())
				return false;
		}

		return true;
	}

}
