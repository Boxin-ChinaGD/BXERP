DROP PROCEDURE IF EXISTS `SP_WxUser_Create`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_WxUser_Create`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN subscribe VARCHAR(5),
	IN oppenId VARCHAR(50),
	IN nickname VARCHAR(100),
	IN sex VARCHAR(5),
	IN iLangauge VARCHAR(10),
	IN city VARCHAR(20),
	IN province VARCHAR(10),
	IN country VARCHAR(100),
	IN headImgUrl VARCHAR(200),
	IN subscribeTime VARCHAR(30),
	IN unionId VARCHAR(30),
	IN remark VARCHAR(100),
	IN groupId VARCHAR(100),
	IN tagIdList VARCHAR(100),
	IN subscribeScene VARCHAR(100),
	IN qrScene VARCHAR(30),
	IN qrSceneStr VARCHAR(30)
)
BEGIN 
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := '数据库错误';
		ROLLBACK;
	END;
	
	START TRANSACTION;
	
		IF(subscribe IS NULL OR subscribe = '') THEN
			SET iErrorCode := 7;
			SET sErrorMsg := '缺少subscribe创建失败';
		ELSEIF (oppenId IS NULL OR oppenId = '') THEN
			SET iErrorCode := 7;
			SET sErrorMsg := '缺少openid，创建失败';
		ELSE
			INSERT INTO t_wxuser(
		   			F_Subscribe,
		   			F_OpenId,
		  			F_NickName,
		  			F_Sex,
		 			F_Language,
		  			F_City,
		  			F_Province, 
		  			F_Country,
		  			F_HeadImgUrl,
		  			F_SubscribeTime,
		  			F_Unionid,
					F_Remark,
		  		 	F_GroupId,
		   			F_TagIdList,
		   			F_SubscribeScene,
		 			F_QrScene,
		  			F_QrSceneStr)
	  			VALUES(
				  subscribe,
	   			  oppenId,
	   			  nickname,
	  			  sex,
				  iLangauge,
	 			  city,
	 			  province,
	 			  country,
	 			  headImgUrl,
	 			  subscribeTime,
	 			  unionId,
	 			  remark,
	 			  groupId,
	 			  tagIdList,
	 			  subscribeScene,
	 			  qrScene,
	  			  qrSceneStr
				);
				
			SELECT F_ID, F_Subscribe, F_OpenId, F_NickName, F_Sex, F_Language, F_City, F_Province, F_Country, F_HeadImgUrl, F_SubscribeTime, F_Unionid, F_Remark, F_GroupId, F_TagIdList, F_SubscribeScene, F_QrScene FROM t_wxuser WHERE F_ID = LAST_INSERT_ID();
			
			SET iErrorCode := 0;
			SET sErrorMsg := '';
		END IF;
	
	COMMIT;
END;
		