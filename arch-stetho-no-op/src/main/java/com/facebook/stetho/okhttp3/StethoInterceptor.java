/*
 * Copyright (c) 2014-present, Facebook, Inc.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */

package com.facebook.stetho.okhttp3;

import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Response;

public class StethoInterceptor implements Interceptor {
  @Override public Response intercept(Chain chain) throws IOException {
    return chain.proceed(chain.request());
  }
}
