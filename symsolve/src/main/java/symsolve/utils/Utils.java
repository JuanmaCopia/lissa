package symsolve.utils;

import korat.finitization.impl.CVElem;

import java.io.*;
import java.util.Arrays;

public class Utils {

    public static boolean createFile(String fileName) {
        boolean result = false;
        try {
            File f = new File(fileName);
            result = f.createNewFile();
        } catch (IOException e) {
            return false;
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
            sb.append(elem.getFieldDomain().getClassOfField().getSimpleName()).append(".").append(elem.getFieldName()).append(" = ").append(value);
            if (i < vectorSize - 1)
                sb.append(" , ");
        }
        System.out.println(sb);
    }

    public static String createAssertTrue(int[] vector) {
        String strVector = createVectorString(vector);
        return String.format("assertTrue(symSolve.isSatisfiable(\"%s\"));", strVector);
    }

    public static String createAssertFalse(int[] vector) {
        String strVector = createVectorString(vector);
        return String.format("assertFalse(symSolve.isSatisfiable(\"%s\"));", strVector);
    }

    private static String createVectorString(int[] vector) {
        String strVector = Arrays.toString(vector);
        return strVector.substring(1, strVector.length() - 1);
    }

}
