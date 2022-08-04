
import sys


def append_to_file(filename, data):
    f = open(filename, "a")
    f.write(data)
    f.close()


def read_file(filename):
    f = open(filename, "r")
    data = f.readlines()
    f.close()
    return data


def create_new_file(filename):
    f = open(filename, "x")
    f.close()


def create_test_code(class_name, result, vector, n):
    s = """
\n@Test
public void CNAMESolverTestNUM() {
    assertEquals(RESULT, symSolve.isSat(VECTOR));
}"""
    s = s.replace("CNAME", class_name)
    s = s.replace("NUM", str(n))
    s = s.replace("RESULT", result)
    return s.replace("VECTOR", '"' + vector + '"')


if __name__ == "__main__":
    if len(sys.argv) < 2:
        print("Usage: <SCRIPT> <path-to-file>")
        sys.exit(-1)

    filename = sys.argv[1]
    test_filename = "testCases" + filename
    create_new_file(test_filename)

    lines = read_file(filename)
    n = 0
    vectors = set()
    for line in lines:
        test = line.split(";")

        class_name = test[0]
        vector = test[1]
        result = test[2].strip()

        if vector in vectors:
            continue
        else:
            vectors.add(vector)

        test_code = create_test_code(class_name, result, vector, n)
        append_to_file(test_filename, test_code)
        n += 1
