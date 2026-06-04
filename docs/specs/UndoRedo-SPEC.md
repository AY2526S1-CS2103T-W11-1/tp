---
status: finished
owner: chuckyang123
created: 2026-06-04
updated: 2026-06-04
acceptance_module: UNDOREDO
req_traceability: REQ-UNDOREDO-001
estimation: M (5 story points)
---

# Undo/Redo 功能 SPEC

## 1. 背景与问题

SoCTAssist 是一个面向 NUS CS2103T TA 的学生管理桌面应用，当前支持 17 个命令（学生 CRUD、小组管理、考勤、作业、答疑）。但用户执行错误操作后**无法撤销**，只能手动恢复（如重新 add_student、重新 mark_attendance 等），这在批量操作（如 `mark_all_attendance`、`add_hw i/all`、`clear`）场景下尤为痛苦。

项目 `docs/diagrams/` 目录已有 6 个 Undo/Redo 状态图（UndoRedoState0-5.puml）和 2 个序列图（UndoSequenceDiagram-Logic.puml、UndoSequenceDiagram-Model.puml），描述了基于 `VersionedAddressBook` 的状态快照方案，但代码中**零实现**——没有 `VersionedAddressBook` 类、没有 `undo`/`redo` 命令、`Model`/`ModelManager` 中没有历史管理方法。

## 2. 目标与非目标

- 目标：
  - 实现 `undo` 命令，撤销最近一次**修改型**命令，恢复 AddressBook 到上一状态
  - 实现 `redo` 命令，重新执行刚被 undo 的命令
  - 支持连续多次 undo/redo（最多 20 步，防止内存溢出）
  - 所有 13 个修改型命令自动记录历史：`add_student`、`edit_student`、`delete`、`clear`、`create_group`、`add_to_group`、`add_hw`、`mark_hw`、`delete_hw`、`mark_attendance`、`mark_all_attendance`、`add_consult`、`delete_consult`
  - 只读命令（`list`、`find`、`find_group`、`list_consult`、`help`、`exit`）不记录历史
- 非目标：
  - 不实现细粒度的单字段 undo（如只撤销某个 homework 的状态变更）
  - 不实现跨会话的 undo/redo（关闭应用后历史丢失，这是预期行为）
  - 不实现分支历史（undo 后执行新命令会丢弃 redo 栈，符合线性历史模型）
  - 不实现 `history` 命令（命令历史导航是独立 feature）

## 3. 知识复用检查

- 已复用：
  - `docs/diagrams/UndoRedoState0-5.puml`：状态快照设计参考
  - `docs/diagrams/UndoSequenceDiagram-Logic.puml`：Logic 层调用序列参考
  - `docs/diagrams/UndoSequenceDiagram-Model.puml`：Model 层 VersionedAddressBook 调用序列参考
  - `docs/team/johndoe.md`：功能描述和设计决策参考
  - AddressBook-Level4 原版 `VersionedAddressBook` 实现模式（AB3 的标准扩展）
- 不复用原因：
  - AB3 原版 `VersionedAddressBook` 不包含 Group 和 Consultation 字段，SoCTAssist 的 AddressBook 有三个列表（persons/consultations/groups），须扩展快照范围
  - AB3 原版无历史上限，存在 OOM 风险；SoCTAssist 加 20 步上限

## 4. 现状分析与代码锚点

### 4.1 关键文件

- `src/main/java/seedu/address/model/Model.java:19-253`：Model 接口，需新增 `undoAddressBook()`/`redoAddressBook()`/`canUndoAddressBook()`/`canRedoAddressBook()`/`commitAddressBook()`
- `src/main/java/seedu/address/model/ModelManager.java:32-516`：Model 实现，当前 `addressBook` 字段为 `AddressBook` 类型，需改为 `VersionedAddressBook`
- `src/main/java/seedu/address/model/AddressBook.java:26-389`：数据容器，`resetData()` 已支持深拷贝恢复
- `src/main/java/seedu/address/logic/LogicManager.java:47-63`：命令执行入口，需在 `execute()` 中调用 `model.commitAddressBook()`
- `src/main/java/seedu/address/logic/parser/AddressBookParser.java:67-128`：命令分派，需新增 `undo`/`redo` case
- `src/main/java/seedu/address/logic/commands/Command.java:9-20`：Command 基类，采用 MutatingCommand 标记接口方案
- `src/main/java/seedu/address/logic/commands/CommandResult.java:12-99`：执行结果，`equals()`/`hashCode()` 不含 `showConsultations`，现有 bug 不在本 SPEC 范围

### 4.2 核心数据结构

```
AddressBook
├── UniquePersonList persons          → ObservableList<Person>
├── UniqueConsultationList consultations → ObservableList<Consultation>
└── UniqueGroupList groups            → ObservableList<Group>
```

当前 `ModelManager` 持有 `AddressBook addressBook`（非版本化）。FilteredList 和 SortedList 包装了 AddressBook 的 ObservableList，因此只要替换底层数据源（通过 `resetData()`），UI 会自动刷新。

### 4.3 现有调用链（命令执行）

```
User Input → LogicManager.execute()
  → AddressBookParser.parseCommand() → Command
  → Command.execute(model) → 修改 model 内 AddressBook
  → Storage.saveAddressBook() → 持久化
  → return CommandResult
```

Undo/Redo 后需增加：

```
User Input "undo" → AddressBookParser → UndoCommand
  → UndoCommand.execute(model)
    → model.undoAddressBook()
      → VersionedAddressBook.undo() → resetData(previousState)
    → Storage.saveAddressBook()
  → return CommandResult
```

### 4.4 已有 PlantUML 设计总结

**状态模型**（UndoRedoState0-5）：
- 线性历史栈：`[ab0, ab1, ab2, ...]`，当前指针指向某个状态
- `undo`：指针左移，恢复前一状态
- `redo`：指针右移，恢复后一状态
- 执行新修改命令时：截断指针右侧历史，追加新状态
- 只读命令不改变状态
- `clear` 后截断后续历史（State ab2 被删除，替换为 ab3）

**序列图**（UndoSequenceDiagram-Logic/Model）：
- Logic 层：`LogicManager → AddressBookParser → UndoCommand → Model.undoAddressBook()`
- Model 层：`Model → VersionedAddressBook.undo() → resetData(ReadOnlyAddressBook)`

## 5. 方案设计

### 5.1 高层设计

采用 **状态快照（Memento）模式**，与 AB4 标准实现一致：

```
┌──────────────────────────────────────────────────────────┐
│                    VersionedAddressBook                    │
│  extends AddressBook                                      │
│  ┌──────────────────────────────────────────────────────┐ │
│  │  List<ReadOnlyAddressBook> addressBookStateList       │ │
│  │  int currentStatePointer                              │ │
│  │  static final int MAX_HISTORY = 20                    │ │
│  └──────────────────────────────────────────────────────┘ │
│  + commit()     : 记录当前状态快照，截断 redo 栈            │
│  + undo()       : 指针左移，resetData 到前一状态           │
│  + redo()       : 指针右移，resetData 到后一状态           │
│  + canUndo()    : 指针 > 0                               │
│  + canRedo()    : 指针 < size - 1                        │
└──────────────────────────────────────────────────────────┘
```

### 5.2 详细方案

#### 5.2.1 新增 `VersionedAddressBook` 类

```java
package seedu.address.model;

public class VersionedAddressBook extends AddressBook {
    private static final int MAX_HISTORY = 20;
    private final List<ReadOnlyAddressBook> addressBookStateList;
    private int currentStatePointer;

    public VersionedAddressBook(ReadOnlyAddressBook initialState) {
        super(initialState);
        addressBookStateList = new ArrayList<>();
        addressBookStateList.add(new AddressBook(initialState));
        currentStatePointer = 0;
    }

    /** 在每次修改型命令执行后调用，保存当前状态快照 */
    public void commit() {
        // 截断 redo 栈（移除 pointer 右侧所有状态）
        for (int i = addressBookStateList.size() - 1; i > currentStatePointer; i--) {
            addressBookStateList.remove(i);
        }
        // 添加新快照
        addressBookStateList.add(new AddressBook(this));
        currentStatePointer++;
        // 超出上限时移除最旧状态
        while (addressBookStateList.size() > MAX_HISTORY + 1) {
            addressBookStateList.remove(0);
            currentStatePointer--;
        }
    }

    /** 恢复到前一状态 */
    public void undo() {
        if (!canUndo()) throw new NoUndoableStateException();
        currentStatePointer--;
        resetData(addressBookStateList.get(currentStatePointer));
    }

    /** 恢复到后一状态 */
    public void redo() {
        if (!canRedo()) throw new NoRedoableStateException();
        currentStatePointer++;
        resetData(addressBookStateList.get(currentStatePointer));
    }

    public boolean canUndo() { return currentStatePointer > 0; }
    public boolean canRedo() { return currentStatePointer < addressBookStateList.size() - 1; }
}
```

#### 5.2.2 修改 `Model` 接口

新增 5 个方法：

```java
void commitAddressBook();       // 修改型命令执行后调用
void undoAddressBook();         // 撤销
void redoAddressBook();         // 重做
boolean canUndoAddressBook();   // 是否可撤销
boolean canRedoAddressBook();   // 是否可重做
```

#### 5.2.3 修改 `ModelManager`

- `addressBook` 字段类型从 `AddressBook` 改为 `VersionedAddressBook`
- 构造函数初始化 `VersionedAddressBook`，并在初始状态后调用一次 `commit()`（确保初始状态可 undo 到）
- 实现 5 个新方法，委托给 `VersionedAddressBook`
- `equals()` 中须包含 `VersionedAddressBook` 的状态指针比较

#### 5.2.4 修改 `LogicManager.execute()`

```java
@Override
public CommandResult execute(String commandText) throws CommandException, ParseException {
    Command command = addressBookParser.parseCommand(commandText);
    CommandResult commandResult = command.execute(model);

    // 修改型命令执行成功后提交状态
    if (command instanceof MutatingCommand) {
        model.commitAddressBook();
    }

    try {
        storage.saveAddressBook(model.getAddressBook());
    } catch (IOException e) { ... }

    return commandResult;
}
```

#### 5.2.5 `MutatingCommand` 标记接口

```java
package seedu.address.logic.commands;

/** 标记修改 AddressBook 状态的命令 */
public interface MutatingCommand {}
```

13 个修改型命令实现此接口：`AddCommand`、`EditCommand`、`DeleteCommand`、`ClearCommand`、`CreateGroupCommand`、`AddToGroupCommand`、`AddHomeworkCommand`、`MarkHomeworkCommand`、`DeleteHomeworkCommand`、`MarkAttendanceCommand`、`MarkAllAttendanceCommand`、`AddConsultationCommand`、`DeleteConsultationCommand`。

`UndoCommand` 和 `RedoCommand` **不实现**此接口（undo/redo 自身不产生新历史，但 undo 后执行新命令会通过 `commit()` 截断 redo 栈）。

#### 5.2.6 新增 `UndoCommand` 和 `RedoCommand`

```java
public class UndoCommand extends Command {
    public static final String COMMAND_WORD = "undo";
    public static final String MESSAGE_SUCCESS = "Undo successful!";
    public static final String MESSAGE_FAILURE = "No commands to undo!";

    @Override
    public CommandResult execute(Model model) throws CommandException {
        if (!model.canUndoAddressBook()) {
            throw new CommandException(MESSAGE_FAILURE);
        }
        model.undoAddressBook();
        return new CommandResult(MESSAGE_SUCCESS);
    }
}

public class RedoCommand extends Command {
    public static final String COMMAND_WORD = "redo";
    public static final String MESSAGE_SUCCESS = "Redo successful!";
    public static final String MESSAGE_FAILURE = "No commands to redo!";

    @Override
    public CommandResult execute(Model model) throws CommandException {
        if (!model.canRedoAddressBook()) {
            throw new CommandException(MESSAGE_FAILURE);
        }
        model.redoAddressBook();
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
```

#### 5.2.7 修改 `AddressBookParser`

新增两个 case：

```java
case UndoCommand.COMMAND_WORD:
    return new UndoCommand();
case RedoCommand.COMMAND_WORD:
    return new RedoCommand();
```

#### 5.2.8 异常类

```java
public class NoUndoableStateException extends RuntimeException {
    public NoUndoableStateException() { super("Current state pointer at start of addressBookStateList"); }
}
public class NoRedoableStateException extends RuntimeException {
    public NoRedoableStateException() { super("Current state pointer at end of addressBookStateList"); }
}
```

### 5.3 失败 & 回滚

| 故障场景 | 处理方式 |
|----------|----------|
| undo 时已在最早状态 | `canUndoAddressBook()` 返回 false，CommandException 提示 "No commands to undo!" |
| redo 时已在最新状态 | `canRedoAddressBook()` 返回 false，CommandException 提示 "No commands to redo!" |
| undo 后执行新修改命令 | `commit()` 截断 redo 栈（移除 pointer 右侧所有状态），线性历史保证一致性 |
| 历史超过 20 步 | `commit()` 自动淘汰最旧状态，指针同步调整 |
| AddressBook 数据损坏（resetData 失败） | 由 AddressBook.resetData() 的现有异常机制处理 |
| 回滚方式 | 删除 `VersionedAddressBook`，恢复 `ModelManager` 中 `addressBook` 类型为 `AddressBook`，移除所有 MutatingCommand 接口引用 |

### 5.4 灰度策略与发布计划

- **阶段 1**：仅开放 undo/redo 命令，不改变现有命令行为（所有修改型命令自动参与历史记录）
- **阶段 2**：在状态栏显示 "可撤销 X 步 / 可重做 Y 步" 提示
- **无功能开关**：undo/redo 是纯增量功能，不影响现有命令行为，直接全量发布
- **发布方式**：随下一个版本（v1.4）一起发布，无需灰度

## 6. Plan 输入约束

- 文件改动范围：
  - 新增：`VersionedAddressBook.java`、`NoUndoableStateException.java`、`NoRedoableStateException.java`、`UndoCommand.java`、`RedoCommand.java`、`MutatingCommand.java`、`UndoCommandTest.java`、`RedoCommandTest.java`、`VersionedAddressBookTest.java`、`ModelManagerUndoRedoTest.java`
  - 修改：`Model.java`、`ModelManager.java`、`LogicManager.java`、`AddressBookParser.java`、13 个修改型 Command 类（添加 `implements MutatingCommand`）
  - 修改：`docs/UserGuide.md`、`docs/DeveloperGuide.md`
- 不变量：
  - 只读命令不改变 undo/redo 栈
  - `commit()` 必须在修改型命令 `execute()` 成功**之后**调用（LogicManager 层面保证）
  - `VersionedAddressBook` 的 `resetData()` 复用 AddressBook 现有实现，UI 通过 ObservableList 自动刷新
  - 现有 17 个命令的功能和测试不能被破坏
- 必须并行的任务：
  - Model 层（VersionedAddressBook + Model 接口 + ModelManager）和 Logic 层（UndoCommand/RedoCommand + Parser）可并行开发
  - 测试和文档可并行

## 7. 可编号验收标准

- **[ACC-UNDOREDO-001]** T1：Given 执行了 add_student 命令, When 用户输入 undo, Then AddressBook 恢复到 add_student 之前的状态
- **[ACC-UNDOREDO-002]** T2：Given 执行了 undo 命令, When 用户输入 redo, Then AddressBook 恢复到 undo 之前的状态
- **[ACC-UNDOREDO-003]** T3：Given 连续执行了 3 个修改型命令, When 用户连续输入 3 次 undo, Then AddressBook 恢复到 3 个命令之前的状态
- **[ACC-UNDOREDO-004]** T4：Given 连续 undo 了 2 次, When 用户连续输入 2 次 redo, Then AddressBook 恢复到最初状态
- **[ACC-UNDOREDO-005]** T5：Given 执行了 undo, When 用户执行新的修改型命令, Then redo 栈被清空，无法 redo 已撤销的操作
- **[ACC-UNDOREDO-006]** T6：Given 没有可撤销的历史, When 用户输入 undo, Then 显示错误提示 "No commands to undo!"
- **[ACC-UNDOREDO-007]** T7：Given 没有可重做的历史, When 用户输入 redo, Then 显示错误提示 "No commands to redo!"
- **[ACC-UNDOREDO-008]** T8：Given 当前有 N 步可撤销, When 用户执行只读命令（list/find/find_group/list_consult/help/exit）, Then 可撤销步数仍为 N
- **[ACC-UNDOREDO-009]** T9：Given 连续执行了 21 个修改型命令, When 用户连续 undo 20 次, Then 最旧状态已被淘汰，无法 undo 到第 21 步之前的状态
- **[ACC-UNDOREDO-010]** T10：Given 当前显示学生列表, When 用户执行 undo/redo, Then UI 自动刷新显示恢复后的学生列表和答疑列表
- **[ACC-UNDOREDO-011]** T11：Given 执行了 clear 命令清空了所有数据, When 用户输入 undo, Then 所有数据（学生、答疑、小组）完整恢复
- **[ACC-UNDOREDO-012]** T12：Given 当前有 N 步历史, When 用户执行 undo/redo, Then 历史栈长度保持 N，undo/redo 不产生新历史记录
- **[ACC-UNDOREDO-013]** T13：Given 用户执行了 undo, When 检查磁盘上的存储文件, Then 文件内容与 undo 后的 AddressBook 状态一致
- **[ACC-UNDOREDO-014]** T14：Given 遍历全部 13 个修改型命令, When 每个命令执行后, Then canUndoAddressBook() 返回 true，即每个命令均产生历史记录

## 8. Eval 验证方案

| 验收 ID (ref §7) | suite | 验证方式 | evidence |
|---|---|---|---|
| ref:001 | unit | `UndoCommandTest`: 执行 add_student 后 undo，验证 AddressBook 回到空状态 | `build/reports/tests/` |
| ref:002 | unit | `RedoCommandTest`: undo 后 redo，验证 AddressBook 恢复 | `build/reports/tests/` |
| ref:003 | unit | `ModelManagerUndoRedoTest`: 连续 3 次 undo | `build/reports/tests/` |
| ref:004 | unit | `ModelManagerUndoRedoTest`: 连续 undo 后连续 redo | `build/reports/tests/` |
| ref:005 | unit | `ModelManagerUndoRedoTest`: undo 后 add 新 person，验证 canRedo==false | `build/reports/tests/` |
| ref:006 | unit | `UndoCommandTest`: 空历史执行 undo 抛 CommandException | `build/reports/tests/` |
| ref:007 | unit | `RedoCommandTest`: 无 redo 历史执行 redo 抛 CommandException | `build/reports/tests/` |
| ref:008 | unit | `ModelManagerUndoRedoTest`: list/find 后历史栈不变 | `build/reports/tests/` |
| ref:009 | unit | `VersionedAddressBookTest`: 21 次 commit 后最早状态被淘汰 | `build/reports/tests/` |
| ref:010 | integration | `LogicManagerTest`: undo/redo 后 filteredPersons 和 filteredConsultations 正确刷新 | `build/reports/tests/` |
| ref:011 | unit | `UndoCommandTest`: clear 后 undo，验证数据完整恢复 | `build/reports/tests/` |
| ref:012 | unit | `ModelManagerUndoRedoTest`: undo/redo 后历史栈长度不变 | `build/reports/tests/` |
| ref:013 | integration | `LogicManagerTest`: undo 后检查 storage 文件内容与模型一致 | `build/reports/tests/` |
| ref:014 | unit | `ModelManagerUndoRedoTest`: 遍历 13 个 MutatingCommand，每个 commit 后 canUndo==true | `build/reports/tests/` |

## 9. 风险与回滚

- 风险：
  1. **内存占用**：每次 commit 深拷贝整个 AddressBook（含 persons + consultations + groups），20 步历史约 20×内存。对于典型 TA 数据量（50-200 学生），每份快照约 50-200KB，20 步总计 1-4MB，可接受。
  2. **遗漏 commit 调用**：未来新增修改型命令时忘记 `implements MutatingCommand`，导致该命令不可 undo。缓解：MutatingCommand 标记接口 + 代码审查检查清单。
  3. **Group 数据一致性**：AddressBook 中 Group 和 Person 存在双向引用。`resetData()` 须正确重建 Group-Person 关系。当前 `resetData()` 调用 `setPersons()` + `setConsultations()` + `setGroupList()`，但 `setGroupList()` 只设置 Group 列表不重建内部引用，须在实现阶段验证。
  4. **ObservableList 引用**：`ModelManager` 的 `FilteredList` 绑定到 `AddressBook.getPersonList()`，当 `VersionedAddressBook.undo()` 调用 `resetData()` 修改底层 `UniquePersonList` 时，JavaFX 的 ObservableList 会自动触发变更事件，无需手动刷新。
- 回滚：
  1. 移除 `VersionedAddressBook`，恢复 `ModelManager.addressBook` 为 `AddressBook` 类型
  2. 移除 `Model` 接口中 5 个 undo/redo 方法
  3. 移除 `UndoCommand`/`RedoCommand`/`MutatingCommand` 及 Parser 注册
  4. 移除 `LogicManager` 中的 commit 逻辑
  5. 移除所有 MutatingCommand 接口引用
  6. 回退后 `gradle build` 应能通过

## 10. 审核问题与待确认决策

### Agent 已自答的问题

| ID | 问题 | Agent 答案 | confidence | evidence |
|---|---|---|---|---|
| RQ-UNDOREDO-AUTO-001 | 使用 MutatingCommand 标记接口还是 Command.isMutating() 方法？ | 使用标记接口 `MutatingCommand`。原因：(1) 编译期检查，新增命令若忘记实现则无法在 LogicManager 中被识别；(2) 比 `isMutating()` 方法更声明式，不依赖运行时返回值；(3) 与 AB4 标准实现一致 | 高 | AB4 标准 + PlantUML 设计 |
| RQ-UNDOREDO-AUTO-002 | 历史上限设为多少？ | 20 步。原因：(1) 典型 TA 数据 50-200 学生，20 步约 1-4MB 内存可接受；(2) 超过 20 步的 undo 场景极少；(3) 防止 OOM | 高 | 内存估算 |
| RQ-UNDOREDO-AUTO-003 | undo/redo 后 FilteredList 是否须手动刷新？ | 不须手动刷新。`resetData()` 修改 `UniquePersonList` 内部 ObservableList，FilteredList 自动感知变更 | 高 | JavaFX ObservableList 机制 + 现有代码中其他命令（如 deletePerson）也不手动刷新 FilteredList |
| RQ-UNDOREDO-AUTO-004 | VersionedAddressBook 应继承 AddressBook 还是组合？ | 继承。原因：(1) 与 AB4 标准一致；(2) ModelManager 已依赖 AddressBook API，继承可最小化改动；(3) resetData() 等 protected 方法可复用 | 高 | AB4 标准 + PlantUML 设计 |
| RQ-UNDOREDO-AUTO-005 | undo/redo 命令是否须要 Parser 类？ | 不须要。undo/redo 无参数，直接在 AddressBookParser 中 `return new UndoCommand()` / `return new RedoCommand()` 即可 | 高 | AB4 标准 + 现有 HelpCommand/ExitCommand 同样无 Parser |
| RQ-UNDOREDO-AUTO-006 | commit 应在 Command.execute() 内部调用还是在 LogicManager 中调用？ | 在 LogicManager.execute() 中调用。原因：(1) 避免每个 Command 都重复调用 commit；(2) 确保 commit 只在 execute 成功后调用（execute 抛异常时不 commit）；(3) 与 AB4 标准一致 | 高 | AB4 标准 + LogicManager 代码 |

### 须要你审核的点

| ID | 优先级 | 背景 | 问题 | 选项A | 选项A优点 | 选项A缺点 | 选项B | 选项B优点 | 选项B缺点 | 推荐 | 不回答的影响 |
|---|---|---|---|---|---|---|---|---|---|---|---|
| RQ-UNDOREDO-001 | P0 | 当前 AddressBook.resetData() 调用 setPersons()+setConsultations()+setGroupList()，但 Group 内部的 students 列表（UniquePersonList）在 setGroupList() 后是否正确重建取决于反序列化时 Group 是否已包含完整 Person 引用 | Group 的 Person 引用在 VersionedAddressBook 快照恢复后是否能保持一致？选项A：验证现有 resetData 足够，若 Group 数据完整则无需修改；选项B：在 resetData 中显式重建 Group 到 Person 引用 | 验证现有 resetData 足够，不修改 | 改动最小，符合现有设计 | 若 Group 反序列化不完整会出现 bug | 在 resetData 中显式重建 Group 到 Person 引用 | 保证一致性，不依赖外部数据格式 | 增加 resetData 复杂度，有引入新 bug 的风险 | A | undo/redo 后 Group 显示数据不正确 |
| RQ-UNDOREDO-002 | P1 | 项目中 johndoe.md 提到 "Added the ability to undo/redo"，但代码无实现。这表明是占位描述而非已实现后删除 | 是否须要参考 AB4 原版 VersionedAddressBook 实现？选项A：参考 AB4 标准实现但适配 SoCTAssist 的三列表结构；选项B：完全自行设计，不参考 AB4 | 参考 AB4 标准实现并适配 | 与课程标准一致，同学和评分者熟悉 | 须遵循 AB4 的设计约束 | 完全自行设计 | 可针对 SoCTAssist 特殊需求优化 | 增加设计工作量，与课程期望不一致的风险 | A | 设计方案存在与课程评分标准不一致的风险 |
| RQ-UNDOREDO-003 | P2 | UserGuide 须要文档化 undo/redo 命令的使用方法和限制 | UserGuide 中 undo/redo 文档的位置？选项A：在"学生管理"之前新增"历史操作"章节；选项B：放在命令汇总表后作为独立章节 | 新增"历史操作"章节在学生管理前 | 按使用频率排列，undo/redo 是高频操作 | 打乱现有章节编号 | 放在命令汇总表后作为独立章节 | 不影响现有章节结构 | undo/redo 位置不显眼 | B | UserGuide 文档不完整 |

## 11. 参考与代码锚点

- AB4 VersionedAddressBook 标准实现：[se-edu/addressbook-level4](https://github.com/se-edu/addressbook-level4/tree/master/src/main/java/seedu/address/model)
- PlantUML 状态图：`docs/diagrams/UndoRedoState0.puml` ~ `UndoRedoState5.puml`
- 序列图：`docs/diagrams/UndoSequenceDiagram-Logic.puml`、`UndoSequenceDiagram-Model.puml`
- johndoe 贡献描述：`docs/team/johndoe.md:12-15`
- 需求追溯：REQ-UNDOREDO-001（用户须要撤销误操作能力）

## 12. 变更记录

| 日期 | 变更摘要 | 作者 |
|---|---|---|
| 2026-06-04 | 初稿 | chuckyang123 |
