package com.telephoto;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.module.model.ReactModuleInfoProvider;
import com.facebook.react.TurboReactPackage;
import com.facebook.react.uimanager.ViewManager;
import com.margelo.nitro.telephoto.*;
import com.margelo.nitro.telephoto.views.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TelephotoPackage extends TurboReactPackage {
  @Nullable
  @Override
  public NativeModule getModule(@NonNull String name, @NonNull ReactApplicationContext reactContext) {
    return null;
  }

  @NonNull
  @Override
  public ReactModuleInfoProvider getReactModuleInfoProvider() {
    return HashMap::new;
  }

  @NonNull
  @Override
  public List<ViewManager> createViewManagers(@NonNull ReactApplicationContext reactContext) {
    List<ViewManager> viewManagers = new ArrayList<>();
    viewManagers.add(new HybridTelephotoManager());
    return viewManagers;
  }

  static {
    TelephotoOnLoad.initializeNative();
  }
}
