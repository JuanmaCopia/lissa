package gov.nasa.jpf.symbc.heap.solving.canonicalizer;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;

import gov.nasa.jpf.symbc.heap.solving.HeapSolvingInstructionFactory;
import gov.nasa.jpf.symbc.heap.solving.config.ConfigParser;
import gov.nasa.jpf.symbc.heap.solving.utils.Utils;
import gov.nasa.jpf.symbc.heap.symbolicinput.Argument;
import gov.nasa.jpf.symbc.heap.symbolicinput.TargetMethod;
import gov.nasa.jpf.vm.ClassInfo;

public class TestCaseGenerator {

    String testClassName;
    String testFileName;
    String fullPathTestFile;
    File testFile;
    ConfigParser config;
    int testNumber = 0;

    public TestCaseGenerator(ConfigParser config) {
        this.config = config;
        this.testClassName = createTestClassName();
        this.testFileName = createTestFileName();
        this.fullPathTestFile = createFullPathTestFile(ConfigParser.TESTCASE_DIR);
        this.testFile = createTestFile();
        assert (this.testFile != null);
        appendPackageName();
        appendImports();
        appendClassDeclarationString();
    }

    public void generateTestCase(String structureCode) {
        String testCase = createTestCase(structureCode);
        Utils.appendToFile(this.testFile, addTabsToLines(testCase));
    }

    public void generateTestCase(HashMap<String, String> varValues, HashMap<ClassInfo, Integer> maxIdMap) {
        String structureCode = generateStructureCode(varValues, maxIdMap);
        generateTestCase(structureCode);
    }

    private String generateStructureCode(HashMap<String, String> varValues, HashMap<ClassInfo, Integer> maxIdMap) {
        String result = createCodeOfObjectDefinitions(maxIdMap);
        result += createCodeOfFieldAssignations(varValues);
        return result;
    }

    private String createCodeOfFieldAssignations(HashMap<String, String> varValues) {
        String result = "";
        for (Entry<String, String> entry : varValues.entrySet()) {
            String field = entry.getKey();
            String fieldValue = entry.getValue();
            result += String.format("%s = %s;\n", field, fieldValue);
        }
        return result;
    }

    private String createCodeOfObjectDefinitions(HashMap<ClassInfo, Integer> maxIdMap) {
        String result = "";
        for (Entry<ClassInfo, Integer> entry : maxIdMap.entrySet()) {
            ClassInfo cls = entry.getKey();
            Integer maxId = entry.getValue();
            result += createCodeDefinitionsForClass(cls, maxId);
        }
        return result;
    }

    private String createCodeDefinitionsForClass(ClassInfo cls, Integer maxId) {
        String result = "";
        String className = cls.getSimpleName();
        for (int i = 0; i <= maxId; i++) {
            result += createCodeOfConstructorCall(className, i);
        }
        return result;
    }

    private String createCodeOfConstructorCall(String className, int id) {
        String variableName = createVariableName(className, id);
        return String.format("%s %s = new %s();\n", className, variableName, className);
    }

    public static String createFieldAccessString(String ownerObject, String fieldName) {
        return ownerObject + "." + fieldName;
    }

    public static String createVariableName(String className, int id) {
        return className.toLowerCase() + "_" + id;
    }

    public void appendFinalBracket() {
        Utils.appendToFile(this.testFile, "\n}\n");
    }

    private String createTestCase(String structureCode) {
        String testCase = "";
        testCase += createTestCaseHeader();
        testCase += addTabsToLines(createTestCaseBody(structureCode));
        testCase += "}\n";
        return testCase;
    }

    private String createTestCaseBody(String structureCode) {
        String body = structureCode + "\n";
        body += createRepOKAssertion() + "\n";
        body += createTargetMethodCode() + "\n";
        body += createRepOKAssertion();
        return body;
    }

    private String createRepOKAssertion() {
        return String.format("assertTrue(%s.repOK());\n", getMainObjectVariableName());
    }

    private String createTargetMethodCode() {
        String result = "";
        TargetMethod method = HeapSolvingInstructionFactory.getTargetMethod();
        result += createArgumentsCode(method) + "\n";
        result += createMethodCallCode(method) + "  // Call to Method Under Test\n";
        return result;
    }

    private String createMethodCallCode(TargetMethod method) {
        String variableName = getMainObjectVariableName();
        String methodName = method.getName();
        String arguments = createArgumentsString(method);
        // TODO Auto-generated method stub
        return String.format("%s.%s(%s);", variableName, methodName, arguments);
    }

    private String createArgumentsString(TargetMethod method) {
        Argument[] args = method.getArguments();
        String commaSeparatedNames = "";
        if (args.length > 0) {
            commaSeparatedNames += args[0].getName();
            for (int i = 1; i < args.length; i++)
                commaSeparatedNames += ", " + args[i].getName();
        }
        return commaSeparatedNames;
    }

    private String createArgumentsCode(TargetMethod method) {
        String code = "";
        for (int i = 0; i < method.getNumberOfArguments(); i++) {
            Argument arg = method.getArgument(i);
            code += arg.getDeclarationCode() + "\n";
        }
        return code;
    }

    private String createTestCaseHeader() {
        return String.format("@Test\npublic void test_%d() {\n", this.testNumber++);
    }

    private File createTestFile() {
        return Utils.createFileAndFolders(this.fullPathTestFile, true);
    }

    private void appendPackageName() {
        String packageDeclaration = String.format("\npackage %s;\n", this.config.symSolveSimpleClassName.toLowerCase());
        Utils.appendToFile(this.testFile, packageDeclaration);
    }

    private void appendImports() {
        String imports = "import static org.junit.jupiter.api.Assertions.assertTrue;\n";
        imports += "import org.junit.jupiter.api.Test;\n";
        Utils.appendToFile(this.testFile, imports);
    }

    private void appendClassDeclarationString() {
        Utils.appendToFile(this.testFile, classDeclarationSring());
    }

    private String createTestClassName() {
        String strategy = this.config.solvingStrategy.name();
        return String.format("%s_%s_%s_%s_Test", config.symSolveSimpleClassName, this.config.targetMethodName, strategy,
                this.config.finitizationArgs);
    }

    private String createDirectoryPath(String directory) {
        return String.format("%s", directory);
    }

    private String createFullPathTestFile(String directory) {
        return createDirectoryPath(directory) + "/" + this.testFileName;
    }

    private String createTestFileName() {
        return String.format("%s.java", this.testClassName);
    }

    private String classDeclarationSring() {
        return String.format("\npublic class %s {\n", this.testClassName);
    }

    private String addTabsToLines(String str) {
        String result = "";
        try {
            result = Utils.addTabsToEachLine(str);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private String getMainObjectVariableName() {
        return String.format("%s_%d", config.symSolveSimpleClassName.toLowerCase(), 0);
    }
}
