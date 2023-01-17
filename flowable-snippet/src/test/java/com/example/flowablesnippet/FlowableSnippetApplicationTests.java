package com.example.flowablesnippet;

import org.flowable.bpmn.model.BaseElement;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.EndEvent;
import org.flowable.bpmn.model.Process;
import org.flowable.engine.*;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ActivityInstance;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.image.ProcessDiagramGenerator;
import org.flowable.task.api.Task;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SpringBootTest
class FlowableSnippetApplicationTests {
    @Autowired
    ProcessEngine processEngine;
    @Autowired
    ProcessEngineConfiguration processEngineConfiguration;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private HistoryService historyService;


    @Test
    void testDeploy() {
        // 部署德发的零食申请流程
        Deployment deploy = repositoryService.createDeployment().addClasspathResource("processes/DefaSnackApply.bpmn20.xml").deploy();
        // 查询德发的零食申请流程定义
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(deploy.getId()).singleResult();
        System.out.println(processDefinition.getKey()); // defa-snack-apply
    }

    @Test
    void testStart() {
        Map<String, Object> variables = new HashMap<>();
        // 流程图里的变量，必须设置
        variables.put("ownerGroup", "Julius' family");
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("defa-snack-apply", variables);
        System.out.println(processInstance.getId()); // 328b2792-94e7-11ed-a626-005056c00008
    }

    @Test
    void testDeleteRuntimeProcess() {
        runtimeService.deleteProcessInstance("328b2792-94e7-11ed-a626-005056c00008", "Defa没有填写信息");
        Map<String, Object> variables = new HashMap<>();
        // 流程图里的变量，必须设置
        variables.put("ownerGroup", "Julius' family");
        variables.put("catMessage", "德发想吃零食~");
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("defa-snack-apply", variables);
        System.out.println(processInstance.getId()); // 7543d0b8-902d-11ed-ae03-005056c00008
    }

    @Test
    void testGenerateGraph() throws IOException {
        // 查询当前的流程实例
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceId("7543d0b8-902d-11ed-ae03-005056c00008")
                .singleResult();
        // 查询当前的流程模型
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processInstance.getProcessDefinitionId());
        // 获取流程图生成器
        ProcessDiagramGenerator processDiagramGenerator = processEngineConfiguration.getProcessDiagramGenerator();
        // 获取流程中活跃的活动Id集合
        List<String> activeActivityIds = runtimeService.createActivityInstanceQuery()
                .processInstanceId("7543d0b8-902d-11ed-ae03-005056c00008")
                .list()
                .stream()
                .map(ActivityInstance::getActivityId)
                .collect(Collectors.toList());
        // 获取生成图的输入流
        InputStream inputStream = processDiagramGenerator.generateDiagram(bpmnModel, "png", activeActivityIds, Collections.emptyList(), "宋体", "宋体", "黑体", null, 1.0, true);
        // 当前路径输出图
        long copy = Files.copy(inputStream, Paths.get("defa-snack-apply.png"));
        System.out.println(copy);
    }

    @Test
    void testTaskPoolQuery() {
        List<Task> taskList = taskService
                .createTaskQuery()
                .processDefinitionKey("defa-snack-apply")
                .taskCandidateGroup("Julius' family")
                .includeProcessVariables()
                .list();
        List<Map<String, Object>> taskMapList = taskList.stream().map(task -> {
            Map<String, Object> map = new HashMap<>();
            map.put("processInstanceId", task.getProcessInstanceId());
            map.put("taskId", task.getId());
            map.putAll(task.getProcessVariables());
            return map;
        }).collect(Collectors.toList());
        System.out.println(taskMapList.toString()); // [{processInstanceId=7543d0b8-902d-11ed-ae03-005056c00008, catMessage=德发想吃零食~, ownerGroup=Julius' family, taskId=755758bf-902d-11ed-ae03-005056c00008}]
    }

    @Test
    void testTaskClaim() {
        taskService.claim("755758bf-902d-11ed-ae03-005056c00008", "Julius");
    }

    @Test
    void testJuliusTodoTaskQuery() {
        List<Task> taskList = taskService
                .createTaskQuery()
                .processDefinitionKey("defa-snack-apply")
                .taskAssignee("Julius")
                .includeProcessVariables()
                .list();
        List<Map<String, Object>> taskMapList = taskList.stream().map(task -> {
            Map<String, Object> map = new HashMap<>();
            map.put("processInstanceId", task.getProcessInstanceId());
            map.put("taskId", task.getId());
            map.putAll(task.getProcessVariables());
            return map;
        }).collect(Collectors.toList());
        System.out.println(taskMapList.toString()); // [{processInstanceId=7543d0b8-902d-11ed-ae03-005056c00008, catMessage=德发想吃零食~, ownerGroup=Julius' family, taskId=755758bf-902d-11ed-ae03-005056c00008}]
    }

    @Test
    void testComplete() {
        // 会持久化的变量variables
        Map<String, Object> variables = new HashMap<>();
        variables.put("ownerComment", "德发选择你喜欢的零食吧~");
        // 无需持久化的变量transientVariables
        Map<String, Object> transientVariables = new HashMap<>();
        // 流程图里的变量，必须设置
        transientVariables.put("catName", "Defa");
        transientVariables.put("pass_flag", true);
        taskService.complete("755758bf-902d-11ed-ae03-005056c00008", variables, transientVariables);
    }

    @Test
    void testDefaTodoTaskQuery() {
        List<Task> taskList = taskService
                .createTaskQuery()
                .processDefinitionKey("defa-snack-apply")
                .taskAssignee("Defa")
                .includeProcessVariables()
                .list();
        List<Map<String, Object>> taskMapList = taskList.stream().map(task -> {
            Map<String, Object> map = new HashMap<>();
            map.put("processInstanceId", task.getProcessInstanceId());
            map.put("taskId", task.getId());
            map.putAll(task.getProcessVariables());
            return map;
        }).collect(Collectors.toList());
        System.out.println(taskMapList.toString()); // [{processInstanceId=7543d0b8-902d-11ed-ae03-005056c00008, catMessage=德发想吃零食~, ownerComment=德发选择你喜欢的零食吧~, ownerGroup=Julius' family, taskId=f2827e35-91c0-11ed-b1c9-005056c00008}]
    }

    @Test
    void testDefaComplete() {
        // 会持久化的变量variables
        Map<String, Object> variables = new HashMap<>();
        variables.put("catComment", "德发选择了猫草~");
        // 无需持久化的变量transientVariables
        Map<String, Object> transientVariables = new HashMap<>();
        // 流程图里的变量，必须设置
        transientVariables.put("owner2", "Julius' Mom");
        transientVariables.put("selected_snack", "catnip");
        taskService.complete("f2827e35-91c0-11ed-b1c9-005056c00008", variables, transientVariables);
    }

    @Test
    void testMomTodoTask() {
        List<Task> taskList = taskService
                .createTaskQuery()
                .processDefinitionKey("defa-snack-apply")
                .taskAssignee("Julius‘ Mom")
                .includeProcessVariables()
                .list();
        List<Map<String, Object>> taskMapList = taskList.stream().map(task -> {
            Map<String, Object> map = new HashMap<>();
            map.put("processInstanceId", task.getProcessInstanceId());
            map.put("taskId", task.getId());
            map.put("taskName", task.getName());
            map.putAll(task.getProcessVariables());
            return map;
        }).collect(Collectors.toList());
        System.out.println(taskMapList.toString()); // [{processInstanceId=7543d0b8-902d-11ed-ae03-005056c00008, catMessage=德发想吃零食~, catComment=德发选择了猫草~, ownerComment=德发选择你喜欢的零食吧~, ownerGroup=Julius' family, taskName=主人投喂猫草, taskId=6f9884ae-91c3-11ed-bbee-005056c00008}]
    }

    @Test
    void testDelegate() {
        taskService.setVariable("6f9884ae-91c3-11ed-bbee-005056c00008", "momComment", "没有空，Julius你自己去喂一下~");
        taskService.setOwner("6f9884ae-91c3-11ed-bbee-005056c00008", "Julius");
        taskService.setAssignee("6f9884ae-91c3-11ed-bbee-005056c00008", "Julius");
    }

    @Test
    void testReverse() {
        List<ActivityInstance> userTaskList = runtimeService.createActivityInstanceQuery()
                .processInstanceId("7543d0b8-902d-11ed-ae03-005056c00008")
                .activityType("userTask")
                .orderByActivityInstanceStartTime()
                .desc()
                .list();
        runtimeService
                .createChangeActivityStateBuilder()
                .processInstanceId("7543d0b8-902d-11ed-ae03-005056c00008")
                .moveActivityIdTo(userTaskList.get(0).getActivityId(), userTaskList.get(1).getActivityId())
                .localVariable(userTaskList.get(1).getActivityId(), "catName", "Defa")
                .changeState();
    }

    @Test
    void testDeleteRuntime() {
        runtimeService.deleteProcessInstance("7543d0b8-902d-11ed-ae03-005056c00008", "德发不想吃了~");
    }

    @Test
    void testStop() {
        // 查询当前的流程实例
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceId("7543d0b8-902d-11ed-ae03-005056c00008")
                .singleResult();
        Task task = taskService.createTaskQuery().taskId("80ad36d5-91ca-11ed-8685-005056c00008").singleResult();
        // 查询当前的流程模型
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processInstance.getProcessDefinitionId());
        // 查询流程
        Process process = bpmnModel.getProcesses().get(0);
        // 查询流程中的EndEvent元素
        List<String> endEventActivityIds = process
                .getFlowElements()
                .stream()
                .filter(flowElement -> flowElement instanceof EndEvent)
                .map(BaseElement::getId)
                .collect(Collectors.toList());
        taskService.setVariable("80ad36d5-91ca-11ed-8685-005056c00008", "stopComment", "德发不想吃了~");
        // 改变当前的节点到结束节点
        runtimeService
                .createChangeActivityStateBuilder()
                .processInstanceId("7543d0b8-902d-11ed-ae03-005056c00008")
                .moveActivityIdTo(task.getTaskDefinitionKey(), endEventActivityIds.get(0))
                .changeState();
    }

    @Test
    void testDefaHistoricTask() {
        List<HistoricTaskInstance> historicTaskInstances = historyService
                .createHistoricTaskInstanceQuery()
                .processDefinitionKey("defa-snack-apply")
                .taskAssignee("Defa")
                .includeProcessVariables()
                .list();
        List<Map<String, Object>> taskMapList = historicTaskInstances.stream().map(task -> {
            Map<String, Object> map = new HashMap<>();
            map.put("processInstanceId", task.getProcessInstanceId());
            map.put("taskId", task.getId());
            map.put("taskName", task.getName());
            map.putAll(task.getProcessVariables());
            return map;
        }).collect(Collectors.toList());
        System.out.println(taskMapList.toString());// [{processInstanceId=7543d0b8-902d-11ed-ae03-005056c00008, catMessage=德发想吃零食~, catComment=德发选择了猫草~, ownerComment=德发选择你喜欢的零食吧~, ownerGroup=Julius' family, taskName=猫猫选择零食, momComment=没有空，Julius你自己去喂一下~, stopComment=德发不想吃了~, taskId=80ad36d5-91ca-11ed-8685-005056c00008}, {processInstanceId=7543d0b8-902d-11ed-ae03-005056c00008, catMessage=德发想吃零食~, catComment=德发选择了猫草~, ownerComment=德发选择你喜欢的零食吧~, ownerGroup=Julius' family, taskName=猫猫选择零食, momComment=没有空，Julius你自己去喂一下~, stopComment=德发不想吃了~, taskId=f2827e35-91c0-11ed-b1c9-005056c00008}]
    }

    @Test
    void testDeleteHistoricProcess() {
        historyService.deleteHistoricProcessInstance("7543d0b8-902d-11ed-ae03-005056c00008");
    }


}
