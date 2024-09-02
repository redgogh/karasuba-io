package com.redgogh.vortextools.http;

/* -------------------------------------------------------------------------------- *\
|*                                                                                  *|
|*    Copyright (C) 2023 RedGogh                                                    *|
|*                                                                                  *|
|*    This program is free software: you can redistribute it and/or modify          *|
|*    it under the terms of the GNU General Public License as published by          *|
|*    the Free Software Foundation, either version 3 of the License, or             *|
|*    (at your option) any later version.                                           *|
|*                                                                                  *|
|*    This program is distributed in the hope that it will be useful,               *|
|*    but WITHOUT ANY WARRANTY; without even the implied warranty of                *|
|*    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the                 *|
|*    GNU General Public License for more details.                                  *|
|*                                                                                  *|
|*    You should have received a copy of the GNU General Public License             *|
|*    along with this program.  If not, see <https://www.gnu.org/licenses/>.        *|
|*                                                                                  *|
|*    This program comes with ABSOLUTELY NO WARRANTY; for details type `show w'.    *|
|*    This is free software, and you are welcome to redistribute it                 *|
|*    under certain conditions; type `show c' for details.                          *|
|*                                                                                  *|
\* -------------------------------------------------------------------------------- */

/* Creates on 2023/6/26. */

import java.util.HashMap;

import static com.redgogh.vortextools.AnyObjects.atos;
import static com.redgogh.vortextools.StringUtils.strwfmt;

/**
 * @author RedGogh   
 */
public class QueryBuilder extends HashMap<String, String> {

    /** 参数拼接 */
    public String argConcat(String url) {
        if (isEmpty())
            return url;

        StringBuilder builder = new StringBuilder();

        for (Entry<String, String> entry : entrySet())
            builder.append(strwfmt("%s=%s&", entry.getKey(), entry.getValue()));
        String finalArguments = atos(builder.toString().toCharArray(), 0, -1); /* 删掉最后一个字符 ‘&’ */

        return strwfmt("%s?%s", url, finalArguments);
    }

}
