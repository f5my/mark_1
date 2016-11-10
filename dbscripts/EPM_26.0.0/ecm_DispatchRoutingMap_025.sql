
set SERVEROUTPUT ON FORMAT WRAPPED
set APPINFO ON
SET VERIFY OFF

SET FEEDBACK OFF
SET TERMOUT OFF

column date_column new_value today_var
column scriptname new_value thescriptname;
column sessionname new_value sessionname;
column dbname new_value dbname;
select 
                SUBSTR(sys_context('USERENV', 'MODULE'), instr(sys_context('USERENV', 'MODULE'), ' ') +1) || '_' ||  sys_context('USERENV','SESSION_USER') || '_' ||sys_context('USERENV','DB_NAME')  || '_'|| to_char(sysdate, 'yyyymmdd') date_column,
  	sys_context('USERENV', 'MODULE') scriptname,
	sys_context('USERENV','SESSION_USER') sessionname,
	sys_context('USERENV','DB_NAME') dbname
from dual;
  
SPOOL &today_var..log

SET VERIFY ON
SET FEEDBACK ON
SET TERMOUT On

insert into appdba_util.scriptinventory (
SCRIPTNAME,
CREATEDATE,
SCHEMANAME,
DATABASE,
ROLLBACKIND,
FAILUREIND,
localGlobalInd)
values
('&thescriptname',
 sysdate,
 '&sessionname',
 '&dbname',
 'N',
 'N',
 'G');
commit;

set echo on;

------------------------------------
--  Created 11/10/2016 - Angel Yang
--  Update targetServerEnvironmentTypeCd to null per Anand's email dated 11/10/2016 @ 1:57 pm.
------------------------------------


SELECT count(*) 
FROM   DispatchRoutingMap
WHERE  TARGETTRADINGPARTNERDENTIFIER = 'PPCCIIO01'
AND    physicalDocumentTypeCd in ('SBMS','SBMR','SBMAR','SBMIS');

UPDATE DispatchRoutingMap
SET    targetServerEnvironmentTypeCd = null, TARGETTRADINGPARTNERDENTIFIER = 'SBCCIIO01'		
WHERE  physicalDocumentTypeCd in ('SBMS','SBMR','SBMAR','SBMIS')
AND	   TARGETTRADINGPARTNERDENTIFIER = 'PPCCIIO01';

SELECT count(*) 
FROM   DispatchRoutingMap
WHERE  TARGETTRADINGPARTNERDENTIFIER = 'PPCCIIO01'
AND    physicalDocumentTypeCd in ('SBMS','SBMR','SBMAR','SBMIS');

commit;

set echo off;
spool off;

