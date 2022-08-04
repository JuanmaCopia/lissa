package gov.nasa.jpf.symbc.heap.solving.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;

import korat.finitization.impl.CVElem;
import symsolve.vector.SymSolveVector;

public class Utils {

    public static boolean createFile(String fileName) {
        boolean result = false;
        try {
            File f = new File(fileName);
            if (f != null) {
                result = f.createNewFile();
            }
        } catch (IOException e) {
            return result;
        }
        return result;
    }

    public static File createFileAndFolders(String fullPathFile, boolean deleteFile) {
        File targetFile = new File(fullPathFile);
        if (deleteFile && targetFile.exists())
            targetFile.delete();

        File directory = targetFile.getParentFile();
        if (!directory.exists())
            directory.mkdirs();

        try {
            targetFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        return targetFile;
    }

    public static boolean fileExist(String fullPathFile) {
        File targetFile = new File(fullPathFile);
        return targetFile.exists();
    }

    public static boolean appendToFile(String fileName, String data) {
        try {
            File f = new File(fileName);
            FileWriter fw = new FileWriter(f, true);
            fw.write(data + "\n");
            fw.close();
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    public static boolean appendToFile(File file, String data) {
        try {
            FileWriter fw = new FileWriter(file, true);
            fw.write(data + "\n");
            fw.close();
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    public static String addTabsToEachLine(String str) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader bufReader = new BufferedReader(new StringReader(str));
        String line = bufReader.readLine();
        while (line != null) {
            sb.append(String.format("\t%s\n", line));
            line = bufReader.readLine();
        }
        return sb.toString();
    }

    public static void printVectorFormat(int[] intVector, CVElem[] structureList) {
        assert (structureList.length == intVector.length);
        int vectorSize = intVector.length;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < vectorSize; i++) {
            CVElem elem = structureList[i];
            int value = intVector[i];
            sb.append(elem.getFieldDomain().getClassOfField().getSimpleName() + "." + elem.getFieldName() + " = "
                    + value);
            if (i < vectorSize - 1)
                sb.append(" , ");
        }
        System.out.println(sb.toString());
    }

    public static String createisSatAssertTrue(int[] vector) {
    	String strVector = createVectorString(vector);
    	return String.format("assertTrue(symSolve.isSatisfiable(\"%s\"));", strVector);
    }

    public static String createisSatAssertFalse(int[] vector) {
    	String strVector = createVectorString(vector);
    	return String.format("assertFalse(symSolve.isSatisfiable(\"%s\"));", strVector);
    }
    
    public static String createSearchAnotherSolutionTestCode(SymSolveVector vector, int[] solutionVector) {
    	StringBuilder sb = new StringBuilder();
    	sb.append(String.format("concreteVector = %s;\n", createInitArrayString(vector.getConcreteVector())));
    	sb.append(String.format("fixedInd = Arrays.asList(%s);\n", removeFirstAndLastChar(vector.getFixedIndices().toString())));
    	sb.append("fixedIndices = new HashSet<>(fixedInd);\n");
    	sb.append("queryVector = new SymSolveVector(concreteVector, fixedIndices);\n");
    	sb.append(String.format("solutionVector = %s;\n", createInitArrayString(solutionVector)));
    	sb.append("assertArrayEquals(solutionVector, symSolve.searchAnotherSolution(queryVector));\n\n");
    	return sb.toString();
    }

    private static String createVectorString(int[] vector) {
    	if (vector == null)
    		return "null";
    	String strVector = Arrays.toString(vector);
    	return strVector.substring(1, strVector.length() - 1);
    }
    
    private static String removeFirstAndLastChar(String str) {
    	return str.substring(1, str.length() - 1);
    }
    
    private static String createInitArrayString(int[] vector) {
    	if (vector == null)
    		return "null";
    	String result = Arrays.toString(vector);
    	result = "new int[] {" + result.substring(1, result.length() - 1) + "}";
    	return result;
    }

}
