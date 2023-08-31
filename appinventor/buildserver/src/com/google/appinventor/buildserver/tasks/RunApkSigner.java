// -*- mode: java; c-basic-offset: 2; -*-
// Copyright 2021-2023 MIT, All rights reserved
// Released under the Apache License, Version 2.0
// http://www.apache.org/licenses/LICENSE-2.0

package com.google.appinventor.buildserver.tasks;

import com.google.appinventor.buildserver.BuildType;
import com.google.appinventor.buildserver.CompilerContext;
import com.google.appinventor.buildserver.Execution;
import com.google.appinventor.buildserver.TaskResult;
import com.google.appinventor.buildserver.interfaces.Task;

/**
 * compiler.runApkSigner()
 */
@BuildType(apk = true)
public class RunApkSigner implements Task {
  @Override
  public TaskResult execute(CompilerContext context) {
    int mx = context.getChildProcessRam() - 200;
    /*
      apksigner sign\
      --ks <keystore file>\
      --ks-key-alias AndroidKey\
      --ks-pass pass:android\
      <APK>
    */
    String[] apksignerCommandLine = {
        System.getProperty("java.home") + "/bin/java", "-jar",
        "-mx" + mx + "M",
        context.getResources().getApksignerJar(), "sign",
        "-ks", context.getKeystoreFilePath(),
        "-ks-key-alias", "AndroidKey",
        "-ks-pass", "pass:android",
        context.getPaths().getDeployFile().getAbsolutePath()
    };

    if (!Execution.execute(null, apksignerCommandLine,
        System.out, System.err)) {
      TaskResult.generateError("Error while running ZipAligned tool");
    }

    return TaskResult.generateSuccess();
  }
}
