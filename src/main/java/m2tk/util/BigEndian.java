/*
 * Copyright (c) M2TK Project. All rights reserved.
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

@SuppressWarnings("all")
public final class BigEndian
{
    private BigEndian() {}

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
        int byte1 = bytes[offset + 0] & MASK_UINT8;
        int byte2 = bytes[offset + 1] & MASK_UINT8;
        return (byte1 << 8) | byte2;
    }

    /**
     * 从字节数组的指定位置读取24位无符号整数。
     * @param bytes 字节数组。
     * @param offset 目标位置。
     * @return 24位无符号整数，范围在 <code>0</code> 到 <code>2^24 - 1</code> 之间。
     */
    public static int getUINT24(byte[] bytes, int offset)
    {
        int byte1 = bytes[offset + 0] & MASK_UINT8;
        int byte2 = bytes[offset + 1] & MASK_UINT8;
        int byte3 = bytes[offset + 2] & MASK_UINT8;
        return (byte1 << 16) | (byte2 << 8) | byte3;
    }

    /**
     * 从字节数组的指定位置读取32位无符号整数。
     * @param bytes 字节数组。
     * @param offset 目标位置。
     * @return 32位无符号整数，范围在 <code>0</code> 到 <code>2^32 - 1</code> 之间。
     */
    public static long getUINT32(byte[] bytes, int offset)
    {
        long byte1 = bytes[offset + 0] & MASK_UINT8;
        long byte2 = bytes[offset + 1] & MASK_UINT8;
        long byte3 = bytes[offset + 2] & MASK_UINT8;
        long byte4 = bytes[offset + 3] & MASK_UINT8;
        return (byte1 << 24) | (byte2 << 16) | (byte3 << 8) | byte4;
    }

    /**
     * 从字节数组的指定位置读取40位无符号整数。
     * @param bytes 字节数组。
     * @param offset 目标位置。
     * @return 40位无符号整数，范围在 <code>0</code> 到 <code>2^40 - 1</code> 之间。
     */
    public static long getUINT40(byte[] bytes, int offset)
    {
        long byte1 = bytes[offset + 0] & MASK_UINT8;
        long byte2 = bytes[offset + 1] & MASK_UINT8;
        long byte3 = bytes[offset + 2] & MASK_UINT8;
        long byte4 = bytes[offset + 3] & MASK_UINT8;
        long byte5 = bytes[offset + 4] & MASK_UINT8;
        return (byte1 << 32) | (byte2 << 24) | (byte3 << 16) | (byte4 << 8) | byte5;
    }

    /**
     * 从字节数组的指定位置读取48位无符号整数。
     * @param bytes 字节数组。
     * @param offset 目标位置。
     * @return 48位无符号整数，范围在 <code>0</code> 到 <code>2^48 - 1</code> 之间。
     */
    public static long getUINT48(byte[] bytes, int offset)
    {
        long byte1 = bytes[offset + 0] & MASK_UINT8;
        long byte2 = bytes[offset + 1] & MASK_UINT8;
        long byte3 = bytes[offset + 2] & MASK_UINT8;
        long byte4 = bytes[offset + 3] & MASK_UINT8;
        long byte5 = bytes[offset + 4] & MASK_UINT8;
        long byte6 = bytes[offset + 5] & MASK_UINT8;
        return (byte1 << 40) | (byte2 << 32) | (byte3 << 24) | (byte4 << 16) |
               (byte5 << 8) | byte6;
    }

    /**
     * 从字节数组的指定位置读取56位无符号整数。
     * @param bytes 字节数组。
     * @param offset 目标位置。
     * @return 56位无符号整数，范围在 <code>0</code> 到 <code>2^56 - 1</code> 之间。
     */
    public static long getUINT56(byte[] bytes, int offset)
    {
        long byte1 = bytes[offset + 0] & MASK_UINT8;
        long byte2 = bytes[offset + 1] & MASK_UINT8;
        long byte3 = bytes[offset + 2] & MASK_UINT8;
        long byte4 = bytes[offset + 3] & MASK_UINT8;
        long byte5 = bytes[offset + 4] & MASK_UINT8;
        long byte6 = bytes[offset + 5] & MASK_UINT8;
        long byte7 = bytes[offset + 6] & MASK_UINT8;
        return (byte1 << 48) | (byte2 << 40) | (byte3 << 32) | (byte4 << 24) |
               (byte5 << 16) | (byte6 << 8) | byte7;
    }

    /**
     * 从字节数组的指定位置读取64位无符号整数。
     * @param bytes 字节数组。
     * @param offset 目标位置。
     * @return 64位无符号整数，范围在 <code>0</code> 到 <code>2^64 - 1</code> 之间。
     *         （由于Java语言的限制，实际输出的有可能是负值，这里只考虑值的二进制表示效果）
     */
    public static long getUINT64(byte[] bytes, int offset)
    {
        long byte1 = bytes[offset + 0] & 0xFF;
        long byte2 = bytes[offset + 1] & 0xFF;
        long byte3 = bytes[offset + 2] & 0xFF;
        long byte4 = bytes[offset + 3] & 0xFF;
        long byte5 = bytes[offset + 4] & 0xFF;
        long byte6 = bytes[offset + 5] & 0xFF;
        long byte7 = bytes[offset + 6] & 0xFF;
        long byte8 = bytes[offset + 7] & 0xFF;
        return (byte1 << 56) | (byte2 << 48) | (byte3 << 40) | (byte4 << 32) |
               (byte5 << 24) | (byte6 << 16) | (byte7 << 8) | byte8;
    }

    /**
     * 在字节数组的指定位置设置8位无符号整数。
     * @param bytes 字节数组。
     * @param offset 目标位置。
     * @param value 8位无符号整数。
     */
    public static void setUINT8(byte[] bytes, int offset, int value)
    {
        bytes[offset] = (byte) (value & 0xFF);
    }

    /**
     * 在字节数组的指定位置设置16位无符号整数。
     * @param bytes 字节数组。
     * @param offset 目标位置。
     * @param value 16位无符号整数。
     */
    public static void setUINT16(byte[] bytes, int offset, int value)
    {
        bytes[offset + 0] = (byte) ((value >>> 8) & 0xFF);
        bytes[offset + 1] = (byte) ((value      ) & 0xFF);
    }

    /**
     * 在字节数组的指定位置设置24位无符号整数。
     * @param bytes 字节数组。
     * @param offset 目标位置。
     * @param value 24位无符号整数。
     */
    public static void setUINT24(byte[] bytes, int offset, int value)
    {
        bytes[offset + 0] = (byte) ((value >>> 16) & 0xFF);
        bytes[offset + 1] = (byte) ((value >>>  8) & 0xFF);
        bytes[offset + 2] = (byte) ((value       ) & 0xFF);
    }

    /**
     * 在字节数组的指定位置设置32位无符号整数。
     * @param bytes 字节数组。
     * @param offset 目标位置。
     * @param value 32位无符号整数。
     */
    public static void setUINT32(byte[] bytes, int offset, long value)
    {
        bytes[offset + 0] = (byte) ((value >>> 24) & 0xFF);
        bytes[offset + 1] = (byte) ((value >>> 16) & 0xFF);
        bytes[offset + 2] = (byte) ((value >>>  8) & 0xFF);
        bytes[offset + 3] = (byte) ((value       ) & 0xFF);
    }

    /**
     * 在字节数组的指定位置设置40位无符号整数。
     * @param bytes 字节数组。
     * @param offset 目标位置。
     * @param value 40位无符号整数。
     */
    public static void setUINT40(byte[] bytes, int offset, long value)
    {
        bytes[offset + 0] = (byte) ((value >>> 32) & 0xFF);
        bytes[offset + 1] = (byte) ((value >>> 24) & 0xFF);
        bytes[offset + 2] = (byte) ((value >>> 16) & 0xFF);
        bytes[offset + 3] = (byte) ((value >>>  8) & 0xFF);
        bytes[offset + 4] = (byte) ((value       ) & 0xFF);
    }

    /**
     * 在字节数组的指定位置设置48位无符号整数。
     * @param bytes 字节数组。
     * @param offset 目标位置。
     * @param value 48位无符号整数。
     */
    public static void setUINT48(byte[] bytes, int offset, long value)
    {
        bytes[offset + 0] = (byte) ((value >>> 40) & 0xFF);
        bytes[offset + 1] = (byte) ((value >>> 32) & 0xFF);
        bytes[offset + 2] = (byte) ((value >>> 24) & 0xFF);
        bytes[offset + 3] = (byte) ((value >>> 16) & 0xFF);
        bytes[offset + 4] = (byte) ((value >>>  8) & 0xFF);
        bytes[offset + 5] = (byte) ((value       ) & 0xFF);
    }

    /**
     * 在字节数组的指定位置设置56位无符号整数。
     * @param bytes 字节数组。
     * @param offset 目标位置。
     * @param value 56位无符号整数。
     */
    public static void setUINT56(byte[] bytes, int offset, long value)
    {
        bytes[offset + 0] = (byte) ((value >>> 48) & 0xFF);
        bytes[offset + 1] = (byte) ((value >>> 40) & 0xFF);
        bytes[offset + 2] = (byte) ((value >>> 32) & 0xFF);
        bytes[offset + 3] = (byte) ((value >>> 24) & 0xFF);
        bytes[offset + 4] = (byte) ((value >>> 16) & 0xFF);
        bytes[offset + 5] = (byte) ((value >>>  8) & 0xFF);
        bytes[offset + 6] = (byte) ((value       ) & 0xFF);
    }

    /**
     * 在字节数组的指定位置设置64位无符号整数。
     * @param bytes 字节数组。
     * @param offset 目标位置。
     * @param value 64位无符号整数（当然，Java无法表示64位无符号整数）。
     */
    public static void setUINT64(byte[] bytes, int offset, long value)
    {
        bytes[offset + 0] = (byte) ((value >>> 56) & 0xFF);
        bytes[offset + 1] = (byte) ((value >>> 48) & 0xFF);
        bytes[offset + 2] = (byte) ((value >>> 40) & 0xFF);
        bytes[offset + 3] = (byte) ((value >>> 32) & 0xFF);
        bytes[offset + 4] = (byte) ((value >>> 24) & 0xFF);
        bytes[offset + 5] = (byte) ((value >>> 16) & 0xFF);
        bytes[offset + 6] = (byte) ((value >>>  8) & 0xFF);
        bytes[offset + 7] = (byte) ((value       ) & 0xFF);
    }

    // 注意：掩码采用的是最高位对齐，因此末尾的纯零字节将被忽略。
    // 纯零首字节将被忽略，例如：mask = 0x00FF，等同于 0xFF；
    // 纯零末字节将被忽略，例如：mask = 0xFF00，等同于 0xFF。
    public static long getBits(byte[] bytes, int offset, long mask)
    {
        if (mask == -1L) // mask: 0xFFFFFFFFFFFFFFFFL
        {
            long part1 = (bytes[offset + 0] & 0xFFL) << 56;
            long part2 = (bytes[offset + 1] & 0xFFL) << 48;
            long part3 = (bytes[offset + 2] & 0xFFL) << 40;
            long part4 = (bytes[offset + 3] & 0xFFL) << 32;
            long part5 = (bytes[offset + 4] & 0xFFL) << 24;
            long part6 = (bytes[offset + 5] & 0xFFL) << 16;
            long part7 = (bytes[offset + 6] & 0xFFL) << 8;
            long part8 = (bytes[offset + 7] & 0xFFL);
            return part1 | part2 | part3 | part4 | part5 | part6 | part7 | part8;
        }

        // 改进的算法采用直接掩码（AND操作），之后移位舍零。
        // 应该比之前的逆序压栈再弹栈再移位的处理效率高那么一点点。

        //            |<-------- mask size -------->|
        // data:      |xxxx|xxxx|xxxx|xxxx|xxxx|xxxx|
        // mask:  (LZ)|0010|1011|0011|1000|1101|1000|(TZ)
        // AND(&) ---------------------------------------
        // masked:    |..x.|x.xx|..xx|x...|xx.x|x...|
        // TRAIL  ---------------------------------------
        // result:         |.x.x|.xx.|.xxx|...x|x.xx|(END)
        // LZ: leading zeros
        // TZ: trailing zeros
        int size = 8 - Long.numberOfLeadingZeros(mask) / 8; // 必须先做除法，利用整除约分，否则结果不正确（差一字节）。
        int tail = Long.numberOfTrailingZeros(mask);

        return AND(bytes, offset, mask, size) >>> tail;
    }

    public static long AND(byte[] bytes, int offset, long value, int size)
    {
        switch (size)
        {
            case 1:
                return value & getUINT8(bytes, offset);
            case 2:
                return value & getUINT16(bytes, offset);
            case 3:
                return value & getUINT24(bytes, offset);
            case 4:
                return value & getUINT32(bytes, offset);
            case 5:
                return value & getUINT40(bytes, offset);
            case 6:
                return value & getUINT48(bytes, offset);
            case 7:
                return value & getUINT56(bytes, offset);
            case 8:
                return value & getUINT64(bytes, offset);
            default:
                throw new IllegalArgumentException("invalid value size: " + size);
        }
    }

    public static long IOR(byte[] bytes, int offset, long value, int size)
    {
        switch (size)
        {
            case 1:
                return value | getUINT8(bytes, offset);
            case 2:
                return value | getUINT16(bytes, offset);
            case 3:
                return value | getUINT24(bytes, offset);
            case 4:
                return value | getUINT32(bytes, offset);
            case 5:
                return value | getUINT40(bytes, offset);
            case 6:
                return value | getUINT48(bytes, offset);
            case 7:
                return value | getUINT56(bytes, offset);
            case 8:
                return value | getUINT64(bytes, offset);
            default:
                throw new IllegalArgumentException("invalid value size: " + size);
        }
    }

    public static long XOR(byte[] bytes, int offset, long value, int size)
    {
        switch (size)
        {
            case 1:
                return value ^ getUINT8(bytes, offset);
            case 2:
                return value ^ getUINT16(bytes, offset);
            case 3:
                return value ^ getUINT24(bytes, offset);
            case 4:
                return value ^ getUINT32(bytes, offset);
            case 5:
                return value ^ getUINT40(bytes, offset);
            case 6:
                return value ^ getUINT48(bytes, offset);
            case 7:
                return value ^ getUINT56(bytes, offset);
            case 8:
                return value ^ getUINT64(bytes, offset);
            default:
                throw new IllegalArgumentException("invalid value size: " + size);
        }
    }
}
