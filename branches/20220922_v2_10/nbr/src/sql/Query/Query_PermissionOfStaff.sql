
-- ��ѯĳ��staff����ĳ��Ȩ��

SELECT st.F_Name, trp.*, tr.F_Name, p.F_SP, p.F_Name, p.F_Domain 
FROM t_staff st, t_staffrole sr, t_role_permission trp, t_role tr, t_permission p 
WHERE trp.F_RoleID = tr.F_ID AND p.F_ID = trp.F_PermissionID AND trp.F_RoleID = 4 AND st.F_ID = sr.F_StaffID 
	AND st.F_ID = 3 -- ָ��staffID
	AND p.F_SP = 'SP_PurchasingOrder_RetrieveN' -- ָ��SP����		
	;
		
-- ��ѯĳ��staff����ϸ��Ϣ������RoleID

SELECT st.F_Name, st.F_Phone, tr.F_Name, tr.F_ID AS RoleID
FROM t_staff st, t_staffrole sr, t_role tr
WHERE st.F_ID = sr.F_StaffID AND sr.F_StaffID = st.F_ID AND tr.F_ID = sr.F_RoleID
--	AND st.F_ID = 1
		;