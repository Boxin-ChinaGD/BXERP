package com.bx.erp.di.modules;

import android.app.Activity;

import com.bx.erp.di.PerActivity;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Ellen on 2018/06/20.
 */
@Module
public class ActivityModule {
  private final Activity activity;

  public ActivityModule(Activity activity) {
    this.activity = activity;
  }

  /**
   * Expose the activity to dependents in the graph.
   */
  @Provides @PerActivity Activity activity() {
    return this.activity;
  }
}
