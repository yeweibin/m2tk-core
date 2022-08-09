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

public final class LittleEndian
{
    private LittleEndian() {}

    /**
     * 单个字节的长度。
     */
    public static final int ONE_BYTE   = 1;
    /**
     * 两个字节的长度。
     */
    public static final int TWO_BYTE   = 2;
    /**
     * 三个字节的长度。
     */
    public static final int THREE_BYTE = 3;
    /**
     * 四个字节的长度。
     */
    public static final int FOUR_BYTE  = 4;
    /**
     * 五个字节的长度。
     */
    public static final int FIVE_BYTE  = 5;
    /**
     * 六个字节的长度。
     */
    public static final int SIX_BYTE   = 6;
    /**
     * 七个字节的长度。
     */
    public static final int SEVEN_BYTE = 7;
    /**
     * 八个字节的长度。
     */
    public static final int EIGHT_BYTE = 8;
    /**
     * 单字节掩码，适用于8位整数。
     */
    public static final int MASK_UINT8  = 0xFF;
    /**
     * 双字节掩码，适用于16位整数。
     */
    public static final int MASK_UINT16 = 0xFFFF;
    /**
     * 三字节掩码，适用于24位整数。
     */
    public static final int MASK_UINT24 = 0xFFFFFF;
    /**
     * 四字节掩码，适用于32位整数。
     */
    public static final long MASK_UINT32 = 0xFFFFFFFFL;
    /**
     * 五字节掩码，适用于40位整数。
     */
    public static final long MASK_UINT40 = 0xFFFFFFFFFFL;
    /**
     * 六字节掩码，适用于48位整数。
     */
    public static final long MASK_UINT48 = 0xFFFFFFFFFFFFL;
    /**
     * 七字节掩码，适用于56位整数。
     */
    public static final long MASK_UINT56 = 0xFFFFFFFFFFFFFFL;

    /**
     * 从字节数组的指定位置读取8位无符号整数。
     * @param bytes 字节数组。
     * @param offset 目标位置。
     * @return 8位无符号整数，范围在 <code>0</code> 到 <code>2^8 - 1</code> 之间。
     */
    public static int getUINT8(byte[] bytes, int offset)
    {
        return bytes[offset] & MASK_UINT8;
    }

    /**
     * 从字节数组的指定位置读取16位无符号整数。
     * @param bytes 字节数组。
     * @param offset 目标位置。
     * @return 16位无符号整数，范围在 <code>0</code> 到 <code>2^16 - 1</code> 之间。
     */
    public static int getUINT16(byte[] bytes, int offset)
    {
        int part1 = (bytes[offset + 0] & 0xFF);
        int part2 = (bytes[offset + 1] & 0xFF) << 8;
        return part2 | part1;
    }

    /**
     * 从字节数组的指定位置读取24位无符号整数。
     * @param bytes 字节数组。
     * @param offset 目标位置。
     * @return 24位无符号整数，范围在 <code>0</code> 到 <code>2^24 - 1</code> 之间。
     */
    public static int getUINT24(byte[] bytes, int offset)
    {
        int part1 = (bytes[offset + 0] & 0xFF);
        int part2 = (bytes[offset + 1] & 0xFF) << 8;
        int part3 = (bytes[offset + 2] & 0xFF) << 16;
        return part3 | part2 | part1;
    }

    /**
     * 从字节数组的指定位置读取32位无符号整数。
     * @param bytes 字节数组。
     * @param offset 目标位置。
     * @return 32位无符号整数，范围在 <code>0</code> 到 <code>2^32 - 1</code> 之间。
     */
    public static long getUINT32(byte[] bytes, int offset)
    {
        long part1 = (bytes[offset + 0] & 0xFF);
        long part2 = (bytes[offset + 1] & 0xFF) << 8;
        long part3 = (bytes[offset + 2] & 0xFF) << 16;
        long part4 = (bytes[offset + 3] & 0xFF) << 24;
        return part4 | part3 | part2 | part1;
    }

    /**
     * 从字节数组的指定位置读取40位无符号整数。
     * @param bytes 字节数组。
     * @param offset 目标位置。
     * @return 40位无符号整数，范围在 <code>0</code> 到 <code>2^40 - 1</code> 之间。
     */
    public static long getUINT40(byte[] bytes, int offset)
    {
        long part1 = (bytes[offset + 0] & 0xFFL);
        long part2 = (bytes[offset + 1] & 0xFFL) << 8;
        long part3 = (bytes[offset + 2] & 0xFFL) << 16;
        long part4 = (bytes[offset + 3] & 0xFFL) << 24;
        long part5 = (bytes[offset + 4] & 0xFFL) << 32;
        return part5 | part4 | part3 | part2 | part1;
    }

    /**
     * 从字节数组的指定位置读取48位无符号整数。
     * @param bytes 字节数组。
     * @param offset 目标位置。
     * @return 48位无符号整数，范围在 <code>0</code> 到 <code>2^48 - 1</code> 之间。
     */
    public static long getUINT48(byte[] bytes, int offset)
    {
        long part1 = (bytes[offset + 0] & 0xFFL);
        long part2 = (bytes[offset + 1] & 0xFFL) << 8;
        long part3 = (bytes[offset + 2] & 0xFFL) << 16;
        long part4 = (bytes[offset + 3] & 0xFFL) << 24;
        long part5 = (bytes[offset + 4] & 0xFFL) << 32;
        long part6 = (bytes[offset + 5] & 0xFFL) << 40;
        return part6 | part5 | part4 | part3 | part2 | part1;
    }

    /**
     * 从字节数组的指定位置读取56位无符号整数。
     * @param bytes 字节数组。
     * @param offset 目标位置。
     * @return 56位无符号整数，范围在 <code>0</code> 到 <code>2^56 - 1</code> 之间。
     */
    public static long getUINT56(byte[] bytes, int offset)
    {
        long part1 = (bytes[offset + 0] & 0xFFL);
        long part2 = (bytes[offset + 1] & 0xFFL) << 8;
        long part3 = (bytes[offset + 2] & 0xFFL) << 16;
        long part4 = (bytes[offset + 3] & 0xFFL) << 24;
        long part5 = (bytes[offset + 4] & 0xFFL) << 32;
        long part6 = (bytes[offset + 5] & 0xFFL) << 40;
        long part7 = (bytes[offset + 6] & 0xFFL) << 48;
        return part7 | part6 | part5 | part4 | part3 | part2 | part1;
    }

    /**
     * 在字节数组的指定位置设置8位无符号整数。
     * @param bytes 字节数组。
     * @param offset 目标位置。
     * @param value 8位无符号整数。
     */
    public static void setUINT8(byte[] bytes, int offset, int value)
    {
        bytes[offset] = (byte) (value & MASK_UINT8);
    }

    /**
     * 在字节数组的指定位置设置16位无符号整数。
     * @param bytes 字节数组。
     * @param offset 目标位置。
     * @param value 16位无符号整数。
     */
    public static void setUINT16(byte[] bytes, int offset, int value)
    {
        bytes[offset + 0] = (byte) ((value     ) & 0xFF);
        bytes[offset + 1] = (byte) ((value >> 8) & 0xFF);
    }

    /**
     * 在字节数组的指定位置设置24位无符号整数。
     * @param bytes 字节数组。
     * @param offset 目标位置。
     * @param value 24位无符号整数。
     */
    public static void setUINT24(byte[] bytes, int offset, int value)
    {
        bytes[offset + 0] = (byte) ((value      ) & 0xFF);
        bytes[offset + 1] = (byte) ((value >>  8) & 0xFF);
        bytes[offset + 2] = (byte) ((value >> 16) & 0xFF);
    }

    /**
     * 在字节数组的指定位置设置32位无符号整数。
     * @param bytes 字节数组。
     * @param offset 目标位置。
     * @param value 32位无符号整数。
     */
    public static void setUINT32(byte[] bytes, int offset, long value)
    {
        bytes[offset + 0] = (byte) ((value      ) & 0xFF);
        bytes[offset + 1] = (byte) ((value >>  8) & 0xFF);
        bytes[offset + 2] = (byte) ((value >> 16) & 0xFF);
        bytes[offset + 3] = (byte) ((value >> 24) & 0xFF);
    }

    /**
     * 在字节数组的指定位置设置40位无符号整数。
     * @param bytes 字节数组。
     * @param offset 目标位置。
     * @param value 40位无符号整数。
     */
    public static void setUINT40(byte[] bytes, int offset, long value)
    {
        bytes[offset + 0] = (byte) ((value      ) & 0xFF);
        bytes[offset + 1] = (byte) ((value >>  8) & 0xFF);
        bytes[offset + 2] = (byte) ((value >> 16) & 0xFF);
        bytes[offset + 3] = (byte) ((value >> 24) & 0xFF);
        bytes[offset + 4] = (byte) ((value >> 32) & 0xFF);
    }

    /**
     * 在字节数组的指定位置设置48位无符号整数。
     * @param bytes 字节数组。
     * @param offset 目标位置。
     * @param value 48位无符号整数。
     */
    public static void setUINT48(byte[] bytes, int offset, long value)
    {
        bytes[offset + 0] = (byte) ((value      ) & 0xFF);
        bytes[offset + 1] = (byte) ((value >>  8) & 0xFF);
        bytes[offset + 2] = (byte) ((value >> 16) & 0xFF);
        bytes[offset + 3] = (byte) ((value >> 24) & 0xFF);
        bytes[offset + 4] = (byte) ((value >> 32) & 0xFF);
        bytes[offset + 5] = (byte) ((value >> 40) & 0xFF);
    }

    /**
     * 在字节数组的指定位置设置56位无符号整数。
     * @param bytes 字节数组。
     * @param offset 目标位置。
     * @param value 56位无符号整数。
     */
    public static void setUINT56(byte[] bytes, int offset, long value)
    {
        bytes[offset + 0] = (byte) ((value      ) & 0xFF);
        bytes[offset + 1] = (byte) ((value >>  8) & 0xFF);
        bytes[offset + 2] = (byte) ((value >> 16) & 0xFF);
        bytes[offset + 3] = (byte) ((value >> 24) & 0xFF);
        bytes[offset + 4] = (byte) ((value >> 32) & 0xFF);
        bytes[offset + 5] = (byte) ((value >> 40) & 0xFF);
        bytes[offset + 6] = (byte) ((value >> 48) & 0xFF);
    }
}
