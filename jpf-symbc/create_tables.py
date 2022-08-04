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


def parse_line(line):
    data = line.split(",")

    method = data[0].strip()
    technique = data[1].strip()
    scope = int(data[2].strip())
    time = data[3].strip()
    paths = data[4].strip()
    spurious = data[5].strip()
    solving_time = data[6].strip()

    return RunData(method, technique, scope, time, paths, spurious, solving_time)


def write_tables_to_file(tables, table_filename):
    create_new_file(table_filename)
    for table in tables:
        append_to_file(table_filename, table + "\n\n\n")


def write_tables_to_file_noel(tables, table_filename):
    create_new_file(table_filename)
    for table in tables:
        append_to_file(table_filename, table + "\n")


def get_technique_empty_data_string(technique, scope, max_tech_scope):
    if scope == max_tech_scope + 1:
        return "TO,-"
    return ","


def get_technique_empty_data_string_paper(technique, scope, max_tech_scope):
    if scope > max_tech_scope:
        return "TO & -"
    return "&"


def correct_case_technique_name(technique):
    t = technique.upper()
    if t == "LIHYBRID":
        return "LIHybrid"
    elif t == "DRIVER":
        return ",Driver"
    elif t == "IFREPOK":
        return ",IfRepOK"
    elif t == "LISSA":
        return ",LISSA"
    elif t == "LISSAM":
        return ",LISSAM"
    elif t == "LISSANOSB":
        return ",LISSANoSB"
    else:
        return technique


class RunData:
    def __init__(self, method, technique, scope, time, paths, spurious, solving_time):
        self.technique = technique
        self.method = method
        self.scope = scope
        self.time = time
        self.paths = paths
        self.spurious = spurious
        self.solving_time = solving_time

    def get_data_only_string(self):
        if self.technique == "LIHYBRID":
            return "{},{} ({})".format(self.time, self.paths, self.spurious)
        elif self.technique == "LISSA" or self.technique == "LISSAM":
            return "{} ({}),{}".format(self.time, self.solving_time, self.paths)
        else:
            return "{},{}".format(self.time, self.paths)

    def get_data_only_string_paper(self):
        if self.technique == "LIHYBRID":
            return "{} & {} ({})".format(self.time, self.paths, self.spurious)
        elif self.technique == "LISSA" or self.technique == "LISSAM":
            return "{} ({}) & {}".format(self.time, self.solving_time, self.paths)
        else:
            return "{} & {}".format(self.time, self.paths)

    def get_data_only_string_paper_onlytime(self):
        assert(self.technique == "LISSA" or self.technique == "LISSANOSB")
        return "{} ({})".format(self.time, self.solving_time)


class MethodData:
    def __init__(self, method):
        self.method = method
        self.technique_map = {}
        self.technique_names = []
        self.max_scopes_per_technique = {}
        self.max_scope = 0
        self.timeouts = set()

    def add_run_data(self, run_data):
        technique = run_data.technique
        if technique not in self.technique_names:
            self.technique_names.append(technique)
        scope = run_data.scope
        if technique not in self.technique_map.keys():
            self.technique_map[technique] = {}

        self.technique_map[technique][int(scope)] = run_data
        # scope_map[int(scope)] = run_data

        print("Method {}: Adding Scope {} for technique {}".format(self.method, scope, technique))

        # set max scope
        if technique not in self.max_scopes_per_technique.keys():
            self.max_scopes_per_technique[technique] = 0
        prev_max = self.max_scopes_per_technique[technique]
        if prev_max < scope:
            self.max_scopes_per_technique[technique] = scope

        if scope > self.max_scope:
            self.max_scope = scope

    def get_run_data(self, technique, scope):
        if technique in self.technique_map.keys():
            scope_map = self.technique_map[technique]
            if scope in self.scope_map.keys():
                return scope_map[scope]
        return None

    def get_data_string(self, technique, scope):
        assert(technique in self.technique_map.keys())
        scope_map = self.technique_map[technique]
        if scope in scope_map.keys():
            return scope_map[scope].get_data_only_string()
        max_scope = self.max_scopes_per_technique[technique]
        return get_technique_empty_data_string(technique, scope, max_scope)

    def get_technique_empty_data_string_paper(self, technique, scope, max_tech_scope):
        if scope > max_tech_scope:
            if technique not in self.timeouts:
                self.timeouts.add(technique)
                return "TO & -"
        return "&"

    def get_technique_empty_data_string_paper_onlytime(self, technique, scope, max_tech_scope):
        if scope > max_tech_scope:
            if technique not in self.timeouts:
                self.timeouts.add(technique)
                return "TO"
        return " "

    def get_data_string_paper(self, technique, scope):
        assert(technique in self.technique_map.keys())
        scope_map = self.technique_map[technique]
        if scope in scope_map.keys():
            return scope_map[scope].get_data_only_string_paper()
        max_scope = self.max_scopes_per_technique[technique]
        return self.get_technique_empty_data_string_paper(technique, scope, max_scope)

    def get_data_string_paper_only_time(self, technique, scope):
        assert(technique in self.technique_map.keys())
        scope_map = self.technique_map[technique]
        if scope in scope_map.keys():
            return scope_map[scope].get_data_only_string_paper_onlytime()
        max_scope = self.max_scopes_per_technique[technique]
        return self.get_technique_empty_data_string_paper_onlytime(technique, scope, max_scope)

    def create_web_table_rq1(self, class_name):
        techniques_str = ""
        for tech in self.technique_names:
            if tech != "LISSANOSB":
                techniques_str += "," + correct_case_technique_name(tech)

        # HEADER
        table = "{}.{}{}\n".format(class_name, self.method, techniques_str)
        table += "Scope,time,paths (spurious),time,paths,time,paths,time (solving),paths,time (solving),paths"

        for i in range(1, self.max_scope + 1):
            table += "\n{scope}".format(scope=i)
            for tech in self.technique_names:
                if tech != "LISSANOSB":
                    print(tech)
                    table += ",{}".format(self.get_data_string(tech, i))

        return table

    def create_web_table_rq2(self, class_name):
        # HEADER
        table = "{}.{}{}\n".format(class_name, self.method, ",LISSANOSB,,LISSA")
        table += "Scope,time,paths,time,paths"

        for i in range(1, self.max_scope + 1):
            table += "\n{scope}".format(scope=i)
            table += ",{}".format(self.get_data_string("LISSANOSB", i))
            table += ",{}".format(self.get_data_string("LISSA", i))
        return table

    def create_method_full_paper_table_rq1(self):
        table = "& {}".format(self.method)

        first = True

        for i in range(1, self.max_scope + 1):
            if first:
                table += "\n & {scope} ".format(scope=i)
                first = False
            else:
                table += "\n&& {scope} ".format(scope=i)
            for tech in self.technique_names:
                if tech != "LISSANOSB":
                    # print(tech)
                    table += "& {} ".format(self.get_data_string_paper(tech, i))
            table += "\\\\"
        return table

    def create_method_onlymax_paper_table_rq1(self):
        self.timeouts = set()
        reported_scopes = set()
        for tech, maxscope in self.max_scopes_per_technique.items():
            if tech != "LISSANOSB":
                reported_scopes.add(maxscope)

        reported_scopes = list(reported_scopes)
        reported_scopes.sort()

        table = "& {}".format(self.method)

        first = True
        for i in reported_scopes:
            if first:
                table += "\n & {scope} ".format(scope=i)
                first = False
            else:
                table += "\n&& {scope} ".format(scope=i)
            for tech in self.technique_names:
                if tech != "LISSANOSB":
                    # print(tech)
                    table += "& {} ".format(self.get_data_string_paper(tech, i))
            table += "\\\\"
        return table

    def create_method_full_paper_table_rq2(self, class_name):
        self.timeouts = set()
        table = "{} & {}".format(class_name, self.method)

        first = True

        for i in range(1, self.max_scope + 1):
            if first:
                table += "\n & {scope} ".format(scope=i)
                first = False
            else:
                table += "\n&& {scope} ".format(scope=i)

            table += "& {} ".format(self.get_data_string_paper_only_time("LISSANOSB", i))
            table += "& {} ".format(self.get_data_string_paper_only_time("LISSA", i))
            table += "\\\\"
        return table


class DataMap:
    def __init__(self, class_name):
        self.method_map = {}
        self.class_name = class_name

    def put(self, run_data):
        method = run_data.method
        if method not in self.method_map.keys():
            self.method_map[method] = MethodData(method)

        method_data = self.method_map[method]
        method_data.add_run_data(run_data)

    def get_rq1_web_tables(self):
        tables = []
        for method_name, method_data in self.method_map.items():
            tables.append(method_data.create_web_table_rq1(self.class_name))
        return tables

    def get_rq2_web_tables(self):
        tables = []
        for method_name, method_data in self.method_map.items():
            tables.append(method_data.create_web_table_rq2(self.class_name))
        return tables

    def get_paper_string_tables_onlymax_rq1(self):
        tables = []
        for method_name, method_data in self.method_map.items():
            tables.append(method_data.create_method_onlymax_paper_table_rq1())
            tables.append("\\cline{2-13}")
        return tables

    def get_paper_string_tables_full_rq1(self):
        tables = []
        for method_name, method_data in self.method_map.items():
            tables.append(method_data.create_method_full_paper_table_rq1())
        return tables

    def get_paper_string_tables_full_rq2(self):
        tables = []
        for method_name, method_data in self.method_map.items():
            tables.append(method_data.create_method_full_paper_table_rq2(self.class_name))
            # tables.append("\\cline{2-13}")
        return tables


if __name__ == "__main__":
    if len(sys.argv) < 2:
        print("Usage: <SCRIPT> <Path-to-csv-file>")
        sys.exit(-1)

    filename = sys.argv[1]
    lines = read_file(filename)

    c = filename.split("/")
    c = c[len(c) - 1]
    class_name = c.split("-")[0]

    data_map = DataMap(class_name)

    lines = lines[1:]

    for line in lines:
        run_data = parse_line(line)
        data_map.put(run_data)

    # tables = data_map.get_rq1_web_tables()
    # write_tables_to_file(tables, class_name + "-RQ1-table.csv")

    tables1 = data_map.get_rq2_web_tables()
    write_tables_to_file(tables1, class_name + "-RQ2-table.csv")

    # tables2 = data_map.get_paper_string_tables_onlymax_rq1()
    # write_tables_to_file_noel(tables2, class_name + "-paper.csv")

    # tables3 = data_map.get_paper_string_tables_full_rq1()
    # write_tables_to_file(tables3, class_name + "full-paper.csv")

    # tables4 = data_map.get_paper_string_tables_full_rq2()
    # write_tables_to_file(tables4, class_name + "full-paper-RQ2.csv")
