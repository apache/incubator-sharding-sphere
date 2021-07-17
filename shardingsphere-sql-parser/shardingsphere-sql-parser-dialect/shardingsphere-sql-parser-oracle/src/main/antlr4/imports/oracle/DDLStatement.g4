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

grammar DDLStatement;

import Symbol, Keyword, OracleKeyword, Literals, BaseRule;

createTable
    : CREATE createTableSpecification TABLE tableName createSharingClause createDefinitionClause createMemOptimizeClause createParentClause
    ;

createIndex
    : CREATE createIndexSpecification INDEX indexName ON createIndexDefinitionClause usableSpecification? invalidationSpecification?
    ;

alterTable
    : ALTER TABLE tableName memOptimizeClause alterDefinitionClause enableDisableClauses
    ;

alterIndex
    : ALTER INDEX indexName alterIndexInformationClause
    ;

dropTable
    : DROP TABLE tableName (CASCADE CONSTRAINTS)? (PURGE)?
    ;
 
dropIndex
    : DROP INDEX indexName ONLINE? FORCE? ((DEFERRED|IMMEDIATE) INVALIDATION)?
    ;

truncateTable
    : TRUNCATE TABLE tableName materializedViewLogClause? dropReuseClause? CASCADE?
    ;

createTableSpecification
    : ((GLOBAL | PRIVATE) TEMPORARY | SHARDED | DUPLICATED)?
    ;

tablespaceClauseWithParen
    : LP_ tablespaceClause RP_
    ;

tablespaceClause
    : TABLESPACE ignoredIdentifier
    ;

createSharingClause
    : (SHARING EQ_ (METADATA | DATA | EXTENDED DATA | NONE))?
    ;

createDefinitionClause
    : createRelationalTableClause | createObjectTableClause | createXMLTypeTableClause
    ;

createXMLTypeTableClause
    : OF? XMLTYPE
      (LP_ (objectProperties) RP_)?
      (XMLTYPE xmlTypeStorageClause)?
      (xmlSchemaSpecClause)?
      (xmlTypeVirtualColumnsClause)?
      (ON COMMIT (DELETE | PRESERVE) ROWS)?
      (oidClause)?
      (oidIndexClause)?
      (physicalProperties)?
      (tableProperties)?
    ;

xmlTypeStorageClause
    : STORE
      (AS ( OBJECT RELATIONAL | ((SECUREFILE | BASICFILE)? (CLOB | BINARY XML) (lobSegname (LP_ lobParameters RP_)? | (LP_ lobParameters RP_))?)))
      | (ALL VARRAYS AS (LOBS | TABLES ))
    ;

xmlSchemaSpecClause
    : (XMLSCHEMA xmlSchemaURLName)? ELEMENT (elementName | xmlSchemaURLName POUND_ elementName)? 
      (STORE ALL VARRAYS AS (LOBS | TABLES))? 
      ((ALLOW | DISALLOW) NONSCHEMA)?
      ((ALLOW | DISALLOW) ANYSCHEMA)?
    ;

xmlTypeVirtualColumnsClause
    : VIRTUAL COLUMNS LP_ (columnName AS LP_ expr RP_ (COMMA_ columnName AS LP_ expr RP_)+) RP_
    ;

oidClause
    : OBJECT IDENTIFIER IS (SYSTEM GENERATED | PRIMARY KEY)
    ;

oidIndexClause
    : OIDINDEX indexName? LP_ (physicalAttributesClause | TABLESPACE tablespaceName)* RP_
    ;

createRelationalTableClause
    : (LP_ relationalProperties RP_)? collationClause? commitClause? physicalProperties? tableProperties?
    ;
    
createMemOptimizeClause
    : (MEMOPTIMIZE FOR READ)? (MEMOPTIMIZE FOR WRITE)? 
    ;    

createParentClause
    : (PARENT tableName)?
    ;

createObjectTableClause
    : OF objectName objectTableSubstitution? 
    (LP_ objectProperties RP_)? (ON COMMIT (DELETE | PRESERVE) ROWS)?
    oidClause? oidIndexClause? physicalProperties? tableProperties?
    ;

relationalProperties
    : relationalProperty (COMMA_ relationalProperty)*
    ;

relationalProperty
    : columnDefinition | virtualColumnDefinition | outOfLineConstraint | outOfLineRefConstraint
    ;

columnDefinition
    : columnName dataType SORT? visibleClause (defaultNullClause expr | identityClause)? (ENCRYPT encryptionSpecification)? (inlineConstraint+ | inlineRefConstraint)?
    ;

visibleClause
    : (VISIBLE | INVISIBLE)?
    ;

defaultNullClause
    : DEFAULT (ON NULL)?
    ;

identityClause
    : GENERATED (ALWAYS | BY DEFAULT (ON NULL)?) AS IDENTITY identifyOptions
    ;

identifyOptions
    : LP_? (identityOption+)? RP_?
    ;

identityOption
    : START WITH (INTEGER_ | LIMIT VALUE)
    | INCREMENT BY INTEGER_
    | MAXVALUE INTEGER_
    | NOMAXVALUE
    | MINVALUE INTEGER_
    | NOMINVALUE
    | CYCLE
    | NOCYCLE
    | CACHE INTEGER_
    | NOCACHE
    | ORDER
    | NOORDER
    ;

encryptionSpecification
    : (USING STRING_)? (IDENTIFIED BY STRING_)? STRING_? (NO? SALT)?
    ;

inlineConstraint
    : (CONSTRAINT ignoredIdentifier)? (NOT? NULL | UNIQUE | primaryKey | referencesClause | CHECK LP_ expr RP_) constraintState?
    ;

referencesClause
    : REFERENCES tableName columnNames? (ON DELETE (CASCADE | SET NULL))?
    ;

constraintState
    : notDeferrable 
    | initiallyClause 
    | RELY | NORELY 
    | usingIndexClause 
    | ENABLE | DISABLE 
    | VALIDATE | NOVALIDATE 
    | exceptionsClause
    ;

notDeferrable
    : NOT? DEFERRABLE
    ;

initiallyClause
    : INITIALLY (IMMEDIATE | DEFERRED)
    ;

exceptionsClause
    : EXCEPTIONS INTO tableName
    ;

usingIndexClause
    : USING INDEX (indexName | createIndexClause)?
    ;

createIndexClause
    :  LP_ createIndex RP_
    ;

inlineRefConstraint
    : SCOPE IS tableName | WITH ROWID | (CONSTRAINT ignoredIdentifier)? referencesClause constraintState?
    ;

virtualColumnDefinition
    : columnName dataType? (GENERATED ALWAYS)? AS LP_ expr RP_ VIRTUAL? inlineConstraint*
    ;

outOfLineConstraint
    : (CONSTRAINT constraintName)?
    (UNIQUE columnNames
    | primaryKey columnNames 
    | FOREIGN KEY columnNames referencesClause
    | CHECK LP_ expr RP_
    ) constraintState?
    ;

outOfLineRefConstraint
    : SCOPE FOR LP_ lobItem RP_ IS tableName
    | REF LP_ lobItem RP_ WITH ROWID
    | (CONSTRAINT constraintName)? FOREIGN KEY lobItemList referencesClause constraintState?
    ;

createIndexSpecification
    : (UNIQUE | BITMAP)?
    ;

clusterIndexClause
    : CLUSTER clusterName indexAttributes?
    ;

indexAttributes
    : (ONLINE | (SORT|NOSORT) | REVERSE | (VISIBLE | INVISIBLE))
    ;

tableIndexClause
    : tableName alias? indexExpressions
    ;

indexExpressions
    : LP_? indexExpression (COMMA_ indexExpression)* RP_?
    ;

indexExpression
    : (columnName | expr) (ASC | DESC)?
    ;

bitmapJoinIndexClause
    : tableName columnSortsClause_ FROM tableAlias WHERE expr
    ;

columnSortsClause_
    : LP_? columnSortClause_ (COMMA_ columnSortClause_)* RP_?
    ;
    
columnSortClause_
    : (tableName | alias)? columnName (ASC | DESC)?
    ;

createIndexDefinitionClause
    : clusterIndexClause | tableIndexClause | bitmapJoinIndexClause
    ;

tableAlias
    : tableName alias? (COMMA_ tableName alias?)*
    ;

alterDefinitionClause
    : (alterTableProperties
    | columnClauses
    | constraintClauses
    | alterTablePartitioning ((DEFERRED| IMMEDIATE) INVALIDATION)?
    | alterExternalTable)?
    ;

alterTableProperties
    : renameTableSpecification | REKEY encryptionSpecification
    ;

renameTableSpecification
    : RENAME TO identifier
    ;

columnClauses
    : operateColumnClause+ | renameColumnClause
    ;

operateColumnClause
    : addColumnSpecification | modifyColumnSpecification | dropColumnClause
    ;

addColumnSpecification
    : ADD LP_ columnOrVirtualDefinitions RP_ columnProperties?
    ;

columnOrVirtualDefinitions
    : columnOrVirtualDefinition (COMMA_ columnOrVirtualDefinition)* 
    ;

columnOrVirtualDefinition
    : columnDefinition | virtualColumnDefinition
    ;

columnProperties
    : columnProperty+
    ;

columnProperty
    : objectTypeColProperties
    ;

objectTypeColProperties
    : COLUMN columnName substitutableColumnClause
    ;

substitutableColumnClause
    : ELEMENT? IS OF TYPE? LP_ ONLY? dataTypeName RP_ | NOT? SUBSTITUTABLE AT ALL LEVELS
    ;

modifyColumnSpecification
    : MODIFY (LP_? modifyColProperties (COMMA_ modifyColProperties)* RP_? | modifyColSubstitutable)
    ;

modifyColProperties
    : columnName dataType? (DEFAULT expr)? (ENCRYPT encryptionSpecification | DECRYPT)? inlineConstraint*
    ;

modifyColSubstitutable
    : COLUMN columnName NOT? SUBSTITUTABLE AT ALL LEVELS FORCE?
    ;

dropColumnClause
    : SET UNUSED columnOrColumnList cascadeOrInvalidate* | dropColumnSpecification
    ;

dropColumnSpecification
    : DROP columnOrColumnList cascadeOrInvalidate* checkpointNumber?
    ;

columnOrColumnList
    : (COLUMN columnName) | columnNames
    ;

cascadeOrInvalidate
    : CASCADE CONSTRAINTS | INVALIDATE
    ;

checkpointNumber
    : CHECKPOINT NUMBER_
    ;

renameColumnClause
    : RENAME COLUMN columnName TO columnName
    ;

constraintClauses
    : addConstraintSpecification | modifyConstraintClause | renameConstraintClause | dropConstraintClause+
    ;

addConstraintSpecification
    : ADD (outOfLineConstraint+ | outOfLineRefConstraint)
    ;

modifyConstraintClause
    : MODIFY constraintOption constraintState+ CASCADE?
    ;

constraintWithName
    : CONSTRAINT constraintName
    ;

constraintOption
    : constraintWithName | constraintPrimaryOrUnique
    ;

constraintPrimaryOrUnique
    : primaryKey | UNIQUE columnNames
    ;

renameConstraintClause
    : RENAME constraintWithName TO ignoredIdentifier
    ;

dropConstraintClause
    : DROP
    (
    constraintPrimaryOrUnique CASCADE? ((KEEP | DROP) INDEX)? | (CONSTRAINT constraintName CASCADE?)
    ) 
    ;

alterExternalTable
    : (addColumnSpecification | modifyColumnSpecification | dropColumnSpecification)+
    ;

objectProperties
    : ((columnName | attributeName) (DEFAULT expr)? (inlineConstraint* | inlineRefConstraint)?)
    | outOfLineConstraint
    | outOfLineRefConstraint
    | supplementalLoggingProps
    ;

alterIndexInformationClause
    : rebuildClause ((DEFERRED|IMMEDIATE) | INVALIDATION)?
    | parallelClause
    | COMPILE
    | (ENABLE | DISABLE)
    | UNUSABLE ONLINE? ((DEFERRED|IMMEDIATE)|INVALIDATION)?
    | (VISIBLE | INVISIBLE)
    | renameIndexClause
    | COALESCE CLEANUP? ONLY? parallelClause?
    | ((MONITORING | NOMONITORING) USAGE)
    | UPDATE BLOCK REFERENCES
    ;

renameIndexClause
    : (RENAME TO indexName)?
    ;
    
objectTableSubstitution
    : NOT? SUBSTITUTABLE AT ALL LEVELS
    ;

memOptimizeClause
    : memOptimizeReadClause? memOptimizeWriteClause?
    ;

memOptimizeReadClause
    : (MEMOPTIMIZE FOR READ | NO MEMOPTIMIZE FOR READ)
    ;

memOptimizeWriteClause
    : (MEMOPTIMIZE FOR WRITE | NO MEMOPTIMIZE FOR WRITE)
    ;

enableDisableClauses
    : (enableDisableClause | enableDisableOthers)?
    ;

enableDisableClause
    : (ENABLE | DISABLE) (VALIDATE |NO VALIDATE)? ((UNIQUE columnName (COMMA_ columnName)*) | PRIMARY KEY | constraintWithName) usingIndexClause? exceptionsClause? CASCADE? ((KEEP | DROP) INDEX)?
    ;

enableDisableOthers
    : (ENABLE | DISABLE) (TABLE LOCK | ALL TRIGGERS | CONTAINER_MAP | CONTAINERS_DEFAULT)
    ;

rebuildClause
    : REBUILD parallelClause?
    ;

parallelClause
    : NOPARALLEL | PARALLEL NUMBER_?
    ;

usableSpecification
    : (USABLE | UNUSABLE)
    ;

invalidationSpecification
    : (DEFERRED | IMMEDIATE) INVALIDATION
    ;

materializedViewLogClause
    : (PRESERVE | PURGE) MATERIALIZED VIEW LOG
    ;

dropReuseClause
    : (DROP (ALL)? | REUSE) STORAGE
    ;

collationClause
    : DEFAULT COLLATION collationName
    ;

commitClause
    : (ON COMMIT (DROP | PRESERVE) ROWS)? (ON COMMIT (DELETE | PRESERVE) ROWS)?
    ;

physicalProperties
    : deferredSegmentCreation? segmentAttributesClause? tableCompression? inmemoryTableClause? ilmClause?
    | deferredSegmentCreation? (organizationClause?|externalPartitionClause?)
    | clusterClause
    ;

deferredSegmentCreation
    : SEGMENT CREATION (IMMEDIATE|DEFERRED)
    ;

segmentAttributesClause
    : ( physicalAttributesClause
    | (TABLESPACE tablespaceName | TABLESPACE SET tablespaceSetName)
    | loggingClause)+
    ;

physicalAttributesClause
    : (PCTFREE NUMBER_ | PCTUSED NUMBER_ | INITRANS NUMBER_ | storageClause)+
    ;

loggingClause
    : LOGGING | NOLOGGING |  FILESYSTEM_LIKE_LOGGING
    ;

storageClause
    : STORAGE LP_
    (INITIAL sizeClause
    | NEXT sizeClause
    | MINEXTENTS NUMBER_
    | MAXEXTENTS (NUMBER_ | UNLIMITED)
    | maxsizeClause
    | PCTINCREASE NUMBER_
    | FREELISTS NUMBER_
    | FREELIST GROUPS NUMBER_
    | OPTIMAL (sizeClause | NULL)?
    | BUFFER_POOL (KEEP | RECYCLE | DEFAULT)
    | FLASH_CACHE (KEEP | NONE | DEFAULT)
    | CELL_FLASH_CACHE (KEEP | NONE | DEFAULT)
    | ENCRYPT
    )+ RP_
    ;

sizeClause
    : NUMBER_ ('K' | 'M' | 'G' | 'T' | 'P' | 'E')?
    ;

maxsizeClause
    : MAXSIZE (UNLIMITED | sizeClause)
    ;

tableCompression
    : COMPRESS
    | ROW STORE COMPRESS (BASIC | ADVANCED)?
    | COLUMN STORE COMPRESS (FOR (QUERY | ARCHIVE) (LOW | HIGH)?)? (NO? ROW LEVEL LOCKING)?
    | NOCOMPRESS
    ;

inmemoryTableClause
    : ((INMEMORY inmemoryAttributes?) | NO INMEMORY)? (inmemoryColumnClause)?
    ;

inmemoryAttributes
    : inmemoryMemcompress? inmemoryPriority? inmemoryDistribute? inmemoryDuplicate?
    ;

inmemoryColumnClause
    : (INMEMORY inmemoryMemcompress? | NO INMEMORY) columnNames
    ;

inmemoryMemcompress
    : MEMCOMPRESS FOR ( DML | (QUERY | CAPACITY) (LOW | HIGH)? ) | NO MEMCOMPRESS
    ;

inmemoryPriority
    : PRIORITY (NONE | LOW | MEDIUM | HIGH | CRITICAL)
    ;

inmemoryDistribute
    : DISTRIBUTE (AUTO | BY (ROWID RANGE | PARTITION | SUBPARTITION))? (FOR SERVICE (DEFAULT | ALL | serviceName | NONE))?
    ;

inmemoryDuplicate
    : DUPLICATE | DUPLICATE ALL | NO DUPLICATE
    ;

ilmClause
    : ILM (ADD POLICY ilmPolicyClause
    | (DELETE | ENABLE | DISABLE) POLICY ilmPolicyName
    | (DELETE_ALL | ENABLE_ALL | DISABLE_ALL))
    ;

ilmPolicyClause
    : ilmCompressionPolicy | ilmTieringPolicy | ilmInmemoryPolicy
    ;

ilmCompressionPolicy
    : tableCompression (SEGMENT | GROUP) ( AFTER ilmTimePeriod OF ( NO ACCESS | NO MODIFICATION | CREATION ) | ON functionName)
    | (ROW STORE COMPRESS ADVANCED | COLUMN STORE COMPRESS FOR QUERY) ROW AFTER ilmTimePeriod OF NO MODIFICATION
    ;

ilmTimePeriod
    : NUMBER_ ((DAY | DAYS) | (MONTH | MONTHS) | (YEAR | YEARS))
    ;

ilmTieringPolicy
    : TIER TO tablespaceName (SEGMENT | GROUP)? (ON functionName)?
    | TIER TO tablespaceName READ ONLY (SEGMENT | GROUP)? (AFTER ilmTimePeriod OF (NO ACCESS | NO MODIFICATION | CREATION) | ON functionName)
    ;

ilmInmemoryPolicy
    : (SET INMEMORY inmemoryAttributes | MODIFY INMEMORY inmemoryMemcompress | NO INMEMORY) SEGMENT (AFTER ilmTimePeriod OF (NO ACCESS | NO MODIFICATION | CREATION) | ON functionName)
    ;

organizationClause
    : ORGANIZATION 
    ( HEAP segmentAttributesClause? heapOrgTableClause 
    | INDEX segmentAttributesClause? indexOrgTableClause 
    | EXTERNAL externalTableClause)
    ;

heapOrgTableClause
    : tableCompression? inmemoryTableClause? ilmClause?
    ;

indexOrgTableClause
    : (mappingTableClause | PCTTHRESHOLD NUMBER_ | prefixCompression)* indexOrgOverflowClause?
    ;

externalTableClause
    : LP_ (TYPE accessDriverType)? (externalTableDataProps)? RP_ (REJECT LIMIT (NUMBER_ | UNLIMITED))? inmemoryTableClause?
    ;

externalTableDataProps
    : (DEFAULT DIRECTORY directoryName)? (ACCESS PARAMETERS ((opaqueFormatSpec) | USING CLOB subquery))? (LOCATION LP_ (directoryName COLON_)? locationSpecifier (COMMA_ (directoryName COLON_)? locationSpecifier)+ RP_)?
    ;

mappingTableClause
    : MAPPING TABLE | NOMAPPING
    ;

prefixCompression
    : COMPRESS NUMBER_? | NOCOMPRESS
    ;

indexOrgOverflowClause
    :  (INCLUDING columnName)? OVERFLOW segmentAttributesClause?
    ;

externalPartitionClause
    : EXTERNAL PARTITION ATTRIBUTES externalTableClause (REJECT LIMIT)?
    ;

clusterRelatedClause
    : CLUSTER clusterName columnNames
    ;

tableProperties
    :columnProperties?
     readOnlyClause?
     indexingClause?
     tablePartitioningClauses?
     attributeClusteringClause?
     (CACHE | NOCACHE)?
     ( RESULT_CACHE ( MODE (DEFAULT | FORCE) ) )?
     parallelClause?
     (ROWDEPENDENCIES | NOROWDEPENDENCIES)?
     enableDisableClause*
     rowMovementClause?
     flashbackArchiveClause?
     (ROW ARCHIVAL)?
     (AS subquery | FOR EXCHANGE WITH TABLE tableName)?
    ;

readOnlyClause
    : READ ONLY | READ WRITE 
    ;

indexingClause
    : INDEXING (ON | OFF)
    ;

tablePartitioningClauses
    : rangePartitions
    | listPartitions
    | hashPartitions
    | compositeRangePartitions
    | compositeListPartitions
    | compositeHashPartitions
    | referencePartitioning
    | systemPartitioning
    | consistentHashPartitions
    | consistentHashWithSubpartitions
    | partitionsetClauses
    ;

rangePartitions
    : PARTITION BY RANGE columnNames
      (INTERVAL LP_ expr RP_ (STORE IN LP_ tablespaceName (COMMA_ tablespaceName)* RP_)?)?
      LP_ PARTITION partitionName? rangeValuesClause tablePartitionDescription (COMMA_ PARTITION partitionName? rangeValuesClause tablePartitionDescription externalPartSubpartDataProps?)* RP_
    ;

rangeValuesClause
    : VALUES LESS THAN LP_? (numberLiterals | MAXVALUE) (COMMA_ (numberLiterals | MAXVALUE))* RP_?
    ;

tablePartitionDescription
    : (INTERNAL | EXTERNAL)?
      deferredSegmentCreation?
      readOnlyClause?
      indexingClause?
      segmentAttributesClause?
      (tableCompression | prefixCompression)?
      inmemoryClause?
      ilmClause?
      (OVERFLOW segmentAttributesClause?)?
      (lobStorageClause | varrayColProperties | nestedTableColProperties)*
    ;

inmemoryClause
    : INMEMORY inmemoryAttributes? | NO INMEMORY
    ;

varrayColProperties
    : VARRAY varrayItem (substitutableColumnClause? varrayStorageClause | substitutableColumnClause)
    ;

nestedTableColProperties
    : NESTED TABLE 
    (nestedItem | COLUMN_VALUE) substitutableColumnClause? (LOCAL | GLOBAL)? STORE AS storageTable 
    LP_ (LP_ objectProperties RP_ | physicalProperties | columnProperties) RP_ 
    (RETURN AS? (LOCATOR | VALUE))?
    ;

lobStorageClause
    : LOB
    ( LP_ lobItem (COMMA_ lobItem)* RP_ STORE AS ((SECUREFILE | BASICFILE) | LP_ lobStorageParameters RP_)+
    | LP_ lobItem RP_ STORE AS ((SECUREFILE | BASICFILE) | lobSegname | LP_ lobStorageParameters RP_)+
    )
    ;

varrayStorageClause
    : STORE AS (SECUREFILE | BASICFILE)? LOB (lobSegname? LP_ lobStorageParameters RP_ | lobSegname)
    ;

lobStorageParameters
    : ((TABLESPACE tablespaceName | TABLESPACE SET tablespaceSetName) | lobParameters storageClause?)+ | storageClause
    ;

lobParameters
    : ( (ENABLE | DISABLE) STORAGE IN ROW
        | CHUNK NUMBER_
        | PCTVERSION NUMBER_
        | FREEPOOLS NUMBER_
        | lobRetentionClause
        | lobDeduplicateClause
        | lobCompressionClause
        | (ENCRYPT encryptionSpecification | DECRYPT)
        | (CACHE | NOCACHE | CACHE READS) loggingClause? 
      )+
    ;

lobRetentionClause
    : RETENTION (MAX | MIN NUMBER_ | AUTO | NONE)?
    ;

lobDeduplicateClause
    : DEDUPLICATE | KEEP_DUPLICATES
    ;

lobCompressionClause
    : (COMPRESS (HIGH | MEDIUM | LOW)? | NOCOMPRESS)
    ;

externalPartSubpartDataProps
    : (DEFAULT DIRECTORY directoryName) (LOCATION LP_ (directoryName COLON_)? locationSpecifier (COMMA_ (directoryName COLON_)? locationSpecifier)* RP_)?
    ;

listPartitions
    : PARTITION BY LIST columnNames
      (AUTOMATIC (STORE IN LP_? tablespaceName (COMMA_ tablespaceName)* RP_?))?
      LP_ PARTITION partitionName? listValuesClause tablePartitionDescription (COMMA_ PARTITION partitionName? listValuesClause tablePartitionDescription externalPartSubpartDataProps?)* RP_
    ;

listValuesClause
    : VALUES ( listValues | DEFAULT )
    ;

listValues
    : (literals | NULL) (COMMA_ (literals | NULL))*
    | (LP_? ( (literals | NULL) (COMMA_ (literals | NULL))* ) RP_?) (COMMA_ LP_? ( (literals | NULL) (COMMA_ (literals | NULL))* ) RP_?)*
    ;

hashPartitions
    : PARTITION BY HASH columnNames (individualHashPartitions | hashPartitionsByQuantity)
    ;

hashPartitionsByQuantity
    : PARTITIONS INTEGER_ (STORE IN (tablespaceName (COMMA_ tablespaceName)*))? (tableCompression | indexCompression)? (OVERFLOW STORE IN (tablespaceName (COMMA_ tablespaceName)*))?
    ;

indexCompression
    : prefixCompression | advancedIndexCompression
    ;

advancedIndexCompression
    : COMPRESS ADVANCED (LOW | HIGH)? | NOCOMPRESS
    ;

individualHashPartitions
    : LP_? (PARTITION partitionName? readOnlyClause? indexingClause? partitioningStorageClause?) (COMMA_ PARTITION partitionName? readOnlyClause? indexingClause? partitioningStorageClause?)* RP_?
    ;

partitioningStorageClause
    : ((TABLESPACE tablespaceName | TABLESPACE SET tablespaceSetName)
    | OVERFLOW (TABLESPACE tablespaceName | TABLESPACE SET tablespaceSetName)?
    | tableCompression
    | indexCompression
    | inmemoryClause
    | ilmClause
    | lobPartitioningStorage
    | VARRAY varrayItem STORE AS (SECUREFILE | BASICFILE)? LOB lobSegname
    )*
    ;

lobPartitioningStorage
    :LOB LP_ lobItem RP_ STORE AS (BASICFILE | SECUREFILE)?
    (lobSegname (LP_ TABLESPACE tablespaceName | TABLESPACE SET tablespaceSetName RP_)?
    | LP_ TABLESPACE tablespaceName | TABLESPACE SET tablespaceSetName RP_
    )?
    ;

compositeRangePartitions
    : PARTITION BY RANGE columnNames 
      (INTERVAL LP_ expr RP_ (STORE IN LP_? tablespaceName (COMMA_ tablespaceName)* RP_?)?)?
      (subpartitionByRange | subpartitionByList | subpartitionByHash) 
      LP_? rangePartitionDesc (COMMA_ rangePartitionDesc)* RP_?
    ;

subpartitionByRange
    : SUBPARTITION BY RANGE columnNames subpartitionTemplate?
    ;

subpartitionByList
    : SUBPARTITION BY LIST columnNames subpartitionTemplate?
    ;

subpartitionByHash
    : SUBPARTITION BY HASH columnNames (SUBPARTITIONS NUMBER_ (STORE IN LP_ tablespaceName (COMMA_ tablespaceName)? RP_)? | subpartitionTemplate)?
    ;

subpartitionTemplate
    : SUBPARTITION TEMPLATE
    (LP_? rangeSubpartitionDesc (COMMA_ rangeSubpartitionDesc)* | listSubpartitionDesc (COMMA_ listSubpartitionDesc)* | individualHashSubparts (COMMA_ individualHashSubparts)* RP_?)
    | hashSubpartitionQuantity
    ;

rangeSubpartitionDesc
    : SUBPARTITION subpartitionName? rangeValuesClause readOnlyClause? indexingClause? partitioningStorageClause? externalPartSubpartDataProps?
    ;

listSubpartitionDesc
    : SUBPARTITION subpartitionName? listValuesClause readOnlyClause? indexingClause? partitioningStorageClause? externalPartSubpartDataProps?
    ;

individualHashSubparts
    : SUBPARTITION subpartitionName? readOnlyClause? indexingClause? partitioningStorageClause?
    ;

rangePartitionDesc
    : PARTITION partitionName? rangeValuesClause tablePartitionDescription
    ((LP_? rangeSubpartitionDesc (COMMA_ rangeSubpartitionDesc)* | listSubpartitionDesc (COMMA_ listSubpartitionDesc)* | individualHashSubparts (COMMA_ individualHashSubparts)* RP_?)
    | hashSubpartitionQuantity)?
    ;

compositeListPartitions
    : PARTITION BY LIST columnNames 
      (AUTOMATIC (STORE IN LP_? tablespaceName (COMMA_ tablespaceName)* RP_?)?)?
      (subpartitionByRange | subpartitionByList | subpartitionByHash) 
      LP_? listPartitionDesc (COMMA_ listPartitionDesc)* RP_?
    ;

listPartitionDesc
    : PARTITIONSET partitionSetName listValuesClause (TABLESPACE SET tablespaceSetName)? lobStorageClause? (SUBPARTITIONS STORE IN LP_? tablespaceSetName (COMMA_ tablespaceSetName)* RP_?)?
    ;

compositeHashPartitions
    : PARTITION BY HASH columnNames (subpartitionByRange | subpartitionByList | subpartitionByHash) (individualHashPartitions | hashPartitionsByQuantity)
    ;

referencePartitioning
    :PARTITION BY REFERENCE LP_ constraint RP_ (LP_? referencePartitionDesc (COMMA_ referencePartitionDesc)* RP_?)?
    ;

referencePartitionDesc
    : PARTITION partitionName? tablePartitionDescription?
    ;

constraint
    : inlineConstraint | outOfLineConstraint | inlineRefConstraint | outOfLineRefConstraint
    ;

systemPartitioning
    : PARTITION BY SYSTEM (PARTITIONS NUMBER_ | referencePartitionDesc (COMMA_ referencePartitionDesc)*)?
    ;

consistentHashPartitions
    : PARTITION BY CONSISTENT HASH columnNames (PARTITIONS AUTO)? TABLESPACE SET tablespaceSetName
    ;

consistentHashWithSubpartitions
    : PARTITION BY CONSISTENT HASH columnNames (subpartitionByRange | subpartitionByList | subpartitionByHash)  (PARTITIONS AUTO)?
    ;

partitionsetClauses
    : rangePartitionsetClause | listPartitionsetClause
    ;

rangePartitionsetClause
    : PARTITIONSET BY RANGE columnNames PARTITION BY CONSISTENT HASH columnNames
      (SUBPARTITION BY ((RANGE | HASH) columnNames | LIST LP_ columnName LP_) subpartitionTemplate?)?
      PARTITIONS AUTO LP_ rangePartitionsetDesc (COMMA_ rangePartitionsetDesc)* RP_
    ;

rangePartitionsetDesc
    : PARTITIONSET partitionSetName rangeValuesClause (TABLESPACE SET tablespaceSetName)? (lobStorageClause)? (SUBPARTITIONS STORE IN tablespaceSetName?)?
    ;

listPartitionsetClause
    : PARTITIONSET BY RANGE LP_ columnName RP_ PARTITION BY CONSISTENT HASH columnNames
      (SUBPARTITION BY ((RANGE | HASH) columnNames | LIST LP_ columnName LP_) subpartitionTemplate?)?
      PARTITIONS AUTO LP_ rangePartitionsetDesc (COMMA_ rangePartitionsetDesc)* RP_
    ;

attributeClusteringClause
    : CLUSTERING clusteringJoin? clusterClause clusteringWhen? zonemapClause?
    ;

clusteringJoin
    : tableName (JOIN tableName ON LP_ expr RP_)+
    ;

clusterClause
    : BY (LINEAR | INTERLEAVED)? ORDER clusteringColumns
    ;

clusteringColumns
    : LP_? clusteringColumnGroup (COMMA_ clusteringColumnGroup)* RP_?
    ;

clusteringColumnGroup
    : columnNames
    ;

clusteringWhen
    : ((YES | NO) ON LOAD)? ((YES | NO) ON DATA MOVEMENT)?
    ;

zonemapClause
    : (WITH MATERIALIZED ZONEMAP (LP_ zonemapName RP_)?) | (WITHOUT MATERIALIZED ZONEMAP)
    ;

rowMovementClause
    : (ENABLE | DISABLE) ROW MOVEMENT
    ;

flashbackArchiveClause
    : FLASHBACK ARCHIVE flashbackArchiveName? | NO FLASHBACK ARCHIVE
    ;

alterSynonym
    : ALTER PUBLIC? SYNONYM (schemaName DOT_)? synonymName (COMPILE | EDITIONABLE | NONEDITIONABLE)
    ;

alterTablePartitioning
    : modifyTablePartition
    | moveTablePartition
    | addTablePartition
    | coalesceTablePartition
    | dropTablePartition
    ;

modifyTablePartition
    : modifyRangePartition
    | modifyHashPartition
    | modifyListPartition
    ;

modifyRangePartition
    : MODIFY partitionExtendedName (partitionAttributes
    | (addRangeSubpartition | addHashSubpartition | addListSubpartition)
    | coalesceTableSubpartition | alterMappingTableClauses | REBUILD? UNUSABLE LOCAL INDEXES
    | readOnlyClause | indexingClause)
    ;

modifyHashPartition
    : MODIFY partitionExtendedName (partitionAttributes | coalesceTableSubpartition
    | alterMappingTableClauses | REBUILD? UNUSABLE LOCAL INDEXES | readOnlyClause | indexingClause)
    ;

modifyListPartition
    : MODIFY partitionExtendedName (partitionAttributes
    | (ADD | DROP) VALUES LP_ listValues RP_
    | (addRangeSubpartition | addHashSubpartition | addListSubpartition)
    | coalesceTableSubpartition | REBUILD? UNUSABLE LOCAL INDEXES | readOnlyClause | indexingClause)
    ;

partitionExtendedName
    : PARTITION partitionName
    | PARTITION FOR LR_ partitionKeyValue (COMMA_ partitionKeyValue)* RP_
    ;

addRangeSubpartition
    : ADD rangeSubpartitionDesc (COMMA_ rangeSubpartitionDesc)* dependentTablesClause? updateIndexClauses?
    ;

dependentTablesClause
    : DEPENDENT TABLES LP_ tableName LP_ partitionSpec (COMMA_ partitionSpec)* RP_
    (COMMA_ tableName LP_ partitionSpec (COMMA_ partitionSpec)* RP_)* RP_
    ;

addHashSubpartition
    : ADD individualHashSubparts dependentTablesClause? updateIndexClauses? parallelClause?
    ;

addListSubpartition
    : ADD listSubpartitionDesc (COMMA_ listSubpartitionDesc)* dependentTablesClause? updateIndexClauses?
    ;

coalesceTableSubpartition
    : COALESCE SUBPARTITION subpartitionName updateIndexClauses? parallelClause? allowDisallowClustering?
    ;

allowDisallowClustering
    : (ALLOW | DISALLOW) CLUSTERING
    ;

alterMappingTableClauses
    : MAPPING TABLE (allocateExtentClause | deallocateUnusedClause)
    ;

deallocateUnusedClause
    : DEALLOCATE UNUSED (KEEP sizeClause)?
    ;

allocateExtentClause
    : ALLOCATE EXTENT (LP_ (SIZE sizeClause | DATAFILE SQ_ fileName SQ_ | INSTANCE NUMBER_)* RP_)?
    ;

partitionSpec
    : PARTITION partitionName? tablePartitionDescription?
    ;

partitionAttributes
    : (physicalAttributesClause | loggingClause | allocateExtentClause | deallocateUnusedClause | shrinkClause)*
      (OVERFLOW (physicalAttributesClause | loggingClause | allocateExtentClause | deallocateUnusedClause)*)?
      tableCompression? inmemoryClause?
    ;

shrinkClause
    : SHRINK SPACE COMPACT? CASCADE?
    ;

moveTablePartition
    : MOVE partitionExtendedName (MAPPING TABLE)? tablePartitionDescription? filterCondition? updateAllIndexesClause? parallelClause? allowDisallowClustering? ONLINE?
    ;

filterCondition
    : INCLUDING ROWS whereClause
    ;

whereClause
    : WHERE expr
    ;

coalesceTablePartition
    : COALESCE PARTITION updateIndexClauses? parallelClause? allowDisallowClustering?
    ;

addTablePartition
    : ADD ((PARTITION partitionName? addRangePartitionClause (COMMA_ PARTITION partitionName? addRangePartitionClause)*)
        |  (PARTITION partitionName? addListPartitionClause (COMMA_ PARTITION partitionName? addListPartitionClause)*)
        |  (PARTITION partitionName? addSystemPartitionClause (COMMA_ PARTITION partitionName? addSystemPartitionClause)*)
        (BEFORE (partitionName | NUMBER_))?
        |  (PARTITION partitionName? addHashPartitionClause)
        ) dependentTablesClause?
    ;

addRangePartitionClause
    : rangeValuesClause tablePartitionDescription? externalPartSubpartDataProps?
    ((LP_? (rangeSubpartitionDesc (COMMA_ rangeSubpartitionDesc)* | listSubpartitionDesc (COMMA_ listSubpartitionDesc)* | individualHashSubparts (COMMA_ individualHashSubparts)*) RP_?)
    | hashSubpartsByQuantity)? updateIndexClauses?
    ;

addListPartitionClause
    : listValuesClause tablePartitionDescription? externalPartSubpartDataProps?
    ((LP_? (rangeSubpartitionDesc (COMMA_ rangeSubpartitionDesc)* | listSubpartitionDesc (COMMA_ listSubpartitionDesc)* | individualHashSubparts (COMMA_ individualHashSubparts)*) RP_?)
    | hashSubpartsByQuantity)? updateIndexClauses?
    ;

hashSubpartsByQuantity
    : SUBPARTITIONS NUMBER_ (STORE IN LP_ tablespaceName (COMMA_ tablespaceName)* RP_)?
    ;

addSystemPartitionClause
    : tablePartitionDescription? updateIndexClauses?
    ;

addHashPartitionClause
    : partitioningStorageClause updateIndexClauses? parallelClause? readOnlyClause? indexingClause?
    ;

dropTablePartition
    : DROP partitionExtendedNames (updateIndexClauses parallelClause?)?
    ;

partitionExtendedNames
    : (PARTITION | PARTITIONS) (partitionName | partitionForClauses) (COMMA_ (partitionName | partitionForClauses))*
    ;

partitionForClauses
    : FOR LP_ partitionKeyValue (COMMA_ partitionKeyValue)* RP_
    ;

updateIndexClauses
    : updateGlobalIndexClause | updateAllIndexesClause
    ;

updateGlobalIndexClause
    : (UPDATE | INVALIDATE) GLOBAL INDEXES
    ;

updateAllIndexesClause
    : UPDATE INDEXES
    (LP_ indexName LP_ (updateIndexPartition | updateIndexSubpartition) RP_
    (COMMA_ indexName LP_ (updateIndexPartition | updateIndexSubpartition) RP_)* RP_)?
    ;

updateIndexPartition
    : indexPartitionDesc indexSubpartitionClause?
    (COMMA_ indexPartitionDesc indexSubpartitionClause?)*
    ;

indexPartitionDesc
    : PARTITION
    (partitionName
    ((segmentAttributesClause | indexCompression)+ | PARAMETERS LP_ SQ_ odciParameters SQ_ RP_ )?
    usableSpecification?
    )?
    ;

indexSubpartitionClause
    : STORE IN LP_ tablespaceName (COMMA_ tablespaceName)* RP_
    | LP_ SUBPARTITION subpartitionName? (TABLESPACE tablespaceName)? indexCompression? usableSpecification?
    (COMMA_ SUBPARTITION subpartitionName? (TABLESPACE tablespaceName)? indexCompression? usableSpecification?)* RP_
    ;

updateIndexSubpartition
    : SUBPARTITION subpartitionName? (TABLESPACE tablespaceName)?
    (COMMA_ SUBPARTITION subpartitionName? (TABLESPACE tablespaceName)?)*
    ;

supplementalLoggingProps
    : SUPPLEMENTAL LOG supplementalLogGrpClause|supplementalIdKeyClause
    ;

supplementalLogGrpClause
    : GROUP logGroupName LP_ columnName (NO LOG)? (COMMA_ columnName (NO LOG)?)* RP_ ALWAYS?
    ;

supplementalIdKeyClause
    : DATA LP_ (ALL | PRIMARY KEY | UNIQUE | FOREIGN KEY) (COMMA_ (ALL | PRIMARY KEY | UNIQUE | FOREIGN KEY))* RP_ COLUMNS
    ;

alterSession
    : ALTER SESSION alterSessionOption
    ;

alterSessionOption
    : adviseClause
    | closeDatabaseLinkClause
    | commitInProcedureClause
    | securiyClause
    | parallelExecutionClause
    | resumableClause
    | shardDdlClause
    | syncWithPrimaryClause
    | alterSessionSetClause
    ;

adviseClause
    : ADVISE (COMMIT | ROLLBACK | NOTHING)
    ;

closeDatabaseLinkClause
    : CLOSE DATABASE LINK dbLink
    ;

commitInProcedureClause
    : (ENABLE | DISABLE) COMMIT IN PROCEDURE
    ;

securiyClause
    : (ENABLE | DISABLE) GUARD
    ;

parallelExecutionClause
    : (ENABLE | DISABLE | FORCE) PARALLEL (DML | DDL | QUERY) (PARALLEL numberLiterals)?
    ;

resumableClause
    : enableResumableClause | disableResumableClause
    ;

enableResumableClause
    : ENABLE RESUMABLE (TIMEOUT numberLiterals)? (NAME stringLiterals)?
    ;

disableResumableClause
    : DISABLE RESUMABLE
    ;

shardDdlClause
    : (ENABLE | DISABLE) SHARD DDL
    ;

syncWithPrimaryClause
    : SYNC WITH PRIMARY
    ;

alterSessionSetClause
    : SET alterSessionSetClauseOption
    ;

alterSessionSetClauseOption
    : parameterClause
    | editionClause
    | containerClause
    | rowArchivalVisibilityClause
    | defaultCollationClause
    ;

parameterClause
    : (parameterName EQ_ parameterValue)+
    ;

editionClause
    : EDITION EQ_ editionName
    ;

containerClause
    : CONTAINER EQ_ containerName (SERVICE EQ_ serviceName)?
    ;

rowArchivalVisibilityClause
    : ROW ARCHIVAL VISIBILITY EQ_ (ACTIVE | ALL)
    ;

defaultCollationClause
    : DEFAULT_COLLATION EQ_ (collationName | NONE)
    ;

alterDatabase
    : ALTER databaseClauses
    ( startupClauses
    | recoveryClauses
    | databaseFileClauses
    | logfileClauses
    | controlfileClauses
    | standbyDatabaseClauses
    | defaultSettingsClauses
    | instanceClauses
    | securityClause
    | prepareClause
    | dropMirrorCopy
    | lostWriteProtection
    | cdbFleetClauses
    | propertyClause )
    ;

databaseClauses
    : DATABASE databaseName | PLUGGABLE DATABASE pdbName
    ;

startupClauses
    : MOUNT ((STANDBY | CLONE) DATABASE)?
    | OPEN ((READ WRITE)? (RESETLOGS | NORESETLOGS)? (UPGRADE | DOWNGRADE)? | READ ONLY)
    ;

recoveryClauses
    : generalRecovery | managedStandbyRecovery | BEGIN BACKUP | END BACKUP
    ;

generalRecovery
    : RECOVER (AUTOMATIC)? (FROM locationName)? (
      (fullDatabaseRecovery | partialDatabaseRecovery | LOGFILE fileName)
      ((TEST | ALLOW NUMBER_ CORRUPTION | parallelClause)+)?
    | CONTINUE DEFAULT?
    | CANCEL
    )
    ;

fullDatabaseRecovery
    : STANDBY? DATABASE
    ((UNTIL (CANCEL | TIME dateValue | CHANGE NUMBER_ | CONSISTENT)
    | USING BACKUP CONTROLFILE
    | SNAPSHOT TIME dateValue
    )+)?
    ;

partialDatabaseRecovery
    : TABLESPACE tablespaceName (COMMA_ tablespaceName)*
    | DATAFILE (fileName | fileNumber) (COMMA_ (fileName | fileNumber))*
    ;

managedStandbyRecovery
    : RECOVER (MANAGED STANDBY DATABASE
    ((USING ARCHIVED LOGFILE | DISCONNECT (FROM SESSION)?
    | NODELAY
    | UNTIL CHANGE NUMBER_
    | UNTIL CONSISTENT | USING INSTANCES (ALL | NUMBER_) | parallelClause)+
    | FINISH | CANCEL)?
    | TO LOGICAL STANDBY (databaseName | KEEP IDENTITY))
    ;

databaseFileClauses
    : RENAME FILE fileName (COMMA_ fileName)* TO fileName
    | createDatafileClause
    | alterDatafileClause
    | alterTempfileClause
    | moveDatafileClause
    ;

createDatafileClause
    : CREATE DATAFILE (fileName | fileNumber) (COMMA_ (fileName | fileNumber))*
    ( AS (fileSpecification (COMMA_ fileSpecification)* | NEW))?
    ;

fileSpecification
    : datafileTempfileSpec | redoLogFileSpec
    ;

datafileTempfileSpec
    : (fileName | asmFileName )? (SIZE sizeClause)? REUSE? autoextendClause?
    ;

autoextendClause
    : AUTOEXTEND (OFF | ON (NEXT sizeClause)? maxsizeClause?)
    ;

redoLogFileSpec
    : ((fileName | asmFileName)
    | LP_ (fileName | asmFileName) (COMMA_ (fileName | asmFileName))* RP_)?
    (SIZE sizeClause)? (BLOCKSIZE sizeClause)? REUSE?
    ;

alterDatafileClause
    : DATAFILE (fileName | NUMBER_) (COMMA_ (fileName | NUMBER_))*
    (ONLINE | OFFLINE (FOR DROP)? | RESIZE sizeClause | autoextendClause | END BACKUP | ENCRYPT | DECRYPT)
    ;

alterTempfileClause
    : TEMPFILE (fileName | NUMBER_) (COMMA_ (fileName | NUMBER_))*
    (RESIZE sizeClause | autoextendClause | DROP (INCLUDING DATAFILES)? | ONLINE | OFFLINE)
    ;

logfileClauses
    : ((ARCHIVELOG MANUAL? | NOARCHIVELOG )
    | NO? FORCE LOGGING
    | SET STANDBY NOLOGGING FOR (DATA AVAILABILITY | LOAD PERFORMANCE)
    | RENAME FILE fileName (COMMA_ fileName)* TO fileName
    | CLEAR UNARCHIVED? LOGFILE logfileDescriptor (COMMA_ logfileDescriptor)* (UNRECOVERABLE DATAFILE)?
    | addLogfileClauses
    | dropLogfileClauses
    | switchLogfileClause
    | supplementalDbLogging)
    ;

logfileDescriptor
    : GROUP NUMBER_ | LP_ fileName (COMMA_ fileName)* RP_ | fileName
    ;

addLogfileClauses
    : ADD STANDBY? LOGFILE
    (((INSTANCE instanceName)? | (THREAD SQ_ NUMBER_ SQ_)?)
    (GROUP NUMBER_)? redoLogFileSpec (COMMA_ (GROUP NUMBER_)? redoLogFileSpec)*
    | MEMBER fileName REUSE? (COMMA_ fileName REUSE?)* TO logfileDescriptor (COMMA_ logfileDescriptor)*)
    ;

controlfileClauses
    : CREATE ((LOGICAL | PHYSICAL)? STANDBY | FAR SYNC INSTANCE) CONTROLFILE AS fileName REUSE?
    | BACKUP CONTROLFILE TO (fileName REUSE? | traceFileClause)
    ;

traceFileClause
    : TRACE (AS fileName REUSE?)? (RESETLOGS | NORESETLOGS)?
    ;

dropLogfileClauses
    : DROP STANDBY? LOGFILE
    (logfileDescriptor (COMMA_ logfileDescriptor)*
    | MEMBER fileName (COMMA_ fileName)*)
    ;

switchLogfileClause
    : SWITCH ALL LOGFILES TO BLOCKSIZE NUMBER_
    ;

supplementalDbLogging
    : (ADD | DROP) SUPPLEMENTAL LOG
    ( DATA
    | supplementalIdKeyClause
    | supplementalPlsqlClause
    | supplementalSubsetReplicationClause)
    ;

supplementalPlsqlClause
    : DATA FOR PROCEDURAL REPLICATION
    ;

supplementalSubsetReplicationClause
    : DATA SUBSET DATABASE REPLICATION
    ;

standbyDatabaseClauses
    : ((activateStandbyDbClause
    | maximizeStandbyDbClause
    | registerLogfileClause
    | commitSwitchoverClause
    | startStandbyClause
    | stopStandbyClause
    | convertDatabaseClause) parallelClause?)
    | (switchoverClause | failoverClause)
    ;

activateStandbyDbClause
    : ACTIVATE (PHYSICAL | LOGICAL)? STANDBY DATABASE (FINISH APPLY)?
    ;

maximizeStandbyDbClause
    : SET STANDBY DATABASE TO MAXIMIZE (PROTECTION | AVAILABILITY | PERFORMANCE)
    ;

registerLogfileClause
    : REGISTER (OR REPLACE)? (PHYSICAL | LOGICAL)? LOGFILE fileSpecification (COMMA_ fileSpecification)* (FOR logminerSessionName)?
    ;

commitSwitchoverClause
    : (PREPARE | COMMIT) TO SWITCHOVER
    ( TO (((PHYSICAL | LOGICAL)? PRIMARY | PHYSICAL? STANDBY) ((WITH | WITHOUT) SESSION SHUTDOWN (WAIT | NOWAIT))?
    | LOGICAL STANDBY)
    | CANCEL
    )?
    ;

startStandbyClause
    : START LOGICAL STANDBY APPLY IMMEDIATE? NODELAY? (NEW PRIMARY dbLink | INITIAL scnValue? | (SKIP_SYMBOL FAILED TRANSACTION | FINISH))?
    ;

scnValue
    : literals
    ;

stopStandbyClause
    : (STOP | ABORT) LOGICAL STANDBY APPLY
    ;

switchoverClause
    : SWITCHOVER TO databaseName (VERIFY | FORCE)?
    ;

convertDatabaseClause
    : CONVERT TO (PHYSICAL | SNAPSHOT) STANDBY
    ;

failoverClause
    : FAILOVER TO databaseName FORCE?
    ;

defaultSettingsClauses
    : DEFAULT EDITION EQ_ editionName
    | SET DEFAULT (BIGFILE | SMALLFILE) TABLESPACE
    | DEFAULT TABLESPACE tablespaceName
    | DEFAULT LOCAL? TEMPORARY TABLESPACE (tablespaceName | tablespaceGroupName)
    | RENAME GLOBAL_NAME TO databaseName DOT_ domain (DOT_ domain)*
    | ENABLE BLOCK CHANGE TRACKING (USING FILE fileName REUSE?)?
    | DISABLE BLOCK CHANGE TRACKING
    | NO? FORCE FULL DATABASE CACHING
    | CONTAINERS DEFAULT TARGET EQ_ (LP_ containerName RP_ | NONE)
    | flashbackModeClause
    | undoModeClause
    | setTimeZoneClause
    ;

setTimeZoneClause
    : SET TIME_ZONE EQ_ SQ_ ( (PLUS_ | MINUS_) dateValue  | timeZoneRegion ) SQ_
    ;

timeZoneRegion
    : STRING_
    ;

flashbackModeClause
    : FLASHBACK (ON | OFF)
    ;

undoModeClause
    : LOCAL UNDO (ON | OFF)
    ;

moveDatafileClause
    : MOVE DATAFILE LP_ (fileName | asmFileName | fileNumber) RP_
    (TO LP_ (fileName | asmFileName) RP_ )? REUSE? KEEP?
    ;

instanceClauses
    : (ENABLE | DISABLE) INSTANCE instanceName
    ;

securityClause
    : GUARD (ALL | STANDBY | NONE)
    ;

prepareClause
    : PREPARE MIRROR COPY copyName (WITH (UNPROTECTED | MIRROR | HIGH) REDUNDANCY)?
    ;

dropMirrorCopy
    : DROP MIRROR COPY mirrorName
    ;

lostWriteProtection
    : (ENABLE | DISABLE | REMOVE | SUSPEND)? LOST WRITE PROTECTION
    ;

cdbFleetClauses
    : leadCdbClause | leadCdbUriClause
    ;

leadCdbClause
    : SET LEAD_CDB EQ_  (TRUE | FALSE)
    ;

leadCdbUriClause
    : SET LEAD_CDB_URI EQ_ uriString
    ;

propertyClause
    : PROPERTY (SET | REMOVE) DEFAULT_CREDENTIAL EQ_ qualifiedCredentialName
    ;

alterSystem
    : ALTER SYSTEM alterSystemOption
    ;

alterSystemOption
    : archiveLogClause
    | checkpointClause
    | checkDatafilesClause
    | distributedRecovClauses
    | flushClause
    | endSessionClauses
    | alterSystemSwitchLogfileClause
    | suspendResumeClause
    | quiesceClauses
    | rollingMigrationClauses
    | rollingPatchClauses
    | alterSystemSecurityClauses
    | affinityClauses
    | shutdownDispatcherClause
    | registerClause
    | setClause
    | resetClause
    | relocateClientClause
    | cancelSqlClause
    | flushPasswordfileMetadataCacheClause
    ;

archiveLogClause
    : ARCHIVE LOG instanceClause? (sequenceClause | changeClause | currentClause | groupClause | logfileClause | nextClause | allClause) toLocationClause?
    ;

checkpointClause
    : CHECKPOINT (GLOBAL | LOCAL)?
    ;

checkDatafilesClause
    : CHECK DATAFILES (GLOBAL | LOCAL)?
    ;

distributedRecovClauses
    : (ENABLE | DISABLE) DISTRIBUTED RECOVERY
    ;

flushClause
    : FLUSH flushClauseOption
    ;

endSessionClauses
    : (disconnectSessionClause | killSessionClause) (IMMEDIATE | NOREPLY)?
    ;

alterSystemSwitchLogfileClause
    : SWITCH LOGFILE
    ;

suspendResumeClause
    : SUSPEND | RESUME
    ;

quiesceClauses
    : QUIESCE RESTRICTED | UNQUIESCE
    ;

rollingMigrationClauses
    : startRollingMigrationClause | stopRollingMigrationClause
    ;

rollingPatchClauses
    : startRollingPatchClause | stopRollingPatchClause
    ;

alterSystemSecurityClauses
    : restrictedSessionClause | setEncryptionWalletOpenClause | setEncryptionWalletCloseClause | setEncryptionKeyClause
    ;

affinityClauses
    : enableAffinityClause | disableAffinityClause
    ;

shutdownDispatcherClause
    : SHUTDOWN IMMEDIATE? dispatcherName
    ;

registerClause
    : REGISTER
    ;

setClause
    : SET alterSystemSetClause+
    ;

resetClause
    : RESET alterSystemResetClause+
    ;

relocateClientClause
    : RELOCATE CLIENT clientId
    ;

cancelSqlClause
    : CANCEL SQL SQ_ sessionId serialNumber (AT_ instanceId)? sqlId? SQ_
    ;

flushPasswordfileMetadataCacheClause
    : FLUSH PASSWORDFILE_METADATA_CACHE
    ;

instanceClause
    : INSTANCE instanceName
    ;

sequenceClause
    : SEQUENCE INTEGER_
    ;

changeClause
    : CHANGE INTEGER_
    ;

currentClause
    : CURRENT NOSWITCH?
    ;

groupClause
    : GROUP INTEGER_
    ;

logfileClause
    : LOGFILE logFileName (USING BACKUP CONTROLFILE)?
    ;

nextClause
    : NEXT
    ;

allClause
    : ALL
    ;

toLocationClause
    : TO logFileGroupsArchivedLocationName
    ;

flushClauseOption
    : sharedPoolClause | globalContextClause | bufferCacheClause | flashCacheClause | redoToClause
    ;

disconnectSessionClause
    : DISCONNECT SESSION SQ_ INTEGER_ COMMA_ INTEGER_ SQ_ POST_TRANSACTION?
    ;

killSessionClause
    : KILL SESSION SQ_ INTEGER_ COMMA_ INTEGER_ (COMMA_ AT_ INTEGER_)? SQ_
    ;

startRollingMigrationClause
    : START ROLLING MIGRATION TO asmVersion
    ;

stopRollingMigrationClause
    : STOP ROLLING MIGRATION
    ;

startRollingPatchClause
    : START ROLLING PATCH
    ;

stopRollingPatchClause
    : STOP ROLLING PATCH
    ;

restrictedSessionClause
    : (ENABLE | DISABLE) RESTRICTED SESSION
    ;

setEncryptionWalletOpenClause
    : SET ENCRYPTION WALLET OPEN IDENTIFIED BY (walletPassword | hsmAuthString)
    ;

setEncryptionWalletCloseClause
    : SET ENCRYPTION WALLET CLOSE (IDENTIFIED BY (walletPassword | hsmAuthString))?
    ;

setEncryptionKeyClause
    : SET ENCRYPTION KEY (identifiedByWalletPassword | identifiedByHsmAuthString)
    ;

enableAffinityClause
    : ENABLE AFFINITY tableName (SERVICE serviceName)?
    ;

disableAffinityClause
    : DISABLE AFFINITY tableName
    ;

alterSystemSetClause
    : setParameterClause | useStoredOutlinesClause | globalTopicEnabledClause
    ;

alterSystemResetClause
    : parameterName scopeClause*
    ;

sharedPoolClause
    : SHARED_POOL
    ;

globalContextClause
    : GLOBAL CONTEXT
    ;

bufferCacheClause
    : BUFFER_CACHE
    ;

flashCacheClause
    : FLASH_CACHE
    ;

redoToClause
    : REDO TO targetDbName (NO? CONFIRM APPLY)?
    ;

identifiedByWalletPassword
    : certificateId? IDENTIFIED BY walletPassword
    ;

identifiedByHsmAuthString
    : IDENTIFIED BY hsmAuthString (MIGRATE USING walletPassword)?
    ;

setParameterClause
    : parameterName EQ_ parameterValue (COMMA_ parameterValue)* alterSystemCommentClause? DEFERRED? containerCurrentAllClause? scopeClause*
    ;

useStoredOutlinesClause
    : USE_STORED_OUTLINES EQ_ (TRUE | FALSE | categoryName)
    ;

globalTopicEnabledClause
    : GLOBAL_TOPIC_ENABLED EQ_ (TRUE | FALSE)
    ;

alterSystemCommentClause
    : COMMENT EQ_ stringLiterals
    ;

containerCurrentAllClause
    : CONTAINER EQ_ (CURRENT | ALL)
    ;

scopeClause
    : SCOPE EQ_ (MEMORY | SPFILE | BOTH) | SID EQ_ (SQ_ sessionId SQ_ | SQ_ ASTERISK_ SQ_)
    ;

analyze
    : (ANALYZE ((TABLE tableName| INDEX indexName) partitionExtensionClause? | CLUSTER clusterName))
    (validationClauses | LIST CHAINED ROWS intoClause? | DELETE SYSTEM? STATISTICS)
    ;

partitionExtensionClause
    : PARTITION (LP_ partitionName RP_ | FOR LP_ partitionKeyValue (COMMA_ partitionKeyValue) RP_)
    | SUBPARTITION (LP_ subpartitionName RP_ | FOR LP_ subpartitionKeyValue (COMMA_ subpartitionKeyValue) RP_)
    ;

validationClauses
    : VALIDATE REF UPDATE (SET DANGLING TO NULL)?
    | VALIDATE STRUCTURE (CASCADE (FAST | COMPLETE (OFFLINE | ONLINE) intoClause?))?
    ;

intoClause
    : INTO tableName
    ;

associateStatistics
    : ASSOCIATE STATISTICS WITH (columnAssociation | functionAssociation) storageTableClause?
    ;

columnAssociation
    : COLUMNS tableName DOT_ columnName (COMMA_ tableName DOT_ columnName)* usingStatisticsType
    ;

functionAssociation
    : (FUNCTIONS function (COMMA_ function)*
    | PACKAGES packageName (COMMA_ packageName)*
    | TYPES typeName (COMMA_ typeName)*
    | INDEXES indexName (COMMA_ indexName)*
    | INDEXTYPES indexTypeName (COMMA_ indexTypeName)*) 
    (usingStatisticsType | defaultCostClause (COMMA_ defaultSelectivityClause)? | defaultSelectivityClause (COMMA_ defaultCostClause)?)
    ;

storageTableClause
    : WITH (SYSTEM | USER) MANAGED STORAGE TABLES
    ;

usingStatisticsType
    : USING (statisticsTypeName | NULL)
    ;

defaultCostClause
    : DEFAULT COST LP_ cpuCost COMMA_ ioCost COMMA_ networkCost RP_
    ;

defaultSelectivityClause
    : DEFAULT SELECTIVITY defaultSelectivity
    ;

disassociateStatistics
    : DISASSOCIATE STATISTICS FROM 
    (COLUMNS tableName DOT_ columnName (COMMA_ tableName DOT_ columnName)*
    | FUNCTIONS function (COMMA_ function)*
    | PACKAGES packageName (COMMA_ packageName)*
    | TYPES typeName (COMMA_ typeName)*
    | INDEXES indexName (COMMA_ indexName)*
    | INDEXTYPES indexTypeName (COMMA_ indexTypeName)*) FORCE?
    ;

audit
    : AUDIT (auditPolicyClause | contextClause)
    ;

noAudit
    : NOAUDIT (noAuditPolicyClause | contextClause)
    ;

auditPolicyClause
    : POLICY policyName (byUsersWithRoles | (BY | EXCEPT) userName (COMMA_ userName)*)? (WHENEVER NOT? SUCCESSFUL)?
    ;

noAuditPolicyClause
    : POLICY policyName (byUsersWithRoles | BY userName (COMMA_ userName)*)? (WHENEVER NOT? SUCCESSFUL)?
    ;

byUsersWithRoles
    : BY USERS WITH GRANTED ROLES roleName (COMMA_ roleName)*
    ;

contextClause
    : contextNamespaceAttributesClause (COMMA_ contextNamespaceAttributesClause)* (BY userName (COMMA_ userName)*)?
    ;

contextNamespaceAttributesClause
    : CONTEXT NAMESPACE namespace ATTRIBUTES attributeName (COMMA_ attributeName)*
    ;

comment
    : COMMENT ON (
    | AUDIT POLICY policyName
    | COLUMN (tableName | viewName | materializedViewName) DOT_ columnName
    | EDITION editionName
    | INDEXTYPE indexTypeName
    | MATERIALIZED VIEW materializedViewName
    | MINING MODEL modelName
    | OPERATOR operatorName
    | TABLE (tableName | viewName)
    ) IS STRING_
    ;
