/*
 * Copyright (c) 2019-2029, Dreamlu (596392912@qq.com & www.dreamlu.net).
 * <p>
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE 3.0;
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.gnu.org/licenses/lgpl.html
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.dreamlu.mica.tinylog.writers;

import org.tinylog.core.LogEntry;
import org.tinylog.core.LogEntryValue;
import org.tinylog.writers.Writer;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * console Writer
 *
 * @author L.cm
 */
public class ConsoleWriter implements Writer {
	private final org.tinylog.writers.ConsoleWriter consoleWriter;
	private static volatile boolean enabled = true;

	public ConsoleWriter() {
		this(Collections.emptyMap());
	}

	public ConsoleWriter(final Map<String, String> properties) {
		this.consoleWriter = new org.tinylog.writers.ConsoleWriter(properties);
	}

	@Override
	public Collection<LogEntryValue> getRequiredLogEntryValues() {
		return consoleWriter.getRequiredLogEntryValues();
	}

	@Override
	public void write(LogEntry logEntry) throws Exception {
		// 判断是否开启 控制台日志
		if (enabled) {
			consoleWriter.write(logEntry);
		}
	}

	public static void setEnabled(boolean enabled) {
		ConsoleWriter.enabled = enabled;
	}

	@Override
	public void flush() throws Exception {

	}

	@Override
	public void close() throws Exception {

	}
}
