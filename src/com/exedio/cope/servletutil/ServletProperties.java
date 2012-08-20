/*
 * Copyright (C) 2004-2012  exedio GmbH (www.exedio.com)
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package com.exedio.cope.servletutil;

import java.util.Collection;

import javax.servlet.ServletContext;

import com.exedio.cope.util.PrefixSource;
import com.exedio.cope.util.Properties;

public final class ServletProperties
{
	private ServletProperties()
	{
		// prevent instantiation
	}

	public static final Properties.Source create(final ServletContext context)
	{
		final String contextPath = context.getContextPath();
		final String prefix;
		if(contextPath==null)
			prefix = null;
		else if("".equals(contextPath))
			prefix = "root.";
		else if(contextPath.startsWith("/"))
			prefix = contextPath.substring(1) + '.';
		else
			prefix = contextPath + '.';

		final Properties.Source initParam = PrefixSource.wrap(new Properties.Source(){
					public String get(final String key)
					{
						return context.getInitParameter(key);
					}

					public Collection<String> keySet()
					{
						return null;
					}

					public String getDescription()
					{
						return toString();
					}

					@Override
					public String toString()
					{
						return "ServletContext '" + contextPath + '\'';
					}
				},
				prefix);

		return new Properties.Source(){
			public String get(final String key)
			{
				if("contextPath".equals(key))
					return contextPath;

				return initParam.get(key);
			}

			public Collection<String> keySet()
			{
				return null;
			}

			public String getDescription()
			{
				return initParam.getDescription();
			}

			@Override
			public String toString()
			{
				return initParam.toString();
			}
		};
	}
}
