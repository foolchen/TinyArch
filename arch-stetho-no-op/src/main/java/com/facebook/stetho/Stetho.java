/*
 * Copyright (c) 2014-present, Facebook, Inc.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */

package com.facebook.stetho;

import android.content.Context;
import com.facebook.stetho.dumpapp.DumperPlugin;

public class Stetho {
  private Stetho() {
  }

  public static InitializerBuilder newInitializerBuilder(Context context) {
    return new InitializerBuilder(context);
  }

  public static void initializeWithDefaults(final Context context) {
  }

  /**
   * Start the listening service, providing a custom initializer as per
   * {@link #newInitializerBuilder}.
   *
   * @see #initializeWithDefaults(Context)
   */
  public static void initialize(final Initializer initializer) {
  }

  public static DumperPluginsProvider defaultDumperPluginsProvider(final Context context) {
    return new DumperPluginsProvider() {
      @Override public Iterable<DumperPlugin> get() {
        return new DefaultDumperPluginsBuilder(context).finish();
      }
    };
  }

  public static InspectorModulesProvider defaultInspectorModulesProvider(final Context context) {
    return new InspectorModulesProvider() {
    };
  }

  /**
   * Convenience mechanism to extend the default set of dumper plugins provided by Stetho.
   *
   * @see #initializeWithDefaults(Context)
   */
  public static final class DefaultDumperPluginsBuilder {

    public DefaultDumperPluginsBuilder(Context context) {
    }

    public DefaultDumperPluginsBuilder provide(DumperPlugin plugin) {
      return this;
    }

    public Iterable<DumperPlugin> finish() {
      return null;
    }
  }

  /**
   * Callers can choose to subclass this directly to provide the initialization configuration
   * or they can construct a concrete instance using {@link #newInitializerBuilder(Context)}.
   */
  public static abstract class Initializer {
  }

  /**
   * Configure what services are to be enabled in this instance of Stetho.
   */
  public static class InitializerBuilder {
    final Context mContext;

    private InitializerBuilder(Context context) {
      mContext = context.getApplicationContext();
    }

    public InitializerBuilder enableDumpapp(DumperPluginsProvider plugins) {
      return this;
    }

    public InitializerBuilder enableWebKitInspector(InspectorModulesProvider modules) {
      return this;
    }

    public Initializer build() {
      return null;
    }
  }
}
