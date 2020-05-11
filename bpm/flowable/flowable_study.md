# flowable学习笔记

## 什么是工作流
工作流属于计算机支持的**协同工作**（Computer Supported Cooperative Work，CSCW）的一部分。

Georgakopoulos给出的工作流定义是：工作流是将一组任务组织起来以完成某个经营过程：定义了任务的触发顺序和触发条件，每个任务可以由一个或多个软件系统完成，也可以由一个或一组人完成，还可以由一个或多个人与软件系统协作完成。

工作流管理联盟(Workflow Management Coalition，WfMC)对工作流给出定义为：工作流是指一类能够**完全自动执行**的经营过程，根据一系列过程**规则**，将**文档、信息或任务**在不同的**执行者**之间进行**传递**与执行。

简单地说，工作流就是一系列相互衔接、自动进行的**业务活动或任务**。
一个工作流包括**一组任务(或活动)** 及它们的相互 **顺序关系**，还包括流程及任务(或活动)的**启动和终止条件**，以及对每个任务(或活动)的描述。

工作流要解决的主要问题是：实现某个**业务目标**。方式是在多个**参与者**之间，利用计算机，按某种预定规则自动传递，触发一系列的任务。经过参与者执行各自的活动，共同协作来完成最终目标。

比如： 产品的上架流程。
- 【业务目标】： 产品上架
- 【业务主体】： 产品
- 【业务流程】： 可行性调研/方案创建->方案确定->研发->测试->上架。
- 【业务活动/参与者】： 可行性调研/方案创建：研究员；方案确定：委员会；研发：开发部；测试：测试部；上架：发行部。

## 使用场景
工作流引擎能支持的业务场景远远不止单据审批，几乎所有涉及到业务流转、多人按流程完成工作的场景背后都可以通过工作流引擎作为支撑。基于工作流引擎，可以搭建客户关系管理系统（CRM）、运输管理系统（TMS）、仓储管理系统（WMS）、财务费用系统等多种复杂业务系统。对于达到一定规模的企业，良好的 BPM（业务流程管理，Business Process Management）体系可以支持创建公司内横跨不同部门的复杂业务流程，既提高工作效率、又可推动企业规范化发展。

## 具体应用
- 1.**关键业务流程**：订单、报价处理、采购处理、合同审核、客户电话处理、供应链管理等
- 2.行政管理类:出差申请、加班申请、请假申请、用车申请、各种办公用品申请、购买申请、日报周报等凡是原来**手工流转处理的行政表单**。
- 3.人事管理类：员工培训安排、绩效考评、职位变动处理、员工档案信息管理等。
- 4.财务相关类：付款请求、应收款处理、日常报销处理、出差报销、预算和计划申请等。
- 5.客户服务类：客户信息管理、客户投诉、请求处理、售后服务管理等。
- 6.特殊服务类：ISO系列对应流程、质量管理对应流程、产品数据信息管理、贸易公司报关处理、物流公司货物跟踪处理等各种**通过表单逐步手工流转完成的任务**
均可应用工作流软件自动规范地实施。

## 工作流分类
按照具体的应用类型，工作流可以分成两个大类：审批流和协同工作流。

### 审批工作流
审批工作流需要注意的是角色权限控制和撤回、驳回等。

### 协同工作流



## 什么是flowable?
Flowable是一个使用Java编写的轻量级业务流程引擎。
支持BPMN 2.0流程定义（用于定义流程的行业XML标准）。
它可以创建流程实例、查询，访问流程实例与相关数据等等。

Flowable是Activiti(Alfresco持有的注册商标)的fork。

flowable核心引擎是一组服务的集合，提供管理与执行业务流程的API.
包括：
- 业务流程引擎（BPMN）
- 决策引擎Decision Model and Notation (DMN)
- 案例模型引擎（CMMN）
- 表单引擎

其中核心是BPMN引擎，其他引擎辅助联动。

## flowable架构
![image](https://flowable.com/open-source/docs/assets/cmmn/cmmn.architecture.png)
Flowable提供了几个web应用，用于演示及介绍Flowable项目提供的功能：

- Flowable IDM: 身份管理应用。为所有Flowable UI应用提供单点登录认证功能，并且为拥有IDM管理员权限的用户提供了管理用户、组与权限的功能。

- Flowable Modeler: 让具有建模权限的用户可以创建流程模型、表单、选择表与应用定义。

- Flowable Task: 运行时任务应用。提供了启动流程实例、编辑任务表单、完成任务，以及查询流程实例与任务的功能。

- Flowable Admin: 管理应用。让具有管理员权限的用户可以查询BPMN、DMN、Form及Content引擎，并提供了许多选项用于修改流程实例、任务、作业等。管理应用通过REST API连接至引擎，并与Flowable Task应用及Flowable REST应用一同部署。

## BPM2.0

节点类型：
- 开始、结束
- 用户任务
- 系统服务
- 条件判断

连线类型
- 有向序列

```html
process
    startEvent
    sequenceFlow
        id
        name
        sourceRef
        targetRef

    userTask
        id
        name
        flowable:assignee
    
    exclusiveGateway
        id
        <conditionExpression></conditionExpression>
    
    serviceTask
        id
        name
        flowable:class

    endEvent
        id

```

## 数据传输

## 参考资料
- [百科](https://baike.baidu.com/item/%E5%B7%A5%E4%BD%9C%E6%B5%81/1094099?fr=aladdin)
- https://zhuanlan.zhihu.com/p/67761802
- https://tkjohn.github.io/flowable-userguide/#_getting_started
- https://github.com/flowable/flowable-engine/tree/master/docker/all-in-one
- https://jeesite.gitee.io/front/flowable/6.4.2/bpmn/index.html#flowableUIApps
- https://www.jianshu.com/p/9757282345c0
