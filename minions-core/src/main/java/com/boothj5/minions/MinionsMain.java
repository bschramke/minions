/*
 * Copyright 2015 - 2016 James Booth
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.boothj5.minions;

import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Map;

class MinionsMain {
    public static void main(String[] args) {
        if (args.length < 1) {
            throw new MinionsException("No configuration file specified.");
        }

        try {
            Yaml yaml = new Yaml();
            InputStream is = new FileInputStream(new File(args[0]));
            Map<String, Object> yamlConfig = (Map<String, Object>)yaml.load(is);
            MinionsConfiguration conf = new MinionsConfiguration(yamlConfig);
            MinionsRunner runner = new MinionsRunner(conf);
            runner.run();
        } catch (FileNotFoundException e) {
            throw new MinionsException("Could not find configuration file: " + args[0]);
        }
    }
}
