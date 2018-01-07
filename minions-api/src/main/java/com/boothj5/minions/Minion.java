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

import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Minion {
    private static final Logger LOG = LoggerFactory.getLogger(Minion.class);
    protected final MinionsRoom room;

    public abstract String getHelp();

    public Options getOptions() {
        return new Options();
    }

    public Minion(MinionsRoom room) {
        this.room = room;
    }

    final void onCommandWrapper(String from, String message) {
        try {
            onCommand(from, message);

            String[] args = splitCommandArgs(message);
            CommandLineParser parser = new DefaultParser();
            try {
                CommandLine cmd = parser.parse( getOptions(), args);
                onCommand(from, cmd);
            } catch (ParseException e) {
                LOG.error("Error while parsing command message", e);
            }


        } catch (RuntimeException rte) {
            LOG.error("Minions RuntimeException", rte);
        }
    }

    final void onMessageWrapper(String from, String message) {
        try {
            onMessage(from, message);
        } catch (RuntimeException rte) {
            LOG.error("Minions RuntimeException", rte);
        }
    }

    public void onMessage(String from, String message) {}

    /**
     * @deprecated
     */
    @Deprecated
    public void onCommand(String from, String message) {}

    public void onCommand(String from, CommandLine message) {}

    public void onRemove() {}

    /**
     * Splits the message into an string array.
     *
     * @param message
     * @return
     */
    public static String[] splitCommandArgs(String message) {
        return splitCommandArgs(Pattern.compile("([^\"]\\S*|\".+?\")\\s*"), message);
    }

    /**
     * Splits the message into an string array, using the given pattern.
     *
     * @param pattern
     * @param message
     * @return
     */
    public static String[] splitCommandArgs(Pattern pattern, String message) {
        Matcher m = pattern.matcher(message);
        List<String> list = new ArrayList<>();

        while (m.find())
            list.add(m.group(1).replace("\"", ""));

        return list.toArray(new String[0]);
    }
}
