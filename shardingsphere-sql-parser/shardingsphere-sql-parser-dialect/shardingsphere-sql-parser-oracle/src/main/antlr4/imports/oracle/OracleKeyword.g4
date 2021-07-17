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

lexer grammar OracleKeyword;

import Alphabet;

BINARY
    : B I N A R Y
    ;

ESCAPE
    : E S C A P E
    ;

MOD
    : M O D
    ;

XOR
    : X O R
    ;

ROW
    : R O W
    ;

ROWS
    : R O W S
    ;

UNKNOWN
    : U N K N O W N
    ;

ALWAYS
    : A L W A Y S
    ;

CASCADE
    : C A S C A D E
    ;

CHECK
    : C H E C K
    ;

GENERATED
    : G E N E R A T E D
    ;

PRIVILEGES
    : P R I V I L E G E S
    ;

READ
    : R E A D
    ;

WRITE
    : W R I T E
    ;

REFERENCES
    : R E F E R E N C E S
    ;

START
    : S T A R T
    ;

TRANSACTION
    : T R A N S A C T I O N
    ;

USER
    : U S E R
    ;

ROLE
    : R O L E
    ;

VISIBLE
    : V I S I B L E
    ;

INVISIBLE
    : I N V I S I B L E
    ;

EXECUTE
    : E X E C U T E
    ;

USE
    : U S E
    ;

DEBUG
    : D E B U G
    ;

UNDER
    : U N D E R
    ;

FLASHBACK
    : F L A S H B A C K
    ;

ARCHIVE
    : A R C H I V E
    ;

REFRESH
    : R E F R E S H
    ;

QUERY
    : Q U E R Y
    ;

REWRITE
    : R E W R I T E
    ;

KEEP
    : K E E P
    ;

SEQUENCE
    : S E Q U E N C E
    ;

INHERIT
    : I N H E R I T
    ;

TRANSLATE
    : T R A N S L A T E
    ;

MERGE
    : M E R G E
    ;

AT
    : A T
    ;

BITMAP
    : B I T M A P
    ;

CACHE
    : C A C H E
    ;

NOCACHE
    : N O C A C H E
    ;

CHECKPOINT
    : C H E C K P O I N T
    ;

CONNECT
    : C O N N E C T
    ;

CONSTRAINTS
    : C O N S T R A I N T S
    ;

CYCLE
    : C Y C L E
    ;

NOCYCLE
    : N O C Y C L E
    ;

DBTIMEZONE
    : D B T I M E Z O N E
    ;

ENCRYPT
    : E N C R Y P T
    ;

DECRYPT
    : D E C R Y P T
    ;

DEFERRABLE
    : D E F E R R A B L E
    ;

DEFERRED
    : D E F E R R E D
    ;

DIRECTORY
    : D I R E C T O R Y
    ;

EDITION
    : E D I T I O N
    ;

ELEMENT
    : E L E M E N T
    ;

END
    : E N D
    ;

EXCEPT
    : E X C E P T
    ;

EXCEPTIONS
    : E X C E P T I O N S
    ;

FORCE
    : F O R C E
    ;

GLOBAL
    : G L O B A L
    ;

IDENTIFIED
    : I D E N T I F I E D
    ;

IDENTITY
    : I D E N T I T Y
    ;

IMMEDIATE
    : I M M E D I A T E
    ;

INCREMENT
    : I N C R E M E N T
    ;

INITIALLY
    : I N I T I A L L Y
    ;

INVALIDATE
    : I N V A L I D A T E
    ;

JAVA
    : J A V A
    ;

LEVELS
    : L E V E L S
    ;

MAXVALUE
    : M A X V A L U E
    ;

MINVALUE
    : M I N V A L U E
    ;

NOMAXVALUE
    : N O M A X V A L U E
    ;

NOMINVALUE
    : N O M I N V A L U E
    ;

NOSORT
    : N O S O R T
    ;

MINING
    : M I N I N G
    ;

MODEL
    : M O D E L
    ;

MODIFY
    : M O D I F Y
    ;

NATIONAL
    : N A T I O N A L
    ;

NEW
    : N E W
    ;

NOORDER
    : N O O R D E R
    ;

NORELY
    : N O R E L Y
    ;

OF
    : O F
    ;

ONLY
    : O N L Y
    ;

PRIOR
    : P R I O R
    ;

PROFILE
    : P R O F I L E
    ;

REF
    : R E F
    ;

REKEY
    : R E K E Y
    ;

RELY
    : R E L Y
    ;

RENAME
    : R E N A M E
    ;

REPLACE
    : R E P L A C E
    ;

RESOURCE
    : R E S O U R C E
    ;

REVERSE
    : R E V E R S E
    ;

ROWID
    : R O W I D
    ;

SALT
    : S A L T
    ;

SCOPE
    : S C O P E
    ;

SORT
    : S O R T
    ;

SOURCE
    : S O U R C E
    ;

SUBSTITUTABLE
    : S U B S T I T U T A B L E
    ;

TABLESPACE
    : T A B L E S P A C E
    ;

TEMPORARY
    : T E M P O R A R Y
    ;

TRANSLATION
    : T R A N S L A T I O N
    ;

TREAT
    : T R E A T
    ;

NO
    : N O
    ;

UNUSED
    : U N U S E D
    ;

VALIDATE
    : V A L I D A T E
    ;

NOVALIDATE
    : N O V A L I D A T E
    ;

VALUE
    : V A L U E
    ;

VARYING
    : V A R Y I N G
    ;

VIRTUAL
    : V I R T U A L
    ;

ZONE
    : Z O N E
    ;

PUBLIC
    : P U B L I C
    ;

SESSION
    : S E S S I O N
    ;

COMMENT
    : C O M M E N T
    ;

LOCK
    : L O C K
    ;

ADVISOR
    : A D V I S O R
    ;

ADMINISTER
    : A D M I N I S T E R
    ;

TUNING
    : T U N I N G
    ;

MANAGE
    : M A N A G E
    ;

MANAGEMENT
    : M A N A G E M E N T
    ;

OBJECT
    : O B J E C T
    ;

CLUSTER
    : C L U S T E R
    ;

CONTEXT
    : C O N T E X T
    ;

EXEMPT
    : E X E M P T
    ;

REDACTION
    : R E D A C T I O N
    ;

POLICY
    : P O L I C Y
    ;

DATABASE
    : D A T A B A S E
    ;

SYSTEM
    : S Y S T E M
    ;

AUDIT
    : A U D I T
    ;

LINK
    : L I N K
    ;

ANALYZE
    : A N A L Y Z E
    ;

DICTIONARY
    : D I C T I O N A R Y
    ;

DIMENSION
    : D I M E N S I O N
    ;

INDEXTYPE
    : I N D E X T Y P E
    ;

INDEXTYPES
    : I N D E X T Y P E S
    ;

EXTERNAL
    : E X T E R N A L
    ;

JOB
    : J O B
    ;

CLASS
    : C L A S S
    ;

PROGRAM
    : P R O G R A M
    ;

SCHEDULER
    : S C H E D U L E R
    ;

LIBRARY
    : L I B R A R Y
    ;

LOGMINING
    : L O G M I N I N G
    ;

MATERIALIZED
    : M A T E R I A L I Z E D
    ;

CUBE
    : C U B E
    ;

MEASURE
    : M E A S U R E
    ;

FOLDER
    : F O L D E R
    ;

BUILD
    : B U I L D
    ;

PROCESS
    : P R O C E S S
    ;

OPERATOR
    : O P E R A T O R
    ;

OUTLINE
    : O U T L I N E
    ;

PLUGGABLE
    : P L U G G A B L E
    ;

CONTAINER
    : C O N T A I N E R
    ;

SEGMENT
    : S E G M E N T
    ;

RESTRICTED
    : R E S T R I C T E D
    ;

COST
    : C O S T
    ;

SYNONYM
    : S Y N O N Y M
    ;

BACKUP
    : B A C K U P
    ;

UNLIMITED
    : U N L I M I T E D
    ;

BECOME
    : B E C O M E
    ;

CHANGE
    : C H A N G E
    ;

NOTIFICATION
    : N O T I F I C A T I O N
    ;

ACCESS
    : A C C E S S
    ;

PRIVILEGE
    : P R I V I L E G E
    ;

PURGE
    : P U R G E
    ;

RESUMABLE
    : R E S U M A B L E
    ;

SYSGUID
    : S Y S G U I D
    ;

SYSBACKUP
    : S Y S B A C K U P
    ;

SYSDBA
    : S Y S D B A
    ;

SYSDG
    : S Y S D G
    ;

SYSKM
    : S Y S K M
    ;

SYSOPER
    : S Y S O P E R
    ;

DBA_RECYCLEBIN
    : D B A UL_ R E C Y C L E B I N
    ;

FIRST
    : F I R S T
    ;

NCHAR
    : N C H A R
    ;

RAW
    : R A W
    ;

VARCHAR
    : V A R C H A R
    ;

VARCHAR2
    : V A R C H A R [2]
    ;

NVARCHAR2
    : N V A R C H A R [2]
    ;

LONG
    : L O N G
    ;
    
BLOB
    : B L O B
    ;

CLOB
    : C L O B
    ;

NCLOB
    : N C L O B
    ;

BINARY_FLOAT
    : B I N A R Y UL_ F L O A T
    ;

BINARY_DOUBLE
    : B I N A R Y UL_ D O U B L E
    ;

PLS_INTEGER
    : P L S UL_ I N T E G E R
    ;

BINARY_INTEGER
    : B I N A R Y UL_ I N T E G E R
    ;

NUMBER
    : N U M B E R
    ;

NATURALN
    : N A T U R A L N
    ;

POSITIVE
    : P O S I T I V E
    ;

POSITIVEN
    : P O S I T I V E N
    ;

SIGNTYPE
    : S I G N T Y P E
    ;

SIMPLE_INTEGER
    : S I M P L E UL_ I N T E G E R
    ;

BFILE
    : B F I L E
    ;

MLSLABEL
    : M L S L A B E L
    ;

UROWID
    : U R O W I D
    ;

JSON
    : J S O N
    ;

DEC
    : D E C
    ;

SHARING
    : S H A R I N G
    ;

PRIVATE
    : P R I V A T E
    ;

SHARDED
    : S H A R D E D
    ;

SHARD
    : S H A R D
    ;

DUPLICATED
    : D U P L I C A T E D
    ;

METADATA
    : M E T A D A T A
    ;

DATA
    : D A T A
    ;

EXTENDED
    : E X T E N D E D
    ;

NONE
    : N O N E
    ;

MEMOPTIMIZE
    : M E M O P T I M I Z E
    ;

PARENT
    : P A R E N T
    ;

IDENTIFIER
    : I D E N T I F I E R
    ;

WORK
    : W O R K
    ;

CONTAINER_MAP
    : C O N T A I N E R UL_ M A P
    ;

CONTAINERS_DEFAULT
    : C O N T A I N E R S UL_ D E F A U L T
    ;

WAIT
    : W A I T
    ;

NOWAIT
    : N O W A I T
    ;

BATCH
    : B A T C H
    ;

BLOCK
    : B L O C K
    ;

REBUILD
    : R E B U I L D
    ;

INVALIDATION
    : I N V A L I D A T I O N
    ;

COMPILE
    : C O M P I L E
    ;

USABLE
    : U S A B L E
    ;

UNUSABLE
    : U N U S A B L E
    ;

ONLINE
    : O N L I N E
    ;

MONITORING
    : M O N I T O R I N G
    ;

NOMONITORING
    : N O M O N I T O R I N G
    ;

USAGE
    : U S A G E
    ;

COALESCE
    : C O A L E S C E
    ;

CLEANUP
    : C L E A N U P
    ;

PARALLEL
    : P A R A L L E L
    ;

NOPARALLEL
    : N O P A R A L L E L
    ;

LOG
    : L O G
    ;

REUSE
    : R E U S E
    ;

STORAGE
    : S T O R A G E
    ;

MATCHED
    : M A T C H E D
    ;

ERRORS
    : E R R O R S
    ;

REJECT
    : R E J E C T
    ;

RETENTION
    : R E T E N T I O N
    ;

CHUNK
    : C H U N K
    ;

PCTVERSION
    : P C T V E R S I O N
    ;

FREEPOOLS
    : F R E E P O O L S
    ;

AUTO
    : A U T O
    ;

DEDUPLICATE
    : D E D U P L I C A T E
    ;

KEEP_DUPLICATES
    : K E E P UL_ D U P L I C A T E S
    ;

COMPRESS
    : C O M P R E S S
    ;

HIGH
    : H I G H
    ;

MEDIUM
    : M E D I U M
    ;

LOW
    : L O W
    ;

NOCOMPRESS
    : N O C O M P R E S S
    ;

READS
    : R E A D S
    ;

CREATION
    : C R E A T I O N
    ;

PCTFREE
    : P C T F R E E
    ;

PCTUSED
    : P C T U S E D
    ;

INITRANS
    : I N I T R A N S
    ;

LOGGING
    : L O G G I N G
    ;

NOLOGGING
    : N O L O G G I N G
    ;

FILESYSTEM_LIKE_LOGGING
    : F I L E S Y S T E M UL_ L I K E UL_ L O G G I N G
    ;

INITIAL
    : I N I T I A L
    ;

MINEXTENTS
    : M I N E X T E N T S
    ;

MAXEXTENTS
    : M A X E X T E N T S
    ;

BASIC
    : B A S I C
    ;

ADVANCED
    : A D V A N C E D
    ;

PCTINCREASE
    : P C T I N C R E A S E
    ;

FREELISTS
    : F R E E L I S T S
    ;

DML
    : D M L
    ;

DDL
    : D D L
    ;

CAPACITY
    : C A P A C I T Y
    ;

FREELIST
    : F R E E L I S T
    ;

GROUPS
    : G R O U P S
    ;

OPTIMAL
    : O P T I M A L
    ;

BUFFER_POOL
    : B U F F E R UL_ P O O L
    ;

RECYCLE
    : R E C Y C L E
    ;

FLASH_CACHE
    : F L A S H UL_ C A C H E
    ;

CELL_FLASH_CACHE
    : C E L L UL_ F L A S H UL_ C A C H E
    ;

MAXSIZE
    : M A X S I Z E
    ;

STORE
    : S T O R E
    ;

LEVEL
    : L E V E L
    ;

LOCKING
    : L O C K I N G
    ;

INMEMORY
    : I N M E M O R Y
    ;

MEMCOMPRESS
    : M E M C O M P R E S S
    ;

PRIORITY
    : P R I O R I T Y
    ;

CRITICAL
    : C R I T I C A L
    ;

DISTRIBUTE
    : D I S T R I B U T E
    ;

RANGE
    : R A N G E
    ;

PARTITION
    : P A R T I T I O N
    ;

SUBPARTITION
    : S U B P A R T I T I O N
    ;

SERVICE
    : S E R V I C E
    ;

DUPLICATE
    : D U P L I C A T E
    ;

ILM
    : I L M
    ;

DELETE_ALL
    : D E L E T E UL_ A L L
    ;

ENABLE_ALL
    : E N A B L E UL_ A L L
    ;

DISABLE_ALL
    : D I S A B L E UL_ A L L
    ;

AFTER
    : A F T E R
    ;

MODIFICATION
    : M O D I F I C A T I O N
    ;

DAYS
    : D A Y S
    ;

MONTHS
    : M O N T H S
    ;

YEARS
    : Y E A R S
    ;

TIER
    : T I E R
    ;

ORGANIZATION
    : O R G A N I Z A T I O N
    ;

HEAP
    : H E A P
    ;

PCTTHRESHOLD
    : P C T T H R E S H O L D
    ;

PARAMETERS
    : P A R A M E T E R S
    ;

LOCATION
    : L O C A T I O N
    ;

MAPPING
    : M A P P I N G
    ;

NOMAPPING
    : N O M A P P I N G
    ;

INCLUDING
    : I N C L U D I N G
    ;

OVERFLOW
    : O V E R F L O W
    ;

ATTRIBUTES
    : A T T R I B U T E S
    ;

RESULT_CACHE
    : R E S U L T UL_ C A C H E
    ;

ROWDEPENDENCIES
    : R O W D E P E N D E N C I E S
    ;

NOROWDEPENDENCIES
    : N O R O W D E P E N D E N C I E S
    ; 

ARCHIVAL
    : A R C H I V A L
    ;

EXCHANGE
    : E X C H A N G E
    ;

INDEXING
    : I N D E X I N G
    ;

OFF
    : O F F
    ;

LESS
    : L E S S
    ;

INTERNAL
    : I N T E R N A L
    ;

VARRAY
    : V A R R A Y
    ;

NESTED
    : N E S T E D
    ;

COLUMN_VALUE
    : C O L U M N UL_ V A L U E
    ;

RETURN
    : R E T U R N
    ;

LOCATOR
    : L O C A  T O R
    ;

MODE
    : M O D E
    ;

LOB
    : L O B
    ;

SECUREFILE
    : S E C U R E F I L E
    ;

BASICFILE
    : B A S I C F I L E
    ;

THAN
    : T H A N
    ;

LIST
    : L I S T
    ;

AUTOMATIC
    : A U T O M A T I C
    ;

HASH
    : H A S H
    ;

PARTITIONS
    : P A R T I T I O N S
    ;

SUBPARTITIONS
    : S U B P A R T I T I O N S
    ;

TEMPLATE
    : T E M P L A T E
    ;

PARTITIONSET
    : P A R T I T I O N S E T
    ;

REFERENCE
    : R E F E R E N C E
    ;

CONSISTENT
    : C O N S I S T E N T
    ;

CLUSTERING
    : C L U S T E R I N G
    ;

LINEAR
    : L I N E A R
    ;

INTERLEAVED
    : I N T E R L E A V E D
    ;

YES
    : Y E S
    ;

LOAD
    : L O A D
    ;

MOVEMENT
    : M O V E M E N T
    ;

ZONEMAP
    : Z O N E M A P
    ;

WITHOUT
    : W I T H O U T
    ;

XMLTYPE
    : X M L T Y P E
    ;

RELATIONAL
    : R E L A T I O N A L
    ;

XML
    : X M L
    ;

VARRAYS
    : V A R R A Y S
    ;

LOBS
    : L O B S
    ;

TABLES
    : T A B L E S
    ;

ALLOW
    : A L L O W
    ;

DISALLOW
    : D I S A L L O W
    ;

NONSCHEMA
    : N O N S C H E M A
    ;

ANYSCHEMA
    : A N Y S C H E M A
    ;

XMLSCHEMA
    : X M L S C H E M A
    ;

COLUMNS
    : C O L U M N S
    ;

OIDINDEX
    : O I D I N D E X
    ;

EDITIONABLE
    : E D I T I O N A B L E
    ;

NONEDITIONABLE
    : N O N E D I T I O N A B L E
    ;

DEPENDENT
    : D E P E N D E N T
    ;

INDEXES
    : I N D E X E S
    ;

SHRINK
    : S H R I N K
    ;

SPACE
    : S P A C E
    ;

COMPACT
    : C O M P A C T
    ;

SUPPLEMENTAL
    : S U P P L E M E N T A L
    ;

ADVISE
    : A D V I S E
    ;

NOTHING
    : N O T H I N G
    ;

GUARD
    : G U A R D
    ;

SYNC
    : S Y N C
    ;

VISIBILITY
    : V I S I B I L I T Y
    ;

ACTIVE
    : A C T I V E
    ;

DEFAULT_COLLATION
    : D E F A U L T UL_ C O L L A T I O N
    ;

MOUNT
    : M O U N T
    ;

STANDBY
    : S T A N D B Y
    ;

CLONE
    : C L O N E
    ;

RESETLOGS
    : R E S E T L O G S
    ;

NORESETLOGS
    : N O R E S E T L O G S
    ;

UPGRADE
    : U P G R A D E
    ;

DOWNGRADE
    : D O W N G R A D E
    ;

RECOVER
    : R E C O V E R
    ;

LOGFILE
    : L O G F I L E
    ;

TEST
    : T E S T
    ;

CORRUPTION
    : C O R R U P T I O N
    ;

CONTINUE
    : C O N T I N U E
    ;

CANCEL
    : C A N C E L
    ;

UNTIL
    : U N T I L
    ;

CONTROLFILE
    : C O N T R O L F I L E
    ;

SNAPSHOT
    : S N A P S H O T
    ;

DATAFILE
    : D A T A F I L E
    ;

MANAGED
    : M A N A G E D
    ;

ARCHIVED
    : A R C H I V E D
    ;

DISCONNECT
    : D I S C O N N E C T
    ;

NODELAY
    : N O D E L A Y
    ;

INSTANCES
    : I N S T A N C E S
    ;

FINISH
    : F I N I S H
    ;

LOGICAL
    : L O G I C A L
    ;

FILE
    : F I L E
    ;

SIZE
    : S I Z E
    ;

AUTOEXTEND
    : A U T O E X T E N D
    ;

BLOCKSIZE
    : B L O C K S I Z E
    ;

OFFLINE
    : O F F L I N E
    ;

RESIZE
    : R E S I Z E
    ;

TEMPFILE
    : T E M P F I L E
    ;

DATAFILES
    : D A T A F I L E S
    ;

ARCHIVELOG
    : A R C H I V E L O G
    ;

MANUAL
    : M A N U A L
    ;

NOARCHIVELOG
    : N O A R C H I V E L O G
    ;

AVAILABILITY
    : A V A I L A B I L I T Y
    ;

PERFORMANCE
    : P E R F O R M A N C E
    ;

CLEAR
    : C L E A R
    ;

UNARCHIVED
    : U N A R C H I V E D
    ;

UNRECOVERABLE
    : U N R E C O V E R A B L E
    ;

THREAD
    : T H R E A D
    ;

MEMBER
    : M E M B E R
    ;

PHYSICAL
    : P H Y S I C A L
    ;

FAR
    : F A R
    ;

TRACE
    : T R A C E
    ;

DISTRIBUTED
    : D I S T R I B U T E D
    ;

RECOVERY
    : R E C O V E R Y
    ;

FLUSH
    : F L U S H
    ;

NOREPLY
    : N O R E P L Y
    ;

SWITCH
    : S W I T C H
    ;

LOGFILES
    : L O G F I L E S
    ;

PROCEDURAL
    : P R O C E D U R A L
    ;

REPLICATION
    : R E P L I C A T I O N
    ;

SUBSET
    : S U B S E T
    ;

ACTIVATE
    : A C T I V A T E
    ;

APPLY
    : A P P L Y
    ;

MAXIMIZE
    : M A X I M I Z E
    ;

PROTECTION
    : P R O T E C T I O N
    ;

SUSPEND
    : S U S P E N D
    ;

RESUME
    : R E S U M E
    ;

QUIESCE
    : Q U I E S C E
    ;

UNQUIESCE
    : U N Q U I E S C E
    ;

SHUTDOWN
    : S H U T D O W N
    ;

REGISTER
    : R E G I S T E R
    ;

PREPARE
    : P R E P A R E
    ;

SWITCHOVER
    : S W I T C H O V E R
    ;

FAILED
    : F A I L E D
    ;

SKIP_SYMBOL
    : S K I P
    ;

STOP
    : S T O P
    ;

ABORT
    : A B O R T
    ;

VERIFY
    : V E R I F Y
    ;

CONVERT
    : C O N V E R T
    ;

FAILOVER
    : F A I L O V E R
    ;

BIGFILE
    : B I G F I L E
    ;

SMALLFILE
    : S M A L L F I L E
    ;

TRACKING
    : T R A C K I N G
    ;

CACHING
    : C A C H I N G
    ;

CONTAINERS
    : C O N T A I N E R S
    ;

TARGET
    : T A R G E T
    ;

UNDO
    : U N D O
    ;

MOVE
    : M O V E
    ;

MIRROR
    : M I R R O R
    ;

COPY
    : C O P Y
    ;

UNPROTECTED
    : U N P R O T E C T E D
    ;

REDUNDANCY
    : R E D U N D A N C Y
    ;

REMOVE
    : R E M O V E
    ;

LOST
    : L O S T
    ;

LEAD_CDB
    : L E A D UL_ C D B
    ;

LEAD_CDB_URI
    : L E A D UL_ C D B UL_ U R I
    ;

PROPERTY
    : P R O P E R T Y
    ;

DEFAULT_CREDENTIAL
    : D E F A U L T UL_ C R E D E N T I A L
    ;

TIME_ZONE
    : T I M E UL_ Z O N E
    ;

RESET
    : R E S E T
    ;

RELOCATE
    : R E L O C A T E
    ;

CLIENT
    : C L I E N T
    ;

PASSWORDFILE_METADATA_CACHE
    : P A S S W O R D F I L E UL_ M E T A D A T A UL_ C A C H E
    ;

NOSWITCH
    : N O S W I T C H
    ;

POST_TRANSACTION
    : P O S T UL_ T R A N S A C T I O N
    ;

KILL
    : K I L L
    ;

ROLLING
    : R O L L I N G
    ;

MIGRATION
    : M I G R A T I O N
    ;

PATCH
    : P A T C H
    ;

ENCRYPTION
    : E N C R Y P T I O N
    ;

WALLET
    : W A L L E T
    ;

AFFINITY
    : A F F I N I T Y
    ;

MEMORY
    : M E M O R Y
    ;

SPFILE
    : S P F I L E
    ;

BOTH
    : B O T H
    ;

SID
    : S I D
    ;

SHARED_POOL
    : S H A R E D UL_ P O O L
    ;

BUFFER_CACHE
    : B U F F E R UL_ C A C H E
    ;

REDO
    : R E D O
    ;

CONFIRM
    : C O N F I R M
    ;

MIGRATE
    : M I G R A T E
    ;

USE_STORED_OUTLINES
    : U S E UL_ S T O R E D UL_ O U T L I N E S
    ;

GLOBAL_TOPIC_ENABLED
    : G L O B A L UL_ T O P I C UL_ E N A B L E D
    ;

INTERSECT
    : I N T E R S E C T
    ;

MINUS
    : M I N U S
    ;

LOCKED
    : L O C K E D
    ;

FETCH
    : F E T C H
    ;

PERCENT
    : P E R C E N T
    ;

TIES
    : T I E S
    ;

SIBLINGS
    : S I B L I N G S
    ;

NULLS
    : N U L L S
    ;

LAST
    : L A S T
    ;

ISOLATION
    : I S O L A T I O N
    ;

SERIALIZABLE
    : S E R I A L I Z A B L E
    ;

COMMITTED
    : C O M M I T T E D
    ;

FILTER
    : F I L T E R
    ;

FACT
    : F A C T
    ;

DETERMINISTIC
    : D E T E R M I N I S T I C
    ;

PIPELINED
    : P I P E L I N E D
    ;

PARALLEL_ENABLE
    : P A R A L L E L UL_ E N A B L E
    ;

OUT
    : O U T
    ;

NOCOPY
    : N O C O P Y
    ;

ACCESSIBLE
    : A C C E S S I B L E
    ;

PACKAGE
    : P A C K A G E
    ;

PACKAGES
    : P A C K A G E S
    ;

USING_NLS_COMP
    : U S I N G UL_ N L S UL_ C O M P
    ;

AUTHID
    : A U T H I D
    ;

SEARCH
    : S E A R C H
    ;

DEPTH
    : D E P T H
    ;

BREADTH
    : B R E A D T H
    ;

ANALYTIC
    : A N A L Y T I C
    ;

HIERARCHIES
    : H I E R A R C H I E S
    ;

MEASURES
    : M E A S U R E S
    ;

OVER
    : O V E R
    ;

LAG
    : L A G
    ;

LAG_DIFF
    : L A G UL_ D I F F
    ;

LAG_DIF_PERCENT
    : L A G UL_ D I F UL_ P E R C E N T
    ;

LEAD
    : L E A D
    ;

LEAD_DIFF
    : L E A D UL_ D I F F
    ;

LEAD_DIFF_PERCENT
    : L E A D UL_ D I F F UL_ P E R C E N T
    ;

HIERARCHY
    : H I E R A R C H Y
    ;

WITHIN
    : W I T H I N
    ;

ACROSS
    : A C R O S S
    ;

ANCESTOR
    : A N C E S T O R
    ;

BEGINNING
    : B E G I N N I N G
    ;

UNBOUNDED
    : U N B O U N D E D
    ;

PRECEDING
    : P R E C E D I N G
    ;

FOLLOWING
    : F O L L O W I N G
    ;

RANK
    : R A N K
    ;

DENSE_RANK
    : D E N S E UL_ R A N K
    ;

AVERAGE_RANK
    : A V E R A G E UL_ R A N K
    ;

ROW_NUMBER
    : R O W UL_ N U M B E R
    ;

SHARE_OF
    : S H A R E UL_ O F
    ;

HIER_ANCESTOR
    : H I E R UL_ A N C E S T O R
    ;

HIER_PARENT
    : H I E R UL_ P A R E N T
    ;

HIER_LEAD
    : H I E R UL_ L E A D
    ;

HIER_LAG
    : H I E R UL_ L A G
    ;

QUALIFY
    : Q U A L I F Y
    ;

HIER_CAPTION
    : H I E R UL_ C A P T I O N
    ;

HIER_DEPTH
    : H I E R UL_ D E P T H
    ;

HIER_DESCRIPTION
    : H I E R UL_ D E S C R I P T I O N
    ;

HIER_LEVEL
    : H I E R UL_ L E V E L
    ;

HIER_MEMBER_NAME
    : H I E R UL_ M E M B E R UL_ N A M E
    ;

HIER_MEMBER_UNIQUE_NAME
    : H I E R UL_ M E M B E R UL_ U N I Q U E UL_ N A M E
    ;

CHAINED
    : C H A I N E D
    ;

STATISTICS
    : S T A T I S T I C S
    ;

DANGLING
    : D A N G L I N G
    ;

STRUCTURE
    : S T R U C T U R E
    ;

FAST
    : F A S T
    ;

COMPLETE
    : C O M P L E T E
    ;

ASSOCIATE
    : A S S O C I A T E
    ;

DISASSOCIATE
    : D I S A S S O C I A T E
    ;

FUNCTIONS
    : F U N C T I O N S
    ;

TYPES
    : T Y P E S
    ;

SELECTIVITY
    : S E L E C T I V I T Y
    ;

RETURNING
    : R E T U R N I N G
    ;

VERSIONS
    : V E R S I O N S
    ;

SCN
    : S C N
    ;

PERIOD
    : P E R I O D
    ;

LATERAL
    : L A T E R A L
    ;

BADFILE
    : B A D F I L E
    ;

DISCARDFILE
    : D I S C A R D F I L E
    ;

PIVOT
    : P I V O T
    ;

UNPIVOT
    : U N P I V O T
    ;

INCLUDE
    : I N C L U D E
    ;

EXCLUDE
    : E X C L U D E
    ;

SAMPLE
    : S A M P L E
    ;

SEED
    : S E E D
    ;

OPTION
    : O P T I O N
    ;

SHARDS
    : S H A R D S
    ;

MATCH_RECOGNIZE
    : M A T C H UL_ R E C O G N I Z E
    ;

PATTERN
    : P A T T E R N
    ;

DEFINE
    : D E F I N E
    ;

ONE
    : O N E
    ;

PER
    : P E R
    ;

MATCH
    : M A T C H
    ;

PAST
    : P A S T
    ;

PERMUTE
    : P E R M U T E
    ;

CLASSIFIER
    : C L A S S I F I E R
    ;

MATCH_NUMBER
    : M A T C H UL_ N U M B E R
    ;

RUNNING
    : R U N N I N G
    ;

FINAL
    : F I N A L
    ;

PREV
    : P R E V
    ;

NOAUDIT
    : N O A U D I T
    ;

WHENEVER
    : W H E N E V E R
    ;

SUCCESSFUL
    : S U C C E S S F U L
    ;

USERS
    : U S E R S
    ;

GRANTED
    : G R A N T E D
    ;

ROLES
    : R O L E S
    ;

NAMESPACE
    : N A M E S P A C E
    ;

ROLLUP
    : R O L L U P
    ;

GROUPING
    : G R O U P I N G
    ;

SETS
    : S E T S
    ;

DECODE
    : D E C O D E
    ;
