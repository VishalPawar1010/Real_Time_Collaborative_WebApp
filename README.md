
# k6 Load Testing Readme : for EagleApps
#### Table of contents
- [Main script configuration \& execution](#main-script-configuration--execution)
  - [Imported Modules](#imported-modules)
  - [Exported Methods](#exported-methods)
  - [Important Methods](#important-methods)
  - [Configuration and Execution Flow (with examples)](#configuration-and-execution-flow-with-examples)
    - [(Option 1) Execute required use-case (with script env var name)](#option-1-execute-required-use-case-with-script-env-var-name)
    - [(Option 2) Execute required test type (with test env var name)](#option-2-execute-required-test-type-with-test-env-var-name)
- [Thresholds](#thresholds)
    - [Threshold Format](#threshold-format)
  - [Example](#example)
- [Configuration.json](#configurationjson)
    - [Configuration Format](#configuration-format)
    - [Example:](#example-1)
- [How to run the tests from bamboo job](#how-to-run-the-tests-from-bamboo-job)
- [Steps are necessary to add new test script](#steps-are-necessary-to-add-new-test-script)

<!-- ## Table of Contents

- [Main script configuration & execution](#main-script-configuration-&-execution)
- [Thresholds](#thresholds)
- [Configuration.json](#configuration.json)
- [How to run test from Bamboo Job](#how-to-run-test-from-bamboo-job)
- [Steps to add new test script](#steps-to-add-new-test-script) -->


# Main script configuration & execution

The `main.js` file orchestrates and configures the execution of load and performance tests using the k6 testing tool. This file defines test configurations, scenarios, thresholds, and provides methods for setting up and tearing down the test environment.

<!-- ## Table of Contents

- [Imported Modules](#imported-modules) - Imports required files/functions
- [Exported Methods](#exported-methods)
- [Important Methods](#other-methods)
- [Configuration and Execution Flow](#configuration-and-execution-flow) -->
## Imported Modules
 - Imports required files/functions
## Exported Methods
Note: The code contains various environment variables (`__ENV`) that influence test behavior.
- `setup()`: Called at the beginning of the test. Performs actions based on environment variables.
- `default()`: Represents core logic of the load test (empty in this case).
- `teardown()`: Called at the end of the test. Performs cleanup actions.

## Important Methods
 
- `getTestConfigs()`: Determines tests to run based on specific environment variables.
- `getExecExecuteFunction(testName)`: Maps test names to execution functions.
- `constructOptions(tests)`: Constructs test options and scenarios based on `getTestConfigs()`.
- `setThresholds(options)`: Sets performance thresholds for each scenario.
- `handleSummary(data)`: Generates test result summaries and a Grafana dashboard URL.

## Configuration and Execution Flow (with examples)

(NOTE : To run the main.js script, make sure you are in the directory where main.js is present, here folder name:LOAD-TESTS )

### (Option 1) Execute required use-case (with script env var name)

```bash
k6 run -e BC_K6_CMCO_CREATE_CO_DELETE_CO=1 main.js
```
1. `setup()` : If env var BC_K6_STUDENT_REG_SEATPOOL is set to greater than 0, it executes function `getSeatPoolCounts()` in seat-pool-count.js script
2. `default()` represents core testing logic (currently empty).
3. `getTestConfigs()` : Sets cmco=1
4. `constructOptions(tests)` : Sets scenario key as `cmco` and value as `1`. Here -> executor: shared iterations, exec: getExecExecuteFunction(cmco).
5. `setThresholds(options)` : Here for cmco thresholds values are fecthed from thesholds.json for key : `cmco`
6. `getExecExecuteFunction(testName)`: Here testName is `cmco` which returns `cmco` value which execute `cmco` function in script create-co-delete-co.js.

7. `teardown()` : If env var BC_K6_STUDENT_REG_SEATPOOL is set to greater than 0, it executes function `getSeatPoolCounts()` in seat-pool-count.js script
8. `handleSummary(data)` generates test result summaries and a Grafana dashboard URL.
(NOTE : For above example we didn't set BC_K6_STUDENT_REG_SEATPOOL env var, so nothing will execute in setup and teardown function )

###  (Option 2) Execute required test type (with test env var name)

```bash
k6 run -e BC_K6_SMOKE_TEST=1 main.js
```
1. Execution flow : `getTestConfigs()` - It sets scenarioNameToExecute as `BC_K6_SMOKE_TEST`. Then from configurations.json for each env var in field `BC_K6_SMOKE_TEST` value is set to 1. e.g.  `BC_K6_CMCO_CREATE_CO_DELETE_CO`: `1` . Suubsequent flow goes accordig to option 1.
---
For more information about k6 and load testing, visit the [k6 documentation](https://k6.io/docs/).

---
# Thresholds

In K6, threshold files allow you to define performance and reliability criteria that your load tests should meet. We have two separate threshold files: one for smoke test (`threshold-smoketest.json`) and another for any other test scenarios (`threshold.json`). These files helps to establish the expected performance characteristics of application under different scenarios.

### Threshold Format

To add new threshold in threshold files, ensure you specify the following properties for each script:

- `Script name`: The name of script for the metrics evaluation.

- `Metric identifier`: The identifier for the metric in K6. This could be the name of the metric as displayed in K6's output.

- `Conditions`: Conditions to check against the metric values. These conditions are used to determine if the test meets the specified threshold.

## Example

Here's an example of the threshold definitions for the "facet_search" scenario:

```json
{
    "facet_search": {
        "http_req_duration_general_facet_search": ["med < 100"],
        "failure_rate{scenario:facet_search}": ["rate == 0"],
        "iteration{scenario:facet_search}": []
    },
    // ... additional scenarios ...
}
```
---
# Configuration.json

The `configuration.json` file is a versatile configuration file used to set up various test scenarios in K6. This JSON file encompasses different test cases, each defined by a test name, description, and specific parameters. These parameters can include both Virtual User (VU) counts and test duration specifications.

### Configuration Format

To add new configurations to the configuration.json file, ensure you specify the following properties for each test scenario:
- `Test name`: A unique identifier for the test scenario. This should be a meaningful and descriptive name for the test.

- `Description`: A brief description of the test scenario. This helps users understand the purpose of the test and its characteristics.

- `Test cases`: Test cases within the scenario, each represented by a key-value pair. The key is the name of environment variable for specific script that will be executed, and the value is the corresponding test parameters, which can include VU counts and test duration specifications.
### Example:
```json
{
   "BC_K6_SMOKE_TEST": {
        "description": "small test with 2 VUs count",
        "BC_K6_CMCO_CREATE_CO_DELETE_CO": "2",
        "BC_K6_ACADEMIC_RECORD_ACCELERATED_COMPLETE_AR": "2",
    },
    // ... additional test configurations ...
}
```
---
# How to run the tests from bamboo job

1. Job:  [Performance Testing](https://panda.bc.edu:8443/browse/EAGLEAPPSPERFORMANCETESTING-PERFTEST)
- Custom Variables to Know
    - BC_K6_LOAD_TEST : To run test with load test
    - K6_OUTPUT : To get output report and send it to prometheius
    - bcsis_eagleapps_ref : (Default is set to develop) To test for your specific branch, give here branch name.
    - Specific script env var name to run required script : e.g BC_K6_FINAL_EXAMS_DISPLAY_FINAL_EXAMS

NOTE: For env var values as
    1. `true/yes/1` returns `true`
    2. `false/no/0` returns `false`
- For daily regression test run : 
    1. Open [Performance Testing](https://panda.bc.edu:8443/browse/EAGLEAPPSPERFORMANCETESTING-PERFTEST) job & Choose to Run Customized...
    2. Set following env vars
        - BC_K6_LOAD_TEST = 1
        - K6_OUTPUT = true
    3. Run the job
- For daily regression test run on custom branch: 
    1. Open [Performance Testing](https://panda.bc.edu:8443/browse/EAGLEAPPSPERFORMANCETESTING-PERFTEST) job & Choose to Run Customized...
    2. Set following env vars
        - BC_K6_LOAD_TEST = 1
        - bcsis_eagleapps_ref : <branch_name> e.g. EAPE-2532-k6-script-to-schedule-final-exams
    3. Run the job
- For custom (branch+script) test run : 
    1. Open [Performance Testing](https://panda.bc.edu:8443/browse/EAGLEAPPSPERFORMANCETESTING-PERFTEST) job & Choose to Run Customized...
    2. Set following env vars -
        - bcsis_eagleapps_ref = <branch_name> e.g. EAPE-2532-k6-script-to-schedule-final-exams
        - <script_env_var> = VUs , e.g. BC_K6_FINAL_EXAMS_DISPLAY_FINAL_EXAMS = 1
    3. Run the job
    
---
# Steps are necessary to add new test script
1. Infra : [Refer this PR](https://git.bc.edu:7993/projects/INFRA/repos/eagleapps/pull-requests/2485/diff#bin/k6_performance_test) 
    1. Add script specific env var name in required files. e.g. `performance_load_test.yml` and `k6_performance_test` in infra repo. 
2. EagleApps : [Refer this PR](https://git.bc.edu:7993/projects/BCSIS/repos/eagleapps/pull-requests/1838/diff#automated-tests/load-tests/threshold.json)
    1. Add script.js in required `"/backend-tests/module_name"` & its data.json file in `"/data/json/module_name"`.
    2. Add thresholds or configurations as required.
    3. Export script function in main.js, add its env in function `getTestConfigs()` as required.





