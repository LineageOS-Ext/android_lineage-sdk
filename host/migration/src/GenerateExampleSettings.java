/*
 * Copyright (C) 2015 The CyanogenMod Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.IOException;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * This is to be run on a live Lineage 12.1 device.
 */
public class GenerateExampleSettings {

    private static ArrayList<Setting> androidSystemSettingList = new ArrayList<Setting>();
    private static ArrayList<Setting> androidSecureSettingList = new ArrayList<Setting>();
    private static ArrayList<Setting> androidGlobalSettingList = new ArrayList<Setting>();
    private static ArrayList<Setting> defaultSettings = new ArrayList<Setting>();

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        if (args.length != 1) {
            System.err.println("Usage: GenerateExampleSettings [target file]");
            System.exit(-1);
        }

        String rootFile = args[0];
        SettingImageCommands androidSettingImage =
                new SettingImageCommands(SettingsConstants.SETTINGS_AUTHORITY);
        androidSettingImage.addQuery(SettingsConstants.SYSTEM, androidSystemSettingList);
        androidSettingImage.addQuery(SettingsConstants.SECURE, androidSecureSettingList);
        androidSettingImage.addQuery(SettingsConstants.GLOBAL, androidGlobalSettingList);
        androidSettingImage.execute();

        for (Setting system : androidSystemSettingList) {
            if (LineageSettings.System.isLegacySetting(system.getKey())) {
                defaultSettings.add(system);
            }
        }

        for (Setting secure : androidSecureSettingList) {
            if (LineageSettings.Secure.isLegacySetting(secure.getKey())) {
                defaultSettings.add(secure);
            }
        }

        for (Setting global : androidGlobalSettingList) {
            if (LineageSettings.Global.isLegacySetting(global.getKey())) {
                defaultSettings.add(global);
            }
        }

        Writer out = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(rootFile),
                Charset.forName("US-ASCII")));

        out.write("# Settings which are to be moved to LineageSettings\n");
        out.write("# Automatically generated by " +
                "lineage-sdk/host/migration/src/GenerateExampleSettings"
                + GenerateExampleSettings.class.getSimpleName() + ".java.\n");
        // Write settings to file for output.
        for (int i = 0; i < defaultSettings.size(); i++) {
            Setting defaultSetting = defaultSettings.get(i);
            // This is the same format as what is spit out by system/bin/content
            out.write("Row: " + i + " name=" + defaultSetting.getKey()
                    + ", type=" + defaultSetting.getKeyType()
                    + ", value=" + defaultSetting.getValue()
                    + ", type=" + defaultSetting.getValueType() + "\n");
        }
        out.close();

        System.out.println("Settings written: " + rootFile.toString());
    }
}
