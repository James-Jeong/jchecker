# jchecker
~~~
Validate two files!
~~~

## 구조
![스크린샷 2022-07-19 오전 10 48 58](https://user-images.githubusercontent.com/37236920/179646582-f71899d9-2b04-4bf7-b7f2-e0975155b636.png)
  
## 실행 예시
~~~
-------------------------------------------------------
 T E S T S
-------------------------------------------------------
Running core.scenario.ScenarioTest
Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.977 sec
Running core.validation.ValidatorTest
10:28:17.897 [main] DEBUG core.config.ConfigManager - 	Get Config [COMMON] > [ID] : [UCHECK_1]
10:28:17.901 [main] DEBUG core.config.ConfigManager - 	Get Config [COMMON] > [THREAD_POOL_SIZE] : [10]
10:28:17.901 [main] DEBUG core.config.ConfigManager - 	Get Config [COMMON] > [RESULT_FILE_PATH] : [/~/result/test_result.json]
10:28:17.901 [main] DEBUG core.config.ConfigManager - 	Get Config [COMMON] > [DISCARD_KEYWORDS] : [Call-ID:,branch=,transactionId=,callId=,timeStamp=,time=,tag=,[2022-]
10:28:17.901 [main] DEBUG core.config.ConfigManager - Load [COMMON] config...(OK)
10:28:17.901 [main] INFO core.config.ConfigManager - Load config [/~/config/user_conf.ini]
10:28:18.085 [main] DEBUG core.service.ServiceManager - | ShutDownHookHandler(ShutDownHookHandler_2df929f1) is initiated. (target=main)
10:28:18.157 [main] DEBUG core.scheduler.schedule.handler.JobScheduler - [JobScheduler(MAIN)] [Scenario_1] is started.
10:28:18.159 [main] INFO core.service.ServiceManager - (Id:1) Scenario: Scenario{id='1', expectedModel=ValidationModel{fileName='/~/expected/sip_stack_uac.log', LastLineNumber=702, curLineNumber=0}, actualModel=ValidationModel{fileName='/~/actual/sip_stack_uac.log', LastLineNumber=702, curLineNumber=0}, curLineNumber=0, isStarted=true, isSuccess=false}
10:28:18.279 [main] DEBUG core.scheduler.schedule.handler.JobScheduler - [JobScheduler(MAIN)] [Scenario_2] is started.
10:28:18.279 [main] INFO core.service.ServiceManager - (Id:2) Scenario: Scenario{id='2', expectedModel=ValidationModel{fileName='/~/expected/sip_stack_uas.log', LastLineNumber=534, curLineNumber=0}, actualModel=ValidationModel{fileName='/~/actual/sip_stack_uas.log', LastLineNumber=534, curLineNumber=0}, curLineNumber=0, isStarted=true, isSuccess=false}
10:28:18.283 [main] DEBUG core.scheduler.schedule.handler.JobScheduler - [JobScheduler(MAIN)] [Scenario_3] is started.
10:28:18.283 [main] INFO core.service.ServiceManager - (Id:3) Scenario: Scenario{id='3', expectedModel=ValidationModel{fileName='/~/expected/test1.txt', LastLineNumber=8, curLineNumber=0}, actualModel=ValidationModel{fileName='/~/actual/test1.txt', LastLineNumber=9, curLineNumber=0}, curLineNumber=0, isStarted=true, isSuccess=false}
10:28:18.287 [main] INFO core.service.ServiceManager - [UCHECK] START
10:28:18.291 [MAIN_JobExecutor-2] DEBUG core.scheduler.schedule.handler.JobScheduler - [JobScheduler(MAIN)] [Scenario_3] is canceled.
10:28:18.303 [MAIN_JobExecutor-2] DEBUG core.validation.handler.ScenarioHandler - [3] [Scenario_3] Success to finish the scenario.
10:28:24.727 [MAIN_JobExecutor-1] DEBUG core.scheduler.schedule.handler.JobScheduler - [JobScheduler(MAIN)] [Scenario_2] is canceled.
10:28:24.728 [MAIN_JobExecutor-1] DEBUG core.validation.handler.ScenarioHandler - [2] [Scenario_2] Success to finish the scenario.
10:28:26.610 [MAIN_JobExecutor-0] DEBUG core.scheduler.schedule.handler.JobScheduler - [JobScheduler(MAIN)] [Scenario_1] is canceled.
10:28:26.610 [MAIN_JobExecutor-0] DEBUG core.validation.handler.ScenarioHandler - [1] [Scenario_1] Success to finish the scenario.
Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 9.449 sec
10:28:27.331 [ShutDownHookHandler_2df929f1] WARN core.service.ServiceManager - | (ShutDownHookHandler_2df929f1) Process is about to quit. (Ctrl+C)
10:28:27.344 [ShutDownHookHandler_2df929f1] DEBUG core.scheduler.schedule.handler.JobScheduler - [JobScheduler(MAIN)] Success to stop all the jobs.
10:28:27.351 [ShutDownHookHandler_2df929f1] DEBUG core.scheduler.schedule.handler.JobScheduler - [JobScheduler(MAIN)] is finished.
10:28:27.416 [ShutDownHookHandler_2df929f1] INFO core.service.ServiceManager - [RESULT]
{
  "1": true,
  "2": true,
  "3": false,
  "totalScenarioCount": 3
}
10:28:27.424 [ShutDownHookHandler_2df929f1] INFO core.service.ServiceManager - [UCHECK] STOP

Results :

Tests run: 2, Failures: 0, Errors: 0, Skipped: 0

[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  57.839 s
[INFO] Finished at: 2022-07-19T10:28:57+09:00
[INFO] ------------------------------------------------------------------------
~~~
  
