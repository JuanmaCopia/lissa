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

package gov.nasa.jpf.symbc.numeric.visitors;

import java.util.HashMap;

import gov.nasa.jpf.symbc.numeric.Comparator;
import gov.nasa.jpf.symbc.numeric.Constraint;
import gov.nasa.jpf.symbc.numeric.ConstraintExpressionVisitor;
import gov.nasa.jpf.symbc.numeric.Expression;
import gov.nasa.jpf.symbc.numeric.IntegerConstant;
import gov.nasa.jpf.symbc.numeric.SymbolicInteger;

public class CollectIntegerConstantsVisitor extends ConstraintExpressionVisitor {

    private HashMap<Expression, Integer> equlityConstants = new HashMap<Expression, Integer>();

    private HashMap<Expression, Integer> nonEqualityConstants = new HashMap<Expression, Integer>();

    @Override
    public void postVisit(Constraint constraint) {
        Comparator comp = constraint.getComparator();
        if (comp.equals(Comparator.EQ)) {
            putEqualityConstantExpressions(constraint);
        } else if (comp.equals(Comparator.NE)) {
            putNonEqualityConstantExpressions(constraint);
        }
    }

    private void putEqualityConstantExpressions(Constraint constraint) {
        Expression left = constraint.getLeft();
        Expression right = constraint.getRight();
        if (right instanceof IntegerConstant && left instanceof SymbolicInteger)
            equlityConstants.put(left, ((IntegerConstant) right).value());
        else if (left instanceof IntegerConstant && right instanceof SymbolicInteger)
            equlityConstants.put(right, ((IntegerConstant) left).value());
    }

    private void putNonEqualityConstantExpressions(Constraint constraint) {
        Expression left = constraint.getLeft();
        Expression right = constraint.getRight();
        if (right instanceof IntegerConstant && left instanceof SymbolicInteger) {
            int value = ((IntegerConstant) right).value();
            if (value == 0 || value == 1) {
                value = negateBooleanRepresentedAsInt(value);
                nonEqualityConstants.put(left, value);
            }
        } else if (left instanceof IntegerConstant && right instanceof SymbolicInteger) {
            int value = ((IntegerConstant) left).value();
            if (value == 0 || value == 1) {
                value = negateBooleanRepresentedAsInt(value);
                nonEqualityConstants.put(right, value);
            }
        }
    }

    private int negateBooleanRepresentedAsInt(int value) {
        if (value == 0)
            return 1;
        return 0;
    }

    public HashMap<Expression, Integer> getEqulityConstants() {
        return this.equlityConstants;
    }

    public HashMap<Expression, Integer> getNonEqulityConstants() {
        return this.nonEqualityConstants;
    }

}
