/*
 * Copyright (c) 2019-2029, Dreamlu 卢春梦 (596392912@qq.com & www.dreamlu.net).
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

package net.dreamlu.mica.xss.core;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.auto.annotation.AutoIgnore;
import net.dreamlu.mica.core.utils.StringPool;
import net.dreamlu.mica.xss.config.MicaXssProperties;
import net.dreamlu.mica.xss.utils.XssUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;

import java.beans.PropertyEditorSupport;

/**
 * 表单 xss 处理
 *
 * @author L.cm
 */
@AutoIgnore
@ControllerAdvice
@ConditionalOnProperty(
	prefix = MicaXssProperties.PREFIX,
	name = "enabled",
	havingValue = "true",
	matchIfMissing = true
)
@RequiredArgsConstructor
public class FormXssClean {
	private final MicaXssProperties properties;
	private final XssCleaner xssCleaner;

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		// 处理前端传来的表单字符串
		binder.registerCustomEditor(String.class, new StringPropertiesEditor(xssCleaner, properties));
	}

	@Slf4j
	@RequiredArgsConstructor
	public static class StringPropertiesEditor extends PropertyEditorSupport {
		private final XssCleaner xssCleaner;
		private final MicaXssProperties properties;

		@Override
		public String getAsText() {
			Object value = getValue();
			return value != null ? value.toString() : StringPool.EMPTY;
		}

		@Override
		public void setAsText(String text) throws IllegalArgumentException {
			if (text == null) {
				setValue(null);
			} else if (XssHolder.isEnabled()) {
				String value = xssCleaner.clean(XssUtil.trim(text, properties.isTrimText()));
				setValue(value);
				log.debug("Request parameter value:{} cleaned up by mica-xss, current value is:{}.", text, value);
			} else {
				setValue(XssUtil.trim(text, properties.isTrimText()));
			}
		}
	}

}
