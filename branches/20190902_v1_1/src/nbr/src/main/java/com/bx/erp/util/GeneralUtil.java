package com.bx.erp.util;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.cache.BaseCache;
import com.bx.erp.cache.CacheManager;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.config.BxConfigGeneral;

public class GeneralUtil {
	private static Log logger = LogFactory.getLog(GeneralUtil.class);

	protected static DecimalFormat formatToCalculate = new DecimalFormat("0.000000");
	protected static DecimalFormat formatToShow = new DecimalFormat("0.00");
	public static Random random = new Random();
	/** 临时的磁盘路径 */
	private static final String Disk = "D:/";

	public static String getStringTime(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default2);// 设置格式化格式
		return sdf.format(date);// 返回格式化后的时间
	}

	public static String toViewNO(double f) {
		DecimalFormat df = new DecimalFormat("0.00");
		return df.format(f);
	}

	/** 对数组ia进行排序并消除重复元素
	 * 
	 * @param ia
	 *            可能为null，可能没有元素
	 * @return 非null的List<Integer> */
	public static List<Integer> sortAndDeleteDuplicated(Integer[] ia) {
		logger.info("对数组ia进行排序并消除重复元素,ia=" + ia);

		List<Integer> list = new ArrayList<Integer>();

		if (ia == null) {
			return list;
		}

		Arrays.sort(ia);

		for (int i = 0; i < ia.length; i++) {
			if (i == 0) {
				list.add(ia[i]);
			} else {
				if (ia[i] != ia[i - 1]) {
					list.add(ia[i]);
				}
			}
		}

		logger.info("list=" + list);
		return list;
	}

	public static void printCurrentFunction(String key, Object value) {
		StackTraceElement ste = new Exception().getStackTrace()[1];
		logger.info(new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default2).format(new Date()) + "\t\t当前执行的方法：" + ste.getClassName() + "." + ste.getMethodName() + "()...");
		if (key != null) {
			logger.info(key + "=" + value);
		}
	}

	public static String printArray(Object[] arr) {
		if (arr == null) {
			return "";
		} else {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < arr.length; i++) {
				sb.append(arr[i].toString() + " ");
			}
			return sb.toString();
		}
	}

	/** 检查ia中有无重复元素 */
	public static boolean hasDuplicatedElement(Integer[] ia) {
		if (ia == null) {
			return true; // null算出错，所以返回true
		}

		HashMap<Integer, Integer> hm = new HashMap<Integer, Integer>();
		for (Integer i : ia) {
			if (hm.containsKey(i)) {
				return true;
			}
			hm.put(i, i);
		}

		return false;
	}

	public static Integer[] toIntArray(String ids) {
		logger.info("将string数组转换为Int");

		String[] sarrID;
		Integer[] iArrID;
		try {
			sarrID = ids.split(",");
			iArrID = new Integer[sarrID.length];
			for (int i = 0; i < sarrID.length; i++) {
				iArrID[i] = Integer.parseInt(sarrID[i]);
			}
		} catch (Exception e) {
			iArrID = null;
		}

		return iArrID;
	}

	public static Double[] toDoubleArray(String ids) {
		String[] sarrID = ids.split(",");
		Double[] iArrID = new Double[sarrID.length];
		try {
			for (int i = 0; i < sarrID.length; i++) {
				iArrID[i] = Double.parseDouble(sarrID[i]);
			}
		} catch (Exception e) {
			iArrID = null;
		}
		return iArrID;
	}

	/** 对Int类型数组进行排序 TODO: Test未编写 **/
	public static int[] toIntDesc(int[] ids) {
		int temp = -1;
		for (int i = 0; i < ids.length; i++) {
			for (int j = i + 1; j < ids.length; j++) {
				if (ids[i] > ids[j]) {
					temp = ids[i];
					ids[i] = ids[j];
					ids[j] = temp;
				}
			}
		}
		return ids;
	}

	/** 将String[]的内容放在一个String中，用于打印 */
	public static String stringArrayToString(String[] sa) {
		if (sa != null) {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < sa.length; i++) {
				sb.append(sa[i]);
				sb.append("\r\n");
			}
			return sb.toString();
		}

		return "";
	}

	public static String formatToCalculate(Double d1) {
		formatToCalculate.setRoundingMode(RoundingMode.HALF_UP);
		return formatToCalculate.format(d1);
	}

	public static String formatToShow(Double d1) {
		formatToShow.setRoundingMode(RoundingMode.HALF_UP);
		return formatToShow.format(d1);
	}

	/** @param o1
	 * @return 精度计算后的数据. */
	public static double round(Object o1) {
		BigDecimal bd = new BigDecimal(o1.toString());
		bd = bd.setScale(6, RoundingMode.HALF_UP);
		double d = bd.doubleValue();
		bd = null;
		return d;
	}

	/** @param o1
	 * @return 精度计算后的数据. */
	public static double round(Object o1, int number) {
		BigDecimal bd = new BigDecimal(o1.toString());
		bd = bd.setScale(number, RoundingMode.HALF_UP);
		double d = bd.doubleValue();
		bd = null;
		return d;
	}

	/** double 相加
	 *
	 * @param d1
	 * @param d2
	 * @return */
	public static double sum(double d1, double d2) {
		BigDecimal bd1 = new BigDecimal(Double.toString(d1));
		BigDecimal bd2 = new BigDecimal(Double.toString(d2));
		return bd1.add(bd2).doubleValue();
	}

	/** double 相减
	 *
	 * @param d1
	 * @param d2
	 * @return */
	public static double sub(double d1, double d2) {
		BigDecimal bd1 = new BigDecimal(Double.toString(d1));
		BigDecimal bd2 = new BigDecimal(Double.toString(d2));
		return bd1.subtract(bd2).doubleValue();
	}

	/** 多个double 相加
	 * 
	 * @param ...
	 * @return */
	public static double sumN(double... param) {
		double sumN = param[0];
		for (int i = 1; i < param.length; i++) {
			sumN = sum(sumN, param[i]);
		}
		return sumN;
	}

	/** double 乘法
	 *
	 * @param d1
	 * @param d2
	 * @return */
	public static double mul(double d1, double d2) {
		BigDecimal bd1 = new BigDecimal(Double.toString(d1));
		BigDecimal bd2 = new BigDecimal(Double.toString(d2));
		return bd1.multiply(bd2).doubleValue();
	}

	/** double 除法
	 *
	 * @param d1
	 * @param d2
	 * @param scale
	 *            四舍五入 小数点位数
	 * @return */
	public static double div(double d1, double d2, int scale) {
		BigDecimal bd1 = new BigDecimal(Double.toString(d1));
		BigDecimal bd2 = new BigDecimal(Double.toString(d2));
		return bd1.divide(bd2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	public static String generateMnemonicCode(String name, String defaultString) {
		ChineseToEN cte = new ChineseToEN();// 用于转换字符串为拼音首字母
		String mnemonicCode = cte.getAllFirstLetter(name);
		char[] c = mnemonicCode.toCharArray();
		StringBuffer sb = new StringBuffer();
		//
		for (int i = 0; i < c.length; i++) {
			if (('a' <= c[i] && c[i] <= 'z') || Character.isDigit(c[i])) {
				sb.append(c[i]);
			}
		}
		if (sb.length() == 0) {
			return defaultString;
		}
		return sb.toString();
	}

	public static boolean hasDulicatedElement(int[] array) {
		HashSet<Integer> hashSet = new HashSet<Integer>();
		for (int i = 0; i < array.length; i++) {
			hashSet.add(array[i]);
		}
		if (hashSet.size() == array.length) {
			return true;
		} else {
			return false;
		}
	}

	/** 检查磁盘空间是否足够和存储路径是否存在 */
	public static boolean checkDiskSpaceAndCreateFolder(Map<String, Object> params, String path) {
		File diskPath = new File(Disk);
		if (!diskPath.exists()) {
			logger.error("该路径不存在:" + diskPath);
			params.put(BaseAction.KEY_HTMLTable_Parameter_msg, "服务器错误");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
			return false;
		}
		ErrorInfo ecOut = new ErrorInfo();
		BxConfigGeneral extraDiskSpaceSize = (BxConfigGeneral) CacheManager.getCache(BaseAction.DBName_Public, EnumCacheType.ECT_BXConfigGeneral).read1(BaseCache.EXTRA_DISK_SPACE_SIZE, BaseBO.SYSTEM, ecOut, BaseAction.DBName_Public);

		long minUsablePatitionSpace = 1;
		logger.info("磁盘可用空间为:" + diskPath.getUsableSpace() / (1024 * 1024 * 1024) + "GB");
		if (diskPath.getUsableSpace() - Integer.valueOf(extraDiskSpaceSize.getValue()) < minUsablePatitionSpace) {
			logger.error("磁盘空间不足!");
			params.put(BaseAction.KEY_HTMLTable_Parameter_msg, "服务器错误");
			params.put(BaseAction.JSON_ERROR_KEY, EnumErrorCode.EC_OtherError.toString());
			return false;
		}
		// 判断文件夹是否存在，如果不存在，创建相应的文件夹,String1传回来DBName
		File uploadDir = new File(path);
		if (!uploadDir.exists()) {
			uploadDir.mkdirs();
		}
		return true;
	}
	
    /**
     * 递归删除目录下的所有文件及子目录下所有文件
     */
    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            //递归删除目录中的子目录下
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }
}
