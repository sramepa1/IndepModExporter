CREATE OR REPLACE uu_hr_pkg AS
/************************************************************************
/* UU_MOD PL/SQL NAME: uu_hr_pkg
/************************************************************************
/* DESCRIPTION: Description of U of U PL/SQL code...
/*              ...
/*              ...
/*
/* CONTROL #'S: PR#=sssnnn SYS#=sssnnnnnn
/*
/*       INPUT: Input sources...
/*
/*      OUTPUT: Output files and reports...
/*
/*      AUTHOR: University of Utah, ACS
/*
/*     HISTORY:
/* Developer-Name (Last, First) mm/dd/yyyy Original Development
/* Developer-Name (Last, First) mm/dd/yyyy PR#=sssnnn (if different from above) Description of U of U modification...
/* ...
/* ...
/************************************************************************
CREATE OR REPLACE PACKAGE BODY uu_hr_pkg
IS
   g_ssn           VARCHAR2(10) := NULL;
   g_emplid1       VARCHAR2(50) := NULL;
   g_emplid2       VARCHAR2(50) := NULL;
   g_name          VARCHAR2(100) := NULL;
--
-- ************************************************************************/
-- ** FUNCTION - GetEmplNm    It returns the name for the employee        */
-- **            based on the EMPLID passed in.                           */
-- ************************************************************************/
--
  FUNCTION GetEmplNm  (p_emplid    IN VARCHAR2)
       RETURN  VARCHAR2
  IS
  BEGIN
--------------------------------------------------------------------------------------------------------
-- IF statement explanation
--  Is it the same employee, then RETURN previous name.  Otherwise,
--  get employee name
--------------------------------------------------------------------------------------------------------
    IF NVL(g_emplid1,'X') <> p_emplid THEN
      BEGIN
        SELECT name
          INTO g_name
          FROM ps_personal_data
          WHERE emplid = p_emplid;
        EXCEPTION
          WHEN OTHERS THEN
            g_name := NULL;
      END;
      g_emplid1 := p_emplid;
    END IF;
    RETURN g_name;
  END GetEmplNm;

--
-- ****************************************************************************
-- ** FUNCTION - GetEmplSsn    It returns the SSN for the employee
-- **            based on the EMPLID passed in.
-- ****************************************************************************
--
  FUNCTION GetEmplSsn
      (p_emplid    IN VARCHAR2)
       RETURN  VARCHAR2
  IS
  BEGIN
    ...
------------------------------------------------------------------------------------------------
-- FOR LOOP   explanation
------------------------------------------------------------------------------------------------
      FOR  emp_rec IN c_empl
      LOOP
        IF emp_rec.empl_rcd# > 0 THEN
          ...
        ELSE
          ...
        END IF;
      END LOOP;
    RETURN g_name;
  END GetEmplNm;
END uu_hr_pkg;

