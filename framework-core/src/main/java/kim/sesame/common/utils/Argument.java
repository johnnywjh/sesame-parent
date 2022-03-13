/**
 * Project: member.common
 * 
 * File Created at 2009-8-24
 * $Id: Argument.java 38089 2010-03-10 03:23:28Z william.liangf $
 * 
 * Copyright 2008 Alibaba.com Croporation Limited.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * Alibaba Company. ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Alibaba.com.
 */
package kim.sesame.common.utils;

import java.util.Collection;
import java.util.Map;


/**
* Description: 校验参数类
*/
public final class Argument {
    
    private Argument() {}
    
    public static void notNull(Object object, String message, Object... args) {
        if (object == null) {
            throw new IllegalArgumentException(String.format(message, args));
        }
    }
    
    public static void notEmpty(Collection<?> object, String message, Object... args) {
        if (object == null || object.size() == 0) {
            throw new IllegalArgumentException(String.format(message, args));
        }
    }

    public static void notEmpty(Map<?, ?> object, String message, Object... args) {
        if (object == null || object.size() == 0) {
            throw new IllegalArgumentException(String.format(message, args));
        }
    }

    public static void notEmpty(Object[] object, String message, Object... args) {
        if (object == null || object.length == 0) {
            throw new IllegalArgumentException(String.format(message, args));
        }
    }
    
    public static void notEmpty(CharSequence object, String message, Object... args) {
        if (object == null || object.length() == 0) {
            throw new IllegalArgumentException(String.format(message, args));
        }
    }

    public static void hasText(String object, String message, Object... args) {
        if (object == null || object.length() == 0 || object.trim().length() == 0) {
            throw new IllegalArgumentException(String.format(message, args));
        }
    }

    public static void isTrue(boolean object, String message, Object... args) {
        if (! object) {
            throw new IllegalArgumentException(String.format(message, args));
        }
    }

    public static void isFalse(boolean object, String message, Object... args) {
        if (object) {
            throw new IllegalArgumentException(String.format(message, args));
        }
    }
    
    public static void notEmptyElements(Collection<?> object, String message, Object... args) {
        notEmpty(object, message, args);
        for (Object element : object) {
            if (element instanceof CharSequence) {
                notEmpty((CharSequence) element, message, args);
            } else {
                notNull(element, message, args);
            }
        }
    }

    public static void notEmptyElements(Map<?, ?> object, String message, Object... args) {
        notEmpty(object, message, args);
        for (Object element : object.values()) {
            if (element instanceof CharSequence) {
                notEmpty((CharSequence) element, message, args);
            } else {
                notNull(element, message, args);
            }
        }
    }

    public static void notEmptyElements(Object[] object, String message, Object... args) {
        notEmpty(object, message, args);
        for (Object element : object) {
            if (element instanceof CharSequence) {
                notEmpty((CharSequence) element, message, args);
            } else {
                notNull(element, message, args);
            }
        }
    }
    
}
