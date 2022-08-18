/*
 * Copyright (c) Ye Weibin. All rights reserved.
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

package m2tk.util;

import java.util.Arrays;

public final class Bytes
{
    private Bytes() {}

    // AND(array1, array2) => array1 &= array2
    public static void and(byte[] array1, int offset1,
                           byte[] array2, int offset2,
                           int length)
    {
        if (array1 == null || array2 == null)
            throw new NullPointerException();
        if (length < 0 || offset1 < 0 || offset2 < 0)
            throw new IllegalArgumentException();
        if (offset1 + length > array1.length ||
            offset2 + length > array2.length)
            throw new IllegalArgumentException();

        for (int i = 0; i < length; i ++)
        {
            int b1 = array1[i + offset1] & 0xFF;
            int b2 = array2[i + offset2] & 0xFF;
            array1[i + offset1] = (byte) (b1 & b2);
        }
    }

    // IOR(array1, array2) => array1 |= array2
    public static void ior(byte[] array1, int offset1,
                           byte[] array2, int offset2,
                           int length)
    {
        if (array1 == null || array2 == null)
            throw new NullPointerException();
        if (length < 0 || offset1 < 0 || offset2 < 0)
            throw new IllegalArgumentException();
        if (offset1 + length > array1.length ||
            offset2 + length > array2.length)
            throw new IllegalArgumentException();

        for (int i = 0; i < length; i ++)
        {
            int b1 = array1[i + offset1] & 0xFF;
            int b2 = array2[i + offset2] & 0xFF;
            array1[i + offset1] = (byte) (b1 | b2);
        }
    }

    // XOR(array1, array2) => array1 ^= array2
    public static void xor(byte[] array1, int offset1,
                           byte[] array2, int offset2,
                           int length)
    {
        if (array1 == null || array2 == null)
            throw new NullPointerException();
        if (length < 0 || offset1 < 0 || offset2 < 0)
            throw new IllegalArgumentException();
        if (offset1 + length > array1.length ||
            offset2 + length > array2.length)
            throw new IllegalArgumentException();

        for (int i = 0; i < length; i ++)
        {
            int b1 = array1[i + offset1] & 0xFF;
            int b2 = array2[i + offset2] & 0xFF;
            array1[i + offset1] = (byte) (b1 ^ b2);
        }
    }

    /**
     * 在字节数组的指定位置设置目标数据。
     * @param bytes 字节数组。
     * @param offset 目标位置。
     * @param value 目标数据。
     */
    public static void setBytes(byte[] bytes, int offset, byte[] value)
    {
        System.arraycopy(value, 0, bytes, offset, value.length);
    }

    /**
     * 比较两个数组中的指定数据是否相同。
     * @param array1 数组1
     * @param offset1 位置1
     * @param array2 数组2
     * @param offset2 位置2
     * @param length 数据长度
     * @return 比较结果。
     */
    public static boolean equals(byte[] array1, int offset1, byte[] array2, int offset2, int length)
    {
        return equalsWithMask(array1, offset1, array2, offset2, null, 0, length);
    }

    public static boolean equals(byte[] array1, byte[] array2)
    {
        return Arrays.equals(array1, array2);
    }

    /**
     * 比较两个数组中的指定数据是否相同。
     * @param array1 数组1
     * @param offset1 位置1
     * @param array2 数组2
     * @param offset2 位置2
     * @param masks 掩码
     * @param offset3 掩码位置
     * @param length 数据长度
     * @return 比较结果。
     */
    public static boolean equalsWithMask(byte[] array1, int offset1,
                                         byte[] array2, int offset2,
                                         byte[] masks, int offset3,
                                         int length)
    {
        if (array1 == array2 && offset1 == offset2)
            return true;
        if (array1 == null || array2 == null)
            return false;

        if (masks == null)
        {
            for (int i = 0; i < length; i ++)
            {
                int mask = 0xFF;
                int value1 = array1[i + offset1] & mask;
                int value2 = array2[i + offset2] & mask;
                if (value1 != value2)
                    return false;
            }
        } else
        {
            for (int i = 0; i < length; i ++)
            {
                int mask = masks[i + offset3] & 0xFF;
                int value1 = array1[i + offset1] & mask;
                int value2 = array2[i + offset2] & mask;
                if (value1 != value2)
                    return false;
            }
        }

        return true;
    }

    public static boolean equalsWithMask(byte[] array1, byte[] array2, byte[] masks)
    {
        return equalsWithMask(array1, 0, array2, 0, masks, 0, array1.length);
    }

    public static byte[] of(String hex)
    {
        if (hex == null || !hex.matches("^([0-9a-fA-F]{2})+$"))
            return EMPTY_BYTE_ARRAY;

        int length = hex.length() / 2;
        byte[] bytes = new byte[length];
        for (int i = 0; i < length; i ++)
        {
            int highHalf = Character.digit(hex.charAt(i * 2    ), 16);
            int  lowHalf = Character.digit(hex.charAt(i * 2 + 1), 16);
            bytes[i] = (byte) ((highHalf << 4) + lowHalf);
        }

        return bytes;
    }

    private static final byte[] EMPTY_BYTE_ARRAY = new byte[0];
    private static final char[] HEX = "0123456789abcdef".toCharArray();

    public static String toHexString(byte[] bytes)
    {
        char[] chars = new char[bytes.length * 2];
        for (int i = 0; i < bytes.length; i ++)
        {
            int v = bytes[i] & 0xFF;
            chars[i * 2    ] = HEX[v >>> 4];
            chars[i * 2 + 1] = HEX[v & 0xF];
        }
        return new String(chars);
    }

    public static String toHexString(byte[] bytes, int offset, int length)
    {
        char[] chars = new char[length * 2];
        for (int i = 0; i < length; i ++)
        {
            int v = bytes[offset + i] & 0xFF;
            chars[i * 2    ] = HEX[v >>> 4];
            chars[i * 2 + 1] = HEX[v & 0xF];
        }
        return new String(chars);
    }

    public static String toHexStringPrettyPrint(byte[] bytes)
    {
        char[] chars = new char[bytes.length * 3];
        for (int i = 0; i < bytes.length; i ++)
        {
            int v = bytes[i] & 0xFF;
            chars[i * 3    ] = HEX[v >>> 4];
            chars[i * 3 + 1] = HEX[v & 0xF];
            chars[i * 3 + 2] = ' ';
        }
        return new String(chars, 0, chars.length - 1);
    }

    public static String toHexStringPrettyPrint(byte[] bytes, int offset, int length)
    {
        char[] chars = new char[length * 3];
        for (int i = 0; i < length; i ++)
        {
            int v = bytes[offset + i] & 0xFF;
            chars[i * 3    ] = HEX[v >>> 4];
            chars[i * 3 + 1] = HEX[v & 0xF];
            chars[i * 3 + 2] = ' ';
        }
        return new String(chars, 0, chars.length - 1);
    }
}
