package com.bx.erp.view.component;

import android.app.Dialog;
import android.content.Context;
import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.ContentLoadingProgressBar;

import com.bx.erp.R;

/**
 * Created by Ellen on 2018/06/20.
 * 加载的loading
 */

public class LoadingDialog {
  Dialog dialog;

  public LoadingDialog(Context context) {
    createLoadingDialog(context);
  }

  private void createLoadingDialog(Context context) {
    dialog = new Dialog(context, R.style.LoadingDialog);
    dialog.setContentView(R.layout.layout_loading_dialog);
    ContentLoadingProgressBar progressBar = (ContentLoadingProgressBar) dialog.findViewById(R.id.progressBar);
    progressBar.getIndeterminateDrawable()
        .setColorFilter(ContextCompat.getColor(context, R.color.colorAccent), PorterDuff.Mode.MULTIPLY);
  }

  public void show() {
    dialog.show();
  }

  public void dismiss() {
    if (dialog.isShowing()) {
      dialog.dismiss();
    }
  }
}
