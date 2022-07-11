package wpos.utils;

public class FastDoubleClickUtils {
  private static int VIEW_TAG_CLICK = 2 << 24;

//  public static boolean isFastDoubleClick(View view) {
//    Long lastClickTime = (Long) view.getTag(VIEW_TAG_CLICK);
//    if (null == lastClickTime) {
//      lastClickTime = 0L;
//    }
//    long time = System.currentTimeMillis();
//    view.setTag(VIEW_TAG_CLICK, time);
//    long timeD = time - lastClickTime;
//    if (0 < timeD && timeD < 500) {
//      return true;
//    }
//    return false;
//  }

}
