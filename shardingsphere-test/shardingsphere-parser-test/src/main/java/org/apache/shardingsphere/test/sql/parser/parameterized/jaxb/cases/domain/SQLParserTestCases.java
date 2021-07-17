/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.shardingsphere.test.sql.parser.parameterized.jaxb.cases.domain;

import com.google.common.base.Preconditions;
import lombok.Getter;
import org.apache.shardingsphere.test.sql.parser.parameterized.jaxb.cases.domain.statement.CommonStatementTestCase;
import org.apache.shardingsphere.test.sql.parser.parameterized.jaxb.cases.domain.statement.SQLParserTestCase;
import org.apache.shardingsphere.test.sql.parser.parameterized.jaxb.cases.domain.statement.dal.ExplainStatementTestCase;
import org.apache.shardingsphere.test.sql.parser.parameterized.jaxb.cases.domain.statement.dal.SetVariableStatementTestCase;
import org.apache.shardingsphere.test.sql.parser.parameterized.jaxb.cases.domain.statement.dal.ShowColumnsStatementTestCase;
import org.apache.shardingsphere.test.sql.parser.parameterized.jaxb.cases.domain.statement.dal.ShowCreateTableStatementTestCase;
import org.apache.shardingsphere.test.sql.parser.parameterized.jaxb.cases.domain.statement.dal.ShowDatabasesStatementTestCase;
import org.apache.shardingsphere.test.sql.parser.parameterized.jaxb.cases.domain.statement.dal.ShowIndexStatementTestCase;
import org.apache.shardingsphere.test.sql.parser.parameterized.jaxb.cases.domain.statement.dal.ShowStatementTestCase;
import org.apache.shardingsphere.test.sql.parser.parameterized.jaxb.cases.domain.statement.dal.ShowTableStatusStatementTestCase;
import org.apache.shardingsphere.test.sql.parser.parameterized.jaxb.cases.domain.statement.dal.ShowTablesStatementTestCase;
import org.apache.shardingsphere.test.sql.parser.parameterized.jaxb.cases.domain.statement.dal.UseStatementTestCase;
import org.apache.shardingsphere.test.sql.parser.parameterized.jaxb.cases.domain.statement.dcl.AlterLoginStatementTestCase;
import org.apache.shardingsphere.test.sql.parser.parameterized.jaxb.cases.domain.statement.dcl.AlterRoleStatementTestCase;
import org.apache.shardingsphere.test.sql.parser.parameterized.jaxb.cases.domain.statement.dcl.AlterUserStatementTestCase;
import org.apache.shardingsphere.test.sql.parser.parameterized.jaxb.cases.domain.statement.dcl.CreateLoginStatementTestCase;
import org.apache.shardingsphere.test.sql.parser.parameterized.jaxb.cases.domain.statement.dcl.CreateRoleStatementTestCase;
import org.apache.shardingsphere.test.sql.parser.parameterized.jaxb.cases.domain.statement.dcl.CreateUserStatementTestCase;
import org.apache.shardingsphere.test.sql.parser.parameterized.jaxb.cases.domain.statement.dcl.DenyUserStatementTestCase;
import org.apache.shardingsphere.test.sql.parser.parameterized.jaxb.cases.domain.statement.dcl.DropLoginStatementTestCase;
import org.apache.shardingsphere.test.sql.parser.parameterized.jaxb.cases.domain.statement.dcl.DropRoleStatementTestCase;
import org.apache.shardingsphere.test.sql.parser.parameterized.jaxb.cases.domain.statement.dcl.DropUserStatementTestCase;
import org.apache.shardingsphere.test.sql.parser.parameterized.jaxb.cases.domain.statement.dcl.GrantStatementTestCase;
import org.apache.shardingsphere.test.sql.parser.parameterized.jaxb.cases.domain.statement.dcl.RenameUserStatementTestCase;
import org.apache.shardingsphere.test.sql.parser.parameterized.jaxb.cases.domain.statement.dcl.RevokeStatementTestCase;
import org.apache.shardingsphere.test.sql.parser.parameterized.jaxb.cases.domain.statement.dcl.SetDefaultRoleStatementTestCase;
import org.apache.shardingsphere.test.sql.parser.parameterized.jaxb.cases.domain.statement.dcl.SetPasswordStatementTestCase;
import org.apache.shardingsphere.test.sql.parser.parameterized.jaxb.cases.domain.statement.dcl.SetRoleStatementTestCase;
import org.apache.shardingsphere.test.sql.parser.parameterized.jaxb.cases.domain.statement.ddl.AlterDatabaseStatementTestCase;
import org.apache.shardingsphere.test.sql.parser.parameterized.jaxb.cases.domain.statement.ddl.AlterFunctionStatementTestCase;
import org.apache.shardingsphere.test.sql.parser.parameterized.jaxb.cases.domain.statement.ddl.AlterIndexStatementTestCase;
import org.apache.shardingsphere.test.sql.parser.parameterized.jaxb.cases.domain.statement.ddl.AlterProcedureStatementTestCase;
import org.apache.shardingsphere.test.sql.parser.parameterized.jaxb.cases.domain.statement.ddl.AlterSequenceStatementTestCase;
import org.apache.shardingsphere.test.sql.parser.parameterized.jaxb.cases.domain.statement.ddl.AlterServerStatementTestCase;
import org.apache.shardingsphere.test.sql.parser.parameterized.jaxb.cases.domain.statement.ddl.AlterSessionStatementTestCase;
import org.apache.shardingsphere.test.sql.parser.parameterized.jaxb.cases.domain.statement.ddl.AlterSynonymStatementTestCase;
import org.apache.shardingsphere.test.sql.parser.parameterized.jaxb.cases.domain.statement.ddl.AlterSystemStatementTestCase;
import org.apache.shardingsphere.test.sql.parser.parameterized.jaxb.cases.domain.statement.ddl.AlterTableStatementTestCase;
import org.apache.shardingsphere.test.sql.parser.parameterized.jaxb.cases.domain.statement.ddl.AnalyzeStatementTestCase;
import org.apache.shardingsphere.test.sql.parser.parameterized.jaxb.cases.domain.statement.ddl.AssociateStatisticsStatementTestCase;
import org.apache.shardingsphere.test.sql.parser.parameterized.jaxb.cases.domain.statement.ddl.AuditStatementTestCase;
import org.apache.shardingsphere.test.sql.parser.parameterized.jaxb.cases.domain.statement.ddl.CommentStatementTestCase;
import org.apache.shardingsphere.test.sql.parser.parameterized.jaxb.cases.domain.statement.ddl.CreateDatabaseStatementTestCase;
import org.apache.shardingsphere.test.sql.parser.parameterized.jaxb.cases.domain.statement.ddl.CreateFunctionStatementTestCase;
import org.apache.shardingsphere.test.sql.parser.parameterized.jaxb.cases.domain.statement.ddl.CreateIndexStatementTestCase;
import org.apache.shardingsphere.test.sql.parser.parameterized.jaxb.cases.domain.statement.ddl.CreateProcedureStatementTestCase;
import org.apache.shardingsphere.test.sql.parser.parameterized.jaxb.cases.domain.statement.ddl.CreateSequenceStatementTestCase;
import org.apache.shardingsphere.test.sql.parser.parameterized.jaxb.cases.domain.statement.ddl.CreateServerStatementTestCase;
import org.apache.shardingsphere.test.sql.parser.parameterized.jaxb.cases.domain.statement.ddl.CreateTableStatementTestCase;
import org.apache.shardingsphere.test.sql.parser.parameterized.jaxb.cases.domain.statement.ddl.CreateTriggerStatementTestCase;
import org.apache.shardingsphere.test.sql.parser.parameterized.jaxb.cases.domain.statement.ddl.CreateViewStatementTestCase;
import org.apache.shardingsphere.test.sql.parser.parameterized.jaxb.cases.domain.statement.ddl.DisassociateStatisticsStatementTestCase;
import org.apache.shardingsphere.test.sql.parser.parameterized.jaxb.cases.domain.statement.ddl.DropDatabaseStatementTestCase;
import org.apache.shardingsphere.test.sql.parser.parameterized.jaxb.cases.domain.statement.ddl.DropFunctionStatementTestCase;
import org.apache.shardingsphere.test.sql.parser.parameterized.jaxb.cases.domain.statement.ddl.DropIndexStatementTestCase;
import org.apache.shardingsphere.test.sql.parser.parameterized.jaxb.cases.domain.statement.ddl.DropProcedureStatementTestCase;
import org.apache.shardingsphere.test.sql.parser.parameterized.jaxb.cases.domain.statement.ddl.DropSequenceStatementTestCase;
import org.apache.shardingsphere.test.sql.parser.parameterized.jaxb.cases.domain.statement.ddl.DropServerStatementTestCase;
import org.apache.shardingsphere.test.sql.parser.parameterized.jaxb.cases.domain.statement.ddl.DropTableStatementTestCase;
import org.apache.shardingsphere.test.sql.parser.parameterized.jaxb.cases.domain.statement.ddl.DropTriggerStatementTestCase;
import org.apache.shardingsphere.test.sql.parser.parameterized.jaxb.cases.domain.statement.ddl.DropViewStatementTestCase;
import org.apache.shardingsphere.test.sql.parser.parameterized.jaxb.cases.domain.statement.ddl.NoAuditStatementTestCase;
import org.apache.shardingsphere.test.sql.parser.parameterized.jaxb.cases.domain.statement.ddl.TruncateStatementTestCase;
import org.apache.shardingsphere.test.sql.parser.parameterized.jaxb.cases.domain.statement.distsql.ral.ShowScalingJobListStatementTestCase;
import org.apache.shardingsphere.test.sql.parser.parameterized.jaxb.cases.domain.statement.distsql.rdl.alter.AlterDataBaseDiscoveryRuleStatementTestCase;
import org.apache.shardingsphere.test.sql.parser.parameterized.jaxb.cases.domain.statement.distsql.rdl.alter.AlterEncryptRuleStatementTestCase;
import org.apache.shardingsphere.test.sql.parser.parameterized.jaxb.cases.domain.statement.distsql.rdl.alter.AlterReadWriteSplittingRuleStatementTestCase;
import org.apache.shardingsphere.test.sql.parser.parameterized.jaxb.cases.domain.statement.distsql.rdl.alter.AlterShardingBindingTableRulesStatementTestCase;
import org.apache.shardingsphere.test.sql.parser.parameterized.jaxb.cases.domain.statement.distsql.rdl.alter.AlterShardingBroadcastTableRulesStatementTestCase;
import org.apache.shardingsphere.test.sql.parser.parameterized.jaxb.cases.domain.statement.distsql.rdl.alter.AlterShardingTableRuleStatementTestCase;
import org.apache.shardingsphere.test.sql.parser.parameterized.jaxb.cases.domain.statement.distsql.rdl.create.AddResourceStatementTestCase;
import org.apache.shardingsphere.test.sql.parser.parameterized.jaxb.cases.domain.statement.distsql.rdl.create.CreateDataBaseDiscoveryRuleStatementTestCase;
import org.apache.shardingsphere.test.sql.parser.parameterized.jaxb.cases.domain.statement.distsql.rdl.create.CreateEncryptRuleStatementTestCase;
import org.apache.shardingsphere.test.sql.parser.parameterized.jaxb.cases.domain.statement.distsql.rdl.create.CreateReadWriteSplittingRuleStatementTestCase;
import org.apache.shardingsphere.test.sql.parser.parameterized.jaxb.cases.domain.statement.distsql.rdl.create.CreateShardingBindingTableRulesStatementTestCase;
import org.apache.shardingsphere.test.sql.parser.parameterized.jaxb.cases.domain.statement.distsql.rdl.create.CreateShardingBroadcastTableRulesStatementTestCase;
import org.apache.shardingsphere.test.sql.parser.parameterized.jaxb.cases.domain.statement.distsql.rdl.create.CreateShardingTableRuleStatementTestCase;
import org.apache.shardingsphere.test.sql.parser.parameterized.jaxb.cases.domain.statement.distsql.rdl.drop.DropDataBaseDiscoveryRuleStatementTestCase;
import org.apache.shardingsphere.test.sql.parser.parameterized.jaxb.cases.domain.statement.distsql.rdl.drop.DropEncryptRuleStatementTestCase;
import org.apache.shardingsphere.test.sql.parser.parameterized.jaxb.cases.domain.statement.distsql.rdl.drop.DropReadWriteSplittingRuleStatementTestCase;
import org.apache.shardingsphere.test.sql.parser.parameterized.jaxb.cases.domain.statement.distsql.rdl.drop.DropResourceStatementTestCase;
import org.apache.shardingsphere.test.sql.parser.parameterized.jaxb.cases.domain.statement.distsql.rdl.drop.DropShardingBindingTableRulesStatementTestCase;
import org.apache.shardingsphere.test.sql.parser.parameterized.jaxb.cases.domain.statement.distsql.rdl.drop.DropShardingBroadcastTableRulesStatementTestCase;
import org.apache.shardingsphere.test.sql.parser.parameterized.jaxb.cases.domain.statement.distsql.rdl.drop.DropShardingTableRuleStatementTestCase;
import org.apache.shardingsphere.test.sql.parser.parameterized.jaxb.cases.domain.statement.distsql.rql.ShowDataBaseDiscoveryRulesStatementTestCase;
import org.apache.shardingsphere.test.sql.parser.parameterized.jaxb.cases.domain.statement.distsql.rql.ShowEncryptRulesStatementTestCase;
import org.apache.shardingsphere.test.sql.parser.parameterized.jaxb.cases.domain.statement.distsql.rql.ShowReadWriteSplittingRulesStatementTestCase;
import org.apache.shardingsphere.test.sql.parser.parameterized.jaxb.cases.domain.statement.distsql.rql.ShowShardingBindingTableRulesStatementTestCase;
import org.apache.shardingsphere.test.sql.parser.parameterized.jaxb.cases.domain.statement.distsql.rql.ShowShardingBroadcastTableRulesStatementTestCase;
import org.apache.shardingsphere.test.sql.parser.parameterized.jaxb.cases.domain.statement.distsql.rql.ShowShardingTableRulesStatementTestCase;
import org.apache.shardingsphere.test.sql.parser.parameterized.jaxb.cases.domain.statement.dml.CallStatementTestCase;
import org.apache.shardingsphere.test.sql.parser.parameterized.jaxb.cases.domain.statement.dml.DeleteStatementTestCase;
import org.apache.shardingsphere.test.sql.parser.parameterized.jaxb.cases.domain.statement.dml.InsertStatementTestCase;
import org.apache.shardingsphere.test.sql.parser.parameterized.jaxb.cases.domain.statement.dml.MergeStatementTestCase;
import org.apache.shardingsphere.test.sql.parser.parameterized.jaxb.cases.domain.statement.dml.SelectStatementTestCase;
import org.apache.shardingsphere.test.sql.parser.parameterized.jaxb.cases.domain.statement.dml.UpdateStatementTestCase;
import org.apache.shardingsphere.test.sql.parser.parameterized.jaxb.cases.domain.statement.tcl.BeginTransactionStatementTestCase;
import org.apache.shardingsphere.test.sql.parser.parameterized.jaxb.cases.domain.statement.tcl.CommitStatementTestCase;
import org.apache.shardingsphere.test.sql.parser.parameterized.jaxb.cases.domain.statement.tcl.RollbackStatementTestCase;
import org.apache.shardingsphere.test.sql.parser.parameterized.jaxb.cases.domain.statement.tcl.SavepointStatementTestCase;
import org.apache.shardingsphere.test.sql.parser.parameterized.jaxb.cases.domain.statement.tcl.SetAutoCommitStatementTestCase;
import org.apache.shardingsphere.test.sql.parser.parameterized.jaxb.cases.domain.statement.tcl.SetConstraintsStatementTestCase;
import org.apache.shardingsphere.test.sql.parser.parameterized.jaxb.cases.domain.statement.tcl.SetTransactionStatementTestCase;
import org.apache.shardingsphere.test.sql.parser.parameterized.jaxb.cases.domain.statement.tcl.XATestCase;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * SQL parser test cases.
 */
@XmlRootElement(name = "sql-parser-test-cases")
@Getter
public final class SQLParserTestCases {
    
    @XmlElement(name = "select")
    private final List<SelectStatementTestCase> selectTestCases = new LinkedList<>();
    
    @XmlElement(name = "update")
    private final List<UpdateStatementTestCase> updateTestCases = new LinkedList<>();
    
    @XmlElement(name = "delete")
    private final List<DeleteStatementTestCase> deleteTestCases = new LinkedList<>();
    
    @XmlElement(name = "insert")
    private final List<InsertStatementTestCase> insertTestCases = new LinkedList<>();
    
    @XmlElement(name = "create-table")
    private final List<CreateTableStatementTestCase> createTableTestCases = new LinkedList<>();
    
    @XmlElement(name = "alter-table")
    private final List<AlterTableStatementTestCase> alterTableTestCases = new LinkedList<>();
    
    @XmlElement(name = "drop-table")
    private final List<DropTableStatementTestCase> dropTableTestCases = new LinkedList<>();
    
    @XmlElement(name = "truncate")
    private final List<TruncateStatementTestCase> truncateTestCases = new LinkedList<>();
    
    @XmlElement(name = "create-index")
    private final List<CreateIndexStatementTestCase> createIndexTestCases = new LinkedList<>();
    
    @XmlElement(name = "alter-index")
    private final List<AlterIndexStatementTestCase> alterIndexTestCases = new LinkedList<>();
    
    @XmlElement(name = "drop-index")
    private final List<DropIndexStatementTestCase> dropIndexTestCases = new LinkedList<>();
    
    @XmlElement(name = "set-constraints")
    private final List<SetConstraintsStatementTestCase> setConstraintsTestCases = new LinkedList<>();
    
    @XmlElement(name = "set-transaction")
    private final List<SetTransactionStatementTestCase> setTransactionTestCases = new LinkedList<>();
    
    @XmlElement(name = "begin-transaction")
    private final List<BeginTransactionStatementTestCase> beginTransactionTestCases = new LinkedList<>();
    
    @XmlElement(name = "set-auto-commit")
    private final List<SetAutoCommitStatementTestCase> setAutoCommitTestCases = new LinkedList<>();
    
    @XmlElement(name = "commit")
    private final List<CommitStatementTestCase> commitTestCases = new LinkedList<>();
    
    @XmlElement(name = "rollback")
    private final List<RollbackStatementTestCase> rollbackTestCases = new LinkedList<>();
    
    @XmlElement(name = "savepoint")
    private final List<SavepointStatementTestCase> savepointTestCases = new LinkedList<>();
    
    @XmlElement(name = "grant")
    private final List<GrantStatementTestCase> grantTestCases = new LinkedList<>();
    
    @XmlElement(name = "revoke")
    private final List<RevokeStatementTestCase> revokeTestCases = new LinkedList<>();
    
    @XmlElement(name = "create-user")
    private final List<CreateUserStatementTestCase> createUserTestCases = new LinkedList<>();
    
    @XmlElement(name = "alter-user")
    private final List<AlterUserStatementTestCase> alterUserTestCases = new LinkedList<>();
    
    @XmlElement(name = "drop-user")
    private final List<DropUserStatementTestCase> dropUserTestCases = new LinkedList<>();
    
    @XmlElement(name = "rename-user")
    private final List<RenameUserStatementTestCase> renameUserTestCases = new LinkedList<>();
    
    @XmlElement(name = "deny-user")
    private final List<DenyUserStatementTestCase> denyUserTestCases = new LinkedList<>();
    
    @XmlElement(name = "create-login")
    private final List<CreateLoginStatementTestCase> createLoginTestCases = new LinkedList<>();
    
    @XmlElement(name = "alter-login")
    private final List<AlterLoginStatementTestCase> alterLoginTestCases = new LinkedList<>();
    
    @XmlElement(name = "drop-login")
    private final List<DropLoginStatementTestCase> dropLoginTestCases = new LinkedList<>();
    
    @XmlElement(name = "create-role")
    private final List<CreateRoleStatementTestCase> createRoleTestCases = new LinkedList<>();
    
    @XmlElement(name = "alter-role")
    private final List<AlterRoleStatementTestCase> alterRoleTestCases = new LinkedList<>();
    
    @XmlElement(name = "drop-role")
    private final List<DropRoleStatementTestCase> dropRoleTestCases = new LinkedList<>();
    
    @XmlElement(name = "set-default-role")
    private final List<SetDefaultRoleStatementTestCase> setDefaultRoleTestCases = new LinkedList<>();
    
    @XmlElement(name = "set-role")
    private final List<SetRoleStatementTestCase> setRoleTestCases = new LinkedList<>();
    
    @XmlElement(name = "set-password")
    private final List<SetPasswordStatementTestCase> setPasswordTestCases = new LinkedList<>();
    
    @XmlElement(name = "use")
    private final List<UseStatementTestCase> useTestCases = new LinkedList<>();
    
    @XmlElement(name = "describe")
    private final List<ExplainStatementTestCase> describeTestCases = new LinkedList<>();
    
    @XmlElement(name = "show-databases")
    private final List<ShowDatabasesStatementTestCase> showDatabasesTestCases = new LinkedList<>();
    
    @XmlElement(name = "show-tables")
    private final List<ShowTablesStatementTestCase> showTablesTestCases = new LinkedList<>();
    
    @XmlElement(name = "show-columns")
    private final List<ShowColumnsStatementTestCase> showColumnsTestCases = new LinkedList<>();
    
    @XmlElement(name = "show-create-table")
    private final List<ShowCreateTableStatementTestCase> showCreateTableTestCases = new LinkedList<>();
    
    @XmlElement(name = "show-table-status")
    private final List<ShowTableStatusStatementTestCase> showTableStatusTestCases = new LinkedList<>();
    
    @XmlElement(name = "show-index")
    private final List<ShowIndexStatementTestCase> showIndexTestCases = new LinkedList<>();
    
    @XmlElement(name = "show")
    private final List<ShowStatementTestCase> showTestCases = new LinkedList<>();
    
    @XmlElement(name = "set-variable")
    private final List<SetVariableStatementTestCase> setVariableTestCases = new LinkedList<>();
    
    @XmlElement(name = "common")
    private final List<CommonStatementTestCase> commonTestCases = new LinkedList<>();
    
    @XmlElement(name = "alter-function")
    private final List<AlterFunctionStatementTestCase> alterFunctionTestCases = new LinkedList<>();
    
    @XmlElement(name = "alter-database")
    private final List<AlterDatabaseStatementTestCase> alterDatabaseTestCase = new LinkedList<>();
    
    @XmlElement(name = "alter-procedure")
    private final List<AlterProcedureStatementTestCase> alterProcedureTestCase = new LinkedList<>();
    
    @XmlElement(name = "alter-server")
    private final List<AlterServerStatementTestCase> alterServerTestCase = new LinkedList<>();
    
    @XmlElement(name = "alter-session")
    private final List<AlterSessionStatementTestCase> alterSessionTestCase = new LinkedList<>();
    
    @XmlElement(name = "alter-synonym")
    private final List<AlterSynonymStatementTestCase> alterSynonymTestCase = new LinkedList<>();
    
    @XmlElement(name = "alter-system")
    private final List<AlterSystemStatementTestCase> alterSystemTestCase = new LinkedList<>();
    
    @XmlElement(name = "create-database")
    private final List<CreateDatabaseStatementTestCase> createDatabaseTestCase = new LinkedList<>();
    
    @XmlElement(name = "create-function")
    private final List<CreateFunctionStatementTestCase> createFunctionTestCase = new LinkedList<>();
    
    @XmlElement(name = "create-procedure")
    private final List<CreateProcedureStatementTestCase> createProcedureTestCase = new LinkedList<>();
    
    @XmlElement(name = "create-server")
    private final List<CreateServerStatementTestCase> createServerTestCase = new LinkedList<>();
    
    @XmlElement(name = "create-trigger")
    private final List<CreateTriggerStatementTestCase> createTriggerTestCase = new LinkedList<>();
    
    @XmlElement(name = "create-view")
    private final List<CreateViewStatementTestCase> createViewTestCase = new LinkedList<>();
    
    @XmlElement(name = "drop-database")
    private final List<DropDatabaseStatementTestCase> dropDatabaseTestCase = new LinkedList<>();
    
    @XmlElement(name = "drop-function")
    private final List<DropFunctionStatementTestCase> dropFunctionTestCase = new LinkedList<>();
    
    @XmlElement(name = "drop-procedure")
    private final List<DropProcedureStatementTestCase> dropProcedureTestCase = new LinkedList<>();
    
    @XmlElement(name = "drop-server")
    private final List<DropServerStatementTestCase> dropServerTestCase = new LinkedList<>();
    
    @XmlElement(name = "drop-trigger")
    private final List<DropTriggerStatementTestCase> dropTriggerTestCase = new LinkedList<>();
    
    @XmlElement(name = "drop-view")
    private final List<DropViewStatementTestCase> dropViewTestCase = new LinkedList<>();
    
    @XmlElement(name = "call")
    private final List<CallStatementTestCase> callProcedureTestCase = new LinkedList<>();
    
    @XmlElement(name = "xa")
    private final List<XATestCase> xaTestCase = new LinkedList<>();
    
    @XmlElement(name = "merge")
    private final List<MergeStatementTestCase> mergeTestCase = new LinkedList<>();
    
    @XmlElement(name = "create-sequence")
    private final List<CreateSequenceStatementTestCase> createSequenceTestCase = new LinkedList<>();
    
    @XmlElement(name = "alter-sequence")
    private final List<AlterSequenceStatementTestCase> alterSequenceTestCase = new LinkedList<>();
    
    @XmlElement(name = "drop-sequence")
    private final List<DropSequenceStatementTestCase> dropSequenceTestCase = new LinkedList<>();
    
    @XmlElement(name = "analyze")
    private final List<AnalyzeStatementTestCase> analyzeTestCase = new LinkedList<>();
    
    @XmlElement(name = "associate-statistics")
    private final List<AssociateStatisticsStatementTestCase> associateStatisticsTestCase = new LinkedList<>();
    
    @XmlElement(name = "disassociate-statistics")
    private final List<DisassociateStatisticsStatementTestCase> disassociateStatisticsTestCase = new LinkedList<>();
    
    @XmlElement(name = "audit")
    private final List<AuditStatementTestCase> auditTestCase = new LinkedList<>();

    @XmlElement(name = "no-audit")
    private final List<NoAuditStatementTestCase> noAuditTestCase = new LinkedList<>();

    @XmlElement(name = "comment")
    private final List<CommentStatementTestCase> commentTestCase = new LinkedList<>();
    
    @XmlElement(name = "add-resource")
    private final List<AddResourceStatementTestCase> addResourceTestCase = new LinkedList<>();
    
    @XmlElement(name = "alter-database-discovery-rule")
    private final List<AlterDataBaseDiscoveryRuleStatementTestCase> alterDataBaseDiscoveryRuleTestCase = new LinkedList<>();
    
    @XmlElement(name = "alter-encrypt-rule")
    private final List<AlterEncryptRuleStatementTestCase> alterEncryptRuleTestCase = new LinkedList<>();
    
    @XmlElement(name = "alter-readwrite-splitting-rule")
    private final List<AlterReadWriteSplittingRuleStatementTestCase> alterReadWriteSplittingRuleTestCase = new LinkedList<>();
    
    @XmlElement(name = "alter-sharding-binding-table-rules")
    private final List<AlterShardingBindingTableRulesStatementTestCase> alterShardingBindingTableRulesTestCase = new LinkedList<>();
    
    @XmlElement(name = "alter-sharding-broadcast-table-rules")
    private final List<AlterShardingBroadcastTableRulesStatementTestCase> alterShardingBroadcastTableRulesTestCase = new LinkedList<>();
    
    @XmlElement(name = "alter-sharding-table-rule")
    private final List<AlterShardingTableRuleStatementTestCase> alterShardingTableRuleTestCase = new LinkedList<>();
    
    @XmlElement(name = "create-database-discovery-rule")
    private final List<CreateDataBaseDiscoveryRuleStatementTestCase> createDataBaseDiscoveryRuleTestCase = new LinkedList<>();
    
    @XmlElement(name = "create-encrypt-rule")
    private final List<CreateEncryptRuleStatementTestCase> createEncryptRuleTestCase = new LinkedList<>();
    
    @XmlElement(name = "create-readwrite-splitting-rule")
    private final List<CreateReadWriteSplittingRuleStatementTestCase> createReadWriteSplittingRuleTestCase = new LinkedList<>();
    
    @XmlElement(name = "create-sharding-binding-table-rule")
    private final List<CreateShardingBindingTableRulesStatementTestCase> createShardingBindingTableRulesTestCase = new LinkedList<>();
    
    @XmlElement(name = "create-sharding-broadcast-table-rule")
    private final List<CreateShardingBroadcastTableRulesStatementTestCase> createShardingBroadcastTableRulesTestCase = new LinkedList<>();
    
    @XmlElement(name = "create-sharding-table-rule")
    private final List<CreateShardingTableRuleStatementTestCase> createShardingTableRuleTestCase = new LinkedList<>();
    
    @XmlElement(name = "drop-database-discovery-rule")
    private final List<DropDataBaseDiscoveryRuleStatementTestCase> dropDataBaseDiscoveryRuleTestCase = new LinkedList<>();
    
    @XmlElement(name = "drop-encrypt-rule")
    private final List<DropEncryptRuleStatementTestCase> dropEncryptRuleTestCase = new LinkedList<>();
    
    @XmlElement(name = "drop-readwrite-splitting-rule")
    private final List<DropReadWriteSplittingRuleStatementTestCase> dropReadWriteSplittingRuleTestCase = new LinkedList<>();
    
    @XmlElement(name = "drop-resource")
    private final List<DropResourceStatementTestCase> dropResourceTestCase = new LinkedList<>();
    
    @XmlElement(name = "drop-sharding-binding-table-rules")
    private final List<DropShardingBindingTableRulesStatementTestCase> dropShardingBindingTableRulesTestCase = new LinkedList<>();
    
    @XmlElement(name = "drop-sharding-broadcast-table-rules")
    private final List<DropShardingBroadcastTableRulesStatementTestCase> dropShardingBroadcastTableRulesTestCase = new LinkedList<>();
    
    @XmlElement(name = "drop-sharding-table-rule")
    private final List<DropShardingTableRuleStatementTestCase> dropShardingTableRuleTestCase = new LinkedList<>();
    
    @XmlElement(name = "show-db-discovery-rules")
    private final List<ShowDataBaseDiscoveryRulesStatementTestCase> showDataBaseDiscoveryRulesTestCase = new LinkedList<>();
    
    @XmlElement(name = "show-encrypt-rules")
    private final List<ShowEncryptRulesStatementTestCase> showEncryptRulesTestCase = new LinkedList<>();
    
    @XmlElement(name = "show-readwrite-splitting-rules")
    private final List<ShowReadWriteSplittingRulesStatementTestCase> showReadWriteSplittingRulesTestCase = new LinkedList<>();
    
    @XmlElement(name = "show-sharding-binding-table-rules")
    private final List<ShowShardingBindingTableRulesStatementTestCase> showShardingBindingTableRulesTestCase = new LinkedList<>();
    
    @XmlElement(name = "show-sharding-broadcast-table-rules")
    private final List<ShowShardingBroadcastTableRulesStatementTestCase> showShardingBroadcastTableRulesTestCase = new LinkedList<>();
    
    @XmlElement(name = "show-sharding-table-rules")
    private final List<ShowShardingTableRulesStatementTestCase> showShardingTableRulesTestCase = new LinkedList<>();
    
    @XmlElement(name = "show-sharding-table-rule")
    private final List<ShowShardingTableRulesStatementTestCase> showShardingTableRuleTestCase = new LinkedList<>();
    
    @XmlElement(name = "show-scaling-job-list")
    private final List<ShowScalingJobListStatementTestCase> showScalingJobListStatementTestCase = new LinkedList<>();
    
    /**
     * Get all SQL parser test cases.
     *
     * @return all SQL parser test cases
     */
    public Map<String, SQLParserTestCase> getAllSQLParserTestCases() {
        Map<String, SQLParserTestCase> result = new HashMap<>();
        putAll(selectTestCases, result);
        putAll(updateTestCases, result);
        putAll(deleteTestCases, result);
        putAll(insertTestCases, result);
        putAll(createTableTestCases, result);
        putAll(alterTableTestCases, result);
        putAll(dropTableTestCases, result);
        putAll(truncateTestCases, result);
        putAll(createIndexTestCases, result);
        putAll(alterIndexTestCases, result);
        putAll(dropIndexTestCases, result);
        putAll(setConstraintsTestCases, result);
        putAll(setTransactionTestCases, result);
        putAll(beginTransactionTestCases, result);
        putAll(setAutoCommitTestCases, result);
        putAll(commitTestCases, result);
        putAll(rollbackTestCases, result);
        putAll(savepointTestCases, result);
        putAll(grantTestCases, result);
        putAll(revokeTestCases, result);
        putAll(createUserTestCases, result);
        putAll(alterUserTestCases, result);
        putAll(dropUserTestCases, result);
        putAll(renameUserTestCases, result);
        putAll(denyUserTestCases, result);
        putAll(createLoginTestCases, result);
        putAll(alterLoginTestCases, result);
        putAll(dropLoginTestCases, result);
        putAll(createRoleTestCases, result);
        putAll(alterRoleTestCases, result);
        putAll(dropRoleTestCases, result);
        putAll(setDefaultRoleTestCases, result);
        putAll(setRoleTestCases, result);
        putAll(setPasswordTestCases, result);
        putAll(useTestCases, result);
        putAll(describeTestCases, result);
        putAll(showDatabasesTestCases, result);
        putAll(showTablesTestCases, result);
        putAll(showColumnsTestCases, result);
        putAll(showCreateTableTestCases, result);
        putAll(showTableStatusTestCases, result);
        putAll(showIndexTestCases, result);
        putAll(showTestCases, result);
        putAll(setVariableTestCases, result);
        putAll(commonTestCases, result);
        putAll(alterFunctionTestCases, result);
        putAll(alterServerTestCase, result);
        putAll(alterSessionTestCase, result);
        putAll(alterSynonymTestCase, result);
        putAll(alterSystemTestCase, result);
        putAll(alterProcedureTestCase, result);
        putAll(alterDatabaseTestCase, result);
        putAll(createViewTestCase, result);
        putAll(createTriggerTestCase, result);
        putAll(createServerTestCase, result);
        putAll(createProcedureTestCase, result);
        putAll(createFunctionTestCase, result);
        putAll(createDatabaseTestCase, result);
        putAll(dropViewTestCase, result);
        putAll(dropTriggerTestCase, result);
        putAll(dropServerTestCase, result);
        putAll(dropProcedureTestCase, result);
        putAll(dropFunctionTestCase, result);
        putAll(dropDatabaseTestCase, result);
        putAll(callProcedureTestCase, result);
        putAll(xaTestCase, result);
        putAll(mergeTestCase, result);
        putAll(createSequenceTestCase, result);
        putAll(alterSequenceTestCase, result);
        putAll(dropSequenceTestCase, result);
        putAll(analyzeTestCase, result);
        putAll(associateStatisticsTestCase, result);
        putAll(disassociateStatisticsTestCase, result);
        putAll(auditTestCase, result);
        putAll(noAuditTestCase, result);
        putAll(commentTestCase, result);
        putAll(addResourceTestCase, result);
        putAll(alterDataBaseDiscoveryRuleTestCase, result);
        putAll(alterEncryptRuleTestCase, result);
        putAll(alterReadWriteSplittingRuleTestCase, result);
        putAll(alterShardingBindingTableRulesTestCase, result);
        putAll(alterShardingBroadcastTableRulesTestCase, result);
        putAll(alterShardingTableRuleTestCase, result);
        putAll(createDataBaseDiscoveryRuleTestCase, result);
        putAll(createEncryptRuleTestCase, result);
        putAll(createReadWriteSplittingRuleTestCase, result);
        putAll(createShardingBindingTableRulesTestCase, result);
        putAll(createShardingBroadcastTableRulesTestCase, result);
        putAll(createShardingTableRuleTestCase, result);
        putAll(dropDataBaseDiscoveryRuleTestCase, result);
        putAll(dropResourceTestCase, result);
        putAll(dropEncryptRuleTestCase, result);
        putAll(dropReadWriteSplittingRuleTestCase, result);
        putAll(dropShardingBindingTableRulesTestCase, result);
        putAll(dropShardingBroadcastTableRulesTestCase, result);
        putAll(dropShardingTableRuleTestCase, result);
        putAll(showDataBaseDiscoveryRulesTestCase, result);
        putAll(showEncryptRulesTestCase, result);
        putAll(showReadWriteSplittingRulesTestCase, result);
        putAll(showShardingBindingTableRulesTestCase, result);
        putAll(showShardingBroadcastTableRulesTestCase, result);
        putAll(showShardingTableRulesTestCase, result);
        putAll(showShardingTableRuleTestCase, result);
        putAll(showScalingJobListStatementTestCase, result);
        return result;
    }
    
    private void putAll(final List<? extends SQLParserTestCase> sqlParserTestCases, final Map<String, SQLParserTestCase> target) {
        Map<String, SQLParserTestCase> sqlParserTestCaseMap = getSQLParserTestCases(sqlParserTestCases);
        Collection<String> sqlParserTestCaseIds = new HashSet<>(sqlParserTestCaseMap.keySet());
        sqlParserTestCaseIds.retainAll(target.keySet());
        Preconditions.checkState(sqlParserTestCaseIds.isEmpty(), "Find duplicated SQL Case IDs: %s", sqlParserTestCaseIds);
        target.putAll(sqlParserTestCaseMap);
    }
    
    private Map<String, SQLParserTestCase> getSQLParserTestCases(final List<? extends SQLParserTestCase> sqlParserTestCases) {
        Map<String, SQLParserTestCase> result = new HashMap<>(sqlParserTestCases.size(), 1);
        for (SQLParserTestCase each : sqlParserTestCases) {
            Preconditions.checkState(!result.containsKey(each.getSqlCaseId()), "Find duplicated SQL Case ID: %s", each.getSqlCaseId());
            result.put(each.getSqlCaseId(), each);
        }
        return result;
    }
}
