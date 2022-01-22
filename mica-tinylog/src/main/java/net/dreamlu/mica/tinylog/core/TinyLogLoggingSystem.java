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

package net.dreamlu.mica.tinylog.core;

import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.logging.*;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.util.ClassUtils;
import org.tinylog.Level;
import org.tinylog.configuration.Configuration;

/**
 * TinyLog LoggingSystem
 *
 * @author L.cm
 */
public class TinyLogLoggingSystem extends Slf4JLoggingSystem {
	private static final LogLevels<Level> LEVELS = new LogLevels<>();

	static {
		LEVELS.map(LogLevel.TRACE, Level.TRACE);
		LEVELS.map(LogLevel.DEBUG, Level.DEBUG);
		LEVELS.map(LogLevel.INFO, Level.INFO);
		LEVELS.map(LogLevel.WARN, Level.WARN);
		LEVELS.map(LogLevel.ERROR, Level.ERROR);
		LEVELS.map(LogLevel.FATAL, Level.ERROR);
		LEVELS.map(LogLevel.OFF, Level.OFF);
	}

	public TinyLogLoggingSystem(ClassLoader classLoader) {
		super(classLoader);
	}

	@Override
	protected String[] getStandardConfigLocations() {
		return new String[]{"tinylog-dev.properties", "tinylog-test.properties", "tinylog.properties"};
	}

	@Override
	protected void loadDefaults(LoggingInitializationContext initializationContext, LogFile logFile) {
		Environment environment = initializationContext.getEnvironment();
		Binder.get(environment)
			.bind("tinylog", Bindable.mapOf(String.class, String.class))
			.ifBound(props -> props.forEach(Configuration::set));
	}

	/**
	 * {@link LoggingSystemFactory} that returns {@link TinyLogLoggingSystem} if possible.
	 */
	@Order
	public static class Factory implements LoggingSystemFactory {

		private static final boolean PRESENT = ClassUtils.isPresent("org.tinylog.Logger",
			TinyLogLoggingSystem.Factory.class.getClassLoader());

		@Override
		public LoggingSystem getLoggingSystem(ClassLoader classLoader) {
			if (PRESENT) {
				return new TinyLogLoggingSystem(classLoader);
			}
			return null;
		}

	}

}
